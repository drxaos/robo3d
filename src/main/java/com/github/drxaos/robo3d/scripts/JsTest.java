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

        for (int i = 0; i < 100; i++) {
            pool.getScriptRunner().runScript("aim.js", data);
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            pool.getScriptRunner().runScript("aim.js", data);
        }
        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start));
    }
}
