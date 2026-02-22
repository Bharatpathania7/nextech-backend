package com.tradigo.tradigo_backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "referrals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Referral {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(String oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getRequiredTreatment() {
        return requiredTreatment;
    }

    public void setRequiredTreatment(String requiredTreatment) {
        this.requiredTreatment = requiredTreatment;
    }

    public boolean isNeedsICU() {
        return needsICU;
    }

    public void setNeedsICU(boolean needsICU) {
        this.needsICU = needsICU;
    }

    public boolean isNeedsVentilator() {
        return needsVentilator;
    }

    public void setNeedsVentilator(boolean needsVentilator) {
        this.needsVentilator = needsVentilator;
    }

    public boolean isNeedsRareMachine() {
        return needsRareMachine;
    }

    public void setNeedsRareMachine(boolean needsRareMachine) {
        this.needsRareMachine = needsRareMachine;
    }

    public double getPatientLatitude() {
        return patientLatitude;
    }

    public void setPatientLatitude(double patientLatitude) {
        this.patientLatitude = patientLatitude;
    }

    public double getPatientLongitude() {
        return patientLongitude;
    }

    public void setPatientLongitude(double patientLongitude) {
        this.patientLongitude = patientLongitude;
    }

    public String getRecommendedHospitalId() {
        return recommendedHospitalId;
    }

    public void setRecommendedHospitalId(String recommendedHospitalId) {
        this.recommendedHospitalId = recommendedHospitalId;
    }

    public String getLlmReason() {
        return llmReason;
    }

    public void setLlmReason(String llmReason) {
        this.llmReason = llmReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Id
    private String id;

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

    private String recommendedHospitalId;
    private String llmReason;

    private LocalDateTime createdAt;
}