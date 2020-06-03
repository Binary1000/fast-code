package ${packageRoot}.entity;

public class ${Entity} {

    private Long id;

#foreach(${column} in ${columns})
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
