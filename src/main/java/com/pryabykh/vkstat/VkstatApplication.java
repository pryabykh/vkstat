package com.pryabykh.vkstat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class VkstatApplication {

	public static void main(String[] args) {
		SpringApplication.run(VkstatApplication.class, args);
	}

}
