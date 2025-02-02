package br.com.fechaki.telephone.v1.service;

public interface SNSService {
    String generateOtp(String telephoneId);
    void sendOtp(String telephoneId, String otpToken);
    boolean validateOtp(String telephoneId, String otpToken);
}
