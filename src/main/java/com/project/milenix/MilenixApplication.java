package com.project.milenix;

import com.project.milenix.authentication_service.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class MilenixApplication {

	public static void main(String[] args) {
		SpringApplication.run(MilenixApplication.class, args);
	}

}
