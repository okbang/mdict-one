<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>scheSubcontract.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
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
	int right = Security.securiPage("Schedule",request,response); 
	Vector subcontractList=(Vector)session.getAttribute("subcontractVector");
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.scheSubcontract.ScheduleSubcontracts")%> </P>
<FORM method="post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_SCHEDULE_HEADER%>" name="frm">
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
String className = "";
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
<P><%if(right == 3 && !isArchive){%><INPUT type="button" class="BUTTON" name="btnAddSubcontract" value="<%=languageChoose.getMessage("fi.jsp.scheSubcontract.Addnew")%>" onclick="jumpURL('subcontractAdd.jsp')"><%}%></P>
</FORM>
</BODY>
</HTML>
