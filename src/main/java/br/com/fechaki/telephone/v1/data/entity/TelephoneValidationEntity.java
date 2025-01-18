package br.com.fechaki.telephone.v1.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_telephone_validation")
@EqualsAndHashCode(of = {"telephoneId", "internationalFormat", "created"})
public class TelephoneValidationEntity {
    @Id
    private UUID telephoneId;
    private boolean valid;
    private String phoneNumber;
    private String localFormat;
    private String internationalFormat;
    private String countryPrefix;
    private String countryCode;
    private String countryName;
    private String location;
    private String carrier;
    private String lineType;
    private LocalDateTime created = LocalDateTime.now();
}
