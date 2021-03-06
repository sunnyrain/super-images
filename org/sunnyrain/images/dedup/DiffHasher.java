package org.sunnyrain.images.dedup;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.BitSet;

/**
 * Perceptual hash that's based on gradients of downsized, gray-scale image.
 * Ref: http://www.hackerfactor.com/blog/index.php?/archives/529-Kind-of-Like-That.html
 */
public class DiffHasher implements ImageHasher {

    private ImageScaler imageScaler;
    public static final int OUTPUT_WIDTH = 9;
    public static final int OUTPUT_HEIGHT = 8;

    public DiffHasher(int width, int height) {
        imageScaler = new ImageScaler(width, height);
    }

    public DiffHasher() {
        this(OUTPUT_WIDTH, OUTPUT_HEIGHT);
    }

    @Override
    public String hash(Image image) {
        BufferedImage downsizedImage = imageScaler.downsize(image);

        // go through the pixels and compare with it's left pixel value
        // if the left pixel's value is brighter, ie. p[x] < p[x+1]
        int outputWidth = downsizedImage.getWidth();
        int outputHeight = downsizedImage.getHeight();
        BitSet bits = new BitSet(outputHeight * (outputWidth-1));
        for (int row = 0; row < outputHeight; row++) {
            for (int col = 0; col < outputWidth-1; col++) {
                int left = downsizedImage.getRGB(col, row) & 0xFF;
                int right = downsizedImage.getRGB(col+1, row) & 0xFF;
                bits.set(row * (outputWidth -1) + col, left < right); // less means brighter
            }
        }
        String hashStr = Long.toHexString(bits.toLongArray()[0]);
        return hashStr;

    }

}
