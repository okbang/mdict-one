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
<TITLE>defectRevProductUpdate.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	
	boolean isOver = false;
	int nDisplayed;
	String rowStyle;
	int iCount = 0;
	
	Vector vModule = (Vector) session.getAttribute("DefectModuleBatchUpdateList");		
	Vector vErrModule = (Vector) request.getAttribute("ErrDefectModuleBatchUpdateList");
	if (vErrModule != null) {
		isOver = true;
		request.removeAttribute("ErrDefectModuleBatchUpdateList");
	}
	
	WPSizeInfo moduleInfo = null;		

	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
	
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	String fromPage = (String) request.getParameter("fromPage");
	if (fromPage == null) fromPage = "";
	
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<form name ="frmdefectRevProductUpdate" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.DefectUpdateProductForReview")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.ReviewProducts")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="3%" align = "center">#</TD>
			<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.Process")%> </TD>
			<TD width="50%"> <%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.Product")%> </TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.PlannedFoundByReview")%></TD>			
		</TR>
	</THEAD>
	<TBODY>
<%
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrModule;
	else vTemp = vModule;
	nRow = vTemp.size();
	
	for (; row < nRow; row++) {
		moduleInfo = (WPSizeInfo) vTemp.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD width="24" align = "center"> 
				<%=row + 1%> 
				<input type = "hidden" name="moduleID" value = "<%=moduleInfo.moduleID%>"/>				
			</TD>
			<TD>				
				<% switch (moduleInfo.processType) {
					case 1: %>
					<%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.Requirement")%>
				<%  break;
					case 2: %>				
					<%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.Design")%>
				<%  break;
					case 3: %>	
					<%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.Coding")%>
				<%  break;
					case 4: %>	
					<%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.Other")%>
				<% 	break;
					} %>				
			</TD>
			<TD><%=moduleInfo.name%></TD>
			<TD  align = "center">
			 <INPUT type ="text" size = "18" maxlength ="18" name="planSize" class="SmallTextbox" value="<%= (Double.isNaN(moduleInfo.newPlanSizeReview)) ? "" :moduleInfo.newPlanSizeReview+""%>"/>
			</TD>
		</TR>
<%	
	}
	nDisplayed = row;	// Indicate numbers of lines displayed
%>	
	</TBODY>
</TABLE>
<BR>
<input type ="hidden" name = "reqType"/>
<input type ="hidden" name = "fromPage" value = "<%=fromPage%>"/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.defectRevProductUpdate.Cancel")%>" class="BUTTON" onclick="<%="defect".equals(fromPage) ? "jumpURL('DefectView.jsp');" : "jumpURL('qualityObjective.jsp');"%>">
</form>

<SCRIPT language="JavaScript">

var nextHiddenIndex = <%=nDisplayed + 1%>;

function addSubmit(){	
	if (checkValid()) {
		frmdefectRevProductUpdate.reqType.value=<%=Constants.DEFECT_REV_PRODUCT_UPDATE%>;
		frmdefectRevProductUpdate.submit();
	} else return false;	
}

function checkValid(){	
	var arrTxt2= document.getElementsByName("planSize");
	
	var length = nextHiddenIndex-1;	
	
	for(i=0; i < length;i++) {
		// check if plan size is a number
		if(isNaN(arrTxt2[i].value) && (trim(arrTxt2[i].value) != "")){
			alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.TheValueMustBeANumber")%>");  			
  			arrTxt2[i].focus();
  			return false;  
  		}
	}	
	return true;
}	


</SCRIPT>
</BODY>
</HTML>
