package com.youngo.core.upload;

import com.youngo.core.exception.RestException;
import com.youngo.core.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Map;

/**
 * Created by fuchen on 2015/12/4.
 * 语音上传
 */
public class VoiceUploadService extends FileUploadService
{
    private final Logger logger = LoggerFactory.getLogger(ImageUploadService.class);
    /**
     * @param group 图片的存放目录
     * @return 图片存储到图片服务器后, 访问该图片的url
     * @throws IOException
     */
    public String[] sendToServer(MultipartHttpServletRequest request, String group)
    {
        validate(request);
        Map<String, MultipartFile> fileMap = request.getFileMap();
        try
        {
            return sendToServer(fileMap, group);
        } catch (IOException e)
        {
            logger.error("语音上传失败", e);
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
            logger.error("语音上传失败", e);
            throw new RestException(Result.param_error,Result.param_error_msg);
        }
    }

    /**
     * 校验图片格式
     */
    private void validate(MultipartHttpServletRequest request)
    {
        //TODO 待补充
    }
}
