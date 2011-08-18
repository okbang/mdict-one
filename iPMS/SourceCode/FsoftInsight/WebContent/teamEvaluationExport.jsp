<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%><%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>teamEvaluationExport.jsp</TITLE>
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
.Header{
	background-color: #ccffcc;
	color: black;
	font-weight: bold;
	vertical-align: middle;
	text-align: center;
	border-style: none;
}
</STYLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	response.addHeader("Content-Disposition", "attachment;filename=TeamEvaluation.xls");
	List teamEvaluationMatric = (List) session.getAttribute("teamEvaluationMatric");
	long pid = Long.parseLong((String) session.getAttribute("projectID"));
	final ProjectInfo pinf = Project.getProjectInfo(pid);
%>

<table class="Header">
	<tr>
		<TD align="center" colspan=2 > FPT Software </TD>
		<TD align="center" colspan=6 > Social Republic of Vietnam <br> Independence - Freedom - Happiness </TD>
	</tr>
</table>
<p></p>
<table class="Header">
	<tr>
		<TD align="center" colspan=8 > <h3> LIST OF EMPLOYEES ENTITLED TO PROJECT ALLOWANCE </h3> </TD>
	</tr>
</table>
<p></p>
<table class="Header">
	<tr>
		<td align="left"> Project:  </td>
		<td align="left" colspan="4">  <%=pinf.getProjectName()%>  </td>
	</tr>
	<tr>
		<td align="left"  > Group:   </td>
		<td align="left" colspan="4" >  <%=pinf.getGroupName()%>  </td>
	</tr>
</table>
<p></p>
<TABLE class="Table" cellspacing="1" cellpadding="1" border="1">
<CAPTION align="left" class="TableCaption"><B> </B></CAPTION>
	<TR class="ColumnLabel">
		
		<TD rowspan =2 > No. </TD>
		<TD rowspan =2 > Employee ID </TD>
		<TD rowspan =2 >Name</TD>
		<TD rowspan =1 align="center" colspan=3 > Evaluation </TD>
		<TD rowspan =2 > Allowance </TD>
		<TD rowspan =2 > Notes </TD>
	</TR>
	<TR class="ColumnLabel">
		<TD rowspan="1"  align="center">&nbsp;&nbsp;Role&nbsp;&nbsp;</TD>
		<TD rowspan="1"  align="center">&nbsp;&nbsp;% Effort&nbsp;&nbsp;</TD>
		<TD rowspan="1"  align="center">&nbsp;&nbsp;Efficiency&nbsp;&nbsp;</TD>
	</TR>
	<% for (int i=0;i<teamEvaluationMatric.size();i++) {
        	TeamEvaluationInfo temp = (TeamEvaluationInfo)teamEvaluationMatric.get(i);
        	String tempRole = "";
        	if (temp.getRole().length == 1) tempRole = temp.getRole()[0];
        	else {
        		tempRole = temp.getRole()[0];
        		for (int j=1;j<temp.getRole().length;j++)
        			tempRole = tempRole + "+" + temp.getRole()[j];
        	}
        	String tempHp = "";
        	try {
        		//tempHp = temp.getHq();
        		if (tempHp==null) tempHp=""; 
        	}
        	catch(Exception e){
        	}
        %>
	        <tr>
	        	<td> <%=i+1%> </td>
	        	<td> <%=temp.getStaffID()==null?"":temp.getStaffID()%> </td>
	        	<td> <%=temp.getName()%> </td>
	        	<td> <%=tempRole%> </td>
	        	<td> <%=temp.getPercentAttend()%> %</td>
	        	<td> <%=tempHp%> </td>
	        	<td> <%=temp.getPc()%> </td>
	        </tr>
	     <%}%>
	     <tr >
	     	<%
	     		float sumPC = 0 ; 
	     		for (int i=0;i<teamEvaluationMatric.size();i++) {
	     			TeamEvaluationInfo temp = (TeamEvaluationInfo)teamEvaluationMatric.get(i);
	     			if (temp.getPc()!=-1)
	     				sumPC+=temp.getPc();	
	     		}
	     	%>
	     	<td> </td>
	     	<td colspan="2" align="center"> <b> Total </b> </td>
	     	<td> </td>
	     	<td> </td>
	     	<td> </td>
	     	<td><b><%=sumPC%></b> </td>
	     	<td> </td>
	     </tr>
</TABLE>
<p></p>
<table class="Header">
	<tr>
		<td align="center" colspan=3 > Group Director </td>
		<td align="center" colspan=5 > Project Manager </td>
	</tr>
</table
