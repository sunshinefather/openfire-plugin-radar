package com.radar.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 消息实体
 * @ClassName:  MsgInfoEnttity   
 *    
 * @author: sunshine  
 * @date:   2015年2月9日 下午2:16:06
 */
public class MsgInfoEnttity
{

    private int type;

    private String from;

    private String to;

    private String content;
    
    public String sendTime;
    
    private int contentType;
    
    private SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public MsgInfoEnttity(){
        
    }
    
    public MsgInfoEnttity(int type, Type contentType, String from,String to,String content, Date sendDate){
        this.type = type;
        this.from = from;
        this.to = to;
        this.content = content;
        this.contentType = contentType.code;
        this.sendTime = time.format(sendDate);
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getSendTime()
    {
        return sendTime;
    }

    public void setSendTime(String sendTime)
    {
        this.sendTime = sendTime;
    }

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}


	public enum Type {
		DIVIDER(0),MESSAGE(1), SOUND(2),IMAGE(3), FILE(4),SVIDEO(5),VIDEO(6),VOICE(7),PATIENT(8),OTHER(10);
		
		private int code;

		Type(int code) {
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}
		
		public void setCode(int code) {
			this.code = code;
		}
		
		@Override
		public String toString() {
			return String.valueOf(this.code);
		}
		
	}
}
