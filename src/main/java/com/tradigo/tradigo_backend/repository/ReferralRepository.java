package com.tradigo.tradigo_backend.repository;

import com.tradigo.tradigo_backend.model.Referral;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReferralRepository extends MongoRepository<Referral, String> {
}