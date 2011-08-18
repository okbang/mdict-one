<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<script type="text/javascript" src="jscript/ajaxtooltip.js">
</script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plMeasureProgUpdate.jsp</TITLE>
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
	int measDisplayed;
	
	Vector vMeas = (Vector) session.getAttribute("MeasurementsProgList");
	Vector vErrMeas = (Vector) request.getAttribute("ErrMeasurementsProgList");	
	
	if (vErrMeas != null) {
		isOver = true;
		nRow = vErrMeas.size();
		request.removeAttribute("ErrMeasurementsProgList");
	}
	else if (vMeas != null) {
		nRow = vMeas.size();		
	}

	MeasureProgInfo measInfo = null;	
	
	String strErr = (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgUpdate.ProjectPlanUpdateMeasurementsProgram")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Update failure ! System error</P>
<%
	} 
%>

<BR>
<form name ="frmMeasUpdate" method= "Post" action ="Fms1Servlet#Measurement">
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgUpdate.Measurements")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="3%" valign ="top"> &nbsp; </TD>
			<TD width="3%" align = "center"> # </TD>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgUpdate.DataToBeCollected")%>* </TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgUpdate.Purpose")%> </TD>
			<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgUpdate.Responsible")%>* </TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgUpdate.When")%>* </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; row < nRow ; row++) {
		if (isOver)  measInfo = (MeasureProgInfo) vErrMeas.elementAt(row);
		else measInfo = (MeasureProgInfo) vMeas.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR class="<%=rowStyle%>" valign="top">
			<TD align = "center"> <A href="javascript:OnDelete(<%=measInfo.mes_id%>)"><IMG src="image/delete1.gif" title="Delete"></img></A></TD>
			<TD align = "center"> <%=row + 1%> 
				<input name ="mes_id" type ="hidden" value="<%=measInfo.mes_id%>">
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_data_colect"><%= (measInfo.mes_data_colect ==null)? "":ConvertString.toHtml(measInfo.mes_data_colect)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_purpose"><%=(measInfo.mes_purpose == null)? "":ConvertString.toHtml(measInfo.mes_purpose)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_responsible"><%=(measInfo.mes_responsible == null)? "":ConvertString.toHtml(measInfo.mes_responsible)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_when"><%=(measInfo.mes_when == null)? "":ConvertString.toHtml(measInfo.mes_when)%></TEXTAREA> 
			</TD>			
		</TR>
<%	
	}
	measDisplayed = row;	// Indicate numbers of lines displayed	
%>		
	</TBODY>
</TABLE>
<BR>
<input type = "hidden" name = "reqType" value = ""/>
<input type = "hidden" name = "delMeasID" value = ""/>

<INPUT type="button" name="plMeasOK" value="<%=languageChoose.getMessage("fi.jsp.plMeasureProgUpdate.OK")%>" class="BUTTON" onclick="updateSubmit();">
<INPUT type="button" name="plMeasCancel" value="<%=languageChoose.getMessage("fi.jsp.plMeasureProgUpdate.Cancel")%>" class="BUTTON" onclick="doBack()">
</form>

<SCRIPT language="JavaScript">
function OnDelete(paraMesID){
	if (window.confirm("Are you sure to delete this product integration ?")!=0) {
		frmMeasUpdate.reqType.value =<%=Constants.PL_MEASUREMENTS_PROGRAM_DELETE%>;
		frmMeasUpdate.delMeasID.value =paraMesID;
		frmMeasUpdate.submit();
	}
}
function updateSubmit(){	
	if (checkValid()) {
		frmMeasUpdate.reqType.value =<%=Constants.PL_MEASUREMENTS_PROGRAM_UPDATE%>;
		frmMeasUpdate.submit();
	} else return false;	
}

function doBack(){
	frmMeasUpdate.reqType.value =<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>;
	frmMeasUpdate.submit();
}

function checkValid(){
	
	var arrTxt= document.getElementsByName("mes_data_colect");
	var arrTxt1= document.getElementsByName("mes_purpose");
	var arrTxt2= document.getElementsByName("mes_responsible");
	var arrTxt3= document.getElementsByName("mes_when");
	
	for(i=0; i < arrTxt.length;i++) {
			if (trim(arrTxt[i].value) =='')  {
				alert("'Data to be collected' is mandatory");
				arrTxt[i].focus();
				return false;
			}
			
			if (trim(arrTxt2[i].value) =='')  {
				alert("'Responsible' is mandatory");
				arrTxt2[i].focus();
				return false;
			}
			
			if (trim(arrTxt3[i].value) =='')  {
				alert("'When' is mandatory");
				arrTxt3[i].focus();
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
