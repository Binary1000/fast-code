package com.cnqisoft.controller;

import com.cnqisoft.entity.Model;
import com.cnqisoft.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ModelController {

    @Autowired
    ModelService modelService;

    @GetMapping("/model/{id}")
    List<Model> queryById(@PathVariable Integer id) {
        return modelService.queryById(id);
    }

    @GetMapping("/model")
    List<Model> queryAll() {
        return modelService.queryAll();
    }

    @PostMapping("/model")
    Model insert(Model model) {
        return modelService.insert(model);
    }

    @DeleteMapping("/model/{id}")
    boolean delete(@PathVariable Integer id) {
        return modelService.delete(id);
    }

    @PutMapping("/model")
    Model update(Model model) {
        return modelService.update(model);
    }

}
