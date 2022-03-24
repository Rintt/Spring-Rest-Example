package com.example.hellojpa.domain;

import com.example.hellojpa.domain.DTO.CustomerApiDTO;

public interface CustomerMapper {
    Customer map(CustomerApiDTO dto);
    CustomerApiDTO map(Customer customer);
    Customer mapInto(CustomerApiDTO dto, Customer entity);
}
