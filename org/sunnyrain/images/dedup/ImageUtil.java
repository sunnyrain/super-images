package org.sunnyrain.images.dedup;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Utility to for shared functionalities of images.
 */
public class ImageUtil {

    private ImageUtil() {
        // make this a utility class.
    }

    public static BufferedImage downsize(Image inputImage, int outputWidth, int outputHeight) {
        // first, downsize the input image and convert it to gray scale
        BufferedImage downsizedImage = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics graphics = downsizedImage.createGraphics();
        graphics.drawImage(inputImage, 0, 0, outputWidth, outputHeight, null);
        graphics.dispose();
        return downsizedImage;
    }

}
