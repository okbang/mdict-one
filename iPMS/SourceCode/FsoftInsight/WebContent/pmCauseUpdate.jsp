<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>pmCauseUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<% 
	Vector normInfoList = (Vector) session.getAttribute("norm");
	Vector WOCustomeMetrics = (Vector)session.getAttribute("WOCustomeMetrics");
	String deviationFromTarget;
%>
<BODY class="BD" onLoad="loadPrjMenu();onLoad();">
<p class="TITLE"> <%=languageChoose.getMessage("fi.jsp.pmCauseUpdate.Causalanalysis")%> </p>
<FORM method="post" name="frm" action="Fms1Servlet?reqType=<%=Constants.UPDATE_PM_CAUSE%>#causal" method="post">
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.pmCauseUpdate.Updatecausalanalysis")%> </CAPTION>
    <TBODY>
    	<TR class="ColumnLabel">
            <TD rowspan="2" width="24" align="center">#</TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.pmCauseUpdate.Name")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.pmCauseUpdate.Unit")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.pmCauseUpdate.ActualValue")%> </TD>
			<TD colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Target")%> </TD>
			<TD colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.norm.Norm")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.pmCauseUpdate.Deviationfromnorm")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DeviationFromTarget")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.pmCauseUpdate.Cause")%> </TD>
        </TR>
        <TR class="ColumnLabel">
			<td align="center"><span style=""> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%> </span></td> <!-- Add by HaiMM -->
			<td align="center"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%><span style=""> </span></td>
			<td align="center"><span style=""><%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%></span></td> <!-- Add by HaiMM -->
			<td align="center"><span style=""> <%=languageChoose.getMessage("fi.jsp.norm.LCL")%> </span></td>
			<td align="center"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%><span style=""> </span></td>
			<td align="center"><span style=""> <%=languageChoose.getMessage("fi.jsp.norm.UCL")%> </span></td>
		</TR>
        
    <%
        boolean style=false;
        String deviation,className ;
        int j=0;
        for(int i = 0; i<normInfoList.size(); i++){
   			deviationFromTarget = "";
        	NormInfo normInfo = (NormInfo)normInfoList.get(i);
        	//only display metrics out of norms (only out of norm are formated by Color)
        	deviation=Color.colorByNorm(CommonTools.formatDouble(normInfo.normDeviation),normInfo.actualValue, normInfo.lcl, normInfo.ucl, normInfo.colorType);
			if(!CommonTools.formatDouble(normInfo.lsl).equals("N/A") && !CommonTools.formatDouble(normInfo.usl).equals("N/A")){
				deviationFromTarget = Color.colorByNorm(CommonTools.formatDouble(normInfo.planDeviation), normInfo.actualValue, normInfo.usl, normInfo.lsl, normInfo.colorType);
			}
			if (!deviation.startsWith("<"))
				continue;
			j++;
			style=!style;
 			className=(style)?"CellBGRnews":"CellBGR3";
	    %>
	        <TR class=<%=className%>>
	            <TD align="center"><%=normInfo.normID%></TD><INPUT type = "hidden" name="mID" value="<%=normInfo.normID%>">
	            <TD><%=languageChoose.getMessage(normInfo.normName)%></TD>
	            <TD><%=normInfo.normUnit%></TD>
	            <TD><%=CommonTools.formatDouble(normInfo.actualValue)%></TD> 
	            <TD><%=CommonTools.formatDouble(normInfo.usl)%></TD>
	            <TD><%=CommonTools.formatDouble(normInfo.plannedValue)%></TD>
	            <TD><%=CommonTools.formatDouble(normInfo.lsl)%></TD>	           
	            <TD><%=CommonTools.formatDouble(normInfo.lcl)%></TD>
	            <TD><%=CommonTools.formatDouble(normInfo.average)%></TD> 
	            <TD><%=CommonTools.formatDouble(normInfo.ucl)%></TD>      
	            <TD><%=deviation%></TD>
	            <TD><%=deviationFromTarget%></TD>
	            <TD><TEXTAREA rows="4" cols="50" name="txtCause"><%=(normInfo.cause==null)?"":normInfo.cause%></TEXTAREA></TD>
	        </TR>
        <%}%>

<%-- allow display custom metric follow Buz rule --%>	
		<% 
	 		int l=0;
			String customMetricDeviationFromTarget = "";
			double customMetricDeviation = 0;
			for (int i = 0; i < WOCustomeMetrics.size(); i++) {
	 			className = (style) ? "CellBGRnews" : "CellBGR3";
	 			WOCustomeMetricInfo info = (WOCustomeMetricInfo)WOCustomeMetrics.elementAt(i);
	 			if((CommonTools.formatDouble(info.actualValue) != "N/A" && CommonTools.formatDouble(info.LCL) != "N/A" && CommonTools.formatDouble(info.UCL) != "N/A" && (info.actualValue < info.LCL || info.actualValue > info.UCL)) || (CommonTools.formatDouble(info.actualValue) != "N/A" && CommonTools.formatDouble(info.plannedValue) != "N/A" && info.actualValue != info.plannedValue)){
					l++;
					style = !style;
					className = (style) ? "CellBGRnews" : "CellBGR3";

					customMetricDeviation = 0;
					if(!CommonTools.formatDouble(info.plannedValue).equals("N/A")){
						customMetricDeviation = (info.actualValue - info.plannedValue) * 100 / info.plannedValue;
					}
					
				%>
					<TR class="<%=className%>">
						<TD align="center">14.<%=l%><INPUT type = "hidden" name="cmID" value="<%=info.cusMetricID%>"></TD>
						<TD><%=ConvertString.toHtml(info.name)%></TD>
						<TD><%=ConvertString.toHtml(info.unit)%></TD>
						<TD><%=CommonTools.formatDouble(info.actualValue)%></TD>
						<TD></TD>
						<TD><%=CommonTools.formatDouble(info.plannedValue)%></TD>
						<TD></TD>
						<TD><%=CommonTools.formatDouble(info.LCL)%></TD>
	            		<TD><%=CommonTools.formatDouble(info.plannedValue)%></TD> 
	            		<TD><%=CommonTools.formatDouble(info.UCL)%></TD>     
						<TD><%=info.deviation%></TD>
						<TD><%=CommonTools.formatDouble(customMetricDeviation)%></TD>
						<TD><TEXTAREA rows="4" cols="50" name="txtCusCause"><%=(info.causal==null)?"":ConvertString.toHtml(info.causal)%></TEXTAREA></TD>
					</TR>
				<%
					}
				}
		%>
    </TBODY>
</TABLE>
<P><INPUT type="button" name="btnOk" value="<%= languageChoose.getMessage("fi.jsp.pmCauseUpdate.OK")%>" class="BUTTON" onclick="update();"> 
	<INPUT type="button" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.pmCauseUpdate.Cancel")%>" class="BUTTON" onclick="jumpURL('postMortemReport.jsp');"></P>
</FORM>
<SCRIPT language="javascript">
function update(){	
	<%if (j>1){%>
	for (var i=0; i< <%=j%>; i++){
		if (!maxLength(frm.txtCause[i],"<%=languageChoose.getMessage("fi.jsp.pmCauseUpdate.Cause") %>",200))
			return;
  	}
  	<%}else{%>
  	if (!maxLength(frm.txtCause,"<%=languageChoose.getMessage("fi.jsp.pmCauseUpdate.Cause") %>",200))
		return;
  	<%}%>

	frm.submit();
}
function onLoad()
{
	frm.txtCause<%=((j==1)?"":"[0]")%>.focus(); 
}  			
</SCRIPT> 
</BODY>
</HTML>
