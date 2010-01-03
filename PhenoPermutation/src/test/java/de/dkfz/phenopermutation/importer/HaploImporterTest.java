package de.dkfz.phenopermutation.importer;

import java.io.File;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.dkfz.phenopermutation.Haplotype;
import de.dkfz.phenopermutation.Person;

public class HaploImporterTest {

    private HaploImporter importer;

    @Before
    public void setup() {
        importer = new HaploImporter();
    }

    @Test
    public void testImporter() {
        Person[] haplos = importer.importHaplos(new File("src/test/resources/haplotest.dat"));
        Assert.assertNotNull(haplos);
        Assert.assertTrue(haplos.length == 6);
        Person person = haplos[0];
        Assert.assertNotNull(person);
        System.out.println(person.getId());
        Assert.assertTrue(person.getId() == 1);
        Haplotype haplo1 = person.getHaplo1();
        Assert.assertTrue(haplo1.get(12));
        Assert.assertTrue(haplo1.get(14));
        Assert.assertTrue(haplo1.get(22));
        Assert.assertTrue(haplo1.get(8369));
    }

}
