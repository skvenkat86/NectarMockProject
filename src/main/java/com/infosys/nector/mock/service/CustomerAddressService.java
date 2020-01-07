package com.infosys.nector.mock.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infosys.nector.mock.model.CustomerAddress;
import com.infosys.nector.mock.repository.CustomerAddressRepository;
import com.infosys.nector.mock.repository.CustomerRepository;

@Service
public class CustomerAddressService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final LocalDate defaultValidTo = LocalDate.of(9999, 12, 31);
	
	@Autowired
	CustomerAddressRepository customerAddrRepo;
	
	@Autowired
	CustomerRepository customerRepo;
	
	public boolean createCustomerAddress(CustomerAddress customerAddr) {
		logger.info("Creation request for customer {}", customerAddr);
		customerAddr.setCustomerAddressId(null);
		customerAddr.setLastUpdatedDate(new Date());
		if(customerAddr.getValidFr() == null) {
			customerAddr.setValidFr(new Date());
		}
		if(customerAddr.getValidTo() == null) {
			customerAddr.setValidTo(java.sql.Date.valueOf(defaultValidTo));
		}
		customerAddrRepo.save(customerAddr);
		return true;
	}
	
	public CustomerAddress getCustomerAddressById(long id) {
		CustomerAddress obj = null; 
		Optional<CustomerAddress> optObj = customerAddrRepo.findById(id);
		 if(optObj.isPresent()) {
			 obj = optObj.get();
		 }
		return obj;
	}
	
	public List<CustomerAddress> getCustomerAddressListByCustomerId(long id) {
		List<CustomerAddress> custAddrList = customerAddrRepo.findByCustomerIdOrderByValidFr(id);
		return custAddrList;
	}

	public List<CustomerAddress> getActiveCustomerAddressListByCustomerId(long id, Date applicableDate) {
		if(applicableDate == null) {
			applicableDate = new Date();
		}
		List<CustomerAddress> custAddrList = customerAddrRepo.getActiveCustomerAddress(id, applicableDate);
		return custAddrList;
	}

	
	public List<CustomerAddress> getAllCustomerAddress(){
		return customerAddrRepo.findAll();
	}
	
	
	public void updateCustomerAddress(CustomerAddress customerAddr) {
		customerAddr.setLastUpdatedDate(new Date());
		customerAddrRepo.save(customerAddr);
	}
	
	public void deleteCustomerAddressByID(Long CustomerAddressId) {
		customerAddrRepo.deleteById(CustomerAddressId);
	}
	
}
