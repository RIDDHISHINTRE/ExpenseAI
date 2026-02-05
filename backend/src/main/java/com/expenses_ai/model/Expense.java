package com.expenses_ai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "expenses")
public class Expense {

    public enum Category {
        FOOD,
        TRAVEL,
        RENT,
        SHOPPING,
        UTILITIES,
        ENTERTAINMENT,
        HEALTH,
        OTHER
    }

    @Id
    private String id;

    @NotBlank(message = "User Id cannot be null")
    private String userId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 50)
    private String title;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Category is required")
    private Category category;   // ✅ enum

    @NotNull(message = "Date is required")
    private LocalDate date;

    @Size(max = 200)
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
