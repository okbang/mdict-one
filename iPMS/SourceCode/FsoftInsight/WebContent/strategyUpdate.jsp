<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>strategyUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%

	String strQltObj=(String)session.getAttribute("qltObjective");
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtQltObjective.focus();">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.strategyUpdate.Quality")%></p>
<FORM action="Fms1Servlet" name="frm" method="post">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.strategyUpdate.Updatestrategy")%></CAPTION>
    <TBODY>    
        <TR>            
            <TD class="CellBGRnews"><TEXTAREA rows="10" cols="90" name="txtQltObjective"><%=strQltObj%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<p>
<INPUT type="hidden" name="reqType" value=<%=Constants.UPDATE_QLT_OBJECTIVE%>>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.strategyUpdate.OK")%>" class="BUTTON" onclick="doUpdate();"> <INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.strategyUpdate.Cancel")%>" onclick="doIt(<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>);"></p></FORM>
<SCRIPT language="javascript">
function doUpdate()
  {  	
  	  if(trim(frm.txtQltObjective.value)==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.strategyUpdate.Thisfieldismandatory")%>"); 
  	  		frm.txtQltObjective.focus();
  	  		return;
  		}
    if(frm.txtQltObjective.value.length >4000){
  			alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.strategyUpdate.This__field__maximum__size__is__4000__characters__Currently__~PARAM1_OBJECTIVE~")%>",frm.txtQltObjective.value.length)));
  			frm.txtQltObjective.focus();
  			return;
  		}
  	  frm.submit();	    	    	
  }
 </SCRIPT> 
</BODY>
</HTML>
