package com.tradigo.tradigo_backend.service;

import com.tradigo.tradigo_backend.Utill.DistanceUtil;
import com.tradigo.tradigo_backend.dto.ReferralRequest;
import com.tradigo.tradigo_backend.model.Hospital;
import com.tradigo.tradigo_backend.model.Referral;
import com.tradigo.tradigo_backend.repository.HospitalRepository;
import com.tradigo.tradigo_backend.repository.ReferralRepository;
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

        if (hospitals.isEmpty()) {
            throw new RuntimeException("No hospitals available");
        }

        // 1️⃣ Filter hospitals
        List<Hospital> filtered = hospitals.stream()
                .filter(h -> !request.isNeedsICU() || h.isIcuAvailable())
                .filter(h -> !request.isNeedsVentilator() || h.getVentilatorCount() > 0)
                .toList();

        if (filtered.isEmpty()) {
            filtered = hospitals; // fallback if nothing matches
        }

        // 2️⃣ Sort by nearest
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

        Hospital bestHospital = nearest.get(0); // safe fallback

        // 3️⃣ Build prompt
        String prompt = buildPrompt(request, nearest);

        String llmResponse;

        try {
            llmResponse = ollamaService.analyze(prompt);
        } catch (Exception e) {
            llmResponse = "AI service temporarily unavailable. Selected nearest hospital.";
        }

        // 4️⃣ Save referral
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

    private String buildPrompt(ReferralRequest request, List<Hospital> hospitals) {

        return """
You are a medical emergency referral AI.

Analyze patient vitals and hospital capabilities.

Patient:
Disease: %s
Blood Pressure: %s
Oxygen Level: %s
Heart Rate: %s
Needs ICU: %s
Needs Ventilator: %s

Available Hospitals:
%s

Choose the BEST hospital and explain briefly why.
""".formatted(
                request.getDisease(),
                request.getBloodPressure(),
                request.getOxygenLevel(),
                request.getHeartRate(),
                request.isNeedsICU(),
                request.isNeedsVentilator(),
                hospitals
        );
    }
}