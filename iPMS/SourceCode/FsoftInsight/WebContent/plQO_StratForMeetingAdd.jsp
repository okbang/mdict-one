<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plQO_StratForMeetingAdd.jsp</TITLE>
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
	int stratDisplayed;
	String rowStyle;	
	
	StrategyOfMeetingInfo stratInfo = null;	
	
	Vector vErrStrat = (Vector) request.getAttribute("ErrStratOfMeetingList");
	if (vErrStrat != null) {
		isOver = true;
		nRow = vErrStrat.size();
		request.removeAttribute("ErrStratOfMeetingList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();document.frmQualityStratAdd.strat_desc[0].focus();" class="BD">

<form name ="frmQualityStratAdd" method= "post" action = "Fms1Servlet#MeetingStrat">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingAdd.AddStrategyForMeetingQualityObjectives")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingAdd.Strategies")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width = "3%" align = "center"> # </TD>
			<TD width="46%"> <%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingAdd.Strategy")%>* </TD>
			<TD width="46%"> <%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingAdd.ExpectedBenefits")%>* </TD>			
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)  stratInfo = (StrategyOfMeetingInfo) vErrStrat.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="strat_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD align = "center"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="strat_desc"><%=isOver ? ConvertString.toHtml(stratInfo.stratDesc):""%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="strat_ex_bene"><%= isOver? ConvertString.toHtml(stratInfo.stratExBene):""%></TEXTAREA> 
			</TD>			
		</TR>
<%	
	}
	stratDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="strat_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD align = "center"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="strat_desc"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="strat_ex_bene"></TEXTAREA> 
			</TD>			
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="strat_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingAdd.AddMoreStrategies")%> </a></p>
<BR>
<input type ="hidden" name = "reqType" value = ""/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingAdd.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingAdd.Cancel")%>" class="BUTTON" onclick="doCancel();">
</form>

<SCRIPT language="JavaScript">

var strat_nextHiddenIndex = <%=stratDisplayed + 1%>;
function addMoreRow() {
     getFormElement("strat_row" + strat_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 strat_nextHiddenIndex++;
	 if(strat_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("strat_addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(strat_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("strat_addMoreLink").style.display = "none";    
}

function addSubmit(){	
	if (checkValid()) {
		frmQualityStratAdd.reqType.value=<%=Constants.PL_STRATEGY_FOR_MEETING_ADD%>;
		frmQualityStratAdd.submit();
	} else return false;	
}

function doCancel(){
	frmQualityStratAdd.reqType.value =<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>;
	frmQualityStratAdd.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("strat_desc");
	var arrTxt1= document.getElementsByName("strat_ex_bene");
	
	var length = strat_nextHiddenIndex-1;
	var checkAllBlank = 0;
	
	for(i=0; i < length;i++) {
		if  (   trim(arrTxt[i].value) ==''
				&& trim(arrTxt1[i].value) ==''  
			) 
		{
			checkAllBlank++;				
		} else {
			if (trim(arrTxt[i].value) =='')  {
				alert("Description is mandatory");
				arrTxt[i].focus();
				return false;
			}
			
			if (trim(arrTxt1[i].value) =='')  {
				alert("Expected benefits information is mandatory");
				arrTxt1[i].focus();
				return false;
			}
		
			if(!maxLength(arrTxt[i],"Please input less than 600 characters",600))
			return false;			
			if(!maxLength(arrTxt1[i],"Please input less than 600 characters",600))
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
