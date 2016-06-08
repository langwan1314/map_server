package com.youngo.mobile.model.paperPlane;

import java.util.List;

public class GoogleLocation {
	private List<AddressComponent> address_components;
	
	private String formatted_address;
	
	private Object geometry;
	
	private String place_id;
	
	private List<String> types;
	
	public List<AddressComponent> getAddress_components() {
		return address_components;
	}

	public void setAddress_components(List<AddressComponent> address_components) {
		this.address_components = address_components;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public Object getGeometry() {
		return geometry;
	}

	public void setGeometry(Object geometry) {
		this.geometry = geometry;
	}

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public enum LocationType{
		street_address, 
		route, 
		intersection, 
		political, 
		country, 
		administrative_area_level_1,
		administrative_area_level_2,
		administrative_area_level_3, 
		administrative_area_level_4,
		administrative_area_level_5,
		colloquial_area,
		locality,
		ward,
		sublocality,
		neighborhood,
		premise,
		subpremise,
		postal_code,
		natural_feature,
		airport,
		park,
		point_of_interest,
	}
}
