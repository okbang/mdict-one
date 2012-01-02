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
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<portlet:defineObjects/>

<script type="text/javascript" src='/${requestScope.contextPath}/scripts/common.js' ></script>
<form name="<portlet:namespace/>Lookup" action="<portlet:actionURL/>" method="POST">
  <input type="hidden" name="screenId" value="Lookup"/>
  <input type="hidden" name="eventId" value="translate"/>
  
  Tự điền <select name="selectedDict">
    <c:forEach var="dictInfo" items="${sessionScope.dictInfos}">
        <%-- Use name of dict as code --%>
        <c:choose>
          <c:when test="${dictInfo.name == requestScope.formBean.selectedDict}">
            <option value="${dictInfo.name}" selected="selected">${dictInfo.name}</option>
          </c:when>
          <c:otherwise><option value="${dictInfo.name}">${dictInfo.name}</option>
          </c:otherwise>
        </c:choose>
        
    </c:forEach>
  </select><br/>
  <input type="text" name="word" value="${requestScope.formBean.word}"/>
  <input type="button" name="translate" value="Translate" onclick='submitAction("<portlet:namespace/>Lookup","translate");'/>
  <br/>
  <%-- Display meanings --%>
  <c:forEach var="dictInfo" items="${requestScope.dictMeanings}">
        ${dictInfo.meaning}<br />
  </c:forEach>

</form>