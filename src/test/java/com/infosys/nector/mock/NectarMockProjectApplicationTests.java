package com.infosys.nector.mock;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.infosys.nector.mock.model.Customer;
import com.infosys.nector.mock.model.CustomerAddress;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NectarMockProjectApplicationTests {

	@LocalServerPort
    int randomServerPort;
	
//	@Test
//	@Order(1)
//	void contextLoads() {
//	}
	
//	@Test
//	@Order(2)
//	@Transactional
//	public void testAddCustomerSuccess() throws URISyntaxException 
//    {
//        RestTemplate restTemplate = new RestTemplate();
//        final String baseUrl = "http://localhost:"+randomServerPort+"/customer/";
//        URI uri = new URI(baseUrl);
//        Customer customer = new Customer(null, "Rahul", new Long(999435), 'M', new Date(), "Rahul@gmail.com");
//         
//        HttpHeaders headers = new HttpHeaders();
////        headers.set("X-COM-PERSIST", "true");      
// 
//        HttpEntity<Customer> request = new HttpEntity<>(customer, headers);
//         
//        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
//         
//        //Verify request succeed
//        Assertions.assertEquals(201, result.getStatusCodeValue());
//    }
	
	@Test
	@Order(3)
    public void testAddCustomerEmailValidation() throws URISyntaxException 
    {
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:"+randomServerPort+"/customer/";
        URI uri = new URI(baseUrl);
        Customer customer = new Customer(null, "Rahul", new Long(999435), 'M', new Date(), "sid@gmail.com");
         
        HttpHeaders headers = new HttpHeaders();
//        headers.set("X-COM-PERSIST", "true");      
 
        HttpEntity<Customer> request = new HttpEntity<>(customer, headers);
         
        try
        {
            restTemplate.postForEntity(uri, request, String.class);
            Assertions.fail();
        }
        catch(HttpClientErrorException ex) 
        {
            //Verify bad request and missing header
            Assertions.assertEquals(400, ex.getRawStatusCode());
            Assertions.assertEquals(true, ex.getResponseBodyAsString().contains("E-Mail already registered."));
        }
        
    }
	
	@Test
	@Order(4)
    public void testUpdateCustomerCustomerIDValidation() throws URISyntaxException 
    {
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:"+randomServerPort+"/customer/";
        URI uri = new URI(baseUrl);
        Customer customer = new Customer(Long.valueOf(-1), "Rahul", Long.valueOf(999435), 'M', new Date(), "Rahul@gmail.com");
         
        HttpHeaders headers = new HttpHeaders();
//        headers.set("X-COM-PERSIST", "true");      
 
        HttpEntity<Customer> request = new HttpEntity<>(customer, headers);
         
        try
        {
            restTemplate.put(uri, request);
            Assertions.fail();
        }
        catch(HttpClientErrorException ex) 
        {
            //Verify bad request and missing header
            Assertions.assertEquals(400, ex.getRawStatusCode());
            Assertions.assertEquals(true, ex.getResponseBodyAsString().contains("Customer Information not available for given ID"));
        }
        
    }
	
	@Test
	@Order(5)
    public void testCustomerAddressByCustomerIdCustomerIDValidation() throws URISyntaxException 
    {
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:"+randomServerPort+"/customerAddressByCustomerId";
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                // Add query parameter
                .queryParam("customerId", Long.valueOf(-1));
        try
        {
        	restTemplate.getForEntity(builder.toUriString(), String.class);
            Assertions.fail();
        }
        catch(HttpClientErrorException ex) 
        {
            //Verify bad request and missing header
            Assertions.assertEquals(400, ex.getRawStatusCode());
            Assertions.assertEquals(true, ex.getResponseBodyAsString().contains("Customer Information not available for given Customer ID"));
        }
    }
	
	@Test
	@Order(6)
    public void testAddCustomerAddressCustomerIDValidation() throws URISyntaxException 
    {
		RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:"+randomServerPort+"/customerAddress/";
        URI uri = new URI(baseUrl);
        CustomerAddress customerAddr = new CustomerAddress(Long.valueOf(-1),Long.valueOf(-1), "DoorNo","FlatNo","StreetNo","AddrLine1","AddrLine2", "City", "PostalCode", new Date(), null, null);
         
        HttpHeaders headers = new HttpHeaders();
 
        HttpEntity<CustomerAddress> request = new HttpEntity<>(customerAddr, headers);
         
        try
        {
            restTemplate.put(uri, request);
            Assertions.fail();
        }
        catch(HttpClientErrorException ex) 
        {
            //Verify bad request and missing header
            Assertions.assertEquals(400, ex.getRawStatusCode());
            Assertions.assertEquals(true, ex.getResponseBodyAsString().contains("Customer Information not available for given Customer ID"));
        }
         
    }
	
	@Test
	@Order(6)
    public void testAddCustomerAddressAddrAlreadyCreatedValidation() throws URISyntaxException 
    {
		RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = "http://localhost:"+randomServerPort+"/customerAddress/";
        URI uri = new URI(baseUrl);
        CustomerAddress customerAddr = new CustomerAddress(null,Long.valueOf(2), "DoorNo","FlatNo","StreetNo","AddrLine1","AddrLine2", "City", "PostalCode", new Date(), null, null);
         
        HttpHeaders headers = new HttpHeaders();
 
        HttpEntity<CustomerAddress> request = new HttpEntity<>(customerAddr, headers);
         
        try
        {
        	restTemplate.postForEntity(uri, request, String.class);
            Assertions.fail();
        }
        catch(HttpClientErrorException ex) 
        {
            //Verify bad request and missing header
            Assertions.assertEquals(400, ex.getRawStatusCode());
            Assertions.assertEquals(true, ex.getResponseBodyAsString().contains("Customer Address Information already available for given Customer ID. Use Update Customer Address Option."));
        }
         
    }



}
