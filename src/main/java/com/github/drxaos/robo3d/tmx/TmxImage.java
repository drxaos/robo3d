package com.github.drxaos.robo3d.tmx;


public class TmxImage {
    private final String mSource;
    private final int mTrans;
    private final int mHeight;
    private final int mWidth;

    @Override
    public String toString() {
        return mSource + ", " + Integer.toHexString(mTrans)
                + ", " + Integer.toString(mWidth)
                + ", " + Integer.toString(mHeight);
    }

    public TmxImage(String source, int trans, int width, int height) {
        mSource = source;
        mTrans = trans;
        mHeight = height;
        mWidth = width;
    }

    public String getSource() {
        return mSource;
    }

    public int getTrans() {
        return mTrans;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }
}
