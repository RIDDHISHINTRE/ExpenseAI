package com.expenses_ai.controller;

import com.expenses_ai.dto.MonthlyBudgetRequest;
import com.expenses_ai.model.MonthlyBudget;
import com.expenses_ai.model.User;
import com.expenses_ai.repository.UserRepository;
import com.expenses_ai.service.MonthlyBudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/monthly-budget")
public class MonthlyBudgetController {

    private final MonthlyBudgetService monthlyBudgetService;
    private final UserRepository userRepository;

    public MonthlyBudgetController(MonthlyBudgetService monthlyBudgetService, UserRepository userRepository) {
        this.monthlyBudgetService = monthlyBudgetService;
        this.userRepository = userRepository;
    }

    private String getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    @PostMapping("/set")
    public ResponseEntity<MonthlyBudget> setBudget(@Valid @RequestBody MonthlyBudgetRequest request) {
        String userId = getLoggedInUserId();
        MonthlyBudget budget = monthlyBudgetService.setBudget(
                userId,
                request.getMonth(),
                request.getYear(),
                request.getBudgetAmount()
        );
        return ResponseEntity.ok(budget);
    }

  
    @PutMapping("/edit")
    public ResponseEntity<MonthlyBudget> editBudget(@Valid @RequestBody MonthlyBudgetRequest request) {
        String userId = getLoggedInUserId();
        MonthlyBudget budget = monthlyBudgetService.editBudget(userId, request.getBudgetAmount());
        return ResponseEntity.ok(budget);
    }

    @GetMapping("/current")
    public ResponseEntity<MonthlyBudget> getCurrentMonthBudget() {
        String userId = getLoggedInUserId();
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        MonthlyBudget budget = monthlyBudgetService.getBudget(userId, month, year)
                .orElse(null);

        return ResponseEntity.ok(budget);
    }
}
