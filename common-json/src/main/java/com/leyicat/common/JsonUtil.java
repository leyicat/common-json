package com.leyicat.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.leyicat.common.json.BeanFilter;
import com.leyicat.common.json.FilterFeature;
import com.leyicat.common.json.JsonFilter0;
import com.leyicat.common.json.JsonFilter1;
import com.leyicat.common.json.JsonFilter2;
import com.leyicat.common.json.JsonFilter3;
import com.leyicat.common.json.JsonFilter4;
import com.leyicat.common.json.JsonFilter5;
import com.leyicat.common.json.JsonFilter6;
import com.leyicat.common.json.JsonFilter7;
import com.leyicat.common.json.JsonFilter8;
import com.leyicat.common.json.JsonFilter9;

/**
 * JSON处理的工具类<br>
 * 根据实际项目的需要，初始化了一些基础属性,主要包括：<br>
 * （1）对于Long和long类型序列化时当作字符串输出，主要是为了解决Java和JavaScript长整型类型范围不一致，
 * 个别情况出现精度丢失的情况
 * （2）日期时间类型的类型
 * （3）不输出null值
 */
public class JsonUtil {
	private final static Logger logger = Logger.getLogger(JsonUtil.class);
	private static ObjectMapper mapper = new ObjectMapper();
	private static Class<?>[] jsonFilter = new Class[]{
		JsonFilter0.class,JsonFilter1.class,JsonFilter2.class,JsonFilter3.class,
		JsonFilter4.class,JsonFilter5.class,JsonFilter6.class,JsonFilter7.class,
		JsonFilter8.class,JsonFilter9.class
	};
	
	static {
		initMapper(mapper);
	}
	
	private static void initMapper(ObjectMapper mapper){
		SimpleModule module = new SimpleModule();
		module.addSerializer(Long.class, ToStringSerializer.instance);
		module.addSerializer(Long.TYPE, ToStringSerializer.instance);

		mapper.registerModule(module);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//过滤对象的null属性
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);//过滤map中的null值
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}
	
	/**
	 * 将Java对象序列化JSON字符串
	 * @param result Javad对象
	 * @return 异常时输出空字符串
	 */
	public static String toJSON(Object result) {
		try {
			return mapper.writeValueAsString(result);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(),e);
			return "";
		}
	}
	
	/**
	 * 将JSON字符串反序列化为Java对象
	 * @param jsonStr
	 * @param clazz	
	 * @return 异常时输出null
	 */
	public static <T> T toJavaObject(String jsonStr,Class<T> clazz) {
		try {
			return mapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 将JSON数组字符串反序列化为Java List
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> toJavaArray(String jsonStr,Class<T> clazz) {
		List<T> result = new ArrayList<>();
		JsonNode jsonNode = JsonUtil.toJSONObject(jsonStr);
		if(jsonNode.isArray()){
			for(int i=0;i<jsonNode.size();i++){
				T t = JsonUtil.toJavaObject(jsonNode.get(i).toString(), clazz);
				result.add(t);
			}
		}
		return result;
	}
	
	/**
	 * 将JSON字符串转换成JSON对象
	 * @param jsonStr
	 * @return 异常时返回null
	 */
	public static JsonNode toJSONObject(String jsonStr) {
		try {
			return mapper.readTree(jsonStr);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 将JSON对象转换成Java对象
	 * @param jsonNode
	 * @param clazz
	 * @return 异常时返回null
	 */
	public static <T> T jsonNodetoJava(JsonNode jsonNode,Class<T> clazz) {
		try {
			return mapper.treeToValue(jsonNode, clazz);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 将Java对象序列化JSON字符串,只输出指定的属性
	 * @param result
	 * @param fields：字段列表
	 * @return
	 */
	public static String toJSON(Object result,String fields){
		return toJSON(result,new BeanFilter<>(result.getClass(),fields));
	}
	
	/**
	 * 将Java对象序列化JSON字符串,只输出指定的属性
	 * @param result
	 * @param fields:字段列表
	 * @param feature:属性过滤类型
	 * @return
	 */
	public static String toJSON(Object result,String fields,FilterFeature feature){
		return toJSON(result,new BeanFilter<>(result.getClass(),fields,feature));
	}
	
	/**
	 * 将Java对象序列化JSON字符串
	 * @param result
	 * @param filters:属性过滤器，可以指定最多10个属性过滤器，每个属性过滤器针对Java对象中的一个Bean(或对象)
	 * @return
	 */
	public static String toJSON(Object result,BeanFilter<?>... filters) {
		if(null==filters || filters.length==0)return toJSON(result);
		
		if(filters.length>10){
			logger.error("BeanFilter too many,max=10,actual number="+filters.length);
		}
		ObjectMapper omap = new ObjectMapper();
		initMapper(omap);
		//FilterProvider fp = new SimpleFilterProvider();
		SimpleFilterProvider fp = new SimpleFilterProvider();
		for(int i=0;i<filters.length;i++){
			BeanFilter<?> fi = filters[i];
			
			omap.addMixIn(fi.getBeanClass(), jsonFilter[i]);
			
			if(fi.getFeature().equals(FilterFeature.Include)){
				fp.addFilter("filter"+i,SimpleBeanPropertyFilter.filterOutAllExcept(fi.getFields().split(",")));
			}else{
				fp.addFilter("filter"+i,SimpleBeanPropertyFilter.serializeAllExcept(fi.getFields().split(",")));
			}
		}
		ObjectWriter writer = omap.writer(fp);
		try {
			return writer.writeValueAsString(result);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}	
}
