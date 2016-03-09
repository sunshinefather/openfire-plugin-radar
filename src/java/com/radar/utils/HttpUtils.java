package com.radar.utils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
/**
 * Http请求工具
 * @ClassName:  HttpUtils   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年6月23日 下午2:57:54
 */
public class HttpUtils
{

    /**
     * 连接超时
     */
    private static int connectTimeOut = 10*1000;
    
    /**
     * 发送get的HTTP请求
     * @Title: doGet
     * @param reqUrl HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     * @throws Exception 错误信息
     */
    public static String doGet(String reqUrl)
    		throws Exception
    		{
    	UTF8GetMethod method = new UTF8GetMethod(reqUrl);
    	HttpClient client = new HttpClient();
    	client.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeOut);
    	try {
    		client.executeMethod(method); //发送请求
    	} catch (HttpException e) {
    		throw new Exception("请求失败", e);
    	} catch (IOException e) {
    		throw new Exception("网络故障", e);
    	}
    	String responseContent = "";
    	try {
    		int status = method.getStatusCode();
    		if(status!=200){
    			switch(status){
    			case 405: 
    				throw new Exception("不允许GET方法访问");
    			case 404: 
    				throw new Exception("访问地址<"+reqUrl+">不存在");
    			default:
    				throw new Exception("远程提交返回错误，状态码为："+status);
    			}
    		}
            //获取返回结果,如果使用getResponseBodyAsString会出现乱码问题
            //responseContent = method.getResponseBodyAsString();
            responseContent=convertStreamToString(method.getResponseBodyAsStream());
    	} catch (IOException e) {
    		throw new Exception("响应失败", e);
    	}
    	return responseContent;
    		}

    /**
     * 发送带参数的POST的HTTP请求
     * @param reqUrl HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     * @throws Exception 错误信息
     */
    public static String doPost(String reqUrl, Map<String, String> parameters)
            throws Exception
    {
        UTF8PostMethod method = new UTF8PostMethod(reqUrl);
        if(parameters!=null)
        {
            NameValuePair [] param = new NameValuePair[parameters.size()];
            int ind = 0;
            for(String key:parameters.keySet()){
                NameValuePair simcard = new NameValuePair(key,parameters.get(key));
                param[ind] = simcard;
                ind++;
            }
            method.setRequestBody(param);
        }
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeOut);
        try {
            client.executeMethod(method); //发送请求
        } catch (HttpException e) {
            throw new Exception("请求失败", e);
        } catch (IOException e) {
            throw new Exception("网络故障", e);
        }
        String responseContent = "";
        try {
            int status = method.getStatusCode();
            if(status!=200){
                switch(status){
                case 405: 
                    throw new Exception("拒绝访问");
                case 404: 
                    throw new Exception("访问地址<"+reqUrl+">不存在");
                default:
                    throw new Exception("远程提交返回错误，状态码为："+status);
                }
            }
            //获取返回结果,如果使用getResponseBodyAsString会出现乱码问题
            //responseContent = method.getResponseBodyAsString();
            responseContent=convertStreamToString(method.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new Exception("响应失败", e);
        }
        return responseContent;
    }    
    
    /**
     * 发送带参数的POST的HTTP请求,可post上传文件
     * @param reqUrl HTTP请求URL
     * @param parameters 参数映射表
     * @param files 文件
     * @return HTTP响应的字符串
     * @throws Exception 错误信息
     */
    public static String doPost(String reqUrl, Map<String, String> parameters,Map<String, File> files)
            throws Exception
    {
        UTF8PostMethod method = new UTF8PostMethod(reqUrl);
        
        Part[] parts = new Part[files==null?0:files.size()];
        int index = 0;
        if(files!=null && files.size()>0)
        {
            for(String key:files.keySet()){
                parts[index++]=new FilePart(key, files.get(key));
            }
        }
        if(parameters!=null)
        {
            NameValuePair [] param = new NameValuePair[parameters.size()];
            int ind = 0;
            for(String key:parameters.keySet()){
                NameValuePair simcard = new NameValuePair(key,parameters.get(key) );
                param[ind] = simcard;
                ind++;
            }
            //如果使用setRequestBody 对方获取不到 param 这里不知道为什么原因  也许是因为有文件的原因
            method.setQueryString(param);
        }
        method.setRequestEntity(new MultipartRequestEntity(parts,method.getParams()));
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeOut);
        try {
            client.executeMethod(method); //发送请求
        } catch (HttpException e) {
            throw new Exception("请求失败", e);
        } catch (IOException e) {
            throw new Exception("网络故障", e);
        }
        String responseContent = "";
        try {
            int status = method.getStatusCode();
            if(status!=200){
                switch(status){
                case 405: 
                    throw new Exception("拒绝访问");
                case 404: 
                    throw new Exception("访问地址<"+reqUrl+">不存在");
                default:
                    throw new Exception("远程提交返回错误，状态码为："+status);
                }
            }
            //获取返回结果,如果使用getResponseBodyAsString会出现乱码问题
            //responseContent = method.getResponseBodyAsString();
            responseContent=convertStreamToString(method.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new Exception("响应失败", e);
        }
        return responseContent;
    }


    public static void main(String[] args) throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("width", "200");
        map.put("height", "200");
        map.put("uid", "29");
        String temp = HttpUtils.doGet("http://blog.csdn.net/peterwanghao/article/details/1771723");
        //String temp = HttpUtils.doGet("http://192.168.253.241:8080/child_spring/rest/user?ids=4035");

        System.out.println("返回的消息是:" + temp);
        
    }
    /**
     * 处理乱码问题
     * @Title: convertStreamToString
     * @Description: TODO  
     * @param: @param is
     * @param: @return      
     * @return: String
     * @author: sunshine  
     * @throws
     */
	public static String convertStreamToString(InputStream is) {      
        StringBuilder str = new StringBuilder();      
        byte[] bytes = new byte[4096];    
        int size = 0;    
        try {      
            while ((size = is.read(bytes)) > 0) {    
                String _tmp = new String(bytes, 0, size, "UTF-8");
                str.append(_tmp);  
            }    
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            try {      
                is.close();      
            } catch (IOException e) {      
               e.printStackTrace();      
            }      
        }      
        return str.toString();   
    }  

}

class UTF8PostMethod extends PostMethod{   
	  public UTF8PostMethod(String url){   
	    super(url);   
	  }   
	  @Override   
	  public String getRequestCharSet() { 
	    return "UTF-8";   
	  }   
}   

class UTF8GetMethod extends GetMethod{   
	    public UTF8GetMethod(String url){   
	      super(url);   
	    }   
	    
	    @Override   
	    public String getRequestCharSet() {   
	      return "UTF-8";   
	    }   
  }