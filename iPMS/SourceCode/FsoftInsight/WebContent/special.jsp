 <%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*,com.fms1.infoclass.group.PCBEffortDistribInfo" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD> 
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	Vector result=(Vector)request.getAttribute("pcbList");
    //PCBEffortDistribInfo [] info = (PCBEffortDistribInfo [])request.getAttribute("pcbList");
   // System.out.println("ok JSP");
%>

<BODY>

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.special.Specialreport")%> </P>

<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.SPECIAL_REPORT%>">
 <%=languageChoose.getMessage("fi.jsp.special.lifecycle")%> 
<SELECT name="life" class="COMBO">
   <OPTION value="0" selected > <%=languageChoose.getMessage("fi.jsp.special.dev")%> </opTION>
   <OPTION value="1" selected > <%=languageChoose.getMessage("fi.jsp.special.main")%> </opTION>
   <OPTION value="2" selected > <%=languageChoose.getMessage("fi.jsp.special.oth")%> </opTION>
	 </SELECT>
 <%=languageChoose.getMessage("fi.jsp.special.type")%> 
<SELECT name="typ" class="COMBO">
   <OPTION value="0" selected > <%=languageChoose.getMessage("fi.jsp.special.effstage")%> </opTION>
   <OPTION value="1" selected > <%=languageChoose.getMessage("fi.jsp.special.QCdefdistprocess")%> </opTION>
   <OPTION value="2" selected > <%=languageChoose.getMessage("fi.jsp.special.QCdefdistproduct")%> </opTION>
	 </SELECT>
 <%=languageChoose.getMessage("fi.jsp.special.Start")%> 
<input name="startdate" value="01-jan-04">
 <%=languageChoose.getMessage("fi.jsp.special.End")%> 
<input name="enddate" value="31-dec-04">
<input type="submit">
</FORM>
 <%=languageChoose.getMessage("fi.jsp.special.Result")%> 
<BR>
 <%=languageChoose.getMessage("fi.jsp.special.codeInitiationDefinitionSolu")%> 
<BR>
<%
if (result!=null){

ProjectInfo prInf;
PCBEffortDistribInfo [] info;
for (int j=0;j<result.size();j++){
	prInf=(ProjectInfo)result.elementAt(j++);
	info = (PCBEffortDistribInfo [])result.elementAt(j);
%>
<BR><%=prInf.getProjectCode()%>
	<%for (int i=0;i<info.length;i++){%>
	,<%=CommonTools.formatDouble(info[i].value)%>
	<%}
}
}%>


</BODY>
</HTML>
