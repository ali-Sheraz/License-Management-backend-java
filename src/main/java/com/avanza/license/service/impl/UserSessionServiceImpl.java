package com.avanza.license.service.impl;

import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.Application;
import com.avanza.license.entity.UserSession;
import com.avanza.license.entity.UserTable;
import com.avanza.license.repositories.ApplicationRepository;
import com.avanza.license.repositories.UserSessionRepository;
import com.avanza.license.repositories.UserTableRepository;
import com.avanza.license.service.UserSessionService;
import com.avanza.license.util.ErrorHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private UserTableRepository userTableRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public UserSession saveUserSession(UserSession userSession, Long userId, Long appId) {

        Optional<UserTable> ownerOptional = userTableRepository.findByUserId(userId);
        if (!ownerOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID);
        } else {
            UserTable userTable = ownerOptional.get();
            Optional<Application> applicationOptional = applicationRepository.findByAppId(appId);
            if (!applicationOptional.isPresent()) {
                ErrorHandlerUtil.handleError(ErrorCode.INVALID_APP_ID);
            } else {
                Application application = applicationOptional.get();
                userSession.setUserTable(userTable);
                userSession.setApplication(application);
                return userSessionRepository.save(userSession);
            }

        }
        return userSession;
    }

    @Override
    public List<UserSession> getAllUserSessions() {
        return userSessionRepository.findAll();
    }


    @Override
    @Transactional
    public void deleteUserSessionRow(Long appId, String sessionId) {
        if (userSessionRepository.existsBySessionIdAndApplicationAppId(sessionId, appId)) {
            userSessionRepository.deleteBySessionIdAndApplicationAppId(sessionId, appId);
        } else {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_SESSION_ID);
        }
    }

//    @Override
//    public UserSession getUserSessionByUserId(Long userId) {
//        Optional<UserSession> userSessionOptional = userSessionRepository.findByUserId(userId);
//        if (!userSessionOptional.isPresent()) {
//            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID);
//        }
//        return userSessionOptional.get();
//    }
    @Override
    public List<UserSession> getUserSessionByUserId(Long userId) {
        return userSessionRepository.findByUserTableUserId(userId);
    }
    public long getSessionCount(Long userId, Long appId) {
        return userSessionRepository.countByUserTable_UserIdAndApplication_AppId(userId, appId);
    }

//    @Override
//    public UserSession getUserSessionById(Long sessionId) {
//        Optional<UserSession> userSessionOptional = userSessionRepository.findBySessionId(sessionId);
//        if (!userSessionOptional.isPresent()) {
//            ErrorHandlerUtil.handleError(ErrorCode.INVALID_SESSION_ID);
//        }
//        return userSessionOptional.get();
//    }
    //    @Override
//    @Transactional
//    public void deleteUserSession(String sessionId) {
//        if (userSessionRepository.existsBySessionId(sessionId)) {
//            userSessionRepository.deleteBySessionId(sessionId);
//        } else {
//            ErrorHandlerUtil.handleError(ErrorCode.INVALID_SESSION_ID);
//        }
//    }
}
