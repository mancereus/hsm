package de.dkfz.phenopermutation;

import de.dkfz.phenopermutation.computation.Permutator;

public interface Result<Res> {

    Permutator[] getPermutators();

    // Map<Permutator, Res> getPermutatorData();

    Res getResult();

    // copmare 2 persons
    void comparePersons(Person person, Person person2);

    // compare person haplotypes
    void addSharingValues(Haplotype haplo1, Haplotype haplo12, int pos, int pos2);

    // person has been compared with all other persons
    void finalizePersonRow(Person per1);

}
