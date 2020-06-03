package com.cnqisoft.service;

import com.cnqisoft.entity.Model;

import java.util.List;

public interface ModelService {

    List<Model> queryById(Integer id);

    List<Model> queryAll();

    Model insert(Model model);

    boolean delete(Integer id);

    Model update(Model model);
}
