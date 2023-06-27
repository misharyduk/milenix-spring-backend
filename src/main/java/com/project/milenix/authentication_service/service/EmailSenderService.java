package com.project.milenix.authentication_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class EmailSenderService {
    private final JavaMailSender mailSender;

    public void sendEmail(String username, String toEmail, String token, String sourceUrl) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.addHeader("Content-type", "text/html; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");

            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom("noreply@milenix");
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("Email verification");

            String htmlMessage = getHtmlOfVerification(username, token, sourceUrl);
            messageHelper.setText(htmlMessage, true);

            CompletableFuture.runAsync(() -> {
                mailSender.send(message);
                System.out.println("Mail successfully send");
            });
        } catch (MessagingException e){
            System.err.println("Cannot send message");
        }

    }

    private String getHtmlOfVerification(String username, String token, String sourceUrl) {
        return String.format(
                "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'>" +
                        "<html xmlns='http://www.w3.org/1999/xhtml'>" +
                        "" +
                        "<head>" +
                        "  <meta http-equiv='Content-Type' content='text/html; charset=utf-8'>" +
                        "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "  <title>Please activate your account</title>" +
                        "  <!--[if mso]><style type='text/css'>body, table, td, a { font-family: Arial, Helvetica, sans-serif !important; }</style><![endif]-->" +
                        "</head>" +
                        "" +
                        "<body style='font-family: Helvetica, Arial, sans-serif; margin: 0px; padding: 0px; background-color: #ffffff;'>" +
                        "  <table role='presentation'" +
                        "    style='width: 100%%; border-collapse: collapse; border: 0px none; border-spacing: 0px; font-family: Arial, Helvetica, sans-serif; background-color: rgb(239, 239, 239);'>" +
                        "    <tbody>" +
                        "      <tr>" +
                        "        <td style='padding: 1rem 2rem; vertical-align: top; width: 100%%;' align='center'>" +
                        "          <table role='presentation'" +
                        "            style='max-width: 600px; border-collapse: collapse; border: 0px none; border-spacing: 0px; text-align: left;'>" +
                        "            <tbody>" +
                        "              <tr>" +
                        "                <td style='padding: 40px 0px 0px;'>" +
                        "                  <div style='text-align: left;'>" +
                        "                    <div style='padding-bottom: 20px;'><img src='https://i.ibb.co/Qbnj4mz/logo.png' alt='Company' style='width: 56px;'></div>" +
                        "                  </div>" +
                        "                  <div style='padding: 20px; background-color: rgb(255, 255, 255);'>" +
                        "                    <div style='color: rgb(0, 0, 0); text-align: left;'>" +
                        "                      <h1 style='margin: 1rem 0'>Final step...</h1>" +
                        "                      <p style='padding-bottom: 16px'>%s, follow this link to verify your email address.</p>" +
                        "                      <p style='padding-bottom: 16px'><a href='%s/api/v1/auth/mail/verification?token=%s' target='_blank'" +
                        "                          style='padding: 12px 24px; border-radius: 4px; color: #FFF; background: #2B52F5;display: inline-block;margin: 0.5rem 0;'>Confirm" +
                        "                          now</a></p>" +
                        "                      <p style='padding-bottom: 16px'>If you didn’t ask to verify this address, you can ignore this email.</p>" +
                        "                      <p style='padding-bottom: 16px'>Thanks,<br><a href='%s/home' target='_blank'>Milenix</a> Team</p>" +
                        "                    </div>" +
                        "                  </div>" +
                        "                  <div style='padding-top: 20px; color: rgb(153, 153, 153); text-align: center;'>" +
                        "                    <p style='padding-bottom: 16px'>Made with ♥</p>" +
                        "                  </div>" +
                        "                </td>" +
                        "              </tr>" +
                        "            </tbody>" +
                        "          </table>" +
                        "        </td>" +
                        "      </tr>" +
                        "    </tbody>" +
                        "  </table>" +
                        "</body>" +
                        "" +
                        "</html>", username, sourceUrl, token, sourceUrl);
    }
}
