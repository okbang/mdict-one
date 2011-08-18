<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.Vector,java.util.Calendar,com.fms1.tools.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>

<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	int right=Security.securiPage("Group home",request,response);
	Vector metrics=(Vector)request.getAttribute("metrics");
	int year=Integer.parseInt((String)request.getAttribute("year"));
	int month=Integer.parseInt((String)request.getAttribute("month"));

	String numBAGroup = (String) session.getAttribute("numBAGroup");

	Vector groupPoint = (Vector) request.getAttribute("groupPoint");
	GroupPointBAInfo gBaInfo1 = null, gBaInfo2 = null;
	double value1, value2;

	String lstMonth1 = (String) request.getAttribute("lastMonth1");
	String lstMonth2 = (String) request.getAttribute("lastMonth2");
%>
<TITLE>SQA Home</TITLE>
</HEAD>
<BODY  class="BD" onload="loadSQAMenu('Home');">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.SQAHome")%> </P>
<br>
<FORM action="Fms1Servlet?reqType=<%=Constants.HOME_SQA%>" name="frm" method="post">
<TABLE >
	<TBODY>
		<TR class="NormalText">
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Month")%> &nbsp; <SELECT name="month" class="COMBO">
					<%for(int i = 1; i <= 12; i++) {
					%><OPTION value="<%=i%>"<%=(i == month ? " selected" : "")%>><%=CommonTools.getMonth(i)%></OPTION>
					<%}%>
				</SELECT>
             &nbsp;<%=languageChoose.getMessage("fi.jsp.welcomeSQA.Year")%> &nbsp;
             <SELECT name="year" class="COMBO">
					<%
					int startYear = 2000;
					int endYear = Calendar.getInstance().get(Calendar.YEAR);
					String selected;
	            	for (int tmpyear =endYear;tmpyear>=startYear;tmpyear--){
	            		selected=(tmpyear==year)?" selected":"";
	            		%><OPTION value=<%=tmpyear+selected%>><%=tmpyear%></OPTION>
	                <%}%>
				</SELECT>
			</TD>
			<TD>
				<INPUT type="submit" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Go")%> ">
			</TD>
		</TR>
	</TBODY>
</TABLE>

<TABLE class="Table" width="95%" cellspacing="1">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.paramText(new String[]{"fi.jsp.welcomeSQA.SQA__Metrics__~PARAM1_MONTH~__~PARAM2_YEAR~",CommonTools.getMonth(month), String.valueOf(year)})%></CAPTION>
    <TBODY >
		<TR class="ColumnLabel">
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.ID")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Name")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Unit")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Norm")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Actual")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Deviation")%> </TD>
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
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.paramText(new String[]{"fi.jsp.welcomeSQA.SQA__Group__point__from__~PARAM1_MONTH~__to__~PARAM2_MONTH~",lstMonth2,lstMonth1})%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="180"> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Groupinformation")%> </TD>
			<TD><%=lstMonth2%></TD>
			<TD><%=lstMonth1%></TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Evolution")%> </TD><%
        		gBaInfo1=(GroupPointBAInfo)groupPoint.elementAt(1);
        		gBaInfo2=(GroupPointBAInfo)groupPoint.elementAt(0);
        		
        		value1 = gBaInfo1.timeLiness;
        		value2 = gBaInfo2.timeLiness;
			%>
		</TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Timeliness")%> </TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Responsetime")%> </TD><%
        		value1 = gBaInfo1.responseTime;
        		value2 = gBaInfo2.responseTime;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Sum")%> </B></TD><%
        		value1 = gBaInfo1.timeLiness + gBaInfo1.responseTime;
        		value2 = gBaInfo2.timeLiness + gBaInfo2.responseTime;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>

        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomeSQA.BudgetPerformance")%> </TD><%
        		value1 = gBaInfo1.budgetPerformance;
        		value2 = gBaInfo2.budgetPerformance;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomeSQA.BusyRate")%> </TD><%
        		value1 = gBaInfo1.busyRate;
        		value2 = gBaInfo2.busyRate;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Customersatisfaction")%> </TD><%
        		value1 = gBaInfo1.customerSatisfaction;
        		value2 = gBaInfo2.customerSatisfaction;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomeSQA.ValueAchievement")%> </TD><%
        		value1 = gBaInfo1.valueAchievement;
        		value2 = gBaInfo2.valueAchievement;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Sum1")%> </B></TD><%
        		value1 = gBaInfo1.budgetPerformance + gBaInfo1.busyRate + gBaInfo1.customerSatisfaction + gBaInfo1.valueAchievement;
        		value2 = gBaInfo2.budgetPerformance + gBaInfo2.busyRate + gBaInfo2.customerSatisfaction + gBaInfo2.valueAchievement;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>

        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomeSQA.LanguageIndex")%> </TD><%
        		value1 = gBaInfo1.languageIndex;
        		value2 = gBaInfo2.languageIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomeSQA.TechnologyIndex")%> </TD><%
        		value1 = gBaInfo1.technologyIndex;
        		value2 = gBaInfo2.technologyIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Sum2")%> </B></TD><%
        		value1 = gBaInfo1.languageIndex + gBaInfo1.technologyIndex;
        		value2 = gBaInfo2.languageIndex + gBaInfo2.technologyIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>

        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Total")%> </B></TD><%
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
            <TD> <%=languageChoose.getMessage("fi.jsp.welcomeSQA.Ranking")%> </TD><%
        		value1 = gBaInfo1.GroupRanking;
        		value2 = gBaInfo2.GroupRanking;
			%>
			<TD><%=CommonTools.formatDouble(value1) + "/" + numBAGroup%></TD>
			<TD><%=CommonTools.formatDouble(value2) + "/" + numBAGroup%></TD>
			<TD><%=CommonTools.formatDouble(value1 - value2)%></TD>
        </TR>
	</TBODY>
</TABLE>
</FORM>

<SCRIPT language="JavaScript">
	var objToHide=new Array(frm.year,frm.month);
</SCRIPT >
</HTML>

