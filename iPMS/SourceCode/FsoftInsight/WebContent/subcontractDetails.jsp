<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>Subcontract</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%

	int caller=Integer.parseInt(session.getAttribute("caller").toString());
	int right = 1;
	String title;
	if(caller==Constants.SCHEDULE_CALLER){
		right = Security.securiPage("Schedule",request,response);
		title=languageChoose.getMessage("fi.jsp.subcontractDetails.Schedule");
	}
	else{ //from Prom project plan 
		right = Security.securiPage("Project plan",request,response);
		title=languageChoose.getMessage("fi.jsp.subcontractDetails.Projectplan");
	}
	Vector vt=(Vector)session.getAttribute("subcontractVector");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	SubcontractInfo info=(SubcontractInfo)vt.get(vtID);	
	String pDeliveryD=CommonTools.dateFormat(info.plannedDeliveryDate);
	String aDeliveryD=CommonTools.dateFormat(info.actualDeliveryDate);
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"><%=title%></p>
<BR>
<FORM method="post" action="subcontractUpdate.jsp?vtID=<%=vtID%>" name="frm">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.subcontractDetails.Supplier_N_Sub-ContractorDetails")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.subcontractDetails.WorkProduct")%> </TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(info.deliverable)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.subcontractDetails.SupplierSubContractorName")%> </TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(info.sName)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> 
            	<%=languageChoose.getMessage("fi.jsp.subcontractDetails.ContactPerson")%><BR>
				<%=languageChoose.getMessage("fi.jsp.subcontractDetails.ContactPersonNote")%>
            </TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(info.contactP)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.subcontractDetails.ExpectedDeliveryDate")%> </TD>
            <TD class="CellBGRnews"><%=pDeliveryD%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.subcontractDetails.Actualdeliverydate")%> </TD>
            <TD class="CellBGRnews"><%=aDeliveryD%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.subcontractDetails.RefToContract")%></TD>
            <TD class="CellBGRnews"><%=((info.refToContract==null)?"N/A":ConvertString.toHtml(info.refToContract))%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.subcontractDetails.Note")%></TD>
            <TD class="CellBGRnews"><%=((info.note==null)?"N/A":ConvertString.toHtml(info.note))%></TD>
        </TR>
    </TBODY> 
</TABLE>
<P><%if(right == 3 && !isArchive){%>
<INPUT type="submit" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.subcontractDetails.Update")%>" class="BUTTON">
<INPUT type="button" class="BUTTON" name="txtDelete" value="<%=languageChoose.getMessage("fi.jsp.subcontractDetails.Delete")%>" onclick="doDelete();"><%}%>
<INPUT type="button" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.subcontractDetails.Back")%>" class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_SUBCONTRACT_GET_LIST:Constants.PL_STRUCTURE_GET_PAGE%>);"></P>
</FORM>
<SCRIPT language="javascript">
  function doDelete(){
  	  if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.subcontractDetails.Areyousuretodelete")%>")){
  			return;
  	  }
  	  frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_SUBCONTRACT%>&vtID=<%=vtID%>#Subcontracts";		
  	  frm.submit();	    	    	
  }
 </SCRIPT> 
</BODY>
</HTML>
