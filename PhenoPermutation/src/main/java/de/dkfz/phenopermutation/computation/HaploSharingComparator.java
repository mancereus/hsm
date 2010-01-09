package de.dkfz.phenopermutation.computation;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.PhenoResult;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.Result;
import de.dkfz.phenopermutation.importer.HaploImporter;
import de.dkfz.phenopermutation.importer.PhenoImporter;

public class HaploSharingComparator {

    private final static Logger log = LoggerFactory.getLogger(HaploSharingComparator.class);
    private final Person[] persons;
    private final Result result;

    public HaploSharingComparator(Result result, Person[] persons) {
        this.persons = persons;
        this.result = result;
        log
                .info("haplolength: {} permutations: {}", persons[0].getHaplo1().getLength(),
                        result.getPermutators().length);
    }

    public void calculateSharing() {
        int personSize = persons.length;
        long start0 = System.currentTimeMillis();

        for (int i = 0; i < personSize - 1; i++) {
            long start = System.currentTimeMillis();
            Person per1 = persons[i];
            compareWithinPersonHaplos(per1);
            for (int j = i + 1; j < personSize; j++) {
                Person per2 = persons[j];
                compareBetweenPersonHaplos(per1, per2);
                // log.info(" person(i) to person(j): {} time: {}", i + "->" +
                // j, System.currentTimeMillis() - start);
            }
            log.info("person({}) finished: {}ms", i, System.currentTimeMillis() - start);
        }
        // compare last person
        compareWithinPersonHaplos(persons[personSize - 1]);
        log.info("all finished: {}min", (System.currentTimeMillis() - start0) / 1000);
    }

    private void compareBetweenPersonHaplos(Person person, Person person2) {
        /*
         * between person comparisons
         */
        result.addSharingValues(person.getHaplo1(), person2.getHaplo1(), person.getPos(), person2.getPos());
        result.addSharingValues(person.getHaplo1(), person2.getHaplo2(), person.getPos(), person2.getPos());

        result.addSharingValues(person.getHaplo2(), person2.getHaplo1(), person.getPos(), person2.getPos());
        result.addSharingValues(person.getHaplo2(), person2.getHaplo2(), person.getPos(), person2.getPos());

    }

    private void compareWithinPersonHaplos(Person person) {
        /*
         * within person comparison
         */
        result.addSharingValues(person.getHaplo1(), person.getHaplo2(), person.getPos(), person.getPos());
    }

    public static void main(String[] args) throws IOException {

        Phenotype[] phenos = new PhenoImporter()
        // .importPhenos(new File("src/test/resources/phenotest.ga"));
                .importPhenos(new File("src/main/resources/mammastu.pheno.ga"));
        Person[] persons = new HaploImporter().importHaplos(new File(
        // "src/test/resources/haplotest.dat"));
                "src/main/resources/mammastu.ent.chr.22.hap"));
        int permutationsize = 100;
        int haplosize = persons[0].getHaplo1().getLength();
        Result result = new PhenoResult(phenos, haplosize, permutationsize, persons.length);

        HaploSharingComparator pc = new HaploSharingComparator(result, persons);
        Map<Permutator, double[]> permutatorSum = pc.computeSharing(phenos, persons);
        // log.info("result:" + permutatorSum);

    }

    public Map<Permutator, double[]> computeSharing(Phenotype[] phenos, Person[] persons) {
        // List<Map<Permutator, Double>> result = Lists.newArrayList();
        calculateSharing();
        Map<Permutator, double[]> permutatorSum = getResult().getPermutatorSum();
        return permutatorSum;
    }

    private Result getResult() {
        return result;

    }

}