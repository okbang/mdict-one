<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plTestStrategyAdd.jsp</TITLE>
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
	int maxRow = QualityObjective.NUMBER_OF_ROW_ADDABLE;
	boolean isOver = false;
	int testDisplayed;
	String rowStyle;	
	
	TestStrategyInfo testInfo = null;	
	
	Vector vErrTest = (Vector) request.getAttribute("ErrTestStratList");
	if (vErrTest != null) {
		isOver = true;
		nRow = vErrTest.size();
		request.removeAttribute("ErrTestStratList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();document.frmTestStratAdd.test_item[0].focus();" class="BD">

<form name ="frmTestStratAdd" method= "post" action = "Fms1Servlet#TestStrat">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.AddTestStrategies")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.Strategies")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="3%" align = "center"> # </TD>
			<TD width="28%"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.TestItem")%>* </TD>
			<TD width="13%"> Test Stage* </TD>
			<TD width="17%"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.PersonInCharge")%>* </TD>
			<TD width="17%"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.CompletionCriteria")%>* </TD>
			<TD width="17%"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.EntryCriteria")%>* </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)  testInfo = (TestStrategyInfo) vErrTest.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="test_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD align = "center"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_item"><%=isOver ? ConvertString.toHtml(testInfo.testItem):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <SELECT name="test_type" class="comBo">
			 	<OPTION value = "0"/>&nbsp;
			 	<OPTION value = "1"/><%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.UnitTest")%>
			 	<OPTION value = "2"/><%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.IntegrationTest")%>
			 	<OPTION value = "3"/><%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.SystemTest")%>
			 </SELECT>
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_reviewer"><%= isOver? ConvertString.toHtml(testInfo.testReviewer):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_completion_criteria"><%= isOver? ConvertString.toHtml(testInfo.testComplCriteria):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_entry_criteria"><%= isOver? ConvertString.toHtml(testInfo.testEntryCriteria):""%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	testDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="test_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD align = "center"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_item"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <SELECT name="test_type" class="comBo">
			 	<OPTION value = "0"/>&nbsp;
			 	<OPTION value = "1"/><%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.UnitTest")%>
			 	<OPTION value = "2"/><%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.IntegrationTest")%>
			 	<OPTION value = "3"/><%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.SystemTest")%>
			 </SELECT>
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_reviewer"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_completion_criteria"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_entry_criteria"></TEXTAREA> 
			</TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="test_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.AddMoreStrategies")%> </a></p>
<BR>
<input type ="hidden" name = "reqType" value = ""/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plTestStrategyAdd.Cancel")%>" class="BUTTON" onclick="doCancel();">
</form>

<SCRIPT language="JavaScript">

var test_nextHiddenIndex = <%=testDisplayed + 1%>;
function addMoreRow() {
     getFormElement("test_row" + test_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 test_nextHiddenIndex++;
	 if(test_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("test_addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(test_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("test_addMoreLink").style.display = "none";    
}

function addSubmit(){	
	if (checkValid()) {
		frmTestStratAdd.reqType.value=<%=Constants.PL_TEST_STRATEGY_ADD%>;
		frmTestStratAdd.submit();
	} else return false;	
}

function doCancel(){
	frmTestStratAdd.reqType.value =<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>;
	frmTestStratAdd.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("test_item");
	var arrTxt1= document.getElementsByName("test_type");
	var arrTxt2= document.getElementsByName("test_reviewer");
	var arrTxt3= document.getElementsByName("test_completion_criteria");
	var arrTxt4= document.getElementsByName("test_entry_criteria");
	
	var length = test_nextHiddenIndex-1;
	var checkAllBlank = 0;
	
	for(i=0; i < length;i++) {
		if  (   trim(arrTxt[i].value) ==''
				&& arrTxt1[i].value =='0'  
				&& trim(arrTxt2[i].value) ==''
				&& trim(arrTxt3[i].value) ==''
				&& trim(arrTxt4[i].value) ==''
			) 
		{
			checkAllBlank++;				
		} else {
			if (trim(arrTxt[i].value) =='')  {
				alert("Test item is mandatory");
				arrTxt[i].focus();
				return false;
			}
			
			if (arrTxt1[i].value =='0')  {
				alert("Type of Test information is mandatory");
				arrTxt1[i].focus();
				return false;
			}
			
			if (trim(arrTxt2[i].value) =='')  {
				alert("Reviewer is mandatory");
				arrTxt2[i].focus();
				return false;
			}
			
			if (trim(arrTxt3[i].value) =='')  {
				alert("Completion criteria is mandatory");
				arrTxt3[i].focus();
				return false;
			}
			
			if (trim(arrTxt4[i].value) =='')  {
				alert("Entry criteria is mandatory");
				arrTxt4[i].focus();
				return false;
			}
		
			if(!maxLength(arrTxt[i],"Please input less than 600 characters",600))
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
