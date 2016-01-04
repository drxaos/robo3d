package com.github.drxaos.robo3d.graphics.gui;

public class Info {

    protected String cursorX = "cx";
    protected String cursorY = "cy";

    protected String selection = "name";
    protected String selectionX = "x";
    protected String selectionY = "y";
    protected String selectionRot = "rot";
    protected String selectionState = "1";

    public String[] getLabels() {
        return new String[]{
                "",
                "State: ",
                "Rotation: ",
                "Location Y: ",
                "Location X: ",
                "Selected: ",
                "",
                "Cursor Y: ",
                "Cursor X: ",
        };
    }

    public void getData(String[] data) {
        data[0] = "";
        data[1] = selectionState;
        data[2] = selectionRot;
        data[3] = selectionY;
        data[4] = selectionX;
        data[5] = selection;
        data[6] = "";
        data[7] = cursorY;
        data[8] = cursorX;
    }

    public void setCursorX(String cursorX) {
        this.cursorX = cursorX;
    }

    public void setCursorY(String cursorY) {
        this.cursorY = cursorY;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public void setSelectionX(String selectionX) {
        this.selectionX = selectionX;
    }

    public void setSelectionY(String selectionY) {
        this.selectionY = selectionY;
    }

    public void setSelectionRot(String selectionRot) {
        this.selectionRot = selectionRot;
    }

    public void setSelectionState(String selectionState) {
        this.selectionState = selectionState;
    }
}
