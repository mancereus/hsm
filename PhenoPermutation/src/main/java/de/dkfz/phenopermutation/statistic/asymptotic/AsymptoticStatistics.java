package de.dkfz.phenopermutation.statistic.asymptotic;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math.MathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.io.Files;

import de.dkfz.phenopermutation.statistic.Statistic;
import de.dkfz.phenopermutation.statistic.asymptotic.AsymptoticResult.TYPE;

public class AsymptoticStatistics implements Statistic {

    private final static Logger log = LoggerFactory.getLogger(AsymptoticStatistics.class);

    private final Map<TYPE, Object> result;

    public AsymptoticStatistics(Map<AsymptoticResult.TYPE, Object> result) {
        this.result = result;
    }

    private String getOutput() throws MathException {
        StringBuilder str = new StringBuilder();
        // for (int i = 0; i < getAx().; i++) {
        // str.append(Joiner.on(" ").join(i, getM(i), getTM(i), getPM(i),
        // "\n"));
        // }
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

    public double getDy(int permpos) {
        return ((double[]) result.get(TYPE.Dy))[permpos];
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

    public double getHy(int permpos) {
        return getDy(permpos) - getBy();
    }

    public double getKy(int permpos) {
        return getGy() + 2 * getBy() - 4 * getDy(permpos);
    }
}
