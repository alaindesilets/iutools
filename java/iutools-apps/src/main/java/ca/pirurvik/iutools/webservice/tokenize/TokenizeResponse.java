package ca.pirurvik.iutools.webservice.tokenize;

import java.util.List;

import ca.nrc.datastructure.Pair;
import ca.pirurvik.iutools.webservice.ServiceResponse;

public class TokenizeResponse extends ServiceResponse {

	public List<Pair<String, Boolean>> tokens;

	public TokenizeResponse() {
		
	}
	
	public TokenizeResponse(List<Pair<String, Boolean>> _tokens) {
		this.tokens = _tokens;
	}

}
