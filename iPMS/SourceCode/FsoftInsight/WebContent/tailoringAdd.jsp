<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
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
<BODY class="BD" onload = "loadPrjMenu();document.frm_tailoringAdd.tailoring_modification;">
<%
    int flag =0; 
	int tailID1=0;
	String[] sTailID = (String[] ) request.getParameterValues("tailID1");
	Vector proTailoringList = (Vector)session.getAttribute("ProTailoringList");
	ProTailoringInfo proTailoringInfo = new ProTailoringInfo();
	Vector catList = (Vector) session.getAttribute("tailoring_category");
	String tailoring_source = (String) session.getAttribute("tailoring_source");
	if(tailoring_source == null) tailoring_source = "0";
    String[] s = request.getParameterValues("tailID1");
	for (int i=0; i<s.length; i++)
	flag=CommonTools.parseInt(s[0]);
	String title;
	if(tailoring_source.equals("1")){ //MANU: called from project plan
		title=languageChoose.getMessage("fi.jsp.tailoringAdd.ProjectPlan");
	}
	else { //MANU: called from menu
		title= languageChoose.getMessage("fi.jsp.tailoringAdd.Tailoring");
	}
%>
<P class="TITLE"><%=title%></P><br>
<form name="frm_tailoringAdd" action="Fms1Servlet#tailoring" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.TAILORING_ADD%>">
<input type = "hidden" name="tailoring_tdID" value="1">
<input type = "hidden" name="projectID" value="<%=session.getAttribute("projectID")%>">

<input type = "hidden" name="flag" value="<%=flag%>">
<input type = "hidden" name="reqType">
<input type = "hidden" name="reqType1">
<DIV align="left">
<P class="TableCaption"><%=languageChoose.getMessage("fi.jsp.tailoringAdd.AddnewTailoring")%></P> 
<TABLE cellspacing="1" class="Table" width="95%" >
<TBODY>
 	<TR>
        <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoring.Description")%>*</TD>
        <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoringAdd.Action")%></TD>
        <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoring.AppCri")%>*</TD>
        <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoringAdd.Type")%></TD>
        <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.tailoringAdd.Category")%></TD>
        <TD class="ColumnLabel" width="25%"><%=languageChoose.getMessage("fi.jsp.tailoring.Reason")%></TD>
    </TR>
<%
	for (int i=0; i<s.length; i++) {
		String className = (i%2 == 0) ?"CellBGRnews":"CellBGR3";
		flag=CommonTools.parseInt(s[i]);
		proTailoringInfo = (ProTailoringInfo) proTailoringList.elementAt(Integer.parseInt(sTailID[i]));
%>		
		<TR>
			<TD><input type = "hidden" name="process_tailID" value="<%=proTailoringInfo.TailoringID%>"> </TD>
			<TD><input type = "hidden" name="action" value="<%=proTailoringInfo.action%>"> </TD>
		</TR>
        <TR>
        	<TD  class="<%=className%>"><%=proTailoringInfo.Tailoring_per%></TD>
            <TD class="<%=className%>"><SELECT name="tailoring_action" class="COMBO" disabled>
                 <OPTION value="1" <%if(proTailoringInfo.action == 1){%>="" selected <%}%>=""><%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Added")%> </OPTION>
                 <OPTION value="2" <%if(proTailoringInfo.action == 2){%>="" selected <%}%>=""><%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Modified")%> </OPTION>
                 <OPTION value="3" <%if(proTailoringInfo.action == 3){%>="" selected <%}%>=""><%=languageChoose.getMessage("fi.jsp.tailoringUpdate.Deleted")%> </OPTION>
            </SELECT>
			</TD>
            <TD class="<%=className%>" ><%=proTailoringInfo.Applicable_Cri%></TD>
            <TD class="<%=className%>">Tailoring</TD>
            <TD class="<%=className%>" ><%=proTailoringInfo.ProcessName%></TD>
            <TD class="<%=className%>" ><TEXTAREA name="tailoring_note" rows="5" cols="30" ></TEXTAREA></TD>
        </TR>
     <%}%>   
    </TBODY>
</TABLE>

</DIV>
</form>
<br>
<INPUT type="button" name="Add" value="<%=languageChoose.getMessage("fi.jsp.tailoringAdd.OK")%>" onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.tailoringAdd.Cancel")%>" class="BUTTON" onclick="doIt(<%=(CommonTools.parseInt(request.getParameter("reqType1"))==Constants.PL_LIFECYCLE_GET_PAGE)?Constants.PL_LIFECYCLE_GET_PAGE:Constants.TAILORING_GET_LIST%>);">
<INPUT type="hidden" name="reqType">
</BODY>
</HTML>
<script language = "javascript">
	function on_Submit()
	{
        var form = document.frm_tailoringAdd;
        if (<%=s.length%> > 1) {
			for (var i = 0; i < <%=s.length%>; i++) {
				if (form.tailoring_note[i].value.length >200) {
					alert("<%=languageChoose.getMessage("fi.jsp.tailoringAdd.Thetextfornotecannot")%>");
					frm_tailoringAdd.tailoring_note[i].focus();
					return;
				}
		    }
		} else {
			if (form.tailoring_note.value.length>200) {
				alert("<%= languageChoose.getMessage("fi.jsp.tailoringAdd.Thetextfornotecannot")%>");
				frm_tailoringAdd.tailoring_note.focus();
				return;
			}
			
		}    
		frm_tailoringAdd.reqType.value="<%=request.getParameter("reqType1")%>";
		document.frm_tailoringAdd.submit();
	}	
</script> 