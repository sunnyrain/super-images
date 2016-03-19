package org.sunnyrain.images.dedup;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Class to scale the image down
 */
public class ImageScaler {

    public static final int OUTPUT_WIDTH = 8;
    public static final int OUTPUT_HEIGHT = 8;

    private final int outputWidth;
    private final int outputHeight;

    public ImageScaler(int width, int height) {
        outputHeight = height;
        outputWidth = width;
    }

    public ImageScaler() {
        this(OUTPUT_WIDTH, OUTPUT_HEIGHT);
    }


    public BufferedImage downsize(Image inputImage) {
        // first, downsize the input image and convert it to gray scale
        BufferedImage downsizedImage = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics graphics = downsizedImage.createGraphics();
        graphics.drawImage(inputImage, 0, 0, outputWidth, outputHeight, null);
        graphics.dispose();
        return downsizedImage;
    }

}
