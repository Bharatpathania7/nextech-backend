package com.tradigo.tradigo_backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hospitals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital {

    @Id
    private String id;

    private String hospitalId;   // Must match admin hospitalId
    private String name;

    private double latitude;
    private double longitude;

    private int bedsAvailable;
    private boolean icuAvailable;
    private int ventilatorCount;

    private boolean oxygenCylinderAvailable;
    private boolean bloodBankAvailable;
    private boolean ambulanceAvailable;
}