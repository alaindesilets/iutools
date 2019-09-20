package ca.inuktitutcomputing.data;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class LinguisticData {
	
	private static LinguisticData singleton = null;
	
	// Note: We keep info as both Vector<Base> and Vector<Morpheme> because for
	//  some unknown reason, we cannot cast from Vector<Base> to Vector<Morpheme>
	//  and some clients expect to receive the info in the later type.
    protected Map<String,Vector<Base>> basesForCanonicalForm = new HashMap<String,Vector<Base>>();
    protected Hashtable<String,Vector<Morpheme>> morphemesForCanonicalForm = new Hashtable<String,Vector<Morpheme>>();
    protected Hashtable<String,Base> idToBaseTable = new Hashtable<String,Base>();
    protected Hashtable<String,Affix> idToAffixTable = new Hashtable<String,Affix>();
    
    public static LinguisticData getInstance() {
    	if (singleton == null) {
    		singleton = new LinguisticData();
    	}
    	return singleton;
    }
    
    
    //--------------------------------------------------------------------------
    public void addBaseForCanonicalForm(String canonicalForm, Base base) throws LinguisticDataException {
    	if (!basesForCanonicalForm.containsKey(canonicalForm)) {
    		basesForCanonicalForm.put(canonicalForm, new Vector<Base>());
    		morphemesForCanonicalForm.put(canonicalForm, new Vector<Morpheme>());
    	}
    	
    	basesForCanonicalForm.get(canonicalForm).add(base);
    	morphemesForCanonicalForm.get(canonicalForm).add((Morpheme)base);
    }
    
    public Vector<Morpheme> getBasesForCanonicalForm(String canonicalForm) {
    	Vector<Morpheme> bases = morphemesForCanonicalForm.get(canonicalForm);
    	return bases;
    }
    

	public String[] getCanonicalFormsForAllBases() {
		String[] forms = basesForCanonicalForm.keySet().toArray(new String[0]);;
		return forms;
	}
    
    public Hashtable<String, Vector<Morpheme>> getBasesForAllCanonicalForms_hashtable() {
    	return morphemesForCanonicalForm;
    }
    


    //--------------------------------------------------------------------------
	public void addEntryToIdToBaseTable(String baseId, Base baseObject) {
		idToBaseTable.put(baseId, baseObject);
	}
	
    public Base getBaseWithId(String morphId) {
    	Base b = null;
    	if (idToBaseTable != null)
    		b = idToBaseTable.get(morphId);
        return b;
    }

    public Base getBaseFromMorphemeIdObject(Morpheme.Id morphId) {
    	Base b = null;
    	if (idToBaseTable != null)
    		b = idToBaseTable.get(morphId.id);
        return b;
    }
    
    public Hashtable<String, Base> getIdToBaseTable() {
    	return idToBaseTable;
    }
    
    public String[] getAllBasesIds() {
    	return (String[])idToBaseTable.keySet().toArray(new String[0]);
    }

	public Hashtable<String, Morpheme> getIdToRootTable() {
		Hashtable<String, Morpheme> table = new Hashtable<String, Morpheme>();
		String clazz = Base.class.getName();
		for (Enumeration<String> rootIds = idToBaseTable.keys(); rootIds.hasMoreElements();) {
			String rootId = rootIds.nextElement();
			Object obj = idToBaseTable.get(rootId);
			if (obj.getClass().getName() == clazz) {
				Base root = idToBaseTable.get(rootId);
				table.put(rootId, root);
			}
		}
		return table;
	}
	

    //--------------------------------------------------------------------------
	public void addEntryToIdToAffixTable(String affixId, Affix affixObject) {
		idToAffixTable.put(affixId, affixObject);
	}
	
    public Affix getAffixWithId(String uniqueId) {
        Affix aff = idToAffixTable.get(uniqueId);
        return aff;
    }

    public Suffix getSuffixWithId(String uniqueId) {
        Suffix afs = (Suffix)idToAffixTable.get(uniqueId);
        return afs;
    }

    public Hashtable<String, Affix>getIdToAffixTable() {
    	return idToAffixTable;
    }
    
    public String[] getAllAffixesIds() {
    	return (String[])idToAffixTable.keySet().toArray(new String[0]);
    }

    //--------------------------------------------------------------------------
	public Hashtable<String, Morpheme> getId2SuffixTable() {
		Hashtable<String, Morpheme> table = new Hashtable<String, Morpheme>();
		for (Enumeration<String> affixIds = idToAffixTable.keys(); affixIds.hasMoreElements();) {
			String affixId = affixIds.nextElement();
			Affix aff = (Affix) idToAffixTable.get(affixId);
			if (aff.getClass().getName().equals("Suffix"))
				table.put(affixId, (Morpheme)aff);
		}
		return table;
	}

// APPELÉE NULLE PART
//    public String[] getAllSuffixesIds() {
//    	Hashtable<String,Morpheme> suffixes = getId2SuffixTable();
//    	String [] suffixesIds = new String[suffixes.size()];
//    	int i=0;
//    	for (Enumeration<String> keys = suffixes.keys(); keys.hasMoreElements();) {
//    		Suffix suf = (Suffix)suffixes.get(keys.nextElement());
//    		suffixesIds[i++] = suf.id;
//    	}
//    	return suffixesIds;
//    }


    //--------------------------------------------------------------------------
	public Hashtable<String, Demonstrative> getIdToDemonstrativeTable() {
		Hashtable<String, Demonstrative> table = new Hashtable<String, Demonstrative>();
		String clazz = Demonstrative.class.getName();
		for (Enumeration<String> demonstrativeIds = idToBaseTable.keys(); demonstrativeIds.hasMoreElements();) {
			String demonstrativeId = demonstrativeIds.nextElement();
			Object obj = idToBaseTable.get(demonstrativeId);
			if (obj.getClass().getName() == clazz) {
				Demonstrative demonstrativeObject = (Demonstrative) idToBaseTable.get(demonstrativeId);
				table.put(demonstrativeId, demonstrativeObject);
			}
		}
		return table;
	}

    //--------------------------------------------------------------------------
	public Hashtable<String,Base> getIdToGiVerbsTable() {
    	Hashtable<String,Base> giverbsHash = new Hashtable<String,Base>();
    	Hashtable<String,Morpheme> bases = getIdToRootTable();
    	for (Enumeration<String> keys = bases.keys(); keys.hasMoreElements();) {
    		String key = keys.nextElement();
    		Base base = (Base)bases.get(key);
    		if (base.isGiVerb())
    			giverbsHash.put(key, base);
    	}
    	return giverbsHash;
    }

    public Base [] getGiVerbs() {
    	Hashtable<String,Base> giverbsHash = getIdToGiVerbsTable();
    	String [] giverbsKeysArray = (String[])giverbsHash.keySet().toArray(new String[]{});
    	Arrays.sort(giverbsKeysArray);
    	Base [] giverbs = new Base[giverbsKeysArray.length];
    	for (int i=0; i<giverbsKeysArray.length; i++)
    		giverbs[i] = (Base)giverbsHash.get(giverbsKeysArray[i]);
    	return giverbs;
    }


	

    //--------------------------------------------------------------------------
	public void reinitializeData() {
//	    examples = new Hashtable<String,Vector<Example>>();
//	    textualRenderings = new Hashtable<String,String[]>();
//	    surfaceFormsOfAffixes = new Hashtable<String,Vector<SurfaceFormOfAffix>>();
		basesForCanonicalForm = new Hashtable<String,Vector<Base>>();
	    morphemesForCanonicalForm = new Hashtable<String,Vector<Morpheme>>();
	    idToBaseTable = new Hashtable<String,Base>();
	    idToAffixTable = new Hashtable<String,Affix>();
//	    words = new Hashtable<String,VerbWord>();
//	    sources = new Hashtable<String,Source>();
//	    groupsOfConsonants = new Hashtable<Character,Vector<String>>();
	}
		

	
}
