<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<LINK rel="stylesheet" type="text/css" href="stylesheet/fms.css">
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<SCRIPT language="javascript" src='jscript/date_format.js'></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>plComReportUpdate.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	boolean isOver = false;
	int comDisplayed;
	String rowStyle;	
	
	ComReportInfo comInfo = null;	
	
	Vector vCom = (Vector) session.getAttribute("ComReportBatchUpdateList");	
	
	Vector vErrCom = (Vector) request.getAttribute("ErrComReportList");
	if (vErrCom != null) {
		isOver = true;
		request.removeAttribute("ErrComReportList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();document.frmComUpdate.parent_type[0].focus();" class="BD">

<form name ="frmComUpdate" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.ProjectPlanComReport")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Update datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.UpdateCommunicationReportingItems")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="24" align="center"> # </TD>
			<TD width="160"> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.CommunicationParentType")%>* </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.CommunicationType")%>* </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.MethodTool")%> </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.When")%> </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.Information")%> </TD>
			<TD width="150"> 
				<%=languageChoose.getMessage("fi.jsp.plComReportUpdate.Participants")%> /<BR>
				<%=languageChoose.getMessage("fi.jsp.plComReportUpdate.Responsible")%>
			</TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrCom;
	else vTemp = vCom;
	nRow = vTemp.size();
	
	for (; row < nRow ; row++) {
		comInfo = (ComReportInfo) vTemp.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="com_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD width="24" align = "center"><%=row + 1%><INPUT type="hidden" name ="comID" value ="<%=comInfo.comID%>"/></TD>
			<TD height="57">
				<SELECT name="parent_type" class="COMBO">					
					<OPTION value = "1" <%=(comInfo.comParentType == 1) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.ProjectTaskTrackingType")%></OPTION>
					<OPTION value = "2" <%=(comInfo.comParentType == 2) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.ProjectMeetingType")%></OPTION>
					<OPTION value = "3" <%=(comInfo.comParentType == 3) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.ProjectTrackingType")%></OPTION>
					<OPTION value = "4" <%=(comInfo.comParentType == 4) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.CustomerCommunicationReportingType")%></OPTION>
					<OPTION value = "5" <%=(comInfo.comParentType == 5) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.CommunicationWithSeniorManagementType")%></OPTION>
					<OPTION value = "6" <%=(comInfo.comParentType == 6) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportUpdate.OtherCommunicationAndReporting")%></OPTION>	
				</SELECT>
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="25" name="com_type"><%= comInfo.comType == null ? "": ConvertString.toHtml(comInfo.comType)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="25" name="com_method_tool"><%=comInfo.comMethodTool == null ? "":  ConvertString.toHtml(comInfo.comMethodTool)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="20" name="com_when"><%=comInfo.comWhen == null ? "":  ConvertString.toHtml(comInfo.comWhen)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="25" name="com_information"><%=comInfo.comInfo == null ? "":  ConvertString.toHtml(comInfo.comInfo)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="20" name="com_resp"><%=comInfo.comResp == null ? "":  ConvertString.toHtml(comInfo.comResp)%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	comDisplayed = row;	// Indicate numbers of lines displayed

%>	
	</TBODY>
</TABLE>
<BR>
<input type ="hidden" name = "reqType"/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plComReportUpdate.OK")%>" class="BUTTON" onclick="javascript:updateSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plComReportUpdate.Cancel")%>" class="BUTTON" onclick="javascript:doCancel();">
</form>

<SCRIPT language="JavaScript">

var com_nextHiddenIndex = <%=comDisplayed + 1%>;

function updateSubmit(){	
	if (checkValid()) {
		frmComUpdate.reqType.value=<%=Constants.COMREPORT_UPDATE%>;
		frmComUpdate.submit();
	} else return false;	
}

function doCancel(){
	frmComUpdate.reqType.value =<%=Constants.COMREPORT_VIEW%>;
	frmComUpdate.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("parent_type");
	var arrTxt1= document.getElementsByName("com_type");
	var arrTxt2= document.getElementsByName("com_method_tool");
	var arrTxt3= document.getElementsByName("com_when");
	var arrTxt4= document.getElementsByName("com_information");
	var arrTxt5= document.getElementsByName("com_resp");
	
	var length = com_nextHiddenIndex-1;
	var checkAllBlank = 0;
	
	for(i=0; i < length;i++) {
		if  (   arrTxt[i].value =='0'
				&& trim(arrTxt1[i].value) =='' 
				&& trim(arrTxt2[i].value) =='' 
				&& trim(arrTxt3[i].value) ==''
				&& trim(arrTxt4[i].value) =='' 
				&& trim(arrTxt5[i].value) =='' 
			) 
		{
			checkAllBlank++;				
		} else {
			if (trim(arrTxt1[i].value) =='')  {
				alert("Communication type is mandatory");
				arrTxt1[i].focus();
				return false;
			}

					
			if(!maxLength(arrTxt1[i],"Please input less than 600 characters",600))
			return false;
			if(!maxLength(arrTxt2[i],"Please input less than 600 characters",600))
			return false;
			if(!maxLength(arrTxt3[i],"Please input less than 600 characters",600))
			return false;
			if(!maxLength(arrTxt4[i],"Please input less than 600 characters",600))
			return false;
			if(!maxLength(arrTxt5[i],"Please input less than 600 characters",600))
			return false;
		}
	}
	
	if (checkAllBlank==length) {
		alert("Please input data");
		arrTxt[0].focus();
		return false;
	}
	return true;
}
</SCRIPT>
</BODY>
</HTML>
