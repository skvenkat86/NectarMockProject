package com.infosys.nector.mock.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.infosys.nector.mock.UtilClass;
import com.infosys.nector.mock.model.Customer;
import com.infosys.nector.mock.model.CustomerAddress;
import com.infosys.nector.mock.service.CustomerAddressService;
import com.infosys.nector.mock.service.CustomerService;

@CrossOrigin
@RestController
public class CustomerAddressController {

	@Autowired
	CustomerService customerService;

	@Autowired
	CustomerAddressService customerAddrService;

	@GetMapping("customerAddress/{id}")
	public ResponseEntity<?> getCustomerAddressById(@PathVariable("id") Long id) {
		//get Customer Address for the Customer Address ID provided 
		CustomerAddress custAddr = customerAddrService.getCustomerAddressById(id);
		return new ResponseEntity<CustomerAddress>(custAddr, HttpStatus.OK);
	}

	//get all customer address for a given customer ID
	@GetMapping("customerAddressByCustomerId")
	public ResponseEntity<?> getCustomerAddressByCustomerId(@RequestParam("customerId") Long id) {
		Customer customer = customerService.getCustomerById(id);
		if (customer == null) {
			return new ResponseEntity<Object>("Customer Information not available for given Customer ID",
					HttpStatus.BAD_REQUEST);
		}
		List<CustomerAddress> custAddr = customerAddrService.getCustomerAddressListByCustomerId(id);
		return new ResponseEntity<List<CustomerAddress>>(custAddr, HttpStatus.OK);
	}

	@GetMapping("/getAllCustomerAddress")
	public ResponseEntity<List<CustomerAddress>> getAllCustomerAddress(@RequestParam Long customerId) {
		List<CustomerAddress> list = customerAddrService.getCustomerAddressListByCustomerId(customerId);
		return new ResponseEntity<List<CustomerAddress>>(list, HttpStatus.OK);
	}

	//get active customer Address for given Customer ID. If applicableDate is not provided it is defaulted to current date
	@GetMapping("/getActiveCustomerAddress")
	public ResponseEntity<List<CustomerAddress>> getAllCustomerAddress(@RequestParam Long customerId,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date applicableDate) {
		List<CustomerAddress> list = customerAddrService.getActiveCustomerAddressListByCustomerId(customerId,
				applicableDate);
		return new ResponseEntity<List<CustomerAddress>>(list, HttpStatus.OK);
	}

	@PostMapping("customerAddress")
	public ResponseEntity<?> addCustomerAddress(@RequestBody CustomerAddress customerAddr,
			UriComponentsBuilder builder) {
		// check if Customer information is available for the customer ID provided
		Customer customerObj = customerService.getCustomerById(customerAddr.getCustomerId());
		if (customerObj == null) {
			return new ResponseEntity<Object>("Customer Information not available for given Customer ID",
					HttpStatus.BAD_REQUEST);
		}
		// check if Customer Information is already created for the Customer ID
		List<CustomerAddress> list = customerAddrService
				.getCustomerAddressListByCustomerId(customerAddr.getCustomerId());
		if (list != null && list.size() > 0) {
			return new ResponseEntity<Object>(
					"Customer Address Information already available for given Customer ID. Use Update Customer Address Option.",
					HttpStatus.BAD_REQUEST);
		}
		boolean flag = customerAddrService.createCustomerAddress(customerAddr);
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				builder.path("/customerAddress/{id}").buildAndExpand(customerAddr.getCustomerAddressId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@PutMapping("customerAddress")
	public ResponseEntity<?> updateCustomerAddress(@RequestBody CustomerAddress customerAddr) {
		// check if Customer information is available for the customer ID provided
		Customer customerObj = customerService.getCustomerById(customerAddr.getCustomerId());
		if (customerObj == null) {
			return new ResponseEntity<Object>("Customer Information not available for given Customer ID",
					HttpStatus.BAD_REQUEST);
		}
		// get the list of available customer address information
		List<CustomerAddress> list = customerAddrService
				.getCustomerAddressListByCustomerId(customerAddr.getCustomerId());
		if (list == null || list.size() == 0) {
			return new ResponseEntity<Object>(
					"Customer Address Information not available for given Customer ID. Use Create Customer Address Option.",
					HttpStatus.BAD_REQUEST);
		}
		CustomerAddress currentActiveAddr = list.get(list.size() - 1);
		LocalDate lastUpdatedDate = UtilClass.convertToLocalDateViaSqlDate(currentActiveAddr.getLastUpdatedDate());
		LocalDate today = LocalDate.now();
		if (ChronoUnit.DAYS.between(lastUpdatedDate, today) <= 6) {
			return new ResponseEntity<Object>(
					"Customer Address Information has been updated recently wihtin a week for given Customer ID. Update can only be done once in a week.",
					HttpStatus.BAD_REQUEST);
		}

		// make sure if there are already 5 records(should not be more than 5), delete
		// the oldest historical address
		if (list.size() == 5) {
			customerAddrService.deleteCustomerAddressByID(list.get(0).getCustomerAddressId());
		}

		// set validFr and validTo if not provided
		updateValidDates(customerAddr);
		// update the validTo date of the active address based on the validFr date of
		// the new address --> to make sure only one address is active

		LocalDate validFrNewCustAddr = UtilClass.convertToLocalDateViaSqlDate(customerAddr.getValidFr());
		currentActiveAddr.setValidTo(java.sql.Date.valueOf(validFrNewCustAddr.minusDays(1)));
		customerAddrService.updateCustomerAddress(currentActiveAddr);

		customerAddrService.createCustomerAddress(customerAddr);
		return new ResponseEntity<CustomerAddress>(customerAddr, HttpStatus.OK);
	}

	public static void updateValidDates(CustomerAddress customerAddr) {
		customerAddr.setCustomerAddressId(null);
		customerAddr.setLastUpdatedDate(new Date());
		if (customerAddr.getValidFr() == null) {
			customerAddr.setValidFr(new Date());
		}
		if (customerAddr.getValidTo() == null) {
			customerAddr.setValidTo(java.sql.Date.valueOf(CustomerAddressService.defaultValidTo));
		}
	}

//	@PostMapping(path= "/addCustomer", consumes = "application/json", produces = "application/json")
//    public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) 
//    {
//		customerService.createCustomer(customer);
//         
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                                    .path("/{id}")
//                                    .buildAndExpand(customer.getCustomerId())
//                                    .toUri();
//         
//        return ResponseEntity.created(location).build();
//    }

}
