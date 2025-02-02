package br.com.fechaki.telephone.v1.service;

import br.com.fechaki.telephone.exception.type.TelephoneOTPException;
import br.com.fechaki.telephone.repository.TelephoneOTPRepository;
import br.com.fechaki.telephone.repository.TelephoneRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.entity.TelephoneOTPEntity;
import br.com.fechaki.telephone.v1.service.impl.SNSServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SNSServiceTest {
    @Mock
    TelephoneRepository telephoneRepository;

    @Mock
    SnsClient snsClient;

    @Mock
    TelephoneOTPRepository otpRepository;

    @InjectMocks
    SNSServiceImpl service;

    @Test
    @DisplayName("Generate OTP Key")
    void generateOtp() {
        String telephoneId = "6031dff8-2cd4-4dc7-b3b7-0d0a8b1d4ee8";
        when(otpRepository.save(any(TelephoneOTPEntity.class))).thenReturn(new TelephoneOTPEntity());
        assertNotNull(service.generateOtp(telephoneId));
    }

    @Test
    @DisplayName("Send OTP Message")
    void sendOtp() {
        String telephoneId = "6031dff8-2cd4-4dc7-b3b7-0d0a8b1d4ee8";
        String otpToken = "123456";
        Optional<TelephoneEntity> entity = Optional.of(new TelephoneEntity());
        PublishResponse response = PublishResponse.builder().build();

        ReflectionTestUtils.setField(service, "validationEnabled", true);

        when(snsClient.publish(any(PublishRequest.class))).thenReturn(response);
        when(telephoneRepository.findById(any(UUID.class))).thenReturn(entity);

        assertDoesNotThrow(() -> service.sendOtp(telephoneId, otpToken));
    }

    @Test
    @DisplayName("Do Not Send OTP Message")
    void doNotSendOtp() {
        String telephoneId = "6031dff8-2cd4-4dc7-b3b7-0d0a8b1d4ee8";
        String otpToken = "123456";
        Optional<TelephoneEntity> entity = Optional.empty();

        ReflectionTestUtils.setField(service, "validationEnabled", false);

        when(telephoneRepository.findById(any(UUID.class))).thenReturn(entity);

        assertThrows(TelephoneOTPException.class, () -> service.sendOtp(telephoneId, otpToken));
    }

    @Test
    @DisplayName("Validate OTP Code")
    void validateOtp() {
        String telephoneId = "6031dff8-2cd4-4dc7-b3b7-0d0a8b1d4ee8";
        String otpToken = "123456";

        when(otpRepository.existsByTelephoneIdAndOtpToken(any(UUID.class), anyString())).thenReturn(true);
        doNothing().when(otpRepository).deleteById(any(UUID.class));

        assertTrue(service.validateOtp(telephoneId, otpToken));
    }

    @Test
    @DisplayName("Invalid OTP Code")
    void invalidOtp() {
        String telephoneId = "6031dff8-2cd4-4dc7-b3b7-0d0a8b1d4ee8";
        String otpToken = "123456";

        when(otpRepository.existsByTelephoneIdAndOtpToken(any(UUID.class), anyString())).thenReturn(false);

        assertFalse(service.validateOtp(telephoneId, otpToken));
    }
}