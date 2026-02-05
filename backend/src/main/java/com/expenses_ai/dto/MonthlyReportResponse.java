package com.expenses_ai.dto;

import com.expenses_ai.model.Expense;
import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportResponse {
    
    private int month;
    private int year;

    private double totalExpense;
    private int transactionCount;

    private Map<String , Double> categoryWiseTotals;
    private String highestSpendingCategory;
    private Expense highestSingleExpense;
    
}
