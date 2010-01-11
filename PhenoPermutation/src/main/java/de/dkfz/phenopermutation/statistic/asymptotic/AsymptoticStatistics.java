package de.dkfz.phenopermutation.statistic.asymptotic;

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

import de.dkfz.phenopermutation.computation.Permutator;
import de.dkfz.phenopermutation.statistic.Statistic;

public class AsymptoticStatistics implements Statistic {

    private final static Logger log = LoggerFactory.getLogger(AsymptoticStatistics.class);

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

    public AsymptoticStatistics(Map<Permutator, double[]> ax, Map<Permutator, double[]> bx,
            Map<Permutator, double[]> dx, Map<Permutator, double[]> ay, Map<Permutator, double[]> by,
            Map<Permutator, double[]> dy) {
        Iterator<double[]> values = ax.values().iterator();
        m = values.next();
        positionsize = m.length;
        mi = new double[positionsize][ax.size()];
        int permidx = 0;
        while (values.hasNext()) {
            double[] next = values.next();
            for (int i = 0; i < next.length; i++) {
                mi[i][permidx] = next[i];
            }
            permidx++;
        }
    }

    private String getOutput() throws MathException {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < getPositionsize(); i++) {
            str.append(Joiner.on(" ").join(i, getM(i), getTM(i), getPM(i), "\n"));
        }
        return str.toString();
    }

    public void writeOutput(String filename) throws MathException {

        log.info("write output");

        Iterator<String> out = Splitter.on(CharMatcher.anyOf("/.")).split(filename).iterator();
        String result = "";
        String tmp = "";
        while (out.hasNext()) {
            result = tmp;
            tmp = out.next();
        }
        String outfile = result;
        try {
            Files.write(getOutput().getBytes(), new File("src/test/resources/" + outfile + ".hsm"));
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

    public double getEM(int posidx) {
        return StatUtils.mean(mi[posidx]);
    }

    public double getSDM(int posidx) {
        return new StandardDeviation().evaluate(mi[posidx]);
    }

    public double getTM(int idx) {
        if (getSDM(idx) <= zero)
            return -99;
        return (m[idx] - getEM(idx)) / getSDM(idx);
    }

    public double getPM(int idx) throws MathException {
        if (getSDM(idx) <= zero)
            return -99;
        return 1.0 - t.cumulativeProbability(getTM(idx));
    }

    public int getPositionsize() {
        return positionsize;
    }
}
