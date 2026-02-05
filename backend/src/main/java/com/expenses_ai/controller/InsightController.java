package com.expenses_ai.controller;

import com.expenses_ai.dto.*;
import com.expenses_ai.model.User;
import com.expenses_ai.service.ExpenseService;
import com.expenses_ai.service.InsightService;
import org.springframework.http.ResponseEntity;
import com.expenses_ai.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/insights")
public class InsightController {

    private final ExpenseService expenseService;
    private final InsightService insightService;
    private final UserRepository userRepository;

    public InsightController(
            ExpenseService expenseService,
            InsightService insightService,
            UserRepository userRepository
    ) {
        this.expenseService = expenseService;
        this.insightService = insightService;
        this.userRepository = userRepository;
    }

    private String getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    // =========================
    // PREDICTION
    // =========================

    @GetMapping("/prediction")
    public ResponseEntity<?> prediction(@RequestParam(defaultValue = "6") int months) {
        String userId = getLoggedInUserId();
        return ResponseEntity.ok(expenseService.predictNextMonthExpense(userId, months));
    }

    @GetMapping("/prediction/ai")
    public ResponseEntity<?> predictionAI(@RequestParam(defaultValue = "2") int months) {
        String userId = getLoggedInUserId();
        ExpensePredictionResponse data = expenseService.predictNextMonthExpense(userId, months);
        String insight = insightService.predictionInsight(data);

        // ✅ wrap in JSON
        return ResponseEntity.ok(Map.of("insight", insight));
    }

    // =========================
    // MONTHLY COMPARISON
    // =========================

    @GetMapping("/monthly-comparison")
    public ResponseEntity<?> monthlyComparison(@RequestParam int month, @RequestParam int year) {
        String userId = getLoggedInUserId();
        return ResponseEntity.ok(expenseService.getMonthComparison(userId, month, year));
    }

    @GetMapping("/monthly-comparison/ai")
    public ResponseEntity<?> monthlyComparisonAI(@RequestParam int month, @RequestParam int year) {
        String userId = getLoggedInUserId();
        MonthlyComparisonResponse data = expenseService.getMonthComparison(userId, month, year);
        String insight = insightService.monthlyComparisonInsight(data);

        return ResponseEntity.ok(Map.of("insight", insight));
    }

    // =========================
    // CATEGORY COMPARISON
    // =========================

    @GetMapping("/category-comparison")
    public ResponseEntity<?> categoryComparison(@RequestParam int month, @RequestParam int year) {
        String userId = getLoggedInUserId();
        return ResponseEntity.ok(expenseService.getCategoryComparison(userId, month, year));
    }

    @GetMapping("/category-comparison/ai")
    public ResponseEntity<?> categoryComparisonAI(@RequestParam int month, @RequestParam int year) {
        String userId = getLoggedInUserId();
        CategoryComparisonResponse data = expenseService.getCategoryComparison(userId, month, year);
        String insight = insightService.categoryComparisonInsight(data);

        return ResponseEntity.ok(Map.of("insight", insight));
    }

    // =========================
    // MONTHLY REPORT
    // =========================

    @GetMapping("/monthly-report")
    public ResponseEntity<?> monthlyReport(@RequestParam int month, @RequestParam int year) {
        String userId = getLoggedInUserId();
        return ResponseEntity.ok(expenseService.getMonthlyReport(userId, month, year));
    }

    @GetMapping("/monthly-report/ai")
    public ResponseEntity<?> monthlyReportAI(@RequestParam int month, @RequestParam int year) {
        String userId = getLoggedInUserId();
        MonthlyReportResponse data = expenseService.getMonthlyReport(userId, month, year);
        String insight = insightService.monthlyReportInsight(data);

        return ResponseEntity.ok(Map.of("insight", insight));
    }
}

