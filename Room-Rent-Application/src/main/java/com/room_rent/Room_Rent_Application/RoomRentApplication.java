package com.room_rent.Room_Rent_Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")

public class RoomRentApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomRentApplication.class, args);
	}

}
