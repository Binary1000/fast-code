package ${packageRoot}.entity;

import javax.validation.constraints.NotNull;
import ${packageRoot}.annotation.EnumValue;
#foreach(${column} in ${columns})
#if(${column.enumeration})import ${packageRoot}.enums.${column.capitalizedName};
import com.fasterxml.jackson.annotation.JsonIgnore;
#end#end

public class ${Entity} {

    private Long id;

#foreach(${column} in ${columns})
#if(${column.required})    @NotNull
#end#if(${column.enumeration})    @EnumValue(${column.capitalizedName}.class)
#end
    private ${column.javaType} ${column.name};

#end
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    };

#foreach(${column} in ${columns})
    public void set${column.capitalizedName}(${column.javaType} ${column.name}) {
        this.${column.name} = ${column.name};
    }

    public ${column.javaType} get${column.capitalizedName}() {
        return ${column.name};
    };

#end

}
