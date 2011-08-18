<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<script type="text/javascript" src="jscript/ajaxtooltip.js">
</script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plTestStrategyUpdate.jsp</TITLE>
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
	int TestDisplayed;
	
	Vector vTest = (Vector) session.getAttribute("TestStrategyList");
	Vector vErrTest = (Vector) request.getAttribute("ErrTestStrategyList");
	if (vErrTest != null) {
		isOver = true;		
		request.removeAttribute("ErrTestStrategyList");
	}
	TestStrategyInfo testInfo = null;	
	
	String strErr = (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.UpdateTestStrategies")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Update failure ! System error</P>
<%
	} 
%>

<BR>
<form name ="frmTestStratUpdate" method= "Post" action ="Fms1Servlet#TestStrat">
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.Strategies")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width = "3%" valign ="top"> &nbsp; </TD>
			<TD width = "3%" align = "center"> # </TD>
			<TD width="28%"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.TestItem")%>* </TD>
			<TD width="13%"> Test Stage* </TD>
			<TD width="14%"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.PersonInCharge")%>* </TD>
			<TD width="17%"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.CompletionCriteria")%>* </TD>
			<TD width="17%"> <%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.EntryCriteria")%>* </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrTest;
	else vTemp = vTest;
	
	nRow = vTemp.size();
	for (; row < nRow ; row++) {
		testInfo = (TestStrategyInfo) vTemp.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR class="<%=rowStyle%>" valign="top">
			<TD align = "center"> <A href="javascript:OnDelete(<%=testInfo.testID%>)"><IMG src="image/delete1.gif" title="Delete"></img></A></TD>
			<TD align = "center"> <%=row + 1%> 
				<input name ="testID" type ="hidden" value="<%=testInfo.testID%>">
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_item"><%= (testInfo.testItem ==null)? "":ConvertString.toHtml(testInfo.testItem)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <SELECT name="test_type" class="comBo">
			 	<OPTION value = "1" <%= (testInfo.testType == 1) ? "selected":""%> /><%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.UnitTest")%>
			 	<OPTION value = "2" <%= (testInfo.testType == 2) ? "selected":""%> /><%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.IntegrationTest")%>
			 	<OPTION value = "3" <%= (testInfo.testType == 3) ? "selected":""%> /><%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.SystemTest")%>
			 </SELECT>
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_reviewer"><%= (testInfo.testReviewer ==null)? "":ConvertString.toHtml(testInfo.testReviewer)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_completion_criteria"><%= (testInfo.testComplCriteria ==null)? "":ConvertString.toHtml(testInfo.testComplCriteria)%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="test_entry_criteria"><%= (testInfo.testEntryCriteria ==null)? "":ConvertString.toHtml(testInfo.testEntryCriteria)%></TEXTAREA> 
			</TD>		
		</TR>
<%	
	}
	TestDisplayed = row;	// Indicate numbers of lines displayed	
%>		
	</TBODY>
</TABLE>
<BR>
<input type = "hidden" name = "reqType"/>
<input type = "hidden" name = "delTestID"/>

<INPUT type="button" name="btnSave" value="<%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.OK")%>" class="BUTTON" onclick="updateSubmit();">
<INPUT type="button" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.plTestStrategyUpdate.Cancel")%>" class="BUTTON" onclick="doBack()">
</form>

<SCRIPT language="JavaScript">
function OnDelete(paraTestID){
	if (window.confirm("Are you sure to delete this strategy ?")!=0) {
		frmTestStratUpdate.reqType.value =<%=Constants.PL_TEST_STRATEGY_DELETE%>;
		frmTestStratUpdate.delTestID.value =paraTestID;
		frmTestStratUpdate.submit();
	}
}
function updateSubmit(){	
	if (checkValid()) {
		frmTestStratUpdate.reqType.value =<%=Constants.PL_TEST_STRATEGY_UPDATE%>;
		frmTestStratUpdate.submit();
	} else return false;	
}

function doBack(){
	frmTestStratUpdate.reqType.value =<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>;
	frmTestStratUpdate.submit();
}

function checkValid(){
	
	var arrTxt= document.getElementsByName("test_item");
	var arrTxt1= document.getElementsByName("test_type");
	var arrTxt2= document.getElementsByName("test_Reviewer");
	var arrTxt3= document.getElementsByName("test_completion_criteria");
	var arrTxt4= document.getElementsByName("test_entry_criteria");

	for(i=0; i < arrTxt.length;i++) {		
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
	return true;
}
</SCRIPT>
</BODY>
</HTML>
