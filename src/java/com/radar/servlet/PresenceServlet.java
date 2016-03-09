package com.radar.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.admin.AuthCheckFilter;
import org.xmpp.packet.IQ;

import com.radar.broadcast.BoardcastEmitter;
import com.radar.utils.HixinUtils;
/**
 * 通知 下线（ 管理员主动把某人下线等）
 * @ClassName:  PresenceServlet   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午6:13:04
 */
public class PresenceServlet extends HttpServlet {
	
	private static final long serialVersionUID = -903213930494328400L;
	//这里的路径  应该是 插件打包后的名称 + url名  
	private static final String SERVLET_URI = "radar/presence";
    
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        AuthCheckFilter.addExclude(SERVLET_URI);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        String users = request.getParameter("to"); //接收人 
        
        if(StringUtils.isEmpty(users)){
        	this.replyError("error","设置下线对象为空", response, out);
        }
        
        //通知下线的IQ
        IQ iq = new IQ();
        iq.setFrom(HixinUtils.getDomain());
        iq.setChildElement("query","http://helome.org/user/deleted"); 
        
        //广播对象
        String [] broadUsers = users.split(","); 
        StringBuilder fail = new StringBuilder();
        
        //遍历广播对象，逐个广播
        for (String userName : broadUsers)
        {
            Boolean flat = BoardcastEmitter.sendBoardCastServer(userName.toLowerCase().trim(), iq);
            if(!flat){
                fail.append(userName);
                fail.append(",");
            }
        }
        
        //返回结果
        if("".equals(fail.toString())){
        	this.replyMessage("ok", response, out);
        }else{
        	this.replyError("error",fail.toString(), response, out); 
        }
    }

    private void replyMessage(String message,HttpServletResponse response, PrintWriter out){
        response.setContentType("text/xml");        
        out.println("<result>" + message + "</result>");
        out.flush();
    }

    private void replyError(String error,String fail,HttpServletResponse response, PrintWriter out){
        response.setContentType("text/xml");        
        out.println("<error>" + error + "</error><fail>"+fail+"</fail>");
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    public void destroy() {
        super.destroy();
        AuthCheckFilter.removeExclude(SERVLET_URI);
    }
}
