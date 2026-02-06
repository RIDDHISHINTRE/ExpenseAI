package com.expenses_ai.controller;

import com.expenses_ai.model.Expense;
import com.expenses_ai.model.Expense.Category;
import com.expenses_ai.model.User;
import com.expenses_ai.service.ExpenseService;
import com.expenses_ai.repository.UserRepository;
import com.expenses_ai.dto.ExpenseRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import java.util.Map;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    
    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseService expenseService , UserRepository userRepository){
       this.expenseService = expenseService;
       this.userRepository = userRepository; 
    }

    private String getLoggedInUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (auth != null) ? auth.getName() : null;

        User user = userRepository.findByEmail(email)
                   .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();        
    }

    @PostMapping("/add")
    public ResponseEntity<?> addExpense(@Valid @RequestBody ExpenseRequest req){
        String userId = getLoggedInUserId();

        expenseService.checkBudgetBeforeAdding(userId, req.getAmount());
        
        Expense saved = expenseService.addExpense(userId , req);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/list")
    public ResponseEntity<?> listExpenses(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
         @RequestParam(defaultValue = "date") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir
    ){
        String userId = getLoggedInUserId();
        Page<Expense> list = expenseService.getExpensesforUser(userId ,page,size,sortBy ,sortDir);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable String id , @Valid @RequestBody ExpenseRequest req){
        String userId = getLoggedInUserId();
        Expense updated = expenseService.updateExpense(id, userId, req);
        return ResponseEntity.ok(updated);
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteExpense(@PathVariable String id) {
        String userId = getLoggedInUserId();
        expenseService.deleteExpense(id, userId);

        return ResponseEntity.ok(
            Map.of("message", "Expense deleted successfully")
        );
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchExpenses(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        String userId = getLoggedInUserId();

        // Sorting logic
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<?> result = expenseService.searchExpenses(
                userId,
                title,
                category,
                pageable
        );

        return ResponseEntity.ok(result);
    }

}
