package com.omniversity.public_community_service.Community.AbstractCommunity;

import com.omniversity.public_community_service.PublicCommunity.AbstractCommunity.AbstractCommunityRepository;
import com.omniversity.public_community_service.PublicCommunity.AbstractCommunity.AbstractCommunityService;
import com.omniversity.public_community_service.PublicCommunity.AbstractCommunity.dto.update.CommunityUpdateLogoImage;
import com.omniversity.public_community_service.PublicCommunity.Entity.AbstractCommunity;
import com.omniversity.public_community_service.PublicCommunity.Entity.UniversityCommunity;
import com.omniversity.public_community_service.PublicCommunity.Exception.CommunityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractCommunityServiceTest {
    @Mock
    private AbstractCommunityRepository abstractCommunityRepository;

    @InjectMocks
    private AbstractCommunityService abstractCommunityService;

    private UniversityCommunity universityCommunity;

    @BeforeEach
    void setup() {
        universityCommunity = new UniversityCommunity();
        universityCommunity.setId(1L);
        universityCommunity.setName("Test Community");
        universityCommunity.setLogoImage("logoImageUrl");
        universityCommunity.setBackgroundImage("backgroundImageUrl");
    }

    @Test
    void testGetCommunityById_Success() {
        when(abstractCommunityRepository.findById(1L)).thenReturn(Optional.of(universityCommunity));

        assertEquals(universityCommunity,abstractCommunityService.getCommunityById(1L));
    }

    @Test
    void testGetCommunityById_NotFound() {
        when(abstractCommunityRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CommunityNotFoundException.class, () -> abstractCommunityService.getCommunityById(2L));
    }

    @Test
    void testCheckUserNameTaken_Taken() {
        when(abstractCommunityRepository.findByName("Test Community")).thenReturn(Optional.of(universityCommunity));

        assertTrue(abstractCommunityService.checkNameTaken("Test Community"));
    }

    @Test
    void testCheckUserNameTaken_NotTaken() {
        when(abstractCommunityRepository.findByName("Test Community")).thenReturn(Optional.empty());

        assertFalse(abstractCommunityService.checkNameTaken("Test Community"));
    }
}
