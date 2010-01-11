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
    private boolean ishaplo2 = false;

    public Haplotype(int length) {
        super(length);
        this.length = length;
    }

    public Haplotype(int length, boolean ishaplo2) {
        this(length);
        this.ishaplo2 = ishaplo2;
    }

    public int getLength() {
        return length;
    }

    public boolean isHaplo2() {
        return ishaplo2;
    }
}
