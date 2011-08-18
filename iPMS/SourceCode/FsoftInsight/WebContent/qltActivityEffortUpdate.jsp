<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>qltActivityEffortUpda.jsp</TITLE>
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
	Vector vt=(Vector)session.getAttribute("qltActivityEffortVector");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	QltActivityEffortInfo info = (QltActivityEffortInfo) vt.get(vtID);
	
	String estimated="";  				
  	String reEstimated="";
  	String actual="";
  	
    if((info.estimated!=-1)&&(!(info.estimated==0)))
        estimated=CommonTools.formatDouble(info.estimated);
    if((info.reEstimated!=-1)&&(!(info.reEstimated==0)))
       	reEstimated=CommonTools.formatDouble(info.reEstimated);
    if((info.actual!=-1)&&(!(info.actual==0)))
       	actual=CommonTools.formatDouble(info.actual);
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtEstimated.focus();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.EffortQualityactivityeffort")%> </p>
<p><%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Unlessspecifiedtheunitforeffortmetricsispersonday")%></p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.UPDATE_QLT_ACTIVITY_EFFORT%>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Updatequalityactivityeffort")%>  </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Activity")%> <INPUT size="20" type="hidden" name="vtID" value="<%=vtIDstr%>"></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(info.activity)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Norm")%> </TD>
            <TD class="CellBGRnews"><%=info.norm%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Planned")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtEstimated" value="<%=estimated%>" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Replanned")%> </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtReEstimated" value="<%=reEstimated%>" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Actual")%> </TD>
            <TD class="CellBGRnews"><%if(info.type!=4){%><INPUT size="11" type="text" maxlength="11" name="txtActual" value="<%=actual%>" class="numberTextBox"><%}else{%><%=actual%><INPUT size="11" type="hidden" maxlength="11" name="txtActual" value="<%=actual%>"><%}%></TD>
        </TR>        
    </TBODY>
</TABLE>
<BR>
<%if(!isArchive){%>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.OK") %>" class="BUTTON" onclick="doAction(this)"> 
<%}%>
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Cancel") %>" onclick="doAction(this)">
</FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
<%if(!isArchive){%>
  	if (button.name=="btnOk") {
  	  	if(frm.txtEstimated.value!="")
  	  	{
  	 	 	if(isNaN(frm.txtEstimated.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Invalidnumberformat") %>");
  				frm.txtEstimated.focus();  		
  				return;  
  			}	
 
  	 	 	if(frm.txtEstimated.value <0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Mustbegreaterthan0")%>");
  				frm.txtEstimated.focus();  		
  				return;  
  			}	
  		}	
  		else{
  				window.alert("<%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Thisfieldismandatory") %>");
  	  			frm.txtEstimated.focus();
  	  			return;
  		}
  		if(frm.txtReEstimated.value!="")
  	  	{
  	 	 	if(isNaN(frm.txtReEstimated.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Invalidnumberformat")%>");
  				frm.txtReEstimated.focus();  		
  				return;  
  			}	
 
  	 	 	if(frm.txtReEstimated.value < 0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Mustbegreaterthan0")%>");
  				frm.txtReEstimated.focus();  		
  				return;  
  			}

  		}
  		if(frm.txtActual.value!="")	{
  	
  			if(isNaN(frm.txtActual.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Invalidnumberformat")%>");
  				frm.txtActual.focus();  		
  				return;
  			}
  			if(frm.txtActual.value<0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.qltActivityEffortUpdate.Mustbegreaterthan0")%>");
  				frm.txtActual.focus();  		
  				return;
  			}
  	  	}	
  	  	
  	  	frm.submit();  		 	
  	}
<%}%>
  	if (button.name=="btnCancel") {
  		doIt(<%=Constants.EFF_QUALITY_ACTIVITY_GET_LIST%>);
  		return;
  	} 	
}
 </SCRIPT> 
</BODY>
</HTML>

