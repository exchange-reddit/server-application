package com.omniversity.server.log;

import com.omniversity.server.log.dto.accountDeleteLogDto;
import com.omniversity.server.log.dto.pwChangeLogDto;
import com.omniversity.server.log.entity.AccountDeleteLog;
import com.omniversity.server.log.entity.PwChangeLog;
import com.omniversity.server.service.HeaderExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @Mock
    private HeaderExtractor headerExtractor;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private LogService logService;

    @Test
    void testLogPWChange() {
        pwChangeLogDto dto = new pwChangeLogDto("1", "oldHash");
        when(headerExtractor.getClientIpAddress(request)).thenReturn("127.0.0.1");

        logService.logPWChange(dto, request);

        verify(logRepository).save(any(PwChangeLog.class));
    }

    @Test
    void testLogAccountDeletion() {
        accountDeleteLogDto dto = new accountDeleteLogDto("1", "Delete Account");
        when(headerExtractor.getClientIpAddress(request)).thenReturn("127.0.0.1");

        logService.logAccountDeletion(dto, request);

        verify(logRepository).save(any(AccountDeleteLog.class));
    }
}