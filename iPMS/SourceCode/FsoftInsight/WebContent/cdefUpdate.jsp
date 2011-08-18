<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>cdefUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
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
	String prjojectcode = (String)session.getAttribute("projCode");
	
	Vector cdVt=(Vector)session.getAttribute("vtCommDefect");
	Vector dtList = (Vector)session.getAttribute("defectType");
	Vector dpVt=(Vector)session.getAttribute("vtDefectLog");
	
	String vtIDstr=request.getParameter("vtID");
	int vtID = Integer.parseInt(vtIDstr);
  	
  	CommDefInfo info = (CommDefInfo) cdVt.get(vtID);

	int right = Security.securiPage("Defects",request,response); 
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.defdesc.focus();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Commondefect")%> </p>
<FORM method="POST" name="frm" action="Fms1Servlet">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Commondefect1")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Commondefectcode")%> </TD>
            <TD class="CellBGR3"><%=info.commonDefCode%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Commondefect2")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA rows="6" cols="50" name="defdesc"><%=info.commdef%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Defecttype")%>* </TD>
            <TD class="CellBGR3">
			<SELECT name="deftype" class="COMBO">
				<OPTION value="1" <%if (info.defecttype == 1) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.01FunctionalityOther")%> </OPTION>
				<OPTION value="2" <%if (info.defecttype == 2) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.02UserInterface")%> </OPTION>
				<OPTION value="3" <%if (info.defecttype == 3) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.03Performance")%> </OPTION>
				<OPTION value="4" <%if (info.defecttype == 4) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.04Designissue")%> </OPTION>
				<OPTION value="5" <%if (info.defecttype == 5) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.05Codingstandard")%> </OPTION>
				<OPTION value="6" <%if (info.defecttype == 6) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.06Document")%> </OPTION>
				<OPTION value="7" <%if (info.defecttype == 7) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.07DataDatabaseintegrity")%> </OPTION>
				<OPTION value="8" <%if (info.defecttype == 8) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.08SecurityAccessControl")%> </OPTION>
				<OPTION value="9" <%if (info.defecttype == 9) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.09Portability")%> </OPTION>
				<OPTION value="10" <%if (info.defecttype == 10) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.10Other")%> </OPTION>
				<OPTION value="11" <%if (info.defecttype == 11) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.11Tools")%> </OPTION>
				<OPTION value="12" <%if (info.defecttype == 12) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.011Reqmisunderstanding")%> </OPTION>
				<OPTION value="13" <%if (info.defecttype == 13) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.012Featuremissing")%> </OPTION>
				<OPTION value="14" <%if (info.defecttype == 14) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.013Codinglogic")%> </OPTION>
				<OPTION value="15" <%if (info.defecttype == 15) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.014Businesslogic")%> </OPTION>
			</SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Rootcause")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="6" cols="50" name="rootcause"><%=info.rootcause%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Causecategory")%> </TD>
            <TD class="CellBGR3">
            <SELECT name="causecate" class="COMBO">
				<OPTION value="1" <%if (info.causecate == 1) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Training")%> </OPTION>
				<OPTION value="2" <%if (info.causecate == 2) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Communication")%> </OPTION>
				<OPTION value="3" <%if (info.causecate == 3) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Oversight")%> </OPTION>
				<OPTION value="4" <%if (info.causecate == 4) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Understanding")%> </OPTION>
				<OPTION value="5" <%if (info.causecate == 5) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Other")%> </OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.cdefUpdate.DPCode")%> </TD>
            <TD class="CellBGR3">
            <SELECT name="dpcode" class="COMBO">
            <OPTION value=""></OPTION>
			<%
            	for (int i = 0; i < dpVt.size(); i++) {
	            	DPLogInfo info2 = (DPLogInfo)dpVt.elementAt(i);
            %>
                <OPTION value="<%=info2.dpcode%>" <% if (info2.dpcode.equalsIgnoreCase(info.dpcode)) {%> selected <%}%>><%=info2.dpcode%></OPTION>
			<%}%>
            </SELECT>
            </TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="hidden" name="reqType" value="<%=Constants.COMMON_DEFECT_UPDATE%>">
<INPUT type="hidden" name="prjojectcode" value="<%=prjojectcode%>">
<INPUT type="hidden" name="commdefid" value="<%=info.commdefID%>">
<INPUT type="hidden" name="commdefcode" value="<%=info.commonDefCode%>">
<P>
<%if (right == 3 && !isArchive){%>
<INPUT type="button" name="btnUpdate" value=" <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Update")%> " class="BUTTON" onclick="doAction(this)">
<INPUT type="button" name="btnDelete" value=" <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Delete")%> " class="BUTTON" onclick="doAction(this)">
<%}%>
<INPUT type="button" name="btnCancel" value=" <%=languageChoose.getMessage("fi.jsp.cdefUpdate.Cancel")%> " class="BUTTON" onclick="doAction(this)"></FORM>
</P>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnUpdate") {
  	
  		if(trim(frm.defdesc.value)==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.cdefUpdate.Thisfieldismandatory")%>");
  		 	frm.defdesc.focus();
  		 	return;
  		}
  		if(frm.defdesc.value.length>300){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.cdefUpdate.ItemLengthCanNotGreaterThan300")%>");
  		  	frm.defdesc.focus(); 
  		  	return;
  	  	}
  		if(frm.rootcause.value.length>300){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.cdefUpdate.ItemLengthCanNotGreaterThan300")%>");
  		  	frm.rootcause.focus(); 
  		  	return;
  	  	}
  		if(frm.dpcode.value.length>100){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.cdefUpdate.ItemLengthCanNotGreaterThan100")%>");
  		  	frm.dpcode.focus(); 
  		  	return;
  	  	}
  		
  	  	frm.submit();  		 	
  	}
  	
  	if (button.name=="btnCancel") {
  		doIt(<%=Constants.COMMON_DEFECT%>);
  		return;
  	} 	
  	if (button.name=="btnDelete") {
  		if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.cdefUpdate.Areyousuretodelete")%>")){   		
  			return;
  		}
  	  	frm.reqType.value = "<%=Constants.COMMON_DEFECT_DELETE%>";
  	  	frm.submit();
  	} 	
  }
 </SCRIPT>
</BODY>
</HTML>