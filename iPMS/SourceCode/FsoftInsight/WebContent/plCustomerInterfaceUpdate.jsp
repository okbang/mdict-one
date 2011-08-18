<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plCustomerInterfaceUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	boolean isOver = false;
	int interfaceDisplayed;
	InterfaceInfo interfaceInfo = null;
	String rowStyle;
		
	Vector vInterface = (Vector) session.getAttribute("CustomerInterfaceList");
	Vector vErrInterface = (Vector) request.getAttribute("ErrCustomerInterfaceList");
	
	if (vErrInterface != null) {
		isOver = true;		
		request.removeAttribute("ErrCustomerInterfaceList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
	
	String strUserErrIdx = (String) request.getAttribute("UserErrIdx");
	int userErrIdx = -1;
	if (strUserErrIdx != null) {
		request.removeAttribute("UserErrIdx");
		userErrIdx = Integer.parseInt(strUserErrIdx);
	}
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<form name ="frmCustomerInterfaceUpdate" method= "post" action = "Fms1Servlet#CustomerInterfaces">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plCustomerInterfaceUpdate.UpdateCustomerInterfaces")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Update datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plCustomerInterfaceUpdate.CustomerInterfaces")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD valign ="top"> &nbsp; </TD>
			<TD width = "24" align = "center"> # </TD>
			<TD width="150"><%=languageChoose.getMessage("fi.jsp.plCustomerInterfaceUpdate.Department")%>* </TD>
			<TD width="160">
				<%=languageChoose.getMessage("fi.jsp.plCustomerInterfaceUpdate.ContactPerson")%>* <BR>
				<%=languageChoose.getMessage("fi.jsp.plCustomerInterfaceUpdate.ContactPersonNote")%>
			</TD>
			<TD width="160"> 
				<%=languageChoose.getMessage("fi.jsp.plCustomerInterfaceUpdate.ContactAddress")%>* <BR>
				<%=languageChoose.getMessage("fi.jsp.plCustomerInterfaceUpdate.ContactAddressNote")%>
			</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.ContactAccount")%></TD>
			<TD width="250"><%=languageChoose.getMessage("fi.jsp.plCustomerInterfaceUpdate.Responsibility")%>* </TD>			
		</TR>
	</THEAD>
	<TBODY>
<%
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrInterface;
	else vTemp = vInterface;
	nRow = vTemp.size();
	// Display current list (last updated data)
	for (; row < nRow; row++) {	
		interfaceInfo = (InterfaceInfo) vTemp.elementAt(row);	
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR class="<%=rowStyle%>" valign="top">
			<TD width = "24" align = "center"> <A href="javascript:OnDelete(<%=interfaceInfo.interfaceID%>)"><IMG src="image/delete1.gif" title="Delete"></img></A></TD>
			<TD align = "center" width = "24"> <%=row + 1%> <input type ="hidden" name = "interfaceID" value = "<%=interfaceInfo.interfaceID%>"/> </TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "23" name="interface_department"><%=ConvertString.toHtml(interfaceInfo.department)%></TEXTAREA> 
			</TD>
			<TD height="57">
<% if (	interfaceInfo.contactPerson == null ) { %>
			 <TEXTAREA rows="4" cols = "25" name="interface_contactPerson"><%= ConvertString.toHtml(interfaceInfo.name) + ",\n" + ConvertString.toHtml(interfaceInfo.position)%></TEXTAREA> 
<% } else {	%> 			 
			<TEXTAREA rows="4" cols = "25" name="interface_contactPerson"><%= ConvertString.toHtml(interfaceInfo.contactPerson)%></TEXTAREA> 
<% } %>	 
			</TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "25" name="interface_contactAddress"><%=ConvertString.toHtml(interfaceInfo.contact)%></TEXTAREA> 
			</TD>
			<TD height="57">			 
            	<INPUT name="strAccountName" size="30" type="text" value="<%=interfaceInfo.contactAccount == null ? "":interfaceInfo.contactAccount%>"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.Search")%>" onclick="javascript:onCheckAccount(<%=row%>,event);"> <BR>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Account")%>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Name"><%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Name")%><BR>            
			</TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "30" name="interface_resp"><%=ConvertString.toHtml(interfaceInfo.responsibility)%></TEXTAREA> 
			</TD>			
		</TR>
<%	
	}
	interfaceDisplayed = row;	// Indicate numbers of lines displayed
%>
		<TR style="display:none">
			<TD></TD>
			<TD align = "center" width = "24"><input type ="hidden" name = "interfaceID" value = "0"/></TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "23" name="interface_department"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "25" name="interface_contactPerson"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "25" name="interface_contactAddress"></TEXTAREA> 
			</TD>
			<TD height="57">			 
            	<INPUT name="strAccountName" size="30" type="text" />
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.Search")%>" onclick="javascript:onCheckAccount(<%=row%>,event);"> <BR>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Account")%>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Name"><%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Name")%><BR>            
			</TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "30" name="interface_resp"></TEXTAREA> 
			</TD>			
		</TR>
	</TBODY>
</TABLE>
<P>
<input type ="hidden" name = "reqType" value = ""/>
<input type ="hidden" name = "delInterfaceID" value = ""/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plCustomerInterfaceUpdate.OK")%>" class="BUTTON" onclick="UpdateSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plCustomerInterfaceUpdate.Cancel")%>" class="BUTTON" onclick="doCancel();">
</form>

<SCRIPT language="JavaScript">

var interface_nextHiddenIndex = <%=interfaceDisplayed + 1%>;

function UpdateSubmit(){	
	if (checkValid()) {
		frmCustomerInterfaceUpdate.reqType.value=<%=Constants.PL_INTERFACE_CUSTOMER_UPDATE%>;
		frmCustomerInterfaceUpdate.submit();
	} else return false;	
}

function doCancel(){
	frmCustomerInterfaceUpdate.reqType.value =<%=Constants.PL_STRUCTURE_GET_PAGE%>;
	frmCustomerInterfaceUpdate.submit();
}

function OnDelete(paraInterfaceID){
	if (window.confirm("Are you sure to delete this interface ?")!=0) {
		frmCustomerInterfaceUpdate.reqType.value =<%=Constants.PL_INTERFACE_CUSTOMER_DELETE%>;
		frmCustomerInterfaceUpdate.delInterfaceID.value =paraInterfaceID;
		frmCustomerInterfaceUpdate.submit();
	}
}

<%
if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){
%>
		alert("<%=languageChoose.getMessage("fi.jsp.woTeamAdd.AccountError")%>");
		frmCustomerInterfaceUpdate.strAccountName[<%=userErrIdx%>].focus();
<%
		request.removeAttribute(StringConstants.FILLTER_USER_ERROR);
		request.removeAttribute("errIdx");
	}
%>

function checkValid(){
	var arrTxt= document.getElementsByName("interface_department");
	var arrTxt1= document.getElementsByName("interface_contactPerson");
	var arrTxt2= document.getElementsByName("interface_contactAddress");
	var arrTxt3= document.getElementsByName("interface_resp");	
	
	var length = interface_nextHiddenIndex-1;	
	
	for(i=0; i < length;i++) {
		
		if (trim(arrTxt[i].value) =='')  {
			alert("Department is mandatory");
			arrTxt[i].focus();
			return false;
		}
		
		if (trim(arrTxt1[i].value) =='')  {
			alert("Contact person is mandatory");
			arrTxt1[i].focus();
			return false;
		}
		
		if (trim(arrTxt2[i].value) =='')  {
			alert("Contact Address is mandatory");
			arrTxt2[i].focus();
			return false;
		}
		
		if (trim(arrTxt3[i].value) =='')  {
			alert("Responsibility is mandatory");
			arrTxt3[i].focus();
			return false;
		}
	
		if(!maxLength(arrTxt[i],"Please input less than 600 characters",600))
		return false;			
		if(!maxLength(arrTxt1[i],"Please input less than 600 characters",600))
		return false;			
		if(!maxLength(arrTxt2[i],"Please input less than 600 characters",600))
		return false;
		if(!maxLength(arrTxt3[i],"Please input less than 600 characters",600))
		return false;
	}	
	return true;
}

	function onCheckAccount(rowCheck,event){
		if (trim(frmCustomerInterfaceUpdate.strAccountName[rowCheck].value) == ""){
			window.alert("<%=languageChoose.getMessage("fi.jsp.woTeamAdd.YouMustInputUserAccount")%>");
			frmCustomerInterfaceUpdate.strAccountName[rowCheck].focus();
			return ;
		}
	    var rd = document.getElementsByName("rdAccountName".concat(rowCheck));
	    var rdAccountName;
        for(var i=0;i<rd.length;i++){
		  if(rd[i].checked){
		  	rdAccountName = rd[i].value;
		  	break;
		  }
        }
        var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_USER%>" + "&Account=" + frmCustomerInterfaceUpdate.strAccountName[rowCheck].value + "&Type=" + rdAccountName;
		javascript:ajax_showOptions(document.forms['frmCustomerInterfaceUpdate'].strAccountName[rowCheck], url, '', event);
	}

</SCRIPT>
</BODY>
</HTML>
