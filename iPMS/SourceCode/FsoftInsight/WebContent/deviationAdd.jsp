<%@page import="com.fms1.tools.*"%>
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>tailoringAdd.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload = "loadPrjMenu();document.frm_tailoringAdd.tailoring_modification.focus();">
<%
Vector catList = (Vector) session.getAttribute("tailoring_category");
String tailoring_source = (String) session.getAttribute("tailoring_source");
if(tailoring_source == null) tailoring_source = "0";
String title;
if(tailoring_source.equals("1")){ //MANU: called from project plan
	title=languageChoose.getMessage("fi.jsp.DeviationAdd.ProjectplanDeviation");
}
else { //MANU: called from menu
	title= languageChoose.getMessage("fi.jsp.DeviationAdd.Deviation");
}
%>
<P class="TITLE"><%=title%></P><br>
<form name="frm_tailoringAdd" action="Fms1Servlet#tailoring" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.DEVIATION_ADD%>">
<input type = "hidden" name="tailoring_tdID" value="1">
<input type = "hidden" name="projectID" value="<%=session.getAttribute("projectID")%>">
<DIV align="left">
<TABLE cellspacing="1" class="Table">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoring.Description")%>*</TD>
            <TD><TEXTAREA name="tailoring_modification" rows="6" cols="80"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.DeviationAdd.Action")%></TD>
            <TD class="CellBGR3"><SELECT name="tailoring_action" class="COMBO">
                <OPTION value="1"> <%=languageChoose.getMessage("fi.jsp.deviationAdd.Added")%> </OPTION>
                <OPTION value="2"> <%=languageChoose.getMessage("fi.jsp.deviationAdd.Modified")%> </OPTION>
                <OPTION value="3"> <%=languageChoose.getMessage("fi.jsp.deviationAdd.Deleted")%> </OPTION>
            </SELECT>
</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoring.AppCri")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA name="tailoring_reason" rows="6" cols="80"></TEXTAREA></TD>
        </TR>
       <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.DeviationAdd.Type")%></TD>
            <TD class="CellBGR3"><input type="text" name="tailoring_type" value=" <%=languageChoose.getMessage("fi.jsp.deviationAdd.Deviation")%> " size="10"disabled></TD>
             
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.DeviationAdd.Category")%></TD>
            <TD class="CellBGR3"><SELECT name="tailoring_category" class = "COMBO">
            <%
            for(int i = 0; i < catList.size(); i++){
            %>
            <OPTION value = "<%=catList.elementAt(i)%>"><%=languageChoose.getMessage((String)catList.elementAt(i))%></OPTION>
            <%
            }
            %>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoring.Reason")%></TD>
            <TD class="CellBGR3"><TEXTAREA name="tailoring_note" rows="6" cols="80"></TEXTAREA></TD>
        </TR>
    </TBODY>
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.deviationAdd.AddnewDeviation")%> </CAPTION>
</TABLE>
</DIV>
</form>



<br>
<INPUT type="button" name="Add" value="<%=languageChoose.getMessage("fi.jsp.DeviationAdd.OK")%>" onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.DeviationAdd.Cancel")%>" class="BUTTON" onclick="doIt(<%=(tailoring_source.equals("1"))?Constants.PL_LIFECYCLE_GET_PAGE:Constants.TAILORING_GET_LIST%>);">

</BODY>
</HTML>
<script language = "javascript">
	function on_Submit()
	{
		
		if(trim(frm_tailoringAdd.tailoring_modification.value) == "")
		{
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationAdd.Youmustentermodification")%>");
			frm_tailoringAdd.tailoring_modification.focus();
			return;
		}
		if(frm_tailoringAdd.tailoring_modification.value.length > 200)
		{
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationAdd.Thetextformodificationcannotbemorethan200characters")%>");
			frm_tailoringAdd.tailoring_modification.focus();
			return;
		}
		if(trim(frm_tailoringAdd.tailoring_reason.value) == "")
		{
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationAdd.Youmustenterreason")%>");
			frm_tailoringAdd.tailoring_reason.focus();
			return;
		}
		if(frm_tailoringAdd.tailoring_reason.value.length > 200)
		{
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationAdd.Thetextforreasoncannotbemorethan200characters")%>");
			frm_tailoringAdd.tailoring_reason.focus();
			return;
		}
		
		if(frm_tailoringAdd.tailoring_note.value.length > 200)
		{
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationAdd.Thetextfornotecannotbemorethan200characters")%>");
			frm_tailoringAdd.tailoring_reason.focus();
			return;
		}
		if(trim(frm_tailoringAdd.tailoring_category.value) == "")
		{
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationAdd.Youmustenterthecategory")%>");
			frm_tailoringAdd.tailoring_category.focus();
			return;
		}				
		document.frm_tailoringAdd.submit();
	}	
</script> 
