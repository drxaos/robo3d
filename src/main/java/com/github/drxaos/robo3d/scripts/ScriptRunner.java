package com.github.drxaos.robo3d.scripts;

import com.github.drxaos.robo3d.scripts.access.AccessWrapper;
import org.dynjs.Clock;
import org.dynjs.Config;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.js.ParserException;
import org.dynjs.parser.js.SyntaxError;
import org.dynjs.runtime.*;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.stream.Collectors;

public class ScriptRunner {
    DynJS dynjs;
    Config config;
    SandboxDynObject sandbox;
    DynObject object;
    InVar inVar = new InVar();
    OutVar outVar = new OutVar();
    Moment moment = new Moment();
    Runner runner;
    Logs logs;
    PrintStream logWriter;

    String scriptName;
    ScriptCache scriptCache;

    class Logs extends ByteArrayOutputStream {
        public void reduce(int max) {
            if (count > max) {
                byte[] rbuf = new byte[max];
                System.arraycopy(buf, count - max, rbuf, 0, max);
                buf = rbuf;
                count = max;
            }
        }
    }

    class Moment implements Clock {

        long time = 0;

        public void setTime(long time) {
            this.time = time;
        }

        @Override
        public long currentTimeMillis() {
            return time;
        }
    }

    public ScriptRunner(final ScriptCache scriptCache) {
        this.scriptCache = scriptCache;

        config = new Config();
        config.setClock(moment);

        logWriter = new PrintStream(logs = new Logs());
        config.setErrorStream(logWriter);
        config.setOutputStream(logWriter);

        object = sandbox = new SandboxDynObject();
        sandbox.setFiltering(true);
        JSObject proxy = (JSObject) Proxy.newProxyInstance(
                JSObject.class.getClassLoader(), new Class[]{JSObject.class},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return method.invoke(object, args);
                    }
                });
        dynjs = new DynJS(config, proxy) {
            // fix for kernel.js
        };
        sandbox.setFiltering(false);

        defineGlobalProperty("IN", inVar, true);
        defineGlobalProperty("OUT", outVar, true);

        runner = new Runner(dynjs) {
            public Object execute() {
                try {
                    JSProgram program = program();
                    scriptCache.put(scriptName, program);
                    Completion completion = executionContext().execute(program);
                    if (completion.type == Completion.Type.BREAK || completion.type == Completion.Type.CONTINUE) {
                        throw new ThrowException(executionContext(), executionContext().createSyntaxError("illegal break or continue"));
                    }
                    Object v = completion.value;
                    if (v instanceof Reference) {
                        return ((Reference) v).getValue(executionContext());
                    }
                    return v;
                } catch (SyntaxError e) {
                    throw new ThrowException(executionContext(), executionContext().createSyntaxError(e.getMessage()));
                } catch (ParserException e) {
                    throw new ThrowException(executionContext(), e);
                }
            }
        };
        ExecutionContext context = ExecutionContext.createEvalExecutionContext(dynjs);
        runner.withContext(context);
    }

    public Object runScript(String name, Data data) throws IOException {
        object = new DynObject(sandbox);

        scriptName = name;
        JSProgram program = scriptCache.get(name);
        if (program != null) {
            runner.withSource(program);
        } else {
            String fileName = "scripts/" + name + ".js";
            InputStream input = JsTest.class.getClassLoader().getResourceAsStream(fileName);
            if (input == null) {
                throw new FileNotFoundException(fileName);
            }
            String js = read(input);
            runner.withSource((JSProgram) null);
            runner.withSource(js);
        }
        moment.setTime(data.time());
        inVar.setData(data);
        outVar.setData(data);

        logWriter.append("\n\n### Logs - " + data.time() + " ###\n");

        Object result = null;
        try {
            result = runner.execute();
        } catch (ThrowException e) {
            ScriptError error;
            if (e.getValue() instanceof Throwable) {
                error = new ScriptError((ExecutionContext) new AccessWrapper<>(e).get("context"), (Throwable) e.getValue(), scriptName);
            } else {
                error = new ScriptError((ExecutionContext) new AccessWrapper<>(e).get("context"), e.getValue(), scriptName);
            }
            error.printStackTrace(logWriter);
        }
        logs.reduce(10000);
        return result;
    }

    void defineGlobalProperty(final String name, final Object value, boolean enumerable) {
        object.defineOwnProperty(null, name, PropertyDescriptor.newDataPropertyDescriptor(value, true, true, enumerable), false);
    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    public String getLogs() {
        return logs.toString();
    }
}