package com.expenses_ai.service;

import com.expenses_ai.model.MonthlyBudget;
import com.expenses_ai.repository.MonthlyBudgetRepository;
import com.expenses_ai.exception.BadRequestException;
import com.expenses_ai.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MonthlyBudgetService {

    private final MonthlyBudgetRepository monthlyBudgetRepository;

    public MonthlyBudgetService(MonthlyBudgetRepository monthlyBudgetRepository) {
        this.monthlyBudgetRepository = monthlyBudgetRepository;
    }

    
    public MonthlyBudget setBudget(String userId, int month, int year, Double amount) {
        if (amount == null || amount <= 0) {
            throw new BadRequestException("Budget amount must be greater than 0");
        }

       
        Optional<MonthlyBudget> existing = monthlyBudgetRepository.findByUserIdAndMonthAndYear(userId, month, year);
        if (existing.isPresent()) {
            throw new BadRequestException("Budget for this month is already set");
        }

        MonthlyBudget budget = new MonthlyBudget();
        budget.setUserId(userId);
        budget.setMonth(month);
        budget.setYear(year);
        budget.setBudgetAmount(amount);

        return monthlyBudgetRepository.save(budget);
    }

    
    public MonthlyBudget editBudget(String userId, Double newAmount) {
        if (newAmount == null || newAmount <= 0) {
            throw new BadRequestException("Budget amount must be greater than 0");
        }

        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        MonthlyBudget budget = monthlyBudgetRepository
                .findByUserIdAndMonthAndYear(userId, currentMonth, currentYear)
                .orElseThrow(() -> new ResourceNotFoundException("Budget for current month not found"));

        budget.setBudgetAmount(newAmount);
        return monthlyBudgetRepository.save(budget);
    }

    
    public Optional<MonthlyBudget> getBudget(String userId, int month, int year) {
        return monthlyBudgetRepository.findByUserIdAndMonthAndYear(userId, month, year);
    }

   
    public boolean isBudgetSet(String userId) {
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        return monthlyBudgetRepository
                .findByUserIdAndMonthAndYear(userId, currentMonth, currentYear)
                .isPresent();
    }
}

