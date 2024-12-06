package com.avanza.license.controller;

import com.avanza.license.entity.UserTable;
import com.avanza.license.service.UserTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2")
public class UserTableController {

    @Autowired
    private UserTableService userTableService;

//    @PostMapping("/userTable")
//    public ResponseEntity<UserTable> saveUser(@RequestBody UserTable user) {
//        UserTable savedUser = userTableService.saveUser(user);
//        return new ResponseEntity<>(savedUser, HttpStatus.OK);
//    }

    @GetMapping("/userTable")
    public ResponseEntity<List<UserTable>> getAllUser() {
        List<UserTable> userList = userTableService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/userTable/{userId}")
    public ResponseEntity<UserTable> getUserById(@PathVariable Long userId) {
        UserTable user = userTableService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/userTable/{userId}")
    public ResponseEntity<UserTable> updateUser(@PathVariable Long userId, @RequestBody UserTable user) {
        UserTable updatedUser = userTableService.updateUser(userId, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }

    @DeleteMapping("/userTable/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userTableService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
