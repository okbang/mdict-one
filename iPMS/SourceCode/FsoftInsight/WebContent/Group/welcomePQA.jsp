<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.*,com.fms1.tools.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>

<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="JavaScript">
        <%@ include file="../javaFns.jsp"%>
</SCRIPT>
<%
	int right=Security.securiPage("Group home",request,response);
	Vector metrics=(Vector)request.getAttribute("metrics");
	Date fromDate=(Date)request.getAttribute("fromDate");
	Date toDate=(Date)request.getAttribute("toDate");
	String fromDateStr =CommonTools.dateUpdate(fromDate);
	String toDateStr =CommonTools.dateUpdate(toDate);

	String numBAGroup = (String) request.getAttribute("numBAGroup");
	
	Vector groupPoint = (Vector) request.getAttribute("groupPoint");
	GroupPointBAInfo gBaInfo1 = null, gBaInfo2 = null;
	double value1, value2;

	String lstMonth1 = (String) request.getAttribute("lastMonth1");
	String lstMonth2 = (String) request.getAttribute("lastMonth2");

%>
<TITLE>PQA Home</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY  class="BD" onload="loadSQAMenu('Home');">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.welcomePQA.PQAHome")%> </P>
<br>
<FORM action="Fms1Servlet?reqType=<%=Constants.HOME_PQA%>" name="frm" method="post">
<TABLE >
	<TBODY>
		<TR class="NormalText">
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Fromdate")%> </TD>
			<TD NOWRAP><INPUT size="9" type="text" maxlength="9" name="fromDate" value="<%=fromDateStr%>">(DD-MMM-YY)</TD>
		</TR>
		<TR class="NormalText">
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Todate")%> </TD>
			<TD NOWRAP><INPUT size="9" type="text" maxlength="9" name="toDate" value="<%=toDateStr%>">(DD-MMM-YY)</TD>
			<TD colspan =4>
				<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.welcomePQA.Go")%> " onclick="dofilter()">
			</TD>
		</TR>
			
	</TBODY>
</TABLE>
</FORM>
<TABLE class="Table" width="95%" cellspacing="1">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.paramText(new String[]{"fi.jsp.welcomePQA.PQA__Metrics__from__~PARAM1_DATE~__to__~PARAM2_DATE~",fromDateStr,toDateStr})%></CAPTION>
    <TBODY >
		<TR class="ColumnLabel">
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomePQA.ID")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Name")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Unit")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Norm")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Actual")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Deviation")%> </TD>
		</TR>
	<%MetricInfo inf;
		for (int i=0;i<metrics.size();i++){
			inf=(MetricInfo)metrics.elementAt(i);
		%><TR class="CellBGRnews">
			<TD><%=inf.id%></TD>
			<TD><%=languageChoose.getMessage(inf.name)%></TD>
			<TD><%=inf.unit%></TD>
			<TD><%=CommonTools.formatDouble(inf.plannedValue)%></TD>
			<TD><%=CommonTools.formatDouble(inf.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(inf.deviation)%></TD>
		</TR>
        <%}%>
          </TBODY> </TABLE>

<P><BR></P>

<TABLE class="Table" cellspacing="1">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.paramText(new String[]{"fi.jsp.welcomePQA.PQA__Group__point__from__~PARAM1_MONTH~__to__~PARAM2_MONTH~",lstMonth2,lstMonth1})%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="180"> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Groupinformation")%> </TD>
			<TD><%=lstMonth2%></TD>
			<TD><%=lstMonth1%></TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Evolution")%> </TD><%
        		gBaInfo1=(GroupPointBAInfo)groupPoint.elementAt(1);
        		gBaInfo2=(GroupPointBAInfo)groupPoint.elementAt(0);
        		
        		value1 = gBaInfo1.timeLiness;
        		value2 = gBaInfo2.timeLiness;
			%>
		</TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePQA.Timeliness")%> </TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePQA.Responsetime")%> </TD><%
        		value1 = gBaInfo1.responseTime;
        		value2 = gBaInfo2.responseTime;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Sum")%> </B></TD><%
        		value1 = gBaInfo1.timeLiness + gBaInfo1.responseTime;
        		value2 = gBaInfo2.timeLiness + gBaInfo2.responseTime;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>

        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePQA.BudgetPerformance")%> </TD><%
        		value1 = gBaInfo1.budgetPerformance;
        		value2 = gBaInfo2.budgetPerformance;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePQA.BusyRate")%> </TD><%
        		value1 = gBaInfo1.busyRate;
        		value2 = gBaInfo2.busyRate;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePQA.Customersatisfaction")%> </TD><%
        		value1 = gBaInfo1.customerSatisfaction;
        		value2 = gBaInfo2.customerSatisfaction;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePQA.ValueAchievement")%> </TD><%
        		value1 = gBaInfo1.valueAchievement;
        		value2 = gBaInfo2.valueAchievement;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Sum1")%> </B></TD><%
        		value1 = gBaInfo1.budgetPerformance + gBaInfo1.busyRate + gBaInfo1.customerSatisfaction + gBaInfo1.valueAchievement;
        		value2 = gBaInfo2.budgetPerformance + gBaInfo2.busyRate + gBaInfo2.customerSatisfaction + gBaInfo2.valueAchievement;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>

        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePQA.LanguageIndex")%> </TD><%
        		value1 = gBaInfo1.languageIndex;
        		value2 = gBaInfo2.languageIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePQA.TechnologyIndex")%> </TD><%
        		value1 = gBaInfo1.technologyIndex;
        		value2 = gBaInfo2.technologyIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Sum2")%> </B></TD><%
        		value1 = gBaInfo1.languageIndex + gBaInfo1.technologyIndex;
        		value2 = gBaInfo2.languageIndex + gBaInfo2.technologyIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>

        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Total")%> </B></TD><%
        		value1 = gBaInfo1.timeLiness + gBaInfo1.responseTime+ 
        		         gBaInfo1.budgetPerformance + gBaInfo1.busyRate +
        		         gBaInfo1.customerSatisfaction + gBaInfo1.valueAchievement +
        		         gBaInfo1.languageIndex + gBaInfo1.technologyIndex;
        		value2 = gBaInfo2.timeLiness + gBaInfo2.responseTime +
        		         gBaInfo2.budgetPerformance + gBaInfo2.busyRate +
        		         gBaInfo2.customerSatisfaction + gBaInfo2.valueAchievement +
        		         gBaInfo2.languageIndex + gBaInfo2.technologyIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR class="TableLeft" >
            <TD> <%=languageChoose.getMessage("fi.jsp.welcomePQA.Ranking")%> </TD><%
        		value1 = gBaInfo1.GroupRanking;
        		value2 = gBaInfo2.GroupRanking;
			%>
			<TD><%=CommonTools.formatDouble(value1) + "/" + numBAGroup%></TD>
			<TD><%=CommonTools.formatDouble(value2) + "/" + numBAGroup%></TD>
			<TD><%=CommonTools.formatDouble(value1 - value2)%></TD>
        </TR>
	</TBODY>
</TABLE>

<SCRIPT language="JavaScript">
	var objToHide=new Array(frm.fromDate,frm.toDate);
	function dofilter(){
		if (mandatoryDateFld(frm.fromDate,"<%= languageChoose.getMessage("fi.jsp.welcomePQA.Fromdate")%>"))
		if (mandatoryDateFld(frm.toDate,"<%= languageChoose.getMessage("fi.jsp.welcomePQA.toDate")%>"))
		if (compareDate(frm.fromDate.value, frm.toDate.value)==1)
			frm.submit();
		else{
			window.alert("<%= languageChoose.getMessage("fi.jsp.welcomePQA.Fromdatemustbebeforetodate")%>");
  			frm.fromDate.focus();  		
   		}
	}
</SCRIPT >
</HTML>

