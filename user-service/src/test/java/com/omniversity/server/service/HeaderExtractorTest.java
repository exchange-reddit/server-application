package com.omniversity.server.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HeaderExtractorTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private HeaderExtractor headerExtractor;

    @Test
    void testExtractIp() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1");
        assertEquals("192.168.1.1", headerExtractor.getClientIpAddress(request));
    }

    @Test
    void testExtractIp_NullHeader() {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        assertEquals("127.0.0.1", headerExtractor.getClientIpAddress(request));
    }
}