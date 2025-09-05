package com.example.hrmanagement.controller;

import com.example.hrmanagement.dto.UserDetailResponse;
import com.example.hrmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    // GET ALL USERS
    @GetMapping("/getAllUsers")
    public List<UserDetailResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET USER BY ID
    @GetMapping("/getUserById/{id}")
    public UserDetailResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // DELETE USER
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
