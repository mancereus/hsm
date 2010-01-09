package de.dkfz.phenopermutation;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.dkfz.phenopermutation.computation.Permutator;
import de.dkfz.phenopermutation.computation.SharingCalculator;

/**
 * result set contains the used permutations and all computed sums. The first
 * permutaion is always the identity
 * 
 * @author mschmitt
 * 
 */
public class PhenoResult implements Result {
    private final static Logger log = LoggerFactory.getLogger(PhenoResult.class);

    final double[] result;
    final private Permutator[] permutators;
    private final int permutationsize;
    private final int positionsize;

    private final Phenotype[] phenos;

    public PhenoResult(Phenotype[] phenos, int positionsize, int permutationsize, int personsize) {
        this.permutationsize = permutationsize;
        this.positionsize = positionsize;
        this.phenos = phenos;
        permutators = new Permutator[permutationsize];
        result = new double[positionsize * permutationsize];
        // first permutation is identity
        permutators[0] = new Permutator(personsize, true);
        for (int j = 1; j < permutationsize; j++) {
            permutators[j] = new Permutator(personsize);
        }
        log.info("initialize result array [{}]", result.length);
    }

    public void addSharingValues(Haplotype h1, Haplotype h2, int per1id, int per2id) {
        // log.info("compare p1h1 p1h2");
        SharingCalculator calc = new SharingCalculator(h1, h2);
        for (int i = 0; i < h1.getLength(); i++) {
            Permutator[] perms = getPermutators();
            int sharingvalue = calc.getNextSharing();
            for (int k = 0; k < perms.length; k++) {
                double pheno1 = phenos[perms[k].getMappedId(per1id)].getValue() - getMu();
                double pheno2 = phenos[perms[k].getMappedId(per2id)].getValue() - getMu();
                addResult(i, k, sharingvalue * pheno1 * pheno2);
            }
        }
    }

    public double getMu() {
        return 0.01;
        // compute from phenos, cache
    }

    public void addResult(int pos, int permutation, double resvalue) {
        int arrindex = pos * permutationsize + permutation;
        result[arrindex] += resvalue;
    }

    public Permutator getPermutator(int permutationindex) {
        return permutators[permutationindex];
    }

    public Map<Permutator, double[]> getPermutatorSum() {
        Map<Permutator, double[]> res = Maps.newHashMap();
        for (int i = 0; i < permutationsize; i++) {
            double[] resarr = new double[positionsize];
            for (int j = 0; j < positionsize; j++) {
                int arrindex = j * permutationsize + i;
                resarr[j] = result[arrindex];
            }
            res.put(permutators[i], resarr);
        }
        return res;
    }

    public Permutator[] getPermutators() {
        return permutators;
    }

}
