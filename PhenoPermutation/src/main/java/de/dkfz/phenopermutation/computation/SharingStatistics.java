package de.dkfz.phenopermutation.computation;

import java.io.File;
import java.util.Map;

import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.importer.HaploImporter;
import de.dkfz.phenopermutation.importer.PhenoImporter;

public class SharingStatistics {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Phenotype[] phenos = new PhenoImporter()
        // .importPhenos(new File("src/test/resources/phenotest.ga"));
                .importPhenos(new File("src/main/resources/mammastu.pheno.ga"));
        Person[] persons = new HaploImporter().importHaplos(new File(
        // "src/test/resources/haplotest.dat"));
                "src/main/resources/mammastu.ent.chr.22.hap"));
        int permutationsize = 100;
        HaploSharingComputation pc = new HaploSharingComputation(persons, phenos, permutationsize);
        Map<Permutator, Double> permutatorSum = pc.computeSharing(phenos, persons);

    }

}
