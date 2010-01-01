package de.dkfz.phenopermutation.computation;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dkfz.phenopermutation.Haplotype;
import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.PhenoResult;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.importer.HaploImporter;
import de.dkfz.phenopermutation.importer.PhenoImporter;

public class PhenoComputation {

    private final static Logger log = LoggerFactory.getLogger(PhenoComputation.class);
    private final Person[] persons;
    private final PhenoResult result;
    private final Phenotype[] phenos;

    public PhenoComputation(Person[] persons, PhenoResult result, Phenotype[] phenos) {
        this.persons = persons;
        this.result = result;
        this.phenos = phenos;
        log
                .info("haplolength: {} permutations: {}", persons[0].getHaplo1().getLength(),
                        result.getPermutators().length);
    }

    public void calculateSharing() {
        int personSize = persons.length;

        for (int i = 0; i < personSize - 1; i++) {
            long start = System.currentTimeMillis();
            Person per1 = persons[i];
            for (int j = i + 1; j < personSize; j++) {
                Person per2 = persons[j];
                comparePersonHaplos(per1, per2);
                // log.info(" person(i) to person(j): {} time: {}", i + "->" +
                // j, System.currentTimeMillis() - start);
            }
            log.info("person({}) finished: {}ms", i, System.currentTimeMillis() - start);
        }
    }

    public double getMu() {
        return 0.01;
        // compute from phenos, cache
    }

    private void comparePersonHaplos(Person person, Person person2) {
        addSharingValues(person.getHaplo1(), person.getHaplo2(), person.getPos(), person.getPos());
        addSharingValues(person.getHaplo1(), person2.getHaplo1(), person.getPos(), person2.getPos());
        addSharingValues(person.getHaplo1(), person2.getHaplo2(), person.getPos(), person2.getPos());

        addSharingValues(person.getHaplo2(), person2.getHaplo1(), person.getPos(), person2.getPos());
        addSharingValues(person.getHaplo2(), person2.getHaplo2(), person.getPos(), person2.getPos());

        addSharingValues(person2.getHaplo1(), person2.getHaplo2(), person2.getPos(), person2.getPos());

    }

    private void addSharingValues(Haplotype h1, Haplotype h2, int per1id, int per2id) {
        // log.info("compare p1h1 p1h2");
        SharingCalculator calc = new SharingCalculator(h1, h2);
        for (int i = 0; i < h1.getLength(); i++) {
            Permutator[] perms = result.getPermutators();
            int sharingvalue = calc.getNextSharing();
            for (int k = 0; k < perms.length; k++) {
                double pheno1 = phenos[perms[k].getMappedId(per1id)].getValue() - getMu();
                double pheno2 = phenos[perms[k].getMappedId(per2id)].getValue() - getMu();
                result.addResult(i, k, sharingvalue * pheno1 * pheno2);
            }
        }
    }

    public static void main(String[] args) throws IOException {

        Phenotype[] phenos = new PhenoImporter()
        // .importPhenos(new File("src/test/resources/phenotest.ga"));
                .importPhenos(new File("src/main/resources/mammastu.pheno.ga"));
        Person[] persons = new HaploImporter().importHaplos(new File(
        // "src/test/resources/haplotest.dat"));
                "src/main/resources/mammastu.ent.chr.22.hap"));
        int haplosize = persons[0].getHaplo1().getLength();
        int permutationsize = 100;
        PhenoResult result = new PhenoResult(haplosize, permutationsize, persons.length);
        PhenoComputation pc = new PhenoComputation(persons, result, phenos);
        // List<Map<Permutator, Double>> result = Lists.newArrayList();
        pc.calculateSharing();
        pc.getResult();

    }

    private PhenoResult getResult() {
        return result;

    }

}