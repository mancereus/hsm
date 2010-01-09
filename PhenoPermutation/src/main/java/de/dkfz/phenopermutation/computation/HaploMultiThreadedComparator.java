package de.dkfz.phenopermutation.computation;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dkfz.phenopermutation.HaploComparator;
import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.Result;
import de.dkfz.phenopermutation.importer.HaploImporter;
import de.dkfz.phenopermutation.importer.PhenoImporter;
import de.dkfz.phenopermutation.statistic.test.SharingResult;

/**
 * has some trouble
 * 
 * @author Matthias
 * 
 */
public class HaploMultiThreadedComparator implements HaploComparator {

    private final static Logger log = LoggerFactory.getLogger(HaploMultiThreadedComparator.class);
    private final Person[] persons;
    private final Result result;
    private final ExecutorService pool;

    public HaploMultiThreadedComparator(Result result, Person[] persons) {
        this.persons = persons;
        this.result = result;
        log
                .info("haplolength: {} permutations: {}", persons[0].getHaplo1().getLength(),
                        result.getPermutators().length);
        int cpus = Runtime.getRuntime().availableProcessors();
        log.info("Number of processors available to the JVM: " + cpus);
        pool = Executors.newFixedThreadPool(cpus);
    }

    @Override
    public void calculateSharing() {
        final int personSize = persons.length;
        long start0 = System.currentTimeMillis();

        CompletionService<Long> completionService = new ExecutorCompletionService<Long>(pool);
        for (int i = 0; i < personSize - 1; i++) {
            final Person per1 = persons[i];
            final int minidx = i;
            completionService.submit(new Callable<Long>() {
                @Override
                public Long call() {
                    long start = System.currentTimeMillis();
                    compareWithinPersonHaplos(per1);
                    for (int j = minidx + 1; j < personSize; j++) {
                        Person per2 = persons[j];
                        compareBetweenPersonHaplos(per1, per2);
                    }
                    log.info("person({}) finished: {}ms", minidx, System.currentTimeMillis() - start);
                    return System.currentTimeMillis() - start;
                }
            });
        }
        for (int i = 0; i < personSize - 1; i++) {
            try {
                Future<Long> future = completionService.take();
                Long duration = future.get();
                log.info("finish person: {}ms", duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
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
        Result result = new SharingResult(phenos, haplosize, permutationsize, persons.length);

        HaploMultiThreadedComparator pc = new HaploMultiThreadedComparator(result, persons);

    }

}