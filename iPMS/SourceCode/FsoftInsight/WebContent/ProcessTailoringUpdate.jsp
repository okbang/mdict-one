<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*,com.fms1.tools.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>proTailUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></Script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadAdminMenu();frm.Tailoring">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.ProcessTailoring")%> </P>
<FORM name="frm" action="Fms1Servlet" method="post">
<%
	final int tailID = CommonTools.parseInt(request.getParameter("tailID"));
	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	Vector proTailoringList = (Vector)session.getAttribute("ProTailoringList");
	final ProTailoringInfo proTailoringInfo = (ProTailoringInfo) proTailoringList.elementAt(tailID);
		
	
%>
	
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.Updateprocesstailoring")%> </CAPTION>
    <COL span="1" width "150">
    <TBODY>
        <TR>
            <TD colspan="80" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.Tailoringpermission")%>*</TD>
            <TD height="40" class="CellBGR3"><TEXTAREA rows="4" cols="50" name="permission"><%=proTailoringInfo.Tailoring_per%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD colspan="80" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.Applicablecriteria")%>*</TD>
            <TD height="40" class="CellBGR3"><TEXTAREA rows="4" cols="50" name="applicable"><%=proTailoringInfo.Applicable_Cri%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD colspan="80" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.Process")%> </TD>
            <TD class="CellBGR3"><SELECT name="processId" class="COMBO">
            <%
           		for(int i=0;i<vtProcess.size();i++)
           		{  
           			ProcessInfo psInfo=(ProcessInfo)vtProcess.get(i);
           	%>
				<OPTION value="<%=psInfo.processId%>"<%=(psInfo.processId==proTailoringInfo.ProcessID)?" selected":""%>><%=languageChoose.getMessage(psInfo.name)%></OPTION>
           	<%
           		}
           	%>
            </SELECT></TD>
        </TR>
        <TR>
        	<TD colspan="80" class="ColumnLabel">Status</TD>
        	<TD class="CellBGR3">
        		<SELECT name = "updateselStatus" class="COMBO"> 
        		    <% byte updateStatus = proTailoringInfo.tailStatus; %>
        			<OPTION value="0" <%=updateStatus == 0 ? "selected" : ""%> >Open</OPTION>
        			<OPTION value="1" <%=updateStatus == 1 ? "selected" : ""%> >Expired</OPTION>
        		</SELECT>
        	</TD>	
        </TR>
         <TR>
        	<TD colspan="80" class="ColumnLabel">Life Cycle</TD>
        	<TD class="CellBGR3">
        		<SELECT class="COMBO" name="updateselLyfeCycle">
        			<% byte updatelyfeCycle = proTailoringInfo.tailLyfeCycle; %>
        			<OPTION value="3" <%=updatelyfeCycle == 3? "selected" : ""%>>General</OPTION>
					<OPTION value="0" <%=updatelyfeCycle == 0 ? "selected" : ""%>>Development</OPTION>
		    		<OPTION value="1" <%=updatelyfeCycle == 1 ? "selected" : ""%>>Maintenance</OPTION>
		    		<OPTION value="2" <%=updatelyfeCycle == 2 ? "selected" : ""%>>Other</OPTION>
        		</SELECT>
        	</TD>	
        </TR>
    </TBODY>
</TABLE>

<P><INPUT type="button" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.OK")%> " class="BUTTON" onclick="onOK()">
<INPUT type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.Delete")%> " class="BUTTON" onclick="onDelete()" >
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.Cancel")%> " class="BUTTON" onclick=" onCancel()"></P>
<INPUT type="hidden" name="reqType">
<INPUT type="hidden" name="cboProcess">
<INPUT type="hidden" name="tailID" value="<%=tailID%>">
</FORM>
</BODY>
<SCRIPT>

function onOK() {
   
	if (trim(frm.permission.value) == "") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.Tailoringpermissioncantbeempty")%>");
		frm.permission.focus();
		return;
	}
	if(!maxLength(frm.permission,'<%=languageChoose.getMessage("fi.jsp.ProcessTailoringAddNew.Tailoringpermission")%>',500))
		{			
			frm.permission.focus();
			return;
		}
		
	if (trim(frm.applicable.value) == "") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.Applicablecriteriacantbeempty")%>");
		frm.permission.focus();
		return;
	}
	if(frm.applicable.value.length > 200)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.Thetextforapplicablecannotbemorethan200characters")%>");
			frm.applicable.focus();
			return;
		}
	frm.reqType.value = "<%=Constants.PROCESS_TAILORING_UPDATE%>";
	frm.cboProcess.value="<%=request.getParameter("cboProcess")%>";
	frm.submit();
}
function onDelete() {
	if (window.confirm("<%= languageChoose.getMessage("fi.jsp.ProcessTailoringUpdate.Areyousuretodeletethisprocesstailoring")%>") != 0) {
		frm.reqType.value = "<%=Constants.PROCESS_TAILORING_REMOVE%>";
		frm.cboProcess.value="<%=request.getParameter("cboProcess")%>";
		frm.submit();
	}
}
function onCancel() {
		frm.reqType.value ="<%=Constants.PROCESS_TAILORING%>";
		frm.cboProcess.value="<%=request.getParameter("cboProcess")%>";
		frm.submit();
	
}
</SCRIPT>
</HTML>
