<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<LINK rel="stylesheet" type="text/css" href="stylesheet/fms.css">
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woTeamEvaluationUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload="loadPrjMenu()">
<%
	boolean isArchive = false;
	int right = Security.securiPage("Work Order",request,response);
	String[] values = (String[]) session.getAttribute("WOAcceptanceMatrix");
	List teamEvaluationMatric = (List) session.getAttribute("teamEvaluationMatric");
	boolean bl=false;
	String rowStyle = "";
	
%>
	<P class="TITLE"> Project Allowance Update </P>
	<BR>
	<form name="wo_teamEvaluationUpdate" action="Fms1Servlet" method = "post">
		<TABLE cellspacing="1" class="Table" width="90%">
			<TBODY>
	
	        <TR class="ColumnLabel">
		        <TD rowspan="1" align="center" width="25%"> Name </TD>
		        <TD rowspan="1" align="center" width="25%"> Role </TD>
		        <TD rowspan="1" align="center" width="10%"> Efficiency </TD>
	        	<TD rowspan="1" align="center" width="10%"> Allowance </TD>		
		        <TD rowspan="1" align="center" width="45%">Notes</TD>
			</TR>
			
			 <% 
			 	int number =  0 ;
			    for (int i=0;i<teamEvaluationMatric.size();i++) {
			 	rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
	        	TeamEvaluationInfo temp = (TeamEvaluationInfo)teamEvaluationMatric.get(i);
	        	for (int j=0;j<temp.getRole().length;j++) {
	         %>
		        <tr class=<%=rowStyle%>>
		           <% if (j==0) { %>
		        	<td rowspan="<%=temp.getRole().length%>"> <%=temp.getName()%> </td>
		        	<%}%>
		        	<td> <%=temp.getRole()[j]%>  </td>
		        	<input type=hidden name="role" value="<%=temp.getRole()[j]%>">
		        	<input type=hidden name="devID" value="<%=temp.getDeveloperID()%>">
		        	<td>
		     
		        		<SELECT name="HP" class="COMBO" onChange="">
		        			<OPTION value="5-Excellent"<%if(temp.getHq()[j].equalsIgnoreCase("5-Excellent")){%>selected<%}%>> 5-Excellent </OPTION>
		        			<OPTION value="4-Very Good"<%if(temp.getHq()[j].equalsIgnoreCase("4-Very Good")){%>selected<%}%>> 4-Very Good </OPTION>
		        			<OPTION value="3-Good"<%if(temp.getHq()[j].equalsIgnoreCase("3-Good")||temp.getHq()[j].equalsIgnoreCase("")){%>selected<%}%>> 3-Good </OPTION>
		        			<OPTION value="2-Acceptable"<%if(temp.getHq()[j].equalsIgnoreCase("2-Acceptable")){%>selected<%}%>> 2-Acceptable </OPTION>
		        			<OPTION value="1-Weak"<%if(temp.getHq()[j].equalsIgnoreCase("1-Weak")){%>selected<%}%>> 1-Weak </OPTION>
		        		</SELECT>
		        	</td>
		        	<% if (j==0) { 
		        		number++;
		        	%>
		        		<td> <input type=text name="PC" value="<%=CommonTools.formatDouble(temp.getPc())%>" onblur="calSum(<%=number%>)"> </td>
		        	<% } else {
		        	 %>
		        	 	<td>  </td>
		        	 <% } %>
		        	
		        	<td height="57"><TEXTAREA style="width:100%;height:100%" name="Note"><%=temp.getNote()[j]==null?"":temp.getNote()[j]%></TEXTAREA> </td>
		        </tr>
		     <%}}%>
		     	<tr class="TableLeft" >
		     		<td colspan="3" align="center"> <b> Total </b> </td>
		     		<td><A name='sum'></A></td>
		     		<td></td>
		     	</tr>
	       
		</TBODY>
		</TABLE>
		<br>
	<%if(right == 3 && !isArchive){%>
	<input type="button" name="Update" value=" Save " class="BUTTON" onclick="doUpdate()">
	<%}%>
	<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.Cancel")%>" class="BUTTON" onclick="jumpURL('woAcceptanceView.jsp');">
	</form>

</BODY>
</HTML>
<script language = "javascript">
	calSum(1);
	function calSum(index){
		var number = document.getElementsByName("PC")[index-1].value;
		if (isNaN(Math.round(number))) {
			alert("Invalid Allowance");
			document.getElementsByName("PC")[index-1].focus();
			return ;
		}
		
		if (number<0) {
			alert("Allowance must be larger than 0");
			document.getElementsByName("PC")[index-1].focus();
			return ;
		}
		var sum = 0;
		for (var i=0;i<document.getElementsByName("PC").length;i++) {
			sum = sum +  parseFloat(document.getElementsByName("PC")[i].value);
		}
		document.all['sum'].innerText = sum ; 
	}
	function doUpdate(){
		if (validate()) {
			wo_teamEvaluationUpdate.action = "Fms1Servlet?reqType=<%=Constants.WO_TEAM_EVALUATION_DO_UPDATE%>";
			wo_teamEvaluationUpdate.submit();
		}
	}
	function validate(){
		for (var i=0;i<document.getElementsByName("PC").length;i++) {
			var number = document.getElementsByName("PC")[i].value;
			if (isNaN(Math.round(number))) {
				alert("Invalid Allowance");
				document.getElementsByName("PC")[i].focus();
				return false;
			}
			
			if (number<0) {
				alert("Allowance must be larger than 0");
				document.getElementsByName("PC")[i].focus();
				return false;
			}
			
		}
		return true;
	}
</script>