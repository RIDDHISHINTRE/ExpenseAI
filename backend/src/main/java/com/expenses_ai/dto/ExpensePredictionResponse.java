package com.expenses_ai.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpensePredictionResponse {
    
    private int monthsUsed;
    private double predictedAmount;
    private String trend; /*Up / down / stable */
}
