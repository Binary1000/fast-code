package ${packageRoot}.mapper;

import ${packageRoot}.entity.${Entity};
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ${Entity}Mapper {

    List<${Entity}> queryById(Integer id);

    List<${Entity}> queryAll();

    int insert(${Entity} ${entity});

    boolean delete(Integer id);

    int update(${Entity} ${entity});

}
