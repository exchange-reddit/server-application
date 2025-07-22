package com.omniversity.public_community_service.Community.UniversityCommunity;

import com.omniversity.public_community_service.PublicCommunity.AbstractCommunity.AbstractCommunityService;
import com.omniversity.public_community_service.PublicCommunity.Entity.AbstractCommunity;
import com.omniversity.public_community_service.PublicCommunity.Entity.UniversityCommunity;
import com.omniversity.public_community_service.PublicCommunity.Entity.enums.City;
import com.omniversity.public_community_service.PublicCommunity.Entity.enums.Country;
import com.omniversity.public_community_service.PublicCommunity.Exception.CommunityNameTakenException;
import com.omniversity.public_community_service.PublicCommunity.Exception.CommunityNotFoundException;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.UniversityCommunityRepository;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.UniversityCommunityService;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.request.UniversityCommunityCreationDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response.UniversityCommunityIntroductionDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response.UniversityCommunityPageDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.mapper.request.UniversityCommunityMapper;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.mapper.response.UniversityCommunityResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniversityCommunityServiceTest {
    @Mock
    private UniversityCommunityRepository universityCommunityRepository;


    @Mock
    private UniversityCommunityResponse universityCommunityResponse;

    @Mock
    private AbstractCommunityService abstractCommunityService;

    @InjectMocks
    private UniversityCommunityService universityCommunityService;

    private UniversityCommunity universityCommunity;

    @BeforeEach
    void setUp() {
        universityCommunity = new UniversityCommunity();
        universityCommunity.setId(1L);
        universityCommunity.setName("Test University");
        universityCommunity.setCreatedDate(LocalDate.now());
    }

    @Test
    void testCreateUniversityCommunity_Success() {
        UniversityCommunityCreationDto dto = new UniversityCommunityCreationDto(
                "Test University",
                "logoImageUrl",
                "backgroundImageUrl",
                "This is a simple description",
                City.SEOUL,
                Country.REPUBLIC_OF_KOREA,
                "testEmail@test.com",
                "test.com",
                "+821000000000"
        );

        when(abstractCommunityService.checkNameTaken(dto.name())).thenReturn(false);
        when(universityCommunityService.createUniversityCommunity(dto)).thenReturn(universityCommunity);

        assertEquals(universityCommunity, universityCommunityService.createUniversityCommunity(dto));
        assertDoesNotThrow(() -> universityCommunityService.createUniversityCommunity(dto));
    }

    @Test
    void testCreateUniversityCommunity_CommunityNameTaken() {
        UniversityCommunityCreationDto dto = new UniversityCommunityCreationDto(
                "Test University",
                "logoImageUrl",
                "backgroundImageUrl",
                "This is a simple description",
                City.SEOUL,
                Country.REPUBLIC_OF_KOREA,
                "testEmail@test.com",
                "test.com",
                "+821000000000"
        );

        when(abstractCommunityService.checkNameTaken(dto.name())).thenReturn(true);

        assertThrows(CommunityNameTakenException.class, () -> universityCommunityService.createUniversityCommunity(dto));
    }

    @Test
    void testGetUniversityCommunityById_Success() {
        when(universityCommunityRepository.findById(1L)).thenReturn(Optional.of(universityCommunity));

        assertEquals(universityCommunity, universityCommunityService.getUniversityCommunityById(1L));
    }

    @Test
    void testGetUniversityCommunityById_NotFound() {
        when(universityCommunityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CommunityNotFoundException.class, () -> universityCommunityService.getUniversityCommunityById(1L));
    }

    @Test
    void testGetUniversityCommunityPageById_Success() {
        UniversityCommunityPageDto resDto = new UniversityCommunityPageDto(
               "Test University",
                LocalDate.now(),
                "logoImageUrl",
                "backgroundImageUrl",
                "Random description",
                City.SEOUL,
                Country.REPUBLIC_OF_KOREA,
                "test@test.com",
                "test.com",
                "+821000000000"
        );

        when(universityCommunityResponse.toResponseUniversityCommunityDto(universityCommunity)).thenReturn(resDto);

        assertEquals(resDto, universityCommunityService.getUniversityCommunityPageById(universityCommunity));
    }

    @Test
    void testGetUniversityCommunityIntroductionById_Success() {
        UniversityCommunityIntroductionDto resDto = new UniversityCommunityIntroductionDto(
                "Test University",
                "backgroundImageUrl",
                "logoImageUrl",
                "Random description"
        );

        when(universityCommunityResponse.toUniversityCommunityIntroductionDto(universityCommunity)).thenReturn(resDto);

        assertEquals(resDto, universityCommunityService.getUniversityCommunityIntroductionById(universityCommunity));
    }
}
