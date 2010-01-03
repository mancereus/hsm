package de.dkfz.phenopermutation.computation;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dkfz.phenopermutation.Haplotype;
import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.PhenoResult;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.importer.HaploImporter;
import de.dkfz.phenopermutation.importer.PhenoImporter;

public class HaploSharingComputation {

    private final static Logger log = LoggerFactory.getLogger(HaploSharingComputation.class);
    private final Person[] persons;
    private final PhenoResult result;
    private final Phenotype[] phenos;
    private final ExecutorService pool;

    public HaploSharingComputation(Person[] persons, Phenotype[] phenos, int permutationsize) {
        int haplosize = persons[0].getHaplo1().getLength();
        PhenoResult result = new PhenoResult(haplosize, permutationsize, persons.length);
        this.persons = persons;
        this.result = result;
        this.phenos = phenos;
        log
                .info("haplolength: {} permutations: {}", persons[0].getHaplo1().getLength(),
                        result.getPermutators().length);
        int cpus = Runtime.getRuntime().availableProcessors();
        log.info("Number of processors available to the JVM: " + cpus);
        pool = Executors.newFixedThreadPool(cpus);
    }

    public void calculateSharing() {
        final int personSize = persons.length;

        CompletionService<Long> completionService = new ExecutorCompletionService<Long>(pool);
        for (int i = 0; i < personSize - 1; i++) {
            final Person per1 = persons[i];
            final int minidx = i;

            completionService.submit(new Callable<Long>() {
                @Override
                public Long call() {
                    final long start = System.currentTimeMillis();
                    log.info("start person({}):", minidx);
                    comparePersons(personSize, minidx, per1);
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
    }

    private void comparePersons(int personSize, int i, Person per1) {
        for (int j = i + 1; j < personSize; j++) {
            Person per2 = persons[j];
            comparePersonHaplos(per1, per2);
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
                synchronized (result) {
                    result.addResult(i, k, sharingvalue * pheno1 * pheno2);
                }
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
        int permutationsize = 100;
        HaploSharingComputation pc = new HaploSharingComputation(persons, phenos, permutationsize);
        Map<Permutator, Double> permutatorSum = pc.computeSharing(phenos, persons);
        log.info("result:" + permutatorSum);

    }

    public Map<Permutator, Double> computeSharing(Phenotype[] phenos, Person[] persons) {
        // List<Map<Permutator, Double>> result = Lists.newArrayList();
        calculateSharing();
        Map<Permutator, Double> permutatorSum = getResult().getPermutatorSum();
        return permutatorSum;
    }

    private PhenoResult getResult() {
        return result;

    }

}