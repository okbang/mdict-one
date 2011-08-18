<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>defectDPLog.jsp</TITLE>
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
	int right = Security.securiPage("Defects",request,response); 
	Vector dpVt=(Vector)session.getAttribute("vtDefectLog");
	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));

	if (workUnitID == Parameters.SQA_WU){
%>
<BODY onload="loadSQAMenu();" class="BD">
<%}else{%>
<BODY onload="loadPrjMenu('Defect','DP Log')" class="BD">
<%}%>
	
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.defectDPLog.DefectPreventionDPLog")%> </p>

<P align="right"><B><A href='Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=Group/DPActions.jsp' target='DPActions'> <%=languageChoose.getMessage("fi.jsp.defectDPLog.ExporttoExcel")%> </A></B></P>
<FORM method="POST" name="frm">
<TABLE class="Table" width="99%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.defectDPLog.DefectPreventionlog")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectDPLog.DPTaskAction")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectDPLog.DPCode")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectDPLog.Assignto")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectDPLog.Commdef.Code")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectDPLog.Targetdate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectDPLog.Status")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectDPLog.Closeddate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectDPLog.Isintime")%> </TD>
        </TR>
        <%	
        	Date now =new Date();
        	String isOnTime ;
        	boolean bl = true;
        	String rowStyle = "";
        	for(int i=0;i<dpVt.size();i++){
       			rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				DPLogInfo info = (DPLogInfo) dpVt.get(i);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="dpLogUpdate.jsp?vtID=<%=i%>"><%=info.dpaction%></A></TD>
            <TD NOWRAP><%=info.dpcode%></TD>
            <TD><%=info.devAccount%></TD>
            <TD NOWRAP><%=(info.commonDefCode == null)? "N/A": info.commonDefCode%></TD>
            <TD NOWRAP><%=CommonTools.dateFormat(info.targetDate)%></TD>
            <TD>
            <%
            	switch (info.dpStatus) {
            	case 0: %> <%=languageChoose.getMessage("fi.jsp.defectDPLog.Open")%> <%break;
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.defectDPLog.Closed")%> <%break;
            	}
            %>
            </TD>
            <TD NOWRAP><%=CommonTools.dateFormat(info.closedDate)%></TD>
            <TD>
            <%	
				if ((info.closedDate==null && now.compareTo(info.targetDate) > 0)||(info.closedDate!=null && info.closedDate.compareTo(info.targetDate) > 0 ))
					isOnTime = languageChoose.getMessage("fi.jsp.defectDPLog.No");
				else if (info.closedDate!=null)
					isOnTime = languageChoose.getMessage("fi.jsp.defectDPLog.Yes");
				else
					isOnTime = "N/A";
            %>
			<%=isOnTime%>
            </TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<%if (right == 3 && !isArchive){%>
<P><INPUT type="button" class="BUTTON" name="btnAddDPLog" value=" <%=languageChoose.getMessage("fi.jsp.defectDPLog.Addnew")%> " onclick="addDPLog();"></P>
<%}%>

<SCRIPT language="javascript">
	function addDPLog(){
		frm.action="dpLogAdd.jsp";
		frm.submit();
	}
</SCRIPT> 
</BODY>
</HTML>
