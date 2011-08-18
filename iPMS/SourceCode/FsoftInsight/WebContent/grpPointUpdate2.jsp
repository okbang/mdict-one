<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.infoclass.group.*,com.fms1.web.*,java.util.*,java.lang.*,java.io.*,java.text.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>grpPointUpdate2.jsp</TITLE>
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
	Vector groupPoint = (Vector) session.getAttribute("vtPoint");
	WorkUnitInfo wuInfo = (WorkUnitInfo) session.getAttribute("workUnit");
	Vector vtgInfo = (Vector) session.getAttribute("vtGroups");

	int curYear = CommonTools.parseInt((String) session.getAttribute("curYear"));
	int curMonth = CommonTools.parseInt((String) session.getAttribute("curMonth"));
	
	int right = Security.securiPage("Org Point",request,response);
	int j = vtgInfo.size();
	int k = groupPoint.size();
	
%>

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Updatepointforgroups")%> </P>

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.UPDATE_GROUP_METRIC2%>">
<P class="TITLE">

 <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Month")%>  <SELECT name="curMonth" class="COMBO">
	<%	
		for (int i = 1; i <= 12; i++) {
	%>
	<OPTION value="<%=i%>"<%=(i == curMonth ? " selected" : "")%>><%=CommonTools.getMonth(i)%></OPTION>
	<%
		}
	%>
</SELECT>
&nbsp;  <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Year")%>  <INPUT size="4" type="text" maxlength="9" name="curYear" onchange="javascript:validateYear();" value="<%=curYear%>">
&nbsp; <INPUT type="button" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.View")%> " onclick="doAction()" class="BUTTON"></P>

<TABLE class="Table" cellspacing="1">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Updategrouppoint")%> </CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="120" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Groupmetrics")%> </TD>
			<%
			for (int i = 0; i<j;i++)
			{
				GroupInfo info = (GroupInfo) vtgInfo.elementAt(i);
				
				if (info.isOperation)
				{
			%>
			<TD class="ColumnLabel"><%=info.name%></TD>
			<INPUT type="hidden" name="wuID" value="<%=info.wuID%>">
			<%
				}
			}
			%>
		</TR>
		<TR>
			<TD class="CellBGRNews" colspan = "<%=k+1%>"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Production")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Project")%> </TD>
	        <%   
	        	for(int i=0;i<k;i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(gInfo.Project)%></TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Proposal")%> </TD>
	        <%   
	        	for(int i=0;i<k;i++)
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
			<TD class="CellBGRNews" colspan = "<%=k+1%>"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Business")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Revenue")%> </TD>
	        <%   
	        	for(int i=0;i<k;i++)
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
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.NetIncome")%> </TD>
	        <%   
	        	for(int i=0;i<k;i++)
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
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Receivable")%> </TD>
	        <%   
	        	for(int i=0;i<k;i++)
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
			<TD class="CellBGRNews" colspan = "<%=k+1%>"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Staff")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Language")%> </TD>
	        <%   
	        	for(int i=0;i<k;i++)
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
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Experience")%> </TD>
	        <%   
	        	for(int i=0;i<k;i++)
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
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Turnover")%> </TD>
	        <%   
	        	for(int i=0;i<k;i++)
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
			<TD width="80" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Technology")%> </TD>
	        <%   
	        	for(int i=0;i<k;i++)
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
			<TD width="80" class="CellBGR3"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Total")%> </B></TD>
	        <%   
	        	for(int i=0;i<k;i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(gInfo.Total)%></TD>
			<%
				}
			%>
		</TR>
		<TR class="TableLeft">
			<TD width="80">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Ranking")%> </TD>
	        <%   
	        	for(int i=0;i<k;i++)
	        	{
	        		GroupPointInfo gInfo=(GroupPointInfo)groupPoint.elementAt(i);
	        		
			%>
				<TD><%=CommonTools.formatDouble(gInfo.GroupRanking)+ "/" + k%></TD>
			<%
				}
			%>
		</TR>
	</TBODY>
</TABLE>

<br>

<%
if(right == 3)
{
%> 
<INPUT type="submit" name="btnUpdate" value=" <%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.Update")%> " class="BUTTON"">
<%
}
%>
<!--
<INPUT type="button" name="cancel" value="Cancel" class="BUTTON" onclick="jumpURL('orgPointUpdate.jsp');">
-->
</FORM>

<SCRIPT language="javascript">

//objs to hide when submenu is displayed
var objToHide = new Array(frm.curMonth, frm.curYear);

function doAction()
{
	frm.reqType.value = <%=Constants.UPDATE_GROUP_POINT2%>;
	frm.submit();  	  		
}

function validateYear()
{
	if(isNaN(frm.curYear.value) || (frm.curYear.value == "")){
 		alert("<%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.TheValueMustBeANumber")%>");
 		frm.curYear.focus();  		
		return;
	}	
	
  	if((frm.curYear.value < 0) || (frm.curYear.value < 2000)){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.YearMustBeAfter2000")%>");
  		frm.curYear.focus();
  		return;
  	}	
}

function validateNum(obj)
{
	if(isNaN(obj.value) || (obj.value == "")){
 		alert("<%=languageChoose.getMessage("fi.jsp.grpPointUpdate2.TheValueMustBeANumber")%>");
 		obj.focus();  		
		return;
	}	
}

</SCRIPT>
</BODY>
</HTML>