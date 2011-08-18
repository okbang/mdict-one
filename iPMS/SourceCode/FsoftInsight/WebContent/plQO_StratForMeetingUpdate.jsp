<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<script type="text/javascript" src="jscript/ajaxtooltip.js">
</script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plQO_StratForMeetingUpdate.jsp</TITLE>
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
	int stratDisplayed;
	
	Vector vStrat = (Vector) session.getAttribute("StratOfMeetingList");
	Vector vErrStrat = (Vector) request.getAttribute("ErrStratOfMeetingList");
	if (vErrStrat != null) {
		isOver = true;		
		request.removeAttribute("ErrStratOfMeetingList");
	}
	StrategyOfMeetingInfo stratInfo = null;	
	
	String strErr = (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingUpdate.UpdateStrategyForMeetingQualityObjectives")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Update failure ! System error</P>
<%
	} 
%>

<BR>
<form name ="frmQualityStratUpdate" method= "Post" action ="Fms1Servlet#MeetingStrat">
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingUpdate.Strategies")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD valign ="top" width="3%"> &nbsp; </TD>
			<TD width="3%" align = "center"> # </TD>
			<TD width="44%"> <%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingUpdate.Strategy")%>* </TD>
			<TD width="45%"> <%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingUpdate.ExpectedBenefits")%>* </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrStrat;
	else vTemp = vStrat;
	
	nRow = vTemp.size();
	for (; row < nRow ; row++) {
		stratInfo = (StrategyOfMeetingInfo) vTemp.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR class="<%=rowStyle%>" valign="top">
			<TD align = "center"> <A href="javascript:OnDelete(<%=stratInfo.stratID%>)"><IMG src="image/delete1.gif" title="Delete"></img></A></TD>
			<TD align = "center"> <%=row + 1%> 
				<input name ="stratID" type ="hidden" value="<%=stratInfo.stratID%>">
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="strat_desc"><%= (stratInfo.stratDesc ==null)? "":stratInfo.stratDesc%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="strat_ex_bene"><%=(stratInfo.stratExBene == null)? "":stratInfo.stratExBene%></TEXTAREA> 
			</TD>			
		</TR>
<%	
	}
	stratDisplayed = row;	// Indicate numbers of lines displayed	
%>		
	</TBODY>
</TABLE>
<BR>
<input type = "hidden" name = "reqType" value = ""/>
<input type = "hidden" name = "delStratID" value = ""/>

<INPUT type="button" name="plStratSave" value="<%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingUpdate.OK")%>" class="BUTTON" onclick="updateSubmit();">
<INPUT type="button" name="plStratCancel" value="<%=languageChoose.getMessage("fi.jsp.plQO_StratForMeetingUpdate.Cancel")%>" class="BUTTON" onclick="doBack()">
</form>

<SCRIPT language="JavaScript">
function OnDelete(paraStratID){
	if (window.confirm("Are you sure to delete this strategy ?")!=0) {
		frmQualityStratUpdate.reqType.value =<%=Constants.PL_STRATEGY_FOR_MEETING_DELETE%>;
		frmQualityStratUpdate.delStratID.value =paraStratID;
		frmQualityStratUpdate.submit();
	}
}
function updateSubmit(){	
	if (checkValid()) {
		frmQualityStratUpdate.reqType.value =<%=Constants.PL_STRATEGY_FOR_MEETING_UPDATE%>;
		frmQualityStratUpdate.submit();
	} else return false;	
}

function doBack(){
	frmQualityStratUpdate.reqType.value =<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>;
	frmQualityStratUpdate.submit();
}

function checkValid(){
	
	var arrTxt= document.getElementsByName("strat_desc");
	var arrTxt1= document.getElementsByName("strat_ex_bene");
	
	for(i=0; i < arrTxt.length;i++) {
		if (trim(arrTxt[i].value) =='')  {			
			alert("Description is mandatory !");
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
	return true;
}

</SCRIPT>
</BODY>
</HTML>
