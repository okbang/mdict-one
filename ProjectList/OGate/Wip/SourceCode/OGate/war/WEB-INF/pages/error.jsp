<!-- 
/*
 * error.jsp 0.1 Apr 16, 2011
 * 
 * Copyright (c) 2011, Open-Ones Groups
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
 -->
 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src='pages/scripts/common.js'></script>
<title>Message</title>
</head>
<%
if (exception != null)
{
    exception.printStackTrace();
%>

<%-- Error message that user will see when an exception is thrown --%>
<p><strong>An error has occurred.  Check the log for details.</strong>

<% } else { %>
<%-- Display text when page is loaded without an exception --%>
<p>You are looking at <code>error.jsp</code>.  If an exception had been thrown
in one of the <code>.jsp</code> files, this page will be loaded and the stack trace
will be logged.

<%
}
%>
<body topmargin="0">
<table border="0" width="40%" id="table1">
    <tr>
        <td>
        <p align="center">Có lỗi xảy ra trong hệ thống. Hãy liên lạc với tác giả
        <a href="mailto:ThachLN@fsoft.com.vn">ThachLN@fsoft.com.vn</a>
        </td>
    </tr>
    <tr>
        <td>
        <p align="center">
        <a href="/">Trở về</a>
        </td>
    </tr>
</table>
</body>

</html>