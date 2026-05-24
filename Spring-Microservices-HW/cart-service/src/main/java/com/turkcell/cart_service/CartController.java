package com.turkcell.cart_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

	@GetMapping("/hello")
	public String hello() {
		return "Hello cart-service";
	}
}
