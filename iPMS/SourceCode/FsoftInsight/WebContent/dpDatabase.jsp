<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>dpDatabase.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<%
	Vector dpVt = (Vector) session.getAttribute("vtDefectLog");
	Vector cdVt = (Vector) session.getAttribute("vtCommDefect");
	
	Vector userList = (Vector) session.getAttribute("userList");
	Vector defectType = (Vector) session.getAttribute("defectType");
	
	Vector vtGroupList = (Vector) session.getAttribute("groupList");
	Vector vtOrgList = (Vector) session.getAttribute("orgList");

	String fromDate = (String) session.getAttribute("fromDate");
	String toDate = (String) session.getAttribute("toDate");

	ReportMonth rm = new ReportMonth();
	if (fromDate == null)
		fromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2,4);

	if (toDate == null)
		toDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2,4);
	
	String strWuID = (String) session.getAttribute("wuID");
	String strStatus = (String) session.getAttribute("strStatus");
	String strOnTime = (String) session.getAttribute("strOnTime");
	
	long lWuID = Parameters.FSOFT_WU; // FSOFT by default
	if (strWuID != null)
		lWuID = Long.parseLong(strWuID);
	
	if (strStatus == null)
		strStatus = "-1";
	int cboOnTime =(strOnTime == null)?-1:Integer.parseInt(strOnTime);
	int j = 0;
	
%>

<BODY onload="loadSQAMenu();" class="BD">
	
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.dpDatabase.DPDatabase")%> </P>

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.DP_DATABASE%>">

<TABLE width="100%" class="NormalText">
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Fromdate")%>  </TD>
		<TD><INPUT type="text" name="fromDate" value="<%=fromDate%>" maxlength="9" size="9"> (DD-MMM-YY) </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Todate")%>  </TD>
		<TD><INPUT type="text" name="toDate" value="<%=toDate%>" maxlength="9" size="9">(DD-MMM-YY) </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Group")%>  </TD>
		<TD>
			<SELECT name="cboGroup" class="COMBO">
				<%	j = vtOrgList.size();
					for (int i = 0; i < j; i++){
						RolesInfo groupInfo = (RolesInfo) vtOrgList.elementAt(i);
				%>
			<OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected": "")%>><%=groupInfo.workunitName%></OPTION>
				<%}
					j = vtGroupList.size();
					for (int i = 0; i < j; i++){
						RolesInfo groupInfo = (RolesInfo) vtGroupList.elementAt(i);
				%>
			<OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected": "")%>><%=groupInfo.workunitName%></OPTION>
				<%}%>
			</SELECT>
		</TD>
	</TR>
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Status")%>  </TD>
		<TD>
		<SELECT name="cboDPStatus" class="COMBO">
			<OPTION value="-1" <%=(strStatus.equalsIgnoreCase("-1") ? " selected": "")%>> <%=languageChoose.getMessage("fi.jsp.dpDatabase.All")%> </OPTION>
			<OPTION value="0" <%=(strStatus.equalsIgnoreCase("0") ? " selected": "")%>> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Open")%> </OPTION>
			<OPTION value="1" <%=(strStatus.equalsIgnoreCase("1") ? " selected": "")%>> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Closed")%> </OPTION>
		</SELECT>
		</TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Isintime")%>  </TD>
		<TD>
		<SELECT name="cboDPOnTime" class="COMBO">
			<OPTION value="-1" <%=(cboOnTime ==-1 ? " selected": "")%>> <%=languageChoose.getMessage("fi.jsp.dpDatabase.All1")%> </OPTION>
			<OPTION value="0" <%=(cboOnTime==0 ? " selected": "")%>> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Yes")%> </OPTION>
			<OPTION value="1" <%=(cboOnTime==1 ? " selected": "")%>> <%=languageChoose.getMessage("fi.jsp.dpDatabase.No")%> </OPTION>
			<OPTION value="2" <%=(cboOnTime==2 ? " selected": "")%>> N/A </OPTION>
		</SELECT>
		</TD>
		<TD><INPUT type="button" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.dpDatabase.View")%> " class="BUTTON" onclick="doAction()"></TD>
		<TD><B><A href='Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=dpDatabaseExport.jsp' target='DP Database'> <%=languageChoose.getMessage("fi.jsp.dpDatabase.ExporttoExcel")%> </A></B></TD>
	</TR>
</TABLE>
</FORM>

<BR>

<TABLE class="Table" width="99%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.dpDatabase.DefectPreventionlog")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Group1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Project")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.DPTaskAction")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.DPCode")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Targetdate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Status1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Closeddate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Isintime1")%> </TD>
        </TR>
        <%Date now =new Date();

        	boolean bl = true;
        	int iRowCount = 0;
        	String rowStyle ;
        	String isOnTime ;
        	boolean go;
        	for(int i=0;i<dpVt.size();i++){
       			rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				DPLogInfo info = (DPLogInfo) dpVt.get(i);

           		
				if ((info.closedDate==null && now.compareTo(info.targetDate) > 0)||(info.closedDate!=null && info.closedDate.compareTo(info.targetDate) > 0 )){
					isOnTime = languageChoose.getMessage("fi.jsp.dpDatabase.No1");
					go= (cboOnTime==1||cboOnTime==-1);
				}
				else if (info.closedDate!=null){
					isOnTime = languageChoose.getMessage("fi.jsp.dpDatabase.Yes1");
					go= (cboOnTime==0||cboOnTime==-1);
				}
				else{
					isOnTime = "N/A";
					go= (cboOnTime==2||cboOnTime==-1);
				}
				if (go){
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=++iRowCount%></TD>
            <TD><%=info.groupName%></TD>
            <TD NOWRAP><%=(info.projectCode == null) ? "N/A": info.projectCode%></TD>
            <TD><A HREF="Fms1Servlet?reqType=<%=Constants.DEFECT_LOG_DRILL%>&vtID=<%=i%>"><%=info.dpaction%></A></TD>
            <TD NOWRAP><%=info.dpcode%></TD>
            <TD NOWRAP><%=CommonTools.dateFormat(info.targetDate)%></TD>
            <TD>
            <%	switch (info.dpStatus) {
	            	case 0: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Open1")%> <%break;
	            	case 1: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Closed1")%> <%break;
            	}
            %>
            </TD>
            <TD NOWRAP><%=CommonTools.dateFormat(info.closedDate)%></TD>
            <TD><%=isOnTime%></TD>
        </TR>
        <% 		}
        	}
        %>
    </TBODY>
</TABLE>

<BR>

<TABLE class="Table" width="99%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Commondefects")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Group2")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Project1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Commondefect")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Commdef.Code")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.DPCode1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Defecttype")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.RootCause")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.dpDatabase.CauseCategory")%> </TD>
        </TR>
        <%
        	bl = true;
        	rowStyle = "";
        	for(int i=0;i<cdVt.size();i++){
       			rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				CommDefInfo info = (CommDefInfo) cdVt.get(i);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><%=info.groupName%></TD>
            <TD NOWRAP><%=(info.projectCode == null) ? "N/A": info.projectCode%></TD>
            <TD><%=info.commdef%></TD>
            <TD NOWRAP><%=info.commonDefCode%></TD>
            <TD NOWRAP><%=(info.dpcode == null)? "N/A": info.dpcode%></TD>
            <TD>
            <%
            	switch (info.defecttype) {
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.01FunctionalityOther")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.02UserInterface")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.03Performance")%> <%break;
            	case 4: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.04Designissue")%> <%break;
            	case 5: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.05Codingstandard")%> <%break;
            	case 6: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.06Document")%> <%break;
            	case 7: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.07DataDatabaseintegrity")%> <%break;
            	case 8: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.08SecurityAccessControl")%> <%break;
            	case 9: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.09Portability")%> <%break;
            	case 10: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.10Other")%> <%break;
            	case 11: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.11Tools")%> <%break;
            	case 12: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.011Reqmisunderstanding")%> <%break;
            	case 13: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.012Featuremissing")%> <%break;
            	case 14: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.013Codinglogic")%> <%break;
            	case 15: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.014Businesslogic")%> <%break;
            	}
            %>
            </TD>
            <TD><%=(info.rootcause == null)? "N/A": info.rootcause%></TD>
            <TD>
            <%
            	switch (info.causecate) {
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Training")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Communication")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Oversight")%> <%break;
            	case 4: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Understanding")%> <%break;
            	case 5: %> <%=languageChoose.getMessage("fi.jsp.dpDatabase.Other")%> <%break;
            	}
            %>
            </TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>

<SCRIPT language="JavaScript">
  function doAction()
  {
  	if(frm.fromDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.DpDatabase.Thisfieldismandatory")%>");
  	 	frm.fromDate.focus();
  	 	return;
  	}
	if (!isDate(frm.fromDate.value)){
 		window.alert("<%=languageChoose.getMessage("fi.jsp.dpDatabase.InvalidDateValue")%>");
  		frm.fromDate.focus();
  		return;
  	}

  	if(frm.toDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.DpDatabase.Thisfieldismandatory")%>");
  	 	frm.toDate.focus();
  	 	return;
  	}
	if (!isDate(frm.toDate.value)){
 		window.alert("<%=languageChoose.getMessage("fi.jsp.dpDatabase.InvalidDateValue")%>");
  		frm.toDate.focus();
  		return;
  	}

  	if(compareDate(frm.fromDate.value, frm.toDate.value) == -1)
  	{
  	 	window.alert("<%=languageChoose.getMessage("fi.jsp.dpDatabase.FromDateMustBeBeforeToDate")%>");
  		frm.fromDate.focus();
  		return;
  	}

  	frm.submit();
  }
  var objToHide=new Array(frm.cboGroup, frm.cboDPStatus, frm.cboDPOnTime);
</SCRIPT >
</BODY>
</HTML>
