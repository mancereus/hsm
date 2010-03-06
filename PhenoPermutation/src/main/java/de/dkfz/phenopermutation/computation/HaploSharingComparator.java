package de.dkfz.phenopermutation.computation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dkfz.phenopermutation.HaploComparator;
import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.Result;

public class HaploSharingComparator implements HaploComparator {

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

    @Override
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
            result.finalizePersonRow(per1);
            log.info("person({}) finished: {}ms", i, System.currentTimeMillis() - start);
        }
        // compare last person
        compareWithinPersonHaplos(persons[personSize - 1]);
        result.finalizePersonRow(persons[personSize - 1]);
        log.info("all finished: {}min", (System.currentTimeMillis() - start0) / 1000);
    }

    private void compareBetweenPersonHaplos(Person person, Person person2) {
        /*
         * between person comparisons
         */
        result.comparePersons(person, person2);
        result.addSharingValues(person.getHaplo1(), person2.getHaplo1(), person.getPos(), person2.getPos());
        result.addSharingValues(person.getHaplo1(), person2.getHaplo2(), person.getPos(), person2.getPos());

        result.addSharingValues(person.getHaplo2(), person2.getHaplo1(), person.getPos(), person2.getPos());
        result.addSharingValues(person.getHaplo2(), person2.getHaplo2(), person.getPos(), person2.getPos());

    }

    private void compareWithinPersonHaplos(Person person) {
        /*
         * within person comparison
         */
        result.comparePersonsPheno(person);
        result.addSharingValues(person.getHaplo1(), person.getHaplo2(), person.getPos(), person.getPos());
    }

    // public static void main(String[] args) throws IOException {
    //
    // Phenotype[] phenos = new PhenoImporter()
    // // .importPhenos(new File("src/test/resources/phenotest.ga"));
    // .importPhenos(new File("src/main/resources/mammastu.pheno.ga"));
    // Person[] persons = new HaploImporter().importHaplos(new File(
    // // "src/test/resources/haplotest.dat"));
    // "src/main/resources/mammastu.ent.chr.22.hap"));
    // int permutationsize = 100;
    // int haplosize = persons[0].getHaplo1().getLength();
    // Result result = new SharingResult(phenos, haplosize, permutationsize,
    // persons.length,
    // 0.66666666666666666666666666666);
    //
    // HaploSharingComparator pc = new HaploSharingComparator(result, persons);
    // // Map<Permutator, double[]> permutatorSum = pc.computeSharing(phenos,
    // // persons);
    // // log.info("result:" + permutatorSum);
    //
    // }

    // public Map<Permutator, double[]> computeSharing() {
    // calculateSharing();
    // return getResult().getPermutatorSum();
    // }

    // private Result getResult() {
    // return result;
    //
    // }

}