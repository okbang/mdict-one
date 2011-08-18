<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>tool.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}

	int right = Security.securiPage("Project plan",request,response);
	int tSize = 0;
	Vector vt=(Vector)session.getAttribute("toolVector");
	if (vt != null )  tSize = vt.size();
	
	if (tSize > 14)
	{
%>
<BODY onload="loadPrjMenu();makeScrollableTable('tableTool',true,'auto')" class="BD">
<%} else {%>
<BODY class="BD" onload="loadPrjMenu()">
<%}%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.tool.ProjectPlanInfrastructures")%></p>
<br/>
<FORM method="POST" action="Fms1Servlet?reqType=<%=Constants.PREPARE_ADD_TOOL%>" name="frm">
<TABLE class="Table" width="95%" cellspacing="1" id="tableTool">
<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.tool.InfrastructuresList")%></CAPTION>
	<THEAD>
	<TR class="ColumnLabel">
	    <TD width="3%" align="center">#</TD>
	    <TD><%=languageChoose.getMessage("fi.jsp.tool.WorkProduct")%></TD>
	    <TD width="24%"><%=languageChoose.getMessage("fi.jsp.tool.Purpose")%></TD>            
	    <TD width="130"><%=languageChoose.getMessage("fi.jsp.tool.ExpectedAvailabilityBy")%></TD>
	    <TD width="20%"><%=languageChoose.getMessage("fi.jsp.tool.Note")%></TD>	    
	</TR>
	</THEAD>
    <TBODY>
        <%
        String className;
        String pur="";
        String note="";
        int currentType;
		int nextType;
		int classType = 0;
		
        ToolInfo toolInfo=null;        
        
        for(int i = 0; i < (tSize-1); i++)
        {        	
  			pur="N/A";
  			note="N/A";  			
        	
        	toolInfo=(ToolInfo)vt.get(i);
        	currentType = new Long(toolInfo.tool_type).intValue();
        	if (currentType > 8 || currentType < 6) currentType = 9;
		 	nextType = new Long(((ToolInfo) vt.elementAt(i+1)).tool_type).intValue();
		 	if (nextType > 8 || nextType < 6) nextType = 9;
		 	className=(classType%2==0)? "CellBGR3":"CellBGRnews";
		 	
        	if(toolInfo.purpose!=null){
        		pur=toolInfo.purpose;        		
        		if(pur.length()>50) pur=pur.substring(0,50)+"...";
        	}
        	if(toolInfo.note!=null){
        		note=toolInfo.note;
        		if(note.length()>50) note=note.substring(0,50)+"..."; 
        	}
        	if (i == 0) {
        %>
        <TR class="CellBGRLongnews" style="font-weight: bold">
			<TD colspan="5">&nbsp;
			<%
				switch(currentType){
					case 6:%><%=languageChoose.getMessage("fi.jsp.tool.DevelopmentEnvironment")%><%break;
					case 7:%><%=languageChoose.getMessage("fi.jsp.tool.Hardware_N_Software")%><%break;
					case 8:%><%=languageChoose.getMessage("fi.jsp.tool.OtherTools")%><%break;
					case 9:%><%=languageChoose.getMessage("fi.jsp.tool.OldDataType")%><%break;
				}
			%>
			</TD>
		</TR>
        <%	
			}
			if (currentType != nextType ) {
		%>
        <TR class="<%=className%>">
            <TD align="center"><%=i+1%></TD>
            <TD><A href="Fms1Servlet?reqType=<%=Constants.GET_TOOL%>&toolID=<%=toolInfo.toolID%>"><%=ConvertString.toHtml(toolInfo.name)%></A></TD>
            <TD><%=ConvertString.toHtml(pur)%></TD>
            <TD><%=(toolInfo.expected_available_stage == null) ? "N/A": ConvertString.toHtml(toolInfo.expected_available_stage)%></TD>
            <TD><%=ConvertString.toHtml(note)%></TD>
        </TR>
        <TR class="CellBGRLongnews" style="font-weight: bold">
			<TD colspan="5">&nbsp;
			<%
				switch(nextType){
					case 6:%><%=languageChoose.getMessage("fi.jsp.tool.DevelopmentEnvironment")%><%break;
					case 7:%><%=languageChoose.getMessage("fi.jsp.tool.Hardware_N_Software")%><%break;
					case 8:%><%=languageChoose.getMessage("fi.jsp.tool.OtherTools")%><%break;
					case 9:%><%=languageChoose.getMessage("fi.jsp.tool.OldDataType")%><%break;
				}
			%>
			</TD>
		</TR>
<%
				classType = 0;
			} else {
				classType++;
				
%>			
		<TR class="<%=className%>">
            <TD align="center"><%=i+1%></TD>
            <TD><A href="Fms1Servlet?reqType=<%=Constants.GET_TOOL%>&toolID=<%=toolInfo.toolID%>"><%=ConvertString.toHtml(toolInfo.name)%></A></TD>
            <TD><%=ConvertString.toHtml(pur)%></TD>
            <TD><%=(toolInfo.expected_available_stage == null) ? "N/A": ConvertString.toHtml(toolInfo.expected_available_stage)%></TD>
            <TD><%=ConvertString.toHtml(note)%></TD>
        </TR>
<%			
			}
		}// end for
		if (tSize > 0) {
			toolInfo = (ToolInfo) vt.elementAt(tSize-1);
			className=(tSize%2==0)? "CellBGR3":"CellBGRnews";
			if(toolInfo.purpose!=null){
				pur=toolInfo.purpose;        		
				if(pur.length()>50) pur=pur.substring(0,50)+"...";
	    	}
	    	if(toolInfo.note!=null){
	    		note=toolInfo.note;
	    		if(note.length()>50) note=note.substring(0,50)+"..."; 
	    	}
	    	if (tSize == 1) {
	    		currentType = new Long(toolInfo.tool_type).intValue();
	        	if (currentType > 8 || currentType < 6) currentType = 9;
%>
		<TR class="CellBGRLongnews" style="font-weight: bold">
			<TD colspan="5">&nbsp;
			<%
				switch(currentType){
					case 6:%><%=languageChoose.getMessage("fi.jsp.tool.DevelopmentEnvironment")%><%break;
					case 7:%><%=languageChoose.getMessage("fi.jsp.tool.Hardware_N_Software")%><%break;
					case 8:%><%=languageChoose.getMessage("fi.jsp.tool.OtherTools")%><%break;
					case 9:%><%=languageChoose.getMessage("fi.jsp.tool.OldDataType")%><%break;
				}
			%>
			</TD>
		</TR>
<%
 			} 		
%>
		<TR class="<%=className%>">
            <TD align="center"><%=tSize%></TD>
            <TD><A href="Fms1Servlet?reqType=<%=Constants.GET_TOOL%>&toolID=<%=toolInfo.toolID%>"><%=ConvertString.toHtml(toolInfo.name)%></A></TD>
            <TD><%=ConvertString.toHtml(pur)%></TD>
            <TD><%=(toolInfo.expected_available_stage == null) ? "N/A": ConvertString.toHtml(toolInfo.expected_available_stage)%></TD>
            <TD><%=ConvertString.toHtml(note)%></TD>
        </TR>
<%
		} 
%>        
    </TBODY>
</TABLE>
<BR>
<%if (right==3 && !isArchive){%>
<INPUT type="submit" class="BUTTON" name="btnAdd" value="<%=languageChoose.getMessage("fi.jsp.tool.Addnew")%>">
<%}%>
</FORM>
</BODY> 
</HTML>
