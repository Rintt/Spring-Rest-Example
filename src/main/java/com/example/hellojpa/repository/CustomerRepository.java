package com.example.hellojpa.repository;

import com.example.hellojpa.domain.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID>/*, NaturalCustomerRepository */
{
    <T> Optional<T> findByCustomerNbr(String customerNbr, Class<T> type);
    List<Customer> findByLastname(String lastname);
    @Query("FROM Customer c WHERE c.lastname = :lastname")
    List<Customer> findCustomerByLastnamePaged(@Param("lastname") String lastname, Pageable pageable);
    <T> List<T> findAllProjectedBy(Class<T> type);


}