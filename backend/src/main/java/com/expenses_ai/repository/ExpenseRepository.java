package com.expenses_ai.repository;

import com.expenses_ai.model.Expense;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.expenses_ai.model.Expense.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.expenses_ai.dto.MonthlyExpenseTotal;

import java.util.List;
import java.util.Optional;


public interface ExpenseRepository extends MongoRepository<Expense , String>{
    
    Page<Expense> findByUserId(String userId ,Pageable pageable);
    
     //Search by title (case-insensitive)
    Page<Expense> findByUserIdAndTitleContainingIgnoreCase(String userId , String title , Pageable pageable);
    
     //Search by category
    Page<Expense> findByUserIdAndCategory(String userId , Category category, Pageable pageable);

    //Search by BOTH title + category
    Page<Expense> findByUserIdAndTitleContainingIgnoreCaseAndCategory(
            String userId,
            String title,
            Category category,
            Pageable pageable
    );

    List<Expense> findByUserId(String userId);

    Optional<Expense> findByIdAndUserId(String id , String userId);

        //5 hours 30 minutes
        // = 5 × 60 × 60 × 1000
        // = 19800000 milliseconds
        // mongo store date in utc time zone but we want ist
        // UTC date + 5h 30m = IST date
        // hence add fields have 19800000 ms
        @Aggregation(pipeline = {
        "{ $match: { userId: ?0 } }",
        "{ $addFields: { " +
                "localDate: { $add: [ '$date', 19800000 ] }" +
        "} }",
        "{ $match: { $expr: { $and: [ " +
                "{ $eq: [ { $month: '$localDate' }, ?1 ] }, " +
                "{ $eq: [ { $year: '$localDate' }, ?2 ] } " +
        "] } } }"
        })
        List<Expense> findExpensesByMonthAndYear(
                String userId,
                int month,
                int year
        );

        @Aggregation(pipeline = {
        "{ $match: { userId: ?0 } }",
        "{ $group: { _id: { year: { $year: '$date' }, month: { $month: '$date' } }, total: { $sum: '$amount' } } }",
        "{ $sort: { '_id.year': -1, '_id.month': -1 } }"
        })
        
        List<MonthlyExpenseTotal> findMonthlyTotals(String userId, Pageable pageable);
}
