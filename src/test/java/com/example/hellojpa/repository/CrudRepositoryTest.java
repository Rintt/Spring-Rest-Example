package com.example.hellojpa.repository;

import com.example.hellojpa.domain.Customer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
class CrudRepositoryTest {
    public static List<Customer> createManyCustomers(int count) {
        var manyOrders = new ArrayList<Customer>();
        for (int i = 1; i < count + 1; i++) {
            var customer = Customer.builder()
                    .customerNbr("C%03d".formatted(i))
                    .firstname("Test")
                    .lastname("Mensch")
                    .build();
            manyOrders.add(customer);
        }
        return manyOrders;
    }

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void basicCrudTest() {
        var customer = Customer.builder()
                .customerNbr("C001")
                .firstname("Test")
                .lastname("Mensch")
                .build();
        customer = customerRepository.saveAndFlush(customer);
        var exists = customerRepository.existsById(customer.getId());
        Assertions.assertTrue(exists);
        var reloadedOrder = customerRepository.findById(customer.getId());
        Assertions.assertTrue(reloadedOrder.isPresent());
        var count = customerRepository.count();
        Assertions.assertEquals(1, count);
        customerRepository.deleteById(customer.getId());
        exists = customerRepository.existsById(customer.getId());
        Assertions.assertFalse(exists);
    }
    @Test
    void queryByExampleMatcherTest() {
        customerRepository.saveAll(createManyCustomers(100));
        Customer example = Customer.builder()
                .customerNbr("C003").build();
        var customers = customerRepository.findAll(Example.of(example));
        Assertions.assertEquals(1, customers.size());
    }
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void methodQueryCreationTest() {
        customerRepository.saveAll(createManyCustomers(10));
        var customers = customerRepository.findByLastname("Mensch");
        Assertions.assertEquals(10, customers.size());
    }
    @Test
    void queryWithPagingTest() {
        customerRepository.saveAllAndFlush(createManyCustomers(20));
        var customers =
                customerRepository.findCustomerByLastnamePaged("Mensch",
                        PageRequest.of(0, 5, Sort.by("customerNbr")));
        Assertions.assertEquals(5, customers.size());
    }
}