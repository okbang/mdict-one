<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<%
	response.setHeader("Content-Type", "text/html; charset=UTF-8");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woAcceptanceView.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload="loadPrjMenu()">
<%
	boolean bl=false;
	String rowStyle = "";
	int right = Security.securiPage("Work Order",request,response);
	String[] values = (String[]) session.getAttribute("WOAcceptanceMatrix");
	List teamEvaluationMatric = (List) session.getAttribute("teamEvaluationMatric");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>
<TABLE width="95%">
	<TR>
		<TD><P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woAcceptanceView.WorkOrderAcceptance")%> </P></TD>
		<TD align="right" valign="bottom"><A href="Fms1Servlet?reqType=<%=Constants.WO_AC_EXPORT%>" target="about:blank"> <%=languageChoose.getMessage("fi.jsp.woAcceptanceView.ExportAcceptanceNote")%> </A></TD>
	</TR>
</TABLE>
<form name="frm_woAcceptanceUpdate" action="woAcceptance.jsp" method = "post">
<br>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woAcceptanceView.Acceptance")%>  </CAPTION>
    <TBODY>
<%for(int i = 0; i < 1; i++){	 	
%><tr>
	<td class="ColumnLabel" width="140">
	<%
	switch (i) {
		case 0:%> <%=languageChoose.getMessage("fi.jsp.woAcceptanceView.Assets")%> <%break;
		case 1:%> <%=languageChoose.getMessage("fi.jsp.woAcceptanceView.Problems")%> <%break;
		case 2:%> <%=languageChoose.getMessage("fi.jsp.woAcceptanceView.RewardandPenalty")%> <%break;
	}
	%></td>
	<td class="CellBGR3"><%=((values[i]==null ||values[i].equals(""))?"N/A" : ConvertString.toHtml(values[i]))%></td>        
</tr>
<%}%>
<tr>
	<td class="ColumnLabel" width="140">
		Proposal
	</td>
	<td class="CellBGR3"><%=((values[3]==null ||values[3].equals(""))?"N/A" : ConvertString.toHtml(values[3]))%></td>
</tr>
</table>
<br>
<%if(right == 3 && !isArchive){%>
<input type="submit" name="Update" value=" <%=languageChoose.getMessage("fi.jsp.woAcceptanceView.Update")%> " class="BUTTON">
<%}%>
</form>
</br></br>

<!--<TABLE width="95%">
	<TR>
		<TD align="right" valign="bottom"><A href="teamEvaluationExport.jsp" target="about:blank"> Export Team Evaluation  </A></TD>
	</TR>
</TABLE>-->
<form name="wo_teamEvaluation" action="Fms1Servlet" method = "post">
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"> Project Allowance  </CAPTION>
    <TBODY>

        <TR class="ColumnLabel">
	        <TD rowspan="2" align="center" width="25%"> Name </TD>
	        <TD rowspan="1" colspan="3"  align="center"> Evaluation </TD>
        	<TD rowspan="2" align="center"> Allowance </TD>		
	        <TD rowspan="2" align="center">Notes</TD>
		</TR>
	    <TR class="ColumnLabel">
	        <TD rowspan="1"  align="center">&nbsp;&nbsp;Role&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="center">&nbsp;&nbsp;% Effort&nbsp;&nbsp;</TD>
	    	<TD rowspan="1"  align="center">&nbsp;&nbsp;Efficiency&nbsp;&nbsp;</TD>
        </TR>
        <%
         double sum = 0 ; 
         for (int i=0;i<teamEvaluationMatric.size();i++) {
        
        	rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  			bl=!bl;
        	TeamEvaluationInfo temp = (TeamEvaluationInfo)teamEvaluationMatric.get(i);
        	sum += temp.getPc();
        		for (int j=0;j<temp.getRole().length;j++) {
        %>
	        <tr class=<%=rowStyle%>>
	        	<%
	        		if (j==0) {
	        	%>
	        		<td rowspan="<%=temp.getRole().length%>"> <%=temp.getName()%></td>
	        	<%}%>
	        	<td> <%=temp.getRole()[j]%> </td>
	        	<%
	        		if (j==0) {
	        	%>
	        		<td rowspan="<%=temp.getRole().length%>"> <%=temp.getPercentAttend()%> %</td>
	        	<%}%>
	        	<td> <%=temp.getHq()[j]%> </td>
	        	<%
	        		if (j==0) {
	        	%>
	        		<td rowspan="<%=temp.getRole().length%>"> <%=CommonTools.formatDouble(temp.getPc())%> </td>
	        	<%}%>
	        	<td> <%=temp.getNote()[j]==null?"":ConvertString.toHtml(temp.getNote()[j])%> </td>
	        </tr>
	     <%}}%>
	     <tr class="TableLeft" >
		     	<td colspan="4" align="center"> <b> Total </b> </td>
		     	<td><%=CommonTools.formatDouble(sum)%></td>
		     	<td></td>
		 </tr>
	</TBODY>
</TABLE>
<br>
<%if(right == 3 && !isArchive){%>
<input type="submit" name="Update" value=" <%=languageChoose.getMessage("fi.jsp.woAcceptanceView.Update")%> " class="BUTTON" onclick="doUpdate()">
<%}%>
</form>
<SCRIPT language="javascript">
	function doUpdate(){
		wo_teamEvaluation.action = "Fms1Servlet?reqType=<%=Constants.WO_UPDATE_TEAM_EVALUATION%>";
		wo_teamEvaluation.submit();
	}
</SCRIPT>

</BODY>
</HTML>
