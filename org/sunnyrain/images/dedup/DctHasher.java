package org.sunnyrain.images.dedup;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.BitSet;

/**
 */
public class DctHasher implements ImageHasher {

    private static final int numElements = 8;

    private ImageScaler imageScaler;

    public DctHasher(int width, int height) {
        imageScaler = new ImageScaler(width, height);
    }

    public DctHasher() {
        this(32, 32);
    }

    @Override
    public String hash(Image image) {
        BufferedImage downsizedImage = imageScaler.downsize(image); // 32 x 32
        float[][] data = new float[downsizedImage.getHeight()][downsizedImage.getWidth()];
        for (int row = 0; row < downsizedImage.getHeight(); row++) {
            for (int col = 0; col < downsizedImage.getWidth(); col++) {
                data[row][col] = downsizedImage.getRGB(col, row) & 0xFF;
            }
        }

        // compute the DCT
        float[][] dctData = forwardDCT(data);
        float sum = 0.0f;
        for (int i = 0; i < numElements; i++) {
            for (int j = 0; j < numElements; j++) {
                sum += dctData[i][j];
            }
        }
        sum -= dctData[0][0]; // do not count the DC component
        float avg = sum / (numElements * numElements - 1);
        BitSet bits = new BitSet(numElements * numElements);
        for (int row = 0; row < numElements; row++) {
            for (int col = 0; col < numElements; col++) {
                bits.set(row * numElements + col, dctData[row][col] > avg);
            }
        }

        String hashStr = Long.toHexString(bits.toLongArray()[0]);
        return hashStr;
    }


    public static float[] forwardDCT(float[] data) {
        final float alpha0 = (float) Math.sqrt(1.0 / data.length);
        final float alpahN = (float) Math.sqrt(2.0 / data.length);
        float result[] = new float[data.length];

        for (int u = 0; u < data.length; u++) {
            for (int x = 0; x < data.length; x++) {
                result[u] += data[x] * (float) Math.cos( (2 * x + 1) * u * Math.PI/ (2 * data.length) );
            }
            result[u] = result[u] * (u == 0 ? alpha0 : alpahN);
        }
        return result;
    }

    public static float[][] forwardDCT(float[][] data) {
        float[][] result = new float[data.length][data.length];

        // 1D DCT of every row
        for (int u = 0; u < result.length; u++) {
            result[u] = forwardDCT(data[u]);
        }

        // 1D DCT of every column of intermediate data
        float[] column = new float[data.length];
        for (int v = 0; v < result.length; v++) {
            for (int row = 0; row < data.length; row++) {
                column[row] = result[row][v];
            }

            float[] temp = forwardDCT(column);
            for (int row = 0; row < data.length; row++) {
                result[row][v] = temp[row];
            }
        }
        return result;
    }
}
