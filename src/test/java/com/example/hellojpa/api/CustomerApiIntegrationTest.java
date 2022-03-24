package com.example.hellojpa.api;

import com.example.hellojpa.domain.DTO.CustomerApiDTO;
import com.example.hellojpa.repository.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerApiIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    CustomerRepository customerRepository;
    @Test
    void testSave() {
        var customer = new CustomerApiDTO("C001", "Test", "Mensch");
// TestRestTemplate knows about the random port!
        var entity = restTemplate.postForEntity("/api/customer", customer, Void.class);
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
