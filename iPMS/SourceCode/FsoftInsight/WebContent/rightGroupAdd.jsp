<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>rightGroupAdd.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src=jscript/javaFns.js></script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="loadAdminMenu();doOnload();" class="BD" >
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Roles")%></p>
<FORM name="frm" method="POST">
<%
	Vector vt=(Vector)session.getAttribute("getPageVector");
	if(vt!=null)
	{
	
%> 

<CENTER></CENTER>
<DIV align="left">
<TABLE cellspacing="1" class = "Table" width="80%">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Addnewrole")%></CAPTION>
    <TBODY align="center">
        <TR>
            <TD colspan="6" class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Roleinformation")%></TD>
        </TR>
        <TR>
            <TD colspan="5" class="CellBGR3" align="left" width="120" nowrap><%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Rolename")%>*</TD>
            <TD  align = "left"class="CellBGRnews" nowrap><INPUT type="text" name="rightGroupName" maxlength="20" size="30"></TD>
        </TR>
        <TR>
            <TD colspan="5" class="CellBGR3" align="left" width="120" nowrap><%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Description")%></TD>
            <TD  align = "left"class="CellBGRnews" nowrap><INPUT size="40" type="text" name="description" maxlength="50"></TD>
        </TR>
        <TR>
            <TD colspan="6" class = "ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Rightmanagement")%> </TD>
        </TR>
        <TR>
            <TD colspan="6" class="CellBGR3" align="left"><%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Mode")%> <B> 1 </B>: <%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Noright")%><BR>
            <%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Mode")%> <B> 2 </B>: <%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Viewonlyright")%><BR>
           <%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Mode")%> <B> 3 </B>: <%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Managerright")%></TD>
        </TR>
        <TR>
            <TD colspan="3" class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Mode")%>
            </TD>
            <TD rowspan="2" colspan="3" class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Page")%> </TD>
        </TR>
        <TR class="ColumnLabel">
            <TD> 1 </TD>
            <TD> 2 </TD>
            <TD> 3 </TD>
        </TR>
<%
	boolean bl=true;
	String className="";
	for(int i=0;i<vt.size();i++)
	{
		PageInfor pageInfor=(PageInfor)vt.get(i);

			if(bl) className="CellBGRnews";
  			else className = "CellBGR3";
  			bl=!bl;		
%>
        <TR>
            <TD class="<%=className%>"><INPUT type="radio" name="<%=ConvertString.replace(pageInfor.name.trim()," ","_")%>" value="1" checked onclick="doAction1(this);"></TD>
            <TD class="<%=className%>"><INPUT type="radio" name="<%=ConvertString.replace(pageInfor.name.trim()," ","_")%>" value="2" onclick="doAction1(this);"></TD>
            <TD class="<%=className%>"><INPUT type="radio" name="<%=ConvertString.replace(pageInfor.name.trim()," ","_")%>" value="3" onclick="doAction1(this);"></TD>
<% 			if 	(pageInfor.name.lastIndexOf("home") !=-1) {//left column for home pages%>
			<TD align="left" class="<%=className%>" colspan="2"><%=pageInfor.name%></TD>
			<TD  class="<%=className%>">&nbsp;</TD>
<%			}
			else{%>
			<TD align="left" class="<%=className%>" colspan="2">&nbsp;</TD>
			<TD align="left" class="<%=className%>" ><%=pageInfor.name%></TD>
<%			}%>       
            
        </TR>
        <%
	}
%>
    </TBODY>
</TABLE>
</DIV>
<CENTER></CENTER>
<BR>
<p align="left">
<INPUT type="button"  name="OK" value="<%=languageChoose.getMessage("fi.jsp.rightGroupAdd.OK") %>" onclick="doAction(this)" class="BUTTON">&nbsp; <INPUT type="button"  name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Cancel") %>" onclick="doAction(this)" class="BUTTON"> </p>
<%
	}
%>
</FORM>
<SCRIPT language="javascript">
function doOnload(){
<% if (request.getParameter("error") != null){%>
window.alert("<%=languageChoose.getMessage("fi.jsp.rightGroupAdd.Rolenamealreadyexistspleasechooseanothername")%>");
<%}%>
frm.rightGroupName.focus();
return;
}
  function doAction1(RB)
  {

  	var a = parseInt(RB.value)-1;
  	//var e;
  	if(RB.name=="Organization_home"){
  	
		<%for(int i=0;i<vt.size();i++)
		{
			PageInfor pageInfor =(PageInfor)vt.elementAt(i);
			if  (pageInfor.type ==1){%>
	  			frm.<%=ConvertString.replace(pageInfor.name.trim()," ","_")%>[a].checked =true;			
			<%}
	 	}%>	
  	}
   	if(RB.name=="Group_home"){
   		
 		<%for(int i=0;i<vt.size();i++)
 		{
			PageInfor pageInfor =(PageInfor)vt.elementAt(i);
			if  (pageInfor.type ==2){%>
	  			frm.<%=ConvertString.replace(pageInfor.name.trim()," ","_")%>[a].checked =true;			
			<%}
 	 	}%>	
  	}
  	if(RB.name=="Project_home"){
  	
		<%for(int i=0;i<vt.size();i++)
		{
			PageInfor pageInfor =(PageInfor)vt.elementAt(i);
			if  (pageInfor.type ==3){%>
	  			frm.<%=ConvertString.replace(pageInfor.name.trim()," ","_")%>[a].checked =true;			
			<%}
	 	}%>	
  	}
  	if(RB.name=="Admin_home"){
	  		
			<%for(int i=0;i<vt.size();i++)
			{
			PageInfor pageInfor =(PageInfor)vt.elementAt(i);
			if  (pageInfor.type ==4){%>
	  			frm.<%=ConvertString.replace(pageInfor.name.trim()," ","_")%>[a].checked =true;			
			<%}
		 	}%>	
  	}
  }	
  function doAction(button)
  {
  	if (button.name=="OK") 
  	{
  		frm.action="Fms1Servlet?reqType=<%=Constants.ADDNEW_RIGHT_GROUP%>";
  		if (trim(frm.rightGroupName.value)=="")
  		{
  			window.alert("<%=languageChoose.getMessage("fi.jsp.rightGroupAdd.YoumustinputRolename")%>");  			
  			frm.rightGroupName.focus();
  			return;
  			
  		}
  		frm.submit(); 	
  	}
  	if (button.name=="Cancel") 
  	{
		window.open("rightGroup.jsp","main");
	} 	
  	
  }
 </SCRIPT> 
</BODY>
</HTML>
