package com.example.hellojpa.implementation;

import com.example.hellojpa.domain.Customer;
import com.example.hellojpa.repository.NaturalCustomerRepository;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Transactional(readOnly = true)
public class NaturalCustomerRepositoryImpl implements NaturalCustomerRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Optional<Customer> findByNaturalId(String customerNbr) {
        Optional<Customer> entity = entityManager.unwrap(Session.class)
                .bySimpleNaturalId(Customer.class)
                .loadOptional(customerNbr);
        return entity;
    }
}
