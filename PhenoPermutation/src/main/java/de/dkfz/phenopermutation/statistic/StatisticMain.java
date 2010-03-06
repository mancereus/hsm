package de.dkfz.phenopermutation.statistic;

import java.io.File;
import java.util.Map;

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
import de.dkfz.phenopermutation.computation.Permutator;
import de.dkfz.phenopermutation.importer.HaploImporter;
import de.dkfz.phenopermutation.importer.PhenoImporter;
import de.dkfz.phenopermutation.statistic.asymptotic.AsymptoticResult;
import de.dkfz.phenopermutation.statistic.asymptotic.AsymptoticStatistics;
import de.dkfz.phenopermutation.statistic.sharing.SharingResult;
import de.dkfz.phenopermutation.statistic.sharing.SharingStatistics;

public class StatisticMain {

    enum Type {
        shar, asympt;
    }

    private final static Logger log = LoggerFactory.getLogger(StatisticMain.class);

    @Argument(value = "stat", alias = "st", description = "statistic (shar, asympt)")
    private final String stat = Type.shar.name();

    @Argument(value = "pheno", alias = "p", description = "phenotype filename", required = true)
    private File pheno;

    @Argument(value = "haplo", alias = "h", description = "haplotype filename")
    private File haplo;

    @Argument(value = "permsize", alias = "ps", description = "permutationsize")
    private final Integer permsize = Integer.valueOf(10);

    @Argument(value = "mu", alias = "mu", description = "mu value")
    private final Double mu = Double.valueOf(0.66666666666666666666666666666);

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
            pheno = new File("test/hsm.pheno");
        // pheno = new File("src/test/resources/phenotest.ga");
        Phenotype[] phenos = new PhenoImporter().importPhenos(pheno);
        // .importPhenos(new File("src/main/resources/mammastu.pheno.ga"));
        String filename = "test/hsm.hap";
        // String filename = "src/test/resources/haplotest.dat";
        if (haplo == null)
            haplo = new File(filename);
        Person[] persons = new HaploImporter().importHaplos(haplo);
        // "src/main/resources/mammastu.ent.chr.22.hap"));
        int haplosize = persons[0].getHaplo1().getLength();
        Statistic shst = null;
        switch (Type.valueOf(stat)) {
        case asympt:
            shst = getAsymptStatistics(phenos, persons, haplosize);

            break;
        case shar:
        default:
            shst = getSharingStatistics(phenos, persons, haplosize);
            break;
        }

        try {
            shst.writeOutput(haplo.getName());
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    private Statistic getSharingStatistics(Phenotype[] phenos, Person[] persons, int haplosize) {
        Statistic shst;
        Result<Map<Permutator, double[]>> result = new SharingResult(phenos, haplosize, permsize, persons.length, mu);
        HaploComparator pc = new HaploSharingComparator(result, persons);
        pc.calculateSharing();
        shst = new SharingStatistics(result.getResult());
        return shst;
    }

    private Statistic getAsymptStatistics(Phenotype[] phenos, Person[] persons, int haplosize) {
        Statistic shst;
        Result<Map<AsymptoticResult.TYPE, Object>> result = new AsymptoticResult(phenos, haplosize, permsize,
                persons.length);
        HaploComparator pc = new HaploSharingComparator(result, persons);
        pc.calculateSharing();
        shst = new AsymptoticStatistics(result.getResult());
        return shst;
    }
}
