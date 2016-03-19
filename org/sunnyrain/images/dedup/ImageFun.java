package org.sunnyrain.images.dedup;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple tests.
 *
 * The list of images are from: http://cloudinary.com/blog/how_to_automatically_identify_similar_images_using_phash
 */
public class ImageFun {

    private static final String[] fileNames = { "koala1.jpg", "koala2.jpg", "koala3.jpg", "koala4.jpg",
            "another_koala.jpg", "woman1.jpg"};
    private static Map<String, Image> imageFiles;

    public static void main(String[] args) throws IOException {
        String dirName = args[0];
        Path dirPath = Paths.get(dirName);
        imageFiles = new HashMap<>();
        for (String filename: fileNames) {
            File imageFile = dirPath.resolve(filename).toFile();
            imageFiles.put(filename, ImageIO.read(imageFile));
        }
        System.out.println("Testing Diff Hashing...");
        testDiff(new DiffHasher());

        System.out.println("Testing Average Hashing...");
        testDiff(new AverageHasher());

        System.out.println("Testing DCT Hashing...");
        testDiff(new DctHasher());
    }

    private static void testDiff(ImageHasher imageHasher) {
        String hash1 = imageHasher.hash(imageFiles.get("koala1.jpg"));
        String hash2 = imageHasher.hash(imageFiles.get("koala2.jpg"));
        String hash3 = imageHasher.hash(imageFiles.get("koala3.jpg"));
        String hash4 = imageHasher.hash(imageFiles.get("koala4.jpg"));
        String hash5 = imageHasher.hash(imageFiles.get("another_koala.jpg"));
        String hash6 = imageHasher.hash(imageFiles.get("woman1.jpg"));


        {
            int diff12 = HashUtil.hammingDistance(hash1, hash2);
            double sim12 = HashUtil.similarity(hash1, hash2);
            System.out.println("diff <koala1.jpg koala2.jpg> = " + diff12 + " and sim12 = " + sim12);
        }

        {
            int diff13 = HashUtil.hammingDistance(hash1, hash3);
            double sim13 = HashUtil.similarity(hash1, hash3);
            System.out.println("diff <koala1.jpg koala3.jpg> = " + diff13 + " and sim13 = " + sim13);
        }

        {
            int diff14 = HashUtil.hammingDistance(hash1, hash4);
            double sim14 = HashUtil.similarity(hash1, hash4);
            System.out.println("diff <koala1.jpg koala4.jpg> = " + diff14 + " and sim14 = " + sim14);
        }

        {
            int diff15 = HashUtil.hammingDistance(hash1, hash5);
            double sim15 = HashUtil.similarity(hash1, hash5);
            System.out.println("diff <koala1.jpg another_koala.jpg> = " + diff15 + " and sim15 = " + sim15);
        }

        {
            int diff16 = HashUtil.hammingDistance(hash1, hash6);
            double sim16 = HashUtil.similarity(hash1, hash5);
            System.out.println("diff <koala1.jpg woman1.jpg> = " + diff16 + " and sim15 = " + sim16);
        }
    }


}
