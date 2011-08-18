<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>training.jsp</TITLE>
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
	
	String strcurPage=(String)session.getAttribute("trainCurPage");	
	String strPageTotal=(String)session.getAttribute("trainPageTotal");
	int pageTotal=Integer.parseInt(strPageTotal);
	int curPage=0;
	String direct=request.getParameter("direct");
	if(direct!=null) 
		{
			if(strcurPage!=null)curPage=Integer.parseInt(strcurPage);
			if (direct.equals("0")) curPage--;
			if (direct.equals("1")) curPage++;	
			strcurPage=String.valueOf(curPage);
			session.setAttribute("trainCurPage",strcurPage);		
		}
	if(direct==null&&strcurPage!=null)
	{
		curPage=Integer.parseInt(strcurPage);
	}
	Vector vt=(Vector)session.getAttribute("trainingVector");
%>
<BODY onload="loadPrjMenu()" class="BD">
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.training.ProjectPlanTrainingplan")%></p>
<FORM name=frm method="POST" action="trainingAdd.jsp">
<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.training.Trainingplanlist")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center" nowrap>#</TD>
            <TD width="35%"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Topic")%></TD>
            <TD width="35%"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Participants")%></TD>                       
            <TD><%=languageChoose.getMessage("fi.jsp.trainingAdd.Duration")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.trainingAdd.Waiver")%></TD>
        </TR>
        <%
		int count=curPage*20;
        int max=count+20;
        boolean flag=true;
        if(max>=vt.size()) 
        {
        	max=vt.size();
        	flag=false;        	
        }
        	boolean bl=false;
        	String rowStyle;
        	String dur;
        	for(int i=count;i<max;i++)
        	{
        		TrainingInfo trainInfo=(TrainingInfo)vt.get(i);
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				dur=(trainInfo.duration != null) ? trainInfo.duration : "";
  				if(dur.length()>50) dur=dur.substring(0,50)+"...";
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="trainingDetails.jsp?vtID=<%=i%>"><%=ConvertString.toHtml(trainInfo.topic)%></A></TD>
            <TD><%=ConvertString.toHtml(trainInfo.participant)%></TD>
            <TD><%=ConvertString.toHtml(dur)%></TD>                       
            <TD><%=ConvertString.toHtml(trainInfo.waiver)%></TD>
        </TR>
        <%}%>
        <TR>
            <TD colspan="6" class="TableLeft" align="right"><%=languageChoose.getMessage("fi.jsp.training.Page")%>:<%=curPage+1%>/<%=pageTotal%> &nbsp;<%if(curPage>0){%><A href="training.jsp?direct=0" class="TableLeft"><%=languageChoose.getMessage("fi.jsp.training.Prev")%></A> <%}%> <% if (flag){%><A href="training.jsp?direct=1"><%=languageChoose.getMessage("fi.jsp.training.Next")%></A><%}%></TD>
        </TR>
    </TBODY>
</TABLE>
<%if (right == 3 && !isArchive){%>
<P><INPUT type="submit" class="BUTTON" name="btnAdd" value="<%=languageChoose.getMessage("fi.jsp.training.Addnew")%>"></P>
<%}%>
</FORM>
</BODY>
</HTML>
