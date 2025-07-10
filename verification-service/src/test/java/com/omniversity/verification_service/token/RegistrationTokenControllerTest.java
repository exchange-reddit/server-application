package com.omniversity.verification_service.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniversity.verification_service.token.dto.RegistrationDto;
import com.omniversity.verification_service.token.entity.RegistrationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegistrationTokenControllerTest {

    @Mock
    private RegistrationTokenService registrationTokenService;

    @InjectMocks
    private RegistrationTokenController registrationTokenController;

    private RegistrationToken registrationToken;

    @BeforeEach
    void setUp() {
        registrationToken = new RegistrationToken();
        registrationToken.setId(1L);
        registrationToken.setCode("RandomCode");
        registrationToken.setExpiryDate(LocalDateTime.now());
        registrationToken.setEmail("test@email.com");
        registrationToken.setVerificationType(1);
    }

    @Test
    void requestTokenEmailTest() {
        RegistrationDto dto = new RegistrationDto(
                "test",
                "test@email.com",
                1
        );

        doReturn(false).when(registrationTokenService).checkDuplicate(dto.email());
        when(registrationTokenService.createRegistrationToken(dto)).thenReturn(registrationToken);

        assertEquals(HttpStatus.OK, registrationTokenController.requestTokenEmail(dto).getStatusCode());
    }
}
