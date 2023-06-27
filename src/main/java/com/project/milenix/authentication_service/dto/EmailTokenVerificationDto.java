package com.project.milenix.authentication_service.dto;

import com.project.milenix.user_service.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class EmailTokenVerificationDto {
    @SequenceGenerator(name = "email_verification_token_request", sequenceName = "email_verification_token_request")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_verification_token_request")
    @Id
    private Integer id;
    private String expectedToken;
    private String actualToken;
    private Date issueDate;
    private Date expirationDate;
    private Date verificationDate;
    @ManyToOne
    private User user;
}
