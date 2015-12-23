package com.github.drxaos.robo3d.scripts;

import java.util.concurrent.LinkedBlockingQueue;

public class ScriptRunnerPool implements Runnable {

    LinkedBlockingQueue<ScriptRunner> pool = new LinkedBlockingQueue<ScriptRunner>(2);
    ScriptCache scriptCache;
    Thread thread;

    public ScriptRunnerPool(ScriptCache scriptCache) {
        this.scriptCache = scriptCache;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.setDaemon(true);
            thread.setName("ScriptRunnerPool");
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                ScriptRunner scriptRunner = new ScriptRunner(scriptCache);
                pool.put(scriptRunner);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ScriptRunner getScriptRunner() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
