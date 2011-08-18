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
<TITLE>productLocList.jsp</TITLE>
</HEAD><%
	LanguageChoose languageChoose = (LanguageChoose) session.getAttribute("LanguageChoose");
	int right = Security.securiPage("Size", request, response);

	WPSizeInfo projectLocSummary = (WPSizeInfo) request.getAttribute("projectLocSummary");
	Vector projectLocByStage = (Vector) request.getAttribute("projectLocByStage");
	Vector projectLanguages = (Vector) request.getAttribute("projectLanguages");
	Vector productSizeList = (Vector) request.getAttribute("productSizeList");
    Integer productsWithoutLoc = (Integer) request.getAttribute("productsWithoutLoc");
    // The Total line of each table in the list
	ProductLocInfo totalLoc = new ProductLocInfo();
	String scrollMethod = "";
	//if (numProductlist > 15) {
	//	scrollMethod = "makeScrollableTable('tableStageLOC',true,'200')";
	//}
	String rowStyle;
%>
<BODY onload="loadPrjMenu();<%=scrollMethod%>" class="BD">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.productLocList.ProductsSizeLoc")%> </P>
<BR>

<TABLE class="HDR">
	<TBODY>
		<TR>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocList.PlannedSize")%> </TD>
			<TD style="text-align:right"> <%=CommonTools.formatDouble(projectLocSummary.estimatedSize)%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.LOC")%> </TD>
		</TR>
		<!--TR> ### This value is removed based on user CR (to avoid confuse)
			<TD> <!%=languageChoose.getMessage("fi.jsp.productLocList.ReplannedSize")%> </TD>
			<TD style="text-align:right"> <!%=CommonTools.formatDouble(projectLocSummary.reestimatedSize)%> </TD>
			<TD> <!%=languageChoose.getMessage("fi.jsp.productLocPages.LOC")%> </TD>
		</TR-->
		<TR>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocList.TotalActualSize")%> </TD>
			<TD style="text-align:right"> <%=CommonTools.formatDouble(projectLocSummary.actualSize)%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.LOC")%> </TD>
		</TR>
		<!--TR> ### This value is removed based on user CR
			<TD> <!%=languageChoose.getMessage("fi.jsp.productLocList.TotalCreatedSize")%> </TD>
			<TD style="text-align:right"> <!%=CommonTools.formatDouble(projectLocSummary.createdSize)%> </TD>
			<TD> <!%=languageChoose.getMessage("fi.jsp.productLocPages.LOC")%> </TD>
		</TR-->
		<TR>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocList.SizeDeviation")%> </TD>
			<TD style="text-align:right"> <%=CommonTools.formatDouble(CommonTools.metricDeviation(projectLocSummary.estimatedSize, projectLocSummary.reestimatedSize, projectLocSummary.actualSize))%> </TD>
			<TD> % </TD>
		</TR>
	</TBODY>
</TABLE>

<BR>

<P class="TableCaption">
<A name="trackingByStageList"> <%=languageChoose.getMessage("fi.jsp.productLocList.SizeTrackingByStage")%> </A>
</P>
<TABLE width="95%" cellspacing="1" class="table" id="tableStageLOC">
	<COLGROUP>
		<COL width="3%" align="center">
		<COL width="17%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
	</COLGROUP>
	<THEAD>
		<TR class="ColumnLabel">
			<TD> # </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocList.Stage")%>.<BR>
			<%=languageChoose.getMessage("fi.jsp.productLocList.Language")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.LOCForProductivitity")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.LOCForQuality")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.MotherBody")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Added")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.Modified")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.TotalLOC")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.ReusedLOC")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocPages.GeneratedLOC")%> </TD>
		</TR>
	</THEAD>
	<TBODY><%
	String stageId;
	totalLoc.resetToZero();
	for (int iStage = 0; iStage < projectLocByStage.size(); iStage++) {
	    ProductLocOfStageInfo stageLoc = (ProductLocOfStageInfo) projectLocByStage.get(iStage);
	    ProductLocInfo stageSummary = stageLoc.getStageSummary();
	    totalLoc.plus(stageSummary);
	    Vector languageLocs = stageLoc.getLanguageLocs();
	    rowStyle = (iStage % 2 == 0) ? "CellBGRnews" : "CellBGR3";
	    stageId = "stage" + (iStage + 1);
	%>
		<TR class="<%=rowStyle%>" style="font:bold"><%
		    if (languageLocs.size() > 0) {    // If there are languages in this stage then show collapse/expanse icon
    		%>
			<TD style="cursor:hand" id="<%=stageId%>" onclick="javascript:collapseExpand('<%=stageId%>', '<%=stageId%>_icon', <%=languageLocs.size()%>, '<%=stageId%>_OpenClose')">
    			<img border="0" id="<%=stageId%>_icon" src="image/IconPlus.gif" width="16" height="16">
	    		<INPUT type="hidden" name="<%=stageId%>_OpenClose" id="<%=stageId%>_OpenClose" value="+">
			</TD><%
			}
			else {
			%>
			<TD>
			</TD><%
			}%>
			<TD> <%=stageSummary.getLanguageName()%> </TD>
			<TD> <%=CommonTools.formatDouble(stageSummary.getLocProductivity())%> </TD>
			<TD> <%=CommonTools.formatDouble(stageSummary.getLocQuality())%> </TD>
			<TD> <%=CommonTools.formatDouble(stageSummary.getMotherBody())%> </TD>
			<TD> <%=CommonTools.formatDouble(stageSummary.getAdded())%> </TD>
			<TD> <%=CommonTools.formatDouble(stageSummary.getModified())%> </TD>
			<TD> <%=CommonTools.formatDouble(stageSummary.getTotal())%> </TD>
			<TD> <%=CommonTools.formatDouble(stageSummary.getReused())%> </TD>
			<TD> <%=CommonTools.formatDouble(stageSummary.getGenerated())%> </TD>
		</TR><%
		for (int iLang = 0; iLang < languageLocs.size(); iLang++) {
			ProductLocInfo loc = (ProductLocInfo) languageLocs.get(iLang);
			%>
			<TR class="<%=rowStyle%>" style="display:none" id="<%=stageId%>_<%=iLang + 1%>">
				<TD> <%=iLang + 1%> </TD>
				<TD> <%=loc.getLanguageName()%> </TD>
				<TD> <%=CommonTools.formatDouble(loc.getLocProductivity())%> </TD>
				<TD> <%=CommonTools.formatDouble(loc.getLocQuality())%> </TD>
				<TD> <%=CommonTools.formatDouble(loc.getMotherBody())%> </TD>
				<TD> <%=CommonTools.formatDouble(loc.getAdded())%> </TD>
				<TD> <%=CommonTools.formatDouble(loc.getModified())%> </TD>
				<TD> <%=CommonTools.formatDouble(loc.getTotal())%> </TD>
				<TD> <%=CommonTools.formatDouble(loc.getReused())%> </TD>
				<TD> <%=CommonTools.formatDouble(loc.getGenerated())%> </TD>
			</TR><%
		}
	}%>
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
		</TR>
	</TBODY>
</TABLE>

<BR>

<TABLE width="95%" cellspacing="1" class="table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.productLocList.ProjectLOC")%> </CAPTION>
	<COLGROUP>
		<COL width="3%" align="center">
		<COL width="17%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
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
		</TR>
	</THEAD>
	<TBODY><%
	totalLoc.resetToZero();
	for (int i = 0; i < projectLanguages.size(); i++) {
	    rowStyle = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
	    ProductLocInfo loc = (ProductLocInfo) projectLanguages.get(i);
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
		</TR><%
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
		</TR>
	</TBODY>
</TABLE>

<BR>

<TABLE class="table" width="95%" cellspacing="1" id="tableProduct1">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.productLocList.ProductList ")%> </CAPTION>
	<COLGROUP>
		<COL width="2%" align="center">
		<COL width="25%">
		<COL width="18%">
		<COL width="18%">
		<COL width="18%">
		<!--COL width="15%" ### This value is removed based on user CR -->
		<COL width="18%">
	</COLGROUP>
	<THEAD>
		<TR class="ColumnLabel">
			<TD> # </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocList.Name")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocList.WorkProduct")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocList.PlannedLOCForProductivity")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocList.ActualLOCForProductivity")%> </TD>
			<!--TD> <!%=languageChoose.getMessage("fi.jsp.productLocList.CreatedLOCForProductivity")%> </TD-->
			<TD> <%=languageChoose.getMessage("fi.jsp.productLocList.Deviation")%> </TD>
		</TR>
	</THEAD>
	<TBODY><%
	for (int i = 0; i < productSizeList.size(); i++) {
	    rowStyle = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
	    WPSizeInfo productLoc = (WPSizeInfo) productSizeList.get(i);
	    %>
		<TR class="<%=rowStyle%>">
			<TD> <%=i + 1%> </TD>
			<TD><A href="javascript:showProduct(<%=i%>)"> <%=productLoc.name%> </A></TD>
			<TD> <%=ConvertString.toHtml(productLoc.categoryName)%> </TD>
			<TD> <%=CommonTools.formatDouble(productLoc.estimatedSize)%> </TD>
			<TD> <%=CommonTools.formatDouble(productLoc.actualSize)%> </TD>
			<!--TD> <!%=CommonTools.formatDouble(productLoc.createdSize)%> </TD-->
			<TD> <%=CommonTools.formatDouble(CommonTools.metricDeviation(productLoc.estimatedSize, productLoc.reestimatedSize, productLoc.actualSize))%> </TD>
		</TR>
	    <%
	}
	%>
	</TBODY>
	<TFOOT>
		<TR>
			<TD colspan="6" class="TableLeft" align="right"><%=(productSizeList.size() > 0) ? "&nbsp;" : ""%></TD>
		</TR>
	</TFOOT>
</TABLE>

<BR><%
	if (right == 3) {	// Enable for admin, disable for viewers
%>
<FORM name="frm" action="Fms1Servlet">
	<INPUT type="hidden" name="reqType" value="">
	<INPUT type="hidden" name="product" value=""><%
	if (productsWithoutLoc.intValue() > 0) {
	%>
	<INPUT type="button" class="button"	name="addnew" onclick="onAddNewLOC()" value="<%=languageChoose.getMessage("fi.jsp.button.AddNew")%>"><%
	}%>
</FORM><%
	}
%>

<BR>

<SCRIPT language="javascript">
//objs to hide when submenu is displayed
//var objToHide = new Array(frm1.selWorkProduct);
function onAddNewLOC() {
    document.frm.reqType.value = "<%=Constants.PRODUCT_LOC_ADD%>";
    document.frm.submit();
}
var products = new Array();
<%
for (int i = 0; i < productSizeList.size(); i++) {
    WPSizeInfo productLoc = (WPSizeInfo) productSizeList.get(i);
    out.write("products[" + i + "]=" + productLoc.moduleID + ";");
}
%>
function showProduct(i) {
    document.frm.reqType.value = "<%=Constants.PRODUCT_LOC_DETAIL%>";
	document.frm.product.value = products[i];
    document.frm.submit();
}
</SCRIPT>
</BODY>
</HTML>