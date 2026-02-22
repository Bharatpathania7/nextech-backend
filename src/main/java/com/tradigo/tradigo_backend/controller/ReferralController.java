package com.tradigo.tradigo_backend.controller;

import com.tradigo.tradigo_backend.dto.ReferralRequest;
import com.tradigo.tradigo_backend.model.Referral;
import com.tradigo.tradigo_backend.service.ReferralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/referral")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReferralController {

    private final ReferralService referralService;

    @PostMapping("/analyze")
    public ResponseEntity<Referral> analyze(
            @RequestBody ReferralRequest request) {

        return ResponseEntity.ok(
                referralService.analyzeReferral(request)
        );
    }
}