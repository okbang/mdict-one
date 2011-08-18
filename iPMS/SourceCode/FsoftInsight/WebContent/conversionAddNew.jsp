<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>conversionAddnew.jsp</TITLE>
<SCRIPT language="javascript" SRC="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadAdminMenu();frm.languageName.focus();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.conversionAddNew.Conversiontable")%> </P>
<%
	Vector methodList = (Vector)session.getAttribute("methodList");
	ConversionInfo conversionInfo = new ConversionInfo();
	if (request.getAttribute("ConversionAddNew") != null){
		conversionInfo =(ConversionInfo) request.getAttribute("ConversionAddNew");
	}
%>
<FORM action="Fms1Servlet" name="frm">
<TABLE class="Table" cellspacing="1" width="560">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.conversionAddNew.Addnewconversion")%> </CAPTION>
    <COL span="1" width="160">
    <COL span="1" width="400">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.conversionAddNew.Language")%>* </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="languageName" value="<%=conversionInfo.language%>"></TD>
        </TR>
        <TR>
     	   <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.conversionAddNew.Unit")%>* </TD>
            <TD class="CellBGR3"><INPUT size="11" type="text" maxlength="10" name="unit" value="<%=conversionInfo.sizeUnit%>"></TD>
        </TR>
        <%
        for (int i = 0; i < methodList.size(); i++) {
        	EstimationMethodInfo methodInfo = (EstimationMethodInfo)methodList.elementAt(i);
        %><TR>
            <%
            if (methodInfo.methodID == 3) {
            %>
            <TD class="ColumnLabel"><%=methodInfo.name%>*</TD>
            <TD class="CellBGR3"><INPUT id="100" size="11" type="text" maxlength="11" name="estMethod" style="text-align: right"></TD>
            <%
            }
            else {
            %>
            <TD class="ColumnLabel"><%=methodInfo.name%></TD>
            <TD class="CellBGR3"><INPUT id="101" size="11" type="text" maxlength="11" name="estMethod" style="text-align: right"></TD>
            <%
            }
            %>
        </TR>
        <%
        }
        %>
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.conversionAddNew.CommonUsed")%></TD>
        	<TD class="CellBGR3"><INPUT type="checkbox" name="chkCommonUsed" value = 1 <%=((conversionInfo.getCommonUsed() == 1)?"checked":"")%>></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.conversionAddNew.Note")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="note"><%=conversionInfo.note%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<P><INPUT type="button" onclick="onOK();" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.conversionAddNew.OK")%> " class="BUTTON">
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.conversionAddNew.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.CONVERSION_LIST_INIT%>)"></P>
<INPUT type="hidden" name="reqType">
</FORM>
<SCRIPT language="javascript">
<%
		if (request.getAttribute(StringConstants.CONVERSION_ADDNEW) != null){
%>
			alert("<%=languageChoose.getMessage("fi.jsp.conversionAddNew.ThisLanguageAlreadyExist")%>");
<%
			request.removeAttribute(StringConstants.CONVERSION_ADDNEW);
	}
%>

	function onOK() {
		var methodSize=<%=methodList.size()%>;
		if (trim(frm.languageName.value) == "") {
			window.alert("<%=languageChoose.getMessage("fi.jsp.conversionAddNew.Thisfieldismandatory")%>");
			frm.languageName.focus();
			return;
		}
		if (trim(frm.unit.value)==""){
			window.alert("<%=languageChoose.getMessage("fi.jsp.conversionAddNew.Thisfieldismandatory")%>");
			frm.unit.focus();
			return;
		
		}
		
		if(methodSize==1)
		{
			if ((frm.estMethod.id == "100") && ((trim(frm.estMethod.value) == "")||(frm.estMethod.value<=0))) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.conversionAddNew.UCPMustBeStrictlyPositive")%>");
				frm.estMethod.focus();
				return;
			}
	
			if ((frm.estMethod.value != "") && isNaN(frm.estMethod.value)) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.conversionAddNew.ValueForMethodMustBeANumber")%>");
				frm.estMethod.focus();
				return;
			}
		}
		else
		 {
			 for (i = 0; i < methodSize; i++) {
				if ((frm.estMethod[i].id == "100") && ((trim(frm.estMethod[i].value) == "")||(frm.estMethod[i].value<=0))) {
					window.alert("<%=languageChoose.getMessage("fi.jsp.conversionAddNew.UCPMustBeStrictlyPositive")%>");
					frm.estMethod[i].focus();
					return;
				}
		
				if ((frm.estMethod[i].value != "") && isNaN(frm.estMethod[i].value)) {
					window.alert("<%=languageChoose.getMessage("fi.jsp.conversionAddNew.ValueForMethodMustBeANumber")%>");
					frm.estMethod[i].focus();
					return;
				}
		}
		 	
		 }
		
	
		if (frm.note.value.length > 200) {
			window.alert("<%=languageChoose.getMessage("fi.jsp.conversionAddNew.Lengthofthisfieldmustbelessthan200")%>");
			frm.note.focus();
			return;
		}
	
		frm.reqType.value = "<%=Constants.CONVERSION_ADDNEW%>";
		frm.submit();	
	}
</SCRIPT>
</BODY>
</HTML>