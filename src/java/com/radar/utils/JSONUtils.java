package com.radar.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONUtils {
	
public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static String toJSONString(Object o,String... filterProperties){
		return toJSONStringWithDateFormat(o,JSONUtils.dateFormat, filterProperties);
	}
	
	public  static String toJSONStringWithDateFormat(Object o,String dateFormat,String...filterProperties){
		SerializeWriter sw=new SerializeWriter();
		JSONSerializer jl=new JSONSerializer(sw);
		jl.config(SerializerFeature.WriteDateUseDateFormat,true);
		jl.setDateFormat(dateFormat);
		jl.config(SerializerFeature.DisableCircularReferenceDetect, true);
		for(final String filterProperty:filterProperties){
			jl.getPropertyFilters().add(new PropertyFilter(){
				public boolean apply(Object arg0, String name, Object arg2) {
					if(filterProperty.equals(name))
						return false;
					else
						return true;
				}});
		}
		jl.write(o);
		return jl.toString();
	}
	
	public static void  response(HttpServletResponse resp,Object o,String... filterProperties){
		try {
			resp.setContentType("application/json;charset=utf-8");
			PrintWriter writer = resp.getWriter();
			writer.write(toJSONString(o, filterProperties));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void Success(Object o,HttpServletResponse resp,String... filterStrings){
		Map<String, Object> resMap= new HashMap<String, Object>();
		resMap.put("status", 200);
		resMap.put("result",o);
		response(resp,resMap,filterStrings);
	}
	
	public static void Failure(String err_msg,HttpServletResponse resp){
		Map<String, Object> resMap= new HashMap<String, Object>();
		resMap.put("status", 500);
		resMap.put("msg",err_msg);
		response(resp,resMap);
	}
}