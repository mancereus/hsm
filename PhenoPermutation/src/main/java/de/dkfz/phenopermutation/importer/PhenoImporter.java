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

import de.dkfz.phenopermutation.Phenotype;

/**
 * import ped file or phenolist
 * 
 * @author mschmitt
 * 
 */
public class PhenoImporter {
    private final static Logger log = LoggerFactory.getLogger(PhenoImporter.class);

    public Phenotype[] importPhenos(File file) {
        log.info("importPhenos");
        List<Phenotype> phenos = Lists.newArrayList();
        try {
            LineReader reader = new LineReader(new FileReader(file));
            String line = null;
            int id = 0;
            while ((line = reader.readLine()) != null) {
                // ignore header
                if (id == 0) {
                    id++;
                    continue;
                }
                Iterable<String> split = Splitter.on(" ").omitEmptyStrings().split(line);
                // only the third value is needed
                Iterator<String> iterator = split.iterator();
                if (!iterator.hasNext())
                    continue;
                @SuppressWarnings("unused")
                String extid = iterator.next();
                @SuppressWarnings("unused")
                String sex = iterator.next();
                String value = iterator.next();
                Phenotype pheno = new Phenotype(id, Double.valueOf(value));
                phenos.add(pheno);
                id++;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("importPhenos finished: " + phenos.size());
        return phenos.toArray(new Phenotype[phenos.size()]);

    }

}
