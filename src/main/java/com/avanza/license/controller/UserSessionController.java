package com.avanza.license.controller;

import com.avanza.license.entity.UserSession;
import com.avanza.license.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserSessionController {

    @Autowired
    private UserSessionService userSessionService;

    @PostMapping("/userSession/{userId}/{appId}")
    public ResponseEntity<UserSession> saveUserSession(@RequestBody UserSession userSession, @PathVariable Long userId, @PathVariable Long appId) {
        UserSession savedSession = userSessionService.saveUserSession(userSession, userId, appId);
        return new ResponseEntity<>(savedSession, HttpStatus.CREATED);
    }

    @GetMapping("/userSession")
    public ResponseEntity<List<UserSession>> getAllUserSession() {
        List<UserSession> userSessions = userSessionService.getAllUserSessions();
        return new ResponseEntity<>(userSessions, HttpStatus.OK);
    }

    //
    @GetMapping("/userSession/{userId}")
    public ResponseEntity<List<UserSession>> getUserSessionByUserId(@PathVariable Long userId) {
        List<UserSession> userSessions = userSessionService.getUserSessionByUserId(userId);
        return new ResponseEntity<>(userSessions, HttpStatus.OK);

    }

    @DeleteMapping("/deleteUserSession/{appId}/{sessionId}")
    public ResponseEntity<Void> deleteUserSessionRow(@PathVariable Long appId, @PathVariable String sessionId) {
        userSessionService.deleteUserSessionRow(appId, sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @DeleteMapping("/userSession/{sessionId}")
//    public ResponseEntity<Void> deleteUserSession(@PathVariable String sessionId) {
//        userSessionService.deleteUserSession(sessionId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//    @GetMapping("/userSession/{sessionId}")
//    public ResponseEntity<UserSession> getUserSessionById(@PathVariable Long sessionId) {
//        UserSession userSession = userSessionService.getUserSessionById(sessionId);
//        return new ResponseEntity<>(userSession,HttpStatus.OK);
//
//    }

}
