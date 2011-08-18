<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>bizdomainList.jsp</TITLE>
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
	Vector bizDomainList = (Vector)session.getAttribute("bizdomainList");
	int vStatus = -1;
	if (request.getParameter("selStatusList") != null) {
		vStatus = Byte.parseByte(request.getParameter("selStatusList"));
	}
%>
<BODY onload="<%=menu%>()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.bizdomainList.BusinessDomain")%> </P>
<FORM action="bizdomainList.jsp" name="frm1" method="POST" >
<P  class="TableCaption">Filter Business </P>
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
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.bizdomainList.BusinessDomain1")%> </CAPTION>
    <COL span="1" width="25" align="center">
    <COL span="1" width="500">
    <TBODY>
        <TR class="ColumnLabel">
            <TD>#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.bizdomainList.Domain")%> </TD>
        </TR>
        <%
	     	int j = -1;
	        for (int i = 0; i < bizDomainList.size(); i++) {
	        	
	        	BizDomainInfo bizDomainInfo = (BizDomainInfo)bizDomainList.elementAt(i);
	        	String className;
	        	if (bizDomainInfo.domainStatus == vStatus || vStatus == -1)
	        	{
	        	    j++;
					if (j%2 == 0) 
						className = "CellBGRnews";
					else 
						className = "CellBGR3";%>
		            <TR class="<%=className%>">
			            <TD>
			          <%if ((level == Constants.RIGHT_ADMIN)&&(right==3)) {%>
			          		<A href="bizdomainUpdate.jsp?bizdomainID=<%=i%>"><%=j+1%></A>			            	
			          <%}else{%>
			          		<%=j+1%>
			          <%}%>
			            </TD>
			            <TD>
			          <%if ((level == Constants.RIGHT_ADMIN)&&(right==3)) {%>
				            <A href="bizdomainUpdate.jsp?bizdomainID=<%=i%>"><%=bizDomainInfo.name%></A>
				      <%}else{%>
				            <%=bizDomainInfo.name%>
				      <%}%>
			            </TD>
		            </TR>
	          <%}
          }
    %>
    </TBODY>
</TABLE>
<P>
<%if ((level == Constants.RIGHT_ADMIN)&&(right==3)){%>
<FORM action="bizdomainAddnew.jsp" name="frm">
<INPUT type="submit" class="BUTTON" name="addnew"  value=" <%=languageChoose.getMessage("fi.jsp.bizdomainList.Addnew")%> ">
</FORM>
<%}%>
</P>
<SCRIPT>
	var objToHide=new Array(frm1.selStatusList);
</SCRIPT>
</BODY>
</HTML>
