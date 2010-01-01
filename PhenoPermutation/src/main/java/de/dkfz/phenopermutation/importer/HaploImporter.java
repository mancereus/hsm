package de.dkfz.phenopermutation.importer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.LineReader;

import de.dkfz.phenopermutation.Haplotype;
import de.dkfz.phenopermutation.Person;

public class HaploImporter {
    private final static Logger log = LoggerFactory.getLogger(HaploImporter.class);

    public HaploImporter() {

    }

    public Person[] importHaplos(File file) {
        log.info("importHaplos");
        List<Person> persons = Lists.newArrayList();
        try {
            LineReader reader = new LineReader(new FileReader(file));
            String line = null;

            // ignore first row
            reader.readLine();

            // number of rows must be a multiple of 3
            while ((line = reader.readLine()) != null) {
                Iterable<String> split = Splitter.on(" ").omitEmptyStrings().split(line);
                // only the first value is needed
                Iterator<String> iterator = split.iterator();
                if (!iterator.hasNext())
                    continue;
                int id = Integer.parseInt(iterator.next());
                // second row
                line = reader.readLine();
                // System.out.println("haplo1: " + line);
                Haplotype h1 = getHaplo(line);
                // third row
                line = reader.readLine();
                Haplotype h2 = getHaplo(line);
                persons.add(new Person(id, h1, h2));
                id++;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("importHaplos finished: " + persons.size());

        return persons.toArray(new Person[persons.size()]);
    }

    private Haplotype getHaplo(String s) {

        int bitlength = s.length();
        Haplotype h = new Haplotype(bitlength);

        Iterable<String> splits = Splitter.on('0').split(s);
        int count = 0;
        for (String onestr : splits) {
            if (onestr.length() == 0) {
                count++;
                continue;
            }
            for (int i = 0; i < onestr.length(); i++) {
                h.set(count);
                count++;
            }
            // new String means one "0"
            count++;
        }
        return h;
    }
}
