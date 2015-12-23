package com.github.drxaos.robo3d.scripts;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.builtins.types.error.StackElement;

import java.util.ArrayList;

public class ScriptError extends ThrowException {
    public ScriptError(ExecutionContext context, Throwable value, String scriptName) {
        super(context, value);
        setUpStackElements(context, scriptName);
    }

    public ScriptError(ExecutionContext context, Object value, String scriptName) {
        super(context, value);
        setUpStackElements(context, scriptName);
    }

    protected void setUpStackElements(final ExecutionContext context) {
    }

    protected void setUpStackElements(final ExecutionContext context, String scriptName) {
        ArrayList<StackElement> stack = new ArrayList<>();
        context.collectStackElements(stack);
        int stackSize = stack.size();

        StackTraceElement[] elements = new StackTraceElement[stackSize];
        for (int i = 0; i < stackSize; ++i) {
            StackElement e = stack.get(i);
            String cn = scriptName;
            String fn = null;
            int dotLoc = e.getDebugContext().indexOf(".");
            if (dotLoc > 0) {
                cn = e.getDebugContext().substring(0, dotLoc);
                fn = e.getDebugContext().substring(dotLoc + 1);
            } else {
                fn = e.getDebugContext();
            }
            elements[i] = new StackTraceElement(cn, fn, scriptName, e.getLineNumber());
        }
        setStackTrace(elements);
    }

    public String toString() {
        String message = getLocalizedMessage();
        return (message != null) ? (message) : "Error";
    }
}
