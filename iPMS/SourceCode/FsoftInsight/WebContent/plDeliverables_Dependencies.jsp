<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plDevilerables_Dependencies.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
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
	Vector vtStage=(Vector)session.getAttribute("stageVector");
	Vector deliverableList = (Vector) session.getAttribute("deliverableList");
	
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

	String rowStyle = "";
	if (deliverableList.size() > 12)
	{%>
<BODY onload="loadPrjMenu();makeScrollableTable('tableDeliverable',true,'250')" class="BD">
<%} else {%>
<BODY class="BD" onload="loadPrjMenu()">
<%}
int right = Security.securiPage("Project plan",request,response);
int woRight =Security.getRights("Work Order",request);
Vector dependencyList = (Vector) session.getAttribute("PLDependencyList");
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.ProjectplanDeliverablesandDependencies")%></P>
<br>
<%if (woRight==3 && !isArchive){%><p> <A href="Fms1Servlet?reqType=<%=Constants.WO_DELIVERABLE_GET_LIST%>"><%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.NoteDeliverablescanbeupdatedinWorkorder")%></A></p><%}%>
<p class = "TableCaption"> <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Deliverables")%> </p>
<TABLE cellspacing="1" class="Table" width="95%" id="tableDeliverable">
<THEAD>
	<TR class="ColumnLabel">
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
 	<td colspan="2" style="font-weight:bold">&nbsp;<%=stageInfo.stage == null? "": stageInfo.stage%></td>
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
<br>

<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class = "TableCaption"><A name="dependencies"><%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.CriticalDependencies")%></A></CAPTION>

<TR class="ColumnLabel">
	<TD width = "24" align = "center">#</TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Dependency")%> </TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Expecteddeliverydate")%> </TD>
	<TD width="200"> <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Note1")%> </TD>
</TR>
<%
String className = "";
for(int i = 0; i < dependencyList.size(); i++){
	className = (i%2==0)?"CellBGRnews":"CellBGR3";
 	DependencyInfo dependencyInfo = (DependencyInfo) dependencyList.elementAt(i);
%>
<tr class="<%=className%>">
	<td width = "24" align = "center"><%=i+1%></TD>
	<TD>
		<a href="Fms1Servlet?reqType=<%=Constants.PL_DEPENDENCY_VIEW%>&plDependency_depID=<%=dependencyInfo.dependencyID%>">
		<%=((dependencyInfo.item == null||dependencyInfo.item.equals(""))?"N/A":ConvertString.toHtml(dependencyInfo.item))%>
		</a>
	</TD>
	<TD><%=((dependencyInfo.plannedDeliveryDate == null)? "N/A": CommonTools.dateFormat(new java.util.Date(dependencyInfo.plannedDeliveryDate.getTime())))%></TD>
	<TD>
	<%=((dependencyInfo.note == null||dependencyInfo.note.equals(""))?"N/A":ConvertString.toHtml(dependencyInfo.note))%>
	</TD>
</TR>
<%}%>
</table>
<br>
<%if(right == 3 && !isArchive){%>
<table align = "left">
<TR>
<TD>
	<form name="frm_plDependencyAddPrep" action="plDependencyAdd.jsp" method = "get">
	<input type="submit" name="add" value=" <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Addnew")%> " class="BUTTON" >
</form>
</TD>
</TR>
</table>
<%}%>


</BODY>
</HTML>
