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
public class AsymptoticResult implements Result<double[], AsymptoticResult.TYPE> {
    private final static Logger log = LoggerFactory.getLogger(AsymptoticResult.class);

    enum TYPE {
        Ax, Bx, Dx, Ay, By, Dy
    };

    final Matrix result;

    // slow because of multidimensional array
    final Matrix tmpDy;
    final Matrix tmpDx;
    final private Permutator[] permutators;
    private final int permutationsize;
    private final int positionsize;

    private final Phenotype[] phenos;

    private final int personsize;

    public AsymptoticResult(Phenotype[] phenos, int positionsize, int permutationsize, int personsize) {
        this.permutationsize = permutationsize;
        this.positionsize = positionsize;
        this.personsize = personsize;
        this.phenos = phenos;
        permutators = new Permutator[permutationsize];
        result = MatrixFactory.dense(ValueType.DOUBLE, positionsize, permutationsize, TYPE.values().length);
        int haplosize = personsize * 2;
        tmpDy = MatrixFactory.dense(ValueType.DOUBLE, positionsize, permutationsize, haplosize);
        tmpDx = MatrixFactory.dense(ValueType.DOUBLE, positionsize, permutationsize, haplosize);
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
        for (int pos = 0; pos < h1.getLength(); pos++) {
            int sharingvalue = calc.getNextSharing();
            for (int permpos = 0; permpos < perms.length; permpos++) {
                double pheno1 = phenos[perms[permpos].getMappedId(per1id)].getValue() - getMu();
                double pheno2 = phenos[perms[permpos].getMappedId(per2id)].getValue() - getMu();
                addResult(pos, permpos, sharingvalue, TYPE.Ax);
                addResult(pos, permpos, sharingvalue * sharingvalue, TYPE.Bx);
                addResult(pos, permpos, sharingvalue * pheno1 * pheno2, TYPE.Ay);
                addResult(pos, permpos, sharingvalue * sharingvalue * pheno1 * pheno2, TYPE.By);
                addTmpDx(pos, permpos, per1id, h1.isHaplo2(), sharingvalue);
                addTmpDy(pos, permpos, per2id, h2.isHaplo2(), sharingvalue * pheno1 * pheno2);
            }
        }
        for (int pos = 0; pos < h1.getLength(); pos++) {
            for (int permpos = 0; permpos < perms.length; permpos++) {
                addResult(pos, permpos, getRowSumSquare(pos, permpos, TYPE.Dx), TYPE.Dx);
                addResult(pos, permpos, getRowSumSquare(pos, permpos, TYPE.Dy), TYPE.Dy);
            }
        }

    }

    private double getRowSumSquare(int pos, int permpos, TYPE type) {
        double rowsum = 0.0;
        for (int i = 0; i < personsize; i++) {
            switch (type) {
            case Dx:
                // add haplo1 and haplo2;
                rowsum += tmpDx.getAsDouble(pos, permpos, i) + tmpDx.getAsDouble(pos, permpos, personsize + i);
                break;
            case Dy:
                // add haplo1 and haplo2;
                rowsum += tmpDy.getAsDouble(pos, permpos, i) + tmpDy.getAsDouble(pos, permpos, personsize + i);
                break;
            default:
                break;
            }
        }
        // square
        return rowsum * rowsum;
    }

    private void addTmpDx(int pos, int permpos, int perid, boolean isHaplo2, double sharingvalue) {
        double oldvalue = tmpDx.getAsDouble(pos, permpos, perid);
        tmpDx.setAsDouble(oldvalue + sharingvalue, pos, permpos, perid + (isHaplo2 ? personsize : 0));
    }

    private void addTmpDy(int pos, int permpos, int perid, boolean isHaplo2, double sharingvalue) {
        double oldvalue = tmpDy.getAsDouble(pos, permpos, perid);
        tmpDy.setAsDouble(oldvalue + sharingvalue, pos, permpos, perid + (isHaplo2 ? personsize : 0));
    }

    private double getMu() {
        return 0.01;
        // compute from phenos, cache
    }

    private void addResult(int pos, int permindex, double resvalue, TYPE type) {
        double oldvalue = result.getAsDouble(pos, permindex, type.ordinal());
        result.setAsDouble(oldvalue + resvalue, pos, permindex, type.ordinal());
    }

    @Override
    public Map<Permutator, double[]> getPermutatorData(TYPE type) {
        Map<Permutator, double[]> res = Maps.newHashMap();
        for (int i = 0; i < permutationsize; i++) {
            double[] resarr = new double[positionsize];
            for (int j = 0; j < positionsize; j++) {
                resarr[j] = result.getAsDouble(j, i, type.ordinal());
            }
            res.put(permutators[i], resarr);
        }
        return res;
    }

    @Override
    public Permutator[] getPermutators() {
        return permutators;
    }

}
