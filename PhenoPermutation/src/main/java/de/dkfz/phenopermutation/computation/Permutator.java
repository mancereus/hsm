package de.dkfz.phenopermutation.computation;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * generates random permutations
 * 
 * @author mschmitt
 * 
 */
public class Permutator {
    private static java.util.Random rand = new java.util.Random();
    private final static Logger log = LoggerFactory.getLogger(Permutator.class);

    private final int size;
    private final int[] sample;

    public Permutator(int size) {
        this(size, false);
    }

    public Permutator(int size, boolean isIdentity) {
        this.size = size;
        sample = new int[size];
        for (int k = 0; k < sample.length; k++) {
            sample[k] = k;
        }
        if (!isIdentity) {
            // loop invariant: the tail of the sample array is randomized.
            // Initially the tail is empty; at each step move a random
            // element from front of array into the tail, then decrement
            // boundary of tail
            int last = sample.length - 1;

            while (last > 0) {
                swap(rand.nextInt(last + 1), last);
                last -= 1;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int getMappedId(int id) {
        return sample[id];
    }

    private void swap(int j, int k) {
        int temp = sample[k];
        sample[k] = sample[j];
        sample[j] = temp;
    }

    @Override
    public String toString() {
        return Arrays.toString(sample);
    }

    public static void main(String[] args) {
        log.info("permutation: {}", new Permutator(10));
    }
}
