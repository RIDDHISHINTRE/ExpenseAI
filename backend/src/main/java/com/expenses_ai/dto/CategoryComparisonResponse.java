package com.expenses_ai.dto;

import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryComparisonResponse {
    
    private int month;
    private int year;
    private List<CategoryComparisonItem> comparisons;
}
