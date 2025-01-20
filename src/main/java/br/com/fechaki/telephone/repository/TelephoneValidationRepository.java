package br.com.fechaki.telephone.repository;

import br.com.fechaki.telephone.v1.data.entity.TelephoneValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TelephoneValidationRepository extends JpaRepository<TelephoneValidationEntity, UUID> {
}
