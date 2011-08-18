<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.* ,com.fms1.common.group.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<TITLE>pcbInformation.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
int right = Security.securiPage("Organization PCB",request,response);
PCBGalInfo pcbGalInfo = (PCBGalInfo)session.getAttribute("pcbGalInfo");
String projects=ConvertString.arrayToString(pcbGalInfo.projectCodes,", ");
String metrics=ConvertString.arrayToString(pcbGalInfo.metrics,", ");
int nWorkUnitType = Integer.parseInt((String)session.getAttribute("workUnitType"));
%>
<BODY class="BD" onload="<%=CommonTools.getMnuFunc(session)%>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.PCBreportGeneralinformation")%> </P>
<P class="NormalText"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.NoteTheothersubpagesofPCBrepor")%>  </P>
<FORM name ="frmGalInfo" method ="POST" action="Fms1Servlet?reqType=<%=Constants.PCB_SAVEINFO%>">
<TABLE class="Table" cellspacing="1" width="95%">
	<TBODY>
		<TR>
			<TD colspan="2" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.Generalinformation")%> </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.Reportname")%> </TD>
			<TD class="CellBGRnews"><%=((pcbGalInfo.reportName==null)?"N/A":pcbGalInfo.reportName)%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.Period")%> </TD>
			<TD class="CellBGRnews"> <%=languageChoose.paramText(new String[]{"fi.jsp.pcbInformation.From~PARAM1_START_DATE~to~PARAM2_END_DATE~",CommonTools.dateFormat(pcbGalInfo.startDate),CommonTools.dateFormat(pcbGalInfo.endDate)})%> </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.Author")%> </TD>
			<TD class="CellBGRnews"><%=pcbGalInfo.author%></TD>
		</TR>

		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.Reportdate")%> </TD>
			<TD class="CellBGRnews"><%=CommonTools.dateFormat(pcbGalInfo.reportDate)%></TD>
		</TR>
		
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.Projects")%> </TD>
			<TD class="CellBGRnews"><%=projects%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.Reporttype")%> </TD>
			<TD class="CellBGRnews"><%=pcbGalInfo.reportTypeComment%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.Metricsincluded")%> </TD>
			<TD class="CellBGRnews"><%=metrics%></TD>
		</TR>
		<TR>
			<TD colspan="2" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbInformation.Methodology")%> </TD>
		</TR>
		<TR>
			<TD colspan="2" class="CellBGRnews">
			<%
			if(pcbGalInfo.methodology==null)
				pcbGalInfo.methodology="";
			if (right==3){
			%><TEXTAREA rows="4" cols="80" name="txtMethodology"><%=pcbGalInfo.methodology%></TEXTAREA>
			<%}else{%><%=ConvertString.toHtml(pcbGalInfo.methodology)%><%}%></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<P>	
<%if (right==3)	{%>
<INPUT	type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.pcbInformation.Save")%>" class="BUTTON" onClick="saveInfo()">
<INPUT	type="button" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.pcbInformation.DeletePCBreport")%>" class="BUTTONWIDTH" onClick="delPCB()">
<%}%>
</P>
</BODY>
<SCRIPT language="JavaScript">
function saveInfo(){
	if (maxLength(frmGalInfo.txtMethodology,"<%=languageChoose.getMessage("fi.jsp.pcbInformation.Methodology")%>",2000))
		frmGalInfo.submit();
}
function delPCB(){
		if (confirm("<%= languageChoose.getMessage("fi.jsp.pcbInformation.AreYouSureToDeleteThisPcbReport")%>")){
			frmGalInfo.action="Fms1Servlet?reqType=<%=Constants.PCB_DELETE%>";
			frmGalInfo.submit();
		}	
}
</SCRIPT>
</HTML>
