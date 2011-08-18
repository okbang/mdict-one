<%@page import="com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML> 
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>finanDetails.jsp</TITLE>
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
	int caller=Integer.parseInt(session.getAttribute("caller").toString());
	int right = 1;//see blow
	
	String title; ;
	if(caller==Constants.SCHEDULE_CALLER){//called from shedule
		right = Security.securiPage("Schedule",request,response);
		title=languageChoose.getMessage("fi.jsp.FinanDetails.ScheduleFinance");
	}
	else { //called from project plan
		right = Security.securiPage("Project plan",request,response);
		title=languageChoose.getMessage("fi.jsp.FinanDetails.ProjectplanFinance");
	}
	
	Vector finanVt=(Vector)session.getAttribute("finanVector");
	
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	
	FinancialInfo finanInfo=(FinancialInfo)finanVt.get(vtID);
	
	String dueD="N/A";
    String actualD="N/A";
    String value="N/A";
    String note="N/A";
    if(finanInfo.condition!=null){
    	note=finanInfo.condition;
    }
    if(finanInfo.value>0){
    	value=CommonTools.formatDouble(finanInfo.value);
    }
    if(finanInfo.actualD!=null){       
        actualD=CommonTools.dateFormat(finanInfo.actualD);
    }
    if(finanInfo.dueD!=null){        		
      dueD=CommonTools.dateFormat(finanInfo.dueD);
    }
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"><%=title%></p>
<FORM method="POST" action="finanUpdate.jsp?vtID=<%=vtID%>" name="frm">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.FinanDetails.Financialplandetails")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanDetails.Item")%><INPUT size="20" type="hidden" name="txtFinanID" value="<%=finanInfo.finanID%>"></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(finanInfo.item)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanDetails.Duedate")%></TD>
            <TD class="CellBGRnews"><%=dueD%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanDetails.Actualenddate")%></TD>
            <TD class="CellBGRnews"><%=actualD%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanDetails.Amount")%>(USD)</TD>
            <TD class="CellBGRnews"><%=value%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanDetails.Note")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(note)%></TD>
        </TR>        
    </TBODY>
</TABLE>
<P>
<%if(right == 3 && !isArchive){%>
<INPUT type="submit" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.FinanDetails.Update")%>" class="BUTTON">
<INPUT type="hidden" name="vtIDstr" value="<%=vtIDstr%>">
<INPUT type="button" class="BUTTON" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.FinanDetails.Delete")%>" onclick="doDelete();">
<%}%>
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.FinanDetails.Back")%>" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER) ?Constants.SCHE_FINANCIAL_PLAN_GET_LIST:Constants.GET_FINAN_LIST%>);">

</P>
</FORM>
<SCRIPT language="javascript">
  function doDelete()
  {  	
  		if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.FinanDetails.Areyousuretodelete")%>")){   		
  			return;
  		}
  		frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_FINAN %>";
  		frm.submit();	
  	
  }
 </SCRIPT> 
</BODY>
</HTML>
