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
<TITLE>woGeneralInfoView.jsp</TITLE>
</HEAD>

<%LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");%>

<BODY class="BD" onload="loadPrjMenu()">
<%
	int right = Security.securiPage("Work Order",request,response);
	// only QA has role for Clossing/Cancelling a Project
	UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
	int iCheckRoleQA = userLoginInfo.getUserRoleLogin();
	ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("WOGeneralInfo");
	Vector constraintList = (Vector) session.getAttribute("PLConstraintList");
	Vector assumptionList = (Vector) session.getAttribute("PLAssumptionList");
	String archiveStatus = (String) session.getAttribute("archiveStatus");
	int i;
	String className=null;
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
		
	boolean mustBeReopened = ProjectInfo.STATUS_CLOSED == projectInfo.getStatus() ||
	                         ProjectInfo.STATUS_CANCELLED == projectInfo.getStatus();
%>
<TABLE width="95%">
	<TR>
		<TD><P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.WorkorderInformation")%> </P></TD>
		<TD align="right" valign="bottom"><A href="Fms1Servlet?reqType=<%=Constants.WO_EXPORT%>" target="about:blank"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ExportWorkOrder")%> </A></TD>
	</TR>
</TABLE>
<BR>
<FORM name="frm" method="POST">

<p align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.General")%> </p>
<TABLE cellspacing="1" class="Table" width="95%">
   	<TBODY>
    	<TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ProjectName")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getProjectName()%> </TD>
        </TR>
    	<TR>
        	<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ProjectCode")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getProjectCode()%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ContractType")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getContractType()%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Customer")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getCustomer()%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.2ndCustomer")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getSecondCustomer()%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ProjectLevel")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getProjectLevel()%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ProjectRank")%> </TD>
            <TD class="CellBGR3"> 
            <%                        	
            	String rank;
            	if ("".equals(projectInfo.getProjectRank()))
            		rank = "N/A"; 
            	else if ("?".equals(projectInfo.getProjectRank()))
            		rank = "Not Rank";
            	else
            	 	rank = projectInfo.getProjectRank();            	 
            %>
            <%=rank%> 
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Group")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getGroupName()%> </TD>            
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Division")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getDivisionName()%> </TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ProjectType")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getProjectType()%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ProjectManager")%> </TD>
            <TD class="CellBGR3"><%=projectInfo.getLeaderName()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ProjectCategory")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getLifecycle()%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.BusinessDomain")%> </TD>
            <TD class="CellBGR3"><%=projectInfo.getBusinessDomain()%></TD>
        </TR>
        <TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ApplicationType")%> </TD>
            <TD class="CellBGR3"> <%=projectInfo.getApplicationType()%> </TD>
        </TR>        
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.ScopeAndObjective")%> </TD>
            <TD class="CellBGR3"> <%=ConvertString.toHtml(projectInfo.getScopeAndObjective())%> </TD>
        </TR>
		<TR>
        	<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Status")%> </TD>
            <TD class="CellBGR3"><%=ProjectInfo.parseStatus(projectInfo.getStatus())%></TD>
		</TR>
    </TBODY>
</TABLE>
<BR>
<%	
	if(right == 3 && !isArchive && !mustBeReopened){
%>
		<input type="button" value="<%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Update")%>" class="BUTTON" onClick="jumpURL('Fms1Servlet?reqType=<%=Constants.WO_GENERAL_INFO%>')">
<%
	}
%>
<%
	if ( right == 3 && iCheckRoleQA == 2 && !isArchive) {
		if (mustBeReopened) {
%>
		<INPUT type="button" class="BUTTON" style='width:80' onclick="javascript:<%="doReopenProject()"%>" value="<%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Reopen")%>">
<%
		}else{
%>
		<INPUT type="button" class="BUTTON" style='width:80' onclick="javascript:<%="doCloseProject()"%>" value="<%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.CloseCancel")%>">
<%
		}
	}
%>
</FORM>
<SCRIPT language="javascript">
	function doReopenProject(){
	 	if (!window.confirm("<%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.DoYouWantToReopenThisProject")%>")) {
 			return;
	 	}
	 	frm.action = "Fms1Servlet?reqType=<%=Constants.WO_REOPEN_PROJECT%>";
	 	frm.submit();
    }
	function doCloseProject(){
		frm.action = "WOProjectClose.jsp";
		frm.submit();
	}
</SCRIPT>
<BR> <BR> <BR>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class = "TableCaption"><A name="constraints"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Constraints")%> </A></CAPTION>
	<TBODY>
    	<TR class="ColumnLabel">
        	<TD width="24" align="center"> # </TD>
            <TD width = "50%"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Description")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Note")%> </TD>
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Type")%> </TD>
		</TR>
		<%
			for(i = 0; i < constraintList.size(); i++)
			{
				className=(i%2==0)?"CellBGRnews":"CellBGR3";
	 			ConstraintInfo constraintInfo = (ConstraintInfo) constraintList.elementAt(i);
		%>
		<TR>
			<TD class="<%=className%>" width="24" align="center"> <%=i+1%> </TD>
			<TD class="<%=className%>">
			<%
				if (right == 3 && !isArchive)
				{
			%>
					<A href="plConstraintUpdate.jsp?plConstraint_ID=<%=constraintInfo.constraintID%>">
			<%
				}
			%>

			<%=(constraintInfo.description == null||constraintInfo.description.trim().equals(""))?"N/A": ConvertString.toHtml(constraintInfo.description)%>

			<%
				if(right == 3 && !isArchive )
				{
			%>
					</A>
			<%
				}
			%>
			</TD>
			<TD class="<%=className%>"><%=(constraintInfo.note == null||constraintInfo.note.trim().equals(""))?"N/A": ConvertString.toHtml(constraintInfo.note)%></TD>
			<TD class="<%=className%>"> <%=languageChoose.getMessage(constraintInfo.GetNameOfType())%> </TD>
		</TR>
		<%
			}
		%>
	</TBODY>
</TABLE>

<BR>

<%
	if(right == 3 && !isArchive)
	{
%>
	<input type="submit" name="add" value="<%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Addnew")%>" class="button" onclick="window.location= 'plConstraintAdd.jsp' ">
	<BR> <BR>
<%
	}
%>

<BR>

<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class="TableCaption"> <A name="Assumptions"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Assumptions")%> </A></CAPTION>
	<TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center"> # </TD>
            <TD width="50%"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Description")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Note")%> </TD>
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Type")%> </TD>
        </TR>
		<%
			for(i = 0; i < assumptionList.size(); i++)
			{
				className=(i%2==0)?"CellBGRnews":"CellBGR3";
			 	ConstraintInfo assumptionInfo = (ConstraintInfo) assumptionList.elementAt(i);
		%>
		<TR class="<%=className%>">
			<TD width ="24" align ="center"> <%=i+1%> </TD>
			<TD>
			<%
				if(right == 3 && !isArchive)
				{
			%>
					<A href="plAssumptionUpdate.jsp?plAssumption_ID=<%=assumptionInfo.constraintID%>">
			<%
				}
			%>
			
			<%=(assumptionInfo.description == null||assumptionInfo.description.trim().equals("")) ? "N/A" : ConvertString.toHtml(assumptionInfo.description)%>
			
			<%
				if(right == 3 && !isArchive)
				{
			%>
					</A>
			<%
				}
			%>
			</TD>
			<TD><%=(assumptionInfo.note == null||assumptionInfo.note.trim().equals(""))?"N/A": ConvertString.toHtml(assumptionInfo.note)%></TD>
			<TD> <%=languageChoose.getMessage(assumptionInfo.GetNameOfType())%> </TD>
		</TR>
		<%
			}
		%>
	</TBODY>
</TABLE>

<BR>

<%	
	if(right == 3 && !isArchive)
	{
%>
	<input type="submit" name="add" value="<%=languageChoose.getMessage("fi.jsp.woGeneralInfoView.Addnew")%>" class="button" onclick="window.location= 'plAssumptionAdd.jsp'">
	<BR> <BR>
<%
	}
%>
</BODY>
</HTML>