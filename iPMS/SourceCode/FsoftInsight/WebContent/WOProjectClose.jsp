<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language="javascript">
	<%@include file="javaFns.jsp"%>
	</SCRIPT>
	<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
	<STYLE TYPE="text/css">
		<%@include file="stylesheet/fms.css"%>
	</STYLE>
	<TITLE>WOProjectClose.jsp</TITLE>
</HEAD>
<%LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");%>
<BODY class="BD" onload="loadPrjMenu()">
<%	
	ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("WOGeneralInfo");
%>
<BR><BR><BR>
<FORM name="frm" method="POST">
<INPUT type="hidden" name="txtStartDate" value="<%=CommonTools.dateFormat(projectInfo.getStartDate())%>">
<DIV align="left">
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION align="left" class = "TableCaption"><%=languageChoose.getMessage("fi.jsp.woProjectClose.CloseCancelProject")%></CAPTION>
	<TBODY>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woProjectClose.Status")%>*</TD>
			<TD class="CellBGR3">
				<SELECT name = "cboStatus" class="COMBO">
					<OPTION value="<%=ProjectInfo.STATUS_CLOSED%>"><%=languageChoose.getMessage("fi.jsp.woProjectClose.Closed")%></OPTION>
					<OPTION value="<%=ProjectInfo.STATUS_CANCELLED%>"><%=languageChoose.getMessage("fi.jsp.woProjectClose.Cancelled")%></OPTION>
				</SELECT>
			</TD>
		</TR>
    <TR>
        <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woProjectClose.ActualFinishDate")%>*</TD>
        <TD class="CellBGR3">
            <INPUT name="txtActualFinish" size="10" value="" maxlength="9"> <%="(dd-mmm-yy)"%>
        </TD>
    </TR>
    <TR>
		<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woProjectClose.Description")%></TD>
            <TD class="CellBGR3"><TEXTAREA rows="10" cols="70" name="txtDescription"></TEXTAREA>
            </TD>
        </TR>
	</TBODY>
</TABLE>
</DIV>
<BR>
<P align="left">
	<INPUT type="button"  name="OK" value="<%=languageChoose.getMessage("fi.jsp.workUnitAdd.OK")%>" onclick="doSave()" class="BUTTON">&nbsp;
	<INPUT type="button"  name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.workUnitAdd.Cancel")%>" onclick="doBack()" class="BUTTON">
</p>
</FORM>
<SCRIPT language="javascript">
	function doSave(){
  		if (trim(frm.txtActualFinish.value) == ""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.woProjectClose.YouMustInputActualFinishDate")%>");
  			frm.txtActualFinish.focus();
  			return;
  		}
  		var _actualEndDate = trim(frm.txtActualFinish.value);
  		if (!isDate(_actualEndDate)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.woProjectClose.InvalidActualFinishDate")%>");
  			frm.txtActualFinish.focus();
  			return ;
  		}
  		if (compareToToday(_actualEndDate) == 1){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.woProjectClose.ActualFinishDateMustBeBeforeOrEqualToDay")%>");
  			frm.txtActualFinish.focus();
  			return;
  		}
  		if (frm.txtStartDate.value != ""){
	  		if (compareDate(frm.txtStartDate.value, _actualEndDate) == -1){
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.woProjectClose.ActualFinishDateMustBeAfterOrEqualStartDate")%>(" + frm.txtStartDate.value + ")");
	  			frm.txtActualFinish.focus();
	  			return;
	  		}
  		}
  		frm.action = "Fms1Servlet?reqType=<%=Constants.WO_CLOSE_CANCEL_PROJECT%>";
		frm.submit();
    }
	function doBack(){
		doIt(<%=Constants.WO_GENERAL_INFO_GET_LIST%>);
	}
</SCRIPT>
</BODY>
</HTML>