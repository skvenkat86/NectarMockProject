package com.infosys.nector.mock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.infosys.nector.mock.model.Customer;
import com.infosys.nector.mock.service.CustomerService;

@CrossOrigin
@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@GetMapping("customer/{id}")
	public ResponseEntity<?> getCustomerById(@PathVariable("id") Long id) {
		Customer customer = customerService.getCustomerById(id);
		if (customer == null) {
			return new ResponseEntity<Object>("Customer Information not available for given ID",
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	@GetMapping("customerByEmail")
	public ResponseEntity<?> getCustomerByEmail(@RequestParam("email") String email) {
		Customer customer = customerService.getCustomerByEmail(email);
		if (customer == null) {
			return new ResponseEntity<Object>("Customer Information not available for given email",
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}

	@GetMapping("/getAllCustomers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> list = customerService.getAllCustomers();
		return new ResponseEntity<List<Customer>>(list, HttpStatus.OK);
	}

	@PostMapping("customer")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer, UriComponentsBuilder builder) {
		//check if EMail provided is already registered for another customer
		Customer existingCustomer = customerService.getCustomerByEmail(customer.getEmail());
		if (existingCustomer != null) {
			return new ResponseEntity<Object>("E-Mail already registered.", HttpStatus.BAD_REQUEST);
		}
		boolean flag = customerService.createCustomer(customer);
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/customer/{id}").buildAndExpand(customer.getCustomerId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@PutMapping("customer")
	public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
		//check if customer information is available for the given customer ID
		Customer existingCustomer = customerService.getCustomerById(customer.getCustomerId());
		if (existingCustomer == null) {
			return new ResponseEntity<Object>("Customer Information not available for given ID",
					HttpStatus.BAD_REQUEST);
		}

		customerService.updateCustomer(customer);
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}

}
