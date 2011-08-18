<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%><%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>exportDeliverable.jsp</TITLE>
<STYLE type="text/css">
BODY {
    font-family: verdana;
	background-color: #ffffff;
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
	response.addHeader("Content-Disposition", "attachment;filename=DeliverableList.xls");
	Vector deliveryList = (Vector)session.getAttribute("deliveryList");
%>

<p></p>
<TABLE class="Table" cellspacing="1" cellpadding="1" border="1">
<CAPTION align="left" class="TableCaption"><B> </B></CAPTION>
	<TR class="ColumnLabel">
		
		<TD align="center"> Project_Code </TD>
		<TD align="center"> Customer </TD>
		<TD align="center">Deliverable</TD>
		<TD align="center"> First_Committed_Date </TD>
		<TD align="center"> Re_Committed_Date</TD>
		<TD align="center"> Actual_Date </TD>
		<TD align="center"> Status </TD>
		<TD align="center"> Note </TD>
	</TR>
	
	<%
		for (int i=0;i<deliveryList.size();i++) {
			Delivery temp = (Delivery)deliveryList.get(i);
	%>
		<TR>
			<TD><%=temp.getProjectCode()==null?"":temp.getProjectCode()%></TD>
			<TD><%=temp.getCustomerName()==null?"":temp.getCustomerName()%></TD>
			<TD><%=temp.getDeliverable()==null?"":temp.getDeliverable()%></TD>
			<TD><%=temp.getFirstCommitDate()==null?"":temp.getFirstCommitDate()%></TD>
			<TD><%=temp.getReCommitDate()==null?"":temp.getReCommitDate()%></TD>
			<TD><%=temp.getActualDate()==null?"":temp.getActualDate()%></TD>
			<TD><%=temp.getStatus()==null?"":temp.getStatus()%></TD>
			<TD><%=temp.getNote()==null?"":temp.getNote()%></TD>
		</TR>
	<%
		}
	%>
	
</TABLE>
<p></p>
