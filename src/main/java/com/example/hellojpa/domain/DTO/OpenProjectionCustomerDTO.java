package com.example.hellojpa.domain.DTO;

import org.springframework.beans.factory.annotation.Value;

public interface OpenProjectionCustomerDTO {
    String getCustomerNumber();
    @Value("#{target.firstname+', '+target.lastname}")
    String getFullname();
}