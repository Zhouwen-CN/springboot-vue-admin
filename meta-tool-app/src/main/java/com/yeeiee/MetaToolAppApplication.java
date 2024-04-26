package com.yeeiee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan
public class MetaToolAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetaToolAppApplication.class, args);
	}

}
