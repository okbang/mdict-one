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
<TITLE>plComReportAdd.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	int maxRow = ComReport.NUMBER_OF_ROW_ADDABLE;
	boolean isOver = false;
	int comDisplayed;
	String rowStyle;	
	
	ComReportInfo comInfo = null;	
	
	Vector vErrCom = (Vector) request.getAttribute("ErrComReportList");
	if (vErrCom != null) {
		isOver = true;
		nRow = vErrCom.size();
		request.removeAttribute("ErrComReportList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();document.frmComAdd.parent_type[0].focus();" class="BD">

<form name ="frmComAdd" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.ProjectPlanComReport")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.AddNewCommunicationReportingItems")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="24" align="center"> # </TD>
			<TD width="160"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.CommunicationParentType")%>* </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.CommunicationType")%>* </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.MethodTool")%> </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.When")%> </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.Information")%> </TD>
			<TD width="150"> 
				<%=languageChoose.getMessage("fi.jsp.plComReportAdd.Participants")%> /<BR>
				<%=languageChoose.getMessage("fi.jsp.plComReportAdd.Responsible")%>
			</TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)  comInfo = (ComReportInfo) vErrCom.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="com_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD width="24" align = "center"><%=row + 1%></TD>
			<TD height="57">
				<SELECT name="parent_type" class="COMBO">
					<OPTION value = "0">&nbsp;</OPTION>
					<OPTION value = "1" <%=(isOver && comInfo.comParentType == 1) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.ProjectTaskTrackingType")%></OPTION>
					<OPTION value = "2" <%=(isOver && comInfo.comParentType == 2) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.ProjectMeetingType")%></OPTION>
					<OPTION value = "3" <%=(isOver && comInfo.comParentType == 3) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.ProjectTrackingType")%></OPTION>
					<OPTION value = "4" <%=(isOver && comInfo.comParentType == 4) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.CustomerCommunicationReportingType")%></OPTION>
					<OPTION value = "5" <%=(isOver && comInfo.comParentType == 5) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.CommunicationWithSeniorManagementType")%></OPTION>
					<OPTION value = "6" <%=(isOver && comInfo.comParentType == 6) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.OtherCommunicationAndReporting")%></OPTION>	
				</SELECT>
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="25" name="com_type"><%= isOver? ConvertString.toHtml(comInfo.comType):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="25" name="com_method_tool"><%= isOver? ConvertString.toHtml(comInfo.comMethodTool):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="20" name="com_when"><%= isOver? ConvertString.toHtml(comInfo.comWhen):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="25" name="com_information"><%= isOver? ConvertString.toHtml(comInfo.comInfo):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="20" name="com_resp"><%= isOver? ConvertString.toHtml(comInfo.comResp):""%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	comDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="com_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD width="24" align = "center"> <%=row + 1%> </TD>
			<TD height="57">
				<SELECT name="parent_type" class="COMBO">
					<OPTION value = "0">&nbsp;</OPTION>
					<OPTION value = "1"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.ProjectTaskTrackingType")%></OPTION>
					<OPTION value = "2"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.ProjectMeetingType")%></OPTION>
					<OPTION value = "3"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.ProjectTrackingType")%></OPTION>
					<OPTION value = "4"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.CustomerCommunicationReportingType")%></OPTION>
					<OPTION value = "5"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.CommunicationWithSeniorManagementType")%></OPTION>
					<OPTION value = "6"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.OtherCommunicationAndReporting")%></OPTION>	
				</SELECT>
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="25" name="com_type"></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="25" name="com_method_tool"></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="20" name="com_when"></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="25" name="com_information"></TEXTAREA> 
			</TD>
			<TD height="57">
			 	<TEXTAREA rows=4 cols ="20" name="com_resp"></TEXTAREA> 
			</TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="com_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.plComReportAdd.AddMoreCommunicationReportingItem")%> </a></p>
<BR>
<input type ="hidden" name = "reqType"/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plComReportAdd.OK")%>" class="BUTTON" onclick="javascript:addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plComReportAdd.Cancel")%>" class="BUTTON" onclick="javascript:doCancel();">
</form>

<SCRIPT language="JavaScript">

var com_nextHiddenIndex = <%=comDisplayed + 1%>;
function addMoreRow() {
     getFormElement("com_row" + com_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 com_nextHiddenIndex++;
	 if(com_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("com_addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(com_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("com_addMoreLink").style.display = "none";    
}

function addSubmit(){	
	if (checkValid()) {
		frmComAdd.reqType.value=<%=Constants.COMREPORT_ADD%>;
		frmComAdd.submit();
	} else return false;	
}

function doCancel(){
	frmComAdd.reqType.value =<%=Constants.COMREPORT_VIEW%>;
	frmComAdd.submit();
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
			if (arrTxt[i].value =='0')  {
				alert("Communication parent type is mandatory");
				arrTxt[i].focus();
				return false;
			}
			
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
