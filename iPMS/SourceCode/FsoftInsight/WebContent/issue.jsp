<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.tools.*, com.fms1.web.* ,com.fms1.html.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>issue.jsp</TITLE>
<STYLE type="text/css">
.header {
    FONT-WEIGHT: bold;
    BACKGROUND-COLOR: #C0C0C0;
}
.footer {
    FONT-WEIGHT: bold;
}
.Title {
	font-weight: bold;
	font-size: 14pt;
	margin-left: 0px;
	margin-top: 20px
}
</STYLE>

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

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
	int nWorkUnitType = Integer.parseInt((String)session.getAttribute("workUnitType"));
	boolean report=(request.getParameter("report")!=null);
	if (report) {
			response.setContentType(Fms1Servlet.CONTENT_TYPE_EXCEL);	
			response.addHeader("Content-Disposition", "attachment;filename=issue.xls");	
	}

	String strRight;
	if (nWorkUnitType==Constants.RIGHT_ORGANIZATION)
		strRight="Organization issues";
	else if (nWorkUnitType==Constants.RIGHT_GROUP)
		strRight="Group issues";
	else
		strRight="Project issues";
	int right = Security.securiPage(strRight,request,response);
	Vector issueList1 = (Vector)session.getAttribute("issueList");
	Vector userList = (Vector)session.getAttribute("issueUserList");
	String fromDateStr=request.getParameter("fromDate");
	String toDateStr=request.getParameter("toDate");
	String keyword =request.getParameter("keyword");
	Date fromDate=CommonTools.parseDate(fromDateStr);
	Date toDate=CommonTools.parseDate(toDateStr);
	//PQA specific------------
	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	WUCombo wuCombo=null;
	if(workUnitID==Parameters.PQA_WU)
		wuCombo=new WUCombo(request,WUCombo.MODE_FILTER);

	//------------------------
	String tableTitle="";
	
	if (fromDate!=null)
		tableTitle="From "+"~PARAM1_DATE~";
	if (toDate!=null)
		tableTitle=tableTitle+ ((tableTitle.length()==0)?"Up to ":" to ")+"~PARAM2_DATE~";
	if (keyword!=null && keyword.length()>0)
		tableTitle=tableTitle+ ((tableTitle.length()==0)?"Keyword '":", keyword '")+"~PARAM3_KEY~'" ;
	
	Vector issueList = new Vector();
	int cboAss=0;
	String cboAssStr=request.getParameter("cboAss");
	if (cboAssStr!=null)
		cboAss=Integer.parseInt(cboAssStr);
	UserInfo info=null;
	String account = "";

	for(int i=0;i<userList.size();i++){
		info=(UserInfo)userList.elementAt(i);
		if (info.developerID== cboAss){
			tableTitle=((tableTitle.length()>0)?tableTitle+", owned by ":"Owned by ")+"~PARAM4_NAME~";
			account = info.account;
			break;
		}
	}
    //System.out.println("check: " + tableTitle);
	IssueInfo issueInfo;
	for (int i = 0; i < issueList1.size(); i++) {
    	issueInfo = (IssueInfo)issueList1.elementAt(i);
    	issueInfo.vectorID=i;
		if ((fromDate == null || issueInfo.dueDate.compareTo(fromDate)>=0)
            &&(toDate == null || issueInfo.dueDate.compareTo(toDate)<=0)
            &&(cboAss == 0 || issueInfo.owner.equals(info.account))
            && (wuCombo==null||wuCombo.selected==-1||wuCombo.selected==issueInfo.wuID||wuCombo.selected==issueInfo.parentwuID)
            &&	(	(keyword==null || keyword.length()==0)
            	||issueInfo.description.indexOf(keyword)!=-1
            	||(issueInfo.comment!=null && issueInfo.comment.indexOf(keyword)!=-1)
            	||(issueInfo.reference!=null && issueInfo.reference.indexOf(keyword)!=-1)
            	)
        	){
            issueList.add(issueInfo);
        }
    }
    int lineNumber=20;
	int issueCount = issueList.size();
	int npage=issueCount/lineNumber;
	if (issueCount%lineNumber!=0)npage++;
	String book=request.getParameter("book");
	int bookmark = (book==null)?1:Integer.parseInt(book);
	int startIssue=(bookmark-1)*lineNumber;
	int endIssue=(bookmark==npage ||npage==0)?issueCount:startIssue+20;
	if (endIssue>issueCount)endIssue=issueCount;
	String strMenuType=CommonTools.getMnuFunc(session);
	//if (issueList.size() > 15)
		//strMenuType +="makeScrollableTable('tableIssue',true,'350')";
if (report){
	startIssue=0;
	endIssue=issueCount;
	
%>
	<style TYPE="text/css"> 
	<!-- 
	<%@ include file="/stylesheet/fms.css" %>	
	--> 
	</style> 
<%}else{%>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<%}%>
</HEAD>
<BODY onload="<%=strMenuType%>;javascript:Import()" class="BD">


<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.issue.Issues")%></P>
<%if (!report){%>
<P align=right><A href="javascript:report()"><%=languageChoose.getMessage("fi.jsp.issue.Export")%>&nbsp;&nbsp;</A></P>
<br>
<!--
<TABLE cellspacing="1" class="HDR" width="100%">
	<TBODY>
		<TR>
			<TD width="10%"></TD>
			<TD></TD>
			<TD align="right"><P align=right><%if(right == 3 && !isArchive){%><a href="javascript:Import();"><%=languageChoose.getMessage("fi.jsp.issue.ImportIssue	")%></a><%}%></p></TD>
		</TR>
	</TBODY>
</TABLE>
-->
<br>
<FORM name="frm_Import" action="Fms1Servlet?reqType=<%=Constants.ISSUE_IMPORT%>"  enctype="MULTIPART/FORM-DATA" method=POST >
<TABLE cellspacing="1" class="HDR" width="95%" id="ImportTable">
	<TBODY>
		<TR>
			<TD align="right"><STRONG>File Name&nbsp;*</STRONG></TD>
	        <TD align="left">
	        	<INPUT type="file" name="importFile">
	       		<INPUT type="button" name="Import" onclick="checkName()" value=" Import " class="Button">
            	<INPUT type="hidden" name="filename">
	        </TD>
	        <TD></TD>
	        </TR>
	        <TR>
	        	<TD></TD>
	        	<TD align="left"><A href="Template_Import_ISSUES.xls"><FONT color="blue" class="label1">Download Template File</FONT></A></TD>
	        </TR>
	</TBODY>
</TABLE>
</FORM>

<BR>
<%
String checkImport = (String)session.getAttribute("Imported");
if(checkImport == "true"){
int[] result = (int[])session.getAttribute("AddedRecord");
%><br>Imported successfull records:<%
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

<FORM action="issue.jsp" name="frm" method="GET">
<INPUT type="hidden" name="report" value="1" disabled>
<TABLE >
	<TBODY>
		<TR class="NormalText">
			<TD><%=languageChoose.getMessage("fi.jsp.issue.Fromdate")%></TD>
			<TD NOWRAP><INPUT size="9" type="text" maxlength="9" name="fromDate" value="<%=(fromDateStr==null ? "":fromDateStr)%>">
	            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showFromDate()'>				
				(DD-MMM-YY)
			</TD>
			<TD>&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.issue.Owner")%></TD>
			<TD>
				<SELECT name="cboAss">
					<OPTION value="0" <%=(0== cboAss ? " selected" : "")%> ><%=languageChoose.getMessage("fi.jsp.issue.all")%></OPTION>
					<%
					for(int i=0;i<userList.size();i++){
						info=(UserInfo)userList.elementAt(i);
						%><OPTION value="<%=info.developerID+"\""+(info.developerID== cboAss ? " selected" : "")+">"+info.account%></OPTION>
					<%}%>
				</SELECT>
			</TD>
			<TD>
			<%if(wuCombo==null){%>
				</TD>
				<TD>
			<%}else{%>
				<%=languageChoose.getMessage(wuCombo.label)%>
				</TD>
				<TD>
				<%=wuCombo.html%>
			<%}%>
			</TD>
		</TR>
		<TR class="NormalText">
			<TD><%=languageChoose.getMessage("fi.jsp.issue.Todate")%></TD>
			<TD><INPUT size="9" type="text" maxlength="9" name="toDate" value="<%=(toDateStr==null ? "":toDateStr)%>">
	            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showToDate()'>			
				(DD-MMM-YY)
			</TD>
			<TD>&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.issue.Keyword")%></TD>
			<TD><INPUT size="9" type="text" maxlength="15" name="keyword" value="<%=(keyword==null ? "":keyword)%>"></TD>
			<TD colspan =2>
				<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.issue.Go")%>" onclick="dofilter()">
			</TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<%}%>
<TABLE class="Table" width="95%" cellspacing="1" id="tableIssue">


<CAPTION align="left" class="<%= (report)?"ColumnLabel":"TableCaption"%>"><%=(tableTitle =="")?"":languageChoose.paramText(new String[]{"fi.jsp.issue."+tableTitle,fromDateStr,toDateStr,keyword,account})%></CAPTION>
		<TR class="ColumnLabel">
		<%if(wuCombo!=null){%>
			<TD><%=languageChoose.getMessage(wuCombo.label)%></TD>
		<%}%>
			<TD  width="40%"><%=languageChoose.getMessage("fi.jsp.issue.Description")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.issue.Status")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.issue.Priority")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.issue.Owner")%></TD>
			<TD NOWRAP><%=languageChoose.getMessage("fi.jsp.issue.DueDate")%></TD>
			</TR>
<%
String className ;
for (int i = startIssue; i < endIssue; i++) {
    issueInfo = (IssueInfo)issueList.elementAt(i);
    className = (i%2 == 0)? "CellBGRnews":"CellBGR3";
	%>	<TR class=<%=(report ?"header ":className)%>>
			<%if(wuCombo!=null){%>
				<TD><%=issueInfo.wuName%></TD>
			<%}%>
			<TD><%if (!report){%><A href="issueDetail.jsp?vtID=<%=issueInfo.vectorID%>"><%}%>
			<%=ConvertString.toHtml(issueInfo.description)%></A></TD>
			<TD><%=languageChoose.getMessage(issueInfo.getStatusName())%></TD>
			<TD><%=languageChoose.getMessage(issueInfo.getPriorityName())%></TD>
			<TD><%=issueInfo.owner%></TD>
			<TD NOWRAP><%=CommonTools.dateFormat(issueInfo.dueDate)%></TD>
		</TR>
<%}%>

<%if (!report){%>

		<TR align="right">
			<TD colspan="6" class="TableLeft"><%=languageChoose.getMessage("fi.jsp.issue.Page")+"  "+bookmark+"/"+npage%> 
			<%
			String queryString=request.getQueryString();
			if (queryString==null)
				queryString="";
			String query=ConvertString.removeFromQueryString(queryString,"book");
				
			if (bookmark > 1) {
            %> <A href="issue.jsp?book=<%=(bookmark-1)+query%>"><%=languageChoose.getMessage("fi.jsp.issue.Prev")%></A>
            <%}
			if (bookmark<npage) {
			%> <A href="issue.jsp?book=<%=(bookmark+1)+query%>"><%=languageChoose.getMessage("fi.jsp.issue.Next")%></A>
			<%}%>
			</TD>
		</TR>

	<%}%>
</TABLE>
<P>
<%if(right == 3 && !isArchive){%>
<INPUT type="button"  value="<%=languageChoose.getMessage("fi.jsp.issue.Addnew")%>" class="BUTTON" onClick="doIt(<%=Constants.ISSUE_ADDPREP%>)">
<%}%>

<SCRIPT language="JavaScript">
	function showFromDate(){
		showCalendar(frm.fromDate, frm.fromDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showToDate(){
		showCalendar(frm.toDate, frm.toDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	//objs to hide when submenu is displayed
	var objToHide=new Array(frm.fromDate,frm.toDate,frm.cboAss);

	function dofilter(){
		frm.report.disabled=true;
		frm.target="";
		common();
	}
	function report(){
		frm.report.disabled=false;
		frm.target="about:blank";
		common();
	}
	function common(){
		if (dateFld(frm.fromDate,"<%=languageChoose.getMessage("fi.jsp.issue.Fromdate")%>"))
		if (dateFld(frm.toDate,"<%=languageChoose.getMessage("fi.jsp.issue.td")%>"))
		if (frm.fromDate.value.length==0 || frm.toDate.value.length==0 || compareDate(frm.fromDate.value,frm.toDate.value)>=0){
			frm.submit();
		}
		else{
			alert("<%= languageChoose.getMessage("fi.jsp.issue.FromdatemustbebeforeorequaltoTodate")%>");
			frm.fromDate.focus();
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
</SCRIPT>

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
</BODY>
</HTML>