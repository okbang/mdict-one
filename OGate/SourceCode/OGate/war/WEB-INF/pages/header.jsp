<%-- 
/*
 * header.jsp 0.1 Apr 30, 2011
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
<%--
Part: Header
Contains: 
 + Open-Ones's slogan (done)
 + List of sponsors (not)
 + Hot news (not)
 + Change language (done)
 + Logon/Logout (done)
 + Setting (for authorized user) (not)
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@page import="com.google.appengine.api.users.User"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="openones.gate.Cons"%>

<form name="frmHeader" action="header.do" method="post">
  <input type="hidden" name="screenId" value="Header" />
  <input type="hidden" name="eventId" value="" />

  <!-- Header -->
  <table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
      <td width="10%" rowspan="3"><a href="/"><img border="0" src="pages/images/oog_logo.png" width="150" height="75"></a>
      </td>
      <td width="38%" rowspan="2"><H2>${applicationScope.ShareAssociateDevelopIdea}</H2>
      </td>
      <td width="38%" rowspan="2">${applicationScope.sponsorList}</td>
      <td class=uportal-channel-text width="14%" nowrap="nowrap" align=right>
        ${applicationScope.Language}: <select size="1" name="lang" onchange='submitAction("frmHeader","Header", "changeLanguage")'>
            <c:forEach var="langItem" items="${outForm.langList}">
              <c:if test='${langItem.name == lang}'>
                <option id="${langItem.id}" value="${langItem.id}" selected="selected">${langItem.name}</option>
              </c:if>
              <c:if test='${langItem.name != lang}'>
                <option id="${langItem.id}">${langItem.name}</option>
              </c:if>
            </c:forEach>
        </select>
        <%-- Display Setting link --%>
        <c:if test="${(not empty user) && (user.isAdmin)}">
         <a href="#" title="${applicationScope.SettingLinkTitle}" onclick='submitAction("frmHeader","Header", "setting")'><img src="pages/images/setting.gif" border="0" width="20" height="20"></a>
        </c:if>
      </td>
    </tr>
    <tr>
      <td width="14%" align="right" nowrap="nowrap">
         <c:if test="${empty user}">
            <input type="button" value="${applicationScope.Logon}" name="login" onclick='submitAction("frmHeader","Header", "login")'>
         </c:if>
         <c:if test="${not empty user}">
           ${applicationScope.Welcome} ${user.nickname} !
           <input type="button" value="${applicationScope.Logoff}" name="logout" onclick='submitAction("frmHeader","Header", "logout")'>
         </c:if>
      </td>
    </tr>
    <tr>
      <td colspan="3">
        <!-- Hot news --> <marquee>Khóa học "Thực hành phát triển phần mềm"" kết thúc trong tháng 6/2011. Số lượng 30. Đối tượng: Giảng viên CNTT. Điểm phản hồi trung bình 4.3/5 - Tốt.</marquee></td>
    </tr>
  </table>
</form>