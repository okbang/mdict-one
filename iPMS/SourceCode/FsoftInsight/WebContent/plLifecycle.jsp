<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plLifecycle.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	int right = Security.securiPage("Project plan",request,response);
	Vector tailoringList = (Vector) session.getAttribute("tailoringList");
	Vector iterationList = (Vector) session.getAttribute("PLIterationList");
	Vector milestoneList = (Vector)session.getAttribute("plMilestoneList");
	ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("ProjectInfo");
	//Vector vtStage=(Vector)session.getAttribute("stageVector");
	//Vector perfVector = (Vector) session.getAttribute("WOPerformanceVector");
	//MetricInfo durationMetric = (MetricInfo) session.getAttribute("durationMetric");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
	
	// HaiMM: CR - Add Requirement section
	boolean existData = false;
	ReqChangesInfo reqInfo = (ReqChangesInfo) session.getAttribute("ReqChangesInfo");	
	if (reqInfo != null) existData = true;
	
	Vector vIntegrList = (Vector) session.getAttribute("ProIntegrList");
	
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plLifeCycle.ProjectplanLifecycle")%></P>
<FORM method="post"  name="frm"></form>
<TABLE width="95%" class="HDR">
    <TBODY>
        <TR>
            <TD width = "10%"><%=languageChoose.getMessage("fi.jsp.plLifeCycle.LifeCycle")%>:</TD>
            <TD><%=projectInfo.getLifecycle()%></TD>
        </TR>
    </TBODY>
</TABLE>
<br>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class = "TableCaption"><A name="iteration"><%=languageChoose.getMessage("fi.jsp.plLifecycle.Iteration") %></A></CAPTION>

<TR class="ColumnLabel">
<TD width = "24" align = "center">#</TD>
<TD ><%=languageChoose.getMessage("fi.jsp.plLifecycle.Stage") %></TD>
<TD ><%=languageChoose.getMessage("fi.jsp.plLifecycle.Iteration") %></TD>
<TD><%=languageChoose.getMessage("fi.jsp.plLifeCycle.Plannedenddate")%></TD>
<TD><%=languageChoose.getMessage("fi.jsp.plLifeCycle.Replannedenddate") %></TD>                       
<TD ><%=languageChoose.getMessage("fi.jsp.plLifecycle.Description") %></TD>
<TD ><%=languageChoose.getMessage("fi.jsp.plLifecycle.Milestone") %></TD>
</TR>
<%String className = "";
for(int i = 0; i < iterationList.size(); i++){
	className=(i%2==0)?"CellBGRnews":"CellBGR3";
 	IterationInfo iterationInfo = (IterationInfo) iterationList.elementAt(i);	
%>
<tr class="<%=className%>">
<td width = "24" align = "center"><%=i+1%></td>
<td><p><%if(right == 3 && !isArchive){%><a href="Fms1Servlet?reqType=<%=Constants.PL_ITERATION_UPDATE_PREPARE %>&plIteration_ID=<%=iterationInfo.iterationID%>"><%}%>
<%for(int j = 0; j < milestoneList.size(); j++){
	MilestoneInfo milestoneInfo = (MilestoneInfo)milestoneList.elementAt(j);
	long milestoneID = milestoneInfo.getMilestoneId();
	if(iterationInfo.milestoneID == milestoneID){
	%><%=milestoneInfo.getName()%>
<%	}
}
if(right == 3  && !isArchive){%></a><%}%>
</td>
<td><%=iterationInfo.iteration%></td>
<td><%=CommonTools.dateFormat(iterationInfo.planEndDate)%></td>
<td><%=CommonTools.dateFormat(iterationInfo.replanEndDate)%></td>
<td><%=(iterationInfo.description==null?"N/A":ConvertString.toHtml(iterationInfo.description))%></td>
<td><%=(iterationInfo.milestone==null?"N/A":ConvertString.toHtml(iterationInfo.milestone))%></td>
</tr>
<%}%>
</table>
<%if(right == 3 && !isArchive){%>
<form name="frm_plIterationAddPrep" action="Fms1Servlet" method = "get"><BR>
<input type = "hidden" name="reqType1" value="<%=Constants.PL_ITERATION_ADD_PREPARE%>">
<input type = "hidden" name="reqType" value="<%=Constants.PL_ITERATION_ADD_PREPARE%>">
<P><input type="submit" name="add" value="<%=languageChoose.getMessage("fi.jsp.plLifecycle.Addnew") %>" class="BUTTON" ></p>
</form>
<%}%>
<br>
<FORM name="frm_tailoringList" action="Fms1Servlet" method="get">
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class = "TableCaption"><A name="tailoring"><%=languageChoose.getMessage("fi.jsp.plLifecycle.Tailoringdeviation")%></A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width = "24" align = "center"># </TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Description")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.AppCri")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Reason")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Action")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Type")%></TD>
        </TR>
	<%
		for (int i = 0; i < tailoringList.size(); i++) {
 			className = (i%2 == 0) ? "CellBGRnews" : "CellBGR3";
 			TailoringDeviationInfo info = (TailoringDeviationInfo)tailoringList.elementAt(i);
	%>
<tr class="<%=className%>">
	<td width="24" align="center"><%=i+1%></td>
	<td>
	<%
		if ((right == 3 && !isArchive)&&(info.type==1)) {
	%>
		<a href="Fms1Servlet?reqType=<%=Constants.TAILORING_UPDATE_PRE%>&pro_tail_ID=<%=info.process_tail_ID%>&tailoring_source=1">
	<%
		} else {
	%>
	<%
			if ((right == 3)&&(info.type==2) && !isArchive) {
	%>
		<a href="Fms1Servlet?reqType=<%=Constants.DEVIATION_UPDATE_PRE%>&deviation_ID=<%=info.tailoringID%>&tailoring_source=1">
	<%
			}
		}
	%>
   		<%=(info.modification == null ? "N/A" : ConvertString.toHtml(info.modification))%>
   	<%
   		if(right == 3 && !isArchive) {
   	%>
   		</a>
   	<%
   		}
   	%>
	</td>
	<td><%=(info.reason==null? "N/A" :ConvertString.toHtml(info.reason))%></td>
	<td><%=(info.note==null? "N/A" :ConvertString.toHtml(info.note))%></td>

	<td>
	<%switch(info.action){
		case 1:%><%=languageChoose.getMessage("fi.jsp.plLifecycle.Added") %><%break; 
		case 2:%><%=languageChoose.getMessage("fi.jsp.plLifecycle.Modified") %><%break;
		case 3:%><%=languageChoose.getMessage("fi.jsp.plLifecycle.Deleted") %><%break; 
	}%>
	</td>
	<td><%switch(info.type){
		case 1:	%><%=languageChoose.getMessage("fi.jsp.plLifecycle.Tailoring") %><%break; 
		case 2:	%><%=languageChoose.getMessage("fi.jsp.plLifecycle.Deviation") %><%break;	 
	}%>
	</td>
</tr>
<%}%>
</table>
</form>
<br>
<%if(right == 3 && !isArchive){%>
<form name="frm_plTailoringAddPrep" action="Fms1Servlet" method = "get">
<input type = "hidden" name="tailoring_source" value="1">
<input type = "hidden" name="reqType1" value="<%=Constants.PL_LIFECYCLE_GET_PAGE%>">
<input type="submit" name="add" value="<%=languageChoose.getMessage("fi.jsp.plLifecycle.Addtailoring") %>" class="BUTTON" onClick="OnAddTailoring();" >
<input type="submit" name="add" value="<%=languageChoose.getMessage("fi.jsp.plLifecycle.Adddeviation") %>" class="BUTTON" onClick="OnAddDeviation();">
<input type = "hidden" name="reqType">
</form>
<%}
String filename=(String)request.getAttribute("schedFileName");
String uploadResult=(String)request.getAttribute("upload");

%>
<br>
<P class = "TableCaption"><%=languageChoose.getMessage("fi.jsp.plLifecycle.Schedule") %></P>
<form action="Fms1Servlet?reqType=<%=Constants.PL_UPLOAD_SCHED%>#uploadresult" enctype="MULTIPART/FORM-DATA" method=post name="frmUpload">
	 <%if (uploadResult!=null){%>
	 <A NAME="uploadresult" class="ERROR"><%=languageChoose.getMessage(uploadResult)%></A><BR>
	 <%}%>
	<%if (filename!=null){%>
		<A href="Fms1Servlet?reqType=<%=Constants.PL_GET_SCHED_FILE%>" TARGET="mpp" title="<%=filename%>"><%=languageChoose.paramText(new String[]{"fi.jsp.plLifeCycle.Download__~PARAM1_FILENAME~", filename}) %></A>
	<BR>
	<%}
	if(right == 3 && !isArchive){%>
	<%=languageChoose.getMessage("fi.jsp.plLifecycle.Selectfiletouploadmax.4Mb") %> <input type="file" name="xorx"  />
	<br />
	<input type="button" class="BUTTON"  value="<%=(filename==null?languageChoose.getMessage("fi.jsp.plLifeCycle.Upload"):languageChoose.getMessage("fi.jsp.plLifeCycle.Overwrite"))%>" onclick="doUpload()"/>
	<%}%>
</form>

<BR>
<TABLE cellspacing="1" class="Table" width="95%" id="tableReqChangesMng">
<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plReqChangesMng.RequirementChangesManagement")%> </CAPTION>
   	<TBODY>
        <TR>
            <TD class="ColumnLabel" width = 35%> Where is the change request logged? </TD>
            <TD class="CellBGR3">&nbsp;<%=(existData == true) ? (reqInfo.reqLogLocation == null ? "N/A": ConvertString.toHtml(reqInfo.reqLogLocation)) : "N/A"%> </TD>
        </TR>        
    	<TR>
        	<TD class="ColumnLabel" width = 35%> Who logs the change request? </TD>
            <TD class="CellBGR3" >&nbsp;<%= (existData == true) ? (reqInfo.reqCreator == null ? "N/A": ConvertString.toHtml(reqInfo.reqCreator)) : "N/A"%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width = 35%> Who reviews the change request? </TD>
            <TD class="CellBGR3">&nbsp;<%=(existData == true) ? (reqInfo.reqReviewer == null ? "N/A": ConvertString.toHtml(reqInfo.reqReviewer)) : "N/A"%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width = 35%> Who approves the change request? </TD>
            <TD class="CellBGR3">&nbsp;<%=(existData == true) ? (reqInfo.reqApprover == null ? "N/A": ConvertString.toHtml(reqInfo.reqApprover)) : "N/A"%> </TD>
        </TR>
    </TBODY>
</TABLE>

<BR>
<%if(right == 3 && !isArchive ){%>
<form name = "frmReqChangesMng" method = "post" action="Fms1Servlet">
	<input type="hidden" name="reqType" value="0">
	<input type = "hidden" name ="groupName" value = "0">
<% if (!existData) { %>
	<input type="button" name="woReqChangesAdd" value="<%=languageChoose.getMessage("fi.jsp.plReqChangesMng.AddNew")%>" class = "BUTTON" onclick = "javascript:addClick();">
<% } else { %>
	<input type="button" name="woReqChangesUpdate" value="<%=languageChoose.getMessage("fi.jsp.plReqChangesMng.Update")%>" class = "BUTTON" onclick = "javascript:updateClick();">
<% } %>
	<INPUT type="button" name="woBack" value="<%=languageChoose.getMessage("fi.jsp.plReqChangesMng.Back")%>" class="BUTTON" onclick="javascript:doBack();">
</form>
<%}%>
<BR>

<TABLE cellspacing="1" width="95%" class="Table" id="tableReqChangesMng">
	<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.RequirementChangesManagement")%></CAPTION>
		<THEAD>
			<TR class="ColumnLabel">
				<TD width = "3%" align = "center"> # </TD>
				<TD width="17%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.ComponentID")%> </TD>
				<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.Description")%> </TD>
				<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.IntegratedWithComponents")%> </TD>
				<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.IntegrationOrder")%> </TD>
				<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.IntegrationReadyNeed")%> </TD>
			</TR>
		</THEAD>
		<TBODY>	
<%
		IntegrStratInfo integrInfo;
		int integrSize = vIntegrList.size();
		for(int i = 0; i < integrSize; i++){
			integrInfo = (IntegrStratInfo) vIntegrList.elementAt(i);
		 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
%>		
			<TR class="<%=className%>">
				<TD align = "center"><%=i+1%></TD>
				<TD><%=integrInfo.integrCompID == null ? "N/A": ConvertString.toHtml(integrInfo.integrCompID)%></TD>
				<TD><%=integrInfo.integrDesc == null ? "N/A": ConvertString.toHtml(integrInfo.integrDesc)%></TD>
				<TD><%=integrInfo.integrWithComp == null ? "N/A": ConvertString.toHtml(integrInfo.integrWithComp)%></TD>
				<TD><%=integrInfo.integrOrder == null ? "N/A": ConvertString.toHtml(integrInfo.integrOrder)%></TD>
				<TD><%=integrInfo.integrReadyNeed == null ? "N/A": ConvertString.toHtml(integrInfo.integrReadyNeed)%></TD>
			</TR>
<%
		}
%>			
		</TBODY>
</TABLE>
<BR>
<%if(right == 3 && !isArchive ){%>
<form name = "frmIntegrationStrategy" method = "post" action="Fms1Servlet">
<input type="hidden" name="reqType" value="0">
<input type="button" name="plIntegrAdd" value="<%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.AddNew")%>" class = "BUTTON" onclick = "javascript:addClick_Pro();">
<% if (integrSize > 0) { %>
<input type="button" name="plIntegrUpdate" value="<%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.Update")%>" class = "BUTTON" onclick = "javascript:updateClick_Pro();">
<% } %>
<INPUT type="button" name="plBack" value="<%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.Back")%>" class="BUTTON" onclick ="javascript:doBack_Pro();">
</form>
<%}%>
<BR>
<SCRIPT language="javascript">

	function doBack_Pro(){
		frmIntegrationStrategy.reqType.value = <%=Constants.WORKUNIT_HOME%>;
		frmIntegrationStrategy.submit();
	}
	
	function addClick_Pro(){		
		frmIntegrationStrategy.reqType.value=<%=Constants.PL_INTEGRATION_STRATEGY_PREPARE_ADD%>;	
		frmIntegrationStrategy.submit();
	}
	
	function updateClick_Pro(){		
		frmIntegrationStrategy.reqType.value=<%=Constants.PL_INTEGRATION_STRATEGY_PREPARE_UPDATE%>;
		frmIntegrationStrategy.submit();
	}


	function doBack(){
		frmReqChangesMng.reqType.value = <%=Constants.WORKUNIT_HOME%>;
		frmReqChangesMng.submit();
	}
	
	function addClick(){		
		frmReqChangesMng.reqType.value=<%=Constants.PL_REQ_CHANGES_MNG_PREPARE_ADD%>;	
		frmReqChangesMng.submit();
	}
	
	function updateClick(){		
		frmReqChangesMng.reqType.value=<%=Constants.PL_REQ_CHANGES_MNG_PREPARE_UPDATE%>;	
		frmReqChangesMng.submit();
	}

	function OnAddTailoring(){
		frm_plTailoringAddPrep.reqType.value=<%=Constants.PROCESS_TAILORING%>;
		frm_plTailoringAddPrep.submit;
	}
	function OnAddDeviation(){
		frm_plTailoringAddPrep.reqType.value=<%=Constants.DEVIATION_ADD_PREPARE %>;
		frm_plTailoringAddPrep.submit;
	}
	function doUpload(){
		if (frmUpload.xorx.value == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.plLifecycle.PleaseSelectfiletoupload")%>");
			frmUpload.xorx.focus();
			return;
		}
		frmUpload.submit();
	}
</script>

</BODY>
</HTML>