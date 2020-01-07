package com.infosys.nector.mock.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infosys.nector.mock.model.Customer;
import com.infosys.nector.mock.repository.CustomerRepository;

@Service
public class CustomerService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CustomerRepository customerRepo;
	
	public boolean createCustomer(Customer customer) {
		logger.info("Creation request for customer {}", customer);
		customer.setCustomerId(null);
		customerRepo.save(customer);
		return true;
	}
	
	public Customer getCustomerById(long id) {
		Customer obj = null; 
		Optional<Customer> optObj = customerRepo.findById(id);
		 if(optObj.isPresent()) {
			 obj = optObj.get();
		 }
		return obj;
	}
	
	public Customer getCustomerByEmail(String email) {
		Customer customer = null;
		List<Customer> list = customerRepo.findByEmail(email);
		if(list != null && list.size() > 0) {
			customer = list.get(0);
		}
		return customer;
	}
	
	public List<Customer> getAllCustomers(){
		return customerRepo.findAll();
	}
	
	
	public void updateCustomer(Customer customer) {
		customerRepo.save(customer);
	}
	
}
