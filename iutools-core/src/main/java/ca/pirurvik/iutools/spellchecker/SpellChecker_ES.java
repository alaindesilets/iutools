package ca.pirurvik.iutools.spellchecker;

import ca.nrc.datastructure.trie.StringSegmenterException;
import ca.pirurvik.iutools.corpus.CompiledCorpusException;
import ca.pirurvik.iutools.corpus.CompiledCorpus_ES;

public class SpellChecker_ES extends SpellChecker {

    private static final String DEFAULT_CHECKER_INDEX = "spell_checker";

    protected String esIndexNameRoot = null;

    public SpellChecker_ES() throws StringSegmenterException, SpellCheckerException {
        super();
        init_SpellChecker_ES(null);
    }

    public SpellChecker_ES(String _checkerIndexName) throws StringSegmenterException, SpellCheckerException {
        super();
        init_SpellChecker_ES(_checkerIndexName);
    }

    private void init_SpellChecker_ES(String _checkerIndexName) throws SpellCheckerException {
        if (_checkerIndexName == null) {
            _checkerIndexName = DEFAULT_CHECKER_INDEX;
        }

        esIndexNameRoot = _checkerIndexName;

        try {
            corpus = new CompiledCorpus_ES(corpusIndexName());
            explicitlyCorrectWords =
                new CompiledCorpus_ES(
                    explicitlyCorrectWordsIndexName());
        } catch (CompiledCorpusException e) {
            throw new SpellCheckerException(e);
        }
    }

    protected String explicitlyCorrectWordsIndexName() {
        return esIndexNameRoot+"_EXPLICLTY_CORRECT";
    }

    protected String corpusIndexName() {
        return esIndexNameRoot;
    }
}
