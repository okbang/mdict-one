<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plStructure.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	int right = Security.securiPage("Project plan",request,response);
	String orgStructure = (String) session.getAttribute("plOrgStructure");
	Vector interfaceList = (Vector) session.getAttribute("plInterfaceList");
	
	ProjectDateInfo prjDateInfo = (ProjectDateInfo) session.getAttribute("prjDateInfo");
	
	Date fixed = new Date("03-Apr-2009");
	boolean compareDate = false;
	if (prjDateInfo.actualStartDate != null) compareDate = prjDateInfo.actualStartDate.before(fixed);

	// divide Interface to 3 part start
	// 1: Fsoft, 2: Customer, 3: Other project
	int iSize = 0;
	int type = 0;
	InterfaceInfo info = null;
	if (interfaceList != null) iSize = interfaceList.size();
	
	Vector[] vIList = new Vector[3];
	
	vIList[0] = new Vector();
	vIList[1] = new Vector();
	vIList[2] = new Vector();
	
	
	for (int n = 0; n < iSize; n++) {		
		info = (InterfaceInfo) interfaceList.elementAt(n);
		type = info.type;
		if (type == 1) {
			vIList[0].addElement(info);
		}
		else if (type == 2) {
			vIList[1].addElement(info);
		}
		else if (type == 3) {
			vIList[2].addElement(info);
		}
	}
	
	session.setAttribute("FsoftInterfaceList",vIList[0]);
	session.setAttribute("CustomerInterfaceList",vIList[1]);
	session.setAttribute("OtherProjectInterfaceList",vIList[2]);
	
	// divide Interface to 3 part end
	
	Vector subcontractList = (Vector) session.getAttribute("subcontractVector");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plStructure.ProjectplanOrganizationstructu")%> </P>
<br>
<% if (compareDate) { %>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class = "TableCaption"> <%=languageChoose.getMessage("fi.jsp.plStructure.Organizationstructure")%> </CAPTION>

<TR>
<TD class="CellBGRnews" >
<%=((orgStructure==null || orgStructure.equals(""))? "N/A" : ConvertString.toHtml(orgStructure))%>
</TD>
</TR>
</table>
<P>
<%if(right == 3 && !isArchive){%>
<form name="frm_plOrgStructureUpdatePrep" action="Fms1Servlet" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_ORG_STRUCTURE_UPDATE_PREPARE%>">
<input type="submit" name="update" value=" <%=languageChoose.getMessage("fi.jsp.plStructure.Update")%> " class="BUTTON" >
</form>
<%}%>
<br><br><br>
<%}%>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class = "TableCaption"><A name="FsoftInterfaces"> <%=languageChoose.getMessage("fi.jsp.plStructure.FsoftInterfaces")%> </A></CAPTION>
<TR  class="ColumnLabel">
	<TD width = "3%" align = "center">#</TD>
	<TD width = "12%"> <%=languageChoose.getMessage("fi.jsp.plStructure.Function")%> </TD>
	<TD width = "18%"> 
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactPerson")%> <br>
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactPersonNote")%>
	</TD>
	<TD width = "27%"> 
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactAddress")%> <br>
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactAddressNote")%>
	</TD>
	<TD width = "15%"><%=languageChoose.getMessage("fi.jsp.plStructure.ContactAccount")%></TD>
	<TD width = "35%"> <%=languageChoose.getMessage("fi.jsp.plStructure.Responsibility")%> </TD>
</TR>

<!-- Fsoft interfaces -->
<%
String className = "";
iSize = vIList[0].size();
for(int i = 0; i < iSize; i++){
	
 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
 	InterfaceInfo interfaceInfo = (InterfaceInfo) vIList[0].elementAt(i);
%>
<tr  class="<%=className%>">
	<td width="3%" align = "center"><%=i+1%></td>
	<td><%=((interfaceInfo.function == null)? "N/A" : ConvertString.toHtml(interfaceInfo.function))%></td>
<%
	if (interfaceInfo.contactPerson == null || "".equalsIgnoreCase(interfaceInfo.contactPerson)) {
		//incase migrate data
		if (interfaceInfo.name == null) interfaceInfo.name = "N/A";
		if (interfaceInfo.position == null) interfaceInfo.position = "N/A";
%>	
	<td><%=ConvertString.toHtml(interfaceInfo.name) + ",<BR>" + ConvertString.toHtml(interfaceInfo.position)%></td>
<%
	} else {
%>
	<td><%=ConvertString.toHtml(interfaceInfo.contactPerson)%></td>
<%
	}
%>
	<td><%=((interfaceInfo.contact == null)? "N/A" : ConvertString.toHtml(interfaceInfo.contact)) %></td>
	<td><%=((interfaceInfo.contactAccount == null)? "N/A" : ConvertString.toHtml(interfaceInfo.contactAccount))%></td>
	<td><%=((interfaceInfo.responsibility == null || interfaceInfo.responsibility.equals(""))? "N/A" : ConvertString.toHtml(interfaceInfo.responsibility))%></td>
</tr>
<%}%>
</table>
<P>
<%if(right == 3 && !isArchive){%>
<form name="frm_plFsoftInterface" action="Fms1Servlet" method = "post">
<input type = "hidden" name="reqType">
<input type="button" name="add" value=" <%=languageChoose.getMessage("fi.jsp.plStructure.Addnew")%> " class="BUTTON" onclick = "javascript:fsoftInterfaceAddClick();">
<% if (iSize > 0) { %>
<input type="button" name="update" value=" <%=languageChoose.getMessage("fi.jsp.plStructure.Update")%> " class="BUTTON" onclick = "javascript:fsoftInterfaceUpdateClick();" >
<% } %>
</form>
<%}%>
<br>
<BR>

<!-- Customer interfaces-->
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class = "TableCaption"><A name="CustomerInterfaces"> <%=languageChoose.getMessage("fi.jsp.plStructure.CustomerInterfaces")%> </A></CAPTION>
<TR  class="ColumnLabel">
	<TD width = "3%" align = "center">#</TD>
	<TD width = "12%"> <%=languageChoose.getMessage("fi.jsp.plStructure.Department")%> </TD>
	<TD width = "18%"> 
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactPerson")%> <br>
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactPersonNote")%>
	</TD>
	<TD width = "27%"> 
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactAddress")%> <br>
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactAddressNote")%>
	</TD>
	<TD width = "15%"><%=languageChoose.getMessage("fi.jsp.plStructure.ContactAccount")%></TD>
	<TD width = "35%"> <%=languageChoose.getMessage("fi.jsp.plStructure.Responsibility")%> </TD>
</TR>

<%
iSize = vIList[1].size();
for(int i = 0; i < iSize; i++){
	
 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
 	InterfaceInfo interfaceInfo = (InterfaceInfo) vIList[1].elementAt(i);
%>
<tr  class="<%=className%>">
	<td width="3%" align = "center"><%=i+1%></td>
	<td><%=((interfaceInfo.department == null)? "N/A" : ConvertString.toHtml(interfaceInfo.department))%></td>
<%
	if (interfaceInfo.contactPerson == null || "".equalsIgnoreCase(interfaceInfo.contactPerson)) {
		//incase migrate data
		if (interfaceInfo.name == null) interfaceInfo.name = "N/A";
		if (interfaceInfo.position == null) interfaceInfo.position = "N/A";
%>	
	<td><%=ConvertString.toHtml(interfaceInfo.name) + ",<BR>" + ConvertString.toHtml(interfaceInfo.position)%></td>
<%
	} else {
%>
	<td><%=ConvertString.toHtml(interfaceInfo.contactPerson)%></td>
<%
	}
%>
	<td><%=((interfaceInfo.contact == null)? "N/A" : ConvertString.toHtml(interfaceInfo.contact)) %></td>
	<td><%=((interfaceInfo.contactAccount == null)? "N/A" : ConvertString.toHtml(interfaceInfo.contactAccount))%></td>
	<td><%=((interfaceInfo.responsibility == null || interfaceInfo.responsibility.equals(""))? "N/A" : ConvertString.toHtml(interfaceInfo.responsibility))%></td>
</tr>
<%}%>
</table>
<P>
<%if(right == 3 && !isArchive){%>
<form name="frm_plCustomerInterface" action="Fms1Servlet" method = "post">
<input type = "hidden" name="reqType">
<input type="button" name="add" value=" <%=languageChoose.getMessage("fi.jsp.plStructure.Addnew")%> " class="BUTTON" onclick = "javascript:customerInterfaceAddClick();">
<% if (iSize > 0) { %>
<input type="button" name="update" value=" <%=languageChoose.getMessage("fi.jsp.plStructure.Update")%> " class="BUTTON" onclick = "javascript:customerInterfaceUpdateClick();" >
<% } %>
</form>
<%}%>
<br>
<BR>

<!--Other project-->
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class = "TableCaption"><A name="OtherProjectInterfaces"> <%=languageChoose.getMessage("fi.jsp.plStructure.OtherProjects")%> </A></CAPTION>
<TR  class="ColumnLabel">
	<TD width = "3%" align = "center">#</TD>
	<TD width = "12%"> <%=languageChoose.getMessage("fi.jsp.plStructure.Project")%> </TD>
	<TD width = "18%"> 
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactPerson")%> <br>
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactPersonNote")%>
	</TD>
	<TD width = "27%"> 
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactAddress")%> <br>
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactAddressNote")%>
	</TD>
	<TD width = "15%"><%=languageChoose.getMessage("fi.jsp.plStructure.ContactAccount")%></TD>
	<TD width = "35%"> <%=languageChoose.getMessage("fi.jsp.plStructure.Dependency")%> </TD>
</TR>

<%
iSize = vIList[2].size();
for(int i = 0; i < iSize; i++){
	
 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
 	InterfaceInfo interfaceInfo = (InterfaceInfo) vIList[2].elementAt(i);
%>
<tr  class="<%=className%>">
	<td width="3%" align = "center"><%=i+1%></td>
	<td><%=((interfaceInfo.otherProjName == null)? "N/A" : ConvertString.toHtml(interfaceInfo.otherProjName))%></td>
<%
	if (interfaceInfo.contactPerson == null || "".equalsIgnoreCase(interfaceInfo.contactPerson)) {
		//incase migrate data
		if (interfaceInfo.name == null) interfaceInfo.name = "N/A";
		if (interfaceInfo.position == null) interfaceInfo.position = "N/A";
%>	
	<td><%=ConvertString.toHtml(interfaceInfo.name) + ",<BR>" + ConvertString.toHtml(interfaceInfo.position)%></td>
<%
	} else {
%>
	<td><%=ConvertString.toHtml(interfaceInfo.contactPerson)%></td>
<%
	}
%>
	<td><%=((interfaceInfo.contact == null)? "N/A" : ConvertString.toHtml(interfaceInfo.contact)) %></td>
	<td><%=((interfaceInfo.contactAccount == null)? "N/A" : ConvertString.toHtml(interfaceInfo.contactAccount))%></td>
	<td><%=((interfaceInfo.dependency == null || "".equals(interfaceInfo.dependency))? "N/A" : ConvertString.toHtml(interfaceInfo.dependency))%></td>
</tr>
<%}%>
</table>
<P>
<%if(right == 3 && !isArchive){%>
<form name="frm_plOtherProject" action="Fms1Servlet" method = "post">
<input type = "hidden" name="reqType" value="<%=Constants.PL_INTERFACE_ADD_PREPARE%>">
<input type="button" name="add" value=" <%=languageChoose.getMessage("fi.jsp.plStructure.Addnew")%> " class="BUTTON" onclick = "javascript:otherProjectAddClick();">
<% if (iSize > 0) { %>
<input type="button" name="update" value=" <%=languageChoose.getMessage("fi.jsp.plStructure.Update")%> " class="BUTTON" onclick = "javascript:otherProjectUpdateClick();" >
<% } %>
</form>
<%}%>
<br>
<BR>

<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class = "TableCaption"><A name="Subcontracts"> <%=languageChoose.getMessage("fi.jsp.plStructure.Supplier_N_SubContractor")%> </A></CAPTION>

<TR class="ColumnLabel">
	<TD width = "3%" align = "center">#</TD>
	<TD width = "20%"><%=languageChoose.getMessage("fi.jsp.plStructure.WorkProduct")%> </TD>
	<TD width = "20%"><%=languageChoose.getMessage("fi.jsp.plStructure.SupplierSubContractorName")%> </TD>
	<TD width = "20%">
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactPerson")%> <br>
		<%=languageChoose.getMessage("fi.jsp.plStructure.ContactPersonNote")%>
	</TD>
	<TD width = "15%"><%=languageChoose.getMessage("fi.jsp.plStructure.ExpectedDeliveryDate")%> </TD>
	<TD width = "17%"><%=languageChoose.getMessage("fi.jsp.plStructure.RefToContract")%> </TD>
</TR>
<%
for(int i = 0; i < subcontractList.size(); i++){
 	className=(i%2==0) ?"CellBGRnews":"CellBGR3";
 	SubcontractInfo subcontractInfo = (SubcontractInfo) subcontractList.elementAt(i);
%>
<tr class="<%=className%>">
	<td align = "center"><%=i+1%></td>
	<td><a href="subcontractDetails.jsp?vtID=<%=i%>"><%=((subcontractInfo.deliverable == null)? "N/A" : subcontractInfo.deliverable)%></a></td>
	<td><%=((subcontractInfo.sName == null)? "N/A" : subcontractInfo.sName)%></td>
	<td><%=((subcontractInfo.contactP == null)? "N/A" : subcontractInfo.contactP)%></td>
	<td><%=((subcontractInfo.plannedDeliveryDate == null)? "N/A" : CommonTools.dateFormat(subcontractInfo.plannedDeliveryDate))%></td>
	<td><%=((subcontractInfo.refToContract == null)? "N/A" : subcontractInfo.refToContract)%></td>
</tr>
<%}%>
</table>
<P>
<%if(right == 3 && !isArchive){%>
<input type="submit" name="add" value=" <%=languageChoose.getMessage("fi.jsp.plStructure.Addnew")%> " class="BUTTON" onclick="jumpURL('subcontractAdd.jsp')" >
<%}%>
</BODY>
<SCRIPT language="JavaScript">

function fsoftInterfaceAddClick(){
	frm_plFsoftInterface.reqType.value=<%=Constants.PL_INTERFACE_FSOFT_ADD_PREPARE%>;
	frm_plFsoftInterface.submit();
}

function fsoftInterfaceUpdateClick(){
	frm_plFsoftInterface.reqType.value=<%=Constants.PL_INTERFACE_FSOFT_UPDATE_PREPARE%>;
	frm_plFsoftInterface.submit();
}


function customerInterfaceAddClick(){
	frm_plCustomerInterface.reqType.value=<%=Constants.PL_INTERFACE_CUSTOMER_ADD_PREPARE%>;
	frm_plCustomerInterface.submit();
}

function customerInterfaceUpdateClick(){
	frm_plCustomerInterface.reqType.value=<%=Constants.PL_INTERFACE_CUSTOMER_UPDATE_PREPARE%>;
	frm_plCustomerInterface.submit();
}

function otherProjectAddClick(){
	frm_plOtherProject.reqType.value=<%=Constants.PL_INTERFACE_OTHER_PROJECT_ADD_PREPARE%>;
	frm_plOtherProject.submit();
}

function otherProjectUpdateClick(){
	frm_plOtherProject.reqType.value=<%=Constants.PL_INTERFACE_OTHER_PROJECT_UPDATE_PREPARE%>;
	frm_plOtherProject.submit();
}

</SCRIPT>

</HTML>

