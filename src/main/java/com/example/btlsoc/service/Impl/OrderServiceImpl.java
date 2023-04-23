package com.example.btlsoc.service.Impl;

import com.example.btlsoc.model.Order;
import com.example.btlsoc.repository.OrderRepository;
import com.example.btlsoc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public Order findByIdString(String id) {
        return orderRepository.findByIdString(id).get();
    }

    @Override
    public Order findOrder(int id) {
        return orderRepository.findOrder(id).get();
    }

    @Override
    public Optional<List<Order>> findAllOrderByUserId(int user_id) {
        return orderRepository.findAllOrderByUserId(user_id);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
