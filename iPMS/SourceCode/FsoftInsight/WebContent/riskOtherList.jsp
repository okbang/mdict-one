<%@page import="java.util.Vector,com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.common.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src='jscript/validate.js'></SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<TITLE>riskOtherList.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>

<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>

<%
	Vector vtTopCommonRisk = (Vector) session.getAttribute("vtTopCommonRisk");
	//
	Vector occuredRisk = (Vector) session.getAttribute("vtOccuredRisk");
	String strWuID = (String) session.getAttribute("wuID");
	String strIsPlaned = (String) session.getAttribute("strIsPlaned");
	Vector vtGroupList = (Vector) session.getAttribute("groupList");
	Vector vtOrgList = (Vector) session.getAttribute("orgList");
	
	String fromDate = (String) session.getAttribute("fromDate");
	String toDate = (String) session.getAttribute("toDate");
	String customer = (String)session.getAttribute("customer");
	
	long lWuID = Parameters.FSOFT_WU; // FSOFT by default
	if (strWuID != null)
		lWuID = Long.parseLong(strWuID);

	if (strIsPlaned == null)
		strIsPlaned = "-1";
	
	int iProcess = CommonTools.parseInt((String) session.getAttribute("strProcess"));

	ReportMonth rm = new ReportMonth();
	if (fromDate == null)
		fromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2,4);

	if (toDate == null)
		toDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2,4);
	
	int j = 0;

	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	
	Vector vtCustomer = (Vector)session.getAttribute("vtCustomer");

	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	
	String scroll="";
	if(occuredRisk.size() > 8){
		scroll="makeScrollableTable('tableId',true,'400')";
	}
	
	String isAdminOfFsoft = (String)session.getAttribute("isAdminOfFsoft");
%>
</SCRIPT>
<SCRIPT language="javascript">
<%
	Vector vtProject = (Vector)session.getAttribute("vtProject");
	String strCurrentProject = (String)session.getAttribute("strCurrentProject");
	
    out.write("var prjNum = " + vtProject.size() + ";\n");
    out.write("var prj = new Array(prjNum);\n");
    for (int i = 0; i < (vtProject.size()-1); i++) {
    	ProjectDateInfo projectDateInfo = (ProjectDateInfo)vtProject.elementAt(i);
    	out.write("\nprj[" + (i) + "]=new Array(3);");
        out.write("prj[" + (i) + "][0]=\"" + projectDateInfo.code + "\";");//Project Code
        out.write("prj[" + (i) + "][1]=\"" + projectDateInfo.groupName + "\";");//Group Name
    }
%>

var valueN;
var flag = 0;
function changeGroup() {
    var myElement;
    var form = document.frm;
    
    for (var i = form.cboPrj.options.length; i >= 0; i--) {
        form.cboPrj.options[i] = null;
    }
    var currGroup = form.cboGroup.value;
    
    var bGroupAll = false;
   if (currGroup == "FSOFT") {
      bGroupAll = true;
   }
    
   myElement = document.createElement("OPTION");
   myElement.value = "(All)";
   myElement.text = "(All)";
   document.getElementById('cboPrj').options.add(myElement);

   var bAdd = false;
   for (i = 1; i < prjNum; i++) {
       bAdd = false;
       if (bGroupAll) {
          bAdd = true;
       }
       else {
          if ((form.cboGroup.value == prj[i-1][1])) {
               bAdd = true;
          }
       }
       if (bAdd == true) {
           myElement = document.createElement("OPTION");
           myElement.value = prj[i-1][0];
           myElement.text = prj[i-1][0];
           document.getElementById('cboPrj').options.add(myElement);
        }
    }
    if (form.cboPrj.options.length == 1) {
        form.cboPrj.options[0].selected = true;
    }
	var strCurrentProject = '<%=strCurrentProject%>';
    if (currGroup == "FSOFT" && strCurrentProject == 'null') {
    	form.cboPrj.options[0].selected = true;
    }
    if(strCurrentProject != 'null'){
    	document.getElementById('cboPrj').value = strCurrentProject;
    }
}

function onLoadComboPrj() {
	var strCurrentProject = '<%=strCurrentProject%>';
	if(strCurrentProject == 'null' ){
		document.getElementById('cboPrj').value = "(All)";
	}
	<%
		session.removeAttribute("strCurrentProject");
	%>
}
</SCRIPT>

</HEAD>
<BODY onLoad="loadPrjMenu();<%=scroll%>" class="BD">

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.RISK_LIST_OTHER%>">

<DIV align="left">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.CommonRisk")%></P>
<TABLE cellspacing="1" class="Table" width="95%" >
<TBODY>
 	<TR class="ColumnLabel">
 		<TD width = "22" align = "center">#</TD>
        <TD><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.SourceName")%>*</TD>
        <TD><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.CategoryName")%></TD>
        <TD width="5%"><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Priority")%></TD>
    </TR>
<%
	RiskSourceInfo riskSourceInfo = new RiskSourceInfo();
	for (int i=0; i<vtTopCommonRisk.size(); i++) {
		String className = (i%2 == 0) ?"CellBGRnews":"CellBGR3";
		riskSourceInfo = (RiskSourceInfo) vtTopCommonRisk.elementAt(i);
%>		
        <TR>
        	<TD width = "22" align = "center" class="<%=className%>"><%=i+1%></TD>
        	<TD  class="<%=className%>"><%if (isAdminOfFsoft.equals("true")){%><a href="Fms1Servlet?reqType=<%=Constants.RISK_DATABASE_UPDATE_PREP%>&riskSourceID=<%=riskSourceInfo.sourceID%>"><%}%><%=riskSourceInfo.sourceName%><%if (isAdminOfFsoft.equals("true")){%></a><%}%></TD>
            <TD class="<%=className%>" ><%=riskSourceInfo.categoryName%></TD>
            <TD class="<%=className%>" align="center"><%=riskSourceInfo.topRisk%></TD>
        </TR>
     <%}%>
     </TBODY>
</TABLE>
</DIV>
<%
if (isAdminOfFsoft.equals("true")){
%>
<br>
     <INPUT type="button" name="btnAdd" value=" <%=languageChoose.getMessage("fi.jsp.paRisk.Add")%> " class="BUTTON" onclick="doAdd()">
<%}%>
<BR>

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.RiskDatabase")%></P>
<TABLE width="100%" class="NormalText">
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.paRisk.Fromdate")%> </TD>
		<TD>
				<INPUT type="text" name="fromDate" size="9" maxlength="9" value = "<%=fromDate%>">
            	<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showFromDate()'>
		</TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.paRisk.Todate")%> </TD>
		<TD>
			<INPUT type="text" name="toDate" size="9" maxlength="9" value = "<%=toDate%>">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showToDate()'>
		</TD>
		<TD></TD>
		<TD>
		</TD>
		<TD></TD>
	</TR>
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
		<SELECT name="cboGroup" class="COMBO" onchange="changeGroup();">
			<%	j = vtOrgList.size();
				for (int i = 0; i < j; i++)	{
					RolesInfo groupInfo = (RolesInfo) vtOrgList.elementAt(i);
			%><OPTION value="<%=groupInfo.workunitName%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
			<%}	
				j = vtGroupList.size();
				for (int i = 0; i < j; i++)	{
					RolesInfo groupInfo = (RolesInfo) vtGroupList.elementAt(i);
			%>
		<OPTION value="<%=groupInfo.workunitName%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
			<%}%>
		</SELECT> </TD>
	<TD></TD>
	<TD></TD>
	</TR>
	<TR>
		<TD><%=languageChoose.getMessage("fi.jsp.paRisk.Customer")%></TD>
		<TD>
			<SELECT name="customer" class="COMBO">
				<OPTION value="All" <%=(customer == null ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paRisk.All")%> </OPTION>
					<%	
						j = vtCustomer.size();
						for (int i = 0; i < j; i++){
				           	ProjectInfo projectInfo = (ProjectInfo)vtCustomer.get(i);
					%>
				<OPTION value="<%=projectInfo.getCustomer()%>"<%=(projectInfo.getCustomer().equals(customer) ? " selected" : "")%>><%=projectInfo.getCustomer()%></OPTION>
					<%}%>
			</SELECT>
		</TD>
		<TD>Project</TD>
		<TD>
			<SELECT id="cboPrj" name="cboPrj" class="SmallCombo">
			</SELECT>
		</TD>
		<TD></TD>
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
            <TD width="30%" align="left"> Description </TD>
            <TD width="27%" align="left"> Trigger </TD>
            <TD width="10%" align="center"> <%=languageChoose.getMessage("fi.jsp.paRisk.Actualimpact")%> </TD>
            <TD width="10%" align="center"> <%=languageChoose.getMessage("fi.jsp.paRisk.Contingency")%> </TD>
        </TR>
    </THEAD>
    <TBODY>
	<%
	j = occuredRisk.size();
	String className;
	String sentence;
	for(int i = 0 ;i < j; i++){
		className=(i%2==0)?"CellBGRnews":"CellBGR3";
 		RiskInfo info = (RiskInfo) occuredRisk.elementAt(i);
	%>
	<TR class="<%=className%>">
		<TD align="center"><%=i+1%></TD>
		<TD><A href="Fms1Servlet?reqType=<%=Constants.RISK_UPDATE_PREP%>&riskID=<%=info.riskID%>&prjID=<%=info.projectID%>"><%=info.projectCode%></A></TD>
		<TD><%=((info.condition == null)?"N/A":ConvertString.toHtml(info.condition))%></TD>
		<TD><%=((info.triggerName == null)?"N/A":ConvertString.toHtml(info.triggerName))%></TD>
			<%
			if (info.pimp != null) {
			%><TD><%
				for (int k =0;k<info.pimp.length;k++){
					sentence=info.pimp[k].equals("N/A")?"N/A":languageChoose.getMessage(info.pimp[k]) + ": " + info.pest[k] + info.punt[k]  ;
				    %>- <%=sentence%>
				    <br>
			   <%}%></TD><%
			}
			else {
			 	%><TD></TD>
		   <%}%>
		<TD><%=((info.contigency == null)?"N/A":ConvertString.toHtml(info.contigency))%></TD>	 
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
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.riskAddnew.Cancel") %>" onclick="back();" class="BUTTON">

</FORM>

<SCRIPT language="JavaScript">
function on_Submit()
	{
        var form = document.frm;
        var max = <%=vtTopCommonRisk.size()%>;
		for (var i = 0; i < max; i++) {
			if (form.topRisk[i].value <= 0 || form.topRisk[i].value > 10 || isNaN(form.topRisk[i].value)) {
				alert("<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.OrderMustBeAPositiveNumberAndLessThan10")%>");
				frm.topRisk[i].focus();
				return;
			}
	    }
		form.submit();
	}
	
function back() {
  	frm.reqType.value = "<%=Constants.RISK_LIST_INIT%>";
	frm.submit();
}

function doAction() {
  	if(frm.fromDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.paRisk.Thisfieldismandatory")%>");
  	 	frm.fromDate.focus();
  	 	return;
  	}
	if (!isDate(frm.fromDate.value)){
 		window.alert("<%=languageChoose.getMessage("fi.jsp.paRisk.Invaliddatevalue")%>");
  		frm.fromDate.focus();
  		return;
  	}

  	if(frm.toDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.paRisk.Thisfieldismandatory")%>");
  	 	frm.toDate.focus();
  	 	return;
  	}
	if (!isDate(frm.toDate.value)){
 		window.alert("<%=languageChoose.getMessage("fi.jsp.paRisk.Invaliddatevalue")%>");
  		frm.toDate.focus();
  		return;
  	}

  	if(compareDate(frm.fromDate.value, frm.toDate.value) == -1){
  	 	window.alert("<%=languageChoose.getMessage("fi.jsp.paRisk.FromdatemustbebeforeTodate")%>");
  		frm.fromDate.focus();
  		return;
  	}
  	
  	frm.submit();
}

function doAdd(){
	frm.reqType.value = "<%=Constants.RISK_DATABASE_ADD_PREPARE%>";
	frm.submit();
}
  
function showFromDate(){
	if(!(isDate(frm.fromDate.value))){
		frm.fromDate.value = "01-Jan-09";
	}
	showCalendar(frm.fromDate, frm.fromDate, "dd-mmm-yy",null,1,-1,-1,true);
}

function showToDate(){
	if(!(isDate(frm.toDate.value))){
		frm.toDate.value = "01-Jan-09";
	}
	showCalendar(frm.toDate, frm.toDate, "dd-mmm-yy",null,1,-1,-1,true);
}

var objToHide = new Array(frm.cboProcess);
</SCRIPT>
<SCRIPT language="JavaScript">
changeGroup();
onLoadComboPrj();
</SCRIPT >

</BODY>
</HTML>
