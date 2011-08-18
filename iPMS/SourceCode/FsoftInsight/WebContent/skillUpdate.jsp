<%@page import="com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>skillUpdate.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
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
	Vector skillList = (Vector) session.getAttribute("vtSkills");

	String fullName = (String) session.getAttribute("assignmentName");
	
	String vtIDstr = request.getParameter("vtID");
	int vtID = Integer.parseInt(vtIDstr);
	
 	SkillInfo info  = (SkillInfo) skillList.get(vtID);
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtSkill.focus();">
<P class="TITLE"> <%=languageChoose.paramText(new String[]{"fi.jsp.skillUpdate.Team__evaluation__~PARAM1_FULLNAME~", fullName})%></p>

<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.UPDATE_SKILL%>">
<TABLE class="Table" width="550" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.skillUpdate.Updateskill")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.skillUpdate.Process")%> </TD>
            <TD class="CellBGRnews"><%=languageChoose.getMessage(info.process)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.skillUpdate.SkillTechnology")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="60" type="text" maxlength="50" name="txtSkill" value="<%=info.skill%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.skillUpdate.Point15")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="20" type="text" maxlength="50" name="txtPoint" value="<%=info.point%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.skillUpdate.Comment")%> </TD>
            <TD class="CellBGRnews"><TEXTAREA rows="5" cols="60" name="txtComment"><%=((info.skillComment == null)? "" : info.skillComment)%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="hidden" name="assignmentID" value="<%=info.assignmentId%>">
<INPUT type="hidden" name="fullName" value="<%=fullName%>">
<INPUT type="hidden" name="projectSkillId" value="<%=info.projectSkillId%>">
<%if(!isArchive){%>	
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.skillUpdate.OK")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.skillUpdate.Delete")%>" onclick="doDelete();">
<%}%>
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.skillUpdate.Cancel")%>" onclick="javascript:window.history.back();">
</FORM>
<SCRIPT language="javascript">
function doAction(button)
{
	var arrPoint = frm.txtPoint.value;
  	if (button.name=="btnOk") {
  		if(frm.txtSkill.value==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.skillUpdate.Thisfieldismandatory")%>");
  		 	frm.txtSkill.focus();
  		 	return;
  		}
  		if(frm.txtSkill.value.length>100){
  	  		window.alert("<%= languageChoose.getMessage("fi.jsp.skillUpdate.Thisfieldlengthcannotgreaterthan100")%>");
  		  	frm.txtSkill.focus(); 
  		  	return;
  	  	}
  		if(frm.txtPoint.value==""){
 			window.alert("<%=languageChoose.getMessage("fi.jsp.skillUpdate.Thisfieldismandatory")%>");
  			frm.txtPoint.focus();  		
  			return;  
  		}
  	 	if(isNaN(frm.txtPoint.value)){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.skillUpdate.Invalidnumberformat")%>");
  			frm.txtPoint.focus();
  			return;
  		}
  	 	 if(frm.txtPoint.value<=0){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.skillUpdate.Mustbegreaterthan0")%>");
  			frm.txtPoint.focus();
  			return;
  		}
  	 	if((frm.txtPoint.value < 1) || (frm.txtPoint.value > 5)){
  			window.alert("<%= languageChoose.getMessage("fi.jsp.skillUpdate.Thepointmustbebetween1and5")%>");
  			frm.txtPoint.focus();
  			return;
  		}
  		if ((arrPoint!=1)&&(arrPoint!=2)&&(arrPoint!=3)&&(arrPoint!=4)&&(arrPoint!= 5)) {
  			alert("<%= languageChoose.getMessage("fi.jsp.skillUpdate.Thepointmustbeinterger")%>");
  			frm.txtPoint.focus();
  			return;
  		}
  		if(frm.txtComment.value.length>500){
  	  		window.alert("<%= languageChoose.getMessage("fi.jsp.skillUpdate.Thisfieldlengthcannotgreaterthan500")%>");
  		  	frm.txtComment.focus(); 
  		  	return;
  	  	}
  		
  	  	frm.submit();  		 	
  	}
}
function doDelete()
{  	
	if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.skillUpdate.Areyousuretodelete")%>")){   		
		return;
	}
	frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_SKILL%>";
	frm.submit();	
}
</SCRIPT> 
</BODY>
</HTML>

