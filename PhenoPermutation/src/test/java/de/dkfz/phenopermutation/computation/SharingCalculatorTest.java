package de.dkfz.phenopermutation.computation;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.dkfz.phenopermutation.Haplotype;

public class SharingCalculatorTest {

    private SharingCalculator calc;

    @Before
    public void setup() {
        Haplotype h1 = new Haplotype(5);
        Haplotype h2 = new Haplotype(5);
        h1.set(3);
        calc = new SharingCalculator(h1, h2);
    }

    @Test
    public void testGetSideSums() {

        Assert.assertEquals(3, calc.getNextSharing());
        calc.getNextSharing();
        Assert.assertEquals(3, calc.getNextSharing());
        Assert.assertEquals(0, calc.getNextSharing());
        Assert.assertEquals(1, calc.getNextSharing());

    }

}
