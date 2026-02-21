package com.tradigo.tradigo_backend.repository;

import com.tradigo.tradigo_backend.model.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HospitalRepository extends MongoRepository<Hospital, String> {

    Optional<Hospital> findByHospitalId(String hospitalId);
}