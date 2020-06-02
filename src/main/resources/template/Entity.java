package ${packageRoot}.entity;

public class ${Entity} {

    private Long id;

#foreach(${column} in ${columns})
    private ${column.javaType} ${column.name};

#end
    private void setId(Long id) {
        this.id = id;
    }

    private Long getId() {
        return id;
    };

#foreach(${column} in ${columns})
    private void set${column.capitalizedName}(${column.javaType} ${column.name}) {
        this.${column.name} = ${column.name};
    }

    private ${column.javaType} get${column.capitalizedName}() {
        return ${column.name};
    };

#end

}
