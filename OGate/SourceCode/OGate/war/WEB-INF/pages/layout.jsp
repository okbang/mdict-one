<%-- 
/*
 * layout.jsp 0.1 Apr 30, 2011
 * 
 * Copyright (c) 2011, Open-Ones Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 --%>
<%@page import="openones.gate.util.ConfManager"%>
<%@page import="openones.gae.users.OUser"%>
<%@page import="openones.gate.control.LayoutControl"%>
<%@page import="openones.gate.Cons"%>
<%@page import="com.google.appengine.api.users.User"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@page import="openones.gae.session.SessionCounter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${applicationScope.HomePage}</title>
<script type="text/javascript" src='pages/scripts/common.js'></script>
<link rel=stylesheet type=text/css href="pages/css/java.css">
</head>
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
        OUser ouser = new OUser(user);
        //System.out.println("ouser.getEmail()=" + ouser.getEmail());
        //System.out.println("ConfManager.getAdmin()=" + ConfManager.getAdminEmail());
        if (ouser.getEmail().equalsIgnoreCase(ConfManager.getAdminEmail())) {
            ouser.setAdmin(true);
        }
        
        session.setAttribute(Cons.SK_USER, ouser);
    }
%>

<body>
  <table border="1" width="100%" cellspacing="0" cellpadding="0">
    <tr>
      <td colspan="3">
        <jsp:include page="/header.part" flush="true"></jsp:include>
      </td>
    </tr>
    <tr>
        <td width="99%" colspan="3" class=uportal-background-dark noWrap align=left height=6>
        <jsp:include page="/navigation.part" flush="true">
            <jsp:param name="screenId" value="${screenId}"/>
            <jsp:param name="eventId" value="${eventId}"/>
        </jsp:include>
        </td>
    </tr>
    <tr>
      <td width="10%" nowrap rowspan="2"><IMG alt=""
        src="pages/images/transparent.gif" width=100 height=1> Menu
        <br/>
        ScreenId: ${screenId}<br/>
        EventId: ${eventId}<br/>
      </td>
      <td width="78%" height="436" valign="top">
        <c:if test='${MainScreen == "Service"}'>
          <jsp:include page="/service.mod" flush="true">
             <jsp:param name="screenId" value="${screenId}"/>
             <jsp:param name="eventId" value="${eventId}"/>
          </jsp:include></td>
        </c:if>

        <c:if test='${MainScreen == "Intro"}'>
           <jsp:include page="/intro.mod" flush="true">
             <jsp:param name="screenId" value="${screenId}"/>
             <jsp:param name="eventId" value="${eventId}"/>
           </jsp:include>
        </c:if>
        
        <c:if test='${empty MainScreen}'>
          <jsp:include page="/main.part" flush="true">
              <jsp:param name="screenId" value="Main"/>
          </jsp:include></td>
        </c:if>
        
        <c:if test='${MainScreen == "Member"}'>
          <jsp:include page="/member.mod" flush="true">
             <jsp:param name="screenId" value="${screenId}"/>
             <jsp:param name="eventId" value="${eventId}"/>
          </jsp:include></td>
        </c:if>
        
        <c:if test='${MainScreen == "Product"}'>
          <jsp:include page="/product.mod" flush="true">
             <jsp:param name="screenId" value="${screenId}"/>
             <jsp:param name="eventId" value="${eventId}"/>
          </jsp:include></td>
        </c:if>
        
      <td width="12%" nowrap height="436" valign="top">
      <IMG alt="" src="pages/images/transparent.gif" width=150 height=1>
        <jsp:include page="/link.part" flush="true">
            <jsp:param name="screenId" value="${screenId}"/>
            <jsp:param name="eventId" value="${eventId}"/>
        </jsp:include></td>
      </td>
    </tr>
    <tr>
      <td width="78%" class=uportal-channel-text>
        <p align="center">${applicationScope.Copyright}
      </td>
      <td width="12%" class=uportal-channel-text>Lượt truy cập: <%= SessionCounter.getNmHits() %>
       <br> Đang đăng nhập: ${nmLogonUser}</td>
    </tr>
  </table>

</body>
</html>