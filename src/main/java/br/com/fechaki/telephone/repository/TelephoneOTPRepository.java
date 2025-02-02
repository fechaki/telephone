package br.com.fechaki.telephone.repository;

import br.com.fechaki.telephone.v1.data.entity.TelephoneOTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TelephoneOTPRepository extends JpaRepository<TelephoneOTPEntity, UUID> {
    boolean existsByTelephoneIdAndOtpToken(UUID telephoneId, String otpToken);
}
