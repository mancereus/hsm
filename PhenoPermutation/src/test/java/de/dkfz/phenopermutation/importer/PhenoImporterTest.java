package de.dkfz.phenopermutation.importer;

import java.io.File;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.dkfz.phenopermutation.Phenotype;

public class PhenoImporterTest {

    private PhenoImporter importer;

    @Before
    public void setup() {
        importer = new PhenoImporter();
    }

    @Test
    public void testImporter() {
        Phenotype[] phenos = importer.importPhenos(new File("src/test/resources/phenotest.ga"));
        Assert.assertNotNull(phenos);
        Assert.assertEquals(6, phenos.length);
        Phenotype phenotype = phenos[1];
        Assert.assertNotNull(phenotype);
        Assert.assertEquals(2, phenotype.getId());
        System.out.println(phenotype.getValue());
        Assert.assertEquals(phenotype.getValue(), -0.499193);
    }

}
