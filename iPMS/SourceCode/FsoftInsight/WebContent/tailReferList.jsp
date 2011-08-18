<%@page import="java.util.Vector,com.fms1.infoclass.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.common.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<SCRIPT language="javascript" src='jscript/validate.js'></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<TITLE>tailReferList.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>

<%
	Vector occuredTail = (Vector) session.getAttribute("vtOccuredTail");
	String strWuID = (String) session.getAttribute("wuID");
	Vector vtGroupList = (Vector) session.getAttribute("groupList");
	Vector vtOrgList = (Vector) session.getAttribute("orgList");
	
	
	long lWuID = Parameters.FSOFT_WU; // FSOFT by default
	if (strWuID != null)
		lWuID = Long.parseLong(strWuID);

	
	int iProcess = CommonTools.parseInt((String) session.getAttribute("strProcess"));

	int j = 0;

	Vector vtProcess = (Vector)session.getAttribute("vtProcess");

	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	
	String scroll="";
	if(occuredTail.size() > 8){
		scroll="makeScrollableTable('tableId',true,'400')";
	}
%>
<BODY onLoad="loadPrjMenu();<%=scroll%>" class="BD">
<P class="TITLE">Tailoring Reference</P>

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.TAILORING_REF%>">

<TABLE width="100%" class="NormalText">
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.paRisk.ProcessSource")%>  </TD>
		<TD>
			<SELECT name="cboProcess" class="COMBO">
			<OPTION value="0" <%=(iProcess == 0 ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paRisk.All")%> </OPTION>
				<%	
					j = vtProcess.size();
					for (int i = 0; i < j; i++){
			           	ProcessInfo psInfo = (ProcessInfo)vtProcess.get(i);
				%>
			<OPTION value="<%=psInfo.processId%>"<%=(psInfo.processId == iProcess ? " selected" : "")%>><%=languageChoose.getMessage(psInfo.name)%></OPTION>
				<%}%>
			</SELECT>
		</TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.paRisk.Group")%> </TD>
		<TD>
			<SELECT name="cboGroup" class="COMBO">
				<OPTION value="-1"></OPTION>
				<%	j = vtOrgList.size();
					for (int i = 0; i < j; i++)	{
						RolesInfo groupInfo = (RolesInfo) vtOrgList.elementAt(i);
				%><OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
				<%}	
					j = vtGroupList.size();
					for (int i = 0; i < j; i++)	{
						RolesInfo groupInfo = (RolesInfo) vtGroupList.elementAt(i);
				%>
			<OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
				<%}%>
			</SELECT> </TD>
		<TD></TD>
		<TD><INPUT type="button" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.paRisk.View")%> " class="BUTTON" onclick="doAction()"></TD>
	</TR>
</TABLE>

</FORM>
<BR>
<FORM name="frmOtherRisk">
<TABLE cellspacing="1" class="Table" width="95%" id="tableId">
<CAPTION align="left" class="TableCaption"></CAPTION>
	<THEAD>
        <TR class="ColumnLabel">
            <TD width="3%" align="center">#</TD>
            <TD width="10%" align="left"> <%=languageChoose.getMessage("fi.jsp.paRisk.Project")%> </TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Description")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.AppCri")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Reason")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Type")%></TD>
        </TR>
    </THEAD>
    <TBODY>
	<%
	j = occuredTail.size();
	String className;
	String sentence;
	for(int i = 0 ;i < j; i++){
		className=(i%2==0)?"CellBGRnews":"CellBGR3";
 		TailoringDeviationInfo info = (TailoringDeviationInfo) occuredTail.elementAt(i);
	%>
	<TR class="<%=className%>">
		<TD align="center"><%=i+1%></TD>
		<TD><%=info.projectCode%></TD>
		<TD><%=((info.modification == null)?"N/A":ConvertString.toHtml(info.modification))%></TD>
		<TD><%=((info.reason == null)?"N/A":ConvertString.toHtml(info.reason))%></TD>
		<TD><%=((info.note == null)?"N/A":ConvertString.toHtml(info.note))%></TD>
		<TD>
			<%switch(info.type){
				case 1:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Tailoring")%> <%break; 
				case 2:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Deviation")%> <%break;	 
			}%>
		</TD>
    </TR>
	<%}%>
	</TBODY>
	<TFOOT>
	        <TR>
	        	<TD colspan="8" class="TableLeft" align="right"></TD>
	        </TR>
	</TFOOT>
</TABLE>
<P>
<BR>
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.riskAddnew.Cancel") %>" onclick="jumpURL('tailoring.jsp')" class="BUTTON">
</FORM>

<SCRIPT language="JavaScript">

function doAction() {
  	frm.submit();
}
  
var objToHide = new Array(frm.cboProcess);
</SCRIPT >

</BODY>
</HTML>
