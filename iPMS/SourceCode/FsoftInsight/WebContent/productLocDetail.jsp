<%@page
	import="java.util.*, java.text.*, com.fms1.infoclass.*,com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*"
	errorPage="error.jsp"
	contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>productLocDetail.jsp</TITLE>
</HEAD><%
	LanguageChoose languageChoose = (LanguageChoose) session.getAttribute("LanguageChoose");
	int right = Security.securiPage("Size", request, response);

	ProductLocDetailInfo locDetail = (ProductLocDetailInfo) request.getAttribute("locDetail");
	WPSizeInfo moduleInfo = locDetail.getProductDetail();
	Vector actualLocList = locDetail.getActualLocs();
	Vector planLocList = locDetail.getPlanLocs();
    // The Total line of each table in the list
	ProductLocInfo totalLoc = new ProductLocInfo();
	String rowStyle;
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.productLocDetail.ProductAndSize")%> </P>
<TABLE class="Table" cellspacing="1" width="560">
    <COL span="1" width="160">
    <COL span="1" width="400">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.moduleView.Productsizeview")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Name")%> </TD>
            <TD class="CellBGR3"> <%=moduleInfo.name%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Workproduct")%> </TD>
            <TD class="CellBGR3"> <%=languageChoose.getMessage(moduleInfo.categoryName)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Plannedsizeunit")%> </TD>
            <TD class="CellBGR3"> <%=(moduleInfo.estimatedSizeUnitName != null) ? ConvertString.toHtml(moduleInfo.estimatedSizeUnitName) : "N/A"%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Plannedsize")%> </TD>
            <TD class="CellBGR3"> <%=CommonTools.formatDouble(moduleInfo.estimatedSize)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Replannedsize")%> </TD>
            <TD class="CellBGR3"> <%=CommonTools.formatDouble(moduleInfo.reestimatedSize)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Actualsizeunit")%> </TD>
            <TD class="CellBGR3"> <%=(moduleInfo.actualSizeUnitName != null) ? ConvertString.toHtml(moduleInfo.actualSizeUnitName) : "N/A"%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Actualsize")%> </TD>
            <TD class="CellBGR3"> <%=CommonTools.formatDouble(moduleInfo.actualSize)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Reuse")%> </TD>
            <TD class="CellBGR3"> <%=CommonTools.formatDouble(moduleInfo.reusePercentage)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.CreatedsizeUCP")%> </TD>
            <TD class="CellBGR3"> <%=CommonTools.formatDouble(moduleInfo.createdSize)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Sizedeviation")%> </TD>
            <TD class="CellBGR3"> <%=CommonTools.formatDouble(moduleInfo.deviation)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleView.Description")%> </TD>
            <TD class="CellBGR3"> <%=(moduleInfo.description != null) ? ConvertString.toHtml(moduleInfo.description) : "N/A"%> </TD>
        </TR>
    </TBODY>
</TABLE>
<BR>

<TABLE width="95%" cellspacing="1" class="table" id="table1">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.productLocPages.PlannedProductLOC")%> </CAPTION>
	<COLGROUP>
		<COL width="2%" align="center">
		<COL width="16%">
		<COL width="10%">
		<COL width="10%">
		<COL width="8%">
		<COL width="8%">
		<COL width="8%">
		<COL width="8%">
		<COL width="8%">
		<COL width="8%">
		<COL width="14%">
	</COLGROUP>
	<THEAD>
		<TR class="ColumnLabel">
			<TD> # </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Language")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.LOCForProductivitity")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.LOCForQuality")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.MotherBody")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Added")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Modified")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.TotalLOC")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.ReusedLOC")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.GeneratedLOC")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Note")%> </TD>
		</TR>
	</THEAD>
	<TBODY><%
	totalLoc.resetToZero();
	for (int i = 0; i < planLocList.size(); i++) {
		rowStyle = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		ProductLocInfo loc = (ProductLocInfo) planLocList.get(i);
		totalLoc.plus(loc);%>
		<TR class="<%=rowStyle%>">
			<TD> <%=i + 1%> </TD>
			<TD> <%=loc.getLanguageName()%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getLocProductivity())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getLocQuality())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getMotherBody())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getAdded())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getModified())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getTotal())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getReused())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getGenerated())%> </TD>
			<TD> <%=ConvertString.toHtml(loc.getNote())%> </TD>
		</TR>
		<%
	}
	%>
		<TR class="TableLeft">
			<TD></TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Total")%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getLocProductivity())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getLocQuality())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getMotherBody())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getAdded())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getModified())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getTotal())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getReused())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getGenerated())%> </TD>
			<TD></TD>
		</TR>
	</TBODY>
</TABLE>
<p>

<BR>

</p>

<TABLE width="95%" class="table" cellspacing="1" class="table2">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.productLocPages.ActualProductLOC")%> </CAPTION>
	<COLGROUP>
		<COL width="2%" align="center">
		<COL width="16%">
		<COL width="10%">
		<COL width="10%">
		<COL width="8%">
		<COL width="8%">
		<COL width="8%">
		<COL width="8%">
		<COL width="8%">
		<COL width="8%">
		<COL width="14%">
	</COLGROUP>
	<THEAD>
		<TR class="ColumnLabel">
			<TD> # </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Language")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.LOCForProductivitity")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.LOCForQuality")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.MotherBody")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Added")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Modified")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.TotalLOC")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.ReusedLOC")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.GeneratedLOC")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Note")%> </TD>
		</TR>
	</THEAD>
	<TBODY><%
	totalLoc.resetToZero();
	for (int i = 0; i < actualLocList.size(); i++) {
		rowStyle = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		ProductLocInfo loc = (ProductLocInfo) actualLocList.get(i);
		totalLoc.plus(loc);%>
		<TR class="<%=rowStyle%>">
			<TD> <%=i + 1%> </TD>
			<TD> <%=loc.getLanguageName()%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getLocProductivity())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getLocQuality())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getMotherBody())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getAdded())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getModified())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getTotal())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getReused())%> </TD>
			<TD> <%=CommonTools.formatDouble(loc.getGenerated())%> </TD>
			<TD> <%=ConvertString.toHtml(loc.getNote())%> </TD>
		</TR>
		<%
	}
	%>
		<TR class="TableLeft">
			<TD></TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Total")%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getLocProductivity())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getLocQuality())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getMotherBody())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getAdded())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getModified())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getTotal())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getReused())%> </TD>
			<TD> <%=CommonTools.formatDouble(totalLoc.getGenerated())%> </TD>
			<TD></TD>
		</TR>
	</TBODY>
</TABLE>
<FORM name="frm" method="POST" action="Fms1Servlet" >
<P>
<%
if (right == 3) {
%>
<INPUT type="button" class="BUTTON" onclick="onUpdateLOC()" name="update" value=" <%=languageChoose.getMessage("fi.jsp.button.Update")%> "><%
}
%>
<INPUT type="button" class="BUTTON" name="back" value=" <%=languageChoose.getMessage("fi.jsp.button.Back")%> " onclick="doIt(<%=Constants.PRODUCT_LOC_LIST%>)">
</P>
<INPUT type="hidden" name="reqType" value = "<%=Constants.PRODUCT_LOC_UPDATE%>">
<INPUT type="hidden" name="productId" value="<%=moduleInfo.moduleID%>">
</FORM>
<BR>

<SCRIPT language="javascript">
function onUpdateLOC() {
	document.frm.submit();
}
</SCRIPT>
</BODY>
</HTML>
