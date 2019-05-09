/*
 * Controller for the search.html page.
 */

class SearchController extends WidgetController {

	constructor(config) {
		super(config);
		this.hitsPerPage = 10;
		this.currentPage = 1;
		this.attachHtmlElements();
	} 
	
	attachHtmlElements() {
		this.setEventHandler("btnSearch", "click", this.onSearch);
		this.onReturnKey("txtQuery", this.onSearch);
	}

	onSearch() {
			var isValid = this.validateQueryInput();
			if (isValid) {
				this.setBusy(true);
				this.invokeSearchService(this.getSearchRequestData(), 
						this.successCallback, this.failureCallback)
			}
	}
	
	invokeSearchService(jsonRequestData, _successCbk, _failureCbk) {
			var controller = this;
			var fctSuccess = 
					function(resp) {
						_successCbk.call(controller, resp);
					};
			var fctFailure = 
					function(resp) {
						_failureCbk.call(controller, resp);
					};
		
			$.ajax({
				type: 'POST',
				url: 'srv/search',
				data: jsonRequestData,
				dataType: 'json',
				async: true,
		        success: fctSuccess,
		        error: fctFailure
			});
	}

	validateQueryInput() {
		var isValid = true;
		var query = this.elementForProp("txtQuery").val();
		if (query == null || query === "") {
			isValid = false;
			this.error("You need to enter something in the query field");
		}
		return isValid;
	}

	successCallback(resp) {
		if (resp.errorMessage != null) {
			this.failureCallback(resp);
		} else {
			this.setQuery(resp.expandedQuery);
			this.setTotalHits(resp.totalHits);
			this.setResults(resp.hits);		
		}
		this.setBusy(false);
	}

	failureCallback(resp) {
		this.enableSearchButton();
		this.error(resp.errorMessage);
		this.enableSearchButton();
		this.setBusy(false);
	}
	
	
	setBusy(flag) {
		if (flag) {
			this.disableSearchButton();		
			this.error("");
		} else {
			this.enableSearchButton();		
		}
	}
	
	
	getSearchRequestData() {
		
		var request = {
				query: this.elementForProp("txtQuery").val(),
				hitsPageNum: this.currentPage,
				hitsPerPage: this.hitsPerPage
		};
		
		var jsonInputs = JSON.stringify(request);
		
		return jsonInputs;
	}
	
	disableSearchButton() {
		this.elementForProp('btnSearch').attr("disabled", true);
	}
	
	enableSearchButton() {
		this.elementForProp('btnSearch').attr("disabled", false);

	}
	
	error(err) {
		this.elementForProp('divError').html(err);
		this.elementForProp('divError').show();	 
		this.setTotalHits(0);
	}
	
	setQuery(query) {
		this.elementForProp("txtQuery").val(query);
	}
	
	setTotalHits(totalHits) {
		var totalHitsText = "No hits found";
		if (totalHits > 0) {
			totalHitsText = "Found "+totalHits+" hits";
		}
		this.elementForProp('divTotalHits').text(totalHitsText);
	}
	
	
	setResults(results) {
		var jsonResults = JSON.stringify(results);
		var divResults = this.elementForProp("divResults");
		
		divResults.empty();
		
		for (var ii = 0; ii < results.length; ii++) {
			var aHit = results[ii];
			var hitHtml = 
					"<div id=\"hit"+ii+"\" class=\"hitDiv\">\n" +
					"  <div id=\"hitTitle\">"+aHit.title+"</div><br/>\n" +
					"  <div id=\"hitSnippet\">"+aHit.snippet+"</div><br/>\n" +
					"  <div id=\"hitURL\">"+aHit.url+"</div><br/>\n" +
					"<div>"
				;
			var aHitDiv = $.parseHTML(hitHtml);
			divResults.append(aHitDiv);
	    }
		
		this.generatePagesButtons(results.length);

		
		divResults.show();
	}
	
	generatePagesButtons(nbHits) {
		var divPageNumbers = this.elementForProp('divPageNumbers');
		divPageNumbers.empty();
		var nbPages = Math.ceil(nbHits / this.hitsPerPage);
		for (var ip=0; ip<nbPages; ip++) {
			var pageLink = '<input class="page-number"' +
				'type="button" '+
				'name="'+'page-number'+(ip+1)+'" '+
				'value="'+(ip+1)+'"/>';
			divPageNumbers.append(pageLink);
			if (ip != nbPages-1)
				divPageNumbers.append('&nbsp;&nbsp;');
		}
//		$('div#hits').css('display','block');
	}
	
	trimStr(str) {
		if (null != str) {
			str = str.replace(/(^\s+|\s+$)/g,"");
			if (str.length == 0) str = null;
		}
		return str;
	}
}