package com.example.btlsoc.controller;


import com.example.btlsoc.model.Plan;
import com.example.btlsoc.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PlanController {
    @Autowired
    private PlanService planService;

    @GetMapping("plan/{id}")
    public ResponseEntity<Plan> getPlan(@PathVariable(name = "id") int id){
        return new ResponseEntity<>(planService.findById(id), HttpStatus.OK);
    }

    @GetMapping("plan/all")
    public ResponseEntity<List<Plan>> getPlans(){
        return new ResponseEntity<>(planService.findAll(), HttpStatus.OK);
    }

}
