package com.tradigo.tradigo_backend.controller;

import com.tradigo.tradigo_backend.model.Hospital;
import com.tradigo.tradigo_backend.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospital")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HospitalController {

    private final HospitalService hospitalService;

    // 🔹 Get logged-in hospital details
    @GetMapping("/me")
    public ResponseEntity<Hospital> getMyHospital() {
        return ResponseEntity.ok(hospitalService.getMyHospital());
    }

    // 🔹 Update hospital resources
    @PutMapping("/me")
    public ResponseEntity<Hospital> updateMyHospital(@RequestBody Hospital updatedData) {
        return ResponseEntity.ok(hospitalService.updateMyHospital(updatedData));
    }
    @PostMapping("/create")
    public ResponseEntity<Hospital> createHospital(@RequestBody Hospital hospital) {
        return ResponseEntity.ok(hospitalService.createHospital(hospital));
    }
    @PostMapping("/seed")
    public String seedHospitals() {
        return hospitalService.seedHospitals();
    }
}