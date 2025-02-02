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
@Table(name = "tbl_telephone_otp")
@EqualsAndHashCode(of = {"telephoneId", "otpToken", "created"})
public class TelephoneOTPEntity {
    @Id
    private UUID telephoneId;
    private boolean validated;
    private String otpToken;
    private LocalDateTime created = LocalDateTime.now();
}
