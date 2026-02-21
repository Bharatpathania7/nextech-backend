package com.tradigo.tradigo_backend.repository;

import com.tradigo.tradigo_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository  extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByHospitalid(String hospitalid);
}
