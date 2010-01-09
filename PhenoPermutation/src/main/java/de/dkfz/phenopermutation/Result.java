package de.dkfz.phenopermutation;

import java.util.Map;

import de.dkfz.phenopermutation.computation.Permutator;

public interface Result<T> {

    Permutator[] getPermutators();

    Map<Permutator, T> getPermutatorData();

    void addSharingValues(Haplotype haplo1, Haplotype haplo12, int pos, int pos2);

}
