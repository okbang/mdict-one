<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plReviewStrategyAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<link rel="stylesheet" href="stylesheet/AutoComplete.css" media="screen" type="text/css">
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<script language="javascript" type="text/javascript" src="jscript/autocomplete.js"></script>
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
	int tempPosition = 0 ;
	int row = 0;
	int nRow = 1;
	int maxRow = QualityObjective.NUMBER_OF_ROW_ADDABLE;
	boolean isOver = false;
	int revDisplayed;
	String rowStyle;	
	
	ReviewStrategyInfo revInfo = null;	
	
	Vector vErrRev = (Vector) request.getAttribute("ErrRevStratList");
	if (vErrRev != null) {
		isOver = true;
		nRow = vErrRev.size();
		request.removeAttribute("ErrRevStratList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();document.frmRevStratAdd.rev_item[0].focus();" class="BD">

<form name ="frmRevStratAdd" method= "post" action = "Fms1Servlet#ReviewStrat">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.AddReviewStrategies")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.Strategies")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="3%" align = "center"> # </TD>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.ReviewItem")%>* </TD>
			<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.TypeOfReview")%>* </TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.Reviewer")%>* </TD>
			<TD width="25%"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.When")%>* </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)  revInfo = (ReviewStrategyInfo) vErrRev.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="rev_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD align = "center"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="rev_item"><%=isOver ? ConvertString.toHtml(revInfo.revItem):""%></TEXTAREA> 
			</TD>
			<TD height="57">
				<INPUT size="25" type="text" maxlength="100" name="rev_type" id="rev_type<%=row%>" value="">
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="rev_reviewer"><%= isOver? ConvertString.toHtml(revInfo.revReviewer):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="rev_when"><%= isOver? ConvertString.toHtml(revInfo.revWhen):""%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	revDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="rev_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD align = "center"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="rev_item"></TEXTAREA> 
			</TD>
			<TD height="57">
				<INPUT size="25" type="text" maxlength="100" name="rev_type" id="rev_type<%=row%>" value="">
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="rev_reviewer"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="rev_when"></TEXTAREA> 
			</TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="rev_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.AddMoreStrategies")%> </a></p>
<BR>
<input type ="hidden" name = "reqType" value = ""/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.Cancel")%>" class="BUTTON" onclick="doCancel();">
</form>

<SCRIPT language="JavaScript">

var rev_nextHiddenIndex = <%=revDisplayed + 1%>;
<% tempPosition = revDisplayed + 1 ;%>
function addMoreRow() {
     getFormElement("rev_row" + rev_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 rev_nextHiddenIndex++;
	 if(rev_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("rev_addMoreLink").style.display = "none";
	     
	 <%
	 	for (int i=0;i<10;i++)
			out.write("AutoComplete_Create('rev_type" + i + "', data);");
	 %>
	
}
init();
function init(){
   if(rev_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("rev_addMoreLink").style.display = "none";    
}

function addSubmit(){	
	if (checkValid()) {
		frmRevStratAdd.reqType.value=<%=Constants.PL_REVIEW_STRATEGY_ADD%>;
		frmRevStratAdd.submit();
	} else return false;	
}

function doCancel(){
	frmRevStratAdd.reqType.value =<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>;
	frmRevStratAdd.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("rev_item");
	var arrTxt1= document.getElementsByName("rev_type");
	var arrTxt2= document.getElementsByName("rev_reviewer");
	var arrTxt3= document.getElementsByName("rev_when");
	
	var length = rev_nextHiddenIndex-1;
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
				alert("Review item is mandatory");
				arrTxt[i].focus();
				return false;
			}
			
			if (trim(arrTxt1[i].value) =='')  {
				alert("Type of Review information is mandatory");
				arrTxt1[i].focus();
				return false;
			}
			
			if (trim(arrTxt2[i].value) =='')  {
				alert("Reviewer is mandatory");
				arrTxt2[i].focus();
				return false;
			}
			
			if (trim(arrTxt3[i].value) =='')  {
				alert("When is mandatory");
				arrTxt3[i].focus();
				return false;
			}
		
			if(!maxLength(arrTxt[i],"Please input less than 600 characters",600))
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

 data = ['<%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.GroupReview")%>',
 		 '<%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.OnePersonReview")%>',
 		 '<%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.OtherType")%>'
 		];
	
<%
	for (int i = 0; i < 10; i++) {
		out.write("AutoComplete_Create('rev_type" + i + "', data);");
	}
%>	
</SCRIPT>
</BODY>
</HTML>
