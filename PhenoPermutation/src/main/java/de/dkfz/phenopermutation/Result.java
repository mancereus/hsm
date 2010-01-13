package de.dkfz.phenopermutation;

import de.dkfz.phenopermutation.computation.Permutator;

public interface Result<Res> {

    Permutator[] getPermutators();

    // Map<Permutator, Res> getPermutatorData();

    void addSharingValues(Haplotype haplo1, Haplotype haplo12, int pos, int pos2);

    Res getResult();

}
