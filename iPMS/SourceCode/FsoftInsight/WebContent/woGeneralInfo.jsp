<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<SCRIPT language="javascript">
var test = true;
<%@ include file="jscript/ajax-dynamic-list.js"%>
</SCRIPT>
<TITLE>woGeneralInfo.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	
	String message1 = (String) session.getAttribute("messageCustomer1");
	String message2 = (String) session.getAttribute("messageCustomer2");
	session.setAttribute("messageCustomer1","true");
	session.setAttribute("messageCustomer2","true");
%>
<BODY class="BD" onload="loadPrjMenu();alertMessage('<%=message1%>','<%=message2%>')">
<%
ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("WOGeneralInfo");
Vector devList = (Vector) session.getAttribute("WODevList");
Vector groupList = (Vector) session.getAttribute("WOGroupList");

Vector appTypeList = (Vector) session.getAttribute("ApplicationTypeList");
Vector bizDomainList = (Vector) session.getAttribute("BizDomainList");
Vector contractTypeList = (Vector) session.getAttribute("ContractTypeList");
String strCustomer = "";
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.WorkorderInformation")%> </P>
<DIV align="left">
<br>
<form name="frm_woSMUpdate" action="Fms1Servlet" method="post">
<input type = "hidden" name="reqType" value="<%=Constants.WO_GENERAL_INFO_UPDATE%>">
<DIV align="left">

<p class="TableCaption"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.GeneralInformation")%></p>
<TABLE cellspacing="1" class="Table" width ="95%">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ProjectName")%>*</TD>
            <TD class="CellBGR3">
            	<input type="text" name="gi_prj_name" value="<%=ConvertString.toHtml(projectInfo.getProjectName())%>" maxlength="50" size="50">
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ProjectCode")%>*</TD>
            <TD class="CellBGR3">
	            <input type="text" name="gi_prj_code" value="<%=projectInfo.getProjectCode()%>" maxlength="50" size="50">
            </TD>
        </TR>        
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ContractType")%>*</TD>
            <TD class="CellBGR3">
            <SELECT name="gi_contract" class="COMBO" style='width:125'>
            <%
         		for (int k = 0; k < contractTypeList.size(); k++) {
        			ContractTypeInfo info = (ContractTypeInfo)contractTypeList.elementAt(k);
         	%>
         		<OPTION value="<%=info.contracttypeID%>" <%if(projectInfo.getContractType().equals(info.contracttypeName)||projectInfo.getContractType().equals(String.valueOf(info.contracttypeID) )){%>selected<%}%>><%=info.contracttypeName%></OPTION>
         	<%
         		}
         	%>
            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Customer")%>*</TD>
            <TD class="CellBGR3">
            	<INPUT name="gi_cst_name" size="30" type="text" value="<%=projectInfo.getCustomer()%>"/>
            	
            	<%
            		if (projectInfo.getTypeCustomer()==0) { 
            	%>
            	<INPUT type="button" class="BUTTON" name="search_Customer" value="<%=languageChoose.getMessage("fi.jsp.woSignature.Search")%>" onclick="onCheckAccount(this, event)"> 
            	<INPUT type="radio" name="customerType1" value="External" checked onclick="test(1);"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.External")%>
	            <INPUT type="radio" name="customerType1" value="Internal" onclick="test(2);"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Internal")%>
            	
 				<% }else {
 				%>
 				<INPUT type="button" style="display: none;" class="BUTTON" name="search_Customer" value="<%=languageChoose.getMessage("fi.jsp.woSignature.Search")%>" onclick="onCheckAccount(this, event)"> 
 				<INPUT type="radio" name="customerType1" value="External" onclick="test(1);" > <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.External")%>
	            <INPUT type="radio" name="customerType1" value="Internal" checked onclick="test(2);"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Internal")%>
 				
 				<%
 				 	}
 				%>
 				<div id="customerType">	
 					Customer Type &nbsp
	            	<SELECT name="customerType" class="COMBO" style='width:125'>
						<OPTION value="External"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.External")%></OPTION>
						<OPTION value="Internal"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Internal")%></OPTION>
					</SELECT>
				</div>
            	<BR>
	            <INPUT type="radio" name="rdCustomerName" value="StandardName" checked> <A name="A1"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.StandardName")%> </A>
	            <INPUT type="radio" name="rdCustomerName" value="FullName"><A name="A1"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.FullName")%></A><BR>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.2ndCustomer")%>*</TD>
            <TD class="CellBGR3">
            	<INPUT name="gi_cst_name_2nd" size="30" type="text" value="<%=projectInfo.getSecondCustomer()%>"/>
            	
            	<%
            		if (projectInfo.getTypeCustomer2()==0) { 
            	%>
            	<INPUT type="button" class="BUTTON" name="search_Customer2nd" value="<%=languageChoose.getMessage("fi.jsp.woSignature.Search")%>" onclick="onCheckAccount(this, event)"> 
            	<INPUT type="radio" name="customer2ndType1" value="External" checked onclick="test1(1);" > <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.External")%>
	            <INPUT type="radio" name="customer2ndType1" value="Internal" onclick="test1(2);" ><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Internal")%>
            	
 				<% }else {
 				%>
 				<INPUT type="button" style="display: none" class="BUTTON" name="search_Customer2nd" value="<%=languageChoose.getMessage("fi.jsp.woSignature.Search")%>" onclick="onCheckAccount(this, event)"> 
 				<INPUT type="radio" name="customer2ndType1" value="External" onclick="test1(1);"  > <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.External")%>
	            <INPUT type="radio" name="customer2ndType1" value="Internal" checked onclick="test1(2);" ><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Internal")%>
 				
 				<%
 				 	}
 				%>
 				<div id="Customer2ndType">
	            	Customer Type &nbsp
	            	<SELECT name="customer2ndType" class="COMBO" style='width:125'>
						<OPTION value="External"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.External")%></OPTION>
						<OPTION value="Internal"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Internal")%></OPTION>
					</SELECT>
				</div>
            	<BR>
	            <INPUT type="radio" name="rdCustomerName2nd" value="StandardName" checked><A name="A2"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.StandardName")%></A>
	            <INPUT type="radio" name="rdCustomerName2nd" value="FullName"><A name="A2"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.FullName")%></A><BR>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ProjectLevel")%>*</TD>
            <TD class="CellBGR3">
            <SELECT name="gi_prj_level" class="COMBO" style='width:125'>
				<OPTION value="Group" <%=(("Group".equals((String)projectInfo.getProjectLevel()))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Group")%> </OPTION>
				<OPTION value="Branch" <%=(("Branch".equals((String)projectInfo.getProjectLevel()))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Branch")%> </OPTION>
				<OPTION value="Company" <%=(("Company".equals((String)projectInfo.getProjectLevel()))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Company")%> </OPTION>				
			</SELECT>
        </TR>
        <TR>
        	<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ProjectRank")%> </TD>
        	<TD class="CellBGR3">
        	<SELECT name="gi_grp_rank" class="COMBO" style='width:125'>
				<OPTION value="" <%=(("NA".equals((String)projectInfo.getProjectRank()))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.NA")%> </OPTION>
				<OPTION value="?" <%=(("?".equals((String)projectInfo.getProjectRank()))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.NR")%> </OPTION>
				<OPTION value="A" <%=(("A".equals((String)projectInfo.getProjectRank()))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.A")%> </OPTION>
				<OPTION value="B" <%=(("B".equals((String)projectInfo.getProjectRank()))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.B")%> </OPTION>
				<OPTION value="C" <%=(("C".equals((String)projectInfo.getProjectRank()))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.C")%> </OPTION>
				<OPTION value="D" <%=(("D".equals((String)projectInfo.getProjectRank()))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.D")%> </OPTION>
			</SELECT>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Group")%> </TD>
            <TD class="CellBGR3">
            <SELECT name="gi_grp_name" class="COMBO" style='width:125'>
         	<%
         		String grp;
         		for (int k = 0; k < groupList.size(); k++) {
         			grp = (String) groupList.elementAt(k);
         	%>
         		<OPTION value="<%=grp%>" <%if(grp.equals((String)projectInfo.getGroupName())){%>selected<%}%>><%=grp%></OPTION>
         	<%
         		}
         	%>
            </SELECT></TD>            
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Division")%></TD>
            <TD class="CellBGR3"><input type="text" name="gi_division_name" value="<%=projectInfo.getDivisionName()%>" maxlength="50" size="50"></TD>
        </TR>
        <% 
            String strLifecycle = projectInfo.getLifecycle();
            String strType = projectInfo.getProjectType();
        %>
        <TR>
        	<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ProjectType")%> </TD>
            <TD class="CellBGR3">         
	            <input type="radio" name="gi_prj_type" value="8" <%if(strType.equals("Internal")||strType.equals("8")){%>checked<%}%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Internal")%> <BR>
	            <input type="radio" name="gi_prj_type" value="0" <%if(strType.equals("External")||strType.equals("0")){%>checked<%}%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.External")%> <BR>          
	            <input type="radio" name="gi_prj_type" value="9" <%if(strType.equals("Public")||strType.equals("9")){%>checked<%}%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Public")%>            
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ProjectManager")%> </TD>
            <TD class="CellBGR3">
            	<SELECT name="gi_prj_dtr" class="COMBO" style='width:250'>
   				<%
   					for (int i = 0; i < devList.size(); i++) {
						AssignmentInfo devInfo = (AssignmentInfo)devList.elementAt(i);
   				%>
   					<OPTION value="<%=devInfo.account%>" <%=(devInfo.account.equals(projectInfo.getLeader())) ? "selected" : ""%>> <%=devInfo.account + " - " + devInfo.devName%></OPTION>
   				<%
   					}
   				%>
            	</SELECT>
			</TD>
        </TR>
        <TR>        
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ProjectCategory")%> </TD>
            <TD class="CellBGR3">
	            <input type="radio" name="gi_prj_cat" value="0" <%if(strLifecycle.equals("Development")||strLifecycle.equals("0")){%>checked<%}%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Development")%> <BR>
	            <input type="radio" name="gi_prj_cat" value="1" <%if(strLifecycle.equals("Maintenance")||strLifecycle.equals("1")){%>checked<%}%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Maintenance")%> <BR>
	            <input type="radio" name="gi_prj_cat" value="2" <%if(strLifecycle.equals("Others")||strLifecycle.equals("2")){%>checked<%}%>> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Others")%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.BusinessDomain")%>*</TD>
            <TD class="CellBGR3">
            <SELECT name="gi_domain" class="COMBO" style='width:250'>
            <%
         		for (int k = 0; k < bizDomainList.size(); k++) {
        			BizDomainInfo info = (BizDomainInfo)bizDomainList.elementAt(k);
         	%>
         		<OPTION value="<%=info.name%>" <%if(info.name.equals((String)projectInfo.getBusinessDomain())){%>selected<%}%>><%=info.name%></OPTION>
         	<%
         		}
         	%>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ApplicationType")%>*</TD>
            <TD class="CellBGR3">
            <SELECT name="gi_apptype" class="COMBO" style='width:250'>
            <%
            	for (int k = 0; k < appTypeList.size(); k++) {
        			AppTypeInfo info = (AppTypeInfo)appTypeList.elementAt(k);
         	%>
         		<OPTION value="<%=info.apptypeID%>" <%if(projectInfo.getApplicationType().equals(info.name)||projectInfo.getApplicationType().equals(String.valueOf(info.apptypeID))){%>selected<%}%>><%=info.name%></OPTION>
         	<%
         		}
         	%>
            </SELECT></TD>
        </TR>               
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ScopeAndObjective")%> </TD>
            <TD class="CellBGR3">
            	<TEXTAREA name="gi_prj_objective" rows="10" cols="70"><%=projectInfo.getScopeAndObjective()%></TEXTAREA>
            </TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Status")%>* </TD>
        	<TD class="CellBGR3">
        		<SELECT name="cboStatus" class="COMBO">
					<OPTION value="3"<%=((projectInfo.getStatus() == 3)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ProjectStatusTentative")%></OPTION>
					<OPTION value="0"<%=((projectInfo.getStatus() == 0)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ProjectStatusOngoing")%></OPTION>
        		</SELECT>
        	</TD>
        </TR>
    </TBODY>
</TABLE>
</DIV>
</form>
<br>
<br>
<input type="button" name="Update" value=" <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.OK")%> " class="BUTTON" onclick="javascript:on_Submit(this, event);">
<input type="button" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.woGeneralInfo.Cancel")%> " class="BUTTON" onclick="jumpURL('woGeneralInfoView.jsp');">
</div>

<script language = "javascript">
<%
	if (request.getAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE) != null){
%>
		alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.ProjectCodealreadyExisted")%>");
<%
		request.removeAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE);
	}
%>
	function onCheckAccount(button, event){
		var strCustomer = "";
		var strForm = "";
		var type = 0;
		var rd;
		if (button.name == "search_Customer"){
			if(frm_woSMUpdate.customerType.value == "Internal"){
				return;
			}
			strCustomer = frm_woSMUpdate.gi_cst_name;
			strForm = frm_woSMUpdate;
			rd = strForm.rdCustomerName;
		}else{
			if(frm_woSMUpdate.customer2ndType.value == "Internal"){
				return;
			}
			strCustomer = frm_woSMUpdate.gi_cst_name_2nd;
			strForm = frm_woSMUpdate;
			rd = strForm.rdCustomerName2nd;
		}
		if (trim(strCustomer.value) == "") {
	  		window.alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustInputCustomerName")%>");
	  		strCustomer.focus();
	  		return;
	  	}
	    var rdCustomerName;
        for(var i = 0; i < rd.length; i++){
		  if(rd[i].checked){
		  	rdCustomerName = rd[i].value;
		  	break;
		  }
        }
        var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_CUSTOMER%>" + "&Account=" + strCustomer.value + "&Type=" + rdCustomerName;
		javascript:ajax_showOptions1(strCustomer, url, '', event);
	}
		
	function on_Submit(button, event){
		var strCustomer = "";
		var strForm = "";
		var type = 0;
		var rd;
		test = true;
				
		if(frm_woSMUpdate.customerType.value == "External"){
			strCustomer = frm_woSMUpdate.gi_cst_name;
			strForm = frm_woSMUpdate;
			rd = strForm.rdCustomerName;
			
			if (trim(strCustomer.value) == "") {
		  		window.alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustInputCustomerName")%>");
		  		strCustomer.focus();
		  		return;
		  	}
		    var rdCustomerName;
	        for(var i = 0; i < rd.length; i++){
			  if(rd[i].checked){
			  	rdCustomerName = rd[i].value;
			  	break;
			  }
	        }
			//var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_CUSTOMER%>" + "&Account=" + strCustomer.value + "&Type=" + rdCustomerName;
			//javascript:ajax_checkInputName(strCustomer, url, '', event);
			
			//confirm("Customer name is '"+strCustomer.value+"' ?");
			//if(test == false){
			//	alert("Customer name is incorrect.");
			//	return;
			//}
		}
		
		//if(frm_woSMUpdate.customer2ndType.value == "External"){
		//	strCustomer = frm_woSMUpdate.gi_cst_name_2nd;
		//	strForm = frm_woSMUpdate;
		//	rd = strForm.rdCustomerName2nd;
		//	if (trim(strCustomer.value) == "") {
		//  		window.alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustInputCustomerName")%>");
		//  		strCustomer.focus();
		//  		return;
		//  	}
		//    var rdCustomerName;
	    //    for(var i = 0; i < rd.length; i++){
		//	  if(rd[i].checked){
		//	  	rdCustomerName = rd[i].value;
		//	  	break;
		//	  }
	    //    }
		//	var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_CUSTOMER%>" + "&Account=" + strCustomer.value + "&Type=" + rdCustomerName;
		//	javascript:ajax_checkInputName(strCustomer, url, '', event);
		//}
		//confirm("Customer 2nd name is "+strCustomer.value+" ?");
		//if(test == false){
		//	alert("Customer 2nd name is incorrect.");
		//	return;
		//}
		
		if(trim(frm_woSMUpdate.gi_prj_code.value) == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustEnterProjectCode")%>");
  			frm_woSMUpdate.gi_prj_code.focus();  		
  			return;  
  		}
  		if(trim(frm_woSMUpdate.gi_prj_name.value) == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustEnterProjectName")%>");
  			frm_woSMUpdate.gi_prj_name.focus();  		
  			return;  
  		}
  		if(trim(frm_woSMUpdate.gi_cst_name.value) == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustEnterCustomerName")%>");
  			frm_woSMUpdate.gi_cst_name.focus();  		
  			return;  
  		}
  		if(trim(frm_woSMUpdate.gi_cst_name_2nd.value) == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustEnterCustomerName2nd")%>");
  			frm_woSMUpdate.gi_cst_name_2nd.focus();  		
  			return;  
  		}
  		/*
  		if(trim(frm_woSMUpdate.gi_cst_loc.value) == ""){
			alert("<!%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustEnterCustomerLocation")%>");
  			frm_woSMUpdate.gi_cst_loc.focus();  		
  			return;  
  		}
  		*/
  		if(trim(frm_woSMUpdate.gi_domain.value) == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustEnterBusinessDomain")%>");
  			frm_woSMUpdate.gi_domain.focus();  		
  			return;  
  		}
		if(trim(frm_woSMUpdate.gi_contract.value) == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustEnterContractType ")%>");
  			frm_woSMUpdate.gi_contract.focus();  		
  			return;  
  		}  		
  		if(trim(frm_woSMUpdate.gi_grp_name.value) == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.YouMustEnterGroupName")%>");
  			frm_woSMUpdate.gi_grp_name.focus();  		
  			return;  
  		}
  		if(frm_woSMUpdate.gi_prj_objective.value.length >4000){
  			alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.woGeneralInfo.This__field__maximum__size__is__4000__characters__Currently__~PARAM1_OBJECTIVE~")%>", frm_woSMUpdate.gi_prj_objective.value.length )));
  			frm_woSMUpdate.gi_prj_objective.focus();  		
  			return;  
  		}
  		document.frm_woSMUpdate.action = "Fms1Servlet?reqType=<%=Constants.WO_GENERAL_INFO_UPDATE%>";				
  		document.frm_woSMUpdate.submit();
	}
	function doHide(){
		var hideObject = document.getElementById("CustomerType");
	  	hideObject.style.display="none";
	  	hideObject = document.getElementById("Customer2ndType");
	  	hideObject.style.display="none";	  	
	}
	function alertMessage(message1,message2) {
		if (message1=='false' && message2=='false') {
			alert("Invalid customer name and 2nd customer name");
			document.getElementsByName("gi_cst_name")[0].focus();
			return;
		}
		if (message1=='true' && message2=='true'){
			return;
		}
		if (message1=='false') {
			alert("Invalid customer name");
			document.getElementsByName("gi_cst_name")[0].focus();
			return;
		}
		if (message2=='false'){
			alert("Invalid 2nd customer name");
			document.getElementsByName("gi_cst_name_2nd")[0].focus();
			return;
		}
	}
	function test(message){
		if (message==1){
			document.getElementsByName("search_Customer")[0].style.display = "";
			document.getElementsByName("rdCustomerName")[0].style.display = "";
			document.getElementsByName("rdCustomerName")[1].style.display = "";
			document.getElementsByName("A1")[0].style.display = "";
			document.getElementsByName("A1")[1].style.display = "";
		}
		else {
			document.getElementsByName("search_Customer")[0].style.display = "none";
			document.getElementsByName("rdCustomerName")[0].style.display = "none";
			document.getElementsByName("rdCustomerName")[1].style.display = "none";
			document.getElementsByName("A1")[0].style.display = "none";
			document.getElementsByName("A1")[1].style.display = "none";
		}
	}
	function test1(message){
		if (message==1){
			document.getElementsByName("search_Customer2nd")[0].style.display = "";
			document.getElementsByName("rdCustomerName2nd")[0].style.display = "";
			document.getElementsByName("rdCustomerName2nd")[1].style.display = "";
			document.getElementsByName("A2")[0].style.display = "";
			document.getElementsByName("A2")[1].style.display = "";
		}
		else {
			document.getElementsByName("search_Customer2nd")[0].style.display = "none"; 
			document.getElementsByName("rdCustomerName2nd")[0].style.display = "none";
			document.getElementsByName("rdCustomerName2nd")[1].style.display = "none";
			document.getElementsByName("A2")[0].style.display = "none";
			document.getElementsByName("A2")[1].style.display = "none";
		}
	}
</script>
<script>
	doHide();
</script>
</BODY>
</HTML>