package br.com.fechaki.telephone.v1.data.entity;


import jakarta.persistence.*;
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
@Table(name = "tbl_telephone")
@EqualsAndHashCode(of = {"countryCode", "areaCode", "phoneNumber"})
public class TelephoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID telephoneId;
    private String countryCode;
    private String areaCode;
    private String phoneNumber;
    private Boolean activated;
    private Boolean validated;
    private boolean deleted;
    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime updated = LocalDateTime.now();
}
