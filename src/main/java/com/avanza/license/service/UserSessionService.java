package com.avanza.license.service;

import com.avanza.license.entity.UserSession;

import java.util.List;

public interface UserSessionService {
    UserSession saveUserSession(UserSession userSession,Long userId,Long appId);
    List<UserSession> getAllUserSessions();
//    void deleteUserSession(String sessionId);

    void deleteUserSessionRow(Long appId,String sessionId);

//    UserSession getUserSessionById(Long sessionId);
   List<UserSession> getUserSessionByUserId(Long userId);

   long getSessionCount(Long userId,Long appId);


}
