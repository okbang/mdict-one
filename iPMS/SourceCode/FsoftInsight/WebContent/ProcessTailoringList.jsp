<%@page import="com.fms1.tools.*"%> 
<%@page import="java.util.Vector,com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.tools.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>ProcessTailoringList.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/tailoring.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right=0;
	String menu;
	int level = 0;
	if (session.getAttribute("workUnitType") != null  ) {
		level = Integer.parseInt((String)session.getAttribute("workUnitType"));
	}
	if (level == Constants.RIGHT_ADMIN){ //called from admin section
		right=Security.securiPage("Parameters",request,response);
		menu="loadAdminMenu";
	}
	else{ //called from project
		right=Security.securiPage("Project parameters",request,response);
		menu="loadPrjMenu";
	}
	Vector proTailoringList = (Vector)session.getAttribute("ProTailoringList");
	Vector prjTailoringList = (Vector) session.getAttribute("tailoringList");
	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	Vector vtSoftwarePro = (Vector)session.getAttribute("vtSoftwarePro");
	String strProcess = (String) session.getAttribute("strProcess");
	String isAll = (String) session.getAttribute("isAll");
	Language lang = (Language)session.getAttribute("lang");
	int iProcess = 0;
	iProcess = CommonTools.parseInt(strProcess);
	int numTailoring=proTailoringList.size();
	String scroll="";
	int Status = -1;
	if (request.getParameter("selStatus") != null) {
		Status = Byte.parseByte(request.getParameter("selStatus"));
	}
	int lyfeCycle = -1;
	if (request.getParameter("selLyfeCycle") != null) {
		lyfeCycle = Byte.parseByte(request.getParameter("selLyfeCycle"));
	}
	int count=0;
	for (int i=0; i<numTailoring; i++) {
		 ProTailoringInfo proTailoring = (ProTailoringInfo) proTailoringList.get(i);
		if (((proTailoring.tailStatus == Status)||(Status==-1))
			&& ((proTailoring.tailLyfeCycle == lyfeCycle)||(lyfeCycle==-1) ||(proTailoring.tailLyfeCycle == 3))) {
			count++;
		}
	}
	boolean bool = ((CommonTools.parseInt(request.getParameter("reqType1")) != 130) && ((CommonTools.parseInt(request.getParameter("reqType1")) != 860))) ;
	ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("ProjectInfo");
	if (!bool) {
		count = 0;
		for (int i=0; i<numTailoring; i++) {
	        ProTailoringInfo proTailoring = (ProTailoringInfo) proTailoringList.get(i);
	        if ((proTailoring.tailStatus != 1) && ((proTailoring.tailLyfeCycle == 3)
	        	|| (projectInfo.getLifecycle()).equals(ProTailoringInfo.parseLifecycle(proTailoring.tailLyfeCycle)))) {
	           count++;
	        }
  	    }    
	}
	if	(count > 17 ) {
		scroll="makeScrollableTable('tableTailoring',true,'320')";
	}
	int iCount =-1;
	for (int i=0; i<numTailoring; i++) {
       ProTailoringInfo proTailoring = (ProTailoringInfo) proTailoringList.get(i);
       if (proTailoring.tailLyfeCycle == 3) {
           iCount++;
       }
  	 }    
	int j=0;
	String reqType1=request.getParameter("reqType1");
%>
<BODY onload="<%=menu%>(); <%if (isAll.equalsIgnoreCase("0")) {%> init(); <%} else {%> fillAll(); <%}%> <%=scroll%>" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.ProcessTailoring")%> </P>
<BR>
<FORM name="frm1" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.PROCESS_TAILORING%>">
<INPUT type="hidden" name="isAll" value="">
<P class="TableCaption">Filter Tailoring</P>
<TABLE width="40%" class="NormalText">
<TR class="NormalText">
	<%
		if (bool) {
	%>	
		<TD nowrap="nowrap">Life cycle</TD>		
	<%}%>
		<TD> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Process")%>  </TD>
	<%
		if (bool) {
	%>	
		<TD>Status</TD>
	<%}%>
</TR>
<TR>
	<%
		if (bool) {
	%>	
	<TD>
		<SELECT name="selLyfeCycle" class="COMBO">
			<OPTION value="-1" <%=lyfeCycle == -1 ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.All")%></OPTION>
			<OPTION value="3" <%=lyfeCycle == 3? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.General	")%></OPTION>
			<OPTION value="0" <%=lyfeCycle == 0 ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Development")%></OPTION>
    		<OPTION value="1" <%=lyfeCycle == 1 ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Maintenance	")%></OPTION>
    		<OPTION value="2" <%=lyfeCycle == 2 ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Other")%></OPTION>
		</SELECT>
	</TD>
	
	<%}%>
	<TD>
		<SELECT name="cboProcess" class="COMBO" style="width:200px">
			<OPTION value="-1"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.All")%></OPTION>
		</SELECT>
    </TD>
	<%
		if (bool) {
	%>	
	<TD>
		<SELECT name="selStatus" class="COMBO">
			<OPTION value="-1" <%=Status == -1 ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.All")%> </OPTION>
		    <OPTION value="0" <%=Status == 0 ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Open")%></OPTION>
    		<OPTION value="1" <%=Status == 1 ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Expired")%></OPTION>
		</SELECT>&nbsp;&nbsp;
	</TD>
	<%}%>
	<TD><INPUT type="button" name="btnView" value="<%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.View")%> " class="BUTTON" onclick="doSearch()" ></TD>
 	<TD><INPUT type="hidden" name="reqType1" ></TD>
</TR>
<TR>
	<TD><A href="javascript:selectAll(document.forms[0].cboProcess)"> more process... </A></TD>
</TR>
</TABLE>
</FORM>


<BR>
<form name="frm_plTailoringAddPrep1" action="Fms1Servlet" method = "get">
<P class="TableCaption"><A name="tailoringlist"> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.ProcessTailoring1")%> </A></P>
<TABLE width="95%" cellspacing="1" class="Table" id="tableTailoring" >
    <COL width="25">
    <%if ((CommonTools.parseInt(request.getParameter("reqType1"))==Constants.TAILORING_GET_LIST)||(CommonTools.parseInt(request.getParameter("reqType1"))==Constants.PL_LIFECYCLE_GET_PAGE)){%>
    <COL width="25">
    <%}%>
    <COL width="400">
    <COL width="250">
    <COL width="100">
<THEAD>
        <TR class="ColumnLabel">
            <%if ((CommonTools.parseInt(request.getParameter("reqType1"))==Constants.TAILORING_GET_LIST)||(CommonTools.parseInt(request.getParameter("reqType1"))==Constants.PL_LIFECYCLE_GET_PAGE)){%>
            <TD></TD>
            <%}%>
            <TD>#</TD>
             <TD> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Tailoringpermission")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.tailoring.AppCri")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Process1")%> </TD> 
            <TD width="11%">Life Cycle</TD>
        </TR>
</THEAD>
<TBODY>
    <%
    j = -1;
	boolean b = false;
    for (int i =0; i < numTailoring; i++) {
    	ProTailoringInfo proTailoringInfo = (ProTailoringInfo)proTailoringList.elementAt(i);
    	if ( ((proTailoringInfo.tailStatus == Status) || (Status == -1))
    	 	&& ((proTailoringInfo.tailLyfeCycle == lyfeCycle ) || (lyfeCycle == -1)||(proTailoringInfo.tailLyfeCycle==3)))
    	{
    		j++;
    		String className = (j%2 == 0) ?"CellBGRnews":"CellBGR3";
    	    if (((CommonTools.parseInt(request.getParameter("reqType1"))==Constants.TAILORING_GET_LIST)
        		||(CommonTools.parseInt(request.getParameter("reqType1"))==Constants.PL_LIFECYCLE_GET_PAGE))){
	        	if ((proTailoringInfo.tailStatus == 1)
	        	     || ((!( (projectInfo.getLifecycle()).equals(ProTailoringInfo.parseLifecycle(proTailoringInfo.tailLyfeCycle))))) 
	        	     && (proTailoringInfo.tailLyfeCycle != 3)) {
	        		j--;
	        		continue; 
	        	}	
	        	b = false;
	        	for (int k=0; k<prjTailoringList.size(); k++) {
	       			TailoringDeviationInfo prjTailList = (TailoringDeviationInfo) prjTailoringList.get(k);
		        	if ((prjTailList.type == 1) && (proTailoringInfo.TailoringID == prjTailList.process_tail_ID) ) {
		        		b = true; 
		        	 }
		       }%>
	          <TR class="<%=className%>">
	      		<TD> <input <%= b == true ? "disabled" : "" %> type="checkbox" name="tailID1" value="<%=i%>"  > </TD> 	   	
	      		<TD><%=j+1%></TD>
		        <TD><%=proTailoringInfo.Tailoring_per%></TD>
		        <TD><%=proTailoringInfo.Applicable_Cri%></TD>
		        <TD><%=(proTailoringInfo.ProcessName.equalsIgnoreCase("N/A") ? "":proTailoringInfo.ProcessName)%></TD>
		 		<TD><%=proTailoringInfo.lyfeCycleName%></TD>
		      </TR>
        <%}else {%>
	         <TR class="<%=className%>"> 
		        <TD><%=j+1%></TD>
		        <TD>
		        <%if ((level == Constants.RIGHT_ADMIN)&&(right==3)) {%>
		        <A href="ProcessTailoringUpdate.jsp?tailID=<%=i%>&cboProcess=<%=request.getParameter("cboProcess")%>"><%=proTailoringInfo.Tailoring_per%></A>
		        <%}else{%>
		        <%=proTailoringInfo.Tailoring_per%>
		        <%}%>
		        </TD>
		        <TD><%=proTailoringInfo.Applicable_Cri%></TD>
		        <TD><%=languageChoose.getMessage(proTailoringInfo.ProcessName)%></TD>
		 		<TD><%=proTailoringInfo.lyfeCycleName%></TD>
		    </TR>
    	<%}%>
    <%}
  }%>
</TBODY>
<TFOOT>
        <TR>
        <%if ((CommonTools.parseInt(request.getParameter("reqType1"))==Constants.TAILORING_GET_LIST)||(CommonTools.parseInt(request.getParameter("reqType1"))==Constants.PL_LIFECYCLE_GET_PAGE)){%>
        <TD colspan="6" class="TableLeft" align="right"></TD>
        <%}else{%>
        <TD colspan="5" class="TableLeft" align="right"></TD>
        <%}%>
        </TR>
</TFOOT>
</TABLE>
<P>
<%if ((CommonTools.parseInt(request.getParameter("reqType1"))==Constants.TAILORING_GET_LIST)||(CommonTools.parseInt(request.getParameter("reqType1"))==Constants.PL_LIFECYCLE_GET_PAGE)){%>
<% if (CommonTools.parseInt(request.getParameter("reqType1"))==Constants.PL_LIFECYCLE_GET_PAGE){%>
<input type = "hidden" name="tailoring_source" value="1"><%}%>
<input type="button" name="add" value="<%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Add")%> " class="BUTTON" onclick="onAddnew();">
<INPUT type="button" name="Back" value="<%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Back")%> " class="BUTTON" onclick="onBack();">
<INPUT type="hidden" name="reqType">
<INPUT type="hidden" name="reqType1">
<%}%>
</FORM>
<%if ((level == Constants.RIGHT_ADMIN)&&(right==3)){%>
<FORM action="ProcessTailoringAddNew.jsp" name="frm">
<INPUT type="hidden" class="BUTTON" name="vLyfeCycle">
<INPUT type="submit" class="BUTTON" name="addnew"  value="<%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.Addnew")%> " onclick="onAdd();">
<INPUT type="hidden" name="cboProcess" value="<%=request.getParameter("cboProcess")%>">
</FORM>
<%}%>
</BODY>
<SCRIPT>

	var processID = <%=iProcess%>;
	
	function init() {
		fillCommonTailorings();
		checkProcess();
	}
	
	function fillAll() {
		appendTailorings(document.forms[0].cboProcess, true);
		checkProcess();
	}
	
	function checkProcess(){
		for (var i = 0; i < document.forms[0].cboProcess.length; i++){
			if(document.forms[0].cboProcess.options[i].value == processID){
				document.forms[0].cboProcess.options[i].setAttribute('selected',true);
				return;
			}
		}
	}
	
	function fillCommonTailorings() {
		// Fill software process
	    appendTailorings(document.forms[0].cboProcess, false);
	}
	
	function selectAll(combo) {
	    // Reset and fill all process
	    resetAndFillTailorings(combo, true, null, 1);
	    checkProcess();
	    frm1.isAll.value ="1";
	}
	
	function appendTailorings(combo, isAll, excludeValue) {
	    if (isAll) {
	        // fill all process
	        for (var i=0;i<all_tai.length;i++) {
	            if (excludeValue != all_tai[i][0]) { // Avoid this option
	                appendOption(combo, all_tai[i][0], all_tai[i][1], null, false);
	            }
	        }
	    }
	    else {
	        // fill common process
	        for (var i=0;i<common_tai.length;i++) {
	            if (excludeValue != common_tai[i][0]) { // Avoid this option
	                appendOption(combo, common_tai[i][0], common_tai[i][1], null, false);
	            }
	        }
	    }
	}

	// Reset (remove options from startIndex) then fill process into combo box
	function resetAndFillTailorings(combo, isAll, excludeValue, startIndex) {
	    if (combo) {
	        while (combo.options.length > startIndex) {  // Clear combo box
	            combo.options[startIndex]=null;
	        }
	        appendTailorings(combo, isAll, excludeValue);
	    }
	}
	
	function onAdd() {
		frm.vLyfeCycle.value = frm1.selLyfeCycle.value;
		return;
	}
	function checkSelect() {
        var count = 0;
	    var form = document.frm_plTailoringAddPrep1;
	    for (var i = 0; i < form.elements.length; i++) {
	        var e = form.elements[i];
	        if (e.name == "tailID1" && e.type == "checkbox") {
	        	if (e.checked == 1) {
        			count = count + 1;
	        	}
	        }
	    }
	    if (count>10) {
	        alert("<%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.MaxTailoring")%>");
	        return false;
	    }
	    if (count<=0) {
	    	alert("<%=languageChoose.getMessage("fi.jsp.ProcessTailoringList.ChooseTailoring")%>");
        	return false;
	    }
	    return true;
   }    	
 function doSearch(){
    <%if(isAll.equalsIgnoreCase("1")) {%>
    	frm1.isAll.value="1";
    <%}%>
    frm1.reqType.value ="<%=Constants.PROCESS_TAILORING_SEARCH%>";
  	frm1.reqType1.value="<%=request.getParameter("reqType1")%>";
  	frm1.submit();
  }
 function onBack() {
	 <%if (CommonTools.parseInt(request.getParameter("reqType1"))==Constants.TAILORING_GET_LIST){%>
	 frm1.reqType.value ="<%=Constants.TAILORING_GET_LIST%>";
	 <%}else{%>
	 frm1.reqType.value ="<%=Constants.PL_LIFECYCLE_GET_PAGE%>";
	 <%}%>
	 frm1.submit();
 }
 function onAddnew() {
 	frm_plTailoringAddPrep1.reqType1.value="<%=request.getParameter("reqType1")%>";
 	frm_plTailoringAddPrep1.reqType.value ="<%=Constants.TAILORING_ADD_PREPARE%>";
 	if (!checkSelect()) {
 		return;
 	}
	frm_plTailoringAddPrep1.submit();
 }
  	<%if (bool) {%>
		var objToHide=new Array(frm1.selLyfeCycle);
		var objToHide2=new Array(frm1.cboProcess);
		var begin = objToHide.length;
		for (var i=0; i<objToHide2.length; i++) {
			objToHide[begin+i] = objToHide2[i]; 
		}
	<%} else {%>	
	 	var objToHide=new Array(frm1.cboProcess);
	 <%}%>	
</SCRIPT>
</HTML>
