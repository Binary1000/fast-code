package com.cnqisoft.fastcode;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

/**
 * @author Binary on 2020/6/1
 */
public class VelocityUtil {

    static  {
        Properties properties = new Properties();
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        properties.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");

        Velocity.init(properties);
    }

    public static String render(String template, VelocityContext velocityContext) throws IOException {
        try (
                StringWriter stringWriter = new StringWriter();
        ){
            Velocity.evaluate(velocityContext, stringWriter, "Velocity Code Generation", template);
            return stringWriter.toString();
        }
    }



}
