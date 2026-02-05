package com.expenses_ai.dto;

import com.expenses_ai.model.Expense.Category;
import lombok.*;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String title;

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    private Category category;   // ✅ enum

    @NotNull
    private LocalDate date;

    @Size(max = 200)
    private String description;
}
