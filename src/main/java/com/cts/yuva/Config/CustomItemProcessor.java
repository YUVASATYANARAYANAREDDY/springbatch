package com.cts.yuva.Config;

import org.springframework.batch.item.ItemProcessor;
import com.cts.yuva.Entity.Customer;

public class CustomItemProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) {
        customer.setEmail(customer.getEmail().toUpperCase());
        return customer;
    }
}
