package de.dkfz.phenopermutation.computation;

import java.util.BitSet;

import de.dkfz.phenopermutation.Haplotype;

public class SharingCalculator {
    // private final static Logger log =
    // LoggerFactory.getLogger(SharingCalculator.class);

    public enum Side {
        left, right
    }

    private final BitSet compDiffs;
    private int maxpos;
    private int indx;
    private int minpos;
    private final int length;

    public SharingCalculator(Haplotype haplo1, Haplotype haplo2) {
        BitSet haplotmp = (BitSet) haplo1.clone();
        haplotmp.xor(haplo2);
        // haplotmp.get(i) == 0 : match auf pos i
        for (int i = 0; i < haplo1.length(); i++) {

            System.out.println(haplo1.get(i) + " " + haplo2.get(i) + " " + haplotmp.get(i));

        }

        compDiffs = haplotmp;
        minpos = -1;
        indx = 0;
        length = haplo1.getLength();
        maxpos = getNextDiff(indx);
    }

    private int getNextDiff(int indx) {
        int next = compDiffs.nextSetBit(indx);
        if (next == -1)
            return length;
        return next;
    }

    public int getNextSharing() {
        // log.info("range: {} {}", indx, maxpos);
        if (indx < maxpos) {
            indx++;
            return (maxpos - minpos - 1);
        }
        // indx == maxpos
        minpos = maxpos;
        indx++;
        maxpos = getNextDiff(indx);
        return 0;
    }

}
