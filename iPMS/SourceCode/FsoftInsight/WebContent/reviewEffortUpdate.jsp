<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>reviewEffortUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	Vector vt=(Vector)session.getAttribute("reviewEffortVector");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	ReviewEffortInfo info=(ReviewEffortInfo)vt.get(vtID);
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtEstimated.focus();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.EffortRevieweffort")%> </p>
<p><%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Unlessspecifiedtheunitforeffortmetricsispersonday")%></p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.UPDATE_REVIEW_EFFORT %>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Revieweffortupdate")%> </CAPTION>
    <INPUT type="hidden" name="txtReviewEffortID" value="<%=info.reviewE_ID%>">
    <INPUT type="hidden" name="txtModuleID" value="<%=info.moduleID%>">
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Modulename")%> </TD>
            <TD class="CellBGRnews"><%=info.moduleName%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Norm")%> </TD>
            <TD class="CellBGRnews"><%=CommonTools.formatDouble(info.norm)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Planned")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtEstimated" value="<%=CommonTools.updateDouble(info.estimated)%>" class="numberTextBox"></TD>
        </TR>        
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Replanned")%> </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtReEstimated" value="<%=CommonTools.updateDouble(info.reEstimated)%>" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Actual")%> </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtActual" value="<%=CommonTools.updateDouble(info.actual)%>" class="numberTextBox"></TD>
        </TR>        
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.OK")%>" class="BUTTON" onclick="doAction(this)"> <INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Cancel")%>" onclick="doIt(<%=Constants.EFF_REVIEW_GET_LIST%>);">
</FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnOk") {
  	  	if(frm.txtEstimated.value!=""){
  	 	 	if(isNaN(frm.txtEstimated.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Invalidnumberformat")%>");
  				frm.txtEstimated.focus();  		
  				return;  
  			}	
  	 	 	if(frm.txtEstimated.value<0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Mustbegreaterthan0")%>");
  				frm.txtEstimated.focus();  		
  				return;  
  			}	
  		} 
  		else 
  		{
  				window.alert("<%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Thisfieldismandatory")%>");
  	  			frm.txtEstimated.focus();
  	  			return;
  		}	 		
  		if(frm.txtReEstimated.value!="")
  	  	{
  	 	 	if(isNaN(frm.txtReEstimated.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Invalidnumberformat")%>");
  				frm.txtReEstimated.focus();  		
  				return;  
  			}	
  	 	 	if(frm.txtReEstimated.value<0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Mustbegreaterthan0")%>");
  				frm.txtReEstimated.focus();  		
  				return;  
  			}	
  		}
  		if(frm.txtActual.value!="")	{
  	 	 	if(isNaN(frm.txtActual.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Invalidnumberformat")%>");
  				frm.txtActual.focus();  		
  				return;  
  			}	
  			if(frm.txtActual.value<0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.reviewEffortUpdate.Mustbegreaterthan0")%>");
  				frm.txtActual.focus();  		
  				return;
  			}
  	  	}
  	  	frm.submit();  		 	
  	}
}
 </SCRIPT> 
</BODY>
</HTML>

