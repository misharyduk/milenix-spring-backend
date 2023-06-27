package com.project.milenix.authentication_service.dao;

import com.project.milenix.authentication_service.dto.EmailTokenVerificationDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationDao extends JpaRepository<EmailTokenVerificationDto, Integer> {

    Optional<EmailTokenVerificationDto> findByExpectedToken(String expectedToken);

}
