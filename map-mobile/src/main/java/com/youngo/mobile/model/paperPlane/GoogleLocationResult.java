package com.youngo.mobile.model.paperPlane;

import java.util.List;

public class GoogleLocationResult {
	private String status;
	
	private List<GoogleLocation> results;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<GoogleLocation> getResults() {
		return results;
	}

	public void setResults(List<GoogleLocation> results) {
		this.results = results;
	}

	public enum GoogleLocationStatus{
		OK, ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED, INVALID_REQUEST, UNKNOWN_ERROR
	}
}
