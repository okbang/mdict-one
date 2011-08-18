<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>apptypeList.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
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
	int level = Integer.parseInt((String)session.getAttribute("workUnitType"));
	
	if (level == Constants.RIGHT_ADMIN){ //called from admin section
		right=Security.securiPage("Parameters",request,response);
		menu="loadAdminMenu";
	}
	else{ //called from project
		right=Security.securiPage("Project parameters",request,response);
		menu="loadPrjMenu";
	}
	Vector appTypeList = (Vector)session.getAttribute("apptypeList");
	int vStatus = -1;
	if (request.getParameter("selStatusList") != null) {
		vStatus = Byte.parseByte(request.getParameter("selStatusList"));
	}

%>
<BODY onload="<%=menu%>()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.apptypeList.ApplicationType")%> </P>
<FORM action="apptypeList.jsp" name="frm1" method="get" >
<P  class="TableCaption">Filter Application</P>
<TABLE>
	<TBODY> 
	 	<TR class="NormalText">
	 		<TD>Status</TD>
	 	</TR>
		<TR class="CellBGR3">
			<TD>
				<SELECT name = "selStatusList" class="COMBO" onchange="submit();">
					<OPTION value="-1" <%=vStatus == -1 ? "selected" : ""%>>All</OPTION>
					<OPTION value="0"  <%=vStatus == 0 ? "selected" : ""%>>Open</OPTION>
					<OPTION value="1" <%=vStatus == 1 ? "selected" : ""%>>Expired</OPTION>
				</SELECT>
			</TD>
		</TR>
	</TBODY>	
</TABLE>
</FORM>

<TABLE cellspacing="1" class="Table" >
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.apptypeList.ApplicationType1")%> </CAPTION>
    <COL span="1" width="25" align="center">
    <COL span="1" width="500">
    <TBODY>
        <TR class="ColumnLabel">
            <TD>#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.apptypeList.ApplicationType2")%></TD>
        </TR>
        <%
	        int j=-1;
	        for (int i = 0; i < appTypeList.size(); i++) {
	        	AppTypeInfo appTypeInfo = (AppTypeInfo)appTypeList.elementAt(i);
	        	if (appTypeInfo.appStatus == vStatus | vStatus == -1) {
	        		j++;
		        	String className;
					if (j%2 == 0) 
						className = "CellBGRnews";
					else 
						className = "CellBGR3"; %>
			        <TR class="<%=className%>">
						<TD><%=j+1%></TD>
			            <TD>
			          <%if ((level == Constants.RIGHT_ADMIN)&&(right==3)) {%>
				            <A href="apptypeUpdate.jsp?apptypeID=<%=i%>"><%=appTypeInfo.name%></A>
				      <%}else{%>
				            <%=appTypeInfo.name%>
				      <%}%>
			            </TD>
		            </TR>
		        <%}
	        }
        %>
    </TBODY>
</TABLE>
<%if ((level == Constants.RIGHT_ADMIN)&&(right==3)){%>
<FORM action="apptypeAddnew.jsp" name="frm">
<INPUT type="submit" class="BUTTON" name="addnew"  value=" <%=languageChoose.getMessage("fi.jsp.apptypeList.Addnew")%> ">
</FORM>
<%}%>
<SCRIPT>
	var objToHide=new Array(frm1.selStatusList);
</SCRIPT>
</BODY>
</HTML>