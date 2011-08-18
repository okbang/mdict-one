<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.infoclass.group.*,com.fms1.web.*,java.util.*,java.lang.*,java.io.*,java.text.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>grpPointUpdate3.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<BODY onload="loadAdminMenu(); doFocus();" class="BD">
<%
	Vector groupPoint = (Vector) session.getAttribute("vtPoint");
	WorkUnitInfo wuInfo = (WorkUnitInfo) session.getAttribute("workUnit");
	Vector vtgInfo = (Vector) session.getAttribute("vtGroups");

	int curYear = CommonTools.parseInt((String) session.getAttribute("curYear"));
	int curMonth = CommonTools.parseInt((String) session.getAttribute("curMonth"));
	
	int right = Security.securiPage("Org Point",request,response);
	int j = vtgInfo.size();
	int k = groupPoint.size();

	String strStartDate = 01 + "-" + curMonth + "-" + curYear;
	String strEndDate   = CommonTools.getNoDay(curMonth, curYear) + "-" + curMonth + "-" + curYear;
%>

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Updatepointforgroups")%> </P>
<FORM name="frmPointBA" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.UPDATE_GROUP_METRIC3%>">
<INPUT type="hidden" name="strStartDate" value="<%=strStartDate%>">
<INPUT type="hidden" name="strEndDate" value="<%=strEndDate%>">
<P class="TITLE">
 <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Month")%>  
<SELECT name="curMonth" class="COMBO" onchange="javascript:doValidateDate();">
<%for (int i = 1; i <= 12; i++) 
  {
%>
	<OPTION value="<%=i%>"<%=(i == curMonth ? " selected" : "")%>><%=CommonTools.getMonth(i)%></OPTION>
<%
  }
%>
</SELECT> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Year")%>  <INPUT type="text" name="curYear" onblur="javascript:doValidateDate();" value="<%=curYear%>" size="4" maxlength="4">
&nbsp; <INPUT type="button" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.View")%> " onclick="doSubmit();" class="BUTTON"></P>
<TABLE class="Table" cellspacing="1">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Updategrouppoint")%> </CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Groupmetrics")%> </TD>
			<%
			for (int i = 0; i<j; i++)
			{
				GroupInfo info = (GroupInfo) vtgInfo.elementAt(i);
				if (!info.isOperation)
				{
			%>
			<TD class="ColumnLabel"><%=info.name%>
				<INPUT type="hidden" name="wuID" value="<%=info.wuID%>">
			</TD>
			
			<%
				}
			}
			%>
		</TR>
		<TR class="CellBGRLongNews">
			<TD class="CellBGR3Long">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Timeliness")%> </TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String timeLiness = CommonTools.formatDouble(gInfo.timeLiness);
			%>
				<TD class="CellBGRLongNews">
				<INPUT size="6" type="text" name="timeLiness" onblur="javascript:validateNum(this);" value="<%=timeLiness%>" maxlength="9">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRLongNews">
			<TD class="CellBGR3Long">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Responsetime")%> </TD>
	        <%  
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
			        String responseTime =(String) CommonTools.formatDouble(gInfo.responseTime);
			%>
				<TD class="CellBGRLongNews">
				<INPUT size="6" type="text" name="responseTime" onblur="javascript:validateNum(this);" value="<%=responseTime%>" maxlength="9" Disabled>
				</TD>
			<%
				}
			%>
		</TR>
		<TR>
			<TD width="80" class="CellBGR3"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Sum")%> </B></TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String sumFirst = CommonTools.formatDouble(gInfo.sumFirst);
	        		
			%>
				<TD class="CellBGRNews"><%=sumFirst%></TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRLongNews">
			<TD class="CellBGR3Long">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.BudgetPerformance")%> </TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String budgetPerformance = CommonTools.formatDouble(gInfo.budgetPerformance);
			%>
				<TD class="CellBGRLongNews">
				<INPUT size="6" type="text" name="budgetPerformance" onblur="javascript:validateNum(this);" value="<%=budgetPerformance%>" maxlength="9">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRLongNews">
			<TD class="CellBGR3Long">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.BusyRate")%> </TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String busyRate = CommonTools.formatDouble(gInfo.busyRate);
			%>
				<TD class="CellBGRLongNews">
				<INPUT size="6" type="text" name="busyRate" onblur="javascript:validateNum(this);" value="<%=busyRate%>" maxlength="9">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRLongNews">
			<TD class="CellBGR3Long">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Customersatisfaction")%> </TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String customerSatisfaction = CommonTools.formatDouble(gInfo.customerSatisfaction);
			%>
				<TD class="CellBGRLongNews">
				<INPUT size="6" type="text" name="customerSatisfaction" onblur="javascript:validateNum(this);" value="<%=customerSatisfaction%>" maxlength="9">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRLongNews">
			<TD class="CellBGR3Long">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.ValueAchievement")%> </TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String valueAchievement = CommonTools.formatDouble(gInfo.valueAchievement);
			%>
				<TD class="CellBGRLongNews">
				<INPUT size="6" type="text" name="valueAchievement" onblur="javascript:validateNum(this);" value="<%=valueAchievement%>" maxlength="9">
				</TD>
			<%
				}
			%>
		</TR>
		<TR>
			<TD width="80" class="CellBGR3"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Sum1")%> </B></TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String sumSecond = CommonTools.formatDouble(gInfo.sumSecond);
	        		
			%>
				<TD class="CellBGRNews"><%=sumSecond%></TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRLongNews">
			<TD class="CellBGR3Long">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.LanguageIndex")%> </TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String languageIndex = CommonTools.formatDouble(gInfo.languageIndex);
			%>
				<TD class="CellBGRLongNews">
				<INPUT size="6" type="text" name="languageIndex" onblur="javascript:validateNum(this);" value="<%=languageIndex%>" maxlength="9">
				</TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRLongNews">
			<TD class="CellBGR3Long">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.TechnologyIndex")%> </TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String technologyIndex = CommonTools.formatDouble(gInfo.technologyIndex);
			%>
				<TD class="CellBGRLongNews">
				<INPUT size="6" type="text" name="technologyIndex" onblur="javascript:validateNum(this);" value="<%=technologyIndex%>" maxlength="9">
				</TD>
			<%
				}
			%>
		</TR>
		<TR>
			<TD width="80" class="CellBGR3"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Sum2")%> </B></TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String sumThird = CommonTools.formatDouble(gInfo.sumThird);
	        		
			%>
				<TD class="CellBGRNews"><%=sumThird%></TD>
			<%
				}
			%>
		</TR>
		<TR class="CellBGRLongNews">
			<TD class="CellBGR3Long"><B> <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Total")%> </B></TD>
	        <%
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		String Total = CommonTools.formatDouble(gInfo.Total);
	        		
			%>
				<TD class="CellBGRNews"><%=Total%></TD>
			<%
				}
			%>

		</TR>
		<TR class="TableLeft">
			<TD >&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Ranking")%> </TD>
	        <%   
	        	for(int i=0; i<k; i++)
	        	{
	        		GroupPointBAInfo gInfo=(GroupPointBAInfo)groupPoint.elementAt(i);
	        		
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
<INPUT type="submit" name="btnUpdate" value=" <%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.Update")%> " class="BUTTON"">
<%
}
%>
<!--
<INPUT type="button" name="btnCancel" value="Cancel" class="BUTTON" onclick="jumpURL('orgPointUpdate.jsp');">
-->
</FORM>
<SCRIPT language="javascript">

	//objs to hide when submenu is displayed
	var objToHide = new Array(frmPointBA.curMonth, frmPointBA.curYear);

	function doFocus() 
	{
		document.frmPointBA.curYear.focus();	
	}
	
	function doSubmit()
	{
	  	document.frmPointBA.reqType.value = <%=Constants.UPDATE_GROUP_POINT3%>;
		document.frmPointBA.submit();
	}
	
	function DaysInMonth(WhichMonth, WhichYear)
	{
		var DaysInMonth = 31;
		if (WhichMonth == "4" || WhichMonth == "6" || WhichMonth == "9" || WhichMonth == "11") DaysInMonth = 30;
		if (WhichMonth == "2" && (WhichYear/4) != Math.floor(WhichYear/4))	DaysInMonth = 28;
		if (WhichMonth == "2" && (WhichYear/4) == Math.floor(WhichYear/4))	DaysInMonth = 29;
		return DaysInMonth;
	}

	function doValidateDate() {
		var scrDay;
		var scrMonth;
		var scrYear;
	
		for (i=0; i<document.forms[0].curMonth.length; i++)
		{
			if ((document.forms[0].curMonth.options[i].selected) && (i >0))
			{
				//scrMonthID = document.forms[0].curMonth.options[i].value;
				scrMonth = document.forms[0].curMonth.options[i].text;
				break;
			}
		}		
		scrYear = document.frmPointBA.curYear.value;
		scrDay = DaysInMonth(scrMonth, scrYear);

		document.frmPointBA.strStartDate.value = 01 +"-"+ scrMonth +"-"+ scrYear;
		document.frmPointBA.strEndDate.value = scrDay +"-"+ scrMonth +"-"+ scrYear;
	
		if(isNaN(document.frmPointBA.curYear.value) || (document.frmPointBA.curYear.value == "")) {
	 		alert("<%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.TheValueMustBeANumber")%>");
	 		document.frmPointBA.curYear.focus();
			return false;
		}
	  	else if((document.frmPointBA.curYear.value < 0) || (document.frmPointBA.curYear.value < 2000)) {
	  		window.alert("<%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.YearMustBeAfter2000")%>");
	  		document.frmPointBA.curYear.focus();
	  		return false;
	  	}
		return true;
	}

	function validateNum(obj)
	{
		if(isNaN(obj.value) || (obj.value == "")){
	 		alert("<%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.TheValueMustBeNumberic")%>");
	 		obj.focus();
			return false;
		}
		else
		{
			if(obj.value > 300){
		 		alert("<%=languageChoose.getMessage("fi.jsp.grpPointUpdate3.TheValueMustBeLessThan300")%>");
		 		obj.focus();
				return false;
			}
		}
		return true;
	}
</SCRIPT>
</BODY>
</HTML>
