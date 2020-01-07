package com.infosys.nector.mock.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class Customer {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	Long customerId;
	
	@Column(nullable = false, length = 50)
	String name;
	@Column(nullable = false)
	Long mobileNo;
	@Column(nullable = false, length = 1)
	char gender;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	Date dob;
	@Column(nullable = false, length = 50)
	String email;
	

	public Customer(Long customerId, String name, Long mobileNo, char gender, Date dob, String email) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.mobileNo = mobileNo;
		this.gender = gender;
		this.dob = dob;
		this.email = email;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Customer() {
		super();
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

