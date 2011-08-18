<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*, java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>cdefAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	String prjojectcode = (String)session.getAttribute("projCode");
	Vector dtList = (Vector)session.getAttribute("defectType");
	Vector dpVt=(Vector)session.getAttribute("vtDefectLog");
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.defdesc.focus();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Commondefects")%> </p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.COMMON_DEFECT_ADDNEW%>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Commondefect")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Commondefect1")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA rows="6" cols="50" name="defdesc"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Defecttype")%>* </TD>
            <TD class="CellBGR3">
            <SELECT name="deftype" class="COMBO">
                <OPTION value="1"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.01FunctionalityOther")%> </OPTION>
                <OPTION value="2"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.02UserInterface")%> </OPTION>
                <OPTION value="3"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.03Performance")%> </OPTION>
                <OPTION value="4"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.04Designissue")%> </OPTION>
                <OPTION value="5"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.05Codingstandard")%> </OPTION>
                <OPTION value="6"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.06Document")%> </OPTION>
                <OPTION value="7"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.07DataDatabaseintegrity")%> </OPTION>
                <OPTION value="8"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.08SecurityAccessControl")%> </OPTION>
                <OPTION value="9"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.09Portability")%> </OPTION>
                <OPTION value="10"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.10Other")%> </OPTION>
                <OPTION value="11"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.11Tools")%> </OPTION>
                <OPTION value="12"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.011Reqmisunderstanding")%> </OPTION>
                <OPTION value="13"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.012Featuremissing")%> </OPTION>
                <OPTION value="14"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.013Codinglogic")%> </OPTION>
                <OPTION value="15"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.014Businesslogic")%> </OPTION>
            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Rootcause")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="6" cols="50" name="rootcause"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Causecategory")%> </TD>
            <TD class="CellBGR3">
            <SELECT name="causecate" class="COMBO">
                <OPTION value="1" selected> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Training")%> </OPTION>
                <OPTION value="2"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Communication")%> </OPTION>
                <OPTION value="3"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Oversight")%> </OPTION>
                <OPTION value="4"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Understanding")%> </OPTION>
                <OPTION value="5"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.Other")%> </OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.cdefAdd.DPCode")%> </TD>
            <TD class="CellBGR3">
            <SELECT name="dpcode" class="COMBO">
            <OPTION value=""></OPTION>
			<%
            	for (int i = 0; i < dpVt.size(); i++) {
	            	DPLogInfo info2 = (DPLogInfo)dpVt.elementAt(i);
            %>
                <OPTION value="<%=info2.dpcode%>"><%=info2.dpcode%></OPTION>
			<%}%>
            </SELECT>
            </TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="hidden" name="prjojectcode" value="<%=prjojectcode%>">
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.cdefAdd.OK")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.cdefAdd.Cancel")%>" onclick="doAction(this)"></FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnOk") {
  	
  		if(trim(frm.defdesc.value)==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.cdefAdd.Thisfieldismandatory")%>");
  		 	frm.defdesc.focus();
  		 	return;
  		}
  		if(frm.defdesc.value.length>300){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.cdefAdd.ItemLengthCanNotGreaterThan300")%>");
  		  	frm.defdesc.focus(); 
  		  	return;
  	  	}
  		if(frm.rootcause.value.length>300){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.cdefAdd.ItemLengthCanNotGreaterThan300")%>");
  		  	frm.rootcause.focus(); 
  		  	return;
  	  	}
  		if(frm.dpcode.value.length>100){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.cdefAdd.ItemLengthCanNotGreaterThan100")%>");
  		  	frm.dpcode.focus(); 
  		  	return;
  	  	}
  		
  	  	frm.submit();  		 	
  	}
  	
  	if (button.name=="btnCancel") {
  		doIt(<%=Constants.COMMON_DEFECT%>);
  		return;
  	} 	
  }
 </SCRIPT>
</BODY>
</HTML>

