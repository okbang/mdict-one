<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>scheFinancialPlan.jsp</TITLE>
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
	int right = Security.securiPage("Schedule",request,response); 
	Vector finanVt=(Vector)session.getAttribute("finanVector");	
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.scheFinancialPlan.ScheduleFinancialplan")%> </P>
<FORM method="post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_SCHEDULE_HEADER%>" name="frm">
<TABLE class="Table" width="95%" cellspacing="1">
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.scheFinalcialPlan.Item") %></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.scheFinalcialPlan.Duedate") %></TD>           
            <TD><%=languageChoose.getMessage("fi.jsp.scheFinalcialPlan.Actualenddate")%></TD>            
        </TR>
        <%
        	String dueD="N/A";
        	String actualD="N/A";  	
        	boolean bl=true;
			String rowStyle="";

        	
        	for(int i=0;i<finanVt.size();i++)
        	{
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				dueD="N/A";
  				actualD="N/A";  				
        		FinancialInfo finanInfo=(FinancialInfo)finanVt.get(i);
        		if(finanInfo.dueD!=null){
        			dueD=CommonTools.dateFormat(finanInfo.dueD);
        		} 
        		if(finanInfo.actualD!=null){
        			actualD=CommonTools.dateFormat(finanInfo.actualD);
        		}   
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="finanDetails.jsp?vtID=<%=i%>"><%=ConvertString.toHtml(finanInfo.item)%></A></TD>
            <TD><%=dueD%></TD>            
            <TD><%=actualD%></TD>
        </TR>
        <%}%>        
    </TBODY>
</TABLE>
<p>
<%if(right == 3 && !isArchive){%><INPUT type="button" name="btnAdd1" value="<%=languageChoose.getMessage("fi.jsp.scheFinalcialPlan.Addnew") %>" class="BUTTON" onclick="addFinan();"><%}%> <BR>
</p>
</FORM>
<SCRIPT language="javascript">
function addFinan(){
	frm.action="finanAdd.jsp";
	frm.submit();
}
</script>
</BODY>
</HTML>
