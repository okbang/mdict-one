<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>protailAddnew.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></Script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<%  Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	int bLyfeCycle =  com.fms1.tools.CommonTools.parseInt(request.getParameter("vLyfeCycle"));  
%>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	
%>
<BODY class="BD" onLoad="loadAdminMenu();frm.Tailoring" >
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.ProcessTailoring")%> </P>
<FORM name="frm" action="Fms1Servlet" method="post">
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.Addnewprocesstailoring")%> </CAPTION>
     <COL span="1" width "150">
    <TBODY>
        <TR>
            <TD colspan="80" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.Tailoringpermission")%>* </TD>
            <TD height="40" class="CellBGR3"><TEXTAREA rows="4" cols="50" name="permission"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD colspan="80" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.Applicablecriteria")%>* </TD>
            <TD height="40" class="CellBGR3"><TEXTAREA rows="4" cols="50" name="applicable"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD colspan="80" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.Process")%> </TD>
            <TD class="CellBGR3"><SELECT name="processId" class="COMBO">
            <%
           		for(int i=0;i<vtProcess.size();i++)
           		{
           			ProcessInfo psInfo=(ProcessInfo)vtProcess.get(i);
           	%>
				<OPTION value="<%=psInfo.processId%>"><%=languageChoose.getMessage(psInfo.name)%></OPTION>
           	<%
           		}
           	%>
            </SELECT></TD>
        </TR>
        <TR>
        	<TD colspan="80" class="ColumnLabel">Life Cycle</TD>
        	<TD class="CellBGR3">
        		<SELECT class="COMBO" name="selAddLyfeCycle">
        			<OPTION value="3" <%=((bLyfeCycle == 3)||(bLyfeCycle == -1))? "selected" : ""%>>General</OPTION>
					<OPTION value="0" <%=bLyfeCycle == 0? "selected" : ""%>>Development</OPTION>
		    		<OPTION value="1" <%=bLyfeCycle == 1? "selected" : ""%>>Maintenance</OPTION>
		    		<OPTION value="2" <%=bLyfeCycle == 2? "selected" : ""%>>Other</OPTION>
        		</SELECT>
        	</TD>	
        </TR>
    </TBODY>
</TABLE>
<P><INPUT type="button" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.OK")%> " class="BUTTON" onclick="onOK();">
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.Cancel")%> " class="BUTTON" onclick="onCancel();"></P>
<INPUT type="hidden" name="reqType" value="<%=Constants.PROCESS_TAILORING_ADD%>">
<INPUT type="hidden" name="cboProcess">
</FORM>
</BODY>
<SCRIPT>
function onOK() {
	if (trim(frm.permission.value) == "") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.Tailoringpermissioncantbeempty")%>");
		frm.permission.focus();
		return;
	}
	if(!maxLength(frm.permission,'<%=languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.Tailoringpermission")%>',500))
	{					
		return;
	}
		
	if (trim(frm.applicable.value) == "") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.Applicablecriteriacantbeempty")%>");
		frm.applicable.focus();
		return;
	}
	if(frm.applicable.value.length > 200)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.Thetextforapplicablecannotbemorethan200characters")%>");
			frm.applicable.focus();
			return;
		}
	
		
	frm.reqType.value = "<%=Constants.PROCESS_TAILORING_ADD%>";
	frm.cboProcess.value="<%=request.getParameter("cboProcess")%>";
	frm.submit();
}
function onCancel() {
		frm.reqType.value = "<%=Constants.PROCESS_TAILORING_SEARCH%>";
		frm.cboProcess.value="<%=request.getParameter("cboProcess")%>";
		frm.submit();
	
}
</SCRIPT>
</HTML>
