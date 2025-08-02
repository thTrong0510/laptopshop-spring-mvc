package com.laptopshop.laptopshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LaptopshopApplication {

	public static void main(String[] args) {
		// khi chạy câu lệnh này thì nó sẽ trả về cái container -> mình có thể hứng để
		// xem các beans nó quản lý
		SpringApplication.run(LaptopshopApplication.class, args);

		/*
		 * ApplicationContext laptopshop =
		 * SpringApplication.run(LaptopshopApplication.class, args);
		 * for (String s : laptopshop.getBeanDefinitionNames()) {
		 * System.out.println(s);
		 * }
		 */
	}

}
