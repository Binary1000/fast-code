package com.cnqisoft.controller;

import com.cnqisoft.entity.Model;
import com.cnqisoft.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
public class ModelController {

    @Autowired
    ModelService modelService;

    @GetMapping("/model/{id}")
    public List<Model> queryById(@PathVariable Integer id) {
        return modelService.queryById(id);
    }

    @GetMapping("/model")
    public List<Model> queryAll() {
        return modelService.queryAll();
    }

    @PostMapping("/model")
    public Model insert(@Validated Model model, MultipartFile modelFile) {
        return modelService.insert(model);
    }

    @PutMapping("/model")
    public Model update(@Validated Model model) {
        return modelService.update(model);
    }

    @DeleteMapping("/model/{id}")
    public boolean delete(@PathVariable Integer id) {
        return modelService.delete(id);
    }

}
