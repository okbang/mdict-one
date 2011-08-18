<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@include file="javaFns.jsp"%>
</SCRIPT>
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>moduleList.jsp</TITLE>
</HEAD>
<%
LanguageChoose languageChoose = (LanguageChoose) session.getAttribute("LanguageChoose");
//HuyNH2 add code for project archive
String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
	int right = Security.securiPage("Size", request, response);
	Vector moduleList = (Vector) session.getAttribute("moduleList");
	Vector WPSizeSumList = (Vector) session.getAttribute("WPSizeSumList");
	Vector vtWorkProductInfo = (Vector) session.getAttribute("workproductList");
	ProjectSizeInfo sizeInfo = (ProjectSizeInfo) session.getAttribute("projectSizeInfo");
	int numProductlist = moduleList.size();

	String scroll1 = "";
	String scroll2 = "";
	String selectedWorkProduct = null;
	String requestSelectedWorkProduct = request.getParameter("selWorkProduct");
	String sessionSelectedWorkProduct = (String)session.getAttribute("sessionSelectedWorkProduct");

	if (requestSelectedWorkProduct == null){
		if (sessionSelectedWorkProduct != null){
			selectedWorkProduct = sessionSelectedWorkProduct;
		}
		else{
			selectedWorkProduct = null;
		}
	} else {
    	session.setAttribute("sessionSelectedWorkProduct", requestSelectedWorkProduct);
    	selectedWorkProduct = requestSelectedWorkProduct;
    }

	long lselectedWorkProduct = (selectedWorkProduct == null || "".equals(selectedWorkProduct))?0:CommonTools.parseLong(selectedWorkProduct);
	int viewProductList = 0;// count actual products will be viewed in the product list
	int numPaperworklist = 0;

	if (numProductlist > 15){
		for(int i = 0; i < numProductlist; i++ ){
			WPSizeInfo module = (WPSizeInfo) moduleList.elementAt(i);
			if ((module.workProductID == lselectedWorkProduct)||(lselectedWorkProduct == 0)){
				viewProductList++;
				if (module.isDocument){
					numPaperworklist++;
				}
			}
		}
	}
	if (viewProductList > 15){
		scroll1 = "makeScrollableTable('tableProduct1',true,'200')";
	}
	if (numPaperworklist > 15){
		scroll2 = "makeScrollableTable('tableProduct2',true,'200')";
	}

%>

<BODY onload="loadPrjMenu();<%=scroll1%>;<%=scroll2%>" class="BD">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.moduleList.Productsandsize")%> </P>
<br>
<FORM name="frm_Import" action="Fms1Servlet?reqType=<%=Constants.MODULE_IMPORT%>" enctype="MULTIPART/FORM-DATA" method=POST >
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
	        <TD align="center"><A href="Template_Import_PRODUCT.xls"><FONT color="blue" class="label1">Download Template File</FONT></A></TD>
	    </TR>
	</TBODY>
</TABLE>
</FORM>

<br>
<%
String checkImport = (String)session.getAttribute("Imported");
if(checkImport == "true"){
	int[] result = (int[])session.getAttribute("AddedRecord");
	int importedOK = 0;
	int importedFail = 0;
	int l = 0;
	while(l < 50){
			if(result[l] > 0){
				importedOK ++;
			}
			l++;
		}
	if(importedOK > 0){
	%><br>Imported successfull records:<%
		l=0;
		importedOK =0;
		importedFail =0;
		while(l < 50){
			if(result[l] > 0){
				if(importedOK > 0){
					%> ,<%
				}
				%>
				<%=result[l]%>
				<%
				importedOK ++;
			}else if(importedOK < 0){
				importedFail++;
			}
			l++;
		}
}	
	  %>
	  <br>
	  <br>
	  <%
	if(importedOK == 0){
	%><br>0 record is imported
	  <br>
	  <br>
	<%
	}
	if(importedFail > 0 && importedOK > 0){
	%><br>There are some records is failed
	  <br>
	  <br>
	<%
	}	
	session.removeAttribute("Imported");
	session.removeAttribute("AddedRecord");
}	
%>
<%
String isImport = (String)session.getAttribute("ImportFail");
if(isImport == "fail"){
	%><br><p style="color: red;">Import Fail<%	
	session.removeAttribute("ImportFail");
}
%>

<TABLE class="HDR">
	<TBODY>
		<TR>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Plannedsize")%></TD>
			<TD style="text-align:right"> <%=CommonTools.formatDouble(sizeInfo.totalEstimatedSize)%> </TD>
			<TD> UCP </TD>
		</TR>
		<TR>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Replannedsize")%> </TD>
			<TD style="text-align:right"> <%=CommonTools.formatDouble(sizeInfo.totalReestimatedSize)%> </TD>
			<TD> UCP </TD>
		</TR>
		<TR>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Totalactualsize")%> </TD>
			<TD style="text-align:right"> <%=CommonTools.formatDouble(sizeInfo.totalActualSize)%> </TD>
			<TD> UCP </TD>
		</TR>
		<TR>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Totalcreatedsize")%> </TD>
			<TD style="text-align:right"> <%=CommonTools.formatDouble(sizeInfo.totalCreatedSize)%> </TD>
			<TD> UCP </TD>
		</TR>
		<TR>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Sizedeviation1")%> </TD>
			<TD style="text-align:right"> <%=CommonTools.formatDouble(sizeInfo.totalSizeDeviation)%> </TD>
			<TD> % </TD>
		</TR>
	</TBODY>
</TABLE>

<BR>

<TABLE width="95%" cellspacing="1" class="table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.moduleList.Sizetrackingbystage")%> </CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Stages")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.PlannedsizeUCP")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.ReplannedsizeUCP")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.ActualsizeUCP")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Reuse")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.CreatedsizeUCP")%>	</TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Sizedeviation2")%>	</TD>
		</TR><%	
			double totalEstimated = Double.NaN;
			double totalReestimated = Double.NaN;
			double totalActual = Double.NaN;
			double totalCreated = Double.NaN;
			
			String className = "CellBGRnews";
			
			if (WPSizeSumList != null)
				for (int i = 0; i < WPSizeSumList.size(); i++) 
				{
					WPSizeInfo wpSize = (WPSizeInfo) WPSizeSumList.elementAt(i);
					className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";

					if (!Double.isNaN(wpSize.estimatedSize)) 
					{
						if (Double.isNaN(totalEstimated))
							totalEstimated = 0;
						totalEstimated += wpSize.estimatedSize;
					}
				
					if (!Double.isNaN(wpSize.reestimatedSize)) 
					{
						if (Double.isNaN(totalReestimated))
							totalReestimated = 0;
						totalReestimated += wpSize.reestimatedSize;
					}
			
					if (!Double.isNaN(wpSize.actualSize)) 
					{
						if (Double.isNaN(totalActual))
							totalActual = 0;
						totalActual += wpSize.actualSize;
					}
		
					if (!Double.isNaN(wpSize.createdSize)) 
					{
						if (Double.isNaN(totalCreated))
							totalCreated = 0;
						totalCreated += wpSize.createdSize;
					}
		%>
		<TR class="<%=className%>" style="text-align:left">
			<TD> <%=wpSize.name%> </TD>
			<TD> <%=CommonTools.formatDouble(wpSize.estimatedSize)%> </TD>
			<TD> <%=CommonTools.formatDouble(wpSize.reestimatedSize)%> </TD>
			<TD> <%=CommonTools.formatDouble(wpSize.actualSize)%> </TD>
			<TD> <%=CommonTools.formatDouble(wpSize.reusePercentage)%> </TD>
			<TD> <%=CommonTools.formatDouble(wpSize.createdSize)%> </TD>
			<TD> <%=CommonTools.formatDouble(wpSize.deviation)%> </TD>
		</TR><%	
				}
		%>
		<TR class="TableLeft">
			<TD><B> <%=languageChoose.getMessage("fi.jsp.moduleList.Total")%> </B></TD>
			<TD><B> <%=CommonTools.formatDouble(totalEstimated)%> </B></TD>
			<TD><B> <%=CommonTools.formatDouble(totalReestimated)%> </B></TD>
			<TD><B> <%=CommonTools.formatDouble(totalActual)%> </B></TD>
			<TD></TD>
			<TD><B> <%=CommonTools.formatDouble(totalCreated)%> </B></TD>
			<TD></TD>
		</TR>
	</TBODY>
</TABLE>
<BR>
<FORM name="frm1" action="moduleList.jsp">
<P class="TableCaption">
<A name="moduleList"> <%=languageChoose.getMessage("fi.jsp.moduleList.Productlist")%> </A>
<SELECT name="selWorkProduct" class="combo">
	<OPTION value="0" <%=((lselectedWorkProduct==0)?"selected":"")%>>
		<%=languageChoose.getMessage("fi.jsp.moduleList.WorkProduct.All")%>
	</OPTION><%
		int wSize = vtWorkProductInfo.size();
		for (int i = 0; i < wSize; i++)
		{
			WorkProductInfo workProductInfo = (WorkProductInfo) vtWorkProductInfo.elementAt(i);
	%>
	<OPTION value="<%=workProductInfo.workProductID%>" <%=((workProductInfo.workProductID == lselectedWorkProduct)? " selected" : "")%>>
		<%=workProductInfo.workProductName%>
	</OPTION><%
		}
	%>
</SELECT>
<INPUT type="submit" class="button" value="<%=languageChoose.getMessage("fi.jsp.moduleList.View")%>">
</P>
</FORM>
<TABLE class="table" width="95%" cellspacing="1" id="tableProduct1">
	<COL span="1" width="24">
	<THEAD>
		<TR class="ColumnLabel">
			<TD><INPUT id="selectAll" type ="checkbox" name="selectAll" onclick="selectAll()"/></TD>
			<TD style="text-align:center" width="24"> # </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Name")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Workproduct")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.PlannedsizeUCP")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.ReplannedsizeUCP")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.ActualsizeUCP")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.CreatedsizeUCP")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Description")%> </TD>
		</TR>
	</THEAD>
	<TBODY><%
			int j = 0;
			if (numProductlist > 0)
				for (int i = 0; i < numProductlist; i++) 
				{
					WPSizeInfo module = (WPSizeInfo) moduleList.elementAt(i);
					className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";					
		
					if ((module.workProductID == lselectedWorkProduct)||(lselectedWorkProduct == 0))
					{
		%>
		<TR class="<%=className%>">
			<TD> <INPUT type ="checkbox" name="prodCheck"/></TD>
			<TD style="text-align:center"> <%=++j%> </TD>
			<TD>
				<A href="Fms1Servlet?reqType=<%=Constants.MODULE_DETAIL%>&vtID=<%=i%>"> <%=module.name%> </A>
				<INPUT type ="hidden" name = "moduleID" value = "<%=module.moduleID%>"/>
			</TD>
			<TD> <%=languageChoose.getMessage(module.categoryName)%> </TD>
			<TD> <%=CommonTools.formatDouble(module.estimatedSizeConv)%> </TD>
			<TD> <%=CommonTools.formatDouble(module.reestimatedSizeConv)%> </TD>
			<TD> <%=CommonTools.formatDouble(module.actualSizeConv)%> </TD>
			<TD> <%=CommonTools.formatDouble(module.createdSize)%> </TD>
			<TD> <%=module.description != null ? module.description : "N/A"%> </TD>
		</TR><%		
					}
				}
		%>
	</TBODY>
	<TFOOT>
		<TR>
			<TD colspan="9" class="TableLeft" align="right"><%=(numProductlist > 0) ? "&nbsp;" : ""%></TD>
		</TR>
	</TFOOT>
</TABLE>
<BR>
<FORM name="frm2" action="Fms1Servlet#moduleList" method ="post">
<INPUT type="hidden" name="reqType"><% 
	if (right == 3 && !isArchive){
%> 
		<INPUT type="button" class="button"	name="addnew" value="<%=languageChoose.getMessage("fi.jsp.moduleList.Addnew")%>" onclick = "return addModule();"><%	
	
		if (wSize > 0) {
%>
	<input type="hidden" name="listChecked" value="0" >
	<input type="button" name="moModuleUpdate" value="<%=languageChoose.getMessage("fi.jsp.moduleList.Update")%>" class = "BUTTON" onclick=" return doBatchAction('update');">
	<input type="button" name="moModuleDelete" value="<%=languageChoose.getMessage("fi.jsp.moduleList.Delete")%>" class = "BUTTON" onclick=" return doBatchAction('delete')">
<% 		
		}
	} 
%>
</FORM>

<BR>

<P class="TableCaption">
<A name="modulelist"> <%=languageChoose.getMessage("fi.jsp.moduleList.Paperworklist")%> </A>
</P>

<TABLE class="table" width="95%" cellspacing="1" id="tableProduct2">
	<COL span="1" width="24">
	<THEAD>
		<TR class="ColumnLabel">
			<TD style="text-align:center"> # </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Name")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Workproduct")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Plannedsizepagesheet")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Replannedsizepagesheet")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Actualsizepagesheet")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Createdsizepagesheet")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleList.Description")%> </TD>
		</TR>
	</THEAD>
	<TBODY><%	
			int k = 0;
			for (int i = 0; i < numProductlist; i++) 
			{
				WPSizeInfo module = (WPSizeInfo) moduleList.elementAt(i);
				className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
	
				if (module.isDocument) 
				{
					if ((module.workProductID == lselectedWorkProduct)||(lselectedWorkProduct == 0))
					{
		%>
		<TR class="<%=className%>">
			<TD style="text-align:center"> <%=++k%> </TD>
			<TD><A href="Fms1Servlet?reqType=<%=Constants.MODULE_DETAIL%>&vtID=<%=i%>"> <%=module.name%> </A></TD>
			<TD> <%=languageChoose.getMessage(module.categoryName)%> </TD>
			<TD> <%=CommonTools.formatDouble(module.estimatedSize)%> </TD>
			<TD> <%=CommonTools.formatDouble(module.reestimatedSize)%> </TD>
			<TD> <%=CommonTools.formatDouble(module.actualSize)%> </TD>
			<TD> <%=CommonTools.formatDouble(module.createdSizeOrigin)%> </TD>
			<TD> <%=module.description != null ? module.description : "N/A"%> </TD>
		</TR><%	
					}
				}
			}
		%>
	</TBODY>
	<TFOOT>
		<TR>
			<TD colspan="8" class="TableLeft" align="right"><%=(numProductlist > 0) ? "&nbsp;" : ""%></TD>
		</TR>
	</TFOOT>
</TABLE>
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
    
    function selectAll(){
		var uCheck = document.getElementsByName("prodCheck");
		
		for (i = 0; i < uCheck.length; i++) {
			uCheck[i].checked = document.getElementById("selectAll").checked;
		}
	}
    
    function addModule() {
    	frm2.reqType.value = "<%=Constants.BATCH_MODULE_PREPARE_ADD%>";
		frm2.submit();
    }
    
    function doBatchAction(action) {
		var uCheck = document.getElementsByName("prodCheck");
		var idList = document.getElementsByName("moduleID");
		
		var uList = "";
		var devList = "";
		var uLength = uCheck.length;
		var nChecked = 0; 
		
		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";				
				nChecked = 1;				
			}
		}
		
		if (nChecked == 0) {
			alert("Please choose data to " + action);
			return false;
		} else {
			uList = uList.substring(0,uList.length-1);
			frm2.listChecked.value = uList;
			
			if (action == "delete") {
				if (confirm("Do you really want to delete ?")) {
					frm2.reqType.value = "<%=Constants.BATCH_MODULE_DELETE%>";
					frm2.submit();
				} else return false;
			} else if (action =="update") {
				frm2.reqType.value = "<%=Constants.BATCH_MODULE_PREPARE_UPDATE%>";
				frm2.submit();
			}
			else return false;
		}
	}
</SCRIPT>

<SCRIPT language="javascript">
function doImport(ext) {
    if (ext != '') {
        document.frm_Import.submit();
    }
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
</SCRIPT>

<BR>
<SCRIPT language="javascript">
//objs to hide when submenu is displayed
var objToHide = new Array(frm1.selWorkProduct);
</SCRIPT>
<SCRIPT>
	Import();
</SCRIPT>
</BODY>
</HTML>