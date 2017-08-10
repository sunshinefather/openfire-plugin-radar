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
 * 版本更新公告
 * @ClassName:  VersionBoardcastServlet   
 *    
 * @author: sunshine  
 * @date:   2015年4月17日 上午10:31:47
 */
public class VersionBoardcastServlet extends HttpServlet {
    
    private static final long serialVersionUID = -253265246210087062L;
    //这里的路径  应该是 插件打包后的名称 + url名  
  	private static final String SERVLET_URI = "radar/versionboardcast";

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
        String touid = request.getParameter("to"); //接收人 
        String version = request.getParameter("version");   //新版本
        
        if(StringUtils.isEmpty(touid)){
            return ;
        }
        
        //广播对象
        String [] users = touid.split(","); 
        StringBuilder fail = new StringBuilder();
        
        //封装版本更新广播IQ包
        IQ iq = new IQ();
        iq.setFrom(HixinUtils.getDomain());
        iq.setChildElement("query","http://helome.org/app/update");
        iq.getChildElement().addElement("item").addAttribute("version", version);
        
        //遍历广播对象
        for (String user : users)
        {
            Boolean flat = BoardcastEmitter.sendBoardCastServer(user, iq);
            if(!flat){
                fail.append(user);
                fail.append(",");
            }
        }
        
        //返回广播结果
        if("".equals(fail.toString()))
           this.replyMessage("ok", response, out);
        else
           this.replyError("error",fail.toString(), response, out); 
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
