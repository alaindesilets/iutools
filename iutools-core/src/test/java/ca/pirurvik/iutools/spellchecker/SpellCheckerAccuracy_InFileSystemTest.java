package ca.pirurvik.iutools.spellchecker;

import ca.pirurvik.iutools.corpus.CompiledCorpusRegistry;
import org.junit.Ignore;

@Ignore
public class SpellCheckerAccuracy_InFileSystemTest extends SpellCheckerAccuracyTest {

    @Override
    protected String usingCorpus() {
        return "HANSARD-1999-2002.v2020-07-19";
    }
}