package ${packageRoot}.controller;

import ${packageRoot}.entity.${Entity};
import ${packageRoot}.service.${Entity}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
public class ${Entity}Controller {

    @Autowired
    ${Entity}Service ${entity}Service;

    @GetMapping("/${entity}/{id}")
    public List<${Entity}> queryById(@PathVariable Integer id) {
        return ${entity}Service.queryById(id);
    }

    @GetMapping("/${entity}")
    public List<${Entity}> queryAll() {
        return ${entity}Service.queryAll();
    }

    @PostMapping("/${entity}")
    public ${Entity} insert(@Validated ${Entity} ${entity}#foreach(${file} in ${files}), MultipartFile ${file}#end) {
        return ${entity}Service.insert(${entity});
    }

    @PutMapping("/${entity}")
    public ${Entity} update(@Validated ${Entity} ${entity}) {
        return ${entity}Service.update(${entity});
    }

    @DeleteMapping("/${entity}/{id}")
    public boolean delete(@PathVariable Integer id) {
        return ${entity}Service.delete(id);
    }

}
