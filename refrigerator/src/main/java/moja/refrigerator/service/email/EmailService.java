package moja.refrigerator.service.email;

public interface EmailService {
    void sendTempPassword(String name, String email, String tempPassword);
}
