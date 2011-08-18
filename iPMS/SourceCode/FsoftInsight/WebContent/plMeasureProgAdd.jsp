<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plMeasureProgAdd.jsp</TITLE>
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
	int maxRow = MeasurementsProg.NUMBER_OF_ROW_ADDABLE;
	boolean isOver = false;
	int measDisplayed;
	String rowStyle;	
	
	MeasureProgInfo measInfo = null;	
	
	Vector vErrMeas = (Vector) request.getAttribute("ErrMeasurementsProgList");
	if (vErrMeas != null) {
		isOver = true;
		nRow = vErrMeas.size();
		request.removeAttribute("ErrMeasurementsProgList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();document.frmMeasAdd.mes_data_colect[0].focus();" class="BD">

<form name ="frmMeasAdd" method= "post" action = "Fms1Servlet#Measurement">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgAdd.ProjectPlanAddNewMeasurementsProgram")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgAdd.Measurements")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="3%" align = "center"> # </TD>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgAdd.DataToBeCollected")%>* </TD>
			<TD width="25%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgAdd.Purpose")%>* </TD>
			<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgAdd.Responsible")%>* </TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgAdd.When")%>* </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)  measInfo = (MeasureProgInfo) vErrMeas.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="meas_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD align = "center"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_data_colect"><%=isOver ? ConvertString.toHtml(measInfo.mes_data_colect):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_purpose"><%= isOver? ConvertString.toHtml(measInfo.mes_purpose):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_responsible"><%=isOver? ConvertString.toHtml(measInfo.mes_responsible):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_when"><%=isOver? ConvertString.toHtml(measInfo.mes_when):""%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	measDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="meas_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top" >
			<TD align = "center"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_data_colect"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_purpose"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_responsible"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="mes_when"></TEXTAREA> 
			</TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="meas_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgAdd.AddMoreMeasurements")%> </a></p>
<BR>
<input type ="hidden" name = "reqType" value = ""/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plMeasureProgAdd.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plMeasureProgAdd.Cancel")%>" class="BUTTON" onclick="doCancel();">
</form>

<SCRIPT language="JavaScript">

var meas_nextHiddenIndex = <%=measDisplayed + 1%>;
function addMoreRow() {
     getFormElement("meas_row" + meas_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 meas_nextHiddenIndex++;
	 if(meas_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("meas_addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(meas_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("meas_addMoreLink").style.display = "none";    
}

function addSubmit(){	
	if (checkValid()) {
		frmMeasAdd.reqType.value=<%=Constants.PL_MEASUREMENTS_PROGRAM_ADD%>;
		frmMeasAdd.submit();
	} else return false;	
}

function doCancel(){
	frmMeasAdd.reqType.value =<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>;
	frmMeasAdd.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("mes_data_colect");
	var arrTxt1= document.getElementsByName("mes_purpose");
	var arrTxt2= document.getElementsByName("mes_responsible");
	var arrTxt3= document.getElementsByName("mes_when");
	
	var length = meas_nextHiddenIndex-1;
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
				alert("'Data to be collected' is mandatory");
				arrTxt[i].focus();
				return false;
			}
			
			if (trim(arrTxt1[i].value) =='')  {
				alert("'Perpose' is mandatory");
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
			
		
			if(!maxLength(arrTxt[i],"Please input less than 600 characters",600))
			return false;			
			if(!maxLength(arrTxt1[i],"Please input less than 600 characters",600))
			return false;			
			if(!maxLength(arrTxt2[i],"Please input less than 600 characters",600))
			return false;
			if(!maxLength(arrTxt3[i],"Please input less than 600 characters",600))
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
