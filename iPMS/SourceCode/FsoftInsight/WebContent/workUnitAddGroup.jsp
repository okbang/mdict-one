<%@page import="com.fms1.tools.*"%>
<%@page import="com.fms1.infoclass.*, com.fms1.infoclass.group.*, com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">	
	<TITLE>workUnitAddGroup.jsp</TITLE>
	<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
	<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language="javascript"><%@ include file="javaFns.jsp"%></SCRIPT>
	<TITLE>workUnitAddGroup.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadAdminMenu();">
<BR>
<FORM name="frmAddGroup" method="POST" onsubmit="return doSave();">
<%
	String err=(String)session.getAttribute("addWUerr");
	if(err != null && !"".equals(err)) {
%>
		<p class="ERROR"><%=err%></p>
<%
		session.setAttribute("addWUerr","");
	}
	Vector orgVector=(Vector)session.getAttribute("WUorgVector");
	// store workunit which user input
	GroupInfo groupInfo = new GroupInfo();
	if (request.getAttribute("WorkUnitGroupInfo") != null){
		groupInfo = (GroupInfo) request.getAttribute("WorkUnitGroupInfo");
	}
	else{
		groupInfo.name = "";
		groupInfo.isOperation = false;
	}

%>
<DIV align="left">
<TABLE cellspacing="1" class="Table" width="95%">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.AddnewWorkUnitGroup")%></CAPTION>
    <TBODY>
        <TR>
            <TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.GroupName")%>* </TD>
            <TD class="CellBGR3"><INPUT type="text" maxlength="50" name="txtworkUnitName" value="<%=ConvertString.toHtml(groupInfo.name)%>" size="60"></TD>
        </TR>
        <TR>
        	<TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.OperationGroup")%></TD>
			<TD class="CellBGR3"><INPUT type="checkbox" name="chkOperationGroup" value= 1 <%=((groupInfo.isOperation)?"checked":"")%>></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.OrganizationName")%>* </TD>
            <TD class="CellBGR3">
	            <SELECT name="cboOrg" class="COMBO">
	                <%
						for(int i = 0; i < orgVector.size(); i++) {
							WorkUnitInfo wuInfoTemp = (WorkUnitInfo)orgVector.get(i);
					%>
	                <OPTION value="<%=wuInfoTemp.workUnitID%>"><%=wuInfoTemp.workUnitName%></OPTION>
	                <%
	                	}
	               	 %>
	            </SELECT>
            </TD>
        </TR>
    </TBODY>
</TABLE>
</DIV>
<BR>
<p align="left">
	<INPUT type="button"  name="OK" value="<%=languageChoose.getMessage("fi.jsp.workUnitAdd.OK")%>" onclick="doSave()" class="BUTTON">&nbsp;
	<INPUT type="button"  name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.workUnitAdd.Cancel")%>" onclick="doBack()" class="BUTTON">
</p>
</FORM>
<SCRIPT language="javascript">
<%
		if (request.getAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE) != null){
%>
			alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.WorkUnitNameAlreadyExist")%>");
<%
			request.removeAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE);
	}
%>

	function doSave(){
  		if (trim(frmAddGroup.txtworkUnitName.value)=="") {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.YouMustInputGroupName")%>");
  			frmAddGroup.txtworkUnitName.focus();
  			return false;
  		}
  		frmAddGroup.action="Fms1Servlet?reqType=<%=Constants.ADDNEW_WORK_UNIT_GROUP%>";
  		frmAddGroup.submit();
  		return true;
	}
	function doBack(){
		doIt(<%=Constants.GET_WORK_UNIT_LIST%>);
	}
	frmAddGroup.txtworkUnitName.focus();
</SCRIPT>
</BODY>
</HTML>