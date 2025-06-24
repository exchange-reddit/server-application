package com.omniversity.server.log;

import com.omniversity.server.log.dto.accountDeleteLogDto;
import com.omniversity.server.log.dto.accountUpdateLogDto;
import com.omniversity.server.log.dto.pwChangeLogDto;

import com.omniversity.server.log.entity.AbstractLog;
import com.omniversity.server.log.entity.AccountDeleteLog;
import com.omniversity.server.log.entity.AccountUpdateLog;
import com.omniversity.server.log.entity.PwChangeLog;
import com.omniversity.server.service.HeaderExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogService {
    private LogRepository logRepository;
    private HeaderExtractor headerExtractor;

    @Autowired
    public LogService (LogRepository logRepository, HeaderExtractor headerExtractor) {
        this.logRepository = logRepository;
        this.headerExtractor = headerExtractor;
    }

    public AbstractLog getLogByUser(Long id) throws RuntimeException {
        Optional<AbstractLog> optionalLog = logRepository.findById(id);

        if (optionalLog.isEmpty()) {
            throw new RuntimeException("The requested log could not be found");
        }

        return optionalLog.get();
    }

    /**
     * About:
     * Log PW change to the SQL table
     * Password prior to change is saved in the DB
     * @param pwChangeLogDto
     */
    public void logPWChange(pwChangeLogDto pwChangeLogDto, HttpServletRequest request) {
        // Retrieve the IP address of the requester from the header
        String ipAddress = headerExtractor.getClientIpAddress(request);

        // Create a new log object
        PwChangeLog log = new PwChangeLog(
                pwChangeLogDto.updateUser(),
                ipAddress,
                pwChangeLogDto.updateContent()
        );

        log.setAuditResult(true);

        logRepository.save(log);
    }

    public void logAccountDeletion(accountDeleteLogDto dto, HttpServletRequest request) {
        // Retrieve the IP address of the requester from the header
        String ipAddress = headerExtractor.getClientIpAddress(request);

        // Create a new log object
        AccountDeleteLog log = new AccountDeleteLog(
                dto.updateUser(),
                ipAddress,
                dto.updateContent()
        );

        log.setAuditResult(true);

        logRepository.save(log);

    }

    public void logAccountUpdate(accountUpdateLogDto dto, HttpServletRequest request) {
        // Retrieve the IP address of the requester from the header
        String ipAddress = headerExtractor.getClientIpAddress(request);

        // Create a new log object
        AccountUpdateLog log = new AccountUpdateLog(
                dto.updateUser(),
                ipAddress,
                dto.updateContent()
        );

        log.setAuditResult(true);

        logRepository.save(log);
    }
}
