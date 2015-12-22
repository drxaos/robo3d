package com.github.drxaos.robo3d.scripts;

import org.dynjs.Clock;
import org.dynjs.Config;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.js.ParserException;
import org.dynjs.parser.js.SyntaxError;
import org.dynjs.runtime.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ScriptRunner {
    DynJS dynjs;
    Config config;
    SandboxDynObject sandbox;
    InVar inVar = new InVar();
    OutVar outVar = new OutVar();
    Runner runner;

    String scriptName;
    ScriptCache scriptCache;

    public ScriptRunner(final ScriptCache scriptCache) {
        this.scriptCache = scriptCache;

        config = new Config();
        config.setClock(new Clock() {
            @Override
            public long currentTimeMillis() {
                return 1;
            }
        });

        sandbox = new SandboxDynObject();
        sandbox.setFiltering(true);
        dynjs = new DynJS(config, sandbox) {
            // fix for kernel.js
        };
        sandbox.setFiltering(false);

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
        defineGlobalProperty("IN", inVar, true);
        defineGlobalProperty("OUT", outVar, true);
        runner.withContext(context);
    }

    public Object runScript(String name, Data data) throws IOException {
        scriptName = name;
        JSProgram program = scriptCache.get(name);
        if (program != null) {
            runner.withSource(program);
        } else {
            String js = read(JsTest.class.getClassLoader().getResourceAsStream("scripts/" + name));
            runner.withSource(js);
        }
        inVar.setData(data);
        outVar.setData(data);
        return runner.execute();
    }

    void defineGlobalProperty(final String name, final Object value, boolean enumerable) {
        sandbox.defineOwnProperty(null, name, PropertyDescriptor.newDataPropertyDescriptor(value, true, true, enumerable), false);
    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}