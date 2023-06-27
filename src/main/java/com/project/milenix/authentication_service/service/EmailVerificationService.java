package com.project.milenix.authentication_service.service;

import com.project.milenix.authentication_service.dao.EmailVerificationDao;
import com.project.milenix.authentication_service.dto.EmailTokenVerificationDto;
import com.project.milenix.user_service.exception.CustomUserException;
import com.project.milenix.user_service.user.model.User;
import com.project.milenix.user_service.user.repo.UserRepository;
import com.project.milenix.user_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class EmailVerificationService {

    private final EmailVerificationDao emailVerificationDao;
    private final UserRepository userRepository;

    public EmailTokenVerificationDto generateToken(Integer userId) throws CustomUserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomUserException("Cannot find user"));
        return EmailTokenVerificationDto.builder()
                .user(user)
                .expectedToken(UUID.randomUUID().toString())
                .issueDate(new Date())
                .expirationDate(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(6)))
                .build();
    }

    public void saveToken(EmailTokenVerificationDto tokenDto){
        emailVerificationDao.save(tokenDto);
    }

    public boolean verifyToken(String actualToken){
        Optional<EmailTokenVerificationDto> tokenEntityOpt = emailVerificationDao.findByExpectedToken(actualToken);
        if(tokenEntityOpt.isEmpty()){
            System.err.println("No such token");
            return false;
        }
        EmailTokenVerificationDto tokenEntity = tokenEntityOpt.get();
        boolean isTokenExpired = tokenEntity.getExpirationDate().before(new Date());
        if(isTokenExpired){
            System.err.println("Token is expired");
            return false;
        }
        tokenEntity.setActualToken(actualToken);
        tokenEntity.setVerificationDate(new Date());
        User user = tokenEntity.getUser();
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        userRepository.save(user);
        emailVerificationDao.save(tokenEntity);
        return true;
    }
}
