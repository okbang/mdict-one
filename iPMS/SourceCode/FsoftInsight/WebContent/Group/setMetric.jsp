<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.Vector,java.util.Calendar,com.fms1.tools.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>

<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	int right=Security.securiPage("Organization norms",request,response);
	NormPlanInfo normPlanInfo=(NormPlanInfo)session.getAttribute("normPlan");

	int id =Integer.parseInt(request.getParameter("id"));
	NormPlanInfo.Row row=(NormPlanInfo.Row)normPlanInfo.rows.elementAt(id);

%>
<TITLE>Set metric</TITLE>
</HEAD>
<BODY  class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.setMetric.Updateactualvalue")%> </P>
<br>
<FORM action="Fms1Servlet?reqType=<%=Constants.SET_METRIC%>" name="frm" method="post">
<INPUT type ="hidden" name="id" value="<%=id%>">
<TABLE >
	<TBODY>
		<TR class="NormalText">
			<TD><%=languageChoose.getMessage(row.metricName)%></TD>
		</TR>
		<TR class="NormalText">
			<TD>
				<INPUT size="9" type="text" maxlength="9" name="metricVal" value="<%=CommonTools.updateDouble(row.actualValue)%>">
			</TD>
			<TD>
				<INPUT type="button"  class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.setMetric.Set")%> " onclick="doMe()">
			</TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<SCRIPT language='javascript'>
function doMe(){
	frm.submit();
	window.close();
}
</SCRIPT>
</HTML>
