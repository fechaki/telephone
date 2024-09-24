package br.com.fechaki.telephone.repository;

import br.com.fechaki.telephone.v1.data.entity.TelephoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TelephoneRepository extends JpaRepository<TelephoneEntity, UUID> {
    TelephoneEntity findFirstByTelephoneIdAndDeletedFalse(UUID telephoneId);
    TelephoneEntity findFirstByCountryCodeAndAreaCodeAndPhoneNumberAndDeletedFalse(String countryCode, String areaCode, String phoneNumber);
}