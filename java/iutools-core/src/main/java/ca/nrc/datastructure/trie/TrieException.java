package ca.nrc.datastructure.trie;

public class TrieException extends Exception {
	public TrieException(String mess, Exception exc) {
		super(mess, exc);
//		super(mess+"; "+exc.getMessage());
	}

	public TrieException(String mess) {
		super(mess);
	}
}