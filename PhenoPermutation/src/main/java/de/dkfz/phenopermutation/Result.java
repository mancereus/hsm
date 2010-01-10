package de.dkfz.phenopermutation;

import java.util.Map;

import de.dkfz.phenopermutation.computation.Permutator;

public interface Result<Res, Type> {

    Permutator[] getPermutators();

    // Map<Permutator, Res> getPermutatorData();

    void addSharingValues(Haplotype haplo1, Haplotype haplo12, int pos, int pos2);

    Map<Permutator, double[]> getPermutatorData(Type type);

}
