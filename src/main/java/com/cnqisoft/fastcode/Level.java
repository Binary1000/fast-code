package com.cnqisoft.fastcode;

/**
 * @author Binary on 2020/6/1
 */
public enum Level {

    /**
     * 控制器
     */
    CONTROLLER("controller", "Controller.java", "Controller.java"),

    /**
     * 实体
     */
    ENTITY("entity", ".java", "Entity.java"),

    /**
     * 服务
     */
    SERVICE("service", "Service.java", "Service.java"),

    /**
     * 服务实现
     */
    SERVICE_IMPL("service/impl", "ServiceImpl.java", "ServiceImpl.java"),

    /**
     * MapperJava
     */
    MAPPER_JAVA("mapper", "Mapper.java", "Mapper.java"),

    /**
     * MapperXml
     */
    MAPPER_XML("mapper", "Mapper.xml", "Mapper.xml");

    public final String packageName;

    public final String fileName;

    public final String templateName;

    Level(String packageName, String fileName, String templateName) {
        this.packageName = packageName;
        this.fileName = fileName;
        this.templateName = templateName;
    }

}
