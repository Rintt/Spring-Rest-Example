package com.example.hellojpa.implementation;

import com.example.hellojpa.domain.Customer;
import com.example.hellojpa.domain.CustomerMapper;
import com.example.hellojpa.domain.DTO.CustomerApiDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapperImpl implements CustomerMapper {
    @Override
    public Customer map(CustomerApiDTO dto) {
        return Customer.builder()
                .customerNbr(dto.customerNbr())
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .build();
    }
    @Override
    public CustomerApiDTO map(Customer customer) {
        return new CustomerApiDTO(
                customer.getCustomerNbr(),
                customer.getFirstname(),
                customer.getLastname()
        );
    }
    @Override
    public Customer mapInto(CustomerApiDTO dto, Customer entity) {
        entity.setFirstname(dto.firstname());
        entity.setLastname(dto.lastname());
        return entity;
    }
}