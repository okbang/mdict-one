<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>costAdd.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtActivity.focus();">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.CostAdd.ProjectplanFinance")%></p>
<FORM method="POST" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_COST%>#cost" name="frm">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.CostAdd.Addnewprojectcost")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostAdd.Activity")%>*<INPUT size="20" type="hidden" name="txtCostID" ></TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtActivity"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostAdd.Type")%></TD>
            <TD class="CellBGRnews">
            <INPUT type="radio" name="txtType" value="1" checked onclick="show();"> <%=languageChoose.getMessage("fi.jsp.costAdd.Labour")%> <BR>
            <INPUT type="radio" name="txtType" value="0" onclick="hide();"> <%=languageChoose.getMessage("fi.jsp.costAdd.NonLabour")%> </TD>
        </TR>   
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostAdd.Projectcost")%> (USD)</TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtCost" id="idCost" class="numberTextBox"></TD>
        </TR>     
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostAdd.Effort")%> (pd)</TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtEffort" id="idEffort" class="numberTextBox"></TD>
        </TR>        
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.CostAdd.OK")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.CostAdd.Cancel")%>" onclick="doIt(<%=Constants.GET_FINAN_LIST%>)">
</FORM>

<SCRIPT language="javascript">
  var ef=false;

  function show(){
		showObj('idEffort');		
		ef=true;		
  }
  function hide(){
		hideObj('idEffort');		
		ef=false;
  }
  function doAction(button)
  {
  	if (button.name=="btnOk"){  
  		
    	if(trim(frm.txtActivity.value)=="") 	
  		{
  			window.alert("<%=languageChoose.getMessage("fi.jsp.CostAdd.Thisfieldismandatory")%>");
  			frm.txtActivity.focus();  		
  			return; 
  			
  		}
		if (!maxLength(frm.txtActivity,"<%=languageChoose.getMessage("fi.jsp.CostAdd.Activity1")%>",200))
			 return;
  		if(ef){	
  			if(trim(frm.txtEffort.value)!="")	{
  				if (!positiveFld(frm.txtEffort,"<%=languageChoose.getMessage("fi.jsp.CostAdd.Effort1")%>"))	
  					return;
  			}
  		}	  		
  		
  		if(trim(frm.txtCost.value)!="")	
  		{	
			if (!positiveFld(frm.txtCost,"<%=languageChoose.getMessage("fi.jsp.CostAdd.Projectcost1")%>"))
				return;
  		}  		
  		frm.submit();
  		
  	}  	
  	
  }
 </SCRIPT> 
</BODY>
</HTML>
