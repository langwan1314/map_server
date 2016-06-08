package com.youngo.core.upload;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.youngo.core.common.MURLEncoder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fuchen on 2015/12/4.
 * 文件上传服务
 */
public class FileUploadService
{
    private String address;
    private List<String> innerAddress;

    /**
     * 对外部暴露的图片服务器地址
     */
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * @return 上传图片时用到的真实的图片服务器地址<br>
     * 因为图片服务器可能会做集群，所以会需要上传到多个节点
     */
    public List<String> getInnerAddress()
    {
        return innerAddress;
    }

    public void setInnerAddress(String innerAddress)
    {
        if(!StringUtils.isEmpty(innerAddress))
        {
            String[] split = innerAddress.split(",");
            this.innerAddress = Arrays.asList(split);
        }
    }

    /**
     * @param group 图片的存放目录
     * @return 图片存储到图片服务器后, 访问该图片的url
     * @throws IOException
     */
    public String[] sendToServer(MultipartHttpServletRequest request, String group) throws IOException
    {
        Map<String, MultipartFile> fileMap = request.getFileMap();
        return sendToServer(fileMap, group);
    }

    /**
     * 通过文件map上传至服务器
     *
     * @return String
     */
    public String[] sendToServer(Map<String, MultipartFile> fileMap, String group) throws IOException
    {
        HttpURLConnection conn = null;
        List<String> fileNames = new LinkedList<String>();
        String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符

        if (fileMap != null)
        {
            Iterator<String> iterator = fileMap.keySet().iterator();
            while (iterator.hasNext())
            {
                MultipartFile file = fileMap.get(iterator.next());
                String filename = createFileNameForStore(file);
                fileNames.add(filename);

                for (String str : innerAddress)
                {
                    try
                    {
                        conn = createConnection(BOUNDARY, str, group);
                        OutputStream out = new DataOutputStream(conn.getOutputStream());
                        String contentType = file.getContentType();

                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\"" + filename + "\"; filename=\"" + filename + "\"\r\n");
                        strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

                        out.write(strBuf.toString().getBytes());

                        DataInputStream in = new DataInputStream(file.getInputStream());
                        int bytes = 0;
                        byte[] bufferOut = new byte[1024];
                        while ((bytes = in.read(bufferOut)) != -1)
                        {
                            out.write(bufferOut, 0, bytes);
                        }
                        in.close();
                        byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
                        out.write(endData);
                        out.flush();
                        out.close();
                        conn.getInputStream();
                    } finally
                    {
                        if (conn != null)
                        {
                            conn.disconnect();
                            conn = null;
                        }
                    }
                }
            }
        }
        return createResult(fileNames, group);
    }

    /**
     * bytes:图片文件的二进制数据
     */
    public String sendToServer(byte[] bytes, String group) throws IOException
    {
        HttpURLConnection conn = null;
        List<String> fileNames = new LinkedList<String>();
        String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
        String filename = createFileNameForStore();
        fileNames.add(filename);
        for (String str : innerAddress)
        {
            try
            {
                conn = createConnection(BOUNDARY, str, group);
                OutputStream out = new DataOutputStream(conn.getOutputStream());

                StringBuffer strBuf = new StringBuffer();
                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\"" + filename + "\"; filename=\"" + filename + "\"\r\n");
                strBuf.append("Content-Type:" + "image/jpeg" + "\r\n\r\n");

                out.write(strBuf.toString().getBytes());

                out.write(bytes);

                byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
                out.write(endData);
                out.flush();
                out.close();
                conn.getInputStream();
            } finally
            {
                if (conn != null)
                {
                    conn.disconnect();
                    conn = null;
                }
            }
        }
        String[] result = createResult(fileNames, group);
        return result[0];
    }
    
    public String sendToServerDownload(String url, String group){
    	String fileName = createFileNameForStore();
    	for (String str : innerAddress){
    		HttpURLConnection conn = null;
    		try {
    			StringBuilder addressSb = new StringBuilder();
    			addressSb.append(str).append("/operate/downloadImageFromInternet?dir=").append(group)
    					.append("&url=").append(MURLEncoder.encode(url,"utf-8")).append("&fileName=").append(fileName);
    			URL ur = new URL(addressSb.toString());
    			conn = (HttpURLConnection) ur.openConnection();
    			conn.setConnectTimeout(3000);
    			conn.setReadTimeout(10000);
    			conn.setDoOutput(true);
    			conn.setDoInput(true);
    			conn.setUseCaches(false);
    			InputStream in = null;
    			byte[] buff = new byte[10];
    			int i = -1;
    			try{
    				in = conn.getInputStream();
    				i = in.read(buff);
    				String result = new String(buff, 0, i);
    				if(!"success".equals(result)){
    					return null;
    				}
    			}catch (IOException e) {
    				return null;
    			}finally {
    				if(null != in){
    					try{in.close();}catch(Throwable t){}
    				}
    			}
    		} catch (UnsupportedEncodingException e) {
    		} catch (MalformedURLException e) {
    			return null;
    		} catch (IOException e) {
    			return null;
    		}finally {
    			if(null != conn){
    				conn.disconnect();
    			}
    		}
    	}
    	List<String> fileNames = new ArrayList<String>();
    	fileNames.add(fileName);
    	String[] result = createResult(fileNames, group);
    	return result[0];
    }

    private HttpURLConnection createConnection(String BOUNDARY, String address, String dir) throws IOException
    {
        URL url = new URL(address + "/operate/upload?dir=" + dir);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        return conn;
    }

    /**
     * @param file 创建服务器用来存放该文件时用的
     * @return
     */
    private String createFileNameForStore(MultipartFile file)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmSS");
        String format = formatter.format(new Date());
        String originalFilename = file.getOriginalFilename();
        StringBuffer buffer = new StringBuffer(format);
        buffer.append(Math.abs(originalFilename.hashCode()));
        String radomNumber = (int) (Math.floor(Math.random() * 10000)) + "";
        while (radomNumber.length() < 4)
        {
            radomNumber = (int) (Math.floor(Math.random() * 10000)) + "";
        }
        buffer.append(radomNumber);
        buffer.append(originalFilename.substring(originalFilename.lastIndexOf(".")));
        return buffer.toString();
    }

    private String createFileNameForStore()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmSS");
        String format = formatter.format(new Date());
        StringBuffer buffer = new StringBuffer(format);
        String radomNumber = (int) (Math.floor(Math.random() * 10000)) + "";
        while (radomNumber.length() < 4)
        {
            radomNumber = (int) (Math.floor(Math.random() * 10000)) + "";
        }
        buffer.append(radomNumber);
        buffer.append(".jpg");
        return buffer.toString();
    }

    private String[] createResult(List<String> fileNames, String group)
    {
        String[] result = new String[fileNames.size()];
        for (int i = 0; i < fileNames.size(); i++)
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append(address);
            buffer.append("/");
            buffer.append(group);
            buffer.append("/");
            buffer.append(fileNames.get(i));
            result[i] = buffer.toString();
        }
        return result;
    }

    /**
     * 删除图片
     *
     * @param param
     * @return
     * @throws IOException
     */
    public String deleteFromServer(Map<String, String> param) throws IOException
    {
        HttpURLConnection conn = null;
        String imageOrginalName = param.get("imageName");
        for (String address : innerAddress)
        {
            try
            {
                URL url = new URL(address + "/operate/delete?imageName=" + imageOrginalName);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("GET");
                conn.connect();
                conn.getInputStream();
            } catch (IOException e)
            {
                throw e;
            } finally
            {
                if (conn != null)
                {
                    conn.disconnect();
                    conn = null;
                }
            }
        }
        return "1";
    }

    /**
     * 改变服务器图片地址路径
     *
     * @param imageUrls
     * @param destinatePath
     * @return
     * @throws IOException
     */
    public String changePath(String imageUrls, String destinatePath) throws IOException
    {
        for (String address : innerAddress)
        {
            String url = address + "/operate/changePath1";
            StringBuffer buffer = new StringBuffer();
            buffer.append("{\"imageUrls\":\"");
            buffer.append(imageUrls.trim());
            buffer.append("\",\"destinatePath\":\"");
            buffer.append(destinatePath);
            buffer.append("\"}");
            sendPost(url, buffer.toString());
        }
        return "success";
    }

    private String sendPost(String url, String param) throws IOException
    {

        OutputStream out = null;
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 设置通用的请求属性
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Connection", "Keep-Alive");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        out = new DataOutputStream(conn.getOutputStream());
        // out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        out.write(param.getBytes());
        // flush输出流的缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应
        conn.getInputStream();

        out.close();

        return "success";
    }

}
