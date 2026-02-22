package com.tradigo.tradigo_backend.dto;

import lombok.Data;

@Data
public class ReferralRequest {

    private String patientName;
    private String disease;
    private String bloodPressure;
    private String oxygenLevel;
    private String heartRate;

    private String requiredTreatment;

    private boolean needsICU;
    private boolean needsVentilator;
    private boolean needsRareMachine;

    private double patientLatitude;
    private double patientLongitude;
}