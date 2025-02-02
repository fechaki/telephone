package br.com.fechaki.telephone.v1.controller;

import br.com.fechaki.telephone.v1.data.request.TelephoneValidationRequest;
import br.com.fechaki.telephone.v1.service.SNSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/telephone/otp")
public class OTPController {
    private final SNSService snsService;

    @PostMapping("/{telephoneID}")
    public void sendValidation(@PathVariable String telephoneID) {
        String otpToken = snsService.generateOtp(telephoneID);
        snsService.sendOtp(telephoneID, otpToken);
    }

    @PostMapping("/{telephoneID}/validate")
    public ResponseEntity<Boolean> validate(@PathVariable String telephoneID, @RequestBody TelephoneValidationRequest request) {
        boolean response = snsService.validateOtp(telephoneID, request.otpToken());
        if (response) {
            return ResponseEntity.ok(true);
        }
        else {
            return ResponseEntity.badRequest().build();
        }

    }
}
