package de.dkfz.phenopermutation.computation;

import java.util.BitSet;

import de.dkfz.phenopermutation.Haplotype;

public class SharingCalculator {
	// private final static Logger log = LoggerFactory
	// .getLogger(SharingCalculator.class);

	public enum Side {
		left, right
	}

	private final BitSet compDiffs;
	private int maxpos;
	private int indx;
	private int nxtindx;
	private int minpos;

	public SharingCalculator(Haplotype haplo1, Haplotype haplo2) {
		BitSet haplotmp = (BitSet) haplo1.clone();
		haplotmp.xor(haplo2);
		// haplotmp.get(i) == 0 : match auf pos i
		compDiffs = haplotmp;
		minpos = -1;
		nxtindx = 0;
		indx = 0;
		maxpos = getNextDiff(nxtindx++);
	}

	private int getNextDiff(int indx) {
		return compDiffs.nextSetBit(indx);
	}

	public int getNextSharing() {
		if (indx < maxpos) {
			indx++;
			return (maxpos - minpos - 1);
		}
		// indx == maxpos
		minpos = maxpos;
		maxpos = getNextDiff(nxtindx++);
		indx++;
		return 0;
	}

}
