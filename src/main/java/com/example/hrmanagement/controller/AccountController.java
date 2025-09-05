package com.example.hrmanagement.controller;

import com.example.hrmanagement.dto.AccountRequest;
import com.example.hrmanagement.dto.AccountResponse;
import com.example.hrmanagement.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/getAllEmployees")
    public List<AccountResponse> getAllEmployee() {
        return accountService.getAllEmployees();
    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody AccountRequest request) {
        accountService.createUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody AccountRequest request) {
        accountService.updateUser(id, request);
        return "User updated successfully";
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        accountService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
