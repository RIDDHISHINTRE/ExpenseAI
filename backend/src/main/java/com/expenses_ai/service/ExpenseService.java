package com.expenses_ai.service;

import com.expenses_ai.dto.CategoryComparisonItem;
import com.expenses_ai.dto.ExpenseRequest;
import com.expenses_ai.dto.MonthlyComparisonResponse;
import com.expenses_ai.dto.MonthlyExpenseTotal;
import com.expenses_ai.exception.ResourceNotFoundException;
import com.expenses_ai.exception.BadRequestException;
import com.expenses_ai.model.Expense;
import com.expenses_ai.model.Expense.Category;
import com.expenses_ai.repository.ExpenseRepository;
import com.expenses_ai.dto.MonthlyReportResponse;
import com.expenses_ai.dto.CategoryComparisonResponse;
import com.expenses_ai.dto.ExpensePredictionResponse;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense addExpense(String userId, ExpenseRequest req) {
        Expense e = new Expense();
        e.setUserId(userId);
        e.setTitle(req.getTitle());
        e.setAmount(req.getAmount());
        e.setCategory(req.getCategory());
        e.setDate(req.getDate());
        e.setDescription(req.getDescription());

        return expenseRepository.save(e);
    }

    public Page<Expense> getExpensesforUser(String userId , int page ,int size
        ,String sortBy , String sortDir
    ) {
        
        if (page < 0 || size <= 0) {
            throw new BadRequestException("Invalid page or size");
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size ,sort);
        return expenseRepository.findByUserId(userId, pageable);
    }

    public Expense updateExpense(String expenseId, String userId, ExpenseRequest req) {
        Expense existing = expenseRepository
                .findByIdAndUserId(expenseId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found")
                );

        existing.setTitle(req.getTitle());
        existing.setAmount(req.getAmount());
        existing.setCategory(req.getCategory());
        existing.setDate(req.getDate());
        existing.setDescription(req.getDescription());
        existing.setUpdatedAt(LocalDateTime.now());

        return expenseRepository.save(existing);
    }

    public void deleteExpense(String expenseId, String userId) {
        Expense existing = expenseRepository
                .findByIdAndUserId(expenseId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found")
                );

        expenseRepository.delete(existing);
    }

    
    public Page<Expense> searchExpenses(
            String userId,
            String title,
            Category category,
            Pageable pageable
    ) {

        // both title and category present
        if (title != null && category != null) {
            return expenseRepository
                    .findByUserIdAndTitleContainingIgnoreCaseAndCategory(
                            userId, title, category, pageable
                    );
        }

        // only title present
        if (title != null) {
            return expenseRepository
                    .findByUserIdAndTitleContainingIgnoreCase(
                            userId, title, pageable
                    );
        }

        // only category present
        if (category != null) {
            return expenseRepository
                    .findByUserIdAndCategory(
                            userId, category, pageable
                    );
        }

        // nothing provided → return all (paginated)
        return expenseRepository.findByUserId(userId, pageable);
    }

    public MonthlyReportResponse getMonthlyReport(
        String userId , int month , int year
    ){
      
        List<Expense> expenses =
        expenseRepository.findExpensesByMonthAndYear(
                userId, month, year
        );

        double totalExpense = 0;
        int transactionCount = expenses.size();

        Map<String, Double> categoryTotals = new HashMap<>();

        Expense highestExpense = null;
        double maxAmount = 0;

        for (Expense e : expenses) {

            totalExpense += e.getAmount();

            String category = e.getCategory().name();
            categoryTotals.put(
                    category,
                    categoryTotals.getOrDefault(category, 0.0) + e.getAmount()
            );

            if (e.getAmount() > maxAmount) {
                maxAmount = e.getAmount();
                highestExpense = e;
            }
        }

        String highestCategory = null;
        double highestCategoryAmount = 0;

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            if (entry.getValue() > highestCategoryAmount) {
                highestCategoryAmount = entry.getValue();
                highestCategory = entry.getKey();
            }
        }

        MonthlyReportResponse response = new MonthlyReportResponse();
        response.setMonth(month);
        response.setYear(year);
        response.setTotalExpense(totalExpense);
        response.setTransactionCount(transactionCount);
        response.setCategoryWiseTotals(categoryTotals);
        response.setHighestSpendingCategory(highestCategory);
        response.setHighestSingleExpense(highestExpense);

        return response;
    }

    public MonthlyComparisonResponse getMonthComparison(
                String userId,
                int month,
                int year
        ) {

        // Get current month expenses (IST safe)
        List<Expense> currentMonthExpenses =
                expenseRepository.findExpensesByMonthAndYear(
                        userId, month, year
                );

        // Calculate previous month & year
        int previousMonth = month - 1;
        int previousYear = year;

        if (previousMonth == 0) {
            previousMonth = 12;
            previousYear = year - 1;
        }

        // Get previous month expenses (IST safe)
        List<Expense> previousMonthExpenses =
                expenseRepository.findExpensesByMonthAndYear(
                        userId, previousMonth, previousYear
                );

        // Calculate totals
        double currentTotal = 0;
        for (Expense e : currentMonthExpenses) {
            currentTotal += e.getAmount();
        }

        double previousTotal = 0;
        for (Expense e : previousMonthExpenses) {
            previousTotal += e.getAmount();
        }

        // Difference
        double difference = currentTotal - previousTotal;

        // Percentage change (safe)
        double percentageChange = 0;
        if (previousTotal != 0) {
            percentageChange = (difference / previousTotal) * 100;
        }

        // Trend
        String trend;
        if (difference > 0) {
            trend = "INCREASE";
        } else if (difference < 0) {
            trend = "DECREASE";
        } else {
            trend = "NO_CHANGE";
        }

        // Build response
        MonthlyComparisonResponse response =
                new MonthlyComparisonResponse();

        response.setMonth(month);
        response.setYear(year);
        response.setCurrentMonthTotal(currentTotal);
        response.setPreviousMonthTotal(previousTotal);
        response.setDifference(difference);
        response.setPercentageChange(
                Math.round(percentageChange * 100.0) / 100.0
        );
        response.setTrend(trend);

        return response;
    }

    public CategoryComparisonResponse getCategoryComparison(
            String userId,
            int month,
            int year
    ) {

        // 1️⃣ Get current month expenses
        List<Expense> currentExpenses =
                expenseRepository.findExpensesByMonthAndYear(
                        userId, month, year
                );

        // 2️⃣ Calculate previous month & year
        int prevMonth = month - 1;
        int prevYear = year;

        if (prevMonth == 0) {
            prevMonth = 12;
            prevYear = year - 1;
        }

        // 3️⃣ Get previous month expenses
        List<Expense> previousExpenses =
                expenseRepository.findExpensesByMonthAndYear(
                        userId, prevMonth, prevYear
                );

        // 4️⃣ Aggregate category totals (current month)
        Map<String, Double> currentMap = new HashMap<>();
        for (Expense e : currentExpenses) {
            String cat = e.getCategory().name();
            currentMap.put(
                    cat,
                    currentMap.getOrDefault(cat, 0.0) + e.getAmount()
            );
        }

        // 5️⃣ Aggregate category totals (previous month)
        Map<String, Double> previousMap = new HashMap<>();
        for (Expense e : previousExpenses) {
            String cat = e.getCategory().name();
            previousMap.put(
                    cat,
                    previousMap.getOrDefault(cat, 0.0) + e.getAmount()
            );
        }

        // 6️⃣ Union of categories
        Set<String> allCategories = new HashSet<>();
        allCategories.addAll(currentMap.keySet());
        allCategories.addAll(previousMap.keySet());

        List<CategoryComparisonItem> result = new ArrayList<>();

        // 7️⃣ Build comparison per category
        for (String category : allCategories) {

            double current = currentMap.getOrDefault(category, 0.0);
            double previous = previousMap.getOrDefault(category, 0.0);

            double diff = current - previous;

            double percentage = 0;
            if (previous != 0) {
                percentage = (diff / previous) * 100;
            }

            String trend;
            if (diff > 0) {
                trend = "INCREASE";
            } else if (diff < 0) {
                trend = "DECREASE";
            } else {
                trend = "NO_CHANGE";
            }

            CategoryComparisonItem item = new CategoryComparisonItem();
            item.setCategory(category);
            item.setCurrentMonthAmount(current);
            item.setPreviousMonthAmount(previous);
            item.setDifference(diff);
            item.setPercentageChange(
                    Math.round(percentage * 100.0) / 100.0
            );
            item.setTrend(trend);

            result.add(item);
        }

        // 8️⃣ Build response
        CategoryComparisonResponse response =
                new CategoryComparisonResponse();

        response.setMonth(month);
        response.setYear(year);
        response.setComparisons(result);

        return response;
    }
    
    
    public ExpensePredictionResponse predictNextMonthExpense(String userId, int nMonths) {

        Pageable pageable = PageRequest.of(0, nMonths);

        List<MonthlyExpenseTotal> rawData =
                expenseRepository.findMonthlyTotals(userId, pageable);

        if (rawData.size() < nMonths) {
            throw new BadRequestException("Not enough data for prediction");
        }

        double sum = 0;
        for (MonthlyExpenseTotal item : rawData) {
            sum += item.getTotal();
        }

        double average = sum / nMonths;

        double latest = rawData.get(0).getTotal();
        double oldest = rawData.get(nMonths - 1).getTotal();

        String trend;
        if (latest > oldest) {
            trend = "UP";
        } else if (latest < oldest) {
            trend = "DOWN";
        } else {
            trend = "STABLE";
        }

        ExpensePredictionResponse response = new ExpensePredictionResponse();
        response.setMonthsUsed(nMonths);
        response.setPredictedAmount(Math.round(average * 100.0) / 100.0);
        response.setTrend(trend);

        return response;
    }
}
