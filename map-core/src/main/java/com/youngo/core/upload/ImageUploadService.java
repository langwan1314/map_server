package com.youngo.core.upload;

import com.youngo.core.exception.RestException;
import com.youngo.core.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fuchen<br>
 *         图片传输工具 ,将接收到的图片传输到图片服务器
 */

public class ImageUploadService extends FileUploadService
{
    private final Logger logger = LoggerFactory.getLogger(ImageUploadService.class);
    /**
     * @param group 图片的存放目录
     * @return 图片存储到图片服务器后, 访问该图片的url
     * @throws IOException
     */
    public String[] sendToServer(MultipartHttpServletRequest request, String group)
    {
        validate(request, 1024);
        Map<String, MultipartFile> fileMap = request.getFileMap();
        try
        {
            return sendToServer(fileMap, group);
        } catch (IOException e)
        {
            logger.error("图片上传失败",e);
            throw new RestException(Result.param_error,Result.param_error_msg);
        }
    }

    public String sendToServer(byte[] bytes, String group)
    {
        try
        {
            return super.sendToServer(bytes,group);
        } catch (IOException e)
        {
            logger.error("图片上传失败", e);
            throw new RestException(Result.param_error,Result.param_error_msg);
        }
    }

    /**
     * 校验图片格式
     */
    private void validate(MultipartHttpServletRequest request, long maxSize)
    {
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext())
        {
            /** 处理文件流 **/
            String name = fileNames.next();
            MultipartFile multipartFile = request.getFile(name);

            long imageSizeByte = multipartFile.getSize();
            if (0 == imageSizeByte)
            {
                throw new RestException(HttpStatus.NOT_MODIFIED.value(), "图片大小为0KB,不能上传哦");
            }
            String originalFilename = multipartFile.getOriginalFilename();
            //String logImageName = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!checkPicture(originalFilename))
            {
                throw new RestException(HttpStatus.NOT_MODIFIED.value(), "图片格式不正确,仅支持jpg|jpeg|png|bmp格式的图片上传哦!");
            }
            long sizeKb = imageSizeByte / 1024;
            if (sizeKb > maxSize)
            {
                throw new RestException(HttpStatus.NOT_MODIFIED.value(), "图片最大限制为" + maxSize + "KB,亲上传的图片超过限制了哦");
            }
        }
    }

    /**
     * @param picture
     * @return the boolean
     * @Title: checkPicture
     * @Description: 校验图片格式
     */
    public boolean checkPicture(String picture)
    {
        Pattern p = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|bmp))$)");
        Matcher m = p.matcher(picture);
        return m.matches();
    }

}
