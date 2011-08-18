<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.* ,com.fms1.common.group.* ,java.util.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="../javaFns.jsp"%>
</SCRIPT>
<TITLE>normPlanSQA.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
int right=0; 

int type= Integer.parseInt(request.getParameter("type"));
String title,menu;
switch (type){
	case MetricDescInfo.GR_SET_SQA_HOME_PAGE:
		title = "SQA";
		right=Security.securiPage("Group norms",request,response);
		menu="loadSQAMenu";
		break;
	case MetricDescInfo.GR_PROCESS:
		title = languageChoose.getMessage("fi.jsp.normPlanSQA.Process");
		right=Security.securiPage("Organization norms",request,response);
		menu="loadOrgMenu";
		break;
	default:
		title="";
		menu="";
		right=Security.securiPage("Organization norms",request,response);
}
NormPlanInfo normPlanInfo=(NormPlanInfo)session.getAttribute("normPlan");
String error=request.getParameter("error");
%> 
<%!
//if the norm is null then we use prev value
String formatNorm(double curVal,double prevVal){
if (Double.isNaN(curVal) && !Double.isNaN(prevVal))
	return CommonTools.updateDouble(prevVal)+"\" style='font-weight: bold;color: red'";
return CommonTools.updateDouble(curVal);
	
}

%>
<BODY class="BD" onload="<%=menu%>()">
<P class="TITLE"><%=languageChoose.paramText(new String[]{"fi.jsp.normPlanSQA.~PARAM1_NAMENORMS~norms", title})%></P>
<P class="ERROR"><%=languageChoose.getMessage(error)%></P>
<FORM action="Fms1Servlet?reqType=<%=Constants.LOADNORMS+"&type="+type%>" name="frm" method="post">
<TABLE >
	<TBODY>
		<TR class="NormalText">
			<TD> <%=languageChoose.getMessage("fi.jsp.normPlanSQA.Year")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.normPlanSQA.Term")%> </TD>
		</TR>
		<TR>
			<TD>
				<SELECT name="txtYear" class="COMBO" >
					<%
					int startYear = 2000;
					java.text.SimpleDateFormat  yearFrmt= new java.text.SimpleDateFormat("yyyy");
					int endYear = Integer.parseInt(yearFrmt.format(new java.util.Date()));
					String selected;
	            	for (int year =endYear;year>=startYear;year--){
	            		selected=(normPlanInfo.year==year)?" selected":"";
	            		%><OPTION value=<%=year+selected%>><%=year%></OPTION>
	                <%}%>
				</SELECT>
			</TD>
			<TD> 
				<SELECT name="txtPeriod" class="COMBO">
					<OPTION value ="S1"<%=((normPlanInfo.term.equals("S1"))?" selected":"")%>>S1</OPTION>
					<OPTION value ="S2"<%=((normPlanInfo.term.equals("S2"))?" selected":"")%>>S2</OPTION>
				</SELECT>
			</TD>
			<TD>
				<INPUT type="submit" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.normPlanSQA.Go")%> ">
			</TD>
			<%if (right==3){%>
			<TD class="NormalText">
 <%=languageChoose.getMessage("fi.jsp.normPlanSQA.NoteSuggestedmetricsinredshoul")%> </TD>
			<%}%>
		</TR>
	</TBODY>
</TABLE>
</FORM>
</BODY>
<BR>
<FORM action="Fms1Servlet?reqType=<%=Constants.SAVENORMS+"&type="+type%>" name="frmN" method="post">
<TABLE cellspacing="1" class="Table" width="95%" >
	<TBODY>
		<TR class="ColumnLabel">
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.normPlanSQA.ID")%> </TD>
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.normPlanSQA.Name")%> </TD>
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.normPlanSQA.Unit")%> </TD>
			<% String prevPeriod =PCB.formatPrevPeriod(normPlanInfo.term,normPlanInfo.year);%>
			<TD colspan="3" class="SimpleColumn" bgcolor="#AEC9E6" align="center"><%=languageChoose.paramText(new String[]{"fi.jsp.normPlanSQA.~PARAM1_NAMENORMS~norms", title})%> <BR><%=prevPeriod%></TD>
			<TD colspan="3" class="SimpleColumn" bgcolor="#89AFDA" align="center"><%=languageChoose.paramText(new String[]{"fi.jsp.normPlanSQA.~PARAM1_NAMENORMS~norms", title})%> <BR><%=normPlanInfo.term+"-"+normPlanInfo.year%></TD>
		</TR>
		<TR class="SimpleColumn">
			<TD bgcolor="#AEC9E6">LCL</TD>
			<TD bgcolor="#AEC9E6"> <%=languageChoose.getMessage("fi.jsp.normPlanSQA.Norm1")%> </TD>
			<TD bgcolor="#AEC9E6">UCL</TD>
			<TD bgcolor="#89AFDA">LCL1</TD>
			<TD bgcolor="#89AFDA"> <%=languageChoose.getMessage("fi.jsp.normPlanSQA.Norm1")%> </TD>
			<TD bgcolor="#89AFDA">UCL1</TD>
		</TR>
	<%	NormPlanInfo.Row row;
		for (int i=0;i<normPlanInfo.rows.size();i++){
			row=(NormPlanInfo.Row)normPlanInfo.rows.elementAt(i);
			if (!row.isMetricGroup){
			%><TR class="CellBGRnews">
				<TD><%=row.strMetricID%></TD>
				<%if (right==3 &&( row.metricID==MetricDescInfo.PQA_SATISFACTION||row.metricID==MetricDescInfo.SATISFIED_INDICATORS)){%>
					<TD><A href="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=Group/setMetric.jsp&id=<%=i%>" target="setmetric"><%=languageChoose.getMessage(row.metricName)%></A></TD>
				<%}else{%>
					<TD><%=languageChoose.getMessage(row.metricName)%></TD>
				<%}%>
				<TD><%=row.metricUnit%></TD>
				<TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(row.prevLCL)%></TD>
				<TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(row.prevNorm)%></TD>
				<TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(row.prevUCL)%></TD>
				<%if (right==3){%>
				<TD bgcolor="#89AFDA">
				<INPUT type="hidden" name="uval" value="<%=CommonTools.formatDouble(MetricDescInfo.getBoundary(row.metricID,false))%>" disabled>
				<INPUT type="hidden" name="lval" value="<%=CommonTools.formatDouble(MetricDescInfo.getBoundary(row.metricID,true))%>" disabled>
				<INPUT name ="lcl"  maxlength="11" size = "5" value="<%=formatNorm(row.LCL,row.prevLCL)%>"></TD>
				<TD bgcolor="#89AFDA"><INPUT name ="norm" maxlength="11" size = "5" value="<%=formatNorm(row.norm,row.prevNorm)%>"></TD>
				<TD bgcolor="#89AFDA"><INPUT name ="ucl" maxlength="11" size = 5" value="<%=formatNorm(row.UCL,row.prevUCL)%>"></TD>
				<%} else{%>
				<TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(row.LCL)%></TD>
				<TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(row.norm)%></TD>
				<TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(row.UCL)%></TD>
				<%}%>
			</TR>
			<%}else {%>
	        <TR class="ColumnLabel">
	            <TD ><%=row.strMetricID%></TD>
	            <TD colspan= 12><%=row.metricName%></TD>
	        </TR>
			<%}
		}%>
	</TBODY>
</TABLE>
</FORM>
<BR>
<%if (right==3){%>
<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.normPlanSQA.Save")%> " onclick="check()">
<SCRIPT language="JavaScript">
function check(){
	for (var w=0;w<frmN.lcl.length;w++ ){
		if (!numberFld(frmN.lcl[w], "LCL") || !numberFld(frmN.norm[w], "<%=languageChoose.getMessage("fi.jsp.normPlanSQA.Norm")%>") ||!numberFld(frmN.ucl[w], "UCL"))
			return;
		if (Number(frmN.lcl[w].value) >Number(frmN.norm[w].value) ||Number(frmN.norm[w].value)>Number(frmN.ucl[w].value)){
  	 		alert("<%= languageChoose.getMessage("fi.jsp.normPlanSQA.NormValueMustBeGreaterThanLclAndSmallerThanUcl")%>");  			
			frmN.norm[w].focus();
			return;
		}
		if (!isNaN(frmN.uval[w].value) && (Number(frmN.ucl[w].value)>Number(frmN.uval[w].value))){
			alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlan.UCL__of__this__metric__must__be__less__or__equal__to__~PARAM1_VALUE~")%>',frmN.uval[w].value)));
			frmN.ucl[w].focus();
			return;
		}
		if (!isNaN(frmN.lval[w].value) && (Number(frmN.lcl[w].value)<Number(frmN.lval[w].value))){
			alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlanSQA.LCL__of__this__metric__must__be__above__or__equal__to__~PARAM1_VALUE~")%>',frmN.lval[w].value)));
			frmN.lcl[w].focus();
			return;
		}
	}
	frmN.submit();
}

</SCRIPT>
<%}%>
<SCRIPT language="JavaScript">
var objToHide=new Array(frm.txtYear,frm.txtPeriod);
</SCRIPT>
<P></P>
</HTML>
