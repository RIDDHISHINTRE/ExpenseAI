package com.expenses_ai.service;

import com.expenses_ai.dto.*;
import org.springframework.stereotype.Service;

@Service
public class InsightService {

    private final LocalLLMService llmService;

    public InsightService(LocalLLMService llmService) {
        this.llmService = llmService;
    }

    // ---------------- Prediction Insight ----------------
    public String predictionInsight(ExpensePredictionResponse data) {

        if (data == null || data.getMonthsUsed() < 2) {
            return "There is not enough past data to explain the prediction.";
        }

        String prompt = """
A prediction has been generated for upcoming expenses.
The overall spending trend is described as: %s.

Explain this prediction in simple English.
Do NOT use numbers, currency symbols, or percentages.
Do NOT mention months, dates, or time ranges.
Do NOT guess reasons outside the data.
Keep it short and clear.
Add one small practical suggestion in plain English.
"""
.formatted(data.getTrend());

        return llmService.generate(prompt);
    }

    // ---------------- Monthly Comparison Insight ----------------
    public String monthlyComparisonInsight(MonthlyComparisonResponse data) {

        if (data == null) {
            return "No comparison data is available.";
        }

        String prompt = """
A comparison was made between the current period and the previous one.
The overall trend is described as: %s.

Explain what this change means in simple English.
Do NOT use numbers, currency symbols, or percentages.
Do NOT assume user behavior or reasons.
Keep it short and easy to understand.
Add one small practical suggestion.
"""
.formatted(data.getTrend());

        return llmService.generate(prompt);
    }

    // ---------------- Category Comparison Insight ----------------
    public String categoryComparisonInsight(CategoryComparisonResponse data) {

        if (data == null || data.getComparisons() == null || data.getComparisons().isEmpty()) {
            return "No category comparison data is available.";
        }

        StringBuilder categorySummary = new StringBuilder();
        for (CategoryComparisonItem item : data.getComparisons()) {
            categorySummary.append(
                "%s shows a %s trend.\n"
                    .formatted(
                        item.getCategory(),
                        item.getTrend()
                    )
            );
        }

        String prompt = """
Here is how spending changed across categories:
%s

Summarize which categories went up or down.
Use only the information above.
Do NOT use numbers or currency.
Do NOT guess reasons.
Write in very simple English.
Add one small relevant suggestion.
"""
.formatted(categorySummary.toString());

        return llmService.generate(prompt);
    }

    // ---------------- Monthly Report Insight ----------------
    public String monthlyReportInsight(MonthlyReportResponse data) {

        if (data == null || data.getTransactionCount() == 0) {
            return "No spending activity was recorded.";
        }

        String prompt = """
A spending report was generated.
The most expensive category is: %s.
There were multiple transactions during this period.

Explain what this report shows in simple English.
Do NOT use numbers, currency symbols, or dates.
Do NOT assume lifestyle or future plans.
Keep it short and clear.
Add one small practical suggestion.
"""
.formatted(data.getHighestSpendingCategory());

        return llmService.generate(prompt);
    }
}
