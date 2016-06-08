package com.youngo.mobile.other.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadTest {
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String FORM_NAME_START = "Content-Disposition: form-data; name=\"";
	private static final String FORM_NAME_END = "\"";
	private static final String FILENAME_START="filename=\"";
	private static final String SEPERATOR = ";";
	public static final String BOUNDARY = "--------androidFilepost";  

	public static void main(String[] args) {
		String method = "GET";
		try {
			
			URL url = new URL("http://127.0.0.1:8080/soga/user/uploadImage");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setChunkedStreamingMode(1024);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			connection.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			connection.addRequestProperty("passport", "b2a3b8c1-2726-4303-848d-1a8500aec430");
			OutputStream outputStream = connection.getOutputStream();
			try{
				StringBuilder oneParam = new StringBuilder();
				oneParam.append(PREFIX).append(BOUNDARY)
				.append(LINE_END).append(FORM_NAME_START)
				.append("files").append(FORM_NAME_END)
				.append(SEPERATOR).append(FILENAME_START)
				.append("aa.jpg").append(FORM_NAME_END)
				.append(LINE_END).append("Content-Type: ").append("application/octet-stream")
				.append(LINE_END).append(LINE_END);
				
				System.out.print(oneParam.toString());
				
				File file = new File("C:/Users/sws/Desktop/cefc1e178a82b9017ef43fa3758da9773812efc1.jpg");
				
				outputStream.write(oneParam.toString().getBytes());
				//System.out.println("write file");
				writeFile(file, outputStream);
				System.out.print(LINE_END);
				outputStream.write(LINE_END.getBytes());
				outputStream.write((PREFIX + BOUNDARY + PREFIX + "\r\n").getBytes());
				System.out.print(PREFIX + BOUNDARY + PREFIX + "\r\n");
			}finally{
				outputStream.flush();
				outputStream.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace(); // Invalidate url;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	private static void writeFile(File file, OutputStream outputStream){
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
	        byte[] b = new byte[1024];  
	        int n;  
			while ((n = in.read(b)) != -1) {  
					outputStream.write(b, 0, n);
			}
			in.close();  
		} catch (IOException e) {
			if(in != null){
				try {	in.close();	} catch (IOException e1) {}
			}
		}  
	}
	
	
}
