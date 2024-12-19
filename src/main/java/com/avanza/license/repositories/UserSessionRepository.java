package com.avanza.license.repositories;


import com.avanza.license.entity.UserSession;
import com.avanza.license.entity.UserSessionId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends CrudRepository<UserSession, UserSessionId> {
    Boolean existsBySessionId(String sessionId);
    void deleteBySessionId(String sessionId);
    Optional<UserSession> findBySessionId(Long sessionId);
    List<UserSession> findAll();

    List<UserSession> findByUserTableUserId(Long userId);
    long count();
    long countByApplicationAppId(Long appId);

    boolean existsBySessionIdAndApplicationAppId(String sessionId, Long appId);

    void deleteBySessionIdAndApplicationAppId(String sessionId, Long appId);
    long countByUserTable_UserIdAndApplication_AppId(Long userId, Long appId);



}
