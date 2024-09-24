package br.com.fechaki.telephone.v1.data.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table(name = "tbl_telephone")
@EqualsAndHashCode(of = {"countryCode", "areaCode", "phoneNumber"})
public class TelephoneEntity {
    @Id
    private UUID telephoneId;
    private String countryCode;
    private String areaCode;
    private String phoneNumber;
    private Boolean activated;
    private Boolean validated;
    private boolean deleted;
    private LocalDateTime created;
    private LocalDateTime updated;
}
