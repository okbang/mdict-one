<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.infoclass.StageInfo,com.fms1.web.*,java.util.*,java.lang.*,java.io.*,java.text.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>grpPointUpdate.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadAdminMenu()" class="BD">

<%
	Vector groupPoint = (Vector) session.getAttribute("groupPoint");
	WorkUnitInfo wuInfo = (WorkUnitInfo) session.getAttribute("workUnit");
	int curYear = Integer.parseInt((String) session.getAttribute("curYear"));
	int curMonth = Integer.parseInt((String) session.getAttribute("curMonth"));
	String numOperationGroup = (String) session.getAttribute("numOperationGroup");
	
	int right = Security.securiPage("Org Point",request,response);
	
	if (wuInfo.type == wuInfo.TYPE_GROUP)
	{
%>

<P class="TITLE"><%=languageChoose.paramText(new String[]{"fi.jsp.grpPointUpdate.Group__~PARAM1_STRING~",wuInfo.workUnitName})%></P>
<%
	}
	else
	{
%>
<P class="TITLE"><%=wuInfo.workUnitName%></P>
<%
	}
%>

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.UPDATE_GROUP_METRIC%>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Year")%>  <INPUT size="4" type="text" maxlength="9"name="curYear" onchange="javascript:validateYear();" value="<%=curYear%>">&nbsp;<INPUT type="button" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.View")%> " onclick="doAction()" class="BUTTON">
 &nbsp;
 <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Selectamonthtoupdate")%>  
<SELECT name="month" class="COMBO">
	<%	
		for (int i = 1; i <= 12; i++) {
	%>
	<OPTION value="<%=i%>"<%=(i == curMonth ? "  Selected": "")%>><%=CommonTools.getMonth(i)%></OPTION>
	<%
		}
	%>
</SELECT>
</P>	
<INPUT type="hidden" name="wuID" value="<%=wuInfo.workUnitID%>">
<TABLE class="Table" cellspacing="1">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Updategrouppoint")%> </CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="120" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Groupmetrics")%> </TD>
			<TD class="ColumnLabel">Jan</TD>
			<TD class="ColumnLabel">Feb</TD>
			<TD class="ColumnLabel">Mar</TD>
			<TD class="ColumnLabel">Apr</TD>
			<TD class="ColumnLabel">May</TD>
			<TD class="ColumnLabel">Jun</TD>
			<TD class="ColumnLabel">Jul</TD>
			<TD class="ColumnLabel">Aug</TD>
			<TD class="ColumnLabel">Sep</TD>
			<TD class="ColumnLabel">Oct</TD>
			<TD class="ColumnLabel">Nov</TD>
			<TD class="ColumnLabel">Dec</TD>
		</TR>
		<TR>
			<TD class="CellBGRNews" colspan = "13"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Production")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Project")%> </TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(gInfo.Project)%></TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Proposal")%> </TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews">
				<INPUT size="6" type="text" maxlength="9"name="Proposal" onchange="javascript:validateNum(this);" value="<%=CommonTools.formatDouble(gInfo.Proposal)%>">
				</TD>
			<%
				}
			%>
		</TR>
		<TR>
			<TD class="CellBGRNews" colspan = "13"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Business")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Revenue")%> </TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews">
				<INPUT size="6" type="text" maxlength="9"name="Revenue" onchange="javascript:validateNum(this);" value="<%=CommonTools.formatDouble(gInfo.Revenue)%>">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.NetIncome")%> </TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews">
				<INPUT size="6" type="text" maxlength="9"name="NetIncome" onchange="javascript:validateNum(this);" value="<%=CommonTools.formatDouble(gInfo.NetIncome)%>">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Receivable")%> </TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews">
				<INPUT size="6" type="text" maxlength="9"name="Receivable" onchange="javascript:validateNum(this);" value="<%=CommonTools.formatDouble(gInfo.Receivable)%>">
				</TD>
			<%
				}
			%>
		</TR>
		<TR>
			<TD class="CellBGRNews" colspan = "13"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Staff")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Language")%> </TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews">
				<INPUT size="6" type="text" maxlength="9"name="Language" onchange="javascript:validateNum(this);" value="<%=CommonTools.formatDouble(gInfo.Language)%>">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Experience")%> </TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews">
				<INPUT size="6" type="text" maxlength="9"name="Experience" onchange="javascript:validateNum(this);" value="<%=CommonTools.formatDouble(gInfo.Experience)%>">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Turnover")%> </TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews">
				<INPUT size="6" type="text" maxlength="9"name="Turnover" onchange="javascript:validateNum(this);" value="<%=CommonTools.formatDouble(gInfo.Turnover)%>">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Technology")%> </TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews">
				<INPUT size="6" type="text" maxlength="9"name="Technology" onchange="javascript:validateNum(this);" value="<%=CommonTools.formatDouble(gInfo.Technology)%>">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Total")%> </B></TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(gInfo.Total)%></TD>
			<%
				}
			%>
		</TR>
<%
	if (wuInfo.type == wuInfo.TYPE_GROUP)
	{
%>
		<TR class="TableLeft">
			<TD width="80">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Ranking")%> </TD>
	        <%   
	        	for(int i=0;i<groupPoint.size();i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD><%=CommonTools.formatDouble(gInfo.GroupRanking)+ "/" + numOperationGroup%></TD>
			<%
				}
			%>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>

<br>

<%
if(right == 3)
{
%> 
<INPUT type="submit" name="btnUpdate" value=" <%=languageChoose.getMessage("fi.jsp.grpPointUpdate.Update")%> " class="BUTTON"">
<%
}
%>
<!--
<INPUT type="button" name="cancel" value="Cancel" class="BUTTON" onclick="jumpURL('orgPointUpdate.jsp');">
-->
</FORM>

<SCRIPT language="javascript">

function doAction()
{
	frm.reqType.value = <%=Constants.VIEW_GROUP_METRIC%>;
	frm.submit();  	  		
}

function validateYear()
{
	if(isNaN(frm.curYear.value) || (frm.curYear.value == "")){
 		alert("<%=languageChoose.getMessage("fi.jsp.grpPointUpdate.TheValueMustBeANumber")%>");
 		frm.curYear.focus();  		
		return;
	}	
	
  	if((frm.curYear.value < 0) || (frm.curYear.value < 2000)){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.grpPointUpdate.YearMustBeAfter2000")%>");
  		frm.curYear.focus();
  		return;
  	}	
}

function validateNum(obj)
{
	if(isNaN(obj.value) || (obj.value == "")){
 		alert("<%=languageChoose.getMessage("fi.jsp.grpPointUpdate.TheValueMustBeANumber")%>");
 		obj.focus();  		
		return;
	}	
}
	
</SCRIPT>
</BODY>
</HTML>
