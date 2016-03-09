<%@page import="com.radar.common.EnvConstant"%>
<%@page import="com.radar.listener.PropertyListenerImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*,org.jivesoftware.openfire.XMPPServer,org.jivesoftware.util.*" errorPage="error.jsp"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<%-- Define Administration Bean --%>
<jsp:useBean id="admin" class="org.jivesoftware.util.WebManager"  />
<c:set var="admin" value="${admin.manager}" />
<%
	admin.init(request, response, session, application, out );
%>

<%
	// Get parameters
    boolean save = request.getParameter("save") != null;
    boolean success = request.getParameter("success") != null;
    String secret = ParamUtils.getParameter(request, "secret");
    boolean enabled = true;
    String searchEnginesUrl = ParamUtils.getParameter(request, "searchEnginesUrl");
    String hiServerUrl = "";
    String memcachedAddress = ParamUtils.getParameter(request, "memcachedAddress");
 
    // Handle a save
    Map errors = new HashMap();
    if (save) {
        if (errors.size() == 0) {
            PropertyListenerImpl.setSecret(secret);
            PropertyListenerImpl.setSearchEnginesUrl(searchEnginesUrl);
            PropertyListenerImpl.setMemcachedAddress(memcachedAddress);
            response.sendRedirect("hi-xin.jsp?success=true");
            return;
        }
    }

    secret = EnvConstant.SECRET;
    searchEnginesUrl = EnvConstant.SEARCH_ENGINESURL;
    memcachedAddress = EnvConstant.MEMCACHED_ADDRESS;
%>

<html>
    <head>
        <title>掌域通插件属性配置</title>
        <meta name="pageID" content="radar"/>
    </head>
    <body>


<p>
 这些属性主要用于插件内部需要使用的属性，其中 searchEnginesUrl和hiServerUrl为聊天纪录拦截写入搜索引擎所必需属性，
 secret属性为提交servlet的http访问所必需使用的密钥
</p>

<%  if (success) { %>

    <div class="jive-success">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr><td class="jive-icon"><img src="images/success-16x16.gif" width="16" height="16" border="0"></td>
        <td class="jive-icon-label">
                 嗨信服务属性添加成功.
        </td></tr>
    </tbody>
    </table>
    </div><br>
<% } %>

<form action="hi-xin.jsp?save" method="post">

<fieldset>
    <legend>hi xin</legend>
    <div>
    <p>
    </p>

    <p>However, the presence of this service exposes a security risk. Therefore,
    a secret key is used to validate legitimate requests to this service.
    </p>
    <ul>
        <input type="radio" name="enabled" value="true" id="rb01"
        <%= ((enabled) ? "checked" : "") %>>
        <label for="rb01"><b>Enabled</b> - hi service requests will be processed.</label>
        <br>
        <input type="radio" name="enabled" value="false" id="rb02"
         <%= ((!enabled) ? "checked" : "") %>>
        <label for="rb02"><b>Disabled</b> - hi service requests will be ignored.</label>
        <br><br>

        <label for="text_secret">Secret key:</label>
        <input type="text" name="secret" value="<%= secret %>" id="text_secret">
        <br><br>
        
        <label for="text_searchEnginesUrl">searchEnginesUrl:</label>
        <input type="text" name="searchEnginesUrl" value="<%= searchEnginesUrl %>" style="width: 500px;" id="text_searchEnginesUrl">
        <br><br>
        
        <label for="text_hiServerUrl">hiServerUrl:</label>
        <input type="text" name="hiServerUrl" value="<%= hiServerUrl %>" style="width: 500px;" id="text_hiServerUrl">
        <br><br>
        
        <label for="text_memcachedAddress">memcachedAddress:</label>
        <input type="text" name="memcachedAddress" value="<%= memcachedAddress %>" style="width: 500px;" id="text_memcachedAddress">
        <br><br>
    </ul>
    </div>
</fieldset>

<br><br>

<input type="submit" value="Save Settings">
</form>
</body>
</html>