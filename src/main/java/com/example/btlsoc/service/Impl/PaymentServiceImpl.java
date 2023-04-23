package com.example.btlsoc.service.Impl;

import com.example.btlsoc.model.Payment;
import com.example.btlsoc.repository.PaymentRepository;
import com.example.btlsoc.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }
}
