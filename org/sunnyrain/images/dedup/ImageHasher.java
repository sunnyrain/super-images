package org.sunnyrain.images.dedup;

import java.awt.Image;

/**
 * Interface for hashing an image to a string
 */
public interface ImageHasher {

    /**
     * Hash the given image to a string that would represent the perceptual features of the image
     * @param image input image
     * @return a string that represents the perceptual features of the image, images that are similar would produce
     * similar hashes (based on some distance, like hamming for example.)
     */
    public String hash(Image image);

}
