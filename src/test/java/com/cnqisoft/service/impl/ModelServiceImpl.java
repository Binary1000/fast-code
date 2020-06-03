package com.cnqisoft.service.impl;

import com.cnqisoft.entity.Model;
import com.cnqisoft.mapper.ModelMapper;
import com.cnqisoft.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<Model> queryById(Integer id) {
        return modelMapper.queryById(id);
    }

    @Override
    public List<Model> queryAll() {
        return modelMapper.queryAll();
    }

    @Override
    public Model insert(Model model) {
        modelMapper.insert(model);
        return model;
    }

    @Override
    public boolean delete(Integer id) {
        return modelMapper.delete(id);
    }

    @Override
    public Model update(Model model) {
        modelMapper.update(model);
        return model;
    }
}
