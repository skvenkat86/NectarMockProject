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
public class CustomerAddress {

	public CustomerAddress() {
	}
	public CustomerAddress(Long customerAddressId, Long customerId, String doorNo, String flatNo, String streetNo,
			String addrLine1, String addrLine2, String city, String postalCode, Date validFr, Date validTo,
			Date lastUpdatedDate) {
		super();
		this.customerAddressId = customerAddressId;
		this.customerId = customerId;
		this.doorNo = doorNo;
		this.flatNo = flatNo;
		this.streetNo = streetNo;
		this.addrLine1 = addrLine1;
		this.addrLine2 = addrLine2;
		this.city = city;
		this.postalCode = postalCode;
		this.validFr = validFr;
		this.validTo = validTo;
		this.lastUpdatedDate = lastUpdatedDate;
	}
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	Long customerAddressId;
	
	@Column(nullable = false)
	Long customerId;
	
	@Column(nullable = true, length = 50)
	String doorNo;
	@Column(nullable = true, length = 50)
	String flatNo;
	@Column(nullable = false, length = 50)
	String streetNo;
	
	@Column(nullable = false, length = 50)
	String addrLine1;
	@Column(nullable = true, length = 50)
	String addrLine2;
	
	@Column(nullable = false, length = 50)
	String city;
	@Column(nullable = false, length = 50)
	String postalCode;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	Date validFr;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	Date validTo;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	Date lastUpdatedDate;
	
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getDoorNo() {
		return doorNo;
	}
	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}
	public String getFlatNo() {
		return flatNo;
	}
	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}
	public String getStreetNo() {
		return streetNo;
	}
	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}
	public String getAddrLine1() {
		return addrLine1;
	}
	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}
	public String getAddrLine2() {
		return addrLine2;
	}
	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public Long getCustomerAddressId() {
		return customerAddressId;
	}
	public void setCustomerAddressId(Long customerAddressId) {
		this.customerAddressId = customerAddressId;
	}
	public Date getValidFr() {
		return validFr;
	}
	public void setValidFr(Date validFr) {
		this.validFr = validFr;
	}
	public Date getValidTo() {
		return validTo;
	}
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
}
