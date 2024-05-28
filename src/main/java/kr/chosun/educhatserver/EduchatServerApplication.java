package kr.chosun.educhatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EduchatServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduchatServerApplication.class, args);
	}

}
