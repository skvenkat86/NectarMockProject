package com.infosys.nector.mock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infosys.nector.mock.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findByEmail(String email);
	
}
