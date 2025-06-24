package com.omniversity.server.log;

import com.omniversity.server.log.entity.AbstractLog;
import com.omniversity.server.log.entity.PwChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<AbstractLog, Long> {
    Optional<AbstractLog> findById(Long id);
    List<AbstractLog> findByUpdateUser(String updateUser);

    /**
    @Query("SELECT 1 FROM AbstractLog 1 WHERE TYPE(1) = :auditType")
    List<AbstractLog> findByAuditType(@Param("auditType") Class<? extends AbstractLog> auditType);

    List<PwChangeLog> findByUpdateUserAndEventType(String updateUser, String auditType);
    */

}
