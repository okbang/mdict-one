<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<input type="hidden" name="ogate_debug" value="ScreenId=${screenId}; Main screen: ${MainScreen}; eventId=${eventId}; tabKey=${tabKey}; tabId=${tabId}; menuId=${menuId}"/>

<c:if test='${empty MainScreen}'>
  Welcome to Open-Ones Group
</c:if>

<c:choose>
  <c:when test="${not empty tabId}">
    <jsp:include page="/mainbody.mod" flush="true">
      <jsp:param name="screenId" value="${screenId}" />
      <jsp:param name="eventId" value="${eventId}" />
      <jsp:param name="tabId" value="${tabId}" />
    </jsp:include>

  </c:when>
  <c:otherwise>
    <c:if test='${MainScreen == "service"}'>
      <jsp:include page="/service.mod" flush="true">
        <jsp:param name="screenId" value="${screenId}" />
        <jsp:param name="eventId" value="${eventId}" />
      </jsp:include>
    </c:if>

    <c:if test='${MainScreen == "member"}'>
      <jsp:include page="/member.mod" flush="true">
        <jsp:param name="screenId" value="${screenId}" />
        <jsp:param name="eventId" value="${eventId}" />
      </jsp:include>
    </c:if>

    <c:if test='${MainScreen == "EditModuleIntro"}'>
      <jsp:include page="/moduleintroeditor.mod" flush="true">
        <jsp:param name="screenId" value="${screenId}" />
        <jsp:param name="eventId" value="${eventId}" />
      </jsp:include>
    </c:if>

    <c:if test='${(not empty menuId) && (eventId == "edit")}'>
      <jsp:include page="/WEB-INF/pages/moduleintroeditor.jsp" flush="true"/>
    </c:if>
    
    <c:if test='${(not empty menuId) && (eventId == "save")}'>
      <jsp:include page="/WEB-INF/pages/moduleintroeditor.jsp" flush="true"/>
    </c:if>
    
    <c:if test='${(not empty menuId) && ((eventId == "list") || (eventId == "delete"))}'>
      <jsp:include page="/WEB-INF/pages/moduleintroeditor.jsp" flush="true"/>
    </c:if>

    <c:if test='${MainScreen == "TabSetting"}'>
      <jsp:include page="/setting.mod" flush="true">
        <jsp:param name="screenId" value="TabSetting" />
        <jsp:param name="eventId" value="${eventId}" />
      </jsp:include>
    </c:if>
    
    <c:if test='${MainScreen == "LangSetting"}'>
      <jsp:include page="/setting.mod" flush="true">
        <jsp:param name="screenId" value="LangSetting" />
        <jsp:param name="eventId" value="${eventId}" />
      </jsp:include>
    </c:if>




  </c:otherwise>
</c:choose>

