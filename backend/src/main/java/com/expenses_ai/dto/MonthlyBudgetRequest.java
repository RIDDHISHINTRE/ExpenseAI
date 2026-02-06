package com.expenses_ai.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MonthlyBudgetRequest {

    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private int month;

    @Min(value = 2000, message = "Year must be valid") // adjust as needed
    private int year;

    @NotNull(message = "Budget amount is required")
    @Positive(message = "Budget must be greater than 0")
    private Double budgetAmount;
}
