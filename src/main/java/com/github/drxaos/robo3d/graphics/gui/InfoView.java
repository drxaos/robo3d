package com.github.drxaos.robo3d.graphics.gui;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class InfoView extends Node implements Control {

    private BitmapText[] labels;
    private Info info;

    private String[] statLabels;
    private String[] statData;

    private boolean enabled = true;

    private final StringBuilder stringBuilder = new StringBuilder();

    public InfoView(String name, AssetManager manager, Info info) {
        super(name);

        setQueueBucket(Bucket.Gui);
        setCullHint(CullHint.Never);

        this.info = info;

        statLabels = this.info.getLabels();
        statData = new String[statLabels.length];
        labels = new BitmapText[statLabels.length];

        BitmapFont font = manager.loadFont("Interface/Fonts/Console.fnt");
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new BitmapText(font);
            labels[i].setLocalTranslation(0, labels[i].getLineHeight() * (i + 1), 0);
            attachChild(labels[i]);
        }

        addControl(this);
    }

    public float getHeight() {
        return labels[0].getLineHeight() * statLabels.length;
    }

    public void update(float tpf) {

        if (!isEnabled())
            return;

        info.getData(statData);
        for (int i = 0; i < labels.length; i++) {
            stringBuilder.setLength(0);
            stringBuilder.append(statLabels[i]).append(statData[i]);
            labels[i].setText(stringBuilder);
        }
    }

    public Control cloneForSpatial(Spatial spatial) {
        return (Control) spatial;
    }

    public void setSpatial(Spatial spatial) {
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void render(RenderManager rm, ViewPort vp) {
    }

}
