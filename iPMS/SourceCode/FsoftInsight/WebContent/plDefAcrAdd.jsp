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
<TITLE>plDefAcrAdd.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	int maxRow = DefAcr.NUMBER_OF_ROW_ADDABLE;
	boolean isOver = false;
	int serDisplayed;
	String rowStyle;	
	
	DefAcrInfo defInfo = null;	
	
	Vector vErrDef = (Vector) request.getAttribute("ErrDefAcrList");
	if (vErrDef != null) {
		isOver = true;
		nRow = vErrDef.size();
		request.removeAttribute("ErrDefAcrList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();document.frmDefAcrAdd.def_acr[0].focus();" class="BD">

<form name ="frmDefAcrAdd" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plDefAcrAdd.ProjectPlanDefinitions_N_Acronyms")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plDefAcrAdd.AddNewDefinitions_N_Acronyms")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="3%" align="center"> # </TD>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plDefAcrAdd.Acronym")%>* </TD>			
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plDefAcrAdd.Definition")%>* </TD>
			<TD width="32%"> <%=languageChoose.getMessage("fi.jsp.plDefAcrAdd.Note")%> </TD>				
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)  defInfo = (DefAcrInfo) vErrDef.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="def_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD width="3%" align = "center"><%=row + 1%></TD>			
			<TD height="57">
			 	<TEXTAREA style="width:100%;height:100%" name="def_acr"><%= isOver? ConvertString.toHtml(defInfo.acronym):""%></TEXTAREA> 
			</TD>			
			<TD height="57">
				<TEXTAREA style="width:100%;height:100%" name="def_def"><%=isOver? ConvertString.toHtml(defInfo.definition):""%></TEXTAREA> 
			</TD>
			<TD height="57">
				<TEXTAREA style="width:100%;height:100%" name="def_note"><%=isOver? ConvertString.toHtml(defInfo.note):""%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	serDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="def_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD width="3%" align = "center"> <%=row + 1%> </TD>			
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="def_acr"></TEXTAREA> 
			</TD>			
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="def_def"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="def_note"></TEXTAREA> 
			</TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="def_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.plDefAcrAdd.AddMoreDefinitions_N_Acronyms")%> </a></p>
<BR>
<input type ="hidden" name = "reqType"/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plDefAcrAdd.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plDefAcrAdd.Cancel")%>" class="BUTTON" onclick="doCancel();">
</form>

<SCRIPT language="JavaScript">

var def_nextHiddenIndex = <%=serDisplayed + 1%>;
function addMoreRow() {
     getFormElement("def_row" + def_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 def_nextHiddenIndex++;
	 if(def_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("def_addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(def_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("def_addMoreLink").style.display = "none";    
}

function addSubmit(){	
	if (checkValid()) {
		frmDefAcrAdd.reqType.value=<%=Constants.DEFINITIONS_ACRONYMS_ADD%>;
		frmDefAcrAdd.submit();
	} else return false;	
}

function doCancel(){
	frmDefAcrAdd.reqType.value =<%=Constants.DEFINITIONS_ACRONYMS_VIEW%>;
	frmDefAcrAdd.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("def_acr");	
	var arrTxt1= document.getElementsByName("def_def");
	var arrTxt2= document.getElementsByName("def_note");
	
	var length = def_nextHiddenIndex-1;
	var checkAllBlank = 0;
	
	for(i=0; i < length;i++) {
		if  (   trim(arrTxt[i].value) ==''
				&& trim(arrTxt1[i].value) =='' 
				&& trim(arrTxt2[i].value) =='' 
				&& trim(arrTxt3[i].value) ==''				
			) 
		{
			checkAllBlank++;				
		} else {
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
