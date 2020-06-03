package ${packageRoot}.service.impl;

import ${packageRoot}.entity.${Entity};
import ${packageRoot}.mapper.${Entity}Mapper;
import ${packageRoot}.service.${Entity}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ${Entity}ServiceImpl implements ${Entity}Service {

    @Autowired
    ${Entity}Mapper ${entity}Mapper;

    @Override
    public List<${Entity}> queryById(Integer id) {
        return ${entity}Mapper.queryById(id);
    }

    @Override
    public List<${Entity}> queryAll() {
        return ${entity}Mapper.queryAll();
    }

    @Override
    public ${Entity} insert(${Entity} ${entity}) {
        ${entity}Mapper.insert(${entity});
        return ${entity};
    }

    @Override
    public boolean delete(Integer id) {
        return ${entity}Mapper.delete(id);
    }

    @Override
    public ${Entity} update(${Entity} ${entity}) {
        ${entity}Mapper.update(${entity});
        return ${entity};
    }
}
