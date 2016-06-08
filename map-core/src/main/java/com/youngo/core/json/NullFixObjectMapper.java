package com.youngo.core.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NullFixObjectMapper extends ObjectMapper {
	private static final long serialVersionUID = 5094210821052504118L;

	public NullFixObjectMapper() {
		super();
		// 空值处理为空串
		setSerializationInclusion(Include.NON_EMPTY);
		configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
	}
}
