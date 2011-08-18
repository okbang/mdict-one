<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<TITLE>rightGroupDetail.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src=jscript/javaFns.js></script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Roles")%></p>
<BODY onLoad="loadAdminMenu();frm.description.focus();" class="BD">
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.UPDATE_RIGHT_GROUP%>" >
<%
	Vector vt=(Vector)session.getAttribute("getRightForPageVector");
	RightGroupInfor rgInfor=(RightGroupInfor)session.getAttribute("getRightGroup");
	if(vt!=null&&rgInfor!=null)
	{	
		
%>
<DIV align="left">
<TABLE  cellspacing="1" class = "Table" width="80%">
    <CAPTION align="left" class = "TableCaption"><%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Updaterole")%></CAPTION>
    <TBODY align="center">
        <TR>
            <TD colspan="6" class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Roleinformation")%></TD>
        </TR>
        <TR>
            <TD colspan="5" class="CellBGR3" align="left"><%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Rolename")%>*</TD>
            <TD align="left" class="CellBGRnews"><INPUT  type="hidden" name="rightGroupName" value="<%=rgInfor.rightGroupID%>"><%=ConvertString.toHtml(rgInfor.rightGroupID)%></TD>
        </TR>
        <TR>
            <TD colspan="5" class="CellBGR3" align="left"><%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Description")%></TD>
            <TD align="left" class="CellBGRnews"><INPUT size="34" type="text" name="description" value="<%=(rgInfor.description.equalsIgnoreCase("N/A")?"":rgInfor.description)%>" maxlength="50"></TD>
        </TR>
        <TR>
            <TD colspan="6" class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Rolemanagement")%> </TD>
        </TR>
        <TR>
            <TD colspan="6" class="CellBGR3" align="left"><%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Mode")%> <B> 1 </B>: <%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Noright")%><BR>
            <%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Mode")%> <B>  2 </B>: <%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Viewonlyright")%><BR>
            <%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Mode")%> <B> 3 </B>: <%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Managerright")%> </TD>
        </TR>
        <TR>
            <TD colspan="3" class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Mode")%></TD>
            <TD rowspan="2" colspan="3" class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Page")%></TD>
        </TR>
        <TR class="ColumnLabel">
            <TD> 1 </TD>
            <TD> 2 </TD>
            <TD> 3 </TD>
        </TR>
        <%
        String className = "";
        boolean bl=true;
	for(int i=0;i<vt.size();i++)
	{
		RightForPageInfor rfpInfor=(RightForPageInfor)vt.get(i);		

		className=(bl)?"CellBGRnews":"CellBGR3";
  		bl=!bl;				
	%>	<TR  class="<%=className%>">
            <TD><INPUT type="radio" name="<%=ConvertString.replace(rfpInfor.pageName2.trim()," ","_")%>" value="1" <%=((rfpInfor.privilege==1)?"checked":"")%> onclick="doAction1(this);"></TD>
            <TD><INPUT type="radio" name="<%=ConvertString.replace(rfpInfor.pageName2.trim()," ","_")%>" value="2" <%=((rfpInfor.privilege==2)?"checked":"")%> onclick="doAction1(this);"></TD>
            <TD><INPUT type="radio" name="<%=ConvertString.replace(rfpInfor.pageName2.trim()," ","_")%>" value="3" <%=((rfpInfor.privilege==3)?"checked":"")%> onclick="doAction1(this);"></TD>
<% 			if 	(rfpInfor.pageName2.lastIndexOf("home") !=-1) {//left column for home pages%>
			<TD align="left" colspan="2"><%=rfpInfor.pageName2%></TD>
			<TD>&nbsp;</TD>
<%			}else{%>
			<TD align="left" colspan="2">&nbsp;</TD>
			<TD align="left" ><%=rfpInfor.pageName2%></TD>
<%			}%>       
            
        </TR>
<%	}%>
    </TBODY>
</TABLE>
<BR>
</DIV>
<DIV align="left"><INPUT type="submit" name="OK" value="<%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.OK") %>" class="BUTTON"> <INPUT type="button" name="Back" value="<%=languageChoose.getMessage("fi.jsp.rightGroupUpdate.Cancel") %>" onclick="doAction(this);" class="BUTTON"></DIV>
<%}%>

</FORM>
<SCRIPT language="javascript">
  function doAction1(RB){
  	var a = parseInt(RB.value)-1;
  	//var e;
  	if(RB.name=="Organization_home"){
  	
		<%for(int i=0;i<vt.size();i++){
			RightForPageInfor rfpInfor =(RightForPageInfor)vt.elementAt(i);
			if  (rfpInfor.level ==1){%>
	  			frm.<%=ConvertString.replace(rfpInfor.pageName2.trim()," ","_")%>[a].checked =true;			
			<%}
	 	}%>	
  	}
   	if(RB.name=="Group_home"){
   		
 		<%for(int i=0;i<vt.size();i++){
 			RightForPageInfor rfpInfor =(RightForPageInfor)vt.elementAt(i);
 			if  (rfpInfor.level ==2){%>
 	  			frm.<%=ConvertString.replace(rfpInfor.pageName2.trim()," ","_")%>[a].checked =true;			
 			<%}
 	 	}%>	
  	}
  	if(RB.name=="Project_home"){
  	
		<%for(int i=0;i<vt.size();i++){
			RightForPageInfor rfpInfor =(RightForPageInfor)vt.elementAt(i);
			if  (rfpInfor.level ==3){%>
	  			frm.<%=ConvertString.replace(rfpInfor.pageName2.trim()," ","_")%>[a].checked =true;			
			<%}
	 	}%>	
  	}
  	if(RB.name=="Admin_home"){
	  		
			<%for(int i=0;i<vt.size();i++){
				RightForPageInfor rfpInfor =(RightForPageInfor)vt.elementAt(i);
				if  (rfpInfor.level ==4){%>
		  			frm.<%=ConvertString.replace(rfpInfor.pageName2.trim()," ","_")%>[a].checked =true;			
				<%}
		 	}%>	
  	}
  }	
  function doAction(button)  {  	  		
  	if (button.name=="Back") 
  		doIt(<%=Constants.GET_RIGHT_GROUP_LIST%>);
  }
 </SCRIPT> 
</BODY>
</HTML>
