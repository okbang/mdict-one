<%@page import="com.fms1.tools.*"%> 
<%@ page contentType="application/vnd.ms-excel" %><%@page import="com.fms1.common.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,java.util.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>DPActions.jsp</TITLE>
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
	response.addHeader("Content-Disposition", "attachment;filename=DPAction.xls");
%>
<BODY>

<%
	Vector dpVt=(Vector)session.getAttribute("vtDefectLog");
	String projectCode=(String)session.getAttribute("projCode");
	Vector userList = (Vector)session.getAttribute("userList");
	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	String groupName = (String) session.getAttribute("groupName");
	
	if (workUnitID == Parameters.SQA_WU)
	{
%>
<P class="Title"> <%=languageChoose.getMessage("fi.jsp.DPActions.DPCDPLog")%> <P>
<%
	}else{
%>
<P class="Title"><P class="Title"><%=languageChoose.getMessage("fi.jsp.DPActions.Project")+ ":" + projectCode + " - " + groupName%><P><P>
<%
	}
%>

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <CAPTION align="left"><B> <%=languageChoose.getMessage("fi.jsp.DPActions.DefectPreventionlog")%> </B></CAPTION>
    <TBODY>
        <TR class="header">
            <TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.DPCode")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.DPTaskAction")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.Process")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.Commdef.Code")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.Createdate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.Assignto")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.Targetdate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.Status")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.Closeddate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.Note")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.Isintime")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.ResultBenefit")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.DPActions.Note1")%> </TD>
        </TR>
        <%
        	for(int i=0;i<dpVt.size();i++){
  				DPLogInfo info = (DPLogInfo) dpVt.get(i);
        %>
        <TR>
            <TD align="center"><%=i+1%></TD>
            <TD><%=info.dpcode%></TD>
            <TD><%=info.dpaction%></TD>
            <TD>
            <%
            	switch (info.processID) {
            	case 0: %> <%=languageChoose.getMessage("fi.jsp.DPActions.Prevention")%> <%break;
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.DPActions.Requirement")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.DPActions.Design")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.DPActions.Coding")%> <%break;
            	case 4: %> <%=languageChoose.getMessage("fi.jsp.DPActions.Test")%> <%break;
            	case 5: %> <%=languageChoose.getMessage("fi.jsp.DPActions.Training")%> <%break;
            	}
            %>
            </TD>
            <TD><%=(info.commonDefCode == null)? "N/A": info.commonDefCode%></TD>
            <TD><%=CommonTools.dateFormat(info.createDate)%></TD>
            <TD>
			<%
			if (workUnitID != Parameters.SQA_WU)
			{
            	for (int j = 0; j < userList.size(); j++) {
	            	AssignmentInfo assInfo = (AssignmentInfo)userList.elementAt(j);
                	if (assInfo.devID == info.devID) {
                	%>
                	<%=assInfo.devName%>
			<%
						break;
					}
				}
			}else{
            	for (int j = 0; j < userList.size(); j++) {
	            	UserInfo userInfo = (UserInfo)userList.elementAt(j);
                	if (userInfo.developerID == info.devID) {
                	%>
                	<%=userInfo.account%>
			<%
						break;
					}
				}
			}
			%>
            </TD>
            <TD><%=CommonTools.dateFormat(info.targetDate)%></TD>
            <TD>
            <%
            	switch (info.dpStatus) {
            	case 0: %> <%=languageChoose.getMessage("fi.jsp.DPActions.Open")%> <%break;
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.DPActions.Closed")%> <%break;
            	}
            %>
            </TD>
            <TD><%=CommonTools.dateFormat(info.closedDate)%></TD>
            <TD><%=(info.dpNote == null)? "": info.dpNote%></TD>
            <TD>
            <%
           		String isOnTime = "N/A";
				if (info.targetDate != null) {
					if (info.closedDate != null) {
						if (info.closedDate.compareTo(info.targetDate) > 0)
							isOnTime = languageChoose.getMessage("fi.jsp.DPActions.No");
						else
							isOnTime = languageChoose.getMessage("fi.jsp.DPActions.Yes");
					}
				}
            %>
			<%=isOnTime%>
            </TD>
            <TD><%=(info.dpBenefit == null)? "N/A": info.dpBenefit%></TD>
            <TD><%=(info.dpNote == null)? "N/A": info.dpNote%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
</BODY>
</HTML>
