<%-- 
/*
 * footer.jsp 0.1 May 30, 2011
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
<%@page import="openones.gae.session.SessionCounter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<table border="0" width="100%" cellspacing="0" cellpadding="0" background="pages/images/bgfooter.gif" title="The web site is deployed from source code at https://open-ones.googlecode.com/svn/trunk/OGate/SourceCode/OGate,revision 321.">
    <tr>
      <td width="78%" class=uportal-channel-text>
        <p align="center">${applicationScope.Copyright}
      </td>
      <td width="12%" class=uportal-channel-text>${applicationScope.NumberOfAccess}: <%= SessionCounter.getNmHits() %>
       <br> ${applicationScope.NumberOfLogin}: ${nmLogonUser}</td>
    </tr>
</table>