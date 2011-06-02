<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%-- Display save result --%>
<c:if test='${eventId == "save"}'>
  <script type="text/javascript">
    alert("${DialogMessage}");  
  </script>
</c:if>
<form name="frmLang" action="/setting.mod" method="post">
  <input type="hidden" name="screenId" value="LangSetting"/>
  <%-- Default event: save --%>
  <input type="hidden" name="eventId" value="save"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr><td align="right">
      <c:if test="${(not empty user) && (user.isAdmin)}">
       <a href="#" onclick='submitAction("frmLang","LangSetting", "edit")'><img border="0" src="pages/images/editContent.gif" width="16" height="16"></a>
       <a href="#" onclick='submitAction("frmLang","LangSetting", "list")'><img border="0" src="pages/images/ed_list_num.gif" width="16" height="16"></a>
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
         <jsp:include page="/WEB-INF/pages/setting/langlist.jsp"></jsp:include>
        </c:when>
      <c:when test='${(eventId == "edit") || (eventId == "save")}'>
        <textarea name="languages" cols="20">${form.languages}</textarea>
      </c:when>
  
      <c:otherwise>
        <textarea name="languages" cols="20">${form.languages}</textarea>    
      </c:otherwise>
    </c:choose>
  </c:if>

  </td>
  </tr>
</table>
<input type="submit" value="Submit" name="Save">
</form>
