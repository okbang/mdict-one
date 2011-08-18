<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>scheOtherQualityAct.jsp</TITLE>
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
	Vector vtOtherAct=(Vector)session.getAttribute("otherActVector");

%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.scheOtherQualityAct.ScheduleOtherqualityactivities")%> </P>
<FORM method="post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_SCHEDULE_HEADER%>" name="frm">
<TABLE width="95%" cellspacing="1" class="Table">
    <TBODY>
        <TR class="ColumnLabel">
            <TD align="center" width="24">#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.scheOtherQualityAct.Activity")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.scheOtherQualityAct.Plannedstartdate")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.scheOtherQualityAct.Plannedenddate")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.scheOtherQualityAct.Actualenddate")%></TD>
        </TR>
        <%
        	boolean bl=true;
        	String rowStyle;
        	String activity;
        	String aEndD;
			String pStartD;
			String pEndD="N/A";
			OtherActInfo oaInfo;
        for(int i=0;i<vtOtherAct.size();i++){  
        	oaInfo=(OtherActInfo)vtOtherAct.get(i);
   			activity=oaInfo.activity==null?"N/A":ConvertString.trunc(oaInfo.activity,50);
       		pStartD=CommonTools.dateFormat(oaInfo.pStartD);
       		pEndD=CommonTools.dateFormat(oaInfo.pEndD);
       		aEndD=CommonTools.dateFormat(oaInfo.aEndD);
        	rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  			bl=!bl;   			
        %>
        <TR class="<%=rowStyle%>">
        	<TD align="center" width="24"><%=i+1%></TD>
            <TD><A href="otherActivityDetails.jsp?vtID=<%=i%>"><%=ConvertString.toHtml(activity)%></A></TD>
            <TD><%=pStartD%></TD>  
            <TD><%=pEndD%></TD>
            <TD><%=aEndD%></TD>       
        </TR>
        <%}%>        
    </TBODY>
</TABLE>
<p><%if(right == 3 && !isArchive){%><INPUT type="button" name="btnAddAct" value="<%=languageChoose.getMessage("fi.jsp.scheOtherQualityAct.Addnew")%>" class="BUTTON" onclick="addOtherAct();"><%}%></P>
</form>
<SCRIPT language="javascript">
function addOtherAct(){
	frm.action="otherActivityAdd.jsp";
	frm.submit();
}
</script>
</BODY>
</HTML>
