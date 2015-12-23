package com.github.drxaos.robo3d.scripts;

import javax.script.ScriptException;
import java.io.IOException;

public class JsTest {

    public static void main(String[] args) throws IOException, ScriptException {
        Data data = new Data();
        data.x = 2;
        data.y = 2;
        data.angle = 2;
        data.turret = 2;

        ScriptRunnerPool pool = new ScriptRunnerPool(new ScriptCache());
        pool.start();

        ScriptRunner runner = pool.getScriptRunner();

        for (int i = 0; i < 10; i++) {
            runner.runScript("aim", data);
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            runner.runScript("aim", data);
        }
        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start));

        System.out.println("---- Logs ----");
        System.out.println(runner.getLogs());
    }
}
