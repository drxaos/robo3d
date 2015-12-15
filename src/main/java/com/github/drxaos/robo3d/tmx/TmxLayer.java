package com.github.drxaos.robo3d.tmx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

abstract class LayerDataReader {

    protected final byte[] mCompressedData;
    protected final int mLevelWidth;
    protected final int mLevelHeight;

    public LayerDataReader(byte[] compressedData, int levelWidth,
                           int levelHeight) {
        mCompressedData = compressedData;
        mLevelWidth = levelWidth;
        mLevelHeight = levelHeight;
    }

    public abstract int[] getData();
}

class GZIPDataReader extends LayerDataReader {

    private final static int INT_SIZE_IN_BYTES = Integer.SIZE / 8;

    public GZIPDataReader(byte[] compressedData, int levelWidth,
                          int levelHeight) {
        super(compressedData, levelWidth, levelHeight);
    }

    @Override
    public int[] getData() {

        GZIPInputStream gis = null;
        int[] out = null;

        try {
            ByteArrayInputStream bais =
                    new ByteArrayInputStream(mCompressedData);

            gis = new GZIPInputStream(bais);

            int intSize = mLevelHeight * mLevelWidth;
            int byteSize = intSize * INT_SIZE_IN_BYTES;

            byte[] rawByteData = new byte[byteSize];
            out = new int[intSize];
            Arrays.fill(out, 0);

            int j = 0;
            int byteNum = 0;

            while (true) {
                int r = gis.read(rawByteData, 0, byteSize);
                if (r == -1) {
                    break;
                }

                // merge bytes to integers
                for (int i = 0; i < r; i++) {
                    out[j] = out[j] | ((rawByteData[i] & 0xFF) << (byteNum * 8));
                    byteNum++;
                    if (byteNum > 3) {
                        byteNum = 0;
                        j++;
                    }
                }
            }
        } catch (IOException e) {
            out = null;
            LogHelper.e("IOException on GZIP reading.");
        } finally {
            try {
                if (gis != null) {
                    gis.close();
                }
            } catch (IOException e) {
                LogHelper.e("IOException on GZIP stream close().");
            }
        }
        return out;
    }
}

public class TmxLayer extends TmxEntity {
    protected static final int FLAG_FLIP_HORIZONTALLY = 0x80000000;
    protected static final int FLAG_FLIP_VERTICALLY = 0x40000000;
    protected static final int FLAG_FLIP_DIAGONALLY = 0x20000000;
    protected static final int MASK_CLEAR = 0xE0000000;

    public final String name;
    public final int width;
    public final int height;

    public final boolean isVisible;

    private int[] mData;

    public TmxLayer(String name, int width, int height, boolean isVisible) {
        super();

        this.height = height;
        this.width = width;
        this.name = name;

        this.isVisible = isVisible;
    }

    void setCompressedData(String data, TmxDataEncoding encoding,
                           TmxDataCompression compression) {

        byte[] decoded;

        switch (encoding) {
            case BASE64:
                try {
                    decoded = Base64.decode(data, Base64.DONT_GUNZIP);
                } catch (IOException e) {
                    LogHelper.e("IOException while base64 decoding.");
                    return;
                }
                break;

            default:
                decoded = data.getBytes();
                break;
        }

        LayerDataReader dr;

        switch (compression) {
            case GZIP:
                dr = new GZIPDataReader(decoded, width, height);
                break;

            default:
                LogHelper.e("Unsupported compression: " + compression.toString());
                return;
        }

        mData = dr.getData();
    }

    public int getTileGid(int x, int y) {
        boolean coordsAreValid = (y >= 0) && (y < height)
                && (x >= 0) && (x < width);

        if (mData == null) {
            LogHelper.e(name + " has null data.");
            return 0;
        }

        return (coordsAreValid) ? mData[y * width + x] : 0;
    }

    public int getTileId(int x, int y) {
        boolean coordsAreValid = (y >= 0) && (y < height)
                && (x >= 0) && (x < width);

        if (mData == null) {
            LogHelper.e(name + " has null data.");
            return 0;
        }

        return (coordsAreValid) ? mData[y * width + x] & ~MASK_CLEAR : 0;
    }

    public boolean isTileFlippedVertically(int x, int y) {
        boolean coordsAreValid = (y >= 0) && (y < height)
                && (x >= 0) && (x < width);

        if (mData == null) {
            LogHelper.e(name + " has null data.");
            return false;
        }

        return (coordsAreValid) && ((mData[y * width + x] & FLAG_FLIP_VERTICALLY) != 0);
    }

    public boolean isTileFlippedHorizontally(int x, int y) {
        boolean coordsAreValid = (y >= 0) && (y < height)
                && (x >= 0) && (x < width);

        if (mData == null) {
            LogHelper.e(name + " has null data.");
            return false;
        }

        return (coordsAreValid) && ((mData[y * width + x] & FLAG_FLIP_HORIZONTALLY) != 0);
    }

    public boolean isTileFlippedDiagonally(int x, int y) {
        boolean coordsAreValid = (y >= 0) && (y < height)
                && (x >= 0) && (x < width);

        if (mData == null) {
            LogHelper.e(name + " has null data.");
            return false;
        }

        return (coordsAreValid) && ((mData[y * width + x] & FLAG_FLIP_DIAGONALLY) != 0);
    }

    public int getTileRotation(int x, int y) {
        return (isTileFlippedDiagonally(x, y) ? 1 : 0) * 90 + (isTileFlippedVertically(x, y) ? 1 : 0) * 180;
    }


}
