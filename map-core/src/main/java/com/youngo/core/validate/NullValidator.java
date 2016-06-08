package com.youngo.core.validate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.youngo.core.exception.RestException;
import com.youngo.core.model.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;



/**
 * @author fuchen
 * 非空验证器
 */
public class NullValidator 
{
	private static final String GET = "get";
	/**
	 * @param target 待验证的对象
	 * @param fields 不允许为空的字段<br>
	 * 若target的fields字段中，存在field的值为空，抛出BizException<br>
	 * for example ：<br>
	 * String[] fields = {"userName" , "id" , "password" , "address.id"};<br>
	 * NullValidator.validate(user,fields);
	 */
	public static void validate(Object target , String[] fields){
		if(target==null){
			throw new RestException(Result.empty_param , Result.empty_param_msg);
		}
		List<String> emptyFields = new ArrayList<String>();
		Class<? extends Object> claz = target.getClass();
		for(String field : fields){
			try {
				String[] splits = field.split("\\.");
				Class<? extends Object> tempClaz = claz;
				Object tempTarget = target;
				for(String split : splits){
					String methodName = generateMethodName(split);
					Method method = BeanUtils.findMethodWithMinimalParameters(tempClaz, methodName);
					tempTarget = method.invoke(tempTarget);
					if(isEmpty(tempTarget)){
						emptyFields.add(field);
					}else{
						tempClaz = tempTarget.getClass();
					}
				}
			} catch (Exception e) {
				throw new RestException(HttpStatus.BAD_REQUEST.value() , HttpStatus.BAD_REQUEST.getReasonPhrase());
			} 		
		}
		createException(claz.getName() , emptyFields);
	}
	
	private static String generateMethodName(String field){
		String str = StringUtils.capitalize(field);
		return GET+str;
	}
	
	private static boolean isEmpty(Object obj){
		if(obj instanceof String){
			return StringUtils.isEmpty(obj);
		}
		return obj==null;
	}
	
	private static void createException(String className , List<String> emptyFields){
		if(!emptyFields.isEmpty()){
			String names=",names:"+className;
			for(String field : emptyFields){
				names = names+":"+field;
			}
			throw new RestException(Result.empty_param , Result.empty_param_msg+names);
		}
	}
	
}
