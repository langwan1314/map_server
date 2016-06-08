package com.youngo.image.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/operate")
public class ImageUpLoadController
{
    private static Logger logger = Logger.getLogger(ImageUpLoadController.class.getName());

    /**
     * @param request
     * @param bytes
     * @throws Exception
     */
    @RequestMapping(value = "/sendbuffer", method = RequestMethod.POST)
    public String sendImage(HttpServletRequest request, @RequestBody byte[] bytes) throws Exception
    {
        String root = request.getServletPath();
        File file = new File(root + "/temp.jpg");
        FileOutputStream fin = new FileOutputStream(file);
        fin.write(bytes);
        return "success";
    }

    /**
     * 上传图片文件
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public String upload(MultipartHttpServletRequest request) throws IOException
    {
        logger.debug("********begin call image upload***************");
        String dir = request.getParameter("dir");
        String logoRealPathDir = request.getSession().getServletContext().getRealPath("/") + dir;
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext())
        {
            /** 得到图片保存目录的真实路径 **/
            /** 根据真实路径创建目录 **/
            File logoSaveFile = new File(logoRealPathDir);

            if (!logoSaveFile.exists())
                logoSaveFile.mkdirs();

            MultipartFile multipartFile = request.getFile(fileNames.next());
            String originalFilename = multipartFile.getOriginalFilename();

            //				String logImageName = format+originalFilename.substring(originalFilename.lastIndexOf("."));
            /** 拼成完整的文件保存路径加文件 **/
            String fileName = logoRealPathDir + "/" + originalFilename;
            File file = new File(fileName);

            multipartFile.transferTo(file);
        }
        //			logger.debug("********image info :"+urls.toString()+"***************");
        logger.debug("********end call image upload***************");
        return "success";
    }
    /**
     * 删除文件
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/delete")
    public String delete(HttpServletRequest request, @RequestParam("imageName") String imageName)
    {
        String root = request.getSession().getServletContext().getRealPath("/");
        String logoRealPathDir = root + imageName.substring(imageName.lastIndexOf("youngo/images/") + 16);
        /** 根据真实路径创建目录 **/
        File file = new File(logoRealPathDir);
        file.delete();
        return "1";
    }


    /**
     * 更改图片的存储路径
     * @return String
     */
    @RequestMapping(method = RequestMethod.POST, value = "/changePath", produces = "application/json")
    public String changeImagePath(@RequestBody Map<String, Object> param, HttpServletRequest request) throws IOException
    {
        logger.debug("***************************begin call changeImagePath Post************");
        String imageUrls = (String) param.get("imageUrls");
        String destinatePath = (String) param.get("destinatePath");
        logger.debug("***destinatePath =" + destinatePath + "********");
        logger.debug("***imageUrls =" + imageUrls + "********");
        String root = request.getSession().getServletContext().getRealPath("/");
        String[] imageUrlArray = imageUrls.split(",");
        for (int i = 0; i < imageUrlArray.length; i++)
        {
            String imageUrl = imageUrlArray[i];
            String imageName = imageUrl.substring(imageUrl.lastIndexOf("/"));
            String imageRealPath = root + imageUrl.substring(imageUrl.lastIndexOf("/youngo/images") + 16);
            String destinatePathDir = root + destinatePath;
            logger.debug("***imageRealPath =" + imageRealPath + "********");
            logger.debug("***destinatePathDir =" + destinatePathDir + "********");

            File changeImageFile = new File(destinatePathDir);
            if (!changeImageFile.exists())
                changeImageFile.mkdirs();

            int temp = 0;
            FileInputStream inputStream = new FileInputStream(imageRealPath);
            FileOutputStream outStream = new FileOutputStream(destinatePathDir + imageName);
            while ((temp = inputStream.read()) != -1)
            {
                outStream.write(temp);
            }
            inputStream.close();
            outStream.close();
            //删除原来目录下的图片
            File preImageFile = new File(imageRealPath);
            preImageFile.delete();
        }
        logger.debug("***************************end call changeImagePath Post************");
        return "success";
    }
    
    @RequestMapping(value="/downloadImageFromInternet", method=RequestMethod.GET)
    public String downlowdImageFromInternet(HttpServletRequest request, String url, String dir, String fileName){
    	HttpURLConnection conn = null;
    	try {
			url = URLDecoder.decode(url, "utf-8");
			URL ur = new URL(url);
    		conn = (HttpURLConnection) ur.openConnection();
	        conn.setConnectTimeout(3000);
	        conn.setReadTimeout(10000);
	        conn.setDoOutput(true);
	        conn.setDoInput(true);
	        conn.setUseCaches(false);
	        InputStream in = null;
	        byte[] buff = new byte[8192];
			int i = -1;
			String root = request.getServletContext().getRealPath("/") + dir;
			File dirFile = new File(root);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			File storeFile = new File(dirFile, fileName);
			FileOutputStream output = null;
        	try{
        		in = conn.getInputStream();
        		output = new FileOutputStream(storeFile);
        		while( (i = in.read(buff)) > 0){
        			output.write(buff, 0, i);
        		}
	        }catch (IOException e) {
	        	return "error";
			}finally {
				if(null != in){
					try{in.close();}catch(Throwable t){}
				}
				if(null != output){
					try{output.close();}catch(Throwable t){}
				}
			}
		} catch (UnsupportedEncodingException e) {
			return "error";
		} catch (MalformedURLException e) {
			return "error";
		} catch (IOException e) {
			return "error";
		}finally {
			if(null != conn){
				conn.disconnect();
			}
		}
    	return "success";
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
}
