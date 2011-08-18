<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%><%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>estEffProcessExport.jsp</TITLE>
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
	response.addHeader("Content-Disposition", "attachment;filename=EstimateEffort.xls");
%>
<%
	boolean isMilestone=Integer.toString(Constants.MILESTONE_GET_PAGE).equals(request.getParameter("reqType"));
	int right2=Security.securiPage((isMilestone)?"Project reports":"Requirements",request,response);
	boolean isReplan="1".equals(session.getAttribute("replan"));
	Vector stageList2=(Vector)session.getAttribute("stageList");
	Vector planRCRList=(Vector)session.getAttribute("planRCRList");
	
	long pid = Long.parseLong((String) session.getAttribute("projectID"));
	final ProjectInfo pinf = Project.getProjectInfo(pid);

	Vector estEffortList =(Vector)session.getAttribute("estEffortList");
	Vector estEffortTotalList =(Vector)session.getAttribute("estEffortTotalList");
	final String sumTotal = (String)session.getAttribute("sumTotal");
	
	boolean isUpdate=false;
	boolean oneIsClosed=false;
	StageInfo milestoneInfo=null;
	if (isMilestone){
		isUpdate=false;
		oneIsClosed=true;
		milestoneInfo=(StageInfo)session.getAttribute("stageInfo");
	}
	else{
		isUpdate=("1".equals(request.getParameter("update")) && !"1".equals(request.getAttribute("updated")));
		oneIsClosed=isReplan?true:"1".equals((String)request.getAttribute("oneClosed"));
	}
	StageInfo stinf;
	StageInfo currentStinf=null;
	int currentStage=0;

	for (int i=0;i<stageList2.size();i++){
		currentStinf=(StageInfo)stageList2.elementAt(i);
		if ((isMilestone && currentStinf.milestoneID== milestoneInfo.milestoneID) 
		|| (currentStinf.aEndD==null && currentStage==0 ||i==stageList2.size()-1)){
			currentStage=i;
			break;
		}
	}
	
	// Reset parameter - Start
	oneIsClosed = false;
	// Reset parameter - End
	//===========================================
	
%>

<TABLE width="95%">
	<TR>
		<TD></TD>
		<TD colspan = 2><P class="TITLE"> <%=pinf.getProjectCode()%> </P></TD>
		<TD align="right" valign="bottom"></TD>
	</TR>
</TABLE>

<p></p>
<TABLE class="Table" cellspacing="1" width="95%">
<CAPTION align="left" class="TableCaption"><B><%=languageChoose.getMessage("fi.jsp.estEffort.EstEffort")%></B></CAPTION>
<TR class="ColumnLabel">
	<TD rowspan =2 width = "20" align="center" >#</TD>
	<TD rowspan =2> <%=languageChoose.getMessage("fi.jsp.estEffort.ActProcess")%></TD>
	<TD rowspan =2> <%=languageChoose.getMessage("fi.jsp.estEffort.TotalPd")%></TD>
	<TD rowspan =2> <%=languageChoose.getMessage("fi.jsp.estEffort.TotalPercen")%></TD>
	<%for (int i=0;i<stageList2.size();i++){
		stinf=(StageInfo)stageList2.elementAt(i);
	%><TD colspan=2><%=stinf.stage%></TD>
	<%}%>
</TR>
<TR class="ColumnLabel">
	<%for (int i=0;i<stageList2.size();i++){
		%>
		<TD width='40'> <%=languageChoose.getMessage("fi.jsp.estEffort.No")%> </TD>
		<TD width='40'> % </TD>
	<%}%>
</TR>
<%
boolean myflag=false;
String color=null;

double sumTotalValue = Double.parseDouble(sumTotal);

double[] arrNoEffort = new double[stageList2.size()]; // to calculate Total Number of Effort
double[] arrPercenEffort = new double[stageList2.size()]; // to calculate Total Percen of Effort

for (int i=0;i<stageList2.size();i++){
	arrNoEffort[i] = 0;
	arrPercenEffort[i] = 0;
}

EstEffortTotalInfo estEffTotalInf;
for (int i=0;i<estEffortTotalList.size();i++){
	estEffTotalInf = (EstEffortTotalInfo)estEffortTotalList.elementAt(i);
	color=myflag?"CellBGRnews":"CellBGR3";
%>
	<TR class='<%=color%>'>
		<TD><%=i+1%></TD>
		<TD><%=estEffTotalInf.processName%></TD>
		<TD><B><%=CommonTools.updateDouble(estEffTotalInf.totalPd)%></B></TD>
		<TD><%=(sumTotalValue > 0) ? CommonTools.formatDouble(estEffTotalInf.totalPd*100/sumTotalValue) : "N/A"%></TD>
		<%
		EstEffortInfo inf;
		int k=0;
		for (int j=0;j<stageList2.size();j++){
			stinf=(StageInfo)stageList2.elementAt(j);
			for (k=0;k<estEffortList.size();k++){
				inf=(EstEffortInfo)estEffortList.elementAt(k);
				if (inf.processName.equals(estEffTotalInf.processName) && inf.milestoneId==stinf.milestoneID){
				%>
				  <TD><%=CommonTools.formatDouble(inf.plannedValue)%></TD>
				  <TD><%=CommonTools.formatDouble(inf.plannedValue*100/estEffTotalInf.totalPd)%></TD>
				<%
				// Calculate Total Number of Effort - Start
				arrNoEffort[j] += inf.plannedValue;
				// Calculate Total Number of Effort - End		
				
				break;
				}
			}
		}%>
	</TR>
<%}%>
	<!-- Show Total in case view page only (isUpdate = false) -->
		<TR class="TableLeft">
			<TD></TD>
			<TD><B><%=languageChoose.getMessage("fi.jsp.DefectView.Total")%></B></TD>
			<TD><B><%=CommonTools.formatDouble(sumTotalValue)%></B></TD>
			<TD><B><%=(sumTotalValue > 0) ? "100" : "0"%></B>%</TD>
		
			<%for (int j=0;j<stageList2.size();j++){
			%>
			<TD><%=CommonTools.formatDouble(arrNoEffort[j])%></TD>
			<TD><%=(CommonTools.formatDouble(arrNoEffort[j]*100/sumTotalValue).equalsIgnoreCase("N/A")) ? "0" : CommonTools.formatDouble(arrNoEffort[j]*100/sumTotalValue)%>%</TD>
			<%
			}%>
		</TR>
</TABLE>
</BODY>
</HTML>
