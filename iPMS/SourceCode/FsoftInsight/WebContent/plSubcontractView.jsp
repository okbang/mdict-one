<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plSubcontractView.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class = "BD">
<%
int right = Security.securiPage("Project plan",request,response);
SubcontractInfo subcontractInfo = (SubcontractInfo)session.getAttribute("plSubcontractInfo");
%>
<form name="frm_plSubcontractUpdatePrep" action="Fms1Servlet" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_SUBCONTRACT_UPDATE_PREPARE%>">
<input type = "hidden" name="plSubcontract_ID" value="<%=subcontractInfo.subcontractID%>">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plSubcontractView.ProjectplanSubcontract")%> </P>

<br>
<TABLE cellspacing="1" class="Table" width = "90%">

<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plSubcontractView.Subcontractinfo")%> </CAPTION>
    <TBODY>
        
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plSubcontractView.Job")%> </TD>
            <TD class="CellBGR3"><%=ConvertString.toHtml(subcontractInfo.job)%>
           </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plSubcontractView.Deliverable")%> </TD>
            <TD class="CellBGR3"><%=ConvertString.toHtml(subcontractInfo.deliverable)%>
           </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plSubcontractView.Planneddeliverydate")%> </TD>
            <TD class="CellBGR3"><%=((subcontractInfo.plannedDeliveryDate == null)? "N/A" : CommonTools.dateFormat(subcontractInfo.plannedDeliveryDate))%></TD>
        </TR>
         <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plSubcontractView.Actualdeliverydate")%> </TD>
            <TD class="CellBGR3"><%=((subcontractInfo.actualDeliveryDate == null)? "N/A" : CommonTools.dateFormat(subcontractInfo.actualDeliveryDate))%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plSubcontractView.Note")%> </TD>
            <TD class="CellBGR3"><%=((subcontractInfo.note == null)? "N/A" : ConvertString.toHtml(subcontractInfo.note))%>
            </TD>
        </TR>
        
    </TBODY>
</TABLE>

</form>
<br>
<form name="frm_plSubcontractDelete" action="Fms1Servlet#Subcontracts" method = "get">
				<input type = "hidden" name="reqType" value="<%=Constants.PL_SUBCONTRACT_DELETE %>" >
				<input type = "hidden" name="plSubcontract_ID" value="<%=subcontractInfo.subcontractID%>">
</form>

<%if(right == 3){%>
<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.plSubcontractView.Update")%> " onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.plSubcontractView.Delete")%> "  onclick="javascript:on_Submit2();"  class="BUTTON">
<input type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.plSubcontractView.Back")%> " class="BUTTON" onclick="jumpURL('plStructure.jsp');">
<%}else{%>
<input type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.plSubcontractView.Back")%> " class="BUTTON" onclick="jumpURL('plStructure.jsp');">
<%}%>

</BODY>
</HTML>
<script language = "javascript">
	function on_Submit1()
	{
	
		
		
  	 	document.frm_plSubcontractUpdatePrep.submit();
	}
	function on_Submit2()
	{
		
		if (window.confirm("<%= languageChoose.getMessage("fi.jsp.plSubcontractView.Areyousure")%>") != 0) {
			document.frm_plSubcontractDelete.submit();
		}
		else{
			return;
		}	
		
	}		
</script> 
