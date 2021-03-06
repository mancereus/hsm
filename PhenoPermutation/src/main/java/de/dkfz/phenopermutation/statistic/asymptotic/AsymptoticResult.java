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
        Ax, Bx, Dx, Ay, By, Dy, M, H, P
    };

    // position
    final double[] ax;
    final double[] bx;
    final double[] dx;
    final double h;
    final int p;
    Double ay = new Double(0.0);
    Double by = new Double(0.0);
    Double dy = new Double(0.0);

    final Map<TYPE, Object> result = Maps.newEnumMap(TYPE.class);

    final private Permutator[] permutators;
    private final int positionsize;

    private final Phenotype[] phenos;

    private final int personsize;
    private final double[][] tmpDx;
    private final double[] tmpDy;
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
        h = personsize * 2.;
        p = permutationsize;
        result.put(TYPE.Ax, ax);
        result.put(TYPE.Bx, bx);
        result.put(TYPE.Dx, dx);
        result.put(TYPE.Ay, ay);
        result.put(TYPE.By, by);
        result.put(TYPE.Dy, dy);
        result.put(TYPE.M, m);
        result.put(TYPE.H, h);
        result.put(TYPE.P, p);

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

        double pheno12 = pheno1 * pheno2;

        ay += pheno12 * 8;
        by += pheno12 * pheno12 * 8;
        result.put(TYPE.Ay, ay);
        result.put(TYPE.By, by);
        tmpDy[person.getPos()] += 2 * pheno1 * pheno2;
        tmpDy[person2.getPos()] += 2 * pheno1 * pheno2;
        tmpDy[person.getPos() + personsize] += 2 * pheno1 * pheno2;
        tmpDy[person2.getPos() + personsize] += 2 * pheno1 * pheno2;
    }

    @Override
    public void comparePersonsPheno(Person person) {
        double pheno1 = phenos[person.getPos()].getValue() - getMu();
        double pheno12 = pheno1 * pheno1;

        ay += pheno12 * 2;
        by += pheno12 * pheno12 * 2;
        result.put(TYPE.Ay, ay);
        result.put(TYPE.By, by);
        tmpDy[person.getPos()] += pheno12;
        tmpDy[person.getPos() + personsize] += pheno12;
    }

    @Override
    public void finalizePersonRow(Person per1) {

        dy += getRowSumSquareDy(per1);

        for (int pos = 0; pos < positionsize; pos++) {
            dx[pos] += getRowSumSquareDx(per1, pos);
        }
        result.put(TYPE.Dy, dy);
    }

    @Override
    public void addSharingValues(Haplotype h1, Haplotype h2, int per1id, int per2id) {
        // log.info("compare p1h1 p1h2");
        SharingCalculator calc = new SharingCalculator(h1, h2);
        Permutator[] perms = getPermutators();

        for (int pos = 0; pos < h1.getLength(); pos++) {
            int sharingvalue = calc.getNextSharing();
            ax[pos] += 2 * sharingvalue;
            bx[pos] += 2 * sharingvalue * sharingvalue;
            tmpDx[pos][per1id + (h1.isHaplo2() ? personsize : 0)] += sharingvalue;
            tmpDx[pos][per2id + (h2.isHaplo2() ? personsize : 0)] += sharingvalue;
            for (int permpos = 0; permpos < perms.length; permpos++) {
                double permpheno1 = phenos[perms[permpos].getMappedId(per1id)].getValue() - getMu();
                double permpheno2 = phenos[perms[permpos].getMappedId(per2id)].getValue() - getMu();
                m[pos][permpos] += 2 * sharingvalue * permpheno1 * permpheno2;
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

        double mu = 0.4622642;
        return mu;
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
