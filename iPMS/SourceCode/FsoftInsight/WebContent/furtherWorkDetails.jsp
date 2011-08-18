<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>futherWorkAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = Security.securiPage("Project reports",request,response);
	Vector vt=(Vector)session.getAttribute("furtherWorkVt");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	DecimalFormat decFm=new DecimalFormat("##0.##");
	
	FurtherWorkInfo info=(FurtherWorkInfo)vt.get(vtID);	
	String time="";
	if(info.time!=-1){
		time=decFm.format(info.time);
	}
	
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Furtherwork")%> </p>
<FORM method="POST" name="frm" action="furtherWorkUpdate.jsp?vtID=<%=vtID%>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Updatefurtherwork")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Item")%> </TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(info.name)%><INPUT size="20" type="hidden" name="txtFurtherWorkID" value="<%=info.fwID%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Resulttobedone")%> </TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(info.result)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Timetododay")%> </TD>
            <TD class="CellBGRnews"><%=time%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Responsibility")%> </TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(info.responsibility)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Note")%> </TD>
            <TD class="CellBGRnews"><%=(info.note.equals("N/A"))?"":ConvertString.toHtml(info.note)%></TD>
        </TR>        
    </TBODY>
</TABLE>
<P><%if(right == 3){%>
<INPUT type="submit" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Update")%>" class="BUTTON">
<INPUT type="button" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Delete")%>" class="BUTTON" onclick="doDelete();">
<%}%> 
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Back")%>" onclick="doIt(<%=Constants.GET_POST_MORTEM%>);"></P> 
</FORM>
<SCRIPT language="javascript">
  function doDelete(){
  	  if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.furtherWorkDetails.Areyousuretodelete")%>")){		  			
  			return;
  	  }		
  	  frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_FURTHER_WORK%>#furtherwork";		
  	  frm.submit();	    	
  }
 </SCRIPT>
</BODY>
</HTML>

