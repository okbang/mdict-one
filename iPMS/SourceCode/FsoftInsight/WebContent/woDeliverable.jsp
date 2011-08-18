<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woDeliverable.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = Security.securiPage("Work Order",request,response);
	Vector deliverableList = (Vector) session.getAttribute("deliverableList");
	Vector moduleList = (Vector)session.getAttribute("woModuleList");
	int mSize = 0;
	if (moduleList != null) mSize = moduleList.size();

	String archiveStatus = (String)session.getAttribute("archiveStatus");

	Vector vtStage=(Vector)session.getAttribute("stageVector");
	MetricInfo durationMetric = (MetricInfo) session.getAttribute("durationMetric");

	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>


<%
	// map deliverables to stage
	int deliverSize = 0;
	int stageSize = 0;
	ModuleInfo 	moduleInfo = null;

	if (vtStage != null)  stageSize = vtStage.size();
	if (deliverableList != null)  deliverSize = deliverableList.size();

	Vector[] existStage  = new Vector[stageSize + 1];
	Date[] dStageEndDate = new Date[stageSize];

	// get all stage end date
	for (int i = 0; i < stageSize; i++) {
		StageInfo stageInfo=(StageInfo) vtStage.get(i);

		existStage[i] = new Vector();
		dStageEndDate[i] = new Date();
		dStageEndDate[i] = stageInfo.pEndD;
		if (dStageEndDate[i] == null) dStageEndDate[i] = stageInfo.bEndD;
	}
	existStage[stageSize] = new Vector();

	int test = 0;
	for (int n = 0; n < deliverSize; n++) {
		moduleInfo = (ModuleInfo) deliverableList.elementAt(n);
		Hashtable moduleTable = new Hashtable();
		moduleTable.put(""+n,moduleInfo);

		Date dDeliveryDate = moduleInfo.rePlannedReleaseDate;
		if (dDeliveryDate == null) dDeliveryDate = moduleInfo.plannedReleaseDate;

		test = 0;
		for (int i = 0; i < stageSize; i++) {
			if (dDeliveryDate.before(dStageEndDate[i]) || dDeliveryDate.equals(dStageEndDate[i])) {
				existStage[i].addElement(moduleTable);
				test++;
				break;
			}
		}
		if(test == 0){
			existStage[stageSize].addElement(moduleTable);
		}
	}
%>
<BODY onload="loadPrjMenu();javascript:Import();" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woDeliverable.WorkorderStagesDeliverables")%> </P>

<!-- landd move stage from project plan/life cycle to workorder/deliverable start -->
<br>
<FORM method="post"  name="frm"></form>
<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.woDeliverable.Stages")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.plLifecycle.Stage")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.plLifeCycle.Numberofiterations")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.plLifeCycle.Plannedenddate") %></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.plLifeCycle.Replannedenddate")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.plLifecycle.Description")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.plLifecycle.Milestone") %></TD>
        </TR>
        <%  boolean bl=false;
    		double dbPlanTotalDuration = 0;
    		String rowStyle = "";
        	for(int i=0;i<vtStage.size();i++){
        		StageInfo stageInfo=(StageInfo)vtStage.get(i);
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
        		if (!stageInfo.pDuration.equalsIgnoreCase("N/A")) {
	        		dbPlanTotalDuration = dbPlanTotalDuration + Double.parseDouble(stageInfo.pDuration);
	        	}
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="stageDetails.jsp?vtID=<%=i%>&source=1"><%=stageInfo.stage%></A></TD>
            <TD align="center"><%=stageInfo.iterationCnt%></TD>
            <TD><%=CommonTools.dateFormat(stageInfo.bEndD)%></TD>
            <TD><%=CommonTools.dateFormat(stageInfo.pEndD)%></TD>
            <TD><%=stageInfo.description%></TD>
            <TD><%=stageInfo.milestone%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<P><%if(right == 3 && !isArchive){%><INPUT type="button" class="BUTTON" name="btnAdd" value="<%=languageChoose.getMessage("fi.jsp.plLifecycle.Addnew")%>" onclick="addStage();"><%}%></P>
<%
    double planVal = (Double.isNaN(durationMetric.rePlannedValue))
			  ? durationMetric.plannedValue
			  : durationMetric.rePlannedValue;
	if (dbPlanTotalDuration != 0) {
		dbPlanTotalDuration = dbPlanTotalDuration + (vtStage.size() - 1);
		if (planVal != dbPlanTotalDuration){
	%><p class="ERROR"><%= languageChoose.paramText(new String[]{"fi.jsp.plLifeCycle.NoteInconsistencybetweentotalplanneddurationofstages~PARAM1_NUMBER~andplandurationofproject~PARAM2_NUMBER~",(String)CommonTools.formatDouble(dbPlanTotalDuration),(String)CommonTools.formatDouble(planVal)} )%><p>
	<%		}
	}%>

<!-- landd move stage from project plan/life cycle to workorder/deliverable end -->

<br>
<!--
<TABLE cellspacing="1" class="HDR" width="95%">
	<TBODY>
		<TR>
			<TD width="10%"></TD>
			<TD></TD>
			<TD align="right"><P align=right><%if(right == 3 && !isArchive){%><a href="javascript:Import();"><%=languageChoose.getMessage("fi.jsp.woDeliverable.ImportDeliverable")%></a><%}%></p></TD>
		</TR>
	</TBODY>
</TABLE>
-->
<FORM name="frm_Import" action="Fms1Servlet?reqType=<%=Constants.WO_DELIVERABLE_IMPORT%>" enctype="MULTIPART/FORM-DATA" method=POST >
<TABLE cellspacing="1" class="HDR" width="95%" id="ImportTable">
	<TBODY>
		<TR>
			<TD align="right"><STRONG>File Name&nbsp;*</STRONG></TD>
	        <TD align="left">
	        	<INPUT type="file" name="importFile">
	       		<INPUT type="button" name="Import" onclick="checkName()" value=" Import " class="Button">
            	<INPUT type="hidden" name="filename">
	        </TD>
	        </TR>
	        <TR>
	        	<TD></TD>
	        	<TD align="center"><A href="Template_Import_WORKORDER.xls"><FONT color="blue" class="label1">Download Template File</FONT></A></TD>
	        </TR>
	</TBODY>
</TABLE>
</FORM>

<%
String checkImport = (String)session.getAttribute("Imported");
if(checkImport == "true"){
int[] result = (int[])session.getAttribute("AddedRecord");
%><br>Imported successfull these records : <%
int l = 0;
int k=0;
	while(l < 50){
		if(result[l] > 0){
			if(k>0){
				%> ,<%
			}
			%>
			<%=result[l]%>
			<%
			k++;
		}
		l++;
	}
	session.removeAttribute("Imported");
	session.removeAttribute("AddedRecord");
}
%>
<%
String isImport = (String)session.getAttribute("ImportFail");
if(isImport == "fail"){
	%><br><p style="color: red;">Import Fail<%
	session.removeAttribute("ImportFail");
}
%>

<br>
<p><%=languageChoose.getMessage("fi.jsp.woDeliverable.NoteDeliverablesareaspecialkin")%>
<A href="Fms1Servlet?reqType=<%=Constants.MODULE_LIST%>">
	<%=languageChoose.getMessage("fi.jsp.woDeliverable.createtheproducts")%>
</A>
<p>
<TABLE cellspacing="1" class="Table" width="95%" id="tableDeliverable">
<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.woDeliverable.Deliverables")%></CAPTION>
<THEAD>
	<TR class="ColumnLabel">
		<TD width ="3%" align = "center"><INPUT id="selectAll" type ="checkbox" name="selectAll" onclick="selectAll()"/></TD>
		<TD width ="3%" align = "center">#</TD>
		<TD width="27%"> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Stage")%></TD>
		<TD width="9%"> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Firstcommitted")%> </TD>
		<TD width="9%"> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Lastcommitted")%> </TD>
		<TD width="9%"> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Actual")%> </TD>
		<TD width="9%" align = "center"> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Status")%> </TD>
		<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Deviation")%> </TD>
		<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Note")%> </TD>
	</TR>
</THEAD>
<TBODY>
<%
	String moduleKey = "";
	Hashtable mHash = null;
	ModuleInfo info = null;
	int iNo = 0;
	int subSize = 0;
	for(int n = 0;n < stageSize;n++){
		StageInfo stageInfo=(StageInfo) vtStage.get(n);
%>
 <tr class="CellBGRLongnews">
 	<td colspan="3" style="font-weight:bold">&nbsp;<%=stageInfo.stage == null? "": stageInfo.stage%></td>
 	<td>
    <% if( stageInfo.bEndD != null) {
  		out.println(CommonTools.dateFormat(new java.util.Date(stageInfo.bEndD.getTime())));
  	 }
  	 else {
  		out.println("N/A");
  	 }
	%>
    </td>
    <td>
    <% if( stageInfo.pEndD != null) {
  		out.println(CommonTools.dateFormat(new java.util.Date(stageInfo.pEndD.getTime())));
  	 }
  	 else {
  		out.println("N/A");
  	 }
	%>
    </td>
    <td>
    <% if( stageInfo.aEndD != null) {
  		out.println(CommonTools.dateFormat(new java.util.Date(stageInfo.aEndD.getTime())));
  	 }
  	 else {
  		out.println("N/A");
  	 }
	%>
    </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
 </tr>
<%
	moduleKey = "";
	subSize = existStage[n].size();
	mHash = null;
	info = null;
	for(int i = 0; i < subSize; i++){
		rowStyle=(i%2 == 0)? "CellBGR3":"CellBGRnews";
		iNo++;
		mHash = (Hashtable) existStage[n].elementAt(i);
 		info = (ModuleInfo) mHash.elements().nextElement();
 		moduleKey = (String) mHash.keys().nextElement();
%>
<tr class=<%=rowStyle%>>
 <td width ="3%" align = "center"><INPUT type ="checkbox" name="checkForUpdate"/></TD>
 <td width ="3%" align = "center"><%=iNo%><INPUT TYPE ="hidden" name ="vtModuleID" value = "<%=moduleKey%>"/></td>
 <TD>
	 <%if(right == 3 && !isArchive){%>
	 	<a href="Fms1Servlet?reqType=<%=Constants.WO_DELIVERABLE_UPDATE_PREP%>&vtid=<%=moduleKey%>">
	 <%}%><%=info.name%>
	 <%if(right == 3 && !isArchive){%>
	 	</a>
	 <%}%>
 </TD>
 <td>
	 <% if( info.plannedReleaseDate != null) {
	 	out.println(CommonTools.dateFormat(new java.util.Date(info.plannedReleaseDate.getTime())));
	 }else{
	 	out.println("N/A");
	 }
	 %>
 </td>
 <td>
	 <% if( info.rePlannedReleaseDate != null) {
	 	out.println(CommonTools.dateFormat(new java.util.Date(info.rePlannedReleaseDate.getTime())));
	 }else{
	 	out.println("N/A");
	 }
	 %>
 </td>
 <td>
	 <% if( info.actualReleaseDate != null) {
	 	out.println(CommonTools.dateFormat(new java.util.Date(info.actualReleaseDate.getTime())));
	 }else{
	 	out.println("N/A");
	 }
	 %>
 </td>
 <td>
  	<%
  		switch(info.status){
			case 1:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Pending")%> <%break;
			case 2:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Accepted")%> <%break;
			case 3:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Rejected")%> <%break;
			case 4:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Cancelled")%> <%break;
			case 6:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.InProgress")%> <%break;
			case 5:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Others")%> <%break;
		}
	%>
  </td>
 <td>
 	<%= CommonTools.formatDouble(info.deviation)%>
 </td>
 <td>
 	<%=(info.note==null)? "N/A":ConvertString.toHtml(info.note)%>
 </td>
</tr>
<%
	}
}

subSize = existStage[stageSize].size();
if(subSize > 0){
	%>
	 <tr class="CellBGRLongnews">
	 	<td colspan="9" style="font-weight:bold;color: red;">Stages are not defined</td>
	 </tr>
	<%
}
	moduleKey = "";
	mHash = null;
	info = null;
	for(int i = 0; i < subSize; i++){
		rowStyle=(i%2 == 0)? "CellBGR3":"CellBGRnews";
		iNo++;
		mHash = (Hashtable) existStage[stageSize].elementAt(i);
 		info = (ModuleInfo) mHash.elements().nextElement();
 		moduleKey = (String) mHash.keys().nextElement();
%>
<tr class=<%=rowStyle%>>
 <td width ="3%" align = "center"><INPUT type ="checkbox" name="checkForUpdate"/></TD>
 <td width ="3%" align = "center"><%=iNo%><INPUT TYPE ="hidden" name ="vtModuleID" value = "<%=moduleKey%>"/></td>
 <TD>
	 <%if(right == 3 && !isArchive){%>
	 	<a href="Fms1Servlet?reqType=<%=Constants.WO_DELIVERABLE_UPDATE_PREP%>&vtid=<%=moduleKey%>">
	 <%}%><%=info.name%>
	 <%if(right == 3 && !isArchive){%>
	 	</a>
	 <%}%>
 </TD>
 <td>
	 <% if( info.plannedReleaseDate != null) {
	 	out.println(CommonTools.dateFormat(new java.util.Date(info.plannedReleaseDate.getTime())));
	 }else{
	 	out.println("N/A");
	 }
	 %>
 </td>
 <td>
	 <% if( info.rePlannedReleaseDate != null) {
	 	out.println(CommonTools.dateFormat(new java.util.Date(info.rePlannedReleaseDate.getTime())));
	 }else{
	 	out.println("N/A");
	 }
	 %>
 </td>
 <td>
	 <% if( info.actualReleaseDate != null) {
	 	out.println(CommonTools.dateFormat(new java.util.Date(info.actualReleaseDate.getTime())));
	 }else{
	 	out.println("N/A");
	 }
	 %>
 </td>
 <td>
  	<%
  		switch(info.status){
			case 1:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Pending")%> <%break;
			case 2:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Accepted")%> <%break;
			case 3:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Rejected")%> <%break;
			case 4:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Cancelled")%> <%break;
			case 6:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.InProgress")%> <%break;
			case 5:%> <%=languageChoose.getMessage("fi.jsp.woDeliverable.Others")%> <%break;
		}
	%>
  </td>
 <td>
 	<%= CommonTools.formatDouble(info.deviation)%>
 </td>
 <td>
 	<%=(info.note==null)? "N/A":ConvertString.toHtml(info.note)%>
 </td>
</tr>
<%
	}
%>
</TBODY>
</table>
<P>
<%
if(right == 3 && !isArchive){%>
<form name="frm_deliverable" method = "post">
	<INPUT TYPE ="hidden" name = "listUpdate"/>
	<input type = "button" name="woDeliAdd" value=" <%=languageChoose.getMessage("fi.jsp.woDeliverable.Addnew")%> " class = "BUTTON" onclick = "javascript:addClick();">
	<% if (deliverSize > 0) { %>
	<input type = "button" name="woDeliUpdate" value=" <%=languageChoose.getMessage("fi.jsp.woDeliverable.Update")%> " class = "BUTTON" onclick = "javascript:updateClick();">
	<input type = "button" name="woDeliDelete" value=" <%=languageChoose.getMessage("fi.jsp.woDeliverable.Delete")%> " class = "BUTTON" onclick = "javascript:deleteClick();">
	<% } %>
</form>
<p>&nbsp;</p>
<%}%>

<SCRIPT language="javascript">
var isImportHide = true;
	function Import(){
		isImportHide = !isImportHide;
	 	var ImportTable = document.getElementById("ImportTable");
  		if (isImportHide) {
    		ImportTable.style.display="";
   		}else{
    		ImportTable.style.display="none";
		}
    }
</SCRIPT>

<SCRIPT language="javascript">

	function selectAll(){
		var uCheck = document.getElementsByName("checkForUpdate");

		for (i = 0; i < uCheck.length; i++) {
			uCheck[i].checked = document.getElementById("selectAll").checked;
		}
	}

	function doImport(ext) {
	    if (ext != '') {
	        document.frm_Import.submit();
	    }
	}

	function checkName() {
	    var name = document.frm_Import.importFile.value;

	    if (name == "" || name == null) {
	        alert("Select MS Excel file!");
	        document.frm_Import.importFile.focus();
	        return;
	    }
	    else {
	        var ext = name.substring(name.lastIndexOf(".") + 1, name.length);
	        ext = ext.toLowerCase();
	        if (ext != "xls") {
	            alert("Select MS Excel file!");
	            document.frm_Import.importFile.focus();
	            return;
	        }
	        else {
	            doImport(ext);
	        }
	    }
	}

	function addStage(){
		frm.action="stageAdd.jsp?source=1";
		frm.submit();
	}


	function addClick(){
		<% if (mSize == 0) { %>
			alert("There is no more deliverable to add");
			return false;
		<% } %>
		frm_deliverable.action = "Fms1Servlet?reqType=<%=Constants.WO_DELIVERABLE_BATCH_PRE_ADD%>";
		// WO_DELIVERABLE_BATCH_PRE_ADD
		// WO_DELIVERABLE_ADD_PREP
		frm_deliverable.submit();
	}

	function updateClick(){
		checkBatchUpdate();
	}

	function deleteClick(){
		checkBatchDelete();
	}

	function checkBatchUpdate() {
		var uCheck = document.getElementsByName("checkForUpdate");
		var idList = document.getElementsByName("vtModuleID");

		var uList = "";

		var uLength = uCheck.length;
		var nChecked = 0;

		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";
				nChecked = 1;
			}
		}

		if (nChecked == 0) {
			alert("Please choose data to update");
			return false;
		} else {
			uList = uList.substring(0,uList.length-1);
			frm_deliverable.listUpdate.value = uList;
			frm_deliverable.action = "Fms1Servlet?reqType=<%=Constants.WO_DELIVERABLE_BATCH_PRE_UPDATE%>";
			frm_deliverable.submit();
		}
	}

	function checkBatchDelete() {
		var uCheck = document.getElementsByName("checkForUpdate");
		var idList = document.getElementsByName("vtModuleID");

		var uList = "";
		var uLength = uCheck.length;
		var nChecked = 0;

		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";
				nChecked = 1;
			}
		}

		if (nChecked == 0) {
			alert("Please choose data to delete");
			return false;
		} else {
			if (confirm("Do you really want to delete ?")) {
				uList = uList.substring(0,uList.length-1);
				frm_deliverable.listUpdate.value = uList;
				frm_deliverable.action = "Fms1Servlet?reqType=<%=Constants.WO_DELIVERABLE_BATCH_DELETE%>";
				frm_deliverable.submit();
			} else return false;
		}
	}
</SCRIPT>

</BODY>
</HTML>
