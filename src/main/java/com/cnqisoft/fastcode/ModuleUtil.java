package com.cnqisoft.fastcode;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;

/**
 * @author Binary on 2020/6/2
 */
public class ModuleUtil {

    public static String getSourcePath(Module module) {
        return ModuleRootManager.getInstance(module).getSourceRoots(JavaSourceRootType.SOURCE).get(0).getPath();
    }

    public static String getResourcePath(Module module) {
        return ModuleRootManager.getInstance(module).getSourceRoots(JavaResourceRootType.RESOURCE).get(0).getPath();
    }
}
