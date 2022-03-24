package com.example.hellojpa.repository;

import com.example.hellojpa.domain.Customer;

import java.util.Optional;

public interface NaturalCustomerRepository {
    Optional<Customer> findByNaturalId(String naturalId);
}