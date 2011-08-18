<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plFsoftInterfaceAdd.jsp</TITLE>
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
	int maxRow = Interface.NUMBER_OF_ROW_ADDABLE;
	boolean isOver = false;
	int interfaceDisplayed;
	String rowStyle;	
	
	InterfaceInfo interfaceInfo = null;	
	
	Vector vErrInterface = (Vector) request.getAttribute("ErrFsoftInterfaceList");
	if (vErrInterface != null) {
		isOver = true;
		nRow = vErrInterface.size();
		request.removeAttribute("ErrFsoftInterfaceList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();document.frmFsoftInterfaceAdd.interface_function[0].focus();" class="BD">

<form name ="frmFsoftInterfaceAdd" method= "post" action = "Fms1Servlet#FsoftInterfaces">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.AddNewFsoftInterfaces")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.FsoftInterfaces")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width = "24" align = "center"> # </TD>
			<TD><%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.Function")%>* </TD>
			<TD>
				<%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.ContactPerson")%>* <BR>
				<%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.ContactPersonNote")%>
			</TD>
			<TD> 
				<%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.ContactAddress")%>* <BR>
				<%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.ContactAddressNote")%>
			</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.ContactAccount")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.Responsibility")%>* </TD>			
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)  interfaceInfo = (InterfaceInfo) vErrInterface.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="interface_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD align = "center" width = "24"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "23" name="interface_function"><%=isOver ? ConvertString.toHtml(interfaceInfo.function):""%></TEXTAREA> 
			</TD>
			<TD height="57">
<% if (	isOver && (interfaceInfo.contactPerson == null) ) { %>
			 <TEXTAREA rows="4" cols = "25" name="interface_contactPerson"><%= isOver? ConvertString.toHtml(interfaceInfo.name) + ",<BR>" + ConvertString.toHtml(interfaceInfo.position):""%></TEXTAREA> 
<% } else {	%> 			 
			<TEXTAREA rows="4" cols = "25" name="interface_contactPerson"><%= isOver? ConvertString.toHtml(interfaceInfo.contactPerson):""%></TEXTAREA> 
<% } %>	 
			</TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "25" name="interface_contactAddress"><%=isOver? ConvertString.toHtml(interfaceInfo.contact):""%></TEXTAREA> 
			</TD>
			<TD height="57">			 
            	<INPUT name="strAccountName" size="30" type="text" value="<%=isOver ? interfaceInfo.contactAccount + "" : ""%>"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.Search")%>" onclick="javascript:onCheckAccount(<%=row%>,event);"> <BR>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Account")%>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Name"><%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Name")%><BR>            
			</TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "30" name="interface_resp"><%=isOver? ConvertString.toHtml(interfaceInfo.responsibility):""%></TEXTAREA> 
			</TD>			
		</TR>
<%	
	}
	interfaceDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="interface_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD align = "center" width = "24"> <%=row + 1%> </TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "23" name="interface_function"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "25" name="interface_contactPerson"></TEXTAREA>
			</TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "25" name="interface_contactAddress"></TEXTAREA> 
			</TD>
			<TD height="57">			 
            	<INPUT name="strAccountName" size="30" type="text"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.Search")%>" onclick="javascript:onCheckAccount(<%=row%>,event);"> <BR>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Account")%>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Name"><%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Name")%><BR>            
			</TD>
			<TD height="57">
			 <TEXTAREA rows="4" cols = "30" name="interface_resp"></TEXTAREA> 
			</TD>			
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="interface_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.AddMoreInterfaces")%> </a></p>
<BR>
<input type ="hidden" name = "reqType" value = ""/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plFsoftInterfaceAdd.Cancel")%>" class="BUTTON" onclick="doCancel();">
</form>

<SCRIPT language="JavaScript">

var interface_nextHiddenIndex = <%=interfaceDisplayed + 1%>;
function addMoreRow() {
     getFormElement("interface_row" + interface_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 interface_nextHiddenIndex++;
	 if(interface_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("interface_addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(interface_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("interface_addMoreLink").style.display = "none";    
}

function addSubmit(){	
	if (checkValid()) {
		frmFsoftInterfaceAdd.reqType.value=<%=Constants.PL_INTERFACE_FSOFT_ADD%>;
		frmFsoftInterfaceAdd.submit();
	} else return false;	
}

function doCancel(){
	frmFsoftInterfaceAdd.reqType.value =<%=Constants.PL_STRUCTURE_GET_PAGE%>;
	frmFsoftInterfaceAdd.submit();
}

<%
if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){
%>
		alert("<%=languageChoose.getMessage("fi.jsp.woTeamAdd.AccountError")%>");
		frmFsoftInterfaceAdd.strAccountName[0].focus();
<%
		request.removeAttribute(StringConstants.FILLTER_USER_ERROR);
		request.removeAttribute("errIdx");
	}
%>

function checkValid(){
	var arrTxt= document.getElementsByName("interface_function");
	var arrTxt1= document.getElementsByName("interface_contactPerson");
	var arrTxt2= document.getElementsByName("interface_contactAddress");
	var arrTxt3= document.getElementsByName("interface_resp");	
	var arrTxt4= document.getElementsByName("strAccountName");	
	
	var length = interface_nextHiddenIndex-1;
	var checkAllBlank = 0;
	
	for(i=0; i < length;i++) {
		if  (   trim(arrTxt[i].value) ==''
				&& trim(arrTxt1[i].value) =='' 
				&& trim(arrTxt2[i].value) =='' 
				&& trim(arrTxt3[i].value) ==''
				&& trim(arrTxt4[i].value) ==''
			) 
		{
			checkAllBlank++;				
		} else {
			if (trim(arrTxt[i].value) =='')  {
				alert("Function is mandatory");
				arrTxt[i].focus();
				return false;
			}
			
			if (trim(arrTxt1[i].value) =='')  {
				alert("Contact person is mandatory");
				arrTxt1[i].focus();
				return false;
			}
			
			if (trim(arrTxt2[i].value) =='')  {
				alert("Contact address is mandatory");
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
	}
	
	if (checkAllBlank==length) {
		alert("Please input data");
		arrTxt[0].focus();
		return false;
	}
	return true;
}

	function onCheckAccount(rowCheck,event){
		if (trim(frmFsoftInterfaceAdd.strAccountName[rowCheck].value) == ""){
			window.alert("<%=languageChoose.getMessage("fi.jsp.woTeamAdd.YouMustInputUserAccount")%>");
			frmFsoftInterfaceAdd.strAccountName[rowCheck].focus();
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
        var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_USER%>" + "&Account=" + frmFsoftInterfaceAdd.strAccountName[rowCheck].value + "&Type=" + rdAccountName;
		javascript:ajax_showOptions(document.forms['frmFsoftInterfaceAdd'].strAccountName[rowCheck], url, '', event);
	}

</SCRIPT>
</BODY>
</HTML>
