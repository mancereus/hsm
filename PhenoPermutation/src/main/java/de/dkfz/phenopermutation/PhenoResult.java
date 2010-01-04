package de.dkfz.phenopermutation;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.dkfz.phenopermutation.computation.Permutator;

/**
 * result set contains the used permutations and all computed sums. The first
 * permutaion is always the identity
 * 
 * @author mschmitt
 * 
 */
public class PhenoResult {
    private final static Logger log = LoggerFactory.getLogger(PhenoResult.class);

    final double[] result;
    final private Permutator[] permutators;
    private final int permutationsize;
    private final int positionsize;

    public PhenoResult(int positionsize, int permutationsize, int personsize) {
        this.permutationsize = permutationsize;
        this.positionsize = positionsize;
        permutators = new Permutator[permutationsize];
        result = new double[positionsize * permutationsize];
        // first permutation is identity
        permutators[0] = new Permutator(personsize, true);
        for (int j = 1; j < permutationsize; j++) {
            permutators[j] = new Permutator(personsize);
        }
        log.info("initialize result array [{}]", result.length);
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
