package com.cnqisoft.mapper;

import com.cnqisoft.entity.Model;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelMapper {

    List<Model> queryById(Integer id);

    List<Model> queryAll();

    int insert(Model model);

    boolean delete(Integer id);

    int update(Model model);

}
