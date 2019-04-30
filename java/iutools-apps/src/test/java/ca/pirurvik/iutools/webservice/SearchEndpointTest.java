package ca.pirurvik.iutools.webservice;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.inuktitutcomputing.core.QueryExpander;
import ca.inuktitutcomputing.core.QueryExpansion;
import ca.nrc.testing.AssertHelpers;
import ca.nrc.ui.web.testing.MockHttpServletRequest;
import ca.nrc.ui.web.testing.MockHttpServletResponse;


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
		
		SearchInputs searchInputs = new SearchInputs().setHitsPerPage(20);
		searchInputs.query = "nunavut";

				
		MockHttpServletResponse response = 
				IUTServiceTestHelpers.postEndpointDirectly(
					IUTServiceTestHelpers.EndpointNames.SEARCH,
					searchInputs
				);
		
		IUTServiceTestHelpers.assertExpandedQueryEquals(
				"(ᓄᓇᕗ OR ᓄᓇᕗᒻᒥ OR ᓄᓇᕘᒥ OR ᓄᓇᕘᑉ OR ᓄᓇᕗᒻᒥᐅᑦ)", 
				response);
		
		String[] queryWords = new String[] {"ᓄᓇᕗ", "ᓄᓇᕗᒻᒥ", "ᓄᓇᕘᒥ", "ᓄᓇᕘᑉ", "ᓄᓇᕗᒻᒥᐅᑦ"};
		double tolerance = 0.5;
		IUTServiceTestHelpers.assertMostHitsMatchWords(queryWords, response, tolerance);
		

	}

	@Test
	public void test__expandQuery__HappyPath() throws Exception {
	
		String query = "inuk";
        QueryExpander expander = new QueryExpander();
		QueryExpansion[] gotExpansions = expander.getExpansions(query);	
		String[] expExpansions = new String[] {"inuit", "inunnut", "inuttitut", "inungnik", "inu"};
		assertExpansionWordsAre(expExpansions, gotExpansions);
	}


	private void assertExpansionWordsAre(String[] expExpansionWords, QueryExpansion[] gotExpansions) throws IOException {
		List<String> gotExpansionWords = new ArrayList<String>();
		if (gotExpansions == null) {
			gotExpansionWords = null;
		} else {
			for (QueryExpansion exp: gotExpansions) {
				gotExpansionWords.add(exp.word);
			}
		}
		AssertHelpers.assertDeepEquals("", expExpansionWords, gotExpansionWords);
	}
	
}
