package com.youngo.mobile.controller.location;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngo.mobile.model.paperPlane.Address;
import com.youngo.mobile.model.paperPlane.AddressComponent;
import com.youngo.mobile.model.paperPlane.GoogleLocation;
import com.youngo.mobile.model.paperPlane.GoogleLocation.LocationType;
import com.youngo.mobile.model.paperPlane.GoogleLocationResult;
import com.youngo.mobile.model.paperPlane.GoogleLocationResult.GoogleLocationStatus;

public class GoogleLocationHandler implements ResponseHandler<Address> {
	
	private ObjectMapper objectMapper = new ObjectMapper();//线程安全  但是否造成多线程竞争资源，需要考虑，能否避免资源竞争。

	@Override
	public Address handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        try{
        	if (statusLine.getStatusCode() == 200 || statusLine.getStatusCode() == 400) {
        		//400是全局参数异常  按理来说正确的配置不会产生400错误
        		if (entity == null)
        			return null;
        		else{
        			byte[] bytes = EntityUtils.toByteArray(entity);
        			if(null != bytes){
        				String s = new String(bytes);
//        				System.out.println(s);
        				GoogleLocationResult result = this.objectMapper.readValue(bytes, 0, bytes.length, GoogleLocationResult.class);
        				if(GoogleLocationStatus.OK.name().equals(result.getStatus())){
        					GoogleLocation location = result.getResults().get(0);
        					List<AddressComponent> components = location.getAddress_components();
        					Address address = getAddress(components);
        					return address;
        				}
        			}
        		}
        	}else if(statusLine.getStatusCode() == 403){
        		
        		//用户没有权限访问google接口，可能原因为tk失效或者 google改变tk策略。
        		//TODO 
        		return null;
        	}else{
        		System.out.println(statusLine.getStatusCode());
        		throw new HttpResponseException(statusLine.getStatusCode(),
        				statusLine.getReasonPhrase());
        	}
        }finally{
        	EntityUtils.consume(entity);
        }
        return null;
	}
	
	private Address getAddress(List<AddressComponent> components){
		Address address = null;
		if(null != components){
			address = new Address();
			String country = null;
			String city = null;
			String administrativeAreaLevel1 = null;
			for(int i = 0, j = components.size(); i < j; i++){
				AddressComponent component = components.get(i);
				List<String> types = component.getTypes();
				if(types.contains(LocationType.locality.name())){
					city = component.getLong_name();
				}else if(types.contains(LocationType.country.name())){
					country = component.getLong_name();
				}else if(types.contains(LocationType.administrative_area_level_1.name())){
					administrativeAreaLevel1 = component.getLong_name();
				}
				if(null!= country && null != city){
					break;
				}
			}
			address.setCountry(country);
			address.setCity(city == null ? administrativeAreaLevel1 : city);
			
		}
		return address;
	}

}
