package com.tradigo.tradigo_backend.service;

import com.tradigo.tradigo_backend.Utill.DistanceUtil;
import com.tradigo.tradigo_backend.dto.ReferralRequest;
import com.tradigo.tradigo_backend.model.Hospital;
import com.tradigo.tradigo_backend.model.Referral;
import com.tradigo.tradigo_backend.repository.HospitalRepository;
import com.tradigo.tradigo_backend.repository.ReferralRepository;
//import com.tradigo.tradigo_backend.util.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferralService {

    private final HospitalRepository hospitalRepository;
    private final ReferralRepository referralRepository;
    private final OllamaService ollamaService;
    private final DistanceUtil distanceUtil;

    public Referral analyzeReferral(ReferralRequest request) {

        List<Hospital> hospitals = hospitalRepository.findAll();

        // 1️⃣ Filter based on needs
        List<Hospital> filtered = hospitals.stream()
                .filter(h -> !request.isNeedsICU() || h.isIcuAvailable())
                .filter(h -> !request.isNeedsVentilator() || h.getVentilatorCount() > 0)
                .toList();

        // 2️⃣ Sort by nearest distance

        List<Hospital> nearest = filtered.stream()
                .sorted(Comparator.comparingDouble(h ->
                        distanceUtil.calculateDistance(
                                request.getPatientLatitude(),
                                request.getPatientLongitude(),
                                h.getLatitude(),
                                h.getLongitude()
                        )))
                .limit(5)
                .toList();

        // 3️⃣ Build LLM prompt
        String prompt = buildPrompt(request, nearest);

        String llmResponse = ollamaService.analyze(prompt);

        Hospital bestHospital = nearest.get(0); // fallback

        Referral referral = Referral.builder()
                .patientName(request.getPatientName())
                .disease(request.getDisease())
                .bloodPressure(request.getBloodPressure())
                .oxygenLevel(request.getOxygenLevel())
                .heartRate(request.getHeartRate())
                .requiredTreatment(request.getRequiredTreatment())
                .needsICU(request.isNeedsICU())
                .needsVentilator(request.isNeedsVentilator())
                .needsRareMachine(request.isNeedsRareMachine())
                .patientLatitude(request.getPatientLatitude())
                .patientLongitude(request.getPatientLongitude())
                .recommendedHospitalId(bestHospital.getHospitalId())
                .llmReason(llmResponse)
                .createdAt(LocalDateTime.now())
                .build();

        return referralRepository.save(referral);
    }

    private String buildPrompt(ReferralRequest request,
                               List<Hospital> hospitals) {

        return """
                You are a medical referral AI.

                Patient:
                Disease: %s
                Blood Pressure: %s
                Oxygen Level: %s
                Heart Rate: %s
                Needs ICU: %s
                Needs Ventilator: %s

                Hospitals Available:
                %s

                Select the best hospital and explain why.
                """
                .formatted(
                        request.getDisease(),
                        request.getBloodPressure(),
                        request.getOxygenLevel(),
                        request.getHeartRate(),
                        request.isNeedsICU(),
                        request.isNeedsVentilator(),
                        hospitals.toString()
                );
    }
}