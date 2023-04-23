package com.example.btlsoc.service;


import com.example.btlsoc.model.Order;

import java.util.List;
import java.util.Optional;


public interface OrderService {
    Order findOrder(int id);

    Order findByIdString(String id);
    Optional<List<Order>> findAllOrderByUserId(int user_id);

    Order save(Order order);
}
