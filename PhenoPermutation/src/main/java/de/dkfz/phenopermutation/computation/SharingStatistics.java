package de.dkfz.phenopermutation.computation;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.io.Files;

import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.importer.HaploImporter;
import de.dkfz.phenopermutation.importer.PhenoImporter;

public class SharingStatistics {

    private final static Logger log = LoggerFactory.getLogger(SharingStatistics.class);

    private final Double m;
    private final double[] mi;
    /**
     * Versuch den T-Test zu initialisieren LB, 09/01/03
     */
    private final static int permutationsize = 100;
    private final TDistribution t = new TDistributionImpl(permutationsize - 1);
    private final static double zero = Math.pow(10, -16);

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
     * @throws MathException
     */
    public static void main(String[] args) throws MathException {
        Phenotype[] phenos = new PhenoImporter().importPhenos(new File("src/test/resources/phenotest.ga"));
        // .importPhenos(new File("src/main/resources/mammastu.pheno.ga"));
        String filename = "src/test/resources/haplotest.dat";
        Person[] persons = new HaploImporter().importHaplos(new File(filename));
        // "src/main/resources/mammastu.ent.chr.22.hap"));
        HaploSharingComputation pc = new HaploSharingComputation(persons, phenos, permutationsize);
        Map<Permutator, Double> data = pc.computeSharing(phenos, persons);
        SharingStatistics shst = new SharingStatistics(data);
        log.info("M: {}", shst.getM());
        log.info("EM: {}", shst.getEM());
        log.info("SDM: {}", shst.getSDM());
        log.info("TM: {}", shst.getTM());
        log.info("PM: {}", shst.getPM());
        shst.writeOutput(filename);

    }

    private void writeOutput(String filename) throws MathException {
        String output = Joiner.on(" ").join(getM(), getTM(), getPM());
        Iterator<String> out = Splitter.on(CharMatcher.anyOf("/.")).split(filename).iterator();
        String result = "";
        String tmp = "";
        while (out.hasNext()) {
            result = tmp;
            tmp = out.next();
        }
        String outfile = result;
        try {
            Files.write(output.getBytes(), new File(outfile + ".hsm"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public double getTM() {
        if (getSDM() <= zero)
            return -99;
        return (m - getEM()) / getSDM();
    }

    public double getPM() throws MathException {
        if (getSDM() <= zero)
            return -99;
        return 1.0 - t.cumulativeProbability(getTM());
    }
}
