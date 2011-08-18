<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>requirementStatus.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	Vector requirementList = (Vector)request.getAttribute("requirementList");
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.requirementStatus.Requirements")%> </P>
<BR>
<FORM name="frm" action="Fms1Servlet" method="post">
<TABLE class="Table" width="95%" cellspacing="1">
<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.requirementStatus.Updatestatusofrequirements")%> </CAPTION>
    <COL span="1" width="24">
        <TR class="ColumnLabel">
            <TD style="text-align: center">#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementStatus.Requirement")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementStatus.Committed")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementStatus.Designed")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementStatus.Coded")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementStatus.Tested")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementStatus.Deployed")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementStatus.Accepted")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementStatus.Cancelled")%></TD>
        </TR>
        <%
        int j;
        j = 0;
        if (requirementList != null)                
        for (int i = 0; i < requirementList.size(); i++) {
        	String className;
        	RequirementInfo reqInfo = (RequirementInfo)requirementList.elementAt(i);
			className = (i%2 == 0)?"CellBGRnews":"CellBGR3";
        %>
         <TR class="<%=className%>">
            <TD style="text-align: center"><%=i + 1%></TD>
            <TD class="<%=className%>"><INPUT size="65" maxlength="200" name="requirementName" value="<%=ConvertString.toHtml(reqInfo.name)%>"></TD>            
			<TD style="text-align: center"><INPUT type="checkbox" name="Committed" value="<%=j * 7 + 1%>" <%=((reqInfo.committedDate != null) ? "checked" : "unchecked")%> onclick="onUpdate(this)"></TD>
			<TD style="text-align: center"><INPUT type="checkbox" name="Designed" value="<%=j * 7 + 2%>" <%=((reqInfo.designedDate != null) ? "checked" : "unchecked")%> onclick="onUpdate(this)"></TD>
			<TD style="text-align: center"><INPUT type="checkbox" name="Coded" value="<%=j * 7 + 3%>" <%=((reqInfo.codedDate != null) ? "checked" : "unchecked")%> onclick="onUpdate(this)"></TD>
			<TD style="text-align: center"><INPUT type="checkbox" name="Tested" value="<%=j * 7 + 4%>" <%=((reqInfo.testedDate != null) ? "checked" : "unchecked")%> onclick="onUpdate(this)"></TD>
			<TD style="text-align: center"><INPUT type="checkbox" name="Deployed" value="<%=j * 7 + 5%>" <%=((reqInfo.deployedDate != null) ? "checked" : "unchecked")%> onclick="onUpdate(this)"></TD>
			<TD style="text-align: center"><INPUT type="checkbox" name="Accepted" value="<%=j * 7 + 6%>" <%=((reqInfo.acceptedDate != null) ? "checked" : "unchecked")%> onclick="onUpdate(this)"></TD>
			<TD style="text-align: center"><INPUT type="checkbox" name="Cancelled" value="<%=j * 7 + 7%>" <%=((reqInfo.cancelledDate != null) ? "checked" : "unchecked")%> onclick="onUpdate(this)"></TD>
			
        </TR>
        <INPUT type="hidden" name="requirementID" value="<%=reqInfo.requirementID%>">
		<%
        	j ++;
		}
		%>
</TABLE>
<INPUT type="hidden" name="reqType" value="<%=Constants.REQUIREMENT_UPDATE_STATUS%>">
<P>
<INPUT type="button" class="BUTTON" name="UpdateStatus" value=" <%=languageChoose.getMessage("fi.jsp.requirementStatus.OK")%> " onclick="onOK()">
<INPUT type="button" class="BUTTON" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.requirementStatus.Cancel")%> " onclick="onCancel()">
</P>
</FORM>
<SCRIPT language="javascript">
	function onOK() {
		frm.reqType.value = <%=Constants.REQUIREMENT_UPDATE_STATUS%>;
		frm.submit();	
	}
	function onCancel() {
		doIt(<%=Constants.REQUIREMENT_LIST_INIT%>);
	}
	
	function onUpdate(obj) {
		if (obj.value < 10000) {
			obj.value = obj.value * 10000;
		}
	}
</SCRIPT>
</BODY>
</HTML>