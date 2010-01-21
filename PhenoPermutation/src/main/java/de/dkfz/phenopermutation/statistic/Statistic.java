package de.dkfz.phenopermutation.statistic;

import org.apache.commons.math.MathException;

public interface Statistic {

    void writeOutput(String filename) throws MathException;

    double getT(int pos);

    double getSDM(int posidx);

    double getEM(int posidx);

    double getTi(int pos, int i);

}
