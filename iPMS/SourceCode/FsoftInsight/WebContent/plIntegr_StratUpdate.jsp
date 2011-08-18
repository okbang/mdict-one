<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<script type="text/javascript" src="jscript/ajaxtooltip.js">
</script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plIntegr_StratUpdate.jsp</TITLE>
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
	int nRow = 0;
	boolean isOver = false;
	String rowStyle;
	int integrDisplayed;
	
	Vector vIntegr = (Vector) session.getAttribute("ProIntegrList");
	Vector vErrIntegr = (Vector) request.getAttribute("ErrProIntegrList");	
	
	if (vErrIntegr != null) {
		isOver = true;
		request.removeAttribute("ErrProIntegrList");
	}

	IntegrStratInfo integrInfo = null;	
	
	String strErr = (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratUpdate.UpdateProductIntegration")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Update failure ! System error</P>
<%
	} 
%>

<BR>
<form name ="frmIntegrUpdate" method= "Post" action ="Fms1Servlet">
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratUpdate.UpdateProductIntegration")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD valign ="top" width = "3%"> &nbsp; </TD>
			<TD width = "3%" align = "center"> # </TD>
			<TD width="17%"><%=languageChoose.getMessage("fi.jsp.plIntegr_StratUpdate.ComponentID")%>* </TD>
			<TD width="19%"><%=languageChoose.getMessage("fi.jsp.plIntegr_StratUpdate.Description")%> </TD>
			<TD width="19%"><%=languageChoose.getMessage("fi.jsp.plIntegr_StratUpdate.IntegratedWithComponents")%> </TD>
			<TD width="15%"><%=languageChoose.getMessage("fi.jsp.plIntegr_StratUpdate.IntegrationOrder")%> </TD>
			<TD width="19%"><%=languageChoose.getMessage("fi.jsp.plIntegr_StratUpdate.IntegrationReadyNeed")%></TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrIntegr;
	else vTemp = vIntegr;
	nRow = vTemp.size();
	
	for (; row < nRow ; row++) {
		integrInfo = (IntegrStratInfo) vTemp.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR class="<%=rowStyle%>" valign="top">
			<TD align = "center"> <A href="javascript:OnDelete(<%=integrInfo.integrID%>)"><IMG src="image/delete1.gif" title="Delete"></img></A></TD>
			<TD align = "center"> <%=row + 1%> 
				<input name ="integrID" type ="hidden" value="<%=integrInfo.integrID%>">
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_comid"><%= (integrInfo.integrCompID ==null)? "":ConvertString.toHtml(integrInfo.integrCompID)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_desc"><%=(integrInfo.integrDesc == null)? "":ConvertString.toHtml(integrInfo.integrDesc)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_withcom"><%=(integrInfo.integrWithComp == null)? "":ConvertString.toHtml(integrInfo.integrWithComp)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_order"><%=(integrInfo.integrOrder == null)? "":ConvertString.toHtml(integrInfo.integrOrder)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="integr_readyneed"><%=(integrInfo.integrReadyNeed == null)? "":ConvertString.toHtml(integrInfo.integrReadyNeed)%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	integrDisplayed = row;	// Indicate numbers of lines displayed	
%>		
	</TBODY>
</TABLE>
<BR>
<input type = "hidden" name = "reqType" value = ""/>
<input type = "hidden" name = "DelIntegrID" value = ""/>

<INPUT type="button" name="plIntegrSave" value="<%=languageChoose.getMessage("fi.jsp.plIntegr_StratUpdate.Ok")%>" class="BUTTON" onclick="updateSubmit();">
<INPUT type="button" name="plIntegrCancel" value="<%=languageChoose.getMessage("fi.jsp.plIntegr_StratUpdate.Cancel")%>" class="BUTTON" onclick="jumpURL('plLifecycle.jsp');">
</form>

<SCRIPT language="JavaScript">
function OnDelete(paraIntegrID){
	if (window.confirm("Are you sure to delete this product integration ?")!=0) {
		frmIntegrUpdate.reqType.value =<%=Constants.PL_INTEGRATION_STRATEGY_DELETE%>;
		frmIntegrUpdate.DelIntegrID.value =paraIntegrID;
		frmIntegrUpdate.submit();
	}
}
function updateSubmit(){	
	if (checkValid()) {
		frmIntegrUpdate.reqType.value =<%=Constants.PL_INTEGRATION_STRATEGY_UPDATE%>;
		frmIntegrUpdate.submit();
	} else return false;	
}

function doBack(){
	frmIntegrUpdate.reqType.value =<%=Constants.PL_INTEGRATION_STRATEGY_GET_LIST%>;
	frmIntegrUpdate.submit();
}

function checkValid(){
	
	var arrTxt  = document.getElementsByName("integr_comid");
	var arrTxt1 = document.getElementsByName("integr_desc");
	var arrTxt2 = document.getElementsByName("integr_withcom");
	var arrTxt3 = document.getElementsByName("integr_order");
	var arrTxt4 = document.getElementsByName("integr_readyneed");
	
	for(i=0; i < arrTxt.length;i++) {
		if (trim(arrTxt[i].value) =='')  {			
			alert("Component ID is mandatory !");
			arrTxt[i].focus();			
			return false;
		}
		
		if(!maxLength(arrTxt[i], "Please input less than 600 characters",600))
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
	return true;
}

function trim(sString)
{
	while (sString.substring(0,1) == ' ')
	{
		sString = sString.substring(1, sString.length);
	}
	while (sString.substring(sString.length-1, sString.length) == ' ')
	{ 
		sString = sString.substring(0,sString.length-1);
	}
	return sString;
}
</SCRIPT>
</BODY>
</HTML>
