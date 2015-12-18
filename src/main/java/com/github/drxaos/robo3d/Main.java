package com.github.drxaos.robo3d;


import com.github.drxaos.robo3d.graphics.App;
import com.jme3.system.AppSettings;

public class Main {

    public static void main(String[] args) {
        App app = new App();
        AppSettings appSettings = new AppSettings(true);
        appSettings.setFullscreen(false);
        appSettings.setVSync(true);
        appSettings.setFrameRate(30);
        appSettings.setResolution(1024, 768);
        appSettings.setDepthBits(24);
        appSettings.setBitsPerPixel(24);
        appSettings.setSamples(1);
        app.setSettings(appSettings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }
}