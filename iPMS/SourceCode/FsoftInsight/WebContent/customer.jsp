<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>customer.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>

<link href="pspl_multiselect.css" rel="stylesheet" type="text/css">
<script language="javascript" src="pspl_multiselect.js"></script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	String nameSearch = (String)request.getSession().getAttribute("nameSearch");
	String listOgSearch =  (String)request.getSession().getAttribute("listOgSearch");
	StringTokenizer st = new StringTokenizer(listOgSearch,",");
	Vector dataListSearch = new Vector();
	while(st.hasMoreTokens()){
		String temp = st.nextToken().trim();
		if (!temp.equals("")){
			dataListSearch.add(temp);
		}
	}
	Vector OgsList = (Vector)session.getAttribute("OgsList");
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
	int right = Security.securiPage("Requirements",request,response);
	String addNewvalue = "1";
	addNewvalue = (String)session.getAttribute("addNewValue");
	session.removeAttribute("addNewValue");
	if (!addNewvalue.equals("1")) {
	%>

<BODY onload="loadOrgMenu();javascript:Import();fail('<%=addNewvalue%>');aC();" class="BD">	
<% } else {
%>
<BODY onload="loadOrgMenu();javascript:Import();aC(); " class="BD">	
<%}%>

<FORM method="post" name="frm1">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.customer.Customer")%> </P>
<br>
<table>
<tr>
<td>
Standard Name : 
<td>
<input type=text name="nameSearch" value="<%=nameSearch%>"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</td>
<td>
OGS : 
</td>
<td>
<!-- code sample, Box Style 6  -->
<input type=hidden name = "listOgSearch" value="">
</td>
<td>
<span id="sample6_box" class="selBox6" >
<select id="sample6" name="sample6[]" multiple size="3" class="msel">
<%
	for(int i=0;i<OgsList.size();i++) {
		String og = (String)OgsList.get(i);
		int flag = 0 ; 
		for (int j=0;j<dataListSearch.size();j++) {
			String temp1 = (String)dataListSearch.get(j);
			if (temp1.equalsIgnoreCase(og.trim())) {
				flag = 1 ; 
				break;
			}
		}
		if (flag==0) {
%>

<option value="<%=og%>"><%=og%></option>
<%	
		}
		else {
%>
<option value="<%=og%>" selected><%=og%></option>	
<%
		}
	}
%>
</select>
</span>
<script language="javascript">
Init
( 'sample6', "arrow_on6.png", "arrow_on6.gif","arrow6", "optionTextBox6", "optionDiv6", "cell6", "cellHover6", "cellSelected6" );
</script>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</td>
<td>
<input type=button class="BUTTON" name="customerSearch" value="Filter" onclick="doSearchCustomer()">
</td>
</tr>
</table>
</form>
<br>
<TABLE cellspacing="1" class="HDR" width="100%">
	<TBODY>
		<TR>
			<TD width="10%"></TD>
			<TD></TD>
			<TD align="right"><P align="right"><%if(right == 3 && !isArchive){%><a href="javascript:Import();"><%=languageChoose.getMessage("fi.jsp.customer.ImportCustomer")%></a><%}%></p></TD>
		</TR>
	</TBODY>
</TABLE>
<br>
<FORM name="frm_Import" action="Fms1Servlet?reqType=<%=Constants.CUSTOMER_IMPORT%>" enctype="MULTIPART/FORM-DATA" method=POST >
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
	    	<TD>
	    		<A href="CustomersImportTemplate.xls"><FONT color="blue" class="label1">Download Template File</FONT></A>
	    	</TD>
	    </TR>
	</TBODY>
</TABLE>
</FORM>

<%
	if(session.getAttribute("ImportFail") != null){
	session.removeAttribute("ImportFail");
%>
	<H3 style="color: red;">Import Fail</H3>
<%
	}
%>
<%
	if(session.getAttribute("ImportSuccess") != null){
	
	Vector importSuccessCustomerList = (Vector)session.getAttribute("importSuccessCustomerList");	
%>
	<H3 style="color: blue;">Imported Customer(s) Successfully</H3>

<br>
<TABLE cellspacing="1" class="Table" width="95%" >
<TBODY>
 	<TR class="ColumnLabel">
	 	<TD>No</TD>
 		<TD><%=languageChoose.getMessage("fi.jsp.customer.StandardName")%></TD>
        <TD><%=languageChoose.getMessage("fi.jsp.customer.FullName")%></TD>
        <TD><%=languageChoose.getMessage("fi.jsp.customer.OGs")%></TD>
        <TD><%=languageChoose.getMessage("fi.jsp.customer.Note")%></TD>
    </TR>
<%
	CustomerInfo customerInfo = new CustomerInfo();
	for (int i=0; i < importSuccessCustomerList.size(); i++) {
		String className = (i%2 == 0) ?"CellBGRnews":"CellBGR3";
		customerInfo = (CustomerInfo) importSuccessCustomerList.elementAt(i);
%>		
        <TR>
        	<TD width = "22" align = "center" class="<%=className%>"><%=i+1%></TD>
        	<TD class="<%=className%>"><%=(CommonTools.formatString(customerInfo.standardName).equals("N/A")?"":CommonTools.formatString(customerInfo.standardName))%></TD>
            <TD class="<%=className%>"><%=(CommonTools.formatString(customerInfo.fullName).equals("N/A")?"":CommonTools.formatString(customerInfo.fullName))%></TD>
            <TD class="<%=className%>"><%=(CommonTools.formatString(customerInfo.ofOGs).equals("N/A")?"":CommonTools.formatString(customerInfo.ofOGs))%></TD>
            <TD class="<%=className%>"><%=(CommonTools.formatString(customerInfo.note).equals("N/A")?"":CommonTools.formatString(customerInfo.note))%></TD>
        </TR>
     <%}%>
     </TBODY>
</TABLE>

<%	
	session.removeAttribute("ImportSuccess");
	session.removeAttribute("importSuccessCustomerList");
	}
%>

<%
	Vector duplicateCustomerList = (Vector)session.getAttribute("duplicateCustomerList");	
	if(duplicateCustomerList != null && duplicateCustomerList.size()>0){
%>
	<H3 style="color: red;">List Duplicate Customers</H3>

<br>
<TABLE cellspacing="1" class="Table" width="95%" >
<TBODY>
 	<TR class="ColumnLabel">
	 	<TD><%=languageChoose.getMessage("fi.jsp.customer.Num")%></TD>
 		<TD><%=languageChoose.getMessage("fi.jsp.customer.StandardName")%></TD>
        <TD><%=languageChoose.getMessage("fi.jsp.customer.FullName")%></TD>
        <TD><%=languageChoose.getMessage("fi.jsp.customer.OGs")%></TD>
        <TD><%=languageChoose.getMessage("fi.jsp.customer.Note")%></TD>
    </TR>
<%
	CustomerInfo customerInfo = new CustomerInfo();
	for (int i=0; i < duplicateCustomerList.size(); i++) {
		String className = (i%2 == 0) ?"CellBGRnews":"CellBGR3";
		customerInfo = (CustomerInfo) duplicateCustomerList.elementAt(i);
%>		
        <TR>
        	<TD width = "22" align = "center" class="<%=className%>"><%=i+1%></TD>
        	<TD class="<%=className%>"><%=(CommonTools.formatString(customerInfo.standardName).equals("N/A")?"":CommonTools.formatString(customerInfo.standardName))%></TD>
            <TD class="<%=className%>"><%=(CommonTools.formatString(customerInfo.fullName).equals("N/A")?"":CommonTools.formatString(customerInfo.fullName))%></TD>
            <TD class="<%=className%>"><%=(CommonTools.formatString(customerInfo.ofOGs).equals("N/A")?"":CommonTools.formatString(customerInfo.ofOGs))%></TD>
            <TD class="<%=className%>"><%=(CommonTools.formatString(customerInfo.note).equals("N/A")?"":CommonTools.formatString(customerInfo.note))%></TD>
        </TR>
     <%}%>
     </TBODY>
</TABLE>

<%	
	session.removeAttribute("duplicateCustomerList");
	}
%>

<%
	Vector loadCustomerVector = (Vector)session.getAttribute("loadCustomerVector");	
	if(loadCustomerVector != null ){
%>

<br>
<FORM method="post" name="frm">
<TABLE cellspacing="1" class="Table" width="100%" border="2" bordercolor="#E2E2E2">
<CAPTION class="TableCaption"><A name="cusList"> <font size="2"> Customers List </font>
	</A></CAPTION>
<TBODY>
 	<TR class="ColumnLabel">
 		<TD class="ColumnLabel" width="3%" align="center"><INPUT
				id="selectAll" type="checkbox" name="selectAll"
				onclick="selectAll1()" /></TD>
	 	<TD><%=languageChoose.getMessage("fi.jsp.customer.Num")%></TD>
 		<TD><%=languageChoose.getMessage("fi.jsp.customer.StandardName")%></TD>
        <TD><%=languageChoose.getMessage("fi.jsp.customer.FullName")%></TD>
        <TD><%=languageChoose.getMessage("fi.jsp.customer.OGs")%></TD>
        <TD><%=languageChoose.getMessage("fi.jsp.customer.Note")%></TD>
    </TR>
<%
	CustomerInfo customerInfo = new CustomerInfo();
	for (int i=0; i < loadCustomerVector.size(); i++) {
		String className = (i%2 == 0) ?"CellBGRnews":"CellBGR3";
		customerInfo = (CustomerInfo) loadCustomerVector.elementAt(i);
%>		
        <TR>
        	<td class="<%=className%>" width="3%" align="center" ><INPUT
				type="checkbox" name="checkForUpdate" value="<%=customerInfo.cusID%>" onclick="check()" /></TD>
        	<TD width = "22" align = "center" class="<%=className%>"><%=i+1%></TD>
        	<TD class="<%=className%>"><%=(ConvertString.toHtml(customerInfo.standardName).equals("N/A")?"":ConvertString.toHtml(customerInfo.standardName))%></TD>
            <TD class="<%=className%>"><%=(ConvertString.toHtml(customerInfo.fullName).equals("N/A")?"":ConvertString.toHtml(customerInfo.fullName))%></TD>
            <TD class="<%=className%>"><%=(ConvertString.toHtml(customerInfo.ofOGs).equals("N/A")?"":ConvertString.toHtml(customerInfo.ofOGs))%></TD>
            <TD class="<%=className%>"><%=(ConvertString.toHtml(customerInfo.note).equals("N/A")?"":ConvertString.toHtml(customerInfo.note))%></TD>
        </TR>
     <%}%>
     </TBODY>
</TABLE>
</form>
<br>
<INPUT type="button" class="BUTTON" name="btnAddProjectHis" value="<%=languageChoose.getMessage("fi.jsp.postMortemReport.AddNew")%>" onclick="jumpURL('addNewCustomer.jsp');">
<%if(right == 3 && !isArchive){%><INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.postMortemReport.Update")%>" class="BUTTON" onclick="doUpdate();"><%}%>


<%	
	session.removeAttribute("loadCustomerVector");
	}
	else {
%>
	<INPUT type="button" class="BUTTON" name="btnAddProjectHis" value="<%=languageChoose.getMessage("fi.jsp.postMortemReport.AddNew")%>" onclick="jumpURL('addNewCustomer.jsp');">
<%
	}
%>
	
<BR>

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
function doImport(ext) {
    if (ext != '') {
        document.frm_Import.submit();
    }
}

function doImport1(ext) {
    if (ext != '') {
        document.frm_Import.submit();
    }
    fail(message);
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
function doUpdate(){
		var value;
		var count = 0  ; 
		var uCheck = document.getElementsByName("checkForUpdate");

		for (i = 0; i < uCheck.length; i++) {
			if(uCheck[i].checked==true) count++;
		}
		value = new Array(count);
		var k = 0 ; 
		for (i = 0; i < uCheck.length; i++) {
			if (uCheck[i].checked==true) {
				value[k] = uCheck[i].value;
				k++;
			}
		}
		if (count==0) {
			alert('Please choose data to update');
			return;
		}
		frm.action="Fms1Servlet?reqType=<%=Constants.CUSTOMER_UPDATE%>&value="+value;
		frm.submit();
}

function check(){
		var count = 0  ; 
		var uCheck = document.getElementsByName("checkForUpdate");

		for (i = 0; i < uCheck.length; i++) {
			if(uCheck[i].checked==true) count++;
		}
		
		if (count<uCheck.length) document.getElementById("selectAll").checked = false;
		if (count==uCheck.length) document.getElementById("selectAll").checked = true;
}
function selectAll1(){
		var uCheck = document.getElementsByName("checkForUpdate");

		for (i = 0; i < uCheck.length; i++) {
			uCheck[i].checked = document.getElementById("selectAll").checked;
		}
}
function fail(message){
	alert('The following customers are already exist: ' + message +' .It can not be added');
}
function doSearchCustomer(){
	setValueElement('listOgSearch',0);
	frm1.action="Fms1Servlet?reqType=<%=Constants.SEARCH_CUSTOMER%>";
	frm1.submit();
}
</SCRIPT>
</BODY>
</HTML>
