package com.expenses_ai.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryComparisonItem {
    
    private String category;

    private double currentMonthAmount;

    private double previousMonthAmount;

    private double difference;

    private double percentageChange;
    
    private String trend;

}
