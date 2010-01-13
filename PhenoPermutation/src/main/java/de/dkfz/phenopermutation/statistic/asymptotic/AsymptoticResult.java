package de.dkfz.phenopermutation.statistic.asymptotic;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.enums.ValueType;

import com.google.common.collect.Maps;

import de.dkfz.phenopermutation.Haplotype;
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
        Ax, Bx, Dx, Ay, By, Dy
    };

    final double[] ax;
    final double[] bx;
    final double[] dx;
    double ay;
    double by;
    final double[] dy;

    final Map<TYPE, Object> result = Maps.newEnumMap(TYPE.class);

    final private Permutator[] permutators;
    private final int permutationsize;
    private final int positionsize;

    private final Phenotype[] phenos;

    private final int personsize;
    private Matrix tmpDx;
    private Matrix tmpDy;

    public AsymptoticResult(Phenotype[] phenos, int positionsize, int permutationsize, int personsize) {
        this.permutationsize = permutationsize;
        this.positionsize = positionsize;
        this.personsize = personsize;
        this.phenos = phenos;
        permutators = new Permutator[permutationsize];
        ax = new double[positionsize];
        bx = new double[positionsize];
        dx = new double[positionsize];
        dy = new double[permutationsize];

        // first permutation is identity
        permutators[0] = new Permutator(personsize, true);
        for (int j = 1; j < permutationsize; j++) {
            permutators[j] = new Permutator(personsize);
        }
    }

    @Override
    public void addSharingValues(Haplotype h1, Haplotype h2, int per1id, int per2id) {
        // log.info("compare p1h1 p1h2");
        SharingCalculator calc = new SharingCalculator(h1, h2);
        Permutator[] perms = getPermutators();
        double pheno1 = phenos[per1id].getValue() - getMu();
        double pheno2 = phenos[per2id].getValue() - getMu();
        ay += pheno1 * pheno2 * 2;
        by += pheno1 * pheno2 * pheno1 * pheno2 * 2;

        int haplosize = personsize * 2;
        tmpDx = MatrixFactory.dense(ValueType.DOUBLE, positionsize, haplosize);
        tmpDy = MatrixFactory.dense(ValueType.DOUBLE, permutationsize, haplosize);

        for (int permpos = 0; permpos < perms.length; permpos++) {
            double permpheno1 = phenos[perms[permpos].getMappedId(per1id)].getValue() - getMu();
            double permpheno2 = phenos[perms[permpos].getMappedId(per2id)].getValue() - getMu();
            double oldvalue = tmpDy.getAsDouble(permpos, per2id);
            tmpDy.setAsDouble(oldvalue + permpheno1 * permpheno2, permpos, per2id + (h2.isHaplo2() ? personsize : 0));
        }
        for (int pos = 0; pos < h1.getLength(); pos++) {
            int sharingvalue = calc.getNextSharing();
            ax[pos] += sharingvalue;
            bx[pos] += sharingvalue * sharingvalue;
            double oldvalue = tmpDx.getAsDouble(pos, per1id);
            tmpDx.setAsDouble(oldvalue + sharingvalue, pos, per1id + (h1.isHaplo2() ? personsize : 0));
        }
        for (int pos = 0; pos < h1.getLength(); pos++) {
            dx[pos] = getRowSumSquareDx(pos);
        }
        for (int permpos = 0; permpos < perms.length; permpos++) {
            dy[permpos] = getRowSumSquareDy(permpos);
        }
        result.put(TYPE.Ax, ax);
        result.put(TYPE.Bx, bx);
        result.put(TYPE.Dx, dx);
        result.put(TYPE.Ay, ay);
        result.put(TYPE.By, by);
        result.put(TYPE.Dy, dy);

    }

    private double getRowSumSquareDx(int pos) {
        double rowsum = 0.0;
        for (int i = 0; i < personsize; i++) {
            // add haplo1 and haplo2;
            rowsum += tmpDx.getAsDouble(pos, i) + tmpDx.getAsDouble(pos, personsize + i);
        }
        // square
        return rowsum * rowsum;
    }

    private double getRowSumSquareDy(int perm) {
        double rowsum = 0.0;
        for (int i = 0; i < personsize; i++) {
            // add haplo1 and haplo2;
            rowsum += tmpDy.getAsDouble(perm, i) + tmpDy.getAsDouble(perm, personsize + i);
        }
        // square
        return rowsum * rowsum;
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
