package com.radar.bean;

import java.util.HashMap;
import java.util.Map;
/**
 * 该数据体用于进行http协议json格式接口的返回信息描述。
 * @ClassName:  JsonEntity   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午2:45:27
 */
public class JsonEntity
{

    public JsonEntity()
    {
    }

    public JsonEntity(Object data, String errorinfo, ResponseCode responseCode)
    {
        this.data = data;
        this.responsecode = responseCode;
        this.errorinfo = errorinfo;
    }

    /**
     * 返回的请求结果查询
     */
    private Object data;

    /**
     * 接口请求的返回代码，从这个代码中可以识别接口调用是否成功，以及调用失败时的错误类型。 具体的code定义参见技术文档
     */
    private ResponseCode responsecode;

    /**
     * 错误的中文描述，错误信息的详细中文描述
     */
    private String errorinfo = "";

    /**
     * @return the data
     */
    public Object getData()
    {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object data)
    {
        this.data = data;
    }

    /**
     * @return the responsecode
     */
    public ResponseCode getResponsecode()
    {
        return responsecode;
    }

    /**
     * @param responsecode the responsecode to set
     */
    public void setResponsecode(ResponseCode responsecode)
    {
        this.responsecode = responsecode;
    }

    /**
     * @return the errorinfo
     */
    public String getErrorinfo()
    {
        return errorinfo;
    }

    /**
     * @param errorinfo the errorinfo to set
     */
    public void setErrorinfo(String errorinfo)
    {
        this.errorinfo = errorinfo;
    }

    /**
     * 错误代码描述
     * <pre>
     * 200 请求成功
     * 301 请求次数已经超过本周期设置的最大值
     * 302 请求频率已超过设定的最大值。
     * 303 该接口工作繁忙，产生拥堵，请稍后再试。
     * 304 
     * 401 规定的必传入项没有传入
     * 402 传入的参数项格式不符合规定
     * 403 传入参数项至少有一项超出规定的长度
     * 404 没有查询到符合条件的数据
     * 405 查询到重复数据
     * 501 服务器内部错误
     * 502 插入操作错误
     * 503 更新操作错误
     * 1001 可用余额不足
     * 1002 交易失败
     * 1003 用户被锁定
     * </pre>
     * 
     * @author wenjie
     */
    public enum ResponseCode
    {
        _200("200"), _301("301"), _302("302"), _303("303"), _304("304"), _401("401"), _402("402"), _403(
                "403"), _404("404"), _405("405"), _501("501"), _502("502"), _503("503"), _504("504"),
                _1001("1001"),_1002("1002"),_1003("1003");

        private String code;

        ResponseCode(String code)
        {
            this.code = code;
        }

        public String getCode()
        {
            return this.code;
        }

        /**
         * 该私有静态方法用于映射字符串和枚举信息的关系
         */
        private static final Map<ResponseCode, String> stringToEnum = new HashMap<ResponseCode, String>();
        static
        {
            for (ResponseCode blah : values())
            {
                stringToEnum.put(blah, blah.toString());
            }
        }

        /**
         * @param symbol
         * @return
         */
        public static String fromString(ResponseCode symbol)
        {
            return stringToEnum.get(symbol);
        }

        @Override
        public String toString()
        {
            return code;
        }
    }
}