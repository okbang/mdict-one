<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>toolDetails.jsp</TITLE>
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
	ToolInfo toolInfo=(ToolInfo)session.getAttribute("toolInfo");
    String source=(toolInfo.source==null)?"N/A":toolInfo.source;
    String des=(toolInfo.description==null)?"N/A":toolInfo.description;
    String note=(toolInfo.note==null)?"N/A":toolInfo.note;
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.toolDetails.ProjectPlanInfrastructures")%></p>
<BR>
<FORM method="POST" action="Fms1Servlet?reqType=<%=Constants.UPDATE_TOOL%>" name="frm">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.toolDetails.Infrastructuredetails")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="180"><%=languageChoose.getMessage("fi.jsp.toolDetails.WorkProduct")%><INPUT size="20" type="hidden" name="txtToolID" value="<%=toolInfo.toolID%>"></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(toolInfo.name)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolDetails.Purpose")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(toolInfo.purpose)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolDetails.Source")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(source)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolDetails.Description")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(des)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolDetails.Status")%></TD>
            <TD class="CellBGRnews"><%=languageChoose.getMessage(toolInfo.status)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolDetails.ExpectedAvailabilityBy")%></TD>
            <TD class="CellBGRnews"><%= (toolInfo.expected_available_stage == null)? "N/A" : ConvertString.toHtml(toolInfo.expected_available_stage)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolDetails.ActualAvailabilityBy")%></TD>
            <TD class="CellBGRnews"><%=(toolInfo.actual_available_stage == null)? "N/A" : ConvertString.toHtml(toolInfo.actual_available_stage)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.toolDetails.Type")%> </TD>
            <TD class="CellBGRnews">
            <%
            	String tool_type = "N/A";
            	Long ltt = new Long(toolInfo.tool_type);
            	switch (ltt.intValue()) {
            		case 1: 
            			tool_type = languageChoose.getMessage("fi.jsp.toolDetails.OS");
            			break;
            		case 2: 
            			tool_type = languageChoose.getMessage("fi.jsp.toolDetails.DBMS");
            			break;
            		case 3: 
            			tool_type = languageChoose.getMessage("fi.jsp.toolDetails.Languages");
            			break;
            		case 4: 
            			tool_type = languageChoose.getMessage("fi.jsp.toolDetails.Tools");
            			break;
            		case 5: 
            			tool_type = languageChoose.getMessage("fi.jsp.toolDetails.Hardwares");
            			break;
            		case 6 :
            			tool_type = languageChoose.getMessage("fi.jsp.toolDetails.DevelopmentEnvironment");
            			break; 
            		case 7 :
            			tool_type = languageChoose.getMessage("fi.jsp.toolDetails.Hardware_N_Software");
            			break;
            		case 8 :
            			tool_type = languageChoose.getMessage("fi.jsp.toolDetails.OtherTools");
            			break;
            		default: 
            			tool_type = "N/A";
            			break;
            	}
            %>
            <%=tool_type%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolDetails.Note")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(note)%></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<%if (right==3 && !isArchive){%>
<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.toolDetails.Update")%>" class="BUTTON" onclick="doAction(this)"> <INPUT type="button" class="BUTTON" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.toolDetails.Delete")%>" onclick="doAction(this)"> <%}%><INPUT type="button" class="BUTTON" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.toolDetails.Back")%>" onclick="doIt(<%=Constants.GET_TOOL_LIST%>);"></FORM>
<%if (right==3 && !isArchive){%>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnUpdate") {
  		frm.action="toolUpdate.jsp";
  		frm.submit();
  	}
  	if (button.name=="btnDelete"){
  	
  		if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.toolDetails.Areyousuretodelete")%>")){  		
  			return;
  		}
  		
  		frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_TOOL%>";
  		frm.submit();
  	}

  	
  }
</SCRIPT>
 <%}%>
</BODY>
</HTML>
