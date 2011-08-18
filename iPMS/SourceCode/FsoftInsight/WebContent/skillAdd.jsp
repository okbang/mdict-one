<%@page import="com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>skillAdd.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	Vector vtProcess = (Vector) session.getAttribute("vtProcess");

	String fullName = (String) session.getAttribute("assignmentName");
	String assignmentId = (String) session.getAttribute("assignmentId");
	
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.cmbProcess.focus();">
<P class="TITLE"> <%=languageChoose.paramText(new String[] {"fi.jsp.skillAdd.Team__evaluation__~PARAM1_FULLNAME~", fullName}) %>  </p>

<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.ADD_SKILL%>">
<TABLE class="Table" width="550" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.skillAdd.Updateskill")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.skillAdd.Process")%> </TD>
            <TD class="CellBGR3"><SELECT name="cmbProcess" class="COMBO">
           <%
           		for(int i=0;i<vtProcess.size();i++)
           		{
           			ProcessInfo psInfo=(ProcessInfo)vtProcess.get(i);
           %>
                <OPTION value="<%=psInfo.processId%>"><%=languageChoose.getMessage(psInfo.name)%></OPTION>
           <%
           		}
           %>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.skillAdd.SkillTechnology")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="60" type="text" maxlength="50" name="txtSkill"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.skillAdd.Point15")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="20" type="text" maxlength="50" name="txtPoint"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.skillAdd.Comment")%> </TD>
            <TD class="CellBGRnews"><TEXTAREA rows="5" cols="60" name="txtComment"></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="hidden" name="assignmentID" value="<%=assignmentId%>">
<INPUT type="hidden" name="fullName" value="<%=fullName%>">
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.skillAdd.OK")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.skillAdd.Cancel")%>" onclick="jumpURL('skillDetail.jsp');">
</FORM>
<SCRIPT language="javascript">
function doAction(button)
{
	var arrPoint = frm.txtPoint.value;
  	if (button.name=="btnOk") {
  		if(frm.txtSkill.value==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.skillAdd.Thisfieldismandatory")%>");
  		 	frm.txtSkill.focus();
  		 	return;
  		}
  		if(frm.txtSkill.value.length>100){
  	  		window.alert("<%= languageChoose.getMessage("fi.jsp.skillAdd.Thisfieldlengthcannotgreaterthan100")%>");
  		  	frm.txtSkill.focus(); 
  		  	return;
  	  	}
  		if(frm.txtPoint.value==""){
 			window.alert("<%=languageChoose.getMessage("fi.jsp.skillAdd.Thisfieldismandatory")%>");
  			frm.txtPoint.focus();
  			return; 
  		}
  	 	if(isNaN(frm.txtPoint.value)){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.skillAdd.Invalidnumberformat")%>");
  			frm.txtPoint.focus();
  			return;
  		}
  	 	if(frm.txtPoint.value<=0){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.skillAdd.Mustbegreaterthan0")%>");
  			frm.txtPoint.focus();
  			return;
  		}
  	 	if((frm.txtPoint.value < 1) || (frm.txtPoint.value > 5)){
  			window.alert("<%= languageChoose.getMessage("fi.jsp.skillAdd.Thepointmustbebetween1and5")%>");
  			frm.txtPoint.focus();
  			return;
  		}
  		if ((arrPoint!=1)&&(arrPoint!=2)&&(arrPoint!=3)&&(arrPoint!=4)&&(arrPoint!= 5)) {
  			alert("<%= languageChoose.getMessage("fi.jsp.skillAdd.Thepointmustbeinterger")%>");
  			frm.txtPoint.focus();
  			return;
  		}
  		if(frm.txtComment.value.length>500){
  	  		window.alert("<%= languageChoose.getMessage("fi.jsp.skillAdd.Thisfieldlengthcannotgreaterthan500")%>");
  		  	frm.txtComment.focus();
  		  	return;
  	  	}
  		
  	  	frm.submit();  		 	
  	}
}
</SCRIPT> 
</BODY>
</HTML>

