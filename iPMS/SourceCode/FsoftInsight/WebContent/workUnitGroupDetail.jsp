<%@page import="com.fms1.tools.*"%>
<%@page import="com.fms1.infoclass.*,com.fms1.infoclass.group.* ,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<TITLE>workUnitGroupDetail.jsp</TITLE>
	<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
	<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
	</SCRIPT>
	<TITLE>workUnitGroupDetail.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadAdminMenu()" class="BD">
<FORM name="frm" method="POST">
<%
	WorkUnitInfo wuInfo = (WorkUnitInfo)session.getAttribute("workUnitInfor");
   	GroupInfo groupInfo = (GroupInfo)session.getAttribute("WUgroupInfo");
	
	session.setAttribute("workUnitParentName","");
	if (wuInfo != null){
%>
<DIV align="left">
<TABLE cellspacing="1" class="Table" width="95%">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.WorkUnitGroupDetails")%></CAPTION>
    <TBODY>
		<INPUT size="20" type="hidden" name="hideWorkUnitID" value="<%=wuInfo.workUnitID%>">
		<INPUT size="20" type="hidden" name="txtWorkUnitName" value="<%=wuInfo.workUnitName%>">
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.WorkUnitGroupName")%></TD>
            <TD class="CellBGR3"><%=ConvertString.toHtml(wuInfo.workUnitName)%></TD>
        </TR>
        <TR>
        	<TD class = "ColumnLabel" width = "240"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.OperationGroup")%></TD>
			<TD class="CellBGR3"><%=(groupInfo.isOperation? languageChoose.getMessage("fi.jsp.workUnitDetail.OperationYes"): languageChoose.getMessage("fi.jsp.workUnitDetail.OperationNo"))%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.OrganizationName")%></TD>
            <TD class="CellBGR3"><%=ConvertString.toHtml(groupInfo.getParentName())%></TD>
        </TR>
    </TBODY>
</TABLE>
</DIV>
<BR>
<%
	}
%>
<%if (!wuInfo.protect){%>
<P class="ERROR">To update Organization or Group, please use correlative functions in <a href="/rms2" target="/rms2">RMS</a></P>
<INPUT type="button" name="Update" value="<%=languageChoose.getMessage("fi.jsp.workUnitDetail.Update")%>" onclick="doUpdate()" class="BUTTON">
<INPUT type="button" name="Delete" value="<%=languageChoose.getMessage("fi.jsp.workUnitDetail.Delete")%>"  onclick="doDelete()" class="BUTTON"> 
<%}%>
<INPUT type="button" name="Back" value="<%=languageChoose.getMessage("fi.jsp.workUnitDetail.Back")%>"  onclick="doBack()" class="BUTTON">
</FORM>
<SCRIPT language="javascript">
	function doBack(){
		doIt(<%=Constants.GET_WORK_UNIT_LIST%>);
	}
	function doUpdate(){
		window.open('/rms2');
	}
<%
	if (wuInfo.getCheckDelete()){
%>
		function doDelete(){
  			if(window.confirm("<%=languageChoose.getMessage("fi.jsp.workUnitDetail.AreYouSuretoDeleteThisWorkunit")%>")) {
  				frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_WORK_UNIT_GROUP%>";
	  			frm.submit();
  			}
  			else {
  				return;
	  		}
		}
<%
	}
	else {
%>
		function doDelete(){
			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitDetail.YouMustRemoveAllProjectAssignmentBeforeDelete")%>");
			return;
		}
<%
	}
%>
</SCRIPT>
</BODY>
</HTML>