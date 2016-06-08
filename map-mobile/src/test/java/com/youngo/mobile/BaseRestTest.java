/**
 * @Title: BaseRestTest.java
 * @Package com.yjh.mobile
 * @Description: rest-assured 测试基类
 * @author yiyan
 * @date 2014-12-15 上午10:20:41
 * @version
 */
package com.youngo.mobile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.DecoderConfig;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.json.config.JsonPathConfig;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.youngo.core.model.Result;
import com.youngo.mobile.model.auth.Auth;
import com.youngo.mobile.model.auth.LoginInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.jmeter.protocol.java.sampler.JUnitSampler;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.jayway.restassured.RestAssured.given;

/**
 * 类名称：BaseRestTest
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-12-15 上午10:20:41
 * 修改人：yiyan
 * 修改时间：2014-12-15 上午10:20:41
 * 修改备注：
 * https://code.google.com/p/rest-assured/wiki/Usage
 * rest-assured 支持 jsonPath 的语法，从json 返回取数据并做校验
 * get("/home").then().assertThat().body("data.ad1.size()",
 * greaterThan(0));
 * 也可以直接转换成对应的Model类，更方便Java开发人员
 * HomePageModel homePageModel =
 * get("/home").jsonPath().getObject("data", HomePageModel.class);
 * assertNotNull(homePageModel.getAd1());
 * assertThat(homePageModel.getAd1().size(), greaterThan(0));
 * * 可以设置默认的返回断言
 * ResponseSpecBuilder builder = new ResponseSpecBuilder();
 * builder.expectStatusCode(200);
 * builder.expectBody("msg", equalTo("success"));
 * ResponseSpecification responseSpec = builder.build();
 * RestAssured.responseSpecification = responseSpec;
 * RestAssured 支持 GSON, JAXB, Jackson and Faster Jackson 可以根据需要配置基础Mapper
 * 以下是Jackson的示例
 * RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
 * new ObjectMapperConfig().jackson2ObjectMapperFactory(new
 * Jackson2ObjectMapperFactory()
 * {
 *
 * @Override public ObjectMapper create(Class aClass, String s)
 * {
 * ObjectMapper objectMapper = new ObjectMapper();
 * objectMapper.configure(DeserializationFeature.
 * FAIL_ON_UNKNOWN_PROPERTIES, false);
 * DateFormat dateFormat = new
 * SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
 * objectMapper.setDateFormat(dateFormat);
 * objectMapper.getDeserializationConfig().with(dateFormat);
 * return objectMapper;
 * }
 */
public abstract class BaseRestTest
{
    /*
     * Debug 代码，当无法解析时，复制这段代码，查看原始请求数据
     * Response response =
     * given().contentType(ContentType.JSON).headers(headers)
     * .body(givenUser).when().post("/user/login");
     * assertNotNull(response);
     * String result = IOUtils.toString(response.asInputStream());
     */

    // 真实用户ID 开发环境
    public static final String defaultUserId = "64839";

    // 默认超时
    public static final int defaultTimeout = 10000;

    ObjectMapper objectMapper;

    static
    {


        // 开发环境
        // RestAssured.baseURI = "http://192.168.2.52/";
        // RestAssured.port = 8080;
        // RestAssured.basePath = "/yunifang/mobile";

        // 开发环境调试图片服务器
        //        RestAssured.baseURI = "http://192.168.2.52/";
        //        RestAssured.port = 80;
        //        RestAssured.basePath = "/yunifang";

        // 集群
        /* RestAssured.baseURI = "http://192.168.2.52/";
         RestAssured.port = 80;
         RestAssured.basePath = "/yunifang/mobile";*/
    	

    	// 香港
//        RestAssured.baseURI = "http://47.89.28.120/";
//        RestAssured.port = 80;
//        RestAssured.basePath = "/mobile";

    	// 正式
//        RestAssured.baseURI = "http://www.bellooo.com";
//        RestAssured.port = 80;
//        RestAssured.basePath = "/mobile";
    	
//        // 本机
        RestAssured.baseURI = "http://localhost/";
        RestAssured.port = 8080;
        RestAssured.basePath = "/soga";
        
        //测试机
//        RestAssured.baseURI = "http://121.42.32.149/";
//        RestAssured.port = 80;
//        RestAssured.basePath = "/mobile";
        
        // RestAssured.requestContentType(ContentType.JSON);
        // RestAssured.responseContentType(ContentType.JSON);

        RestAssured.config = RestAssuredConfig.config().decoderConfig(new DecoderConfig().defaultContentCharset("UTF-8")).config().encoderConfig(new EncoderConfig().defaultContentCharset("UTF-8"));

        // JsonPath 转换日期时的配置
        JsonPath.config = new JsonPathConfig("UTF-8").jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory()
        {
            @Override
            public ObjectMapper create(Class aClass, String s)
            {
                ObjectMapper objectMapper = new ObjectMapper();
                DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                objectMapper.setDateFormat(dateFormat);
                return objectMapper;
            }
        });
    }

    /**
     * @param url
     * @return
     */
    public Result doGet(String url)
    {
        return given().headers(doLogin()).get(url).as(Result.class);
    }


    /**
     * @param url
     * @param headers
     * @return
     */
    public Result doGet(String url, Map<String,String> headers)
    {
        return given().headers(doLogin(headers)).get(url).as(Result.class);
    }

    public <T> T doGet(String url , Class<T> objectType)
    {
        return given().headers(doLogin()).get(url).jsonPath().using(JsonPath.config).getObject("data", objectType);
    }
    /**
     * 如果传headers，注意不能为空
     * @param url
     * @param headers
     * @param objectType
     * @return
     */
    public <T> T doGet(String url, Map<String,String> headers, Class<T> objectType)
    {
        return given().headers(doLogin(headers)).get(url).jsonPath().using(JsonPath.config).getObject("data", objectType);
    }

    /**
     * @param url
     * @param params
     * @return
     */
    public Result doGetWithParam(String url, Map<String, String> params)
    {
        return given().headers(doLogin()).params(params).get(url).as(Result.class);
    }

    /**
     * @param url
     * @param params
     * @param objectType
     * @return
     */
    public <T> T doGetWithParam(String url, Map<String, String> params, Class<T> objectType)
    {
         return given().headers(doLogin()).params(params).get(url).jsonPath().using(JsonPath.config).getObject("data", objectType);
    }

    /**
     * @param url
     * @param object
     * @return
     */
    public Result doPost(String url, Object object)
    {
        return given().contentType(ContentType.JSON).headers(doLogin()).body(object).when().post(url).as(Result.class);
    }

    /**
     * @param url       请求的URL
     * @param params    参数，可以包括需要上传的文件和表单数据
     * @return
     */
    public Result doPostWithMutiPart(String url, Map<String, Object> params)
    {
        RequestSpecification specification = given().headers(doLogin());
        Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext())
        {
            Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof File)
            {
                specification.multiPart(key, (File) value);
            } else
            {
                specification.formParam(key, value);
            }
        }
        return specification.when().post(url).as(Result.class);

    }

    /**
     * @param url
     * @param headers
     * @param object
     * @return
     */
    public Result doPost(String url, Map<String, String> headers, Object object)
    {
        return given().contentType(ContentType.JSON).headers(doLogin(headers)).body(object).when().post(url).as(Result.class);
    }


    /**
     * @param url
     * @param headers
     * @param object
     * @param objectType
     * @return
     */
    public <T> T doPost(String url, Map<String, String> headers, Object object, Class<T> objectType)
    {
        Response post = given().contentType(ContentType.JSON).headers(doLogin(headers)).body(object).when().post(url);
        return post.jsonPath().using(JsonPath.config).getObject("data", objectType);
    }

    /**
     * @param url
     * @param object
     * @param objectType
     * @return
     */
    public <T> T doPost(String url, Object object, Class<T> objectType)
    {
        return given().contentType(ContentType.JSON).headers(doLogin()).body(object).when().post(url).jsonPath().using(JsonPath.config).getObject("data", objectType);
    }

    public Map<String ,String > doLogin()
    {
        HashMap<String, String> header = new HashMap<>();
//        Auth auth = new Auth();
//        auth.setToken("9fedca71-f954-4e47-a840-3458cc7370dX");
//        LoginInfo loginInfo = given().contentType(ContentType.JSON).body(auth).when().post("/login/tokenAuth").jsonPath().using(JsonPath.config).getObject("data", LoginInfo.class);
//        String passport = loginInfo.getPassport();
        header.put("passport","9449b7b3-93af-4619-b5ad-e0d9a3600a4b");
        return header;
    }

    /**
     * @param headers
     * @return
     */
    public Map<String, String> doLogin(Map<String, String> headers)
    {
        if(org.springframework.util.StringUtils.isEmpty(headers.get("headers")))
        {
            Auth auth = new Auth();
            auth.setToken("83b7e1c6a22424f5b4c47bb30798b770");
            LoginInfo loginInfo = given().contentType(ContentType.JSON).body(auth).when().post("/login/tokenAuth").jsonPath().using(JsonPath.config).getObject("data", LoginInfo.class);
            String passport = loginInfo.getPassport();
            headers.put("passport",passport);
        }
        return headers;
    }

    /**
     * @return
     */
    public ObjectMapper getObjectMapper()
    {
        if (this.objectMapper == null)
        {
            this.objectMapper = new ObjectMapper();
        }

        return this.objectMapper;
    }

    /**
     * @param json
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public List<?> jsonToList(String json) throws IOException
    {
        return this.getObjectMapper().readValue(json, List.class);
    }

    /**
     * @param json
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public Map<String, Object> jsonToMap(String json) throws IOException
    {
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>()
        {
        };

        return this.getObjectMapper().readValue(json, typeRef);
    }

    /**
     * 随机返回50位字符串
     *
     * @return
     */
    public String randomImei()
    {
        return RandomStringUtils.randomAlphabetic(50);
    }

    /**
     * 随机生成六位验证码
     *
     * @return
     */
    public String randomValidationCode()
    {
        String radomNumber = (int) (Math.floor(Math.random() * 1000000)) + "";
        while (radomNumber.length() < 6)
        {
            radomNumber = (int) (Math.floor(Math.random() * 1000000)) + "";
        }

        return radomNumber;
    }

    /**
     * @param variableName
     * @return
     */
    public String getJMeterVariable(String variableName)
    {
        JUnitSampler sampler = new JUnitSampler();
        if (sampler.getThreadContext() != null && sampler.getThreadContext().getVariables() != null)
        {
            return sampler.getThreadContext().getVariables().get(variableName);
        } else
        {
            return "";
        }
    }

}
