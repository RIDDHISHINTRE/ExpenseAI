package com.expenses_ai.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsightResponse<T> {

    private T data;
    private String aiInsight;
}
