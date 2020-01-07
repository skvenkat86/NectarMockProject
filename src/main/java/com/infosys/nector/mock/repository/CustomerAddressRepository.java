package com.infosys.nector.mock.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.infosys.nector.mock.model.CustomerAddress;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
	List<CustomerAddress> findByCustomerId(Long customerId);
	
	List<CustomerAddress> findByCustomerIdOrderByValidFr(Long customerId);
	
	@Query(value = "from CustomerAddress t where customerId = :customerId and :applicableDate BETWEEN validFr AND validTo")
	public List<CustomerAddress> getActiveCustomerAddress(@Param("customerId")long customerId,@Param("applicableDate")Date applicableDate);
}
