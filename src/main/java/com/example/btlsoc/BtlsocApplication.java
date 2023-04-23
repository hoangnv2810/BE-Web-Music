package com.example.btlsoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BtlsocApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtlsocApplication.class, args);
	}

}
