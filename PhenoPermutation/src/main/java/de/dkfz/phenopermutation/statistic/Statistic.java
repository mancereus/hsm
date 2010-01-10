package de.dkfz.phenopermutation.statistic;

import org.apache.commons.math.MathException;

public interface Statistic {

    void writeOutput(String filename) throws MathException;

}
