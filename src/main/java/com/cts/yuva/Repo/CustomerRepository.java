package com.cts.yuva.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.yuva.Entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
