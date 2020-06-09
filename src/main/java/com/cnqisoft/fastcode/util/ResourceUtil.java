package com.cnqisoft.fastcode.util;

import java.io.InputStream;

/**
 * @author Binary on 2020/6/2
 */
public class ResourceUtil {

    public static InputStream getResourceAsStream(String resourcePath) {
        return ResourceUtil.class.getClassLoader().getResourceAsStream(resourcePath);
    }

}
