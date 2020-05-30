package com.cnqisoft.fastcode;

import java.sql.SQLException;
import java.util.List;

public class Table {

    private static final Charset DEFAULT_CHARSET = Charset.UTF8;

    private static final Engine DEFAULT_ENGINE = Engine.InnoDB;

    private String name;

    private Charset charset;

    private Engine engine;

    private List<Field> fields;

    public Table(String name, List<Field> fields) {
        this(name, fields, DEFAULT_CHARSET, DEFAULT_ENGINE);
    }

    public Table(String name, List<Field> fields, Charset charset, Engine engine) {
        this.name = name;
        this.fields = fields;
        this.charset = charset;
        this.engine = engine;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getTableDDL() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("CREATE TABLE `%s` ( \n", name));
        stringBuilder.append("  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,\n");

        for (Field field : fields) {
            stringBuilder.append(field.getString());
        }

        stringBuilder.append("  PRIMARY KEY (`id`) USING BTREE\n");
        stringBuilder.append(String.format(") ENGINE=%s DEFAULT CHARSET=%s", engine.name(), charset.name()));

        return stringBuilder.toString();
    }

    public void generate() throws SQLException {
        String sql = getTableDDL();
        DBUtil.executeUpdate(sql);
    }

}
