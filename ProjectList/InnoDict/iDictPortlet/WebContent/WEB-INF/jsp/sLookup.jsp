<%--
 * Licensed to Open-Ones Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Open-Ones Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="javax.portlet.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<portlet:defineObjects/>

<portlet:actionURL var="formAction">
  <portlet:param name="action" value="translate" />
</portlet:actionURL>

<form:form commandName="lookupForm" method="post" action="${formAction}">
  Tự điển <form:select path="selectedDict" items="${lookupForm.dictNames}"/>
  <br/>
 
  <form:input path="word"/>
  <input type="submit" name="translate" value="Translate"/>
  <br/>
  <%-- Display meanings --%>
  <form:label path="dictMeanings"></form:label>
  <br/>
  
  <c:forEach var="dictInfo" items="${lookupForm.dictMeanings}">
        ${dictInfo.meaning}<br />
  </c:forEach>

</form:form>