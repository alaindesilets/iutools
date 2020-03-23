package ca.pirurvik.iutools.spellchecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.nrc.datastructure.Pair;

public class SpellingCorrection {
	public String orig;
	public Boolean wasMispelled = false;
	public List<ScoredSpelling> scoredCandidates = 
				new ArrayList<ScoredSpelling>();
	public String correctLead;
	public String correctTail;

	public SpellingCorrection(String _orig, String[] _corrections, Boolean _wasMispelled) {
		List<String> correctionsList = Arrays.asList(_corrections);
		initialize(_orig, correctionsList, null, _wasMispelled);
	}

	public SpellingCorrection(String _orig, String[] _corrections, Double[] _scores, Boolean _wasMispelled) {
		List<String> correctionsList = Arrays.asList(_corrections);
		List<Double> scoresList = Arrays.asList(_scores);
		initialize(_orig, correctionsList, scoresList, _wasMispelled);
	}
	
	public SpellingCorrection(String _orig, List<String> _corrections) {
		initialize(_orig, _corrections, null, true);
	}
	
	public SpellingCorrection(String _orig) {
		initialize(_orig, null, null, null);
	}

	public SpellingCorrection(String word, boolean _wasMispelled) {
		initialize(word, null, null, _wasMispelled);
	}


	private void initialize(String _orig, List<String> _corrections, 
			List<Double> _scores, Boolean _wasMispelled) {
		this.orig = _orig;
		if (_wasMispelled != null) this.wasMispelled = _wasMispelled;
	}

	public List<ScoredSpelling> getScoredPossibleSpellings() {
		return scoredCandidates;
	}
	
	
	public List<String> getPossibleSpellings() {
		List<String> possibleSpellings = new ArrayList<String>();
		for (ScoredSpelling scoredSpelling: scoredCandidates) {
			possibleSpellings.add(scoredSpelling.spelling);
		}
		return possibleSpellings;
	}

	public SpellingCorrection setPossibleSpellings(List<ScoredSpelling> _scoredSpellings) {
		scoredCandidates = _scoredSpellings;
				
		if (scoredCandidates != null 
				&& scoredCandidates.size() > 0 
				&& scoredCandidates.get(0).spelling.equals(orig)) {
			this.scoredCandidates.remove(0);
		}
				
		return this;
	}
	
	public String correctExtremities() {
		String correctPortions = null;
		if (correctLead != null && correctTail != null) {
			int middleStart = correctLead.length();
			int middleEnd = orig.length() - correctTail.length();
			if (middleStart < middleEnd) {
				correctPortions = 
					correctLead+
					"["+orig.substring(middleStart, middleEnd)+"]"+
					correctTail;
			}
		}
		
		return correctPortions;
	}
}
