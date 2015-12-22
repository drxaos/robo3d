package com.github.drxaos.robo3d.scripts;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.PropertyDescriptor;

import java.util.Arrays;
import java.util.List;

public class SandboxDynObject extends DynObject {

    static final List<String> FILTER = Arrays.asList(
            "JSAdapter",
            "__v8StackGetter",
            "dynjs",
            "Packages",
            "java",
            "javax",
            "org",
            "com",
            "io",
            "System",
            "Intl",
            "module",
            "exports",
            "require",
            "include",
            "load"
    );

    boolean filtering = false;

    public void setFiltering(boolean filtering) {
        this.filtering = filtering;
    }

    @Override
    public boolean defineOwnProperty(ExecutionContext context, String name, PropertyDescriptor desc, boolean shouldThrow) {
        if (filtering) {
            if (name.startsWith("__Builtin_")) {
                return false;
            }
            if (FILTER.contains(name)) {
                return false;
            }
        }
        return super.defineOwnProperty(context, name, desc, shouldThrow);
    }
}
