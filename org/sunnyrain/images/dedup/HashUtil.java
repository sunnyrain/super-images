package org.sunnyrain.images.dedup;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * Utility class for dealing with hash.
 */
public class HashUtil {

    private HashUtil() {
        // make it a utility class
    }

    public static int hammingDistance(String hash1, String hash2) {
        // convert both to bitsets and count the diff
        BitSet bs1 = BitSet.valueOf(new long[]{new BigInteger(hash1, 16).longValue()});
        BitSet bs2 = BitSet.valueOf(new long[]{new BigInteger(hash2, 16).longValue()});
        bs1.xor(bs2);  // xor would show the diff between these two sets.
        return bs1.cardinality();
    }

    public static double similarity(String hash1, String hash2) {
        return 1.0 - hammingDistance(hash1, hash2) / 64.0;
    }

}
