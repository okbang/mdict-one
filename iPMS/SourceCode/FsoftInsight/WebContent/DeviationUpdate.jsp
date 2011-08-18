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
<BODY class="BD" onload = "loadPrjMenu();document.frm_deviationUpdate.deviation_modification.focus();">
<%
Vector catList = (Vector) session.getAttribute("tailoring_category");
String tailoring_source = (String) session.getAttribute("tailoring_source");
if(tailoring_source == null) tailoring_source = "0";
String title;
if(tailoring_source.equals("1")){ //MANU: called from project plan
	title=languageChoose.getMessage("fi.jsp.DeviationUpdate.ProjectplanDeviation");
}
else { //MANU: called from menu
	title= languageChoose.getMessage("fi.jsp.DeviationUpdate.Deviation");
}

DeviationInfo info = (DeviationInfo)session.getAttribute("deviationInfo");

%>
<P class="TITLE"><%=title%></P><br>
<form name="frm_deviationUpdate" action="Fms1Servlet#tailoring" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.DEVIATION_UPDATE%>">
<input type = "hidden" name="deviation_tdID" value="<%=info.deviationID%>">
<input type = "hidden" name="projectID" value="<%=info.projectID%>">
<DIV align="left">
<TABLE cellspacing="1" class="Table">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Modification")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA name="deviation_modification" rows="6" cols="80"><%=info.modification%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Action")%></TD>
            <TD class="CellBGR3"><SELECT name="deviation_action" class = "COMBO">
                 <OPTION value="1" <%if(info.action == 1){%>="" selected <%}%>=""><%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Added")%> </OPTION>
                 <OPTION value="2" <%if(info.action == 2){%>="" selected <%}%>=""><%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Modified")%> </OPTION>
                 <OPTION value="3" <%if(info.action == 3){%>="" selected <%}%>=""><%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Deleted")%> </OPTION>
            </SELECT>
</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoring.AppCri")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA name="deviation_reason" rows="6" cols="80"><%=info.reason%></TEXTAREA></TD>
        </TR>
         <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Type")%></TD>
            <TD class="CellBGR3"><input type="text" name="tailoring_type" value=" <%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Deviation")%> " size="10" disabled></TD>
             
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Category")%></TD>
            <TD class="CellBGR3"><SELECT name="deviation_category" class = "COMBO">
            <%
            for(int i = 0; i < catList.size(); i++){
            %>
            <OPTION value = "<%=catList.elementAt(i)%>" <%if(info.category.equals(catList.elementAt(i))){%>selected<%}%>><%=languageChoose.getMessage((String)catList.elementAt(i))%></OPTION>
            <%
            }
            %>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoring.Reason")%></TD>
            <TD class="CellBGR3"><TEXTAREA name="deviation_note" rows="6" cols="80"><%=((info.note == null)? "" : info.note)%></TEXTAREA></TD>
        </TR>
    </TBODY>
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.DeviationUpdate.UpdateDeviation")%> </CAPTION>
</TABLE>
</DIV>
</form>

<form name="frm_deviationDelete" action="Fms1Servlet#tailoring" method = "get">
				<input type = "hidden" name="reqType" value="<%=Constants.DEVIATION_DELETE%>" >
				<input type = "hidden" name="deviation_deleteID" value="<%=info.deviationID%>" >
				
</form>
<br>
<INPUT type="button" name="update2" value="<%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Update") %>" onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="delete" value="<%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Delete")%>"  onclick="javascript:on_Submit2();"  class="BUTTON">
<input type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Cancel")%>"    class="BUTTON" onclick="doIt(<%=(tailoring_source.equals("1"))?Constants.PL_LIFECYCLE_GET_PAGE:Constants.TAILORING_GET_LIST%>);">

</BODY>
</HTML>
<script language = "javascript">
	function on_Submit1()
	{

		if(trim(frm_deviationUpdate.deviation_modification.value)==""){
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Youmustentermodification")%>");
			frm_deviationUpdate.deviation_modification.focus();
			return;
		}
		if(frm_deviationUpdate.deviation_modification.value.length > 200){
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Thetextformodificationcannotbemorethan200characters") %>");
			frm_deviationUpdate.deviation_modification.focus();
			return;
		}
		if(trim(frm_deviationUpdate.deviation_reason.value) == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Youmustenterreason")%>");
			frm_deviationUpdate.deviation_reason.focus();
			return;
		}
		if(frm_deviationUpdate.deviation_reason.value.length > 200)	{
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Thetextforreasoncannotbemorethan200characters")%>");
			frm_deviationUpdate.deviation_reason.focus();
			return;
		}		
		if(frm_deviationUpdate.deviation_note.value.length > 200){
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Thetextfornotecannotbemorethan200characters")%>");
			frm_deviationUpdate.deviation_reason.focus();
			return;
		}
		if(trim(frm_deviationUpdate.deviation_category.value) == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Youmustenterthecategory")%>");
			frm_deviationUpdate.deviation_category.focus();
			return;
		}				
		document.frm_deviationUpdate.submit();
	}
	function on_Submit2()
	{
		if (window.confirm("<%=languageChoose.getMessage("fi.jsp.DeviationUpdate.Areyousure")%>") != 0) {
			document.frm_deviationDelete.submit();
		}
		else{
			return;
		}	
		
	}		
</script> 
