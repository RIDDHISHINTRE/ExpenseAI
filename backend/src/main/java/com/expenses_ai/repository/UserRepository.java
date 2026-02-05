package com.expenses_ai.repository;

import java.util.Optional;
import com.expenses_ai.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User , String>{

      Optional<User> findByEmail(String email);

      boolean existsByEmail(String email);
}