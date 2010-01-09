package de.dkfz.phenopermutation.computation;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import de.dkfz.phenopermutation.Haplotype;
import de.dkfz.phenopermutation.Person;
import de.dkfz.phenopermutation.Phenotype;
import de.dkfz.phenopermutation.Result;
import de.dkfz.phenopermutation.statistic.test.SharingResult;

public class PhenoComputationTest {

    private List<Person> persons;
    private int positionsize;

    @Before
    public void setup() {
        // HaploImporter importer = new HaploImporter();
        // persons = importer.importHaplos(new
        // File("src/test/resources/haplotest.dat"));
        persons = Lists.newArrayList();
        positionsize = 6;
        Haplotype h1 = new Haplotype(positionsize);
        Haplotype h2 = new Haplotype(positionsize);
        Haplotype h3 = new Haplotype(positionsize);
        Haplotype h4 = new Haplotype(positionsize);
        h1.set(0);
        h1.set(4, false);
        Person person0 = new Person(1, h1, h2);
        Person person1 = new Person(2, h3, h4);
        persons.add(person0);
        persons.add(person1);

    }

    @Test
    public void calculateSharing() {
        Phenotype[] phenos = new Phenotype[persons.size()];
        for (int i = 0; i < phenos.length; i++) {
            phenos[i] = new Phenotype(i, 1.0);
        }
        Person[] personsarr = persons.toArray(new Person[persons.size()]);
        int haplosize = personsarr[0].getHaplo1().getLength();
        Result result = new SharingResult(phenos, haplosize, 2, personsarr.length);

        HaploSharingComparator pc = new HaploSharingComparator(result, personsarr);
        pc.calculateSharing();
    }
}
