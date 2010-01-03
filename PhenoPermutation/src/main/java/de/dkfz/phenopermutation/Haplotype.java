package de.dkfz.phenopermutation;

import java.util.BitSet;

/**
 * extends BitSet to define the length of the BitSet
 * 
 * @author mschmitt
 * 
 */
public class Haplotype extends BitSet {

    private static final long serialVersionUID = 3218510359456693175L;
    private final int length;

    public Haplotype(int length) {
        super(length);
        this.length = length;
    }

    public int getLength() {
        return length;
    }

}