<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>   
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>CIRegisterUpdate.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.CIRegister")%></P>
<FORM name="frm" action="Fms1Servlet" method="post">
<TABLE class="Table"   cellspacing="1">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.UpdateCIRegister")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
        	<TD><%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Stage")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Workproduct")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Product")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Baseline")%></TD>
            <TD width="50" align="center"> <%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Dev")%> </TD>
            <TD width="50" align="center"> <%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Review")%> </TD>
            <TD width="50" align="center"> <%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Test")%> </TD>
            <TD width="50" align="center"> <%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Release")%> </TD>
            <TD width="150" align="center"><%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Note")%></TD>
         </TR>
        <%
        Vector vectCIRegister = (Vector)session.getAttribute("CIRegister");
      	int numrecords =vectCIRegister.size();
        if (vectCIRegister != null) {
                for (int i = 0; i < numrecords; i++) {
                        ModuleInfo objCIRegisterInfo = (ModuleInfo)vectCIRegister.elementAt(i);
                        String className ;
                        if (i%2 == 0) className = "CellBGRnews";
                        else className = "CellBGR3";
						%> 
				        <TR class="<%=className%>">
				            <TD><%=objCIRegisterInfo.stage%></TD>
				            <TD><%=languageChoose.getMessage(objCIRegisterInfo.wpName)%></TD>
				            <TD><%=objCIRegisterInfo.name%><INPUT type="hidden" name="moduleName<%=i%>" value="<%=objCIRegisterInfo.name%>"></TD>
				            <TD><INPUT size="15" type="text" maxlength="50" name="baseline<%=i%>" value='<%=((objCIRegisterInfo.baseline==null) ? "" : objCIRegisterInfo.baseline)%>'></TD>
            				<TD align="center"><INPUT type="radio" <%=(objCIRegisterInfo.baselineStatus ==1)?"checked":""%> name="GROUPRADIO<%=i%>" value="1" ></TD>
				            <TD align="center"><INPUT type="radio" <%=(objCIRegisterInfo.baselineStatus ==2)?"checked":""%> name="GROUPRADIO<%=i%>" value="2" ></TD>
				            <TD align="center"><INPUT type="radio" <%=(objCIRegisterInfo.baselineStatus ==3)?"checked":""%> name="GROUPRADIO<%=i%>" value="3" ></TD>
				            <TD align="center"><INPUT type="radio" <%=(objCIRegisterInfo.baselineStatus ==4)?"checked":""%> name="GROUPRADIO<%=i%>" value="4" ></TD>
				            <TD class="CellBGRnews"><TEXTAREA rows="3" cols="50" name="baseline_note<%=i%>"><%=((objCIRegisterInfo.baselineNote ==null) ? "" : objCIRegisterInfo.baselineNote)%></TEXTAREA></TD>
        </TR> 
        		<%}%> 
			
    </TBODY>
</TABLE>
<INPUT type="hidden" name="nRows" value=<%=numrecords%>>
<INPUT type="hidden" name="reqType" value=<%=Constants.UPDATE_CI_REGISTER%>>
<BR>
<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.OK")%>" class="BUTTON" onclick="doSubmit();"> 
<%}else{%>
<p class="ERROR"><%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Pleaseusetheapplicationtoaccesstothispage")%></P>
<%}%>
<INPUT type="button" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Cancel")%>" class="BUTTON" onclick="doIt(<%=Constants.GET_CI_REGISTER%>)">
</FORM>

</BODY>

<SCRIPT language="javascript">
  function doSubmit()
  {
  		<%for (int i=0; i<numrecords;i++){%>
			if (frm.elements[<%=i%>].value.search("<")!=-1) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Pleasedonotuse")%>"); 
	  	  		frm.elements[<%=i%>].focus(); 
				return; 
			}
			if (frm.elements[<%=i+5%>].value.search("<")!=-1) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Pleasedonotuse")%>"); 
	  	  		frm.elements[<%=i+5%>].focus(); 
				return;
			}
			if(frm.baseline_note<%=i%>.value.length>200)
			{
				alert("<%=languageChoose.getMessage("fi.jsp.CIRegisterUpdate.Lengthofthisfieldmustbelessthan200")%>");
				frm.baseline_note<%=i%>.focus(); 
				return;
			}

		<%}%>
	frm.submit();
 }
</SCRIPT>
</HTML>
