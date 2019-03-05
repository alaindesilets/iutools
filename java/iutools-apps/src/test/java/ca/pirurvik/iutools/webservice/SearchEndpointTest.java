package ca.pirurvik.iutools.webservice;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ca.inuktitutcomputing.core.QueryExpander;
import ca.inuktitutcomputing.core.QueryExpansion;
import ca.nrc.testing.AssertHelpers;

public class SearchEndpointTest {

	SearchEndpoint endPoint = null;
	
	@Before
	public void setUp() throws Exception {
		endPoint = new SearchEndpoint();
	}

	
	/***********************
	 * VERIFICATION TESTS
	 ***********************/	

	@Test
	public void test__SearchEndpoint__HappyPath() throws Exception {
		String query = "qarasaujakkut";
		SearchInputs inputs = new SearchInputs(query);		
		SearchResponse results = endPoint.executeEndPoint(inputs);
		
		String gotExpandedQuery = results.expandedQuery;
		AssertHelpers.assertStringEquals("BLAH", gotExpandedQuery);
		
	}

	@Test
	public void test__expandQuery__HappyPath() throws Exception {
	
		String query = "qarasaujakkut";
        QueryExpander expander = new QueryExpander();
		QueryExpansion[] gotExpansions = expander.getExpansions(query);	
		String[] expExpansions = new String[] {"BLAH"};
		assertExpansionWordsAre(expExpansions, gotExpansions);
	}


	private void assertExpansionWordsAre(String[] expExpansionWords, QueryExpansion[] gotExpansions) throws IOException {
		List<String> gotExpansionWords = new ArrayList<String>();
		for (QueryExpansion exp: gotExpansions) {
			gotExpansionWords.add(exp.word);
		}
		AssertHelpers.assertDeepEquals("", expExpansionWords, gotExpansionWords);
	}
	
}