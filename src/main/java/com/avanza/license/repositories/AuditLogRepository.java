package com.avanza.license.repositories;
import com.avanza.license.entity.AuditLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface AuditLogRepository extends CrudRepository<AuditLog,Long>{

}
