<%@page import="com.fms1.infoclass.*, com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>effWeekly.jsp</TITLE>
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
	int right = Security.securiPage("Effort",request,response);
	Vector vtWeek=(Vector)session.getAttribute("weeklyEffortVector");	
	String actualEffort=(String)session.getAttribute("actualEffort");
	String[] totalArr=(String[])session.getAttribute("totalWeeklyEffortArr");	
	
	//weekly effort page index
	String strcurPage=(String)session.getAttribute("weekECurPage");	
	String strPage=request.getParameter("page");
	int curPage=1;
	if (ConvertString.isNumber(strPage)){
		curPage= Integer.parseInt(strPage);
		session.setAttribute("weekECurPage",strPage);	
	}
	else
		curPage=Integer.parseInt(strcurPage);
				
		
	int nweeks = vtWeek.size();
	
	int pageCount;
	if ((nweeks % 20) == 0) {
		pageCount = nweeks / 20;
	}
	else {
		pageCount = nweeks / 20 + 1;
	}
	if ((curPage<1) || (curPage>pageCount))
		Fms1Servlet.callPage("error.jsp?error=Wrong page number",request,response);
%>
<BODY onload="loadPrjMenu()" class="BD">
<p class="TITLE"> <%=languageChoose.getMessage("fi.jsp.effWeekly.EffortWeeklyeffort")%> </p>
<p><%=languageChoose.getMessage("fi.jsp.EffWeekly.Unlessspecifiedtheunitforeffortmetricsispersonday")%></p>
<FORM name=frm method="POST">
<TABLE class="Table" cellspacing="1" width="95%">
    <TBODY>
        <TR  class="ColumnLabel">
            <TD>  <%=languageChoose.getMessage("fi.jsp.effWeekly.Totalplannedeffortpd")%> </TD>
            <TD>  <%=languageChoose.getMessage("fi.jsp.effWeekly.Totalactualeffortpd")%> </TD>
            <TD>  <%=languageChoose.getMessage("fi.jsp.effWeekly.Totaldeviation")%> </TD>
        </TR>
        <TR class="CellBGRnews">
            <TD align="left"><%=totalArr[0]%></TD>
            <TD align="left"><%=actualEffort%></TD>
            <TD align="left"><%=totalArr[1]%></TD>
        </TR>
    </TBODY>

</TABLE>
<p></p>

<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION class="TableCaption"><A name="weekly"> <%=languageChoose.getMessage("fi.jsp.effWeekly.Weeklyeffortdetail")%> </A><BR>
    </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD  width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.effWeekly.Date")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.effWeekly.Planned")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.effWeekly.Actual")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.effWeekly.Deviation")%> </TD>
        </TR>
        <%         	
         	boolean bl=false;
        	String rowStyle="";
        	for(int i=(curPage-1)*20;(i<curPage*20)&&(i<nweeks);i++)
        	{
        		WeeklyEffortInfo weekInfo=(WeeklyEffortInfo)vtWeek.get(i);
				rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
        %>
        <TR class="<%=rowStyle%>">
            <TD align="center"><%=i+1%></TD>
            <TD><%if(right == 3 && !isArchive){%><A href="weeklyEffortUpdate.jsp?vtID=<%=i%>"><%=CommonTools.dateFormat(weekInfo.date)%></A><%}else{%><%=CommonTools.dateFormat(weekInfo.date)%><%}%></TD>
            <TD><%=CommonTools.formatDouble(weekInfo.estimatedE)%></TD>
            <TD><%=CommonTools.formatDouble(weekInfo.actualE)%></TD>
            <TD><%=CommonTools.formatDouble(weekInfo.deviation)%></TD>
        </TR>
        <%
        	}
        %>
        <TR><a name="footer"></a>
            <TD colspan="5" class="TableLeft" align="right"><%=languageChoose.getMessage("fi.jsp.EffWeekly.Page")%>:<%=curPage%>/<%=pageCount%> &nbsp;<%if(curPage>1){%>
            <A href="effWeekly.jsp?page=<%=curPage-1%>#footer" ><%=languageChoose.getMessage("fi.jsp.EffWeekly.Prev") %></A> 
            <%}
			 if (curPage<pageCount){%>
			 <A href="effWeekly.jsp?page=<%=curPage+1%>#footer"><%=languageChoose.getMessage("fi.jsp.EffWeekly.Next") %></A>
			 <%}%></TD>
        </TR>
    </TBODY>
</TABLE>
<p></p>
</FORM>
</BODY>
</HTML>
