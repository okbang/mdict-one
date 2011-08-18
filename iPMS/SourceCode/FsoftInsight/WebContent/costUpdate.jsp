<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,com.fms1.tools.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>costUpdate.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	
	CostInfo costInfo=(CostInfo)session.getAttribute("costInfo");	
	DecimalFormat decfm=new DecimalFormat("##0.##");
	
    String cost="";
	String effort="";
    if(costInfo.effort>0){
		effort=decfm.format(costInfo.effort);
	}
	if(costInfo.cost>0){
		cost=decfm.format(costInfo.cost);
	}

%>
<BODY class="BD" <%if(costInfo.type==1){%>onload="show();loadPrjMenu()"<%}else{%>onload="hide();loadPrjMenu()"<%}%>>
<P class="TITLE"><%= languageChoose.getMessage("fi.jsp.CostUpdate.ProjectplanFinance")%></p>
<FORM method="POST" action="Fms1Servlet?reqType=<%=Constants.UPDATE_COST%>#cost" name="frm">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.CostUpdate.Updateprojectcost")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostUpdate.Activity")%>*<INPUT size="20" type="hidden" name="txtCostID" value="<%=costInfo.costID%>"></TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtActivity"><%=costInfo.act%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostUpdate.Type")%></TD>
            <TD class="CellBGRnews">
            <INPUT type="radio" name="txtType" value="1"<%if(costInfo.type==1){%> checked<%}%> onclick="show();"> <%=languageChoose.getMessage("fi.jsp.costUpdate.Labour")%> <BR>
            <INPUT type="radio" name="txtType" value="0"<%if(costInfo.type==0){%> checked<%}%> onclick="hide();"> <%=languageChoose.getMessage("fi.jsp.costUpdate.NonLabour")%> </TD>
        </TR> 
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostUpdate.Projectcost")%> (USD) </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtCost" value="<%=cost%>" id="idCost" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostUpdate.Effort")%> (pd) </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtEffort" value="<%=effort%>" id="idEffort" class="numberTextBox"></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.CostUpdate.OK")%>" class="BUTTON" onclick="doAction(this)"> <INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.CostUpdate.Cancel")%>" onclick="doAction(this)">
</FORM>
<SCRIPT language="javascript">
  var ef=false;

  function show(){
		showObj('idEffort');		
		ef=true;	
		frm.txtActivity.focus();	
  }
  function hide(){
  		hideObj('idEffort');  				
		ef=false;
		frm.txtActivity.focus();
  }
  
  function doAction(button)
  {
  	if (button.name=="btnOk"){  
  		
  		if(trim(frm.txtActivity.value)=="") 	
  		{
  			window.alert("<%=languageChoose.getMessage("fi.jsp.CostUpdate.Thisfieldismandatory")%>");
  			frm.txtActivity.focus();  		
  			return; 
  			
  		}
		if (!maxLength(frm.txtActivity,"<%=languageChoose.getMessage("fi.jsp.CostUpdate.Activity1")%>",200))
			 return;
  		if(ef){	
  			if(trim(frm.txtEffort.value)!="")	{
  				if (!positiveFld(frm.txtEffort,"<%=languageChoose.getMessage("fi.jsp.CostUpdate.Effort1")%>"))	
  					return;
  			}
  		}	  		
  		
  		if(trim(frm.txtCost.value)!="")	
  		{	
			if (!positiveFld(frm.txtCost,"<%=languageChoose.getMessage("fi.jsp.CostUpdate.Projectcost1")%>"))
				return;
  		}  		
  		frm.submit();
  		
  	}
  	if (button.name=="btnCancel") {
  		jumpURL('Fms1Servlet?reqType=<%=Constants.GET_FINAN_LIST%>');  		
  	} 	
  	
  }
 </SCRIPT> 
</BODY>
</HTML>
