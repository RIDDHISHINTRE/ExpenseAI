package com.expenses_ai.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyComparisonResponse{
    
    private int month;
    private int year;

    private double currentMonthTotal;
    private double previousMonthTotal;

    private double difference;
    private double percentageChange;

    private String trend;
}