<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.* ,java.util.Vector" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="JavaScript">
        <%@ include file="../javaFns.jsp"%>
</SCRIPT>
<TITLE>pcbComments.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
int right = Security.securiPage("Organization PCB",request,response);
Vector metricList=(Vector)session.getAttribute("pcbMetrics");
PCBGalInfo pcbGalInfo = (PCBGalInfo)session.getAttribute("pcbGalInfo");
//Classify metric above/below control limit
Vector metricAboveUCL=new Vector();
Vector metricBelowLCL=new Vector();
for (int i =0; i<metricList.size();i++){
	PCBMetricInfo metricInfo =(PCBMetricInfo)metricList.elementAt(i);
	if (metricInfo.actualAvg<metricInfo.normLCL)
		metricBelowLCL.add(metricInfo);
	else if(metricInfo.actualAvg>metricInfo.normUCL)
		metricAboveUCL.add(metricInfo);
}
int nWorkUnitType = Integer.parseInt((String)session.getAttribute("workUnitType"));
%>
<BODY class="BD" onload="<%=CommonTools.getMnuFunc(session)%>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.pcbComments.PCBreportComments")%> </P>
<P  align="right"></P>
<FORM name="frmComments" method ="POST" action="Fms1Servlet?reqType=<%=Constants.PCB_SAVECOMMENTS%>">
<TABLE cellspacing="1 "class="Table" width="95%">
	<TBODY>
		<TR>
			<TD class="ColumnLabel" width="20%" > <%=languageChoose.getMessage("fi.jsp.pcbComments.Period")%> </TD>
			<TD class="CellBGRnews"  > <%=languageChoose.paramText(new String[]{"fi.jsp.pcbComments.from~PARAM1_START_DATE~to~PARAM2_END_DATE~",CommonTools.dateFormat(pcbGalInfo.startDate),CommonTools.dateFormat(pcbGalInfo.endDate)})%> </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbComments.Reporttype")%> </TD>
			<TD class="CellBGRnews"  ><%=pcbGalInfo.reportTypeComment%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbComments.General")%> </TD>
			<TD class="CellBGRnews" ><%if (right==3){%><TEXTAREA rows="6" cols="60" name="txtGeneral"><%=((pcbGalInfo.galComment==null)?"":pcbGalInfo.galComment)%></TEXTAREA><%}else{%><%=((pcbGalInfo.galComment==null)?"":ConvertString.toHtml(pcbGalInfo.galComment))%><%}%></TD>
		</TR>
		<!-- Metrics higher than UCL -->
		
		<TR class="ColumnLabel">
			<TD rowspan="<%=metricBelowLCL.size()+metricAboveUCL.size()+2%>" > <%=languageChoose.getMessage("fi.jsp.pcbComments.Analysis")%> </TD>
			<TD ><%=((metricAboveUCL.size()==0)?languageChoose.getMessage("fi.jsp.pcbComments.NometricswithvalueshigherthanUCLofnorm"):languageChoose.getMessage("fi.jsp.pcbComments.Metric(s)withvalueshigherthanUCLofnorm"))%> </TD>
		</TR>
		<%
		for (int i =0;i<metricAboveUCL.size();i++){
			PCBMetricInfo metricInfo =(PCBMetricInfo)metricAboveUCL.elementAt(i);
		%><TR class="CellBGRnews">
			<TD>-<%=languageChoose.getMessage(metricInfo.name)%>:<BR>
			<%if (right==3){%><INPUT type="text" name="txtMetricUCL<%=i%>" size="60" maxlength="200" value="<%=((metricInfo.analysis==null)?"":metricInfo.analysis)%>">
			<%}else{%><%=((metricInfo.analysis==null)?"":metricInfo.analysis)%><%}%>
			</TD>
		</TR>
		<%}%>
		<!-- Metrics lower than LCL -->
		<TR>
			<TD  class="ColumnLabel"><%=((metricBelowLCL.size()==0)? languageChoose.getMessage("fi.jsp.pcbComments.NometricswithvalueslowerthanLCLofnorm"):languageChoose.getMessage("fi.jsp.pcbComments.Metric(s)withvalueslowerthanLCLofnorm"))%> </TD>
		</TR>
		<%
		for (int i =0;i<metricBelowLCL.size();i++){
			PCBMetricInfo metricInfo =(PCBMetricInfo)metricBelowLCL.elementAt(i);
		%><TR class="CellBGRnews">
			<TD>-<%=languageChoose.getMessage(metricInfo.name)%>:<BR>
			<%if (right==3){%><INPUT type="text" name="txtMetricLCL<%=i%>" size="60" maxlength="200" value="<%=((metricInfo.analysis==null)?"":metricInfo.analysis)%>">
			<%}else{%><%=((metricInfo.analysis==null)?"":metricInfo.analysis)%><%}%>
			</TD>
		</TR>
		<%}%>
		<TR>
	</TBODY>
</TABLE>
</FORM>
<BR>
<%if (right==3)	{%>
<INPUT	type="button" name="btnOK" value=" <%=languageChoose.getMessage("fi.jsp.pcbComments.Save")%> " class="BUTTON" onClick="saveComments()">
<%}%>
<BR>
<SCRIPT language="JavaScript">
function saveComments(){
	if (maxLength(frmComments.txtGeneral,"<%=languageChoose.getMessage("fi.jsp.pcbComments.Comments")%>",4000))
		frmComments.submit();
}
/*function doMe(){
	window.showModalDialog("pcbMetrics.jsp", null,"dialogWidth: 600px; dialogHeight: 600px");
}*/
</SCRIPT>
</BODY>
</HTML>
