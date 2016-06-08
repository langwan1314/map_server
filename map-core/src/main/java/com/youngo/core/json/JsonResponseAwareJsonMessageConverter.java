package com.youngo.core.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.youngo.core.exception.RestException;
import com.youngo.core.model.Result;
import org.apache.log4j.Logger;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.Map;


/**
 * Adds support for {@link JsonResponse} annotation
 * 
 * @author Jack Matthews
 * 
 */
final class JsonResponseAwareJsonMessageConverter extends MappingJackson2HttpMessageConverter
{
	private static Logger logger = Logger.getLogger(JsonResponseAwareJsonMessageConverter.class);

	public JsonResponseAwareJsonMessageConverter()
	{
		super();
		setObjectMapper(new NullFixObjectMapper());
	}

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException
	{
		if (object instanceof ResponseWrapper)
		{
			writeJson((ResponseWrapper) object, outputMessage);
		} else
		{
			super.writeInternal(fixResult(object), outputMessage);
		}
	}

	protected void writeJson(ResponseWrapper response, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        NullFixObjectMapper objectMapper = new NullFixObjectMapper();
        // Add support for jackson mixins
        JsonMixin[] jsonMixins = response.getJsonResponse().mixins();
        for (JsonMixin jsonMixin : jsonMixins) {
        	objectMapper.addMixInAnnotations(jsonMixin.target(), jsonMixin.mixin());
        }
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
        try {
        	objectMapper.writeValue(jsonGenerator, fixResult(response.getOriginalResponse()));
        } catch (IOException ex) {
        	logger.error("Could not write JSON: " , ex);
            throw new RestException(Result.param_error , Result.param_error_msg);
        }
    }

	private Object fixResult(Object returnValue)
	{
		if (returnValue instanceof Result || returnValue instanceof Map)
		{
			return returnValue;
		}
		Result result = new Result();
		if (returnValue instanceof RestException)
		{
			RestException ex = (RestException) returnValue;
			result.setCode(ex.getCode());
			result.setMsg(ex.getMessage());
		} else
		{
			result.setCode(Result.success_code);
			result.setMsg(Result.success_msg);
			if (returnValue == null)
			{
				result.setData("");
			} else
			{
				result.setData(returnValue);
			}
		}
		return result;
	}

}
