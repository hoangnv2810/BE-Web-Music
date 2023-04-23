package com.example.btlsoc.service.Impl;

import com.example.btlsoc.model.Plan;
import com.example.btlsoc.repository.PlanRepository;
import com.example.btlsoc.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanRepository planRepository;

    @Override
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    @Override
    public Plan findById(int id) {
        return planRepository.findById(id).get();
    }
}
