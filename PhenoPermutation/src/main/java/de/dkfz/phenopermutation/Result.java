package de.dkfz.phenopermutation;

import java.util.Map;

import de.dkfz.phenopermutation.computation.Permutator;

public interface Result {

    Permutator[] getPermutators();

    Map<Permutator, double[]> getPermutatorSum();

    void addSharingValues(Haplotype haplo1, Haplotype haplo12, int pos, int pos2);

}
