package com.javatechie.spring.paypal.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

//import net.guides.springboot2.springboot2jpacrudexample.model.Employee;

//import net.guides.springboot2.springboot2jpacrudexample.model.Employee;
//import net.guides.springboot2.springboot2jpacrudexample.repository.EmployeeRepository;

import com.javatechie.spring.paypal.api.*;
@RestController
@Controller
public class PaypalController {

	@Autowired
	PaypalService service;
	@Autowired
	private OrderRepository orderRepository;

	public static final String SUCCESS_URL = "pay/success";
	public static final String CANCEL_URL = "pay/cancel";

	@GetMapping("/")
	public String home() {
		return "home";
	}
	/*@PostMapping("/ord")
	public Order createEmployee(@Valid @RequestBody Order order) {
		  System.out.println(" order amount "+order.getPrice());
		  
		return orderRepository.save(order);
	}
	*/
	@GetMapping("/allorders")
	public List<Order> getAllEmployees() {
		return orderRepository.findAll();
	}
	/*
	 
	 @GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	 @PostMapping("/employees")
	public Employee createEmployee(@Valid @RequestBody Employee employee) {
		return employeeRepository.save(employee);
	}
	 */
	@PostMapping("/pay")
	public String payment(@Valid @RequestBody Order order) {
		try {
			orderRepository.save(order);
			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), "http://localhost:9090/" + CANCEL_URL,
					"http://localhost:9090/" + SUCCESS_URL);
			for(Links link:payment.getLinks()) {
				if(link.getRel().equals("approval_url")) {
					return "redirect:"+link.getHref();
				}
			}
			
		} catch (PayPalRESTException e) {
		
			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	 @GetMapping(value = CANCEL_URL)
	    public String cancelPay() {
	        return "cancel";
	    }

	    @GetMapping(value = SUCCESS_URL)
	    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
	        try {
	        	
	            Payment payment = service.executePayment(paymentId, payerId);
	            System.out.println(payment.toJSON());
	            if (payment.getState().equals("approved")) {
	                return "success";
	            }
	        } catch (PayPalRESTException e) {
	         System.out.println(e.getMessage());
	        }
	        return "redirect:/";
	    }

}
