package de.dkfz.phenopermutation.statistic.asymptotic;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.io.Files;

import de.dkfz.phenopermutation.statistic.Statistic;
import de.dkfz.phenopermutation.statistic.asymptotic.AsymptoticResult.TYPE;

public class AsymptoticStatistics implements Statistic {

    private final static Logger log = LoggerFactory.getLogger(AsymptoticStatistics.class);

    private final Map<TYPE, Object> result;

    private final double haplosize;

    private final int permutationsize;

    private final TDistribution t;

    private final static double zero = Math.pow(10, -16);

    private final double[][] m;

    public AsymptoticStatistics(Map<AsymptoticResult.TYPE, Object> result) {
        this.result = result;
        m = ((double[][]) result.get(TYPE.M));
        permutationsize = getP();
        haplosize = getH();
        t = new TDistributionImpl(haplosize - 1);

    }

    private String getOutput() throws MathException {
        StringBuilder str = new StringBuilder();
        for (int pos = 0; pos < getPositionsize(); pos++) {

            double M = getM(pos);
            double T = getT(pos);
            double pi = 0;
            double pj = 0;
            double Mi;
            double Ti;

            for (int j = 0; j < permutationsize; j++) {

                Mi = getMi(pos, j);
                Ti = getTi(pos, j);
                pi = T < Ti ? pi + 1. : pi;
                pj = M < Mi ? pj + 1. : pj;

            }

            pi /= (new Double(permutationsize)).doubleValue() - 1.;
            pj /= (new Double(permutationsize)).doubleValue() - 1.;

            double p = getPM(pos);

            // str.append(Joiner.on(" ").join(i, getM(i), getAy(), getBy(),
            // getDy(), getGy(), getHy(), getKy(), getAx(i),
            str.append(Joiner.on(" ").join(pos, M, T, p, pi, pj, "\n"));
        }
        System.out.println(str);
        return str.toString();
    }

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
            String file = "test/" + outfile + ".hsm";
            Files.write(getOutput().getBytes(), new File(file));
            log.info("write output to {}", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPositionsize() {
        return ((double[]) result.get(TYPE.Ax)).length;
    }

    public Double getH() {
        return (Double) result.get(TYPE.H);
    }

    public Integer getP() {
        return (Integer) result.get(TYPE.P);
    }

    public double getAx(int pos) {
        return ((double[]) result.get(TYPE.Ax))[pos];
    }

    public double getBx(int pos) {
        return ((double[]) result.get(TYPE.Bx))[pos];
    }

    public double getDx(int pos) {
        return ((double[]) result.get(TYPE.Dx))[pos];
    }

    public Double getAy() {
        return (Double) result.get(TYPE.Ay);
    }

    public Double getBy() {
        return (Double) result.get(TYPE.By);
    }

    public Double getDy() {
        return (Double) result.get(TYPE.Dy);
    }

    /*
     * Gx := AxAx Hx := Dx – Bx Kx := Gx + 2Bx – 4Dx
     * 
     * Gy := AyAy Hy := Dy – By Ky := Gy + 2By – 4Dy
     */

    public double getGx(int pos) {
        return getAx(pos) * getAx(pos);
    }

    public double getHx(int pos) {
        return getDx(pos) - getBx(pos);
    }

    public double getKx(int pos) {
        return getGx(pos) + 2 * getBx(pos) - 4 * getDx(pos);
    }

    public double getGy() {
        return getAy() * getAy();
    }

    public double getHy() {
        return getDy() - getBy();
    }

    public double getKy() {
        return getGy() + 2 * getBy() - 4 * getDy();
    }

    // EM = Ax * Ay / (h*(h-1));
    @Override
    public double getEM(int pos) {
        return getAx(pos) * getAy() / (haplosize * (haplosize - 1));
    }

    /**
     * V = (2*Bx*By + 4*Hx*Hy/(h-2) + Kx*Ky/((h-2)) – GxGy/(h*(h-1)))/(h*(h-1))
     * Wenn V>0, dann ist SDM := sqrt(V) V = ( 2*bx*by + 4*hx*hy/ (n-2) + kx*ky/
     * ((n-2)*(n-3)) - gx*gy/ (n*(n-1)))/(n*(n-1));
     */
    @Override
    public double getSDM(int pos) {
        double vup1 = 2 * getBy() * getBx(pos) + 4 * getHx(pos) * getHy() / (haplosize - 2);
        double vup2 = getKx(pos) * getKy() / ((haplosize - 2) * (haplosize - 3));
        double vup3 = getGx(pos) * getGy() / (haplosize * (haplosize - 1));
        double vup = vup1 + vup2 - vup3;
        double vdown = (haplosize * (haplosize - 1));
        double v = vup / vdown;
        if (v > 0)
            return Math.sqrt(v);
        return 0;
    }

    private double getM(int pos) {
        return m[pos][0];
    }

    private double getMi(int pos, int i) {
        return m[pos][i];
    }

    /*
     * Wenn V>0, dann ist t=(M-EM)/SDM sonst t:= -99
     */
    @Override
    public double getT(int pos) {
        if (getSDM(pos) > 0) {
            return (getM(pos) - getEM(pos)) / getSDM(pos);
        }
        return -99;
    }

    /*
     * Wenn V>0, dann ist t=(M-EM)/SDM sonst t:= -99
     */
    @Override
    public double getTi(int pos, int i) {
        if (getSDM(pos) > 0) {
            return (getMi(pos, i) - getEM(pos)) / getSDM(pos);
        }
        return -99;
    }

    public double getPM(int idx) throws MathException {
        if (getSDM(idx) <= zero)
            return -99;
        return 1.0 - t.cumulativeProbability(getT(idx));
    }

}
