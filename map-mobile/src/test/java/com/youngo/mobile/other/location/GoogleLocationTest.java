package com.youngo.mobile.other.location;

import com.youngo.mobile.controller.location.GoogleLocationService;
import com.youngo.mobile.model.paperPlane.Address;

public class GoogleLocationTest {

	public static void main(String[] args) {
		GoogleLocationService service = new GoogleLocationService();
		Address address = service.reverseGeoCoding(60.1695664d, 24.9357125d, "zh");
		System.out.println(address);
		
		
	}

}
