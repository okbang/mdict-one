<%@page import="openones.gate.util.GUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%--set some common properties (new version)--%>
<c:set var="skin" value=""/>
<c:set var="active_bg" value="dark"/>
<c:set var="inactive_bg" value="med"/>
<c:set var="before_selected_tab" value="before_active_tab"/>
<c:set var="after_selected_tab" value="after_active_tab"/>
<c:set var="after_inactive_tab" value="after_inactive_tab"/>
<%-- Default: Set default tab is the left most one --%>
<c:set var="selectedTabIndex" value="${minTabOrderNo}"/>

<c:forEach var="tab" items="${moduleTabs}">
    <c:if test="${MainScreen == tab.id}">
        <c:set var="selectedTabIndex" value="${tab.orderNo}"></c:set>
    </c:if>
</c:forEach>

<form name="frmNavigation" action="main.do">
      <input type="hidden" name="screenId" value="Navigation" />
      <input type="hidden" name="eventId" value="" />
      <input type="hidden" name="tabId" value="" />
<%-- New version: dynamic tabs are loaded from persistence layer --%>
<table border="0" width="100%" cellspacing="0" cellpadding="0" name="newNavigation" >
    <tr height="20" align="left" valign="top">
        <td width="99%" colspan="3" class=uportal-background-dark noWrap
        align=left valign="top" height="1">
            <table border=0 cellSpacing=0 cellPadding=0 height="1">
                <tr>
                    <%-- tab: instance of ModuleDTO --%>
                    <c:forEach var="tab2" items="${moduleTabs}">
                        <c:choose>
                            <%--If selected tab is the left most one(first tab) --%>
                            <c:when test="${selectedTabIndex==tab.orderNo}">
                                <%--Display first tab by after_selected_tab  --%>
                                <%--code--%>
                                <td class=uportal-background-shadow noWrap align=left height="1">
                                <table border=0 cellSpacing=0 cellPadding=0>
                                <tr>
                                <td class=uportal-background-dark noWrap><img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                <td class=uportal-background-med rowSpan=4 width=22><img alt="" src="pages/images/${after_selected_tab}.gif" width=22 height=23></td>
                                </tr>
                                <tr>
                                <td class=uportal-background-dark height=20 vAlign=center noWrap>
                                <img alt="" src="pages/images/transparent.gif" width=10 height=10>
                                <a class=uportal-tab-text-selected href="#" 
                                onclick='submitNav("frmNavigation","Navigation", "clickTab", "${tab2.id}")'>${tab2.name}</a>
                                <img alt="" src="pages/images/transparent.gif" width=10 height=10></td>
                                </tr>
                                <tr>
                                <td class=uportal-background-dark noWrap><img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                </tr>
                                <tr>
                                <td class=uportal-background-dark noWrap><img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                </tr>
                                </table>
                                </td>
                                <%--/code --%>

                            </c:when> 
                            <c:otherwise>
                                <c:choose>
                                    <%--if the next tab is selected tab--%>              
                                    <c:when test="${selectedTabIndex == tab2.orderNo + 1}">
                                    <%--display tab by before_selected_tab--%>
                                        <%--code--%>
                                        <td class=uportal-background-shadow noWrap align=left height="1">
                                <table border=0 cellSpacing=0 cellPadding=0>
                                <tr>
                                <td class=uportal-background-highlight noWrap>
                                <img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                <td class=uportal-background-med rowSpan=4 width=22><img alt="" src="pages/images/${before_selected_tab}.gif" width=22 height=23></td>
                                </tr>
                                <tr>
                                <td class=uportal-background-med height=20 vAlign=center noWrap>
                                <img alt="" src="pages/images/transparent.gif" width=10 height=10> 
                                <a class=uportal-tab-text href="#" onclick='submitNav("frmNavigation","Navigation", "clickTab", "${tab2.id}")'>${tab2.name}</a>
                                <img alt="" src="pages/images/transparent.gif" width=10 height=10></td>
                                </tr>
                                <tr>
                                <td class=uportal-background-shadow noWrap><img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                </tr>
                                <tr>
                                <td class=uportal-background-highlight noWrap><img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                </tr>
                                </table>
                                </td>
                                        <%--/code--%>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <%--if this tab is selected tab--%>
                                            <c:when test="${selectedTabIndex == tab2.orderNo}">
                                            <%--display tab by after_selected_tab --%>
                                                <%--code --%>
                                                <td class=uportal-background-shadow noWrap align=left height="1">
                                <table border=0 cellSpacing=0 cellPadding=0>
                                <tr>
                                <td class=uportal-background-dark noWrap><img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                <td class=uportal-background-med rowSpan=4 width=22><img alt="" src="pages/images/${after_selected_tab}.gif" width=22 height=23>
                                </td>
                                </tr>
                                <tr>
                                <td class=uportal-background-dark height=20 vAlign=center noWrap>
                                <img alt="" src="pages/images/transparent.gif" width=10 height=10> 
                                <a class=uportal-tab-text-selected href="#" onclick='submitNav("frmNavigation","Navigation", "clickTab", "${tab2.id}")'>${tab2.name}</a>
                                <img alt="" src="pages/images/transparent.gif" width=10 height=10></td>
                                </tr>
                                <tr>
                                <td class=uportal-background-dark noWrap><img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                </tr>
                                <tr>
                                <td class=uportal-background-dark noWrap><img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                </tr>
                                </table>
                                </td>
                                                <%--/code --%>    
                                            </c:when>
                                            <c:otherwise>
                                            <%--if this tab is inselected tab --%>
                                            <%--display tab by after_inactive_tab--%>
                                                <%--code --%>
                                                <td class=uportal-background-shadow noWrap align=left height="1">
                                <table border=0 cellSpacing=0 cellPadding=0>
                                <tr>
                                <td class=uportal-background-highlight noWrap><%--tren cung --%>
                                <img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                <td class=uportal-background-med rowSpan=4 width=22>
                                <img alt="" src="pages/images/${after_inactive_tab}.gif" width=22 height=23>
                                </td>
                                </tr>
                                <tr>
                                <td class=uportal-background-med height=20 vAlign=center noWrap>
                                <img alt="" src="pages/images/transparent.gif" width=10 height=10> 
                                <a class=uportal-tab-text href="#" 
                                onclick='submitNav("frmNavigation","Navigation", "clickTab", "${tab2.id}")'>${tab2.name}</a>
                                <img alt="" src="pages/images/transparent.gif" width=10 height=10></td>
                                </tr>
                                <tr>
                                <td class=uportal-background-shadow noWrap><img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                </tr>
                                <tr>
                                <td class=uportal-background-highlight noWrap><%--duoi cung --%>
                                <img alt="" src="pages/images/transparent.gif" width=1 height=1></td>
                                </tr>
                                </table>
                                </td>
               
                                                <%--/code --%>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>  
                                                      
                    </c:forEach>  
              <TD class=uportal-background-med width="100%" noWrap align="right">
              <TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-channel-text height=20 vAlign=center
                    align="right" noWrap>${applicationScope.NumberOfMember}: <%= GUtil.getNumOfMemeber("http://groups.google.com/group/open-ones") %></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-shadow noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
                <TR>
                  <TD class=uportal-background-highlight noWrap><IMG
                    alt="" src="pages/images/transparent.gif" width=1
                    height=1></TD>
                </TR>
              </TABLE></TD>                                                                                                     
                </tr> 
            </table>
        </td>
    </tr>
</table>
</form>