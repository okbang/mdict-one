<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<script type="text/javascript" src="jscript/ajaxtooltip.js">
</script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plReviewStrategyUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<link rel="stylesheet" href="stylesheet/AutoComplete.css" media="screen" type="text/css">
<script language="javascript" type="text/javascript" src="jscript/autocomplete.js"></script>
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
	int revDisplayed;
	
	Vector vRev = (Vector) session.getAttribute("ReviewStrategyList");
	Vector vErrRev = (Vector) request.getAttribute("ErrReviewStrategyList");
	if (vErrRev != null) {
		isOver = true;		
		request.removeAttribute("ErrReviewStrategyList");
	}
	ReviewStrategyInfo revInfo = null;	
	
	String strErr = (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyUpdate.UpdateReviewStrategies")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Update failure ! System error</P>
<%
	} 
%>

<BR>
<form name ="frmRevStratUpdate" method= "Post" action ="Fms1Servlet#ReviewStrat">
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyUpdate.Strategies")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="3%" valign ="top" > &nbsp; </TD>
			<TD width="3%" align = "center"> # </TD>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyUpdate.ReviewItem")%>* </TD>
			<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyUpdate.TypeOfReview")%>* </TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyUpdate.Reviewer")%>* </TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plReviewStrategyUpdate.When")%>* </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrRev;
	else vTemp = vRev;
	
	nRow = vTemp.size();
	for (; row < nRow ; row++) {
		revInfo = (ReviewStrategyInfo) vTemp.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR class="<%=rowStyle%>" valign="top">
			<TD align = "center"> <A href="javascript:OnDelete(<%=revInfo.revID%>)"><IMG src="image/delete1.gif" title="Delete"></img></A></TD>
			<TD align = "center"> <%=row + 1%> 
				<input name ="revID" type ="hidden" value="<%=revInfo.revID%>">
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="rev_item"><%= (revInfo.revItem ==null)? "":revInfo.revItem%></TEXTAREA> 
			</TD>
			<TD height="57">
				<INPUT size="25" type="text" maxlength="100" name="rev_type" id="rev_type<%=row%>" value="<%=revInfo.revType%>">
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="rev_reviewer"><%= (revInfo.revReviewer ==null)? "":revInfo.revReviewer%></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="rev_when"><%= (revInfo.revWhen ==null)? "":revInfo.revWhen%></TEXTAREA> 
			</TD>		
		</TR>
<%	
	}
	revDisplayed = row;	// Indicate numbers of lines displayed	
%>		
	</TBODY>
</TABLE>
<BR>
<input type = "hidden" name = "reqType" value = ""/>
<input type = "hidden" name = "delRevID" value = ""/>

<INPUT type="button" name="btnSave" value="<%=languageChoose.getMessage("fi.jsp.plReviewStrategyUpdate.OK")%>" class="BUTTON" onclick="updateSubmit();">
<INPUT type="button" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.plReviewStrategyUpdate.Cancel")%>" class="BUTTON" onclick="doBack()">
</form>

<SCRIPT language="JavaScript">
function OnDelete(paraRevID){
	if (window.confirm("Are you sure to delete this strategy ?")!=0) {
		frmRevStratUpdate.reqType.value =<%=Constants.PL_REVIEW_STRATEGY_DELETE%>;
		frmRevStratUpdate.delRevID.value =paraRevID;
		frmRevStratUpdate.submit();
	}
}
function updateSubmit(){	
	if (checkValid()) {
		frmRevStratUpdate.reqType.value =<%=Constants.PL_REVIEW_STRATEGY_UPDATE%>;
		frmRevStratUpdate.submit();
	} else return false;	
}

function doBack(){
	frmRevStratUpdate.reqType.value =<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>;
	frmRevStratUpdate.submit();
}

function checkValid(){
	
	var arrTxt= document.getElementsByName("rev_item");
	var arrTxt1= document.getElementsByName("rev_type");
	var arrTxt2= document.getElementsByName("rev_reviewer");
	var arrTxt3= document.getElementsByName("rev_when");

	for(i=0; i < arrTxt.length;i++) {		
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
	return true;
}
 data = ['<%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.GroupReview")%>',
 		 '<%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.OnePersonReview")%>',
 		 '<%=languageChoose.getMessage("fi.jsp.plReviewStrategyAdd.OtherType")%>'
 		];
	
<%
	for (int i = 0; i < nRow; i++) {
		out.write("AutoComplete_Create('rev_type" + i + "', data);");
	}
%>	
</SCRIPT>
</BODY>
</HTML>
