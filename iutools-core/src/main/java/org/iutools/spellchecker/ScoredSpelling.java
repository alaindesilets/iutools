package org.iutools.spellchecker;

public class ScoredSpelling {
	public String spelling = null;
	public Double ngramSim = null;
	public Double editDist = null;
	public Long frequency = null;

	public ScoredSpelling() {
		init_ScoredSpelling((String)null, (Double)null, (Double)null);
	}

	public ScoredSpelling(String _spelling) {
		init_ScoredSpelling(_spelling, (Double)null, (Double)null);
	}

	public ScoredSpelling(String _spelling, Double _ngramSim) {
		init_ScoredSpelling(_spelling, _ngramSim, (Double)null);
	}

	private void init_ScoredSpelling(
		String _spelling, Double _ngramSim, Double _editDist) {
		this.spelling = _spelling;
		this.ngramSim = _ngramSim;
		this.editDist = _editDist;
	}

	public String toString() {
		String str = spelling+" (dist="+ ngramSim +")";
		return str;
	}

	public ScoredSpelling setFrequency(Long _freq) {
		this.frequency = _freq;
		return this;
	}

	@Override
	public boolean equals(Object other) {
		boolean answer = false;
		if (other instanceof ScoredSpelling) {
			ScoredSpelling otherSpelling = (ScoredSpelling)other;
			answer = this.spelling.equals(otherSpelling.spelling);
		}
		return answer;
	}

	@Override
	public int hashCode() {
		return this.spelling.hashCode();
	}
}