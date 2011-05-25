<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK"%>
<%--
<script type="text/javascript" src="/fckeditor/fckeditor.js"></script>
 --%>
<form name="frmIntro" action="/intro.mod" method="post">
  <input type="hidden" name="screenId" value="Intro"/>
  <%-- Default event: save --%>
  <input type="hidden" name="eventId" value="save"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr><td align="right">
      <c:if test="${(not empty user) && (user.isAdmin)}">
       <a href="#" onclick='submitAction("frmIntro","Intro", "edit")'><img border="0" src="pages/images/editContent.gif" width="16" height="16"></a>
       <a href="#" onclick='submitAction("frmIntro","Intro", "list")'><img border="0" src="pages/images/ed_list_num.gif" width="16" height="16"></a>
      </c:if>
    
  </td>
  </tr>
  <tr>
  <td>
   <!--  Admin -->
  <c:if test="${(not empty user) && (user.isAdmin)}">
    <c:choose>
         <%-- list screen --%>
        <c:when test='${(eventId == "list") || (eventId == "delete")}'>
         <jsp:include page="/WEB-INF/pages/intro/list.jsp"></jsp:include>
        </c:when>
      <c:when test='${(eventId == "edit") || (eventId == "save")}'>
        <FCK:editor instanceName="content" height="500">
          <jsp:attribute name="value">
          <p>${introForm.content}</p>
          </jsp:attribute>
        </FCK:editor>
      </c:when>
  
        <c:otherwise>
    ${introForm.content}    
        </c:otherwise>
    </c:choose>
  </c:if>
  
  <%--  Guest or Normal user --%>
  <c:if test="${(empty user) || (not user.isAdmin)}">
  <p>${introForm.content}</p>
  </c:if>
  </td>
  </tr>
</table>
</form>
          
