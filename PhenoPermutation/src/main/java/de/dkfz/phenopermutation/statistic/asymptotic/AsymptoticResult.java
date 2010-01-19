package de.dkfz.phenopermutation.statistic.asymptotic;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.dkfz.phenopermutation.Haplotype;
import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.Result;
import de.dkfz.phenopermutation.computation.Permutator;
import de.dkfz.phenopermutation.computation.SharingCalculator;

/**
 * result set contains the used permutations and all computed sums. The first
 * permutation is always the identity
 * 
 * @author mschmitt
 * 
 */
public class AsymptoticResult implements Result<Map<AsymptoticResult.TYPE, Object>> {
    private final static Logger log = LoggerFactory.getLogger(AsymptoticResult.class);

    public enum TYPE {
        Ax, Bx, Dx, Ay, By, Dy, M
    };

    // position
    final double[] ax;
    final double[] bx;
    final double[] dx;
    double ay;
    double by;
    double dy;

    final Map<TYPE, Object> result = Maps.newEnumMap(TYPE.class);

    final private Permutator[] permutators;
    private final int positionsize;

    private final Phenotype[] phenos;

    private final int personsize;
    private double[][] tmpDx;
    private double[] tmpDy;
    private final double[][] m;

    public AsymptoticResult(Phenotype[] phenos, int positionsize, int permutationsize, int personsize) {
        this.positionsize = positionsize;
        this.personsize = personsize;
        this.phenos = phenos;
        permutators = new Permutator[permutationsize];
        ax = new double[positionsize];
        bx = new double[positionsize];
        dx = new double[positionsize];
        tmpDx = new double[positionsize][personsize * 2];
        tmpDy = new double[personsize * 2];
        m = new double[positionsize][permutationsize];
        result.put(TYPE.Ax, ax);
        result.put(TYPE.Bx, bx);
        result.put(TYPE.Dx, dx);
        result.put(TYPE.Ay, ay);
        result.put(TYPE.By, by);
        result.put(TYPE.Dy, dy);
        result.put(TYPE.M, m);

        // first permutation is identity
        permutators[0] = new Permutator(personsize, true);
        for (int j = 1; j < permutationsize; j++) {
            permutators[j] = new Permutator(personsize);
        }

    }

    @Override
    public void comparePersons(Person person, Person person2) {
        double pheno1 = phenos[person.getPos()].getValue() - getMu();
        double pheno2 = phenos[person2.getPos()].getValue() - getMu();
        ay += pheno1 * pheno2 * 2;
        by += pheno1 * pheno2 * pheno1 * pheno2 * 2;
        tmpDy[person.getPos()] += pheno1 * pheno2;
        tmpDy[person2.getPos()] += pheno1 * pheno2;
        tmpDy[person.getPos() + personsize] += pheno1 * pheno2;
        tmpDy[person2.getPos() + personsize] += pheno1 * pheno2;
    }

    @Override
    public void finalizePersonRow(Person per1) {
        // nothing to do
        dy += getRowSumSquareDy(per1);
        tmpDy = new double[personsize * 2];

        for (int pos = 0; pos < positionsize; pos++) {
            dx[pos] += getRowSumSquareDx(per1, pos);
        }
        tmpDx = new double[positionsize][personsize * 2];

    }

    @Override
    public void addSharingValues(Haplotype h1, Haplotype h2, int per1id, int per2id) {
        // log.info("compare p1h1 p1h2");
        SharingCalculator calc = new SharingCalculator(h1, h2);
        Permutator[] perms = getPermutators();

        for (int pos = 0; pos < h1.getLength(); pos++) {
            int sharingvalue = calc.getNextSharing();
            ax[pos] += sharingvalue;
            bx[pos] += sharingvalue * sharingvalue;
            tmpDx[pos][per1id + (h1.isHaplo2() ? personsize : 0)] += sharingvalue;
            tmpDx[pos][per2id + (h2.isHaplo2() ? personsize : 0)] += sharingvalue;
            for (int permpos = 0; permpos < perms.length; permpos++) {
                double permpheno1 = phenos[perms[permpos].getMappedId(per1id)].getValue() - getMu();
                double permpheno2 = phenos[perms[permpos].getMappedId(per2id)].getValue() - getMu();
                m[per2id + (h2.isHaplo2() ? personsize : 0)][permpos] += sharingvalue + permpheno1 * permpheno2;
            }
        }

    }

    private double getRowSumSquareDx(Person per1, int pos) {
        double row1 = tmpDx[pos][per1.getPos()];
        double row2 = tmpDx[pos][personsize + per1.getPos()];
        return row1 * row1 + row2 * row2;
    }

    private double getRowSumSquareDy(Person per1) {
        double row1 = tmpDy[per1.getPos()];
        double row2 = tmpDy[per1.getPos() + personsize];
        return row1 * row1 + row2 * row2;
    }

    private double getMu() {
        return 0.01;
        // compute from phenos, cache
    }

    @Override
    public Map<AsymptoticResult.TYPE, Object> getResult() {
        return result;
    }

    @Override
    public Permutator[] getPermutators() {
        return permutators;
    }

}
