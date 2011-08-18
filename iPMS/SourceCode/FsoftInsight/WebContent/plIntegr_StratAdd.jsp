<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plIntegr_StratAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	int maxRow = ProductIntegration.NUMBER_OF_ROW_ADDABLE;
	boolean isOver = false;
	int integrDisplayed;
	String rowStyle;	
	
	IntegrStratInfo integrInfo = null;	
	
	Vector vErrIntegr = (Vector) request.getAttribute("ErrProIntegrList");
	if (vErrIntegr != null) {
		isOver = true;
		nRow = vErrIntegr.size();
		request.removeAttribute("ErrProIntegrList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();document.frmIntegrAdd.integr_comid[0].focus();" class="BD">

<form name ="frmIntegrAdd" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratAdd.AddNewProductIntegration")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratAdd.AddNewProductIntegration")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width = "3%" align = "center"> # </TD>
			<TD width="17%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratAdd.ComponentID")%>* </TD>
			<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratAdd.Description")%> </TD>
			<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratAdd.IntegratedWithComponents")%> </TD>
			<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratAdd.IntegrationOrder")%> </TD>
			<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratAdd.IntegrationReadyNeed")%></TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)  integrInfo = (IntegrStratInfo) vErrIntegr.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="integr_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD align = "center"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_comid"><%=isOver ? ConvertString.toHtml(integrInfo.integrCompID):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_desc"><%= isOver? ConvertString.toHtml(integrInfo.integrDesc):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_withcom"><%=isOver? ConvertString.toHtml(integrInfo.integrWithComp):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_order"><%=isOver? ConvertString.toHtml(integrInfo.integrOrder):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_readyneed"><%=isOver? ConvertString.toHtml(integrInfo.integrReadyNeed):""%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	integrDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="integr_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD align = "center"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_comid"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_desc"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_withcom"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_order"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_readyneed"></TEXTAREA> 
			</TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="integr_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratAdd.AddMoreProductIntegration")%> </a></p>
<BR>
<input type ="hidden" name = "reqType" value = ""/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plReqChangesAdd.Ok")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plReqChangesAdd.Cancel")%>" class="BUTTON" onclick="jumpURL('plLifecycle.jsp');">
</form>

<SCRIPT language="JavaScript">

var integr_nextHiddenIndex = <%=integrDisplayed + 1%>;
function addMoreRow() {
     getFormElement("integr_row" + integr_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 integr_nextHiddenIndex++;
	 if(integr_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("integr_addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(integr_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("integr_addMoreLink").style.display = "none";    
}

function addSubmit(){	
	if (checkValid()) {
		frmIntegrAdd.reqType.value=<%=Constants.PL_INTEGRATION_STRATEGY_ADD%>;
		frmIntegrAdd.submit();
	} else return false;	
}

function doCancel(){
	frmIntegrAdd.reqType.value =<%=Constants.PL_INTEGRATION_STRATEGY_GET_LIST%>;
	frmIntegrAdd.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("integr_comid");
	var arrTxt1= document.getElementsByName("integr_desc");
	var arrTxt2= document.getElementsByName("integr_withcom");
	var arrTxt3= document.getElementsByName("integr_order");
	var arrTxt4= document.getElementsByName("integr_readyneed");
	
	var length = integr_nextHiddenIndex-1;
	var checkAllBlank = 0;
	
	for(i=0; i < length;i++) {
		if  (   trim(arrTxt[i].value) ==''
				&& trim(arrTxt1[i].value) =='' 
				&& trim(arrTxt2[i].value) =='' 
				&& trim(arrTxt3[i].value) ==''
				&& trim(arrTxt4[i].value) =='' 
			) 
		{
			checkAllBlank++;				
		} else {
			if (trim(arrTxt[i].value) =='')  {
				alert("Component ID is mandatory");
				arrTxt[i].focus();
				return false;
			}
		
			if(!maxLength(arrTxt[i],"Please input less than 600 characters",600))
			return false;			
			if(!maxLength(arrTxt1[i],"Please input less than 600 characters",600))
			return false;			
			if(!maxLength(arrTxt2[i],"Please input less than 600 characters",600))
			return false;
			if(!maxLength(arrTxt3[i],"Please input less than 600 characters",600))
			return false;
			if(!maxLength(arrTxt4[i],"Please input less than 600 characters",600))
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
