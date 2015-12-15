package com.github.drxaos.robo3d;


import com.github.drxaos.robo3d.graphics.Game3DRenderer;
import com.jme3.system.AppSettings;

public class Main {

    public static void main(String[] args) {
        Game3DRenderer app = new Game3DRenderer();
        AppSettings appSettings = new AppSettings(true);
        appSettings.setFullscreen(false);
        appSettings.setVSync(true);
        appSettings.setResolution(1024, 768);
        appSettings.setDepthBits(24);
        appSettings.setBitsPerPixel(24);
        appSettings.setSamples(4);
        app.setSettings(appSettings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }
}