package com.radar.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.admin.AuthCheckFilter;
import org.xmpp.packet.Message;

import com.radar.broadcast.BoardcastEmitter;
import com.radar.utils.HixinUtils;
/**
 * 发送Message广播信息 
 * @ClassName:  BroadcastByMessageServlet   
 *    
 * @author: sunshine  
 * @date:   2015年2月2日 下午6:09:30
 */
public class BroadcastByMessageServlet extends HttpServlet {

    private static final long serialVersionUID = -387573832820473644L;
    //这里的路径  应该是 插件打包后的名称 + url名  
  	private static final String SERVLET_URI = "radar/broadcastbymessage";
  	
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
        String context = request.getParameter("context"); //发送内容
        String broadUsers = request.getParameter("to"); //接收人 
        String senduser = request.getParameter("senduser"); //发送人  发送人基本都内置管理员
        
        if(StringUtils.isEmpty(broadUsers) || StringUtils.isEmpty(senduser)){
        	this.replyError("error","广播对象为空", response, out);
        }
        
        //广播对象
        String [] users = broadUsers.split(","); 
        StringBuilder fail = new StringBuilder();
        
        //封装发送的message
        Message message = new Message();
        message.setID(UUID.randomUUID().toString());
        message.setType(Message.Type.chat);
        message.setBody(context);
        
        //遍历广播对象，逐个广播
        for (String user : users)
        {
            message.setFrom(senduser+"@"+HixinUtils.getDomain()+"/"+HixinUtils.getResource());
            Boolean flat = BoardcastEmitter.sendBoardCastAndStoreServer(user, message);
            if(!flat){
                fail.append(user);
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
