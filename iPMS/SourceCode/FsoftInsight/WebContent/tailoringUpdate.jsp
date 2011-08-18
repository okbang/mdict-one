<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>tailoringUpdate.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload = "loadPrjMenu();document.frm_tailoringUpdate.tailoring_note.focus();">
<%

String tailoring_source = (String) session.getAttribute("tailoring_source");
if(tailoring_source == null) tailoring_source = "0";
String title;
if(tailoring_source.equals("1")){ //MANU: called from project plan
	title=languageChoose.getMessage("fi.jsp.tailoringUpdate.ProjectPlan");
}
else { //MANU: called from menu
	title= languageChoose.getMessage("fi.jsp.tailoringUpdate.Tailoring");
}

TailoringInfo info = (TailoringInfo)session.getAttribute("tailoringInfo");

%>
<P class="TITLE"><%=title%></P><br>
<form name="frm_tailoringUpdate" action="Fms1Servlet#tailoring" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.TAILORING_UPDATE%>">
<input type = "hidden" name="pro_tail_ID" value="<%=info.process_tailID%>">
<input type = "hidden" name="projectID" value="<%=info.projectID%>">
<DIV align="left">
<TABLE cellspacing="1" class="Table">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Modification")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA name="tailoring_modification" rows="6" cols="80" disabled><%=info.modification%></TEXTAREA></TD>
        </TR>
		<TR>
			<TD><input type = "hidden" name="action" value="<%=info.action%>"> </TD>
		</TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Action")%></TD>
            <TD class="CellBGR3"><SELECT name="tailoring_action" class = "COMBO" disabled>
                 <OPTION value="1" <%if(info.action == 1){%>="" selected <%}%>=""><%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Added")%> </OPTION>
                 <OPTION value="2" <%if(info.action == 2){%>="" selected <%}%>=""><%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Modified")%> </OPTION>
                 <OPTION value="3" <%if(info.action == 3){%>="" selected <%}%>=""><%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Deleted")%> </OPTION>
            </SELECT>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoring.AppCri")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA name="tailoring_reason" rows="6" cols="80" disabled><%=info.reason%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Type")%></TD>
            <TD class="CellBGR3"><input type="text" name="tailoring_type" value=" <%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Tailoring")%> " size="10"disabled></TD>
             
        </TR>
         <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Category")%></TD>
             <TD class="CellBGR3"><input type="text" name="tailoring_category" value="<%=info.category%>" size="25"disabled></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoring.Reason")%></TD>
            <TD class="CellBGR3"><TEXTAREA name="tailoring_note" rows="6" cols="80"><%=((info.note == null)? "" : info.note)%></TEXTAREA></TD>
        </TR>
    </TBODY>
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.tailoringUpdate.UpdateTailoring")%>  </CAPTION>
</TABLE>
</DIV>
</form>

<form name="frm_tailoringDelete" action="Fms1Servlet#tailoring" method = "get">
				<input type = "hidden" name="reqType" value="<%=Constants.TAILORING_DELETE%>" >
				<input type = "hidden" name="pro_tail_ID" value="<%=info.process_tailID%>">
				<input type = "hidden" name="projectID" value="<%=info.projectID%>">
				
				
</form>
<br>
<INPUT type="button" name="update2" value="<%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Update")%>" onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="delete" value="<%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Delete")%>"  onclick="javascript:on_Submit2();"  class="BUTTON">
<input type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Cancel")%>"    class="BUTTON" onclick="doIt(<%=(tailoring_source.equals("1"))?Constants.PL_LIFECYCLE_GET_PAGE:Constants.TAILORING_GET_LIST%>);">

</BODY>
</HTML>
<script language = "javascript">
	function on_Submit1()
	{
	
		if(frm_tailoringUpdate.tailoring_note.value.length > 200){
			alert("<%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Thetextfornotecannotbe")%>");
			frm_tailoringUpdate.tailoring_reason.focus();
			return;
		}
				
		document.frm_tailoringUpdate.submit();
	}
	function on_Submit2()
	{
		if (window.confirm("<%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Areyousure")%>") != 0) {
			document.frm_tailoringDelete.submit();
		}
		else{
			return;
		}	
		
	}		
</script> 
