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
<TITLE>plDefAcrUpdate.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	boolean isOver = false;
	int serDisplayed;
	String rowStyle;	
	
	DefAcrInfo defInfo = null;	
	
	Vector vDef = (Vector) session.getAttribute("DefAcrBatchUpdateList");
	
	Vector vErrDef = (Vector) request.getAttribute("ErrDefAcrList");
	if (vErrDef != null) {
		isOver = true;
		request.removeAttribute("ErrDefAcrList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<form name ="frmDefAcrUpdate" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plDefAcrUpdate.ProjectPlanDefinitions_N_Acronyms")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plDefAcrUpdate.UpdateDefinitions_N_Acronyms")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="3%" align="center"> # </TD>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plDefAcrUpdate.Acronym")%>* </TD>			
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plDefAcrUpdate.Definition")%>* </TD>
			<TD width="32%"> <%=languageChoose.getMessage("fi.jsp.plDefAcrUpdate.Note")%> </TD>				
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrDef;
	else vTemp = vDef;
	nRow = vTemp.size();
	
	for (; row < nRow ; row++) {
		defInfo = (DefAcrInfo) vTemp.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="ser_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD width="3%" align = "center"><%=row + 1%><INPUT type="hidden" name ="defID" value ="<%=defInfo.defID%>"/></TD>			
			<TD height="57">
			 	<TEXTAREA style="width:100%;height:100%" name="def_acr"><%= (defInfo.acronym != null) ? ConvertString.toHtml(defInfo.acronym):""%></TEXTAREA> 
			</TD>
			<TD height="57">
				<TEXTAREA style="width:100%;height:100%" name="def_def"><%= (defInfo.definition != null )? ConvertString.toHtml(defInfo.definition):""%></TEXTAREA> 
			</TD>
			<TD height="57">
				<TEXTAREA style="width:100%;height:100%" name="def_note"><%=(defInfo.note != null) ? ConvertString.toHtml(defInfo.note):""%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	serDisplayed = row;	// Indicate numbers of lines displayed	
%>
		
	</TBODY>
</TABLE>
<BR/>
<input type ="hidden" name = "reqType"/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plDefAcrUpdate.OK")%>" class="BUTTON" onclick="updateSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plDefAcrUpdate.Cancel")%>" class="BUTTON" onclick="doCancel();">
</form>

<SCRIPT language="JavaScript">

var ser_nextHiddenIndex = <%=serDisplayed + 1%>;

function updateSubmit(){	
	if (checkValid()) {
		frmDefAcrUpdate.reqType.value=<%=Constants.DEFINITIONS_ACRONYMS_UPDATE%>;
		frmDefAcrUpdate.submit();
	} else return false;	
}

function doCancel(){
	frmDefAcrUpdate.reqType.value =<%=Constants.DEFINITIONS_ACRONYMS_VIEW%>;
	frmDefAcrUpdate.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("def_acr");	
	var arrTxt1= document.getElementsByName("def_def");
	var arrTxt2= document.getElementsByName("def_note");
	
	var length = ser_nextHiddenIndex-1;
	var checkAllBlank = 0;
	
	for(i=0; i < length;i++) {
		if (trim(arrTxt[i].value) =='')  {
			alert("Acronym is mandatory");
			arrTxt[i].focus();
			return false;
		}

		if (trim(arrTxt1[i].value) =='')  {
			alert("Definition is mandatory");
			arrTxt1[i].focus();
			return false;
		}
				
		if(!maxLength(arrTxt[i],"Please input less than 600 characters",600))
		return false;
		if(!maxLength(arrTxt1[i],"Please input less than 600 characters",600))
		return false;
		if(!maxLength(arrTxt2[i],"Please input less than 600 characters",600))
		return false;
	}
	
	return true;
}
</SCRIPT>
</BODY>
</HTML>
