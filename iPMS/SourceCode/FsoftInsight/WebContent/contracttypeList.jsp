<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>contracttypeList.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = 0;
	String menu;
	int level = Integer.parseInt((String)session.getAttribute("workUnitType"));
	
	if (level == Constants.RIGHT_ADMIN)
	{ //called from admin section
		right = Security.securiPage("Parameters",request,response);
		menu = "loadAdminMenu";
	}
	else
	{ //called from project
		right = Security.securiPage("Project parameters",request,response);
		menu = "loadPrjMenu";
	}
	Vector contractTypeList = (Vector)session.getAttribute("contracttypeList");
	int status = -1;
	if (request.getParameter("selStatusList") != null) 
	{
		status = Byte.parseByte(request.getParameter("selStatusList"));
	}
%>
<BODY onload="<%=menu%>()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.contracttypeList.ContractType")%> </P>
<FORM action="contracttypeList.jsp" name="frmList" method="get" >
<P  class="TableCaption">Filter Contract</P>
<TABLE>
	<TBODY> 
	 	<TR class="NormalText">
	 		<TD>Status</TD>
	 	</TR>
		<TR class="CellBGR3">
			<TD>
				<SELECT name = "selStatusList" class="COMBO" onchange="submit();">
					<OPTION value="-1" <%=status == -1 ? "selected" : ""%>>All</OPTION>
					<OPTION value="0"  <%=status == 0 ? "selected" : ""%>>Open</OPTION>
					<OPTION value="1" <%=status == 1 ? "selected" : ""%>>Expired</OPTION>
				</SELECT>
			</TD>
		</TR>
	</TBODY>	
</TABLE>
</FORM>

<TABLE cellspacing="1" class="Table" >
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.contracttypeList.ContractType")%> </CAPTION>
    <COL span="1" width="25" align="center">
    <COL span="1" width="500">
    <TBODY>
        <TR class="ColumnLabel">
            <TD>#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.contracttypeList.ContractType")%></TD>
        </TR>
        <%
	        int j=-1;
	        for (int i = 0; i < contractTypeList.size(); i++) {
	        	ContractTypeInfo contractTypeInfo = (ContractTypeInfo)contractTypeList.elementAt(i);
	        	if (contractTypeInfo.contracttypeStatus == status | status == -1) {
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
				            <A href="contracttypeUpdate.jsp?contracttypeID=<%=i%>"><%=ConvertString.toHtml(contractTypeInfo.contracttypeName)%></A>
				      <%}else{%>
				            <%=contractTypeInfo.contracttypeName%>
				       <%}%>
			            </TD>
		            </TR>
		        <%}
	        }
        %>
    </TBODY>
</TABLE>
<BR>
<%if ((level == Constants.RIGHT_ADMIN)&&(right==3)){%>
<FORM action="contracttypeAddnew.jsp" name="frmAddnew">
<INPUT type="submit" class="BUTTON" name="addnew" value="<%=languageChoose.getMessage("fi.jsp.contracttypeList.Addnew")%>">
</FORM>
<%}%>
<SCRIPT>
	var objToHide=new Array(frmList.selStatusList);
</SCRIPT>
</BODY>
</HTML>