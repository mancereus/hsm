package de.dkfz.phenopermutation.statistic.asymptotic;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math.MathException;
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

    private int haplosize;

    public AsymptoticStatistics(Map<AsymptoticResult.TYPE, Object> result) {
        this.result = result;
    }

    private String getOutput() throws MathException {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < getPositionsize(); i++) {
            str.append(Joiner.on(" ").join(i, getM(i), getT(i), getEM(i), "\n"));
        }
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
            String file = "src/test/resources/" + outfile + ".hsm";
            Files.write(getOutput().getBytes(), new File(file));
            log.info("write output to {}", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPositionsize() {
        return ((double[]) result.get(TYPE.Ax)).length;
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
     * Gx := Ax*Ax Hx := Dx – Bx Kx := Gx + 2*Bx – 4*Dx
     * 
     * Gy := Ay*Ay Hy := Dy – By Ky := Gy + 2*By – 4*Dy
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
     * Wenn V>0, dann ist SDM := sqrt(V)
     */
    @Override
    public double getSDM(int pos) {
        double vup1 = 2 * getBy() * getBx(pos) + 4 * getHx(pos) * getHy() / (haplosize - 2);
        double vup2 = getKx(pos) * getKy() / (haplosize - 2);
        double vup3 = getGx(pos) * getGy() / (haplosize * (haplosize - 1));
        double vup = vup1 + vup2 - vup3;
        double vdown = (haplosize * (haplosize - 1));
        double v = vup / vdown;
        if (v > 0)
            return Math.sqrt(v);
        return 0;
    }

    private double getM(int pos) {
        // FIXME: compute M
        return 0;
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

}
