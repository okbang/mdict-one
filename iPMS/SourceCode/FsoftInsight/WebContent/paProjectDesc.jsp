<%@page import="java.util.Vector,com.fms1.infoclass.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.common.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<TITLE>paProjectDesc.jsp</TITLE>
<SCRIPT language="JavaScript">
	function doOnload() {
		window.open("header.jsp","header");
		loadOrgMenu();
	}
</SCRIPT>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<BODY onLoad="doOnload();" class="BD">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.ProcessAssetsProjectDescriptio")%> </P>

<%
	String strWuID = (String) session.getAttribute("wuID");
	
	String strProjectCategory = (String) session.getAttribute("strProjectCategory");
	String strApplicationType = (String) session.getAttribute("strApplicationType");
	String strBusinessDomain = (String) session.getAttribute("strBusinessDomain");
	String strCustomer = (String) session.getAttribute("strCustomer");
	
	String fromDate = (String) session.getAttribute("fromDate");
	String toDate = (String) session.getAttribute("toDate");
	
	String strProjectCode = (String) session.getAttribute("strProjectCode");
	String strProjectCust = (String) session.getAttribute("strProjectCust");
	String strProjectMember = (String) session.getAttribute("strProjectMember");
	String strProjectLanguage = (String) session.getAttribute("strProjectLanguage");
	String strProjectDBMS = (String) session.getAttribute("strProjectDBMS");
	String strProjectOS = (String) session.getAttribute("strProjectOS");
	String strProjectTool = (String) session.getAttribute("strProjectTool");
	
	Vector vtBizDomainList =  (Vector) session.getAttribute("bizdomainList");
	Vector vtAppTypeList = (Vector) session.getAttribute("apptypeList");
	Vector vtProjectList = (Vector) session.getAttribute("projectInfoList");
	Vector vtGroupList = (Vector) session.getAttribute("groupList");
	Vector vtOrgList = (Vector) session.getAttribute("orgList");
	Vector vtCustomer = (Vector) session.getAttribute("vtCustomer");
	
	long lWuID = Parameters.FSOFT_WU; // FSOFT by default
	lWuID = CommonTools.parseLong(strWuID);
		
	if (strProjectCategory == null)
		strProjectCategory = "-1";
		
	long lApplicationType = 0;
	lApplicationType = CommonTools.parseLong(strApplicationType);
	
	
	if (CommonTools.parseInt(strBusinessDomain) == 0)
		strBusinessDomain="0";
				
	if (CommonTools.parseInt(strCustomer)==0 )
		strCustomer="-1";
	
	ReportMonth rm = new ReportMonth();
	
	if (fromDate == null)
		fromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2,4);

	if (toDate == null)
		toDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2,4);

	if (strProjectCode == null)
		strProjectCode = "";

	if (strProjectCust == null)
		strProjectCust = "";

	if (strProjectMember == null)
		strProjectMember = "";

	if (strProjectLanguage == null)
		strProjectLanguage = "";

	if (strProjectDBMS == null)
		strProjectDBMS = "";

	if (strProjectOS == null)
		strProjectOS = "";

	if (strProjectTool == null)
		strProjectTool = "";
	
	int j = 0;
%>

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.PROASS_PROJECT_DESC%>">
<TABLE width="100%" class="NormalText">
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Fromdate")%> </TD>
		<TD>
			<INPUT type="text" name="fromDate" value="<%=fromDate%>" maxlength="9" size="9">(DD-MMM-YY)
			&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.paProjectDesc.Todate")%>
			<INPUT type="text" name="toDate" value="<%=toDate%>" maxlength="9" size="9">(DD-MMM-YY)
		</TD>
		<TD>
			<%=languageChoose.getMessage("fi.jsp.paProjectDesc.Group")%>
			<SELECT name="cboGroup" class="COMBO">
			<%	
				j = vtOrgList.size();
				for (int i = 0; i < j; i++)
				{
					RolesInfo groupInfo = (RolesInfo) vtOrgList.elementAt(i);
			%>
				<OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
			<%
				}
			%>
			<%				
				j = vtGroupList.size();
				for (int i = 0; i < j; i++)
				{
					RolesInfo groupInfo = (RolesInfo) vtGroupList.elementAt(i);
			%>
				<OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
			<%
				}
			%>
			</SELECT>
		</TD>
		<TD>
			<%=languageChoose.getMessage("fi.jsp.paProjectDesc.Type")%>&nbsp;
			<SELECT name="cboProjectCategory" class="COMBO">
				<OPTION value="-1" <%=(strProjectCategory.equalsIgnoreCase("-1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.All")%> </OPTION>
				<OPTION value=0 <%=(strProjectCategory.equalsIgnoreCase("0") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Development")%> </OPTION>
				<OPTION value=1 <%=(strProjectCategory.equalsIgnoreCase("1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Maintenance")%> </OPTION>
				<OPTION value=2 <%=(strProjectCategory.equalsIgnoreCase("2") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Other")%> </OPTION>
			</SELECT>
		</TD>
	</TR>
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Applicationtype")%> </TD>
		<TD>
			<SELECT name="cboApplicationType" class="COMBO">
				<OPTION value="0"<%=(lApplicationType == 0 ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.All1")%> </OPTION>
				<%	
					j = vtAppTypeList.size();
					for (int i = 0; i < j; i++)
					{
						AppTypeInfo appTypeInfo = (AppTypeInfo) vtAppTypeList.elementAt(i);
				%>
			<OPTION value="<%=appTypeInfo.apptypeID%>"<%=(appTypeInfo.apptypeID == lApplicationType ? " selected" : "")%>><%=appTypeInfo.name%></OPTION>
				<%
					}
				%>
			</SELECT>
		</TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Businessdomain")%> </TD>
		<TD>
			<SELECT name="cboBusinessDomain" class="COMBO">
			<OPTION value="0"<%=(strBusinessDomain.equalsIgnoreCase("0") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.All2")%> </OPTION>
				<%	
					j = vtBizDomainList.size();
					for (int i = 0; i < j; i++)
					{
						BizDomainInfo bizDomainInfo = (BizDomainInfo) vtBizDomainList.elementAt(i);
				%>
			<OPTION value="<%=bizDomainInfo.name%>"<%=(bizDomainInfo.name.equalsIgnoreCase(strBusinessDomain) ? " selected" : "")%>><%=bizDomainInfo.name%></OPTION>
				<%
					}
				%>
			</SELECT>
		</TD>
	</TR>
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Customer")%> </TD>
		<TD>
			<SELECT name="cboCustomer" class="COMBO">
			<OPTION value="0"<%=(strCustomer.equalsIgnoreCase("-1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.All3")%> </OPTION>
				<%	
					j = vtCustomer.size();
					for (int i = 0; i < j; i++)
					{
						ProjectInfo projectInfo = (ProjectInfo) vtCustomer.elementAt(i);
				%>
			<OPTION value="<%=projectInfo.getCustomer()%>"<%=(projectInfo.getCustomer().equalsIgnoreCase(strCustomer) ? " selected" : "")%>><%=projectInfo.getCustomer()%></OPTION>
				<%
					}
				%>
			</SELECT>
		</TD>
		<TD><INPUT type="button" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.paProjectDesc.View")%> " class="BUTTON" onclick="doAction()"></TD>
		<TD><B><A href="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=paProjectDescExport.jsp" target="about:blank"> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.ExporttoExcel")%> </A></B></TD>
	</TR>
</TABLE>

</FORM>
<BR>

<TABLE class="Table" cellspacing="1" width="100%">
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="24" align="center">#</TD>
			<TD width="220"> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Project")%> </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Customer1")%> </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Applicationtype1")%> </TD>
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.paProjectDesc.Bizdomain")%> </TD>
		</TR>
	<%
		if (vtProjectList != null) {
			for (int i = 0; i < vtProjectList.size(); i ++)
			{
				ProjectInfo projectInfo = (ProjectInfo)vtProjectList.elementAt(i);
		        String className;
				
				className = (i%2 == 0) ? "CellBGRnews" : "CellBGR3";
	%>
		<TR class="<%=className%>">
			<TD><%=i + 1%></TD>
			<TD><A href="Fms1Servlet?reqType=<%=Constants.PROASS_PROJECT_DETAIL%>&projectCode=<%=projectInfo.getProjectCode()%>&fromDate=<%=fromDate%>&toDate=<%=toDate%>"><%=projectInfo.getProjectName()%></A></TD>
			<TD><%=projectInfo.getCustomer()%></TD>
			<TD><%=projectInfo.getApplicationType()%></TD>
			<TD><%=projectInfo.getBusinessDomain()%></TD>
		</TR>
	<%
			}
		}
	%>
	</TBODY>
</TABLE>

<SCRIPT language="JavaScript">
  	function doAction() {
	  	if (frm.fromDate.value == "") {
	  		window.alert("<%=languageChoose.getMessage("fi.jsp.paProjectDesc.Thisfieldismandatory")%>");
	  	 	frm.fromDate.focus();
	  	 	return;
	  	}
		if (!isDate(frm.fromDate.value)) {
	 		window.alert("<%=languageChoose.getMessage("fi.jsp.paProjectDesc.Invaliddatevalue")%>");
	  		frm.fromDate.focus();
	  		return;
	  	}
	  	if (frm.toDate.value == "") {
	  		window.alert("<%=languageChoose.getMessage("fi.jsp.paProjectDesc.Thisfieldismandatory")%>");
	  	 	frm.toDate.focus();
	  	 	return;
	  	}
		if (!isDate(frm.toDate.value)) {
	 		window.alert("<%=languageChoose.getMessage("fi.jsp.paProjectDesc.Invaliddatevalue")%>");
	  		frm.toDate.focus();
	  		return;
	  	}
	  	if(compareDate(frm.fromDate.value, frm.toDate.value) == -1) {
	  	 	window.alert("<%=languageChoose.getMessage("fi.jsp.paProjectDesc.FromdatemustbebeforeTodate")%>");
	  		frm.fromDate.focus();
	  		return;
	  	}
	  	frm.submit();
	}
	var objToHide=new Array(frm.cboApplicationType, frm.cboCustomer);
</SCRIPT >

</BODY>
</HTML>