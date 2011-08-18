<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>scheReviewAndTestAct.jsp</TITLE>
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
	Security.securiPage("Schedule",request,response); 
	Vector vtModun=(Vector)session.getAttribute("moduleVector");
	String sortBy=(String) session.getAttribute("sortBy");
	int iSortBy= (sortBy == null)?2:Integer.parseInt(sortBy);
	
	if (vtModun.size() > 16)
	{%><BODY onload="loadPrjMenu();makeScrollableTable('tableModule',true,'auto')" class="BD">
	<%} else {%><BODY class="BD" onload="loadPrjMenu()">
<%}%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.ScheduleReviewandtestactivitie")%> </P>
<FORM name="frm2" action="Fms1Servlet" method="post">
<INPUT type="hidden" name="reqType" value="<%=Constants.SCHE_REVIEW_TEST_GET_LIST%>" >
<p class="NormalText"> <%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.Sortby")%> 
<SELECT name="orderBy" class="COMBO">
	<OPTION value ="1" <%if(iSortBy==1){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.Status")%> </OPTION>
	<OPTION value ="2" <%if(iSortBy==2){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.Planreleasedate")%> </OPTION>
	<OPTION value ="3" <%if(iSortBy==3){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.Actualreleasedate")%> </OPTION>
</SELECT>
<INPUT type="submit" class="BUTTON" name="OK" value=" <%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.OK")%> ">
</p>
</FORM>
<TABLE class="Table" cellspacing="1" width = "95%" id="tableModule">
<THEAD>
<TR class="ColumnLabel">            
    <TD><%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.Product")%></TD>
    <TD><%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.Workproduct")%></TD>
    <TD> <%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.Status1")%> </TD>
    <TD><%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.Plannedreleasedate")%></TD>
    <TD><%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.Actualreleasedate")%></TD>
    <TD><%=languageChoose.getMessage("fi.jsp.scheReviewAndTestAct.Scheduledeviation")%>(%)</TD>
</TR>
</THEAD>
<TBODY>
        <%       
          	boolean bl=false;
        	String rowStyle,reviewer,approver;
        	for(int i=0;i<vtModun.size();i++)
        	{
        		ModuleInfo modun=(ModuleInfo)vtModun.get(i);
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl; 
  				reviewer=modun.reviewer;
  				approver=modun.approver;  				
  				if(reviewer.length()>50){
  					reviewer=reviewer.substring(0,50)+"...";
  				}
  				if(approver.length()>50){
  					approver=approver.substring(0,50)+"...";
  				}
        %>
        <TR class=<%=rowStyle%>>           
            <TD><A href="moduleDetails.jsp?vtID=<%=i%>"><%=((modun.isDel)?"<B>"+modun.name+"</B>":modun.name)%></A></TD>
            <TD><%=languageChoose.getMessage(modun.wpName)%></TD>
            <TD><%=languageChoose.getMessage(modun.getStatusName())%></TD>                       
            <TD><%=CommonTools.dateFormat(modun.thePlanReleaseDate)%></TD>                         
            <TD><%=CommonTools.dateFormat(modun.actualReleaseDate)%></TD>                   
            <TD><%=CommonTools.formatDouble(modun.deviation)%></TD>
        </TR>
        <%}%>        
    </TBODY>
</TABLE>
<p>
</p>
<SCRIPT language="javascript">
//objs to hide when submenu is displayed
var objToHide=new Array(frm2.orderBy);
</SCRIPT>
</BODY>
</HTML>
