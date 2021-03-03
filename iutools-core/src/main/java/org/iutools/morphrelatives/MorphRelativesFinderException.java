package org.iutools.morphrelatives;

public class MorphRelativesFinderException extends Exception {

	public MorphRelativesFinderException(String mess, Exception e) {
		super(mess, e);
	}

	public MorphRelativesFinderException(Exception e) {
		super(e);
	}

	public MorphRelativesFinderException(String mess) {
		super(mess);
	}
}
