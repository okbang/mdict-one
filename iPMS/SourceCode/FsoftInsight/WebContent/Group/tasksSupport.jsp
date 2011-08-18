<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.*,java.util.Calendar,com.fms1.tools.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="JavaScript">
        <%@ include file="../javaFns.jsp"%>
</SCRIPT>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	int right=Security.securiPage("Group monitoring",request,response);
	FilterInfo inf=(FilterInfo)session.getAttribute("tasksfilter");
	boolean report=(request.getParameter("report")!=null);
	if (report) {
		response.setContentType(Fms1Servlet.CONTENT_TYPE_EXCEL);
		response.addHeader("Content-Disposition", "attachment;filename=tasksSupport.xls");	
	}
	response.setContentType((report)?Fms1Servlet.CONTENT_TYPE_EXCEL:Fms1Servlet.CONTENT_TYPE_NORMAL);

	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	int []types=null;
	boolean isSupport=(workUnitID==Parameters.SQA_WU||workUnitID==Parameters.PQA_WU);
	if (workUnitID==Parameters.SQA_WU) 
		types=TaskInfo.allTypesSQA;
	else if(workUnitID==Parameters.PQA_WU)
		types=TaskInfo.allTypesPQA;
	else
		types=TaskInfo.allTypesOrg;
	Vector tasks=(Vector)session.getAttribute("tasks");
	String [] status=isSupport ?TaskInfo.statusSQA:TaskInfo.statusOrg;
	long projectID=inf.projectID;
	long groupID=inf.groupID;
	Vector prjList=(Vector)session.getAttribute("projectstasks");
	int completion=inf.completion;
	int type=inf.type;
	Vector grpList=(Vector)session.getAttribute("groupstasks");
	
	String fromDateStr=CommonTools.dateUpdate(inf.fromDate);
	long cboAss=inf.assignment;
	
	String toDateStr=CommonTools.dateUpdate(inf.toDate);
	Vector userList=(Vector)session.getAttribute("userstasks");
	String selectedType=null;
	String assignedTo=null;
	String selectedGroup="";
	String selectedProject=null;
	String sel;
	if (report){%>
		<style TYPE="text/css"> 
		<!-- 
		<%@ include file="../stylesheet/fms.css" %>	
		--> 
		</style> 
	<%}else{%>
		<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
	<%}%>
<TITLE>tasksSupport.jsp</TITLE>
</HEAD>
<BODY  class="BD" onload="<%=CommonTools.getMnuFunc(session)%><%=((grpList!=null)?"chgGroup()":"")%>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Tasks")%> </P>
<%if (!report){%>
	<P align=right><A href="javascript:report()" > <%=languageChoose.getMessage("fi.jsp.tasksSupport.Export")%> </A></P>
	<br>
	<P> <%=languageChoose.paramText(new String[]{"fi.jsp.tasksSupport.Note__Only__running__projects__during__the__selected__timeframe__will__be__displayed__in__the__~PARAM1_STRING~__project__~PARAM2_STRING~__combo", "<i>", "</i>"})%></P>
<%}else{%>
	<!--
<%}%>
<FORM action="Fms1Servlet?reqType=<%=Constants.TASK_LIST%>" name="frm" method="post">
<INPUT type="hidden" name="report" value="1" disabled>
<TABLE >
	<TBODY>
		<TR class="NormalText">
			<TD><%=languageChoose.getMessage("fi.jsp.tasksSupport.Fromdate")%></TD>
			<TD NOWRAP><INPUT size="9" type="text" maxlength="9" name="fromDate" value="<%=fromDateStr%>">(DD-MMM-YY)</TD>
			<%if (isSupport ||workUnitID==Parameters.FSOFT_WU){%>
			<TD><%=languageChoose.getMessage("fi.jsp.tasksSupport.Group")%></TD>
			<TD>
				<SELECT name="group" class="COMBO" onchange="chgGroup()">
					<OPTION value="0"<%=(0== groupID ? " selected" : "")%>><%=languageChoose.getMessage("fi.jsp.tasksSupport.all")%></OPTION>
					<%for(int i = 0; i < grpList.size(); i++) {
						GroupInfo grpInf=(GroupInfo)grpList.elementAt(i);
						if (grpInf.wuID == groupID) {
							sel="selected";
							selectedGroup=grpInf.name;
						}
						else
							sel="";
						%><OPTION value="<%=grpInf.wuID+"\""+sel+">"+grpInf.name%></OPTION>
					<%}%>
				</SELECT>
			</TD>
			<%}%>
			<TD><%=languageChoose.getMessage("fi.jsp.tasksSupport.Status")%></TD>
			<TD>
				<SELECT name="completion" class="COMBO">
					<OPTION value="0"<%=(0== completion ? " selected" : "")%>><%=languageChoose.getMessage("fi.jsp.tasksSupport.all")%></OPTION>
				<%if(isSupport){ %>
					<OPTION value="-1"<%=(-1== completion ? " selected" : "")%>><%=languageChoose.getMessage("fi.jsp.tasksSupport.Open")%></OPTION>
				<%}
				for(int i=1;i<status.length;i++){%>
					<OPTION value=<%=i+(i== completion ? " selected" : "")%>><%=languageChoose.getMessage(status[i])%></OPTION>
				<%}%>
				</SELECT>
			</TD>
		</TR>
		<TR class="NormalText">
			<TD><%=languageChoose.getMessage("fi.jsp.tasksSupport.Todate")%></TD>
			<TD><INPUT size="9" type="text" maxlength="9" name="toDate" value="<%=toDateStr%>">(DD-MMM-YY)</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.tasksSupport.Project")%></TD>
			<TD>
				<SELECT name="project" class="COMBO">
					<OPTION value="0"<%=(0== projectID ? " selected" : "")%>><%=languageChoose.getMessage("fi.jsp.tasksSupport.all")%></OPTION>
					<%for(int i = 0; i < prjList.size(); i++) {
						ProjectInfo prjInf=(ProjectInfo)prjList.elementAt(i);
						if (prjInf.getProjectId() == projectID) {
							sel="selected";
							selectedProject=prjInf.getProjectCode();
							
						}
						else
							sel="";
						%><OPTION value="<%=prjInf.getProjectId()+"\""+sel+">"+prjInf.getProjectCode()%></OPTION>
					<%}%>
				</SELECT>
			</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.tasksSupport.Type")%></TD>
			<TD>
				<SELECT name="type" class="COMBO">
					<OPTION value="-1"<%=(-1== type ? " selected" : "")%>><%=languageChoose.getMessage("fi.jsp.tasksSupport.all")%></OPTION>
				<%for(int i=0;i<types.length;i++){
					if (types[i]== type )
						selectedType=TaskInfo.types[types[i]];
					%><OPTION value=<%=types[i]+(types[i]== type ? " selected" : "")+">"+languageChoose.getMessage(TaskInfo.types[types[i]])%></OPTION>
				<%}%>
				</SELECT>
			</TD>
		</TR>
		<TR>
		<%if(isSupport){%>
			<TD class="NormalText"><%=languageChoose.getMessage("fi.jsp.tasksSupport.Assignedto")%></TD>
			<TD class="NormalText">
				<SELECT name="cboAss">
					<OPTION value="0" <%=(0== cboAss ? " selected" : "")%>><%=languageChoose.getMessage("fi.jsp.tasksSupport.all")%></OPTION>
					<OPTION value="-1" <%=(-1== cboAss ? " selected" : "")%>>N/A</OPTION>
					<%UserInfo info;
					for(int i=0;i<userList.size();i++){
						info=(UserInfo)userList.elementAt(i);
						if (info.developerID== cboAss)
							assignedTo=info.Name;
						%><OPTION value="<%=info.developerID+"\""+(info.developerID== cboAss ? " selected" : "")+">"+info.account%></OPTION>
					<%}%>
				</SELECT>
			</TD>
		<%}else{%>
		<input type='hidden' name='cboAss' value='0'>
		<%}%>
			<TD colspan =4>
				<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.tasksSupport.Go")%>" onclick="dofilter()">
			</TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<%if (report){%>
-->
<%}%>
<TABLE class="Table" width="95%" cellspacing="1">
	<%	selectedProject=((projectID==0)?"":", " + languageChoose.paramText(new String[]{"fi.jsp.tasksSupport.project~PARAM1_PROJECT_NAME~",selectedProject}));// selectedProject);
		if (grpList!=null)
			selectedGroup=((groupID==0)?"":", " + languageChoose.paramText(new String[]{"fi.jsp.tasksSupport.group~PARAM1_GROUP_NAME~",selectedGroup}));//selectedGroup);
		String strStatus;
		if (completion ==0)	
			strStatus="";
		else if (completion==-1){
			strStatus= ", " + languageChoose.getMessage("fi.jsp.tasksSupport.statusOpen");
		}else{
			strStatus= ", " + languageChoose.paramText(new String[]{"fi.jsp.tasksSupport.status~PARAM1_STATUS~", languageChoose.getMessage(status[completion])});//status[completion];
		}
		String tableTitle = (fromDateStr.length()>0)? languageChoose.paramText(new String[]{"fi.jsp.tasksSupport.From~PARAM1_FROM_DATE~to~PARAM2_TO_DATE~",fromDateStr,toDateStr}):languageChoose.paramText(new String[]{"fi.jsp.tasksSupport.Upto~PARAM1_DATE~",toDateStr});
		tableTitle = tableTitle + selectedGroup+selectedProject+strStatus + ((selectedType==null)?"":", " + languageChoose.paramText(new String[]{"fi.jsp.tasksSupport.type~PARAM1_TYPE~", languageChoose.getMessage(selectedType)}))+((assignedTo==null)?"":", " + languageChoose.paramText(new String[]{"fi.jsp.tasksSupport.assignedto~PARAM1_PERSON~",assignedTo}));
	%> 
    <CAPTION align="left" class="<%= (report)?"ColumnLabel":"TableCaption"%>"><%=tableTitle%></CAPTION>

    <TBODY >
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Group")%> <br> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Project")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Type")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Description")%> </TD>
            <%if(isSupport){ %>
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Assignedto")%> </TD> 
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Actualeffortpd")%> </TD> 
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Planneddate")%> </TD>
            <%}else{%>
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Planneddate1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Replanneddate")%> </TD>
            <%}%>
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Actualdate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Status")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.tasksSupport.Note")%> </TD>
        </TR>
        <%
        TaskInfo taskInfo;
        boolean blStyle=false;
        String style;
        Date now=new Date();
        Date pldate;
        String color;
        for (int i =0;i<tasks.size();i++){
        	
        	taskInfo=(TaskInfo)tasks.elementAt(i);
        	if ((completion==0|| completion ==taskInfo.status||(completion==-1 && (taskInfo.status==TaskInfo.STATUS_PENDING||taskInfo.status==TaskInfo.STATUS_NOT_PASSED)))
	        	&&(0== projectID||taskInfo.prjID==projectID)
	        	&&(groupID==0||groupID==taskInfo.parentwuID||groupID==taskInfo.wuID)
	        	&&(-1== type||taskInfo.typeID==type)
	        	&&( 0== cboAss||taskInfo.assignedTo==cboAss||(cboAss==-1 && taskInfo.assignedTo==0))){
        		blStyle=!blStyle;
        		style=(blStyle)? "CellBGRnews":"CellBGR3";
        		pldate=(taskInfo.rePlanDate!=null)?taskInfo.rePlanDate:taskInfo.planDate;
        		if ( pldate.compareTo(now)<0 && (taskInfo.status == TaskInfo.STATUS_PENDING || taskInfo.status == TaskInfo.STATUS_NOT_PASSED))
        			color="style='color: red'";
        		else
        			color="";
        			
         %><TR class="<%=style%>">
            <TD><%=((taskInfo.prjCode==null)?taskInfo.grpName:taskInfo.prjCode)%></TD>
             <TD><%=languageChoose.getMessage(taskInfo.type)%></TD>
            <TD>
            <%if ((!report) && taskInfo.manualTask){
            	if (right==3) {%>
            	<A HREF="javascript:doIt('<%=Constants.TASK_UPDATEPREP%>&vtID=<%=i%>')">
            	<%}
            }else if (!report && taskInfo.type==TaskInfo.CONTROL){%>
           		<A HREF="javascript:drillOut(<%=i%>)">
            <%}else if (!report){%>
           		<A HREF="javascript:drill(<%=i%>)">
            <%}%>
            <%=(taskInfo.desc==null)?"N/A":taskInfo.desc%>
            </A>
            </TD>
             <%if(isSupport){ %>
            <TD><%=((taskInfo.assignedToStr==null)?"N/A":taskInfo.assignedToStr)%></TD>
            <TD><%=CommonTools.formatDouble(taskInfo.effort)%></TD>
            <TD NOWRAP <%=color%> ><%=CommonTools.dateFormat(taskInfo.planDate)%></TD>
            <%}else{%>
            <TD NOWRAP <%=color%> ><%=CommonTools.dateFormat(taskInfo.planDate)%></TD>
            <TD NOWRAP <%=color%> ><%=CommonTools.dateFormat(taskInfo.rePlanDate)%></TD>
            <%}%>
            <TD NOWRAP <%=color%> ><%=CommonTools.dateFormat(taskInfo.actualDate)%></TD>
            <TD><%=languageChoose.getMessage(status[taskInfo.status])%></TD>
            <TD><%=taskInfo.note == null ? "":ConvertString.trunc(taskInfo.note,100)%></TD>
        </TR>
          	<%}
          }%>
 </TBODY >
</TABLE >
<p>
<%if (isSupport && right==3) {%>
<INPUT type="BUTTON" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.tasksSupport.Addnew")%> " onclick="doIt(<%=Constants.TASK_ADDPREP%>)">
<%}%>
<p>
<SCRIPT language="JavaScript">

	var prjGrpMap=new Array();
	<%for(int i = 0; i < prjList.size(); i++) {
		ProjectInfo prjInf=(ProjectInfo)prjList.elementAt(i);
	%>prjGrpMap[<%=i%>]=[<%=prjInf.getParent()+","+prjInf.getProjectId()+",'"+prjInf.getProjectCode()%>'];
	<%}%>
//objs to hide when submenu is displayed
var objToHide=new Array(frm.fromDate,frm.toDate,frm.cboAss);
	function dofilter(){
		if (mandatoryDateFld(frm.toDate,"<%= languageChoose.getMessage("fi.jsp.tasksSupport.todate")%>"))
		if (dateFld(frm.fromDate,"<%= languageChoose.getMessage("fi.jsp.tasksSupport.Fromdate")%>"))
		if (compareDate(frm.fromDate.value,frm.toDate.value)>=0){
			frm.target="";
			frm.report.disabled=true;
			frm.submit();
		}
		else{
			alert("<%= languageChoose.getMessage("fi.jsp.tasksSupport.FromdatemustbebeforeorequaltoTodate")%>");
			frm.fromDate.focus();
		}
	}
	function report(){
		if (mandatoryDateFld(frm.toDate,"<%= languageChoose.getMessage("fi.jsp.tasksSupport.todate")%>"))
		if (dateFld(frm.fromDate,"<%= languageChoose.getMessage("fi.jsp.tasksSupport.Fromdate")%>"))
		if (compareDate(frm.fromDate.value,frm.toDate.value)>=0){
			frm.report.disabled=false;
			frm.target="about:blank";
			frm.submit();
		}
		else{
			alert("<%= languageChoose.getMessage("fi.jsp.tasksSupport.FromdatemustbebeforeorequaltoTodate")%>");
			frm.fromDate.focus();
		}
	}
	function chgGroup(){
		if (frm.group){
			var theGroup=frm.group.options[frm.group.selectedIndex].value;
			for (var i =frm.project.options.length-1;i>=0;i--)
				frm.project.options[i]=null;
			var j=1;
			frm.project.options[0]=new Option('all',0);	
			for (var i =0;i<prjGrpMap.length;i++){
					if ((theGroup==0||prjGrpMap[i][0]==theGroup)){
						frm.project.options[j++]=new Option(prjGrpMap[i][2], prjGrpMap[i][1],(prjGrpMap[i][1]==<%=projectID%>),(prjGrpMap[i][1]==<%=projectID%>));	
					}
			}
		}
	}
	function drill(vtid){
		window.open("Group/drillup.jsp?wuID=<%=workUnitID%>", "drillup","width = 100, height= 30");
		doIt('<%=Constants.GROUPMONITORINGDRILL%>&vtID='+vtid);
	}
	function drillOut(vtid){
		window.open('Fms1Servlet?reqType=<%=Constants.GROUPMONITORINGDRILL%>&vtID='+vtid,"NCMS");
	}
</SCRIPT>
</BODY>
</HTML>
