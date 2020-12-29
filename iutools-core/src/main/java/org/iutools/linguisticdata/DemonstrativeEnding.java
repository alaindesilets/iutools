/*
 * Conseil national de recherche Canada 2003
 * 
 * Cr�� le 5-Dec-2003
 * par Benoit Farley
 * 
 */
package org.iutools.linguisticdata;

import java.util.HashMap;
import java.util.Hashtable;

import org.iutools.utilities.Debugging;

public class DemonstrativeEnding extends Affix {
	//
	String grammCase;
	String number;
	
	static public Hashtable<String,DemonstrativeEnding> hash = new Hashtable<String,DemonstrativeEnding>();

	static String[] cases = {"abl", "acc", "dat", "gen", "loc", "nom", "sim", "via"};
	static String[] types = {"tad", "tpd"};
	//
	
	//--------------------------------------------------------------------------------------------------------
	public DemonstrativeEnding() { 
	}
	
	public DemonstrativeEnding(HashMap<String,String> v) {
		morpheme = (String) v.get("morpheme");
		Debugging.mess("DemonstrativeEnding/1", 1, "morpheme= " + morpheme);
		type = (String) v.get("type");
		grammCase = (String) v.get("case");
		number = (String) v.get("number");
		englishMeaning = (String) v.get("engMean");
		frenchMeaning = (String) v.get("freMean");
		dbName = (String) v.get("dbName");
		tableName = (String) v.get("tableName");
		setAttrs();
		makeContextualBehaviours();
	}
	
	//--------------------------------------------------------------------------------------------------------
	public void addToHash(String key, Object obj) {
	    hash.put(key,(DemonstrativeEnding)obj);
	}

	public String getTransitivityConstraint() {
	    return null;
	}
	
	public String[] getCombiningParts() {
	    return null;
	}

	/*
		Demonstrative endings are a special type of affix in that
		they do not have different forms and actions in different
		contexts, like noun endings, verb endings and suffixes.
		They can be considered as having a neutral action in all
		possible contexts. IMPORTANT: adverbial demonstrative radicals
		may also end in 'v', 'n' and 'g', so an additional context
		should be created: 'C' for "any consonant".
	 */
	public void makeContextualBehaviours() {
		String[] forms = new String[] { morpheme };
		Action[] actions1 = new Action[] { new Action.Neutral() };
		Action[] actions2 = new Action[] { null };
		ContextualBehaviour contextualBehaviour = new ContextualBehaviour('C',morpheme,new Action.Neutral(),null);
		contextualBehaviours.put('C', new ContextualBehaviour[] {contextualBehaviour});
	}

	public String getSignature() {
		StringBuffer sb = new StringBuffer();
		sb.append(type);
		sb.append("-");
		sb.append(grammCase);
		if (number != null) {
		    sb.append("-");
		    sb.append(number);
		}
		return sb.toString();
	}

	//--------------------------------------------------------------------------------------------------------
	public boolean agreeWithTransitivity(String trans) {
	    return true;
	}
	
//    Vector getIdsOfCompositesWithThisRoot() {
//         return null;
//    }
    
	void setAttrs() {
		setAttributes();
		setId();
	}

    void setAttributes() {
    	HashMap<String,Object> endingAttrs = new HashMap<String,Object>();
    	endingAttrs.put("case",grammCase);
    	endingAttrs.put("number",number);
    	super.setAttributes(endingAttrs);
    }

	//--------------------------------------------------------------------------------------------------------
	public String showData() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n[DemonstrativeEnding: morpheme= " + morpheme + "\n");
		sb.append("type= " + type + "\n");
		sb.append("case= " + grammCase + "\n");
		sb.append("number= " + number + "\n");
		sb.append("englishMeaning= " + englishMeaning + "\n");
		sb.append("frenchMeaning= " + frenchMeaning + "]\n");
		return sb.toString();
	}

}
