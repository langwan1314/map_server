package com.youngo.admin.common;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @author fuchen<br>
 * 图片传输工具 ,将接收到的图片传输到图片服务器
 */

public class ImageTransportUtil {
	
	private String address;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	
	/**
	 * @param group 图片的存放目录
	 * @return 图片存储到图片服务器后,访问该图片的url
	 * @throws IOException
	 */
	public String sendToImageServer(MultipartHttpServletRequest request , String group) throws IOException {
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(address+"/upload");
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			Map<String, MultipartFile> fileMap = request.getFileMap();
			// file
			//用来记录宽与高 
			String widthAndHeight="";
			if (fileMap != null) {
				Iterator<String> iterator = fileMap.keySet().iterator();
				while (iterator.hasNext()) {
					MultipartFile file = fileMap.get(iterator.next());
					String filename = file.getOriginalFilename();
					String contentType = file.getContentType();

					MultipartFile multipartFile = request.getFile(iterator.next());
					 
					//获取图片的宽与高
					widthAndHeight=getImageWidthAndHight(multipartFile);
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + group
							+ "\"; filename=\"" + filename + "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(file.getInputStream());
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line);
				strBuf.append("\n");
			}
			reader.close();
			reader = null;
			return strBuf.toString()+widthAndHeight;
		} catch (IOException e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
	}
	
	 /**
	  * 获取图片的宽和高，返回至前台  返回格式 "700,1000" （700为宽,1000为高）
	  * @Description:TODO
	  * @Title: getImageWidthAndHight
	  * @author atao
	  * @date 2015-1-15 下午5:59:32
	  * @return String
	  */
	private String getImageWidthAndHight(MultipartFile file) throws IOException
	{
		String ret=",";
		InputStream is = file.getInputStream();
		BufferedImage src =javax.imageio.ImageIO.read(is);
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		ret=ret+width+","+height;
		return ret;
	}
}
