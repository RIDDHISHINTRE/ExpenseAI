package com.expenses_ai.repository;

import com.expenses_ai.model.MonthlyBudget;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface MonthlyBudgetRepository extends MongoRepository<MonthlyBudget , String>{
    
    Optional<MonthlyBudget> findByUserIdAndMonthAndYear(String userId, int month, int year); 
}
