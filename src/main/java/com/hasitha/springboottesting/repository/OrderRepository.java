package com.hasitha.springboottesting.repository;

import com.hasitha.springboottesting.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
