<%@page import="com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML> 
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>darPlanDetails.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
%>
<%
	int right = Security.securiPage("Project reports", request, response);
	Vector dpVt = (Vector)session.getAttribute("dar");
	String vtIDstr = request.getParameter("vtID");
	int vtID = Integer.parseInt(vtIDstr);
	DARPlanInfo darPlanInfo = (DARPlanInfo) dpVt.get(vtID);
	session.setAttribute("doer", darPlanInfo.doer );
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE">DAR Plan</p>
<FORM method="POST" action="darPlanUpdate.jsp?vtID=<%=vtID%>" name="frm">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.darPlanDetails.Caption ")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.darPlanDetails.Item")%></TD>
            <TD class="CellBGRnews"><%=darPlanInfo.darItem%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.darPlanDetails.Doer")%></TD>
            <TD class="CellBGRnews"><%=darPlanInfo.doer%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.darPlanDetails.Targetdate")%></TD>
            <TD class="CellBGRnews"><%=CommonTools.dateFormat(darPlanInfo.planDate)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.darPlanDetails.Actualdate")%></TD>
            <TD class="CellBGRnews"><%=(CommonTools.dateFormat(darPlanInfo.actualDate)=="N/A")?"":CommonTools.dateFormat(darPlanInfo.actualDate)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.darPlanDetails.Cause")%></TD>
            <TD class="CellBGRnews"><%=darPlanInfo.darCause%></TD>
        </TR>        
    </TBODY>
</TABLE>
<P>
<%if(right == 3 && !isArchive){%>
<INPUT type="hidden" name="vtID" value="<%=vtIDstr%>">
<INPUT type="hidden" name="darPlanID" value="<%=darPlanInfo.darPlanID%>">
<INPUT type="submit" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.darPlanDetails.Update")%>" class="BUTTON">
<INPUT type="button" class="BUTTON" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.darPlanDetails.Delete")%>" onclick=doDelete();>
<%}%>	 	

<INPUT type="button" class="BUTTON" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.darPlanDetails.Back")%>" 
	onclick="jumpURL('qualityObjective.jsp#darplan');" >
</P>
</FORM>
<SCRIPT language="javascript">
	function doDelete()
	{  	
  		if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.darPlanDetails.Areyousuretodelete")%>")){   		
  			return;
  		}
  		frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_DARPLAN%>#darplan";
  		frm.submit();
	}
</SCRIPT>
</BODY>
</HTML>