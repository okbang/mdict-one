<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.Vector,java.util.Calendar,com.fms1.tools.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>

<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="JavaScript">
        <%@ include file="../javaFns.jsp"%>
</SCRIPT>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	int right=Security.securiPage("Organization monitoring",request,response);
	int reqType =Integer.parseInt(request.getParameter("reqType"));
	if (reqType==Constants.PLAN_ADD||reqType==Constants.PLAN_UPDATE||reqType==Constants.PLAN_DELETE)
		reqType=Constants.PLAN;
	else if (reqType!=Constants.PLAN)
		reqType=Constants.DECISION;
	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	Vector tasks=(Vector)session.getAttribute("tasks");
	String fromDateStr=request.getParameter("fromDate");
	
	String toDateStr=request.getParameter("toDate");
	String tableTitle="";
	
	if (fromDateStr==null)
		fromDateStr="";
	else if (fromDateStr.length()>0)
		tableTitle=languageChoose.paramText(new String[]{"fi.jsp.decision.From__~PARAM1_FROM_DATE~",fromDateStr});//fromDateStr;
	if (toDateStr==null)
		toDateStr="";
	else if (toDateStr.length()>0)
		tableTitle=tableTitle+ " " + ((fromDateStr.length()==0)? languageChoose.paramText(new String[]{"fi.jsp.decision.Up__to__~PARAM1_TO_DATE~",toDateStr}):languageChoose.paramText(new String[]{"fi.jsp.decision.to__~PARAM1_TO_DATE~",toDateStr}));//toDateStr;
	String assign=request.getParameter("cboAss");
	int cboAss=(assign==null)?0:Integer.parseInt(assign);
	String assignedTo=null;
	Vector userList=(Vector)session.getAttribute("userstasks");

%>
<TITLE>descision.jsp</TITLE>
</HEAD>
<BODY  class="BD" onload="loadOrgMenu();">
<P class="TITLE"><%=((reqType==Constants.DECISION)? languageChoose.getMessage("fi.jsp.decision.ManagementDecisions"):languageChoose.getMessage("fi.jsp.decision.Plans"))%></P>
<br>
<FORM action="Fms1Servlet?reqType=<%=reqType%>" name="frm" method="post">
<TABLE >
	<TBODY>
		<TR class="NormalText">
			<TD> <%=languageChoose.getMessage("fi.jsp.decision.Fromdate")%> </TD>
			<TD NOWRAP><INPUT size="9" type="text" maxlength="9" name="fromDate" value="<%=fromDateStr%>"> (DD-MMM-YY) </TD>
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.decision.Assignedto")%> </TD>
			<TD>
				<SELECT name="cboAss">
					<OPTION value="0" <%=(0== cboAss ? " selected" : "")%> > <%=languageChoose.getMessage("fi.jsp.decision.all")%> </OPTION>
					<%UserInfo info =null;
					for(int i=0;i<userList.size();i++){
						info=(UserInfo)userList.elementAt(i);
						if (info.developerID== cboAss)
						    tableTitle=((tableTitle.length()>0)?tableTitle + " , "+ languageChoose.paramText(new String[]{"fi.jsp.decision.assigned__to~PARAM1_NAME~",info.Name}):languageChoose.paramText(new String[]{"fi.jsp.decision.Assigned__to~PARAM1_NAME~",info.Name}));//info.Name;
						%><OPTION value="<%=info.developerID+"\""+(info.developerID== cboAss ? " selected" : "")+">"+info.account%></OPTION>
					<%}%>
				</SELECT>
			</TD>
			<TD colspan =4>
				<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.decision.Go")%> " onclick="dofilter()">
			</TD>
		</TR>
		<TR class="NormalText">
			<TD> <%=languageChoose.getMessage("fi.jsp.decision.Todate")%> </TD>
			<TD><INPUT size="9" type="text" maxlength="9" name="toDate" value="<%=toDateStr%>"> (DD-MMM-YY) </TD>

		</TR>
	</TBODY>
</TABLE>
</FORM>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><%=tableTitle%></CAPTION>
    <TBODY >
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.decision.Code")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.decision.Description")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.decision.Planneddate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.decision.Actualdate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.decision.Assignedto1")%> </TD> 
            <%if (reqType==Constants.DECISION){%>
            <TD> <%=languageChoose.getMessage("fi.jsp.decision.Feasible")%> </TD>
            <%}%>
            <TD> <%=languageChoose.getMessage("fi.jsp.decision.Process")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.decision.Note")%> </TD>
        </TR>
        <%
        TaskInfo taskInfo;
        String style;
        for (int i =0;i<tasks.size();i++){
        	taskInfo=(TaskInfo)tasks.elementAt(i);
        	if (0== cboAss||taskInfo.assignedTo==cboAss){
        		style=(i%2 == 0)? "CellBGRnews":"CellBGR3";
         %><TR class="<%=style%>">
            <TD><%=taskInfo.code%></TD>
            
            <TD>
            <%if (right==3) {%>
            	<A HREF="javascript:clickMe(<%=i%>)">
            <%}%>
            <%=taskInfo.desc%>
            </A>
            </TD>
            <TD NOWRAP><%=CommonTools.dateFormat((taskInfo.rePlanDate==null)?taskInfo.planDate:taskInfo.rePlanDate)%></TD>
            <TD NOWRAP><%=CommonTools.dateFormat(taskInfo.actualDate)%></TD>
            <TD><%=((taskInfo.assignedToStr==null)?"N/A":taskInfo.assignedToStr)%></TD>
            <%if (reqType==Constants.DECISION){%>
            <TD><%
            	if (taskInfo.feasible) {
            		%><p align = "center"><input type = "checkbox" checked></p><%
            	}	
            }%></TD>
            <TD><%=languageChoose.getMessage(ProcessInfo.getProcessName(taskInfo.processID))%></TD>
            <TD><%=CommonTools.formatString(taskInfo.note)%></TD>
        </TR>
          	<%}
          }%>
</TBODY > </TABLE>
<p>
<%if (right==3) {%>
<INPUT type= "BUTTON" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.decision.Addnew")%>" onclick="doIt('<%=Constants.GET_PAGE%>&page=Group/decisionAdd.jsp&type=<%=reqType%>')">
<%}%>
<p>
</BODY>
<SCRIPT language="JavaScript">
//objs to hide when submenu is displayed
var objToHide=new Array(frm.fromDate,frm.toDate,frm.cboAss);
	function dofilter(){
		if (dateFld(frm.fromDate,"<%= languageChoose.getMessage("fi.jsp.decision.Fromdate")%>"))
		if (dateFld(frm.toDate,"<%= languageChoose.getMessage("fi.jsp.decision.Todate")%>"))
		
		if (frm.fromDate.value.length==0 || frm.toDate.value.length==0 || compareDate(frm.fromDate.value,frm.toDate.value)>=0){
			if (!frm.fromDate.value !='<%=fromDateStr%>'
				||frm.toDate.value !='<%=toDateStr%>'){
				frm.submit();
			}
			else{
				frm.action= "<%=Constants.GET_PAGE%>&page=Group/decision.jsp";
				frm.submit();
			}
		}
		else{
			alert("<%=languageChoose.getMessage("fi.jsp.decision.FromDateMustBeBeforeOrEqualToToDate")%>");
			frm.fromDate.focus();
		}
	}
	function clickMe(vtID){
	doIt('<%=Constants.GET_PAGE%>&page=Group/decisionUpdate.jsp&vtID='+vtID);
	}
</SCRIPT>
</HTML>
