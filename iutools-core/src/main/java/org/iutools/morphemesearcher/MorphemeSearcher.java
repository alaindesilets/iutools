package org.iutools.morphemesearcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import ca.nrc.json.PrettyPrinter;
import org.iutools.corpus.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.iutools.linguisticdata.LinguisticDataException;
import org.iutools.morph.Decomposition;
import org.iutools.morph.Decomposition.DecompositionExpression;
import org.iutools.morph.MorphInukException;
import org.iutools.morph.MorphologicalAnalyzer;
import org.iutools.morph.MorphologicalAnalyzerException;

public class MorphemeSearcher {
	
	protected CompiledCorpus corpus = null;
	protected int nbWordsToBeDisplayed = 20;
	protected int maxNbInitialCandidates = 100;
	
	public MorphemeSearcher() throws MorphemeSearcherException {
		try {
			useCorpus(CompiledCorpusRegistry.getCorpusWithName());
		} catch (IOException | CompiledCorpusRegistryException e) {
			throw new MorphemeSearcherException(e);
		}
	}
	
	public void useCorpus(CompiledCorpus _corpus) throws IOException {
		corpus = _corpus;
	}
	
	public void setNbDisplayedWords(int n) {
		this.nbWordsToBeDisplayed = n;
	}
	
	public List<MorphSearchResults> wordsContainingMorpheme(String morpheme) throws Exception {
		Logger tLogger = Logger.getLogger("org.iutools.morphemesearcher.MorphemeSearcher.wordsContainingMorpheme");
		tLogger.trace("morpheme= "+morpheme);
		
		HashMap<String,List<WordWithMorpheme>> morphid2wordsFreqs =
			mostFrequentWordsWithMorpheme(morpheme);
		Bin[] rootBins = separateWordsByRoot(morphid2wordsFreqs);

		HashMap<String,List<ScoredExample>> morphids2scoredExamples = null;
		try {
			morphids2scoredExamples = computeWordsWithScoreFromBins(rootBins);
		} catch (Exception e) {
			throw new MorphemeSearcherException(e);
		}
		tLogger.trace("morphids2scoredExamples: "+morphids2scoredExamples.size());
		List<MorphSearchResults> words = new ArrayList<MorphSearchResults>();
		Set<String> keys = morphids2scoredExamples.keySet();
		Iterator<String> iter = keys.iterator();
		while ( iter.hasNext()) {
			String morphId = iter.next();
			tLogger.trace("iteration for generation of Words - morphId: "+morphId);
			List<ScoredExample> scoredExamples = morphids2scoredExamples.get(morphId);
			MorphSearchResults wordsObject = new MorphSearchResults(morphId,scoredExamples);
			words.add(wordsObject);
		}
		tLogger.trace("words: "+words.size());
		return words;
	}
	
	
	protected HashMap<String, List<ScoredExample>> computeWordsWithScoreFromBins(
			Bin[] rootBins) throws MorphemeSearcherException {
		Logger tLogger = Logger.getLogger("org.iutools.morphemesearcher.MorphemeSearcher.computeWordsWithScoreFromBins");
		
		HashMap<String, List<ScoredExample>> morphids2limitedScoredExamples = 
				new HashMap<String, List<ScoredExample>>();
		for (int ib=0; ib<rootBins.length; ib++) {
			tLogger.trace("Looking at bin #"+ib);
			HashMap<String, List<ScoredExample>> morphid2scoredWords = 
					computeWordsWithScore(rootBins[ib].morphid2wordsFreqs);
			tLogger.trace("Finished scoring words in bin");

			for (Map.Entry<String,List<ScoredExample>> mapElement : morphid2scoredWords.entrySet()) { 
	            String morphid = mapElement.getKey(); 
				tLogger.trace("Looking at bin #"+ib+"; morphid="+morphid);
	            List<ScoredExample> listOfScoredExamples = mapElement.getValue();
				if (tLogger.isTraceEnabled()) {
					tLogger.trace("Finished scoring words in bin. "+
						"listOfScoredExamples.size()="+listOfScoredExamples.size());
				}

				if (!listOfScoredExamples.isEmpty()) {
					tLogger.trace("Bin has some examples");
		            ScoredExample firstScoredExampleInBinForMorphid = 
		            		listOfScoredExamples.get(0);
		            if ( !morphids2limitedScoredExamples.containsKey(morphid) ) {
		            	morphids2limitedScoredExamples.put(morphid, new ArrayList<ScoredExample>());
		            }
		            if (morphids2limitedScoredExamples.get(morphid).size() < nbWordsToBeDisplayed)
		            	morphids2limitedScoredExamples.get(morphid).add(firstScoredExampleInBinForMorphid);
				} else {
					tLogger.trace("Bin is EMPTY");					
				}
			}
		}
		
		return morphids2limitedScoredExamples;
	}

	protected Bin[] separateWordsByRoot(HashMap<String, List<WordWithMorpheme>> morphid2wordsFreqs) {
		HashMap<String,Bin> bins = new HashMap<String,Bin>();
		Set<String> keys = morphid2wordsFreqs.keySet();
		Iterator<String> iter = keys.iterator();
		while ( iter.hasNext() ) {
			String morphId = iter.next();
			List<WordWithMorpheme> wordWithMorphemeS = morphid2wordsFreqs.get(morphId);
			for (int im=0; im<wordWithMorphemeS.size(); im++) {
				WordWithMorpheme wordWithMorpheme = wordWithMorphemeS.get(im);
				DecompositionExpression decomposition = new DecompositionExpression(wordWithMorpheme.decomposition);
				String rootId = decomposition.parts[0].morphid;
				Bin bin = null;
				if ( !bins.containsKey(rootId) ) {
					bin = new Bin(rootId, new HashMap<String,List<WordWithMorpheme>>());
					bins.put(rootId, bin);
				} 
				bin = bins.get(rootId);
				if ( !bin.morphid2wordsFreqs.containsKey(morphId) ) {
					bin.morphid2wordsFreqs.put(morphId, new ArrayList<WordWithMorpheme>());
				}
				List<WordWithMorpheme> list = bin.morphid2wordsFreqs.get(morphId);
				list.add(wordWithMorpheme);
				bin.morphid2wordsFreqs.put(morphId,list);
			}
		}
		
		return bins.values().toArray(new Bin[] {});
	}

	private HashMap<String, List<ScoredExample>> computeWordsWithScore(HashMap<String,
		List<WordWithMorpheme>> mostFrequentWordsWithMorpheme) throws MorphemeSearcherException {
		Logger logger = Logger.getLogger("org.iutools.morphemesearcher.MorphemeSearcher.computeWordsWithScore");
		if (logger.isTraceEnabled()) {
			logger.trace("invoked with mostFrequentWordsWithMorpheme.size()="+mostFrequentWordsWithMorpheme.size());
		}
		HashMap<String, List<ScoredExample>> morphids2scoredExamples = new HashMap<String, List<ScoredExample>>();
		Set<String> morphIds = mostFrequentWordsWithMorpheme.keySet();
		Iterator<String> iter = morphIds.iterator();
		while ( iter.hasNext() ) {
			List<ScoredExample> scoredExamples = new ArrayList<ScoredExample>();
			String morphId = iter.next();
			List<WordWithMorpheme> wordsWithMorpheme = mostFrequentWordsWithMorpheme.get(morphId);
			logger.trace("wordsFreqs.size()="+wordsWithMorpheme.size());
			for (int iwf=0; iwf<wordsWithMorpheme.size(); iwf++) {
				logger.trace("iwf: "+iwf);
				WordWithMorpheme morphemeExample = wordsWithMorpheme.get(iwf);
				try {
					ScoredExample scoredEx =
						generateScoredExample(morphemeExample);
					scoredExamples.add(scoredEx);
				} catch (MorphemeSearcherException e) {
					// generateScoredExample calls the morphpological analyzer, 
					// which may time out; catch the exception, do not register a 
					// scored example and continue with next word
				}
			}
			Collections.sort(scoredExamples, ScoredExamplesComparator);
				
			morphids2scoredExamples.put(morphId, scoredExamples);
		}
		
		return morphids2scoredExamples;
	}

	public static Comparator<ScoredExample> ScoredExamplesComparator = new Comparator<ScoredExample>() {

		public int compare(ScoredExample s1, ScoredExample s2) {
			if (s1.score < s2.score)
				return 1;
			else if (s1.score > s2.score)
				return -1;
			else
				return 0;
	   }};	
	   
	private HashMap<String,List<WordWithMorpheme>>
		mostFrequentWordsWithMorpheme(String morpheme)
		throws MorphemeSearcherException {
		Logger tLogger = Logger.getLogger("org.iutools.morphemesearcher.MorphemeSearcher.mostFrequentWordsWithMorpheme");
		if (tLogger.isTraceEnabled()) {
			tLogger.trace("Using corpus="+ PrettyPrinter.print(corpus));
		}

		List<WordWithMorpheme> wordsWithMorpheme;
		try {
			wordsWithMorpheme = this.corpus.wordsContainingMorpheme(morpheme);
		} catch (CompiledCorpusException e) {
			throw new MorphemeSearcherException(e);
		}
		HashMap<String,List<WordWithMorpheme>> morphid2WordsFreqs = new HashMap<String,List<WordWithMorpheme>>();
		for (WordWithMorpheme wordAndMorphid: wordsWithMorpheme) {
			String word = wordAndMorphid.word;
			String morphId = wordAndMorphid.morphemeId;
			List<WordWithMorpheme> wordsFreqs = morphid2WordsFreqs.get(morphId);
			if (wordsFreqs==null) {
				wordsFreqs = new ArrayList<WordWithMorpheme>();
			}
			wordsFreqs.add(wordAndMorphid);
			morphid2WordsFreqs.put(morphId, wordsFreqs);
		}
		
		Set<String> morphIds = morphid2WordsFreqs.keySet();
		Iterator<String> iter = morphIds.iterator();
		while (iter.hasNext()) {
			String morphId = iter.next();
			List<WordWithMorpheme> wordsFreqs = morphid2WordsFreqs.get(morphId);
			Collections.sort(wordsFreqs, (WordWithMorpheme p1, WordWithMorpheme p2) -> {
				int cmp = Long.compare(p2.frequency, p1.frequency);
				if (cmp == 0) {
					cmp = Integer.compare(p1.word.length(), p2.word.length());
				}
				if (cmp == 0) {
					cmp = p1.word.compareTo(p2.word);
				}
				return cmp;
			});
			
			int truncateSize = 
					Math.min(wordsFreqs.size(), maxNbInitialCandidates);
			morphid2WordsFreqs.put(morphId,wordsFreqs.subList(0, truncateSize));
		}

		return morphid2WordsFreqs;
	}

	
	private ScoredExample generateScoredExample(WordWithMorpheme morphemeExample) throws MorphemeSearcherException {
		Logger logger = Logger.getLogger("org.iutools.morphemesearcher.MorphemeSearcher.generateScoredExample");
		ScoredExample scoredEx = null;
		try {
			boolean allowAnalysisWithAdditionalFinalConsonant = false;
			logger.trace("    generateScoredExample --- step 1: morphFreqInAnalyses");
			Double morphemeFreq = morphFreqInAnalyses(morphemeExample, true);
			logger.trace("    generateScoredExample --- step 2: wordFreqInCorpus");
			Long wordFreq = wordFreqInCorpus(morphemeExample.word,
				allowAnalysisWithAdditionalFinalConsonant);
			Double score = 10000*morphemeFreq + wordFreq;
			scoredEx = new ScoredExample(morphemeExample.word, score, wordFreq);
			logger.trace("    generateScoredExample --- finished");
		} catch (LinguisticDataException | TimeoutException | MorphInukException e) {
			throw new MorphemeSearcherException(e);
		}
		return scoredEx;
	}

	private Long wordFreqInCorpus(String word, boolean allowAnalysisWithAdditionalFinalConsonant) throws MorphemeSearcherException {
		long nbOccurrencesOfWord;
		try {
			nbOccurrencesOfWord = this.corpus.totalOccurencesOf(word);
		} catch (CompiledCorpusException e) {
			throw new MorphemeSearcherException(e);
		}
		return nbOccurrencesOfWord;
	}

	public Double morphFreqInAnalyses(
		WordWithMorpheme morphemeExample,
		boolean allowAnalysisWithAdditionalFinalConsonant) throws LinguisticDataException, TimeoutException, MorphInukException, MorphemeSearcherException {
		MorphologicalAnalyzer analyzer = new MorphologicalAnalyzer();
		String morpheme = morphemeExample.morphemeId;
		String[][] decompositions = morphemeExample.decompsSample;
		if (decompositions == null) {
			try {
				Decomposition[] decompObjects =
					analyzer.decomposeWord(morphemeExample.word,
						allowAnalysisWithAdditionalFinalConsonant);
				decompositions = Decomposition.decomps2morphemes(decompObjects);
			} catch (MorphologicalAnalyzerException e) {
				throw new MorphemeSearcherException(e);
			}
		}
		int numDecsWithMorpheme = 0;
		for (String[] decomp: decompositions) {
			String decompStr = StringUtils.join(decomp, " ");
			if (decompStr.contains(morpheme)) {
				numDecsWithMorpheme++;
			} else {
				int x = 1;
			}

		}
		Double freq = 1.0 * numDecsWithMorpheme / decompositions.length;
		return freq;
	}
	
	//--------------------------------------------------------------------------

	public static class WordFreq {
		public String word;
		public Long freq;
		public Double score = 0.0;
		
		public WordFreq(String _word, Long _freq) {
			this.word = _word;
			this.freq = _freq;
		}
	}

	public class WordFreqComparator implements Comparator<ScoredExample> {
	    @Override
	    public int compare(ScoredExample a, ScoredExample b) {
	    	if (a.score.longValue() > b.score.longValue())
	    		return -1;
	    	else if (a.score.longValue() < b.score.longValue())
				return 1;
	    	else 
	    		return a.word.compareToIgnoreCase(b.word);
	    }
	}
	
	public class Bin {
		public String rootId;
		public HashMap<String,List<WordWithMorpheme>> morphid2wordsFreqs;
		
		public Bin(String _rootid, HashMap<String,List<WordWithMorpheme>> _morphid2wordsFreqs) {
			this.rootId = _rootid;
			this.morphid2wordsFreqs = _morphid2wordsFreqs;
		}
	}
}

