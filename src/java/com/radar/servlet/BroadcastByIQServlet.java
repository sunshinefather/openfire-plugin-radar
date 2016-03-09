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
 * 企业后台 添加用户 删除用户后的更新通讯录广播
 * @ClassName:  BroadcastByIQServlet   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午6:10:53
 */
public class BroadcastByIQServlet extends HttpServlet {
	
	private static final long serialVersionUID = -903213930494328400L;
	//这里的路径  应该是 插件打包后的名称 + url名  
	private static final String SERVLET_URI = "radar/broadcastbyiq";
    
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
        String broadUsers = request.getParameter("to"); //接收人 
        
        if(StringUtils.isEmpty(broadUsers)){
        	this.replyError("error","广播对象为空", response, out);
        }
        
        //更新通讯录的IQ包
        IQ iq = new IQ();
        iq.setFrom(HixinUtils.getDomain());
        iq.setChildElement("query","http://helome.org/contacts/update"); 
        
        //广播对象
        String [] broadUserNames = broadUsers.split(","); 
        StringBuilder fail = new StringBuilder();
        
        //遍历广播对象，逐个广播
        for (String userName : broadUserNames)
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
