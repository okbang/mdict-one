<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></Script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>conversionUpdate.jsp</TITLE>
<%
	String temp=request.getParameter("languageNo");
	String error=request.getParameter("error");
	int languageNo;	
	//check for bad parameters
	if ((temp==null)||(temp.equals("0"))||!ConvertString.isNumber(temp))
		languageNo=1;
	else
		languageNo=Integer.parseInt(temp);
	Vector conversionList= (Vector)session.getAttribute("conversionList");
	String[] languageDetail=(String[])conversionList.elementAt(languageNo);
	String[] header=(String[])conversionList.elementAt(0);
%>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadAdminMenu()" class="BD">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.conversionUpdate.Conversiontable")%> </P>
<FORM action="Fms1Servlet" name="frm">
<%if (error!=null){%>
<P class="ERROR"><%=languageChoose.getMessage(error)%><P>
<%}%>
<TABLE class="Table" cellspacing="1" width="560">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.conversionUpdate.Updateconversion")%> </CAPTION>
    <COL span="1" width="160">
    <COL span="1" width="400">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage(header[0])%>*</TD><TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="languageName" value="<%=languageDetail[0]%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage(header[1])%>*</TD><TD class="CellBGR3"><INPUT size="11" type="text" maxlength="10" name="unit" value="<%=languageDetail[1]%>"></TD>
        </TR>
        <%
        int j = 0;
        for (int i = 3; i < header.length-1; i++) {
        %>
        <TR>
            <%
            if (header[i].trim().equalsIgnoreCase("UML User Case Point")||header[i].trim().equalsIgnoreCase("UML Use Case Point"))
            {
            	j = i;
            %>
	            <TD class="ColumnLabel"><%=header[i]%>*</TD>
	            <TD class="CellBGR3"><INPUT id="100" size="11" type="text" maxlength="11" name="estMethod" style="text-align: right" value="<%=languageDetail[i]%>"></TD>
            <%
            }else {
            %>
	            <TD class="ColumnLabel"><%=header[i]%></TD>
	            <TD class="CellBGR3"><INPUT id="101" size="11" type="text" maxlength="11" name="estMethod" style="text-align: right" value="<%=languageDetail[i].equals("N/A")?"":languageDetail[i]%>"></TD>
            <%
            }
            %>
        </TR>
        <%
        }
        %>
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage(header[header.length-1])%></TD>
        	<TD class="CellBGR3">
        		<INPUT type = "checkbox" name = "chkCommonUsed" value = 1 <%=("Yes".equals(languageDetail[header.length-1])?"checked":"")%>>
        	</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage(header[2])%></TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="note"><%=languageDetail[2].equals("N/A")?"":languageDetail[2]%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<P><INPUT type="button" onclick="onOK();" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.conversionUpdate.OK")%> " class="BUTTON">
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.conversionUpdate.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.CONVERSION_LIST_INIT%>)"></P>
<INPUT type="hidden" name="reqType">
<INPUT type="hidden" name="languageNo" value="<%=languageNo%>">
</FORM>

<SCRIPT language="javascript">
function onOK() {
	if (trim(frm.languageName.value)== "")
	{
		window.alert("<%=languageChoose.getMessage("fi.jsp.conversionUpdate.Thisfieldismandatory")%>");
		frm.languageName.focus();
		return;
	}
	if (trim(frm.unit.value) == "")
	{
		window.alert("<%=languageChoose.getMessage("fi.jsp.conversionUpdate.Thisfieldismandatory")%>");
		frm.unit.focus();
		return;
	}
	
	for (i = 0; i < <%=header.length -4%>; i++) {
		if ((frm.estMethod[i].id == "100") && ((trim(frm.estMethod[i].value) == "")||(frm.estMethod[i].value<=0))) {
			window.alert("<%=languageChoose.getMessage("fi.jsp.conversionUpdate.UCPMustBeStrictlyPositive")%>");
			frm.estMethod[i].focus();
			return;
		}
		
		if ((frm.estMethod[i].value != "") && isNaN(frm.estMethod[i].value)) {
			window.alert("<%=languageChoose.getMessage("fi.jsp.conversionUpdate.ValueOfMethodMustBeANumber")%>");
			frm.estMethod[i].focus();
			return;
		}
	}
	
	if (frm.note.value.length > 200)
	{
		window.alert("<%=languageChoose.getMessage("fi.jsp.conversionUpdate.Lengthofthisfieldmustbelessthan200")%>");
		frm.note.focus();
		return;
	}
	
	frm.reqType.value = "<%=Constants.CONVERSION_UPDATE%>";
	frm.submit();
}
</SCRIPT>
</BODY>

</HTML>
