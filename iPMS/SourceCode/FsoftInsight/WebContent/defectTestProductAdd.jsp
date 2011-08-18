<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<LINK rel="stylesheet" type="text/css" href="stylesheet/fms.css">
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>defectTestProductAdd.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	int maxRow = Defect.NUMBER_OF_ROW_ADDABLE;
	boolean isOver = false;
	int nDisplayed;
	String rowStyle;
	int iCount = 0;
	
	WPSizeInfo moduleInfo = null;	
	
	Vector vErr = (Vector) request.getAttribute("ErrDefectTestProductList");
	if (vErr != null) {
		isOver = true;
		nRow = vErr.size();
		request.removeAttribute("ErrDefectTestProductList");
	}	
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
	
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	Vector reqProduct = (Vector) session.getAttribute("RequirementProductList");
	Vector designProduct = (Vector) session.getAttribute("DesignProductList");
	Vector codeProduct = (Vector) session.getAttribute("CodingProductList");
	Vector otherProduct = (Vector) session.getAttribute("OtherProductList");
	String fromPage = (String) request.getParameter("fromPage");
	if (fromPage == null) fromPage = "";
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<form name ="frmdefectTestProductAdd" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.DefectAddProductForTest")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.TestProducts")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="24" align = "center">#</TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Process")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Product")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.PlannedFoundByTest")%></TD>			
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)   moduleInfo = (WPSizeInfo) vErr.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD width="24" align = "center"> <%=row + 1%> </TD>
			<TD>
				<SELECT name="process" class="COMBO" onchange="SelectProcess('<%=row%>');" style="width:100px">
					<OPTION value = "0">&nbsp;</OPTION>
					<OPTION value = "1"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Requirement")%> </OPTION>
					<OPTION value = "2"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Design")%> </OPTION>
					<OPTION value = "3"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Coding")%> </OPTION>
					<OPTION value = "4"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Other")%> </OPTION>
				</SELECT>
			</TD>
			<TD>
			 	<SELECT name="product" class="COMBO" style="width:350px">
			 		<OPTION value ="0">&nbsp;</OPTION>
				</SELECT>
			</TD>
			<TD  align = "center">
			 <INPUT type ="text" size = "18" maxlength ="18" name="planSize" class="SmallTextbox" value=""/>
			</TD>
		</TR>
<%	
	}
	nDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD width="24" align = "center"> <%=row + 1%> </TD>
			<TD>
				<SELECT name="process" class="COMBO" onchange="SelectProcess('<%=row%>');" style="width:100px">
					<OPTION value = "0">&nbsp;</OPTION>
					<OPTION value = "1"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Requirement")%> </OPTION>
					<OPTION value = "2"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Design")%> </OPTION>
					<OPTION value = "3"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Coding")%> </OPTION>
					<OPTION value = "4"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Other")%> </OPTION>
				</SELECT>
			</TD>
			<TD>
			 	<SELECT name="product" class="COMBO" style="width:350px">
					<OPTION value ="0">&nbsp;</OPTION>
				</SELECT>
			</TD>
			<TD align = "center">
			 <INPUT type ="text" size = "18" maxlength ="18" name="planSize" class="SmallTextbox" value=""/>
			</TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.AddMoreTestProduct")%> </a></p>
<BR>
<input type ="hidden" name = "reqType"/>
<input type ="hidden" name = "fromPage" value = "<%=fromPage%>"/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.defectTestProductAdd.Cancel")%>" class="BUTTON" onclick="<%="defect".equals(fromPage) ? "jumpURL('DefectView.jsp');" : "jumpURL('qualityObjective.jsp');"%>">
</form>

<SCRIPT language="JavaScript">

var nextHiddenIndex = <%=nDisplayed + 1%>;
function addMoreRow() {
     getFormElement("_row" + nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 nextHiddenIndex++;
	 if(nextHiddenIndex > <%=maxRow%>)
	     getFormElement("addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(nextHiddenIndex > <%=maxRow%>) 
       getFormElement("addMoreLink").style.display = "none";    
}

var reqProduct = new Array();
var reqProductID = new Array();
<% 	
	for (int i = 0; i < reqProduct.size(); i++) { 
		WPSizeInfo mInfo = (WPSizeInfo) reqProduct.elementAt(i);
		if (mInfo.isDefectTest==0 && !mInfo.isDocument) {			
%>
			reqProduct[<%=iCount%>] = "<%=mInfo.name%>";
			reqProductID[<%=iCount%>] = <%=mInfo.moduleID%>;
<% 
			iCount++;
		}
	} 
%>
var designProduct = new Array();
var designProductID = new Array();
<% 
	iCount = 0;
	for (int i = 0; i < designProduct.size(); i++) { 
		WPSizeInfo mInfo = (WPSizeInfo) designProduct.elementAt(i);
		if (mInfo.isDefectTest==0 && !mInfo.isDocument) {
			
%>
			designProduct[<%=iCount%>] = "<%=mInfo.name%>";
			designProductID[<%=iCount%>] = <%=mInfo.moduleID%>;
<% 
			iCount++;
		}
	} 
%>

var codeProduct = new Array();
var codeProductID = new Array();
<% 
	iCount = 0;	
	for (int i = 0; i < codeProduct.size(); i++) { 
		WPSizeInfo mInfo = (WPSizeInfo) codeProduct.elementAt(i);
		if (mInfo.isDefectTest==0 && !mInfo.isDocument) {			
%>
			codeProduct[<%=iCount%>] = "<%=mInfo.name%>";
			codeProductID[<%=iCount%>] = <%=mInfo.moduleID%>;
<% 
			iCount++;
		}
	} 
%>

var otherProduct = new Array();
var otherProductID = new Array();
<% 
	iCount = 0;
	for (int i = 0; i < otherProduct.size(); i++) { 
		WPSizeInfo mInfo = (WPSizeInfo) otherProduct.elementAt(i);
		if (mInfo.isDefectTest==0 && !mInfo.isDocument) {			
%>			
			otherProduct[<%=iCount%>] = "<%=mInfo.name.substring(0,mInfo.name.indexOf("\r\n")==-1?mInfo.name.length():mInfo.name.indexOf("\r\n"))%>";
			otherProductID[<%=iCount%>] = <%=mInfo.moduleID%>;
<% 
			iCount++;
		}
	} 
%>

function addSubmit(){	
	if (checkValid()) {
		frmdefectTestProductAdd.reqType.value=<%=Constants.DEFECT_TEST_PRODUCT_ADD%>;
		frmdefectTestProductAdd.submit();
	} else return false;	
}

function checkValid(){
	var arrTxt= document.getElementsByName("process");
	var arrTxt1= document.getElementsByName("product");
	var arrTxt2= document.getElementsByName("planSize");
	var checkAllBlank = 0;
	
	var length = nextHiddenIndex-1;	
	
	for(i=0; i < length;i++) {
		if  (   arrTxt[i].value =='0'
			&& arrTxt1[i].value =='0' 
			&& trim(arrTxt2[i].value) ==''
		) 
		{
			checkAllBlank++;				
		} else {
			if (arrTxt[i].value =='0')  {
				alert("Please input process");
				arrTxt[i].focus();
				return false;
			}
			
			if (arrTxt1[i].value =='0')  {
				alert("Please input product");
				arrTxt1[i].focus();
				return false;
			}
			
			// check if plan size is a number
			if(isNaN(arrTxt2[i].value) && (trim(arrTxt2[i].value) != "")){
				alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.TheValueMustBeANumber")%>");  			
	  			arrTxt2[i].focus();
	  			return false;  
	  		}
	  		
	  		idx=contains(arrTxt1,arrTxt1[i],i,length-1);
			if (idx != -1) {
				alert("Duplicate product !");
				arrTxt1[idx].focus();
				return false;
			}
	  	}	  
	}
	
	if (checkAllBlank==length) {
		alert("Please input data");
		arrTxt[0].focus();
		return false;
	}	
	return true;
}

	function SelectProcess(j)
	{
		var processValue = document.getElementsByName("process")[j].value;
		
		for (var i = document.getElementsByName("product")[j].length; i >= 1; i--){
	        document.getElementsByName("product")[j].options[i] = null;
	    }
	    
		switch (processValue) {
			case "1":
				for (var i = 0; i < reqProduct.length; i++)
		    	{	
		    		myElement = document.createElement("option");
		            myElement.value = reqProductID[i];
		            myElement.text = reqProduct[i];
		            document.getElementsByName("product")[j].add(myElement);
		        }
		        break;
			case "2":
				for (var i = 0; i < designProduct.length; i++)
		    	{	
			 		myElement = document.createElement("option");
		            myElement.value = designProductID[i];
		            myElement.text = designProduct[i];
		            document.getElementsByName("product")[j].add(myElement);
		        }
		        break;		
			case "3":
				for (var i = 0; i < codeProduct.length; i++)
		    	{	
			 		myElement = document.createElement("option");
		            myElement.value = codeProductID[i];
		            myElement.text = codeProduct[i];
		            document.getElementsByName("product")[j].add(myElement);
		        }
		        break;
			
			case "4":
				for (var i = 0; i < otherProduct.length; i++)
		    	{	
			 		myElement = document.createElement("option");
		            myElement.value = otherProductID[i];
		            myElement.text = otherProduct[i];
		            document.getElementsByName("product")[j].add(myElement);
		        }
		        break;
		}
	}
	
	function contains(a,e,idx, iLength) {
		for(j=iLength;j>=0;j--) {
			if (j==idx) continue;
			if (a[j].value==e.value) return j;  
		}
		return -1;
	}

</SCRIPT>
</BODY>
</HTML>
