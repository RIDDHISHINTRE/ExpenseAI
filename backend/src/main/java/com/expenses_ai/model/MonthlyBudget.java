package com.expenses_ai.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "monthly_budget")
public class MonthlyBudget {
    
    @Id
    private String id;

    @NotBlank(message = "User id is required")
    private String userId;

    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private int month;

    @Min(value = 2000, message = "Year must be valid")
    private int year;

    @NotNull(message = "Budget amount is required")
    @Positive(message = "Budget must be greater than zero")
    private Double budgetAmount;

}
