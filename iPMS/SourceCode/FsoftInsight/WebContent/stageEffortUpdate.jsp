<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>stageEffortUpdate.jsp</TITLE>
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
	Vector vt=(Vector)session.getAttribute("stageEffortVector");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	StageEffortInfo info=(StageEffortInfo)vt.get(vtID);

%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtEstimated.focus();">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.EffortStageeffort")%></p>
<p><%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Unlessspecifiedtheunitforeffortmetricsispersonday")%></p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.UPDATE_STAGE_EFFORT%>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Updatestageeffort")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Stage")%><INPUT size="20" type="hidden" name="vtID" value="<%=vtIDstr%>"></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(info.stage)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Norm")%></TD>
            <TD class="CellBGRnews"><%=CommonTools.formatDouble(info.norm)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Planned")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtEstimated" value="<%=CommonTools.updateDouble(info.estimated)%>" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Replanned")%></TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtReEstimated" value="<%=CommonTools.updateDouble(info.reEstimated)%>" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Actual")%></TD>
            <TD class="CellBGRnews"><%=CommonTools.formatDouble(info.actual)%></TD>
        </TR>        
    </TBODY>
</TABLE>
<BR>
<%if(!isArchive){%>	
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Ok")%>" class="BUTTON" onclick="doAction(this)">
<%}%>
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Cancel")%>" onclick="doAction(this)">
</FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnOk") {
  	  	if(frm.txtEstimated.value!="")
  	  	{
  	 	 	if(isNaN(frm.txtEstimated.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Invalidnumberformat")%>");
  				frm.txtEstimated.focus();  		
  				return;  
  			}	
  	 	 	if(frm.txtEstimated.value<=0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Mustbegreaterthan0")%>");
  				frm.txtEstimated.focus();  		
  				return;  
  			}	
  		}
  		else{
  			window.alert("<%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Youmustinputinmandatoryfields")%>");
  			frm.txtEstimated.focus();  		
  			return;  
  		}
  		if(frm.txtReEstimated.value!="")
  	  	{
  	 	 	if(isNaN(frm.txtReEstimated.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Invalidnumberformat")%>");
  				frm.txtReEstimated.focus();  		
  				return;  
  			}	
  	 	 	if(frm.txtReEstimated.value<=0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Mustbegreaterthan0")%>");
  				frm.txtReEstimated.focus();  		
  				return;  
  			}	
  			if(frm.txtEstimated.value==""){
  	  			window.alert("<%=languageChoose.getMessage("fi.jsp.stageEffortUpdate.Youcannotinputreestimatedeffortwithoutestimatedeffort")%>");
  	  			frm.txtEstimated.focus();
  	  			return;
  	  		}
  		}	
  	  	
  	  	frm.submit();
  	}
  	if (button.name=="btnCancel") {
  		doIt(<%=Constants.EFF_STAGE_GET_LIST%>);
  		return;
  	} 	
}
 </SCRIPT> 
</BODY>
</HTML>
