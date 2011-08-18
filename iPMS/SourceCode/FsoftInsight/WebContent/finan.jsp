<%@page import="com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>finan.jsp</TITLE>
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
	Vector finanVt=(Vector)session.getAttribute("finanVector");
	Vector costVt=(Vector)session.getAttribute("costVector");
	CostTotalInfo totalInfo=(CostTotalInfo)session.getAttribute("costTotalInfo");	
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.finan.ProjectplanFinance")%></p>
<FORM method="POST" name="frm">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.finan.Financialplanlist")%></CAPTION>
    <TBODY >
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.finan.Item")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.finan.Duedate")%></TD>           
            <TD><%=languageChoose.getMessage("fi.jsp.finan.Amount")%>  (USD) </TD>
            <TD width="30%"><%=languageChoose.getMessage("fi.jsp.finan.Note")%></TD>
        </TR>
        <%
        	String dueD="N/A";
        	String amount="N/A";
        	String note="N/A";
        	boolean bl=true;
        	String rowStyle="";
        	double totalAmount = 0;
        	
        	for(int i=0;i<finanVt.size();i++)
        	{
        		if(bl) rowStyle="CellBGRnews";
  				else rowStyle = "CellBGR3";
  				bl=!bl;
  				dueD="N/A";
  				amount="N/A";
  				note="N/A";
        		FinancialInfo finanInfo=(FinancialInfo)finanVt.get(i);
        		if(finanInfo.dueD!=null){
        			dueD=CommonTools.dateFormat(finanInfo.dueD);
        		}    
        		if(finanInfo.condition!=null) {
        			note= finanInfo.condition;
        			if(note.length()>50) note=note.substring(0,50)+"...";   
        		}	
        		if(finanInfo.value>0){
					amount=CommonTools.formatDouble(finanInfo.value);
					totalAmount = totalAmount + finanInfo.value;
        		}
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="finanDetails.jsp?vtID=<%=i%>"><%=ConvertString.toHtml(finanInfo.item)%></A></TD>
            <TD><%=dueD%></TD>            
            <TD><%=amount%></TD>
            <TD><%=ConvertString.toHtml(note)%></TD>
        </TR>
        <%
        	}
        %>
        <TR class="TableLeft">
            <TD></TD>
            <TD><B> <%=languageChoose.getMessage("fi.jsp.finan.Total")%> </B></TD>
            <TD></TD>            
            <TD><B><%=CommonTools.formatDouble(totalAmount)%><B></TD>
            <TD></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<%if (right == 3 && !isArchive){%>
<INPUT type="button" name="btnAdd1" value="<%=languageChoose.getMessage("fi.jsp.finan.Addnew")%>" class="BUTTON" onclick="doAction(this)"> <BR>
<%}%>
<BR>
<BR>
<BR>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><A name="cost"><%=languageChoose.getMessage("fi.jsp.finan.Projectcost")%></A></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="24" align="center">#</TD>
            <TD class="ColumnLabel" width="35%"><%=languageChoose.getMessage("fi.jsp.finan.Activity")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.finan.Type")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.finan.Effort")%> (pd) </TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.finan.Projectcost")%> (USD) </TD>
        </TR>
        <%
        	String type="Labour";
        	String cost="N/A";
			String effort="N/A";
			totalAmount = 0;
        	double totalEffort = 0;
			
        	for(int i=0;i<costVt.size();i++)
        	{
        		if(bl) rowStyle="CellBGRnews";
  				else rowStyle = "CellBGR3";
  				bl=!bl;
  				type=languageChoose.getMessage("fi.jsp.finan.Labour");
  				cost="N/A";
  				effort="N/A";
        		CostInfo costInfo=(CostInfo)costVt.get(i);
        		
        		if(costInfo.type==0)type=languageChoose.getMessage("fi.jsp.finan.Nonlabour");
        		
        		if(costInfo.effort>0){
					effort=CommonTools.formatDouble(costInfo.effort);
					totalEffort = totalEffort + costInfo.effort;
				}
				
				if(costInfo.cost>0){
					cost=CommonTools.formatDouble(costInfo.cost);
					totalAmount = totalAmount + costInfo.cost;
				}
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="Fms1Servlet?reqType=<%=Constants.GET_COST%>&costID=<%=costInfo.costID%>"><%=ConvertString.toHtml(costInfo.act)%></A></TD>
            <TD><%=ConvertString.toHtml(type)%></TD>
            <TD><%if(costInfo.type==1){%><%=effort%><%}%>&nbsp;</TD>
            <TD><%=cost%></TD>
        </TR>
        <%
        	}
        %>
        <TR class="TableLeft">
            <TD></TD>
            <TD><B> <%=languageChoose.getMessage("fi.jsp.finan.Total1")%> </B></TD>
            <TD></TD>
            <TD><B><%=CommonTools.formatDouble(totalEffort)%><B></TD>
            <TD><B><%=CommonTools.formatDouble(totalAmount)%><B></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<%if (right == 3 && !isArchive){%>
<INPUT type="button" name="btnAdd2" value="<%=languageChoose.getMessage("fi.jsp.finan.Addnew")%>" class="BUTTON" onclick="doAction(this)"><BR>
<%}%>
<BR>
<BR>
<BR>
<DIV align="left">
<TABLE width="95%" class="Table" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.finan.Totalsforprojectcost")%></CAPTION>
    <TBODY class="CellBGRnews">
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.finan.Totaleffort")%> (pd) </TD>
            <TD><%=CommonTools.formatDouble(totalInfo.effort)%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.finan.Totallabourcost")%> (USD) </TD>
            <TD><%=CommonTools.formatDouble(totalInfo.labour)%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.finan.Totalnonlabourcost")%> (USD)</TD>
            <TD><%=CommonTools.formatDouble(totalInfo.n_labour)%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.finan.TotalCost")%>(USD) </TD>
            <TD><%=CommonTools.formatDouble(totalInfo.cost)%></TD>
        </TR>
    </TBODY>
</TABLE>
</DIV>
</FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnAdd1") frm.action="finanAdd.jsp";
  	if (button.name=="btnAdd2") frm.action="costAdd.jsp";
  	frm.submit();
  	
  }
</SCRIPT>
<P><BR>
</P>
</BODY>
</HTML>
