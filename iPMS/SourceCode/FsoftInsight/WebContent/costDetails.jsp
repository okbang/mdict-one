<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,com.fms1.tools.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML> 
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>costDetails.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD> 
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
	int right = Security.securiPage("Project plan",request,response);
	
	CostInfo costInfo=(CostInfo)session.getAttribute("costInfo");	
	DecimalFormat decfm=new DecimalFormat("##0.##");
	
    String type=languageChoose.getMessage("fi.jsp.costDetails.Labour");
    if(costInfo.type==0) type=languageChoose.getMessage("fi.jsp.costDetails.NonLabour");
    String cost="N/A";
	String effort="N/A";
    if(costInfo.effort>0){
		effort=decfm.format(costInfo.effort);
	}
	if(costInfo.cost>0){
		cost=decfm.format(costInfo.cost);
	}

%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.CostDetails.ProjectplanFinance")%></p>
<FORM method="POST" action="Fms1Servlet?reqType=<%=Constants.DELETE_COST%>#cost" name="frm">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.CostDetails.Projectcostdetails")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostDetails.Activity")%><INPUT size="20" type="hidden" name="txtCostID" value="<%=costInfo.costID%>"></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(costInfo.act)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostDetails.Type")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(type)%></TD>
        </TR>        
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostDetails.Effort")%> (pd)</TD>
            <TD class="CellBGRnews"><%=effort%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.CostDetails.Projectcost")%> (USD)</TD>
            <TD class="CellBGRnews"><%=cost%></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<%
if(right == 3 && !isArchive){
%>
<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.CostDetails.Update")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.CostDetails.Delete")%>" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.CostDetails.Cancel")%>" onclick="doAction(this)">
<%
}
else{
%>
		<INPUT type="button" class="BUTTON" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.CostDetails.Back")%>" onclick="doAction(this)">
<%
}
%>
</FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnUpdate"){  	
  		frm.action="costUpdate.jsp";
  		frm.submit();
  		
  	}
  	if (button.name=="btnDelete") 
  	{
  		if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.CostDetails.Areyousuretodelete")%>")){   		
  			return;
  		}
  		//frm.action="Frm1Servlet?reqType=<%=Constants.DELETE_COST %>";
  		frm.submit();
  	}
  	if (button.name=="btnBack") {
  		doIt(<%=Constants.GET_FINAN_LIST%>);
  		return;
  	} 	
  	
  }
 </SCRIPT> 
</BODY>
</HTML>
