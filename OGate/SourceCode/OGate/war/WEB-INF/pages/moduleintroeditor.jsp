<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK"%>
<%--
<script type="text/javascript" src="/fckeditor/fckeditor.js"></script>
 --%>
<form name="frmModuleEdit" action="/moduleintroeditor.mod" method="post">
  <input type="hidden" name="screenId" value="ModuleIntroEditor"/>
  <%-- Default event: save --%>
  <input type="hidden" name="eventId" value="save"/>
  <input type="hidden" name="menuId" value="${menuId}"/>
  <input type="hidden" name="tabModuleId" value="${tabName}"/>
  <input type="hidden" name="tabModuleName" value="${tabName}"/>
  
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr><td align="right">
      <c:if test="${(not empty user) && (user.isAdmin)}">
       <a href="#" onclick='submitAction("frmModuleEdit","ModuleIntroEditor", "edit")'><img border="0" src="pages/images/editContent.gif" width="16" height="16"></a>
       <a href="#" onclick='submitAction("frmModuleEdit","ModuleIntroEditor", "list")'><img border="0" src="pages/images/ed_list_num.gif" width="16" height="16"></a>
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
         <jsp:include page="/WEB-INF/pages/module/list.jsp"></jsp:include>
        </c:when>
      <c:when test='${(eventId == "edit") || (eventId == "save")}'>
      <b>Cập nhật nội dung cho cho tab ${tabName}</b>
        <FCK:editor instanceName="content" height="500">
          <jsp:attribute name="value">${tabModule.content}</jsp:attribute>
        </FCK:editor>
      </c:when>
  
      <c:otherwise>
        ${tabModule.content}    
      </c:otherwise>
    </c:choose>
  </c:if>
  
  <%--  Guest or Normal user --%>
  <c:if test="${(empty user) || (not user.isAdmin)}">
  Module: ${tabName}
  <p>${tabModule.content}</p>
  </c:if>
  </td>
  </tr>
</table>
</form>
          
