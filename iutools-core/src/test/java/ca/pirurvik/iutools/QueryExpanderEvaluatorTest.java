package ca.pirurvik.iutools;


import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ca.nrc.datastructure.trie.StringSegmenter_IUMorpheme;
import ca.pirurvik.iutools.QueryExpanderEvaluator;
import ca.pirurvik.iutools.corpus.CompiledCorpus_InMemory;

public class QueryExpanderEvaluatorTest {

	@Test(expected=FileNotFoundException.class)
	public void test__QueryExpanderEvaluator__Synopsis() throws Exception {
		
		String compiledCorpusTrieFilePath = "/path/to/json/file/of/compiled/corpus";
		String goldStandardCSVFilePath = "/path/to/gold/standard/csv/file";
		QueryExpanderEvaluator evaluator = 
			new QueryExpanderEvaluator(compiledCorpusTrieFilePath,goldStandardCSVFilePath);
		// if statistics are to be computed over morphemes instead of words:
		evaluator.setOptionComputeStatsOverSurfaceForms(true);
		evaluator.run();
	}
}
