<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.*,java.util.Vector " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<META http-equiv="Content-Style-Type" content="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<TITLE>PCBReport.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
boolean isSQA=(workUnitID==Parameters.SQA_WU);

String error =request.getParameter("error");
Vector pcbList= (Vector)session.getAttribute("pcbList");
int nWorkUnitType = Integer.parseInt((String)session.getAttribute("workUnitType"));
String strMenuType;
int right;
if (nWorkUnitType==Constants.RIGHT_GROUP)
	right=Security.securiPage("Group PCB",request,response);
else
	right=Security.securiPage("Organization PCB",request,response);

%>
<BODY class="BD"  onload="<%=CommonTools.getMnuFunc(session)%>doOnChangeFilters();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.pcbReport.PCBreport")%> </P>
<BR>
<FORM name='frmPCB' action='Fms1Servlet#errBook' method='POST'>
<INPUT type="hidden" name="projectIDs">
<TABLE >
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.pcbReport.ExistingPCBreports")%> </CAPTION>
	<TBODY>
		<TR class="NormalText">
			<TD> <%=languageChoose.getMessage("fi.jsp.pcbReport.Year")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.pcbReport.Term")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.pcbReport.Reporttype")%> </TD>
		</TR>
		<TR>
			<TD>
				<SELECT name="txtYearGet" class="COMBO" onchange="doOnChangeFilters();">
				<OPTION value="all" selected> <%=languageChoose.getMessage("fi.jsp.pcbReport.All")%> </OPTION>
				<%
				int startYear = 2000;
				java.text.SimpleDateFormat  yearFrmt= new java.text.SimpleDateFormat("yyyy");
				int endYear = Integer.parseInt(yearFrmt.format(new java.util.Date()));
				
            	for (int year =endYear;year>=startYear;year--){
            	%><OPTION value=<%=year%>><%=year%></OPTION>
                <%}%>
				</SELECT>
			</TD>
			<TD> 
				<SELECT name="txtPeriodGet" class="COMBO" onchange="doOnChangeFilters();">
					<OPTION value ="all" selected> <%=languageChoose.getMessage("fi.jsp.pcbReport.All1")%> </OPTION>
					<OPTION value ="S1"> S1 </OPTION>
					<OPTION value ="S2"> S2 </OPTION>
					<%if (!isSQA){%>
				    <option value ="Q1"> Q1 </option>
				    <option value ="Q2"> Q2 </option>
				    <option value ="Q3"> Q3 </option>
				    <option value ="Q4"> Q3 </option>
				    <%}%>
				</SELECT>
			
			</TD>
			<TD>
				<SELECT name="txtTypeGet" class="COMBO"  onchange="doOnChangeFilters();" <%=(isSQA?"disabled":"")%> >
					<OPTION value="all" selected> <%=languageChoose.getMessage("fi.jsp.pcbReport.All2")%> </OPTION>
					<OPTION value=0 > <%=languageChoose.getMessage("fi.jsp.pcbReport.Development")%> </OPTION>
					<OPTION value=1> <%=languageChoose.getMessage("fi.jsp.pcbReport.Maintenance")%> </OPTION>
					<OPTION value=2> <%=languageChoose.getMessage("fi.jsp.pcbReport.Other")%> </OPTION>
					<OPTION value=7> <%=languageChoose.getMessage("fi.jsp.pcbReport.Custom")%> </OPTION>
					<OPTION value=6> <%=languageChoose.getMessage("fi.jsp.pcbReport.Allprojects")%> </OPTION>
				</SELECT>
			</TD>
			<TD>
			</TD>
			<TD>
			</TD>
		</TR>
	</TBODY>
</TABLE>
<TABLE >
	<TBODY>
		<TR>
			<TD rowspan="2">
			<SELECT size="5" name="txtOldReport" class="COMBO">
			</SELECT></TD>
			<TD></TD>
		</TR>
		<TR>
			<TD align="right">
				<INPUT	type="button" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.pcbReport.View")%> " class="BUTTON" onClick="viewReport()">
			</TD>
		</TR>
	</TBODY>
</TABLE>
<P></P>
<%if (right==3){%>
<%=((error==null)?"":"<A name ='errBook' class='ERROR'>"+ (languageChoose.getMessage("fi.jsp.pcbReport.error")) +"</P>")%>
<TABLE>
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.pcbReport.CreatePCBreport")%> </CAPTION>
		<TR class="NormalText">
			<TD> <%=languageChoose.getMessage("fi.jsp.pcbReport.Year1")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.pcbReport.Term1")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.pcbReport.Reporttype1")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.pcbReport.Reportname")%> </TD>
		</TR>
		<TR>
			<TD>
				<SELECT name="txtYear" class="COMBO" onchange="periodChange();">
				<%
            	for (int year =endYear;year>=startYear;year--){
            	%><OPTION value="<%=year%>" <%=(year==endYear)?"selected":""%>><%=year%></OPTION>
                <%}%>
				</SELECT>
			</TD>
			<TD> 
				<SELECT name="txtPeriod" class="COMBO" onChange="periodChange()" >
					<OPTION value ="S1" selected> S1 </OPTION>
					<OPTION value ="S2"> S2 </OPTION>
					<%if (!isSQA){%>
				    <option value ="Q1"> Q1</option>
				    <option value ="Q2"> Q2 </option>
				    <option value ="Q3"> Q3 </option>
				    <option value ="Q4"> Q4 </option>
				    <%}%>
				</SELECT>
			
			</TD>
			<TD>
				<SELECT name="txtType" class="COMBO" onChange="lifeCycleChange(this.selectedIndex)" <%=(isSQA?"disabled":"")%>>
					<OPTION value="all" selected> <%=languageChoose.getMessage("fi.jsp.pcbReport.All3")%> </OPTION>
					<OPTION value="dev"> <%=languageChoose.getMessage("fi.jsp.pcbReport.Development1")%> </OPTION>
					<OPTION value="mai"> <%=languageChoose.getMessage("fi.jsp.pcbReport.Maintenance1")%> </OPTION>
					<OPTION value="oth"> <%=languageChoose.getMessage("fi.jsp.pcbReport.Other1")%> </OPTION>
					<OPTION value="cus"> <%=languageChoose.getMessage("fi.jsp.pcbReport.Custom1")%> </OPTION>
				</SELECT>
			</TD>
			<TD><INPUT name="txtReportName" maxlength="30"></TD>
		</TR>
	</TBODY>
</TABLE>
<A name="customProjects"></A>
<TABLE width="95%">
	<TBODY>
		<TR>
			<TD class="NormalText" align="top">
			<%if (!isSQA){%>
				<FIELDSET>
					<INPUT type="checkbox" name="chkSoft" value="<%=MetricDescInfo.GR_SOFTWARE%>" checked onclick="checkSoft(this.checked,frmPCB.chkSoftware)"> <%=languageChoose.getMessage("fi.jsp.pcbReport.Software")%> <BR>&nbsp;&nbsp;&nbsp;
					<INPUT type="checkbox" name="chkSoftware" value="<%=MetricDescInfo.GR_REQUIREMENT%>" checked> <%=languageChoose.getMessage("fi.jsp.pcbReport.Requirement")%> <BR>&nbsp;&nbsp;&nbsp;
					<INPUT type="checkbox" name="chkSoftware" value="<%=MetricDescInfo.GR_SCHEDULE%>" checked> <%=languageChoose.getMessage("fi.jsp.pcbReport.Schedule")%> <BR>&nbsp;&nbsp;&nbsp;
					<INPUT type="checkbox" name="chkSoftware" value="<%=MetricDescInfo.GR_EFFORT%>" checked> <%=languageChoose.getMessage("fi.jsp.pcbReport.Effort")%> <BR>&nbsp;&nbsp;&nbsp;
					<INPUT type="checkbox" name="chkSoftware" value="<%=MetricDescInfo.GR_PRODUCT_QUALITY%>" checked> <%=languageChoose.getMessage("fi.jsp.pcbReport.Productquality")%> <BR>&nbsp;&nbsp;&nbsp;
					<INPUT type="checkbox" name="chkSoftware" value="<%=MetricDescInfo.GR_PRODUCTIVITY%>" checked> <%=languageChoose.getMessage("fi.jsp.pcbReport.Productivity")%> <BR>&nbsp;&nbsp;&nbsp;
					<INPUT type="checkbox" name="chkSoftware" value="<%=MetricDescInfo.GR_PRODUCT_SIZE%>" checked> <%=languageChoose.getMessage("fi.jsp.pcbReport.Productsize")%> <BR>&nbsp;&nbsp;&nbsp;
				</FIELDSET>
				<%}else{%>
				<FIELDSET>
					<INPUT type="checkbox" name="chkSQA" value="<%=MetricDescInfo.GR_SQA%>" checked onclick="checkSoft(this.checked,frmPCB.chkSQAGroup)"> <%=languageChoose.getMessage("fi.jsp.pcbReport.SQA")%> <BR>&nbsp;&nbsp;&nbsp;
					<INPUT type="checkbox" name="chkSQAGroup" value="<%=MetricDescInfo.GR_DP%>" checked> <%=languageChoose.getMessage("fi.jsp.pcbReport.Defectprevention")%> <BR>&nbsp;&nbsp;&nbsp;
				</FIELDSET>
				<%}%>
				
				
<!--
				<FIELDSET><INPUT type="checkbox" name="chkHuman" value="Human"
					checked>Human capital management<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT
					type="checkbox" name="chkRecruitement" value="Recruitement" checked>Recruitement<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT
					type="checkbox" name="chkTraining" value="Training" checked>Training<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT
					type="checkbox" name="chkHRM" value="HRM">HRM<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT
					type="checkbox" name="chkCollaboratorMan" value="CollaboratorMan"checked>Collaborator management<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT
					type="checkbox" name="chkInternMan" value="InternMan" checked>Collaborator management
				</FIELDSET>
				<BR>
			</TD>
			<TD align="top">
				<FIELDSET><INPUT type="checkbox" name="chkProcessManagement"
					value="ProcessManagement" checked>Process management<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT
					type="checkbox" name="chkProcessCompliance"
					value="ProcessCompliance" checked>Process compliance<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT
					type="checkbox" name="chkProcessService" value="ProcessService"checked>Process service<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT
					type="checkbox" name="chkProcessEffectiveness"
					value="ProcessEffectiveness" checked>Process effectiveness
				</FIELDSET>


				
			<INPUT type="checkbox" name="chkCustomerMan"
				value="CustomerMan" checked>Customer management<BR>
			<INPUT type="checkbox" name="chkISMan" value="ISMan" checked>IS mangagement<BR>
			<INPUT type="checkbox" name="chkTechMan" value="TechMan" checked>Technology management<BR>
			<INPUT type="checkbox" name="chkAdmin" value="Admin" checked>Admin
			
				-->
			</TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<BR>
<INPUT	type="button" name="btnCreate" value=" <%=languageChoose.getMessage("fi.jsp.pcbReport.Create")%> " class="BUTTON" onClick="createPCB();">
<P></P>
<%}%>
</BODY>
</HTML>
<SCRIPT language="javascript">
//objs to hide when submenu is displayed
var objToHide=new Array(frmPCB.txtYearGet,frmPCB.txtPeriodGet,frmPCB.txtTypeGet,frmPCB.txtOldReport <%=((right==3)?",frmPCB.txtYear,frmPCB.txtPeriod,frmPCB.txtType":"")%>);

<%

	StringBuffer yearBuf = new StringBuffer();
	StringBuffer periodsBuf = new StringBuffer();
	StringBuffer rTypesBuf = new StringBuffer();
	StringBuffer namesBuf = new StringBuffer();
	if  (pcbList.size()>0){
		PCBGalInfo pcbInfo = (PCBGalInfo)pcbList.elementAt(0);
		yearBuf.append(pcbInfo.year);
		periodsBuf.append(pcbInfo.period);
		rTypesBuf.append(pcbInfo.reportType);
		namesBuf.append((pcbInfo.reportName==null)?"":pcbInfo.reportName);
		for (int i = 1; i < pcbList.size(); i++){
			pcbInfo = (PCBGalInfo)pcbList.elementAt(i);
			yearBuf.append(",").append(pcbInfo.year);
			periodsBuf.append("','").append(pcbInfo.period);
			rTypesBuf.append(",").append(pcbInfo.reportType);
			namesBuf.append("','").append((pcbInfo.reportName==null)?"":pcbInfo.reportName);
		}
		
	}
	//one value is added to the array to force javascript to use it as array
%>
var arrayLength=<%=pcbList.size()%>
var years=new Array(<%=((pcbList.size()==0)?"0":yearBuf.toString())%>,0);
var periods= new Array('<%=periodsBuf.toString()%>','');
var rTypes= new Array(<%=((pcbList.size()==0)?"0":rTypesBuf.toString())%>,0);
var names=new Array('<%=namesBuf.toString()%>','');

var rv;
function createPCB(){
	alert("This function have been disabled");
	return;	
	if (rv && rv[0]){
		var prjIDS =new Array();
		for (var i=0;i<rv.length;i++)
			prjIDS[i]=rv[i][0];
		frmPCB.projectIDs.value=prjIDS;
	}
	if (trim(frmPCB.txtReportName.value)=="")
		if (!confirm("<%= languageChoose.getMessage("fi.jsp.pcbReport.DoYouReallyWantToLeaveTheReportNameBlank")%>")){
			frmPCB.txtReportName.focus();
			return;
		}
	if (!oneChecked(frmPCB.chkSoftware)&&!oneChecked(frmPCB.chkSQAGroup)){
 		alert("<%= languageChoose.getMessage("fi.jsp.pcbReport.PleaseSelectAtLeastOneMetricGroup")%>");  			
		if (frmPCB.checkSoft)
			frmPCB.chkSoft.focus();
		else if (frmPCB.chkSSQA)
			frmPCB.chkSQA.focus();
		return;
	}
	frmPCB.action='Fms1Servlet?reqType=<%=Constants.PCB_CREATE%>#errBook';
	frmPCB.submit();
}
function doPopup()
{
	rv = window.showModalDialog("Fms1Servlet?reqType=<%=Constants.PCB_SELECT_PROJECT%>"+"&year="+frmPCB.txtYear.options[frmPCB.txtYear.selectedIndex].value+"&period="+frmPCB.txtPeriod.options[frmPCB.txtPeriod.selectedIndex].value, null,"dialogWidth: 400px; dialogHeight: 300px");
	if (rv && rv[0]){
		var strCustoProject="<TABLE><TR class='NormalText'><TD><B><%= languageChoose.getMessage("fi.jsp.pcbReport.Customprojects") %> </B></TD><TD></TD>"
		for (var i=0;i<rv.length;i++)
			strCustoProject += "<TR class='NormalText'><TD>"+rv[i][1]+" </TD><TD>"+rv[i][2]+"</TD></TR>";
		document.all["customProjects"].innerHTML=strCustoProject+"</TABLE><BR>";
	}
	else
		frmPCB.txtType.selectedIndex=0;		
} 
function lifeCycleChange(index){
	if (frmPCB.txtType.options[index].value =="cus")
		doPopup();
	else{
		document.all["customProjects"].innerHTML="";
		rv=null;
	}
}
function periodChange(){
	if (frmPCB.txtType.options[frmPCB.txtType.selectedIndex].value =="cus"){
		document.all["customProjects"].innerHTML="";
		rv=null;
		frmPCB.txtType.selectedIndex=0;	
	}

}
function checkSoft(checked,toUpdate){
	toUpdate.checked=checked;//in case only one item
	for (var i =0;i<toUpdate.length;i++)
		toUpdate[i].checked=checked;
}
function oneChecked(obj){
	if (!obj)
		return false;
	if (obj.checked)
		return true;
	for (var i =0;i<obj.length;i++)
		if (obj[i].checked)
			return true;
	return false;
}
function doOnChangeFilters(){
	if (arrayLength>0){
		var year =frmPCB.txtYearGet.options[frmPCB.txtYearGet.selectedIndex].value;
		var period =frmPCB.txtPeriodGet.options[frmPCB.txtPeriodGet.selectedIndex].value;
		var rType =frmPCB.txtTypeGet.options[frmPCB.txtTypeGet.selectedIndex].value;
		for (var i =frmPCB.txtOldReport.options.length-1;i>=0;i--)
			frmPCB.txtOldReport.options[i]=null;
		var j=0;
		var spacer="   ";
		for (var i =0;i<arrayLength;i++){
			if ((year=="all"||year==years[i])&&(period=="all"||period==periods[i])&&(rType=="all"||rType==rTypes[i]))
			frmPCB.txtOldReport.options[j++]=new Option(" "+years[i]+"      "+periods[i]+"        "+getReportType(rTypes[i])+"      "+names[i], i);	
		
		}
	}
//	if (arrayLength <=0){
//		window.alert("arrayLength<=0");
//	}
}
var arrNames=new Array();
arrNames[0]="Development";
arrNames[1]="Maintenance ";
arrNames[2]="Other      ";
arrNames[6]="All               ";
arrNames[7]="Custom        ";
function getReportType(numType){
return arrNames[numType];
}
function viewReport(){
	alert("This function have been disabled");
	return;
	if ((arrayLength<=0) ||frmPCB.txtOldReport.selectedIndex<0){
 		alert("<%= languageChoose.getMessage("fi.jsp.pcbReport.PleaseSelectAReportFirst")%>");  			
		frmPCB.txtOldReport.focus();
		return;
	}
	frmPCB.action='Fms1Servlet?reqType=<%=Constants.PCB_VIEW%>&vtID='+frmPCB.txtOldReport.options[frmPCB.txtOldReport.selectedIndex].value;
	frmPCB.submit();
}

</SCRIPT>
