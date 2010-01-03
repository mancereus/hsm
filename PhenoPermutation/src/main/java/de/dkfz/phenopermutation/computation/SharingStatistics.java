package de.dkfz.phenopermutation.computation;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.importer.HaploImporter;
import de.dkfz.phenopermutation.importer.PhenoImporter;

public class SharingStatistics {

    private final static Logger log = LoggerFactory.getLogger(SharingStatistics.class);

    private final Double m;
    private final double[] mi;

    public SharingStatistics(Map<Permutator, Double> data) {
        Iterator<Double> values = data.values().iterator();
        m = values.next();
        mi = new double[data.size() - 1];
        int idx = 0;
        while (values.hasNext()) {
            mi[idx++] = values.next();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Phenotype[] phenos = new PhenoImporter().importPhenos(new File("src/test/resources/phenotest.ga"));
        // .importPhenos(new File("src/main/resources/mammastu.pheno.ga"));
        Person[] persons = new HaploImporter().importHaplos(new File("src/test/resources/haplotest.dat"));
        // "src/main/resources/mammastu.ent.chr.22.hap"));
        int permutationsize = 100;
        HaploSharingComputation pc = new HaploSharingComputation(persons, phenos, permutationsize);
        Map<Permutator, Double> data = pc.computeSharing(phenos, persons);
        SharingStatistics shst = new SharingStatistics(data);
        log.info("M: {}", shst.getM());
        log.info("EM: {}", shst.getEM());
        log.info("SDM: {}", shst.getSDM());
        log.info("PM: {}", shst.getPM());

    }

    public Double getM() {
        return m;
    }

    public double[] getMi() {
        return mi;
    }

    public double getEM() {
        return StatUtils.mean(mi);
    }

    public double getSDM() {
        return new StandardDeviation().evaluate(mi);
    }

    public double getPM() {
        if (getSDM() == 0)
            return -99;
        return (m - getEM()) / getSDM();
    }
}
