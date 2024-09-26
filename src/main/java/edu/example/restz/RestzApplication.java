package edu.example.restz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = { "edu.example.restz",
							    "edu.example.sample" })
@EnableJpaAuditing		//엔티티 시간 자동 처리 설정
public class RestzApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestzApplication.class, args);
	}
}
