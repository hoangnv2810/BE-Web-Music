package com.example.btlsoc.controller;

import com.example.btlsoc.model.Order;
import com.example.btlsoc.model.User;
import com.example.btlsoc.security.CustomUserDetailsService;
import com.example.btlsoc.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("order/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable(name = "id") String id){
        Order order = orderService.findByIdString(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("orders")
    public ResponseEntity<?> getOrders(Principal principal){
        User user = (User) customUserDetailsService.loadUserByUsername(principal.getName());
        return new ResponseEntity<>(orderService.findAllOrderByUserId(user.getId()), HttpStatus.OK);
    }

}
