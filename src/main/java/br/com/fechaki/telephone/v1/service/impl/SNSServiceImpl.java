package br.com.fechaki.telephone.v1.service.impl;

import br.com.fechaki.telephone.exception.type.TelephoneOTPException;
import br.com.fechaki.telephone.repository.TelephoneOTPRepository;
import br.com.fechaki.telephone.repository.TelephoneRepository;
import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import br.com.fechaki.telephone.v1.data.entity.TelephoneOTPEntity;
import br.com.fechaki.telephone.v1.service.SNSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class SNSServiceImpl implements SNSService {
    private final TelephoneRepository telephoneRepository;
    private final TelephoneOTPRepository otpRepository;

    @Value("${fechaki.telephone.validation.enabled}")
    private boolean validationEnabled;

    private final SnsClient snsClient;
    private final SecureRandom random;

    public SNSServiceImpl(SnsClient snsClient, TelephoneRepository telephoneRepository, TelephoneOTPRepository otpRepository) {
        this.snsClient = snsClient;
        this.random = new SecureRandom();
        this.telephoneRepository = telephoneRepository;
        this.otpRepository = otpRepository;
    }

    @Override
    public String generateOtp(String telephoneId) {
        String otpToken = String.valueOf(100000 + random.nextInt(900000));
        TelephoneOTPEntity entity = new TelephoneOTPEntity();
        entity.setOtpToken(otpToken);
        entity.setTelephoneId(UUID.fromString(telephoneId));
        otpRepository.save(entity);
        log.info("OTP generated for {}: {}", telephoneId, otpToken);
        return otpToken;
    }

    @Override
    public void sendOtp(String telephoneId, String otpToken) {
        Optional<TelephoneEntity> entity = telephoneRepository.findById(UUID.fromString(telephoneId));
        if (entity.isPresent()) {
            String message = "Seu código de Validação Fechaki é: " + otpToken;
            String phoneNumber = buildPhoneNumber(entity.get());

            if(validationEnabled) {
                PublishRequest request = PublishRequest.builder()
                        .message(message)
                        .phoneNumber(phoneNumber)
                        .build();

                PublishResponse response = snsClient.publish(request);
                log.info("SNS Publish response: {}", response);
            }
        }
        else {
            throw new TelephoneOTPException(telephoneId);
        }

    }

    @Override
    public boolean validateOtp(String telephoneId, String otpToken) {
        boolean result = otpRepository.existsByTelephoneIdAndOtpToken(UUID.fromString(telephoneId), otpToken);
        if(result) {
            otpRepository.deleteById(UUID.fromString(telephoneId));
        }
        log.info("OTP validated for {}: {} ({})", telephoneId, otpToken, result);
        return result;
    }

    private String buildPhoneNumber(TelephoneEntity entity) {
        return "+" + entity.getCountryCode() + entity.getAreaCode() + entity.getPhoneNumber();
    }
}
