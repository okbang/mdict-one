<%@page import="com.fms1.tools.*,com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>moduleView.jsp</TITLE>
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
%>
<BODY onload="loadPrjMenu()" class="BD"><P></P>
<%
	int right = Security.securiPage("Size", request, response);
	int schedRight = Security.getRights("Schedule", request);
	Vector moduleList = (Vector)session.getAttribute("moduleList");
	String vtID = request.getParameter("vtID");
	WPSizeInfo moduleInfo = (WPSizeInfo)moduleList.elementAt(Integer.parseInt(vtID));
	int hasLoc = 0;	
	if (request.getAttribute("HasLoc") != null){
		 hasLoc = Integer.parseInt((String)request.getAttribute("HasLoc"));
	}
	String createdSize = CommonTools.formatDouble(moduleInfo.createdSize);
	String deviation = CommonTools.formatDouble(moduleInfo.deviation);
	String reestSize = CommonTools.formatDouble(moduleInfo.reestimatedSize);
	String estSize = CommonTools.formatDouble(moduleInfo.estimatedSize);
	String actSizeName = "N/A";
	if (moduleInfo.actualSizeUnitName != null)
			actSizeName = moduleInfo.actualSizeUnitName;
		
	String estimatedSizeUnitName = "N/A";
	if (moduleInfo.estimatedSizeUnitName != null)
		estimatedSizeUnitName = moduleInfo.estimatedSizeUnitName;
	
	String actSize= CommonTools.formatDouble(moduleInfo.actualSize);
	String reuse= CommonTools.formatDouble(moduleInfo.reusePercentage);
	String desc = "N/A";
	if (moduleInfo.description != null)
		desc = moduleInfo.description;
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.moduleView.Productandsize")%> </P>
<TABLE class="Table" cellspacing="1" width="560">
    <COL span="1" width="160">
    <COL span="1" width="400">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.moduleView.Productsizeview")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Name")%> </TD>
            <TD class="CellBGR3"><%=moduleInfo.name%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Workproduct")%> </TD>
            <TD class="CellBGR3"><%=languageChoose.getMessage(moduleInfo.categoryName)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Plannedsizeunit")%> </TD>
            <TD class="CellBGR3"><%=estimatedSizeUnitName%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Plannedsize")%> </TD>
            <TD class="CellBGR3"><%=estSize%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Replannedsize")%> </TD>
            <TD class="CellBGR3"><%=reestSize%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Actualsizeunit")%> </TD>
            <TD class="CellBGR3"><%=actSizeName%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Actualsize")%> </TD>
            <TD class="CellBGR3"><%=actSize%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Reuse")%> </TD>
            <TD class="CellBGR3"><%=reuse%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.CreatedsizeUCP")%> </TD>
            <TD class="CellBGR3"><%=createdSize%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Sizedeviation")%> </TD>
            <TD class="CellBGR3"><%=deviation%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Description")%> </TD>
            <TD class="CellBGR3"><%=desc%></TD>
        </TR>
    </TBODY>
</TABLE>
<FORM name="frm" action="Fms1Servlet#modulelist">
<P>
<%if(right == 3 && !isArchive){%>
<INPUT type="button" class="BUTTON" onclick="onUpdate()" name="update" value=" <%=languageChoose.getMessage("fi.jsp.moduleView.Update")%> ">
<INPUT type="button" class="BUTTON" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.moduleView.Delete")%> " onclick="onDelete()">
<%}
if (schedRight>1){%>
<INPUT type="button" class="BUTTONWIDTH" value=" <%=languageChoose.getMessage("fi.jsp.moduleView.Viewscheduleinfo")%> " onclick="doIt('<%=Constants.SCHE_REVIEW_TEST_VIEW+"&vtID="+vtID%>')">
<%}%>
<%if (right == 3 && moduleInfo.getHasLoc() == 1){
	if (hasLoc == 1){%>
<INPUT type="button" class="BUTTON" name="UpdateLOC" value="<%=languageChoose.getMessage("fi.jsp.moduleView.UpdateLoc")%>" onclick="doUpdateLOC()">
<%}else{%>
<INPUT type="button" class="BUTTON" name="AddLOC" value="<%=languageChoose.getMessage("fi.jsp.moduleView.AddLoc")%>" onclick="doAddNewLOC()">
<%}}%>
<INPUT type="button" class="BUTTON" name="back" value=" <%=languageChoose.getMessage("fi.jsp.moduleView.Back")%> " onclick="doIt(<%=Constants.MODULE_LIST%>);">
</P>
<INPUT type="hidden" name="reqType">
<INPUT type="hidden" name="vtID" value="<%=vtID%>">
<INPUT type="hidden" name="productId" value="<%=moduleInfo.moduleID%>">
</FORM>
<SCRIPT language="javascript">
function onUpdate() {
	frm.reqType.value = <%=Constants.MODULE_UPDATE_PREP%>;
	frm.submit();
}
function onDelete() {
	if (window.confirm("<%= languageChoose.getMessage("fi.jsp.moduleView.Areyousuretodeletethismodule")%>") != 0) {
		frm.reqType.value = <%=Constants.MODULE_DELETE%>;
		frm.submit();
	}
}
function doAddNewLOC(){
	frm.reqType.value = <%=Constants.PRODUCT_LOC_ADD%>;
	frm.submit();
}
function doUpdateLOC(){
	frm.reqType.value = <%=Constants.PRODUCT_LOC_UPDATE%>;
	frm.submit();
}
</SCRIPT>
</BODY>
</HTML>