package com.youngo.mobile.model.paperPlane;

import java.util.List;

public class PapersParam {
	private String firstPlaneId;
	private String lastPlaneId;
	private List<String> planeIds;
	
	public PapersParam(String firstPlaneId, String lastPlaneId) {
		this.lastPlaneId = lastPlaneId;
		this.firstPlaneId = firstPlaneId;
	}
	
	public PapersParam(String firstPlaneId, String lastPlaneId, List<String> papers) {
		super();
		this.firstPlaneId = firstPlaneId;
		this.lastPlaneId = lastPlaneId;
		this.planeIds = papers;
	}

	public String getLastPlaneId() {
		return lastPlaneId;
	}
	
	public void setLastPlaneId(String lastPlaneId) {
		this.lastPlaneId = lastPlaneId;
	}
	
	public String getFirstPlaneId() {
		return firstPlaneId;
	}
	
	public void setFirstPlaneId(String firstPlaneId) {
		this.firstPlaneId = firstPlaneId;
	}
	
	public List<String> getPlaneIds() {
		return planeIds;
	}
	
	public void setPlaneIds(List<String> papers) {
		this.planeIds = papers;
	}

	@Override
	public String toString() {
		return "PapersParam [firstPlaneId=" + firstPlaneId + ", lastPlaneId=" + lastPlaneId + ", papers=" + planeIds
				+ "]";
	}
}
