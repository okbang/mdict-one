<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>updateCustomer.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<link href="pspl_multiselect.css" rel="stylesheet" type="text/css">
<script language="javascript" src="pspl_multiselect.js"></script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	int right = Security.securiPage("Project reports", request, response);
	Vector data = (Vector)session.getAttribute("customerUpdateList");
	String duplicateUpdateCustomer = (String) session.getAttribute("duplicateUpdateCustomer");
	String line = (String) session.getAttribute("line");
	Vector OgsList = (Vector)session.getAttribute("OgsList");
%>
<BODY onLoad="loadOrgMenu();aC();" class="BD">
<br><br>
<P class="TITLE"> Update Customers </P>
<br>
<%
	if (duplicateUpdateCustomer.equalsIgnoreCase("1")) {
%>
<H4 style="color: red">Error when save. Duplicate customer name, please check your input carefully</H4>
Duplicate in line: <%=line%>
<% }
%>
<br>
<br>
<FORM method="post" name="frm">
<TABLE cellspacing="1" class="Table" width="95%" >
<TBODY>
 	<TR class="ColumnLabel">
 		<TD align=center width="3%">#</TD>
 		<TD align=center><%=languageChoose.getMessage("fi.jsp.customer.StandardName")%>*</TD>
        <TD align=center><%=languageChoose.getMessage("fi.jsp.customer.FullName")%>*</TD>
        <TD align=center><%=languageChoose.getMessage("fi.jsp.customer.OGs")%></TD>
        <TD align=center><%=languageChoose.getMessage("fi.jsp.customer.Note")%></TD>
    </TR>
<%
	for (int i=0; i < data.size(); i++) {
		String className = (i%2 == 0) ?"CellBGRnews":"CellBGR3";
		CustomerInfo cus = (CustomerInfo)data.get(i);
%>		
        <TR id="tr">
        	<input type=hidden name='customerId' value="<%=cus.cusID%>">
        	<TD align=center width="3%" class="<%=className%>"><%=i+1%></TD>
        	<TD width = "200" class="<%=className%>"><input size='25' type=text name='sName' value="<%=cus.standardName%>"></TD>
            <TD width = "300" class="<%=className%>"><input size='35' type=text name='fName' value="<%=cus.fullName%>"></TD>
            
            <TD width = "50" class="<%=className%>">
	        <span id="sample6<%=i%>_box" class="selBox6" >
				<select id="sample6<%=i%>" name="sample6[]" multiple size="3" class="msel">
					<%
						for(int i1=0;i1<OgsList.size();i1++) {
							String og = (String)OgsList.get(i1);
							int flag = 0 ; 
							if (cus.ofOGs!=null && (!cus.ofOGs.trim().equals(""))) {
								StringTokenizer st = new StringTokenizer(cus.ofOGs,",");
								while (st.hasMoreTokens()) {
									String temp1 = st.nextToken().trim();
									if (temp1.equalsIgnoreCase(og.trim())) {
										flag = 1 ; 
										break;
									}
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
				( 'sample6<%=i%>', "arrow_on6.png", "arrow_on6.gif","arrow6", "optionTextBox6", "optionDiv6", "cell6", "cellHover6", "cellSelected6" );
			</script>
          	</TD>
            
            <input  type=hidden name='OGS' value="">
            
            <TD class="<%=className%>"><TEXTAREA rows="4" cols="50" name="note"><%=cus.note==null?"":cus.note%></TEXTAREA></TD>
        </TR> 	
     <%
     }%>
     </TBODY>
</TABLE>
</form>

<%if (right == 3 ) {%> <INPUT type="button"
	value="Save"
	class="BUTTON" onclick="doSave();"> <%}%> 
	<INPUT type="button"
	value="Cancel"
	class="BUTTON" onclick="doCancel();">

<SCRIPT language="javascript">
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
function doSave(){
	if(!validate()) return;
	
	var value1 = document.getElementsByName("OGS");
	var value2 = document.getElementsByName("sample6[]");
	
	for (var i=0;i<value1.length;i++) {
		var choose = '';
		for (var j=0;j<value2[i].options.length;j++) {
			if(value2[i].options[j].selected == true) {
				choose += value2[i].options[j].innerHTML + ',';
			}
		}
		value1[i].value = choose; 
	}
	for (var i=0;i<value1.length;i++) {
		if (value1[i].value.trim()!='') {
			var temp1 = '';
			for (var k=0;k<(value1[i].value.length-1);k++){
				temp1 += value1[i].value.charAt(k);
			}
			value1[i].value = temp1 ; 
		}
	}
	
	frm.action = "Fms1Servlet?reqType="+<%=Constants.DO_CUSTOMER_UPDATE%>;
	frm.submit();
	
}
function validate(){
	for (var i=0;i<document.getElementsByName("tr").length;i++) {
			if(document.getElementsByName("sName")[i].value=='') {
				alert('Standard Name is required');
				document.getElementsByName("sName")[i].focus();
				return false;
			}
			if(document.getElementsByName("fName")[i].value=='') {
				alert('Full Name is required');
				document.getElementsByName("fName")[i].focus();
				return false;
			}
	}
	return true;
}
function doCancel(){
	frm.action = "Fms1Servlet?reqType="+<%=Constants.LOAD_CUSTOMER_PAGE%>;
	frm.submit();
}
</SCRIPT>