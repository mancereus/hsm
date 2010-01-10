package de.dkfz.phenopermutation.statistic;

import java.io.File;

import org.apache.commons.math.MathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

import de.dkfz.phenopermutation.HaploComparator;
import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.Result;
import de.dkfz.phenopermutation.computation.HaploSharingComparator;
import de.dkfz.phenopermutation.importer.HaploImporter;
import de.dkfz.phenopermutation.importer.PhenoImporter;
import de.dkfz.phenopermutation.statistic.test.SharingResult;
import de.dkfz.phenopermutation.statistic.test.SharingStatistics;

public class StatisticMain {

    private final static Logger log = LoggerFactory.getLogger(StatisticMain.class);

    @Argument(value = "pheno", alias = "p", description = "phenotype filename", required = true)
    private File pheno;

    @Argument(value = "haplo", alias = "h", description = "haplotype filename")
    private File haplo;

    @Argument(value = "permsize", alias = "ps", description = "permutationsize")
    private final Integer permsize = Integer.valueOf(100);

    public StatisticMain() {
    }

    /**
     * @param args
     * @throws MathException
     */
    public static void main(String[] args) throws MathException {
        log.info("something is working");
        StatisticMain main = new StatisticMain();
        Args.usage(main);
        Args.parse(main, args);
        main.compute();

    }

    private void compute() {
        if (pheno == null)
            pheno = new File("src/test/resources/phenotest.ga");
        Phenotype[] phenos = new PhenoImporter().importPhenos(pheno);
        // .importPhenos(new File("src/main/resources/mammastu.pheno.ga"));
        String filename = "src/test/resources/haplotest.dat";
        if (haplo == null)
            haplo = new File(filename);
        Person[] persons = new HaploImporter().importHaplos(haplo);
        // "src/main/resources/mammastu.ent.chr.22.hap"));
        int haplosize = persons[0].getHaplo1().getLength();
        Result<double[], SharingResult.TYPE> result = new SharingResult(phenos, haplosize, permsize, persons.length);

        HaploComparator pc = new HaploSharingComparator(result, persons);
        pc.calculateSharing();
        Statistic shst = new SharingStatistics(result.getPermutatorData(SharingResult.TYPE.VAL));
        try {
            shst.writeOutput(filename);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
}
