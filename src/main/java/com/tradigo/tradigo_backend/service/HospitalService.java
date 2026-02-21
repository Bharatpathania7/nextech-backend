package com.tradigo.tradigo_backend.service;

import com.tradigo.tradigo_backend.model.Hospital;
import com.tradigo.tradigo_backend.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    // 🔹 Get hospital of logged-in admin
    public Hospital getMyHospital() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String hospitalId = auth.getName();

        return hospitalRepository.findByHospitalId(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found"));
    }

    // 🔹 Update resources of logged-in hospital
    public Hospital updateMyHospital(Hospital updatedData) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String hospitalId = auth.getName();

        Hospital hospital = hospitalRepository.findByHospitalId(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital not found"));

        hospital.setBedsAvailable(updatedData.getBedsAvailable());
        hospital.setVentilatorCount(updatedData.getVentilatorCount());
        hospital.setIcuAvailable(updatedData.isIcuAvailable());
        hospital.setOxygenCylinderAvailable(updatedData.isOxygenCylinderAvailable());
        hospital.setBloodBankAvailable(updatedData.isBloodBankAvailable());
        hospital.setAmbulanceAvailable(updatedData.isAmbulanceAvailable());

        return hospitalRepository.save(hospital);
    }
    public Hospital createHospital(Hospital hospital) {

        if (hospitalRepository.findByHospitalId(hospital.getHospitalId()).isPresent()) {
            throw new RuntimeException("Hospital already exists");
        }

        return hospitalRepository.save(hospital);
    }
    public String seedHospitals() {

        hospitalRepository.deleteAll(); // Optional reset

        for (int i = 1; i <= 70; i++) {

            String id = String.format("DH_%03d", i);

            Hospital hospital = Hospital.builder()
                    .hospitalId(id)
                    .name("District Hospital " + i)
                    .latitude(30.3398 + (Math.random() * 1))
                    .longitude(76.3869 + (Math.random() * 1))
                    .bedsAvailable((int) (5 + Math.random() * 30))
                    .icuAvailable(Math.random() > 0.5)
                    .ventilatorCount((int) (Math.random() * 6))
                    .oxygenCylinderAvailable(true)
                    .bloodBankAvailable(Math.random() > 0.4)
                    .ambulanceAvailable(Math.random() > 0.3)
                    .build();

            hospitalRepository.save(hospital);
        }

        return "50 hospitals seeded successfully";
    }
}