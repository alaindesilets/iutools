package ca.inuktitutcomputing.morph;

import java.util.concurrent.Callable;

import ca.inuktitutcomputing.utilities.StopWatch;

/**
 * Class for calling the morphological analyzer in a way that supports 
 * timeouts.
 * 
 * @author desilets
 *
 */
public class MorphAnalyzerTask implements Callable<Decomposition[]> {

	StopWatch stopWatch = null;
	String word = null;
	boolean lenient = true;
	MorphologicalAnalyzerAbstract analyzer = null;
	
	public MorphAnalyzerTask(String _word, Boolean _lenient, 
			MorphologicalAnalyzerAbstract morphologicalAnalyzerAbstract) {
		this.word = _word;
		if (_lenient != null) {
			this.lenient = _lenient;
		}
		this.analyzer = morphologicalAnalyzerAbstract;
	}
	
	@Override
	public Decomposition[] call() throws Exception {
		long start = System.currentTimeMillis();
		Decomposition[] decomps = analyzer.doDecompose(word, lenient);
		long elapsed = System.currentTimeMillis() - start;
		
		return decomps;
	}
}