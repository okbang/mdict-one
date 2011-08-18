<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%><%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>financePlanExport.jsp</TITLE>
<STYLE type="text/css">
BODY {
    font-family: verdana;
	background-color: #ccffcc;
}
.Title {
	color: navy;
	font-weight: bold;
	font-size: 16pt;
	margin-left: 0px;
	margin-top: 20px
}
.TableCaption {
	font-family: verdana, geneva, arial, helvetica, sans-serif;
	font-size: 13;
	text-align: left;
	font-weight: bold;
}
.ColumnLabel {
	background-color: #003366;
	color: white;
	font-weight: bold;
	vertical-align: middle;
	text-align: center;
	border-style: solid;
}
.ColumnLabel1 {
	background-color: #003366;
	color: white;
	font-weight: bold;
	vertical-align: middle;
	text-align: left;
	border-style: solid;
}
.TableFooter {
	background-color: #666699;
	color: white;
	vertical-align: middle;
	text-align: center;
}
.Table {
	border-style: solid;
	border-width: thin;
	text-align: left;
}
.ColumnLabel{
	border-width: thin;
	border-style: solid;
	vertical-align: top;
}
.CellBGRnews{
	border-width: thin;
	border-style: solid;
	vertical-align: top;
}
.CellBGR3{
	border-width: thin;
	border-style: solid;
	vertical-align: top;
}
</STYLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	response.addHeader("Content-Disposition", "attachment;filename=FinancePlan.xls");
%>
<%
	boolean isMilestone=Integer.toString(Constants.MILESTONE_GET_PAGE).equals(request.getParameter("reqType"));
	int right2=Security.securiPage((isMilestone)?"Project reports":"Requirements",request,response);
	boolean isReplan="1".equals(session.getAttribute("replan"));
	Vector durationList=(Vector)session.getAttribute("durationList");

	Vector financePlanList =(Vector)session.getAttribute("financePlanList");
	Vector financePlanTotalList =(Vector)session.getAttribute("financePlanTotalList");
	final String sumTotal = (String)session.getAttribute("sumTotal");
	
	boolean isUpdate=false;
	boolean oneIsClosed=false;

	isUpdate=("1".equals(request.getParameter("update")) && !"1".equals(request.getAttribute("updated")));
	oneIsClosed=isReplan?true:"1".equals((String)request.getAttribute("oneClosed"));

	DurationInfo durationInf;
	
	// Reset parameter - Start
	oneIsClosed = false;
	// Reset parameter - End

	long pid = Long.parseLong((String) session.getAttribute("projectID"));
	final ProjectInfo pinf = Project.getProjectInfo(pid);
	
%>

<TABLE width="95%">
	<TR>
		<TD></TD>
		<TD colspan = 3><P class="TITLE"> <%=pinf.getProjectCode()%> </P></TD>
		<TD align="right" valign="bottom"></TD>
	</TR>
</TABLE>

<p></p>

<TABLE class="Table" cellspacing="1">
<CAPTION align="left" class="TableCaption"><B><%=languageChoose.getMessage("fi.jsp.finance.ExportCaption")%></B></CAPTION>
<TR class="ColumnLabel">
	<TD rowspan =2 width = "20" align="center" >#</TD>
	<TD rowspan =2 > <%=languageChoose.getMessage("fi.jsp.finance.Item")%> </TD>
	<TD rowspan =2><%=languageChoose.getMessage("fi.jsp.finance.Total")%></TD>
	<TD rowspan =2><%=languageChoose.getMessage("fi.jsp.finance.BudgetPercen")%></TD>
	<TD align="center" colspan=<%=durationList.size()%> > <%=languageChoose.getMessage("fi.jsp.finance.BudgetTitle")%> </TD>
	<TD rowspan =2 > <%=languageChoose.getMessage("fi.jsp.finance.Note")%> </TD>
</TR>
<TR class="ColumnLabel">
	<%for (int i=0;i<durationList.size();i++){
	durationInf=(DurationInfo)durationList.elementAt(i);
		%>
		<TD width='40'> <%=durationInf.name%> </TD>

	<%}%>
</TR>
<%
boolean myflag=false;
String color=null;

double sumTotalValue = Double.parseDouble(sumTotal);

double[] arrNoEffort = new double[durationList.size()]; // to calculate Total Number of Effort
double[] arrPercenEffort = new double[durationList.size()]; // to calculate Total Percen of Effort

for (int i=0;i<durationList.size();i++){
	arrNoEffort[i] = 0;
	arrPercenEffort[i] = 0;
}

FinanceTotalInfo financeTotalInfo;
for (int i=0;i<financePlanTotalList.size();i++){
	financeTotalInfo = (FinanceTotalInfo)financePlanTotalList.elementAt(i);
	color=myflag?"CellBGRnews":"CellBGR3";
%>
	<TR class='<%=color%>'>
		<TD><%=i+1%></TD>
		<TD><%=financeTotalInfo.financeName%></TD>
		<TD><B><%=CommonTools.updateDouble(financeTotalInfo.totalBg)%></B></TD>
		<TD><%=(sumTotalValue > 0) ? CommonTools.formatDouble(financeTotalInfo.totalBg*100/sumTotalValue) : "N/A"%></TD>
		<%
						
		FinanceInfo inf;
		int k=0;
		for (int j=0;j<durationList.size();j++){
			durationInf=(DurationInfo)durationList.elementAt(j);
			for (k=0;k<financePlanList.size();k++){
				inf=(FinanceInfo)financePlanList.elementAt(k);
				if (inf.financeName.equals(financeTotalInfo.financeName) && inf.week==durationInf.week
					&& inf.month==durationInf.month&& inf.year==durationInf.year){
					%>
					<TD class="CellBGRnews"><div id='row<%=i%>stage<%=j%>'><%=(CommonTools.formatDouble(inf.plannedValue).equalsIgnoreCase("N/A")) ? "" : CommonTools.formatDouble(inf.plannedValue)%></div></TD>
					<%
				// Calculate Total Number of Effort - Start
				if (!Double.isNaN(inf.plannedValue)) {
					arrNoEffort[j] += inf.plannedValue;
				}
				// Calculate Total Number of Effort - End
				
				break;
				}
			}
		}%>
		<%
		if (isUpdate){%>
			<TD class="CellBGRnews"><input id ='note<%=i%>'' name='note<%=i%>' value='<%=financeTotalInfo.note%>' maxlength = '255' size='50'></TD>
		<%}else{%>
			<TD><%=ConvertString.toHtml(financeTotalInfo.note)%></TD>
		<%}%>
		
	</TR>
<%}%>
	<!-- Show Total in case view page only (isUpdate = false) -->
		<TR class="TableLeft">
			<TD></TD>
			<TD><B><%=languageChoose.getMessage("fi.jsp.DefectView.Total")%></B></TD>
			<TD><B><%=CommonTools.formatDouble(sumTotalValue)%></B></TD>
			<TD><B><%=(sumTotalValue > 0) ? "100" : "0"%></B>%</TD>
		
			<%for (int j=0;j<durationList.size();j++){
			%>
			<TD><B><%=(CommonTools.formatDouble(arrNoEffort[j]).equalsIgnoreCase("0")) ? "":CommonTools.formatDouble(arrNoEffort[j])%></B></TD>
			<%
			}%>
			<TD></TD>
		</TR>
</TABLE>
</BODY>
</HTML>