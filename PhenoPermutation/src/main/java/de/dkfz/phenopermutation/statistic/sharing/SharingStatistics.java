package de.dkfz.phenopermutation.statistic.sharing;

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

import de.dkfz.phenopermutation.HaploComparator;
import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.Result;
import de.dkfz.phenopermutation.computation.HaploSharingComparator;
import de.dkfz.phenopermutation.computation.Permutator;
import de.dkfz.phenopermutation.importer.HaploImporter;
import de.dkfz.phenopermutation.importer.PhenoImporter;
import de.dkfz.phenopermutation.statistic.Statistic;

public class SharingStatistics implements Statistic {

    private final static Logger log = LoggerFactory.getLogger(SharingStatistics.class);

    private final double[] m;
    // permutation, pos
    private final double[][] mi;
    /**
     * Versuch den T-Test zu initialisieren LB, 09/01/03
     */
    private final static int permutationsize = 100;
    private final TDistribution t = new TDistributionImpl(permutationsize - 1);

    private final int positionsize;
    private final static double zero = Math.pow(10, -16);

    public SharingStatistics(Map<Permutator, double[]> data) {
        Iterator<double[]> values = data.values().iterator();
        m = values.next();
        positionsize = m.length;
        mi = new double[positionsize][data.size()];
        int permidx = 0;
        while (values.hasNext()) {
            double[] next = values.next();
            for (int i = 0; i < next.length; i++) {
                mi[i][permidx] = next[i];
            }
            permidx++;
        }
    }

    /**
     * @param args
     * @throws MathException
     */
    public static void main(String[] args) throws MathException {
        log.info("something is working");
        Phenotype[] phenos = new PhenoImporter().importPhenos(new File("test/hsm.pheno"));
        // .importPhenos(new File("src/main/resources/mammastu.pheno.ga"));
        String filename = "test/hsm.hap";
        Person[] persons = new HaploImporter().importHaplos(new File(filename));
        // "src/main/resources/mammastu.ent.chr.22.hap"));
        int haplosize = persons[0].getHaplo1().getLength();
        Result<Map<Permutator, double[]>> result = new SharingResult(phenos, haplosize, permutationsize,
                persons.length, 0.66666666666666666666666666666);

        HaploComparator pc = new HaploSharingComparator(result, persons);
        pc.calculateSharing();
        SharingStatistics shst = new SharingStatistics(result.getResult());

        shst.writeOutput(filename);
    }

    private String getOutput() throws MathException {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < getPositionsize(); i++) {
            str.append(Joiner.on(" ").join(i, getM(i), getT(i), getPM(i), "\n"));
            if (getT(i) < 0.0000001) {
                log.info("pos {} is significant: {}", i, getT(i));
            }

        }
        return str.toString();
    }

    @Override
    public void writeOutput(String filename) throws MathException {

        Iterator<String> out = Splitter.on(CharMatcher.anyOf("/.")).split(filename).iterator();
        String result = "";
        String tmp = "";
        while (out.hasNext()) {
            result = tmp;
            tmp = out.next();
        }
        String outfile = result;
        try {
            String outfilename = "test/" + outfile + ".out";
            log.info("write output to {}", outfilename);
            Files.write(getOutput().getBytes(), new File(outfilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Double getM(int idx) {
        return m[idx];
    }

    public double[] getMi(int posidx) {
        return mi[posidx];
    }

    @Override
    public double getEM(int posidx) {
        return StatUtils.mean(mi[posidx]);
    }

    @Override
    public double getSDM(int posidx) {
        return new StandardDeviation().evaluate(mi[posidx]);
    }

    @Override
    public double getT(int idx) {
        if (getSDM(idx) <= zero)
            return -99;
        return (m[idx] - getEM(idx)) / getSDM(idx);
    }

    public double getPM(int idx) throws MathException {
        if (getSDM(idx) <= zero)
            return -99;
        return 1.0 - t.cumulativeProbability(getT(idx));
    }

    public int getPositionsize() {
        return positionsize;
    }

    @Override
    public double getTi(int pos, int i) {
        // TODO Auto-generated method stub
        return 0;
    }

}
