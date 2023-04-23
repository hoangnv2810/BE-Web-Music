package com.example.btlsoc.service;

import com.example.btlsoc.model.Plan;

import java.util.List;

public interface PlanService {
    List<Plan> findAll();

    Plan findById(int id);
}
