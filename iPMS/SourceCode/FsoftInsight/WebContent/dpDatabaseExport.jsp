<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>
<%@page import="com.fms1.common.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>dpDatabaseExport.jsp</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<STYLE type="text/css">
.header {
    FONT-WEIGHT: bold;
    BACKGROUND-COLOR: #C0C0C0;
}
.footer {
    FONT-WEIGHT: bold;
}
.Title {
	font-weight: bold;
	font-size: 14pt;
	margin-left: 0px;
	margin-top: 20px
}

</STYLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	response.addHeader("Content-Disposition", "attachment;filename=dpDatabase.xls");
%>
<BODY>

<%
	Vector dpVt = (Vector) session.getAttribute("vtDefectLog");
	Vector cdVt=(Vector)session.getAttribute("vtCommDefect");
	
	Vector userList = (Vector) session.getAttribute("userList");
%>

<P class="Title"> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.DPDatabase")%> <P>

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <CAPTION align="left"><B> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.DefectPreventionlog")%> </B></CAPTION>
    <TBODY>
        <TR class="header">
            <TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Group")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Project")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.DPCode")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.DPTaskAction")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Process")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Commdef.Code")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Createdate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Assignto")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Targetdate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Status")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Closeddate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Isintime")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.ResultBenefit")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Note")%> </TD>
        </TR>
        <%
        	for(int i=0;i<dpVt.size();i++){
  				DPLogInfo info = (DPLogInfo) dpVt.get(i);
        %>
        <TR>
            <TD align="center"><%=i+1%></TD>
            <TD><%=info.groupName%></TD>
            <TD><%=(info.projectCode == null) ? "N/A" : info.projectCode%></TD>
            <TD><%=info.dpcode%></TD>
            <TD><%=info.dpaction%></TD>
            <TD>
            <%
            	switch (info.processID) {
            	case 0: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Prevention")%> <%break;
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Requirement")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Design")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Coding")%> <%break;
            	case 4: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Test")%> <%break;
            	case 5: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Training")%> <%break;
            	}
            %>
            </TD>
            <TD><%=(info.commonDefCode == null)? "N/A" : info.commonDefCode%></TD>
            <TD><%=CommonTools.dateFormat(info.createDate)%></TD>
            <TD>
			<%
            	for (int j = 0; j < userList.size(); j++) {
	            	UserInfo userInfo = (UserInfo)userList.elementAt(j);
                	if (userInfo.developerID == info.devID) {
                	%>
                	<%=userInfo.account%>
			<%
						break;
					}
				}
			%>
            </TD>
            <TD><%=CommonTools.dateFormat(info.targetDate)%></TD>
            <TD>
            <%
            	switch (info.dpStatus) {
            	case 0: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Open")%> <%break;
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Closed")%> <%break;
            	}
            %>
            </TD>
            <TD><%=CommonTools.dateFormat(info.closedDate)%></TD>
            <TD>
            <%
           		String isOnTime = "N/A";
				if (info.targetDate != null) {
					if (info.closedDate != null) {
						if (info.closedDate.compareTo(info.targetDate) > 0)
							isOnTime = languageChoose.getMessage("fi.jsp.dpDatabaseExport.No");
						else
							isOnTime = languageChoose.getMessage("fi.jsp.dpDatabaseExport.Yes");
					}
				}
            %>
			<%=isOnTime%>
            </TD>
            <TD><%=(info.dpBenefit == null)? "N/A" : info.dpBenefit%></TD>
            <TD><%=(info.dpNote == null)? "N/A" : info.dpNote%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>

<BR>

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <CAPTION align="left"><B> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Commondefects")%> </B></CAPTION>
    <TBODY>
        <TR class="header">
            <TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Group1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Project1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Commondefect")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Commdef.Code1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.DPCode1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Defecttype")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.RootCause")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.CauseCategory")%> </TD>
        </TR>
        <%
        	for(int i=0;i<cdVt.size();i++){
  				CommDefInfo info = (CommDefInfo) cdVt.get(i);
        %>
        <TR>
            <TD align="center"><%=i+1%></TD>
            <TD><%=info.groupName%></TD>
            <TD><%=(info.projectCode == null) ? "N/A": info.projectCode%></TD>
            <TD><%=info.commdef%></TD>
            <TD><%=info.commonDefCode%></TD>
            <TD><%=(info.dpcode == null)? "N/A" : info.dpcode%></TD>
            <TD>
            <%
            	switch (info.defecttype) {
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.01FunctionalityOther")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.02UserInterface")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.03Performance")%> <%break;
            	case 4: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.04Designissue")%> <%break;
            	case 5: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.05Codingstandard")%> <%break;
            	case 6: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.06Document")%> <%break;
            	case 7: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.07DataDatabaseintegrity")%> <%break;
            	case 8: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.08SecurityAccessControl")%> <%break;
            	case 9: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.09Portability")%> <%break;
            	case 10: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.10Other")%> <%break;
            	case 11: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.11Tools")%> <%break;
            	case 12: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.011Reqmisunderstanding")%> <%break;
            	case 13: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.012Featuremissing")%> <%break;
            	case 14: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.013Codinglogic")%> <%break;
            	case 15: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.014Businesslogic")%> <%break;
            	}
            %>
            </TD>
            <TD><%=(info.rootcause == null)? "N/A": info.rootcause%></TD>
            <TD>
            <%
            	switch (info.causecate) {
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Training1")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Communication")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Oversight")%> <%break;
            	case 4: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Understanding")%> <%break;
            	case 5: %> <%=languageChoose.getMessage("fi.jsp.dpDatabaseExport.Other")%> <%break;
            	}
            %>
            </TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>

</BODY>
</HTML>
