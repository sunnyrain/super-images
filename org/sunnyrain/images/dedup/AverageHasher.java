package org.sunnyrain.images.dedup;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.BitSet;

/**
 * Perceptual hash that generates a fingerprint based on whether the grayscale value is above/below the average
 * value of a downsized grayscale image.
 * See: http://www.hackerfactor.com/blog/index.php?/archives/432-Looks-Like-It.html
 */
public class AverageHasher implements ImageHasher {

    public static final int OUTPUT_WIDTH =8;
    public static final int OUTPUT_HEIGHT =8;

    private ImageScaler imageScaler;
    public AverageHasher(int width, int height) {
        imageScaler = new ImageScaler(width, height);
    }

    public AverageHasher() {
        this(OUTPUT_WIDTH, OUTPUT_HEIGHT);
    }

    @Override
    public String hash(Image image) {

        BufferedImage downsizedImage = imageScaler.downsize(image);

        int outputHeight = downsizedImage.getHeight();
        int outputWidth = downsizedImage.getWidth();

        // calculate the mean / avg gray scale
        int sum = 0;
        for (int x = 0; x < outputHeight; x++) {
            for (int y = 0; y < outputWidth; y++) {
                sum += downsizedImage.getRGB(y, x) & 0xFF; // already a gray scale here
            }
        }
        int avg = (int) ((float)sum / outputHeight / outputWidth);

        BitSet bits = new BitSet(outputHeight * outputWidth);
        for (int row = 0; row < outputHeight; row++) {
            for (int col = 0; col < outputWidth; col++) {
                bits.set(row * outputWidth + col, (downsizedImage.getRGB(row, col) & 0xFF) > avg);
            }
        }

        String hashStr = Long.toHexString(bits.toLongArray()[0]);
        return hashStr;
    }

}
