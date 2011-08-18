<%@page import="com.fms1.tools.*"%>
<%@page import="com.fms1.infoclass.*,com.fms1.infoclass.group.*, com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<TITLE>workUnitGroupUpdate.jsp</TITLE>
	<LINK rel = "stylesheet" href = "stylesheet/fms.css" type = "text/css">
	<SCRIPT language = "JavaScript" src = "jscript/javaFns.js"></SCRIPT>
	<SCRIPT language = "javascript">
	<%@ include file = "javaFns.jsp"%>
	</SCRIPT>
	<TITLE>workUnitGroupUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad = "loadAdminMenu();" class = "BD">
<P><BR>
</P>
<FORM name="frm" method="POST" action="Fms1Servlet?reqType=<%=Constants.UPDATE_WORK_UNIT_GROUP%>" onsubmit="return doSave();">
<%
	String err=(String)session.getAttribute("addWUerr");
	if(err != null && err != ""){
%>
		<p class = "ERROR"><%=err%></p>
<%
		session.setAttribute("addWUerr","");
	}
	WorkUnitInfo wuInfo = (WorkUnitInfo)session.getAttribute("workUnitInfor");
	Vector orgVector = (Vector)session.getAttribute("WUorgVector");
    GroupInfo groupInfo = (GroupInfo)session.getAttribute("WUgroupInfo");
    
	if(wuInfo!=null){
%>
<DIV align="left">
<TABLE cellspacing="1" class="Table" width="95%">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.workUnitUpdate.UpdateWorkUnitGroup")%></CAPTION>
    <TBODY>
           <INPUT type="hidden" readonly name="hideWorkUnitID" value="<%=wuInfo.workUnitID%>">
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.workUnitUpdate.WorkunitGroupName")%>* </TD>
            <TD class="CellBGR3">
            	<INPUT type="text" size="60" name="txtWorkUnitName" value="<%=wuInfo.workUnitName.trim()%>" maxlength="50">
            </TD>
        </TR>
        <TR>
        	<TD class = "ColumnLabel" width = "240"><%=languageChoose.getMessage("fi.jsp.workUnitUpdate.OperationGroup")%></TD>
        	<TD class = "CellBGR3">
        		<INPUT type = "checkbox" name = "chkOperationGroup" value = 1 <%=((groupInfo.isOperation)?"checked":"")%>>
        	</TD>
        </TR>
        <TR>
            <TD  class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.workUnitUpdate.OrganizationName")%>* </TD>
            <TD class="CellBGR3">
	            <SELECT name="cboOrg" class="COMBO">
	                <%
						for(int i = 0; i < orgVector.size(); i++){
							WorkUnitInfo wuInfoTemp = (WorkUnitInfo)orgVector.get(i);
							if(!wuInfoTemp.workUnitName.equals(wuInfo.workUnitName)){
					%>
	                <OPTION value="<%=wuInfoTemp.workUnitID%>"
	                	<%if (wuInfo.parentWorkUnitID != 0) {
	                		if(wuInfoTemp.workUnitID == wuInfo.parentWorkUnitID) {%>
	                			selected<%
	                		}
	                	}%>
	                ><%=wuInfoTemp.workUnitName%></OPTION>
	                <%}}%>
	                </SELECT>
             </TD>
        </TR>
    </TBODY>
</TABLE></DIV>
<BR>
<%
	}
%><p align="left">
<INPUT type="button" name="OK" value="<%=languageChoose.getMessage("fi.jsp.workUnitUpdate.OK")%>" onclick="doSave()" class="BUTTON"> <INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.workUnitUpdate.Cancel")%>" onclick="doBack()" class="BUTTON"></p>
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
		if (trim(frm.txtWorkUnitName.value)=="") {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitUpdate.YouMustInputGroupName")%>");
  			frm.txtWorkUnitName.focus();
  			return false;
  		}
  		frm.submit();
		return true;
	}	
	function doBack(){
		doIt(<%=Constants.GET_WORK_UNIT_LIST%>);
	}
	frm.txtWorkUnitName.focus();
</SCRIPT>
</BODY>
</HTML>