<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.web.*,com.fms1.common.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>header</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY  bgcolor="#ffffff" style="margin: 0px" onload="mycursor()">
<%
	String strHome=(String)session.getAttribute("defaultHome");
	int home=WorkUnitInfo.TYPE_ADMIN;
	if (strHome!=null)
		home=Integer.parseInt(strHome);
	String strPassword = "";
	String strName = "";
	UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
	if (userLoginInfo != null){
		strPassword = userLoginInfo.Password.trim();
		strName = userLoginInfo.Name;
	}
	String FMS1version = Parameters.FMS1version;
	String workunit = (String)session.getAttribute("workUnitName");
	Vector groupList = (Vector) session.getAttribute("groupList");
	Vector grpList = (Vector) session.getAttribute("groupList");
	Vector orgList = (Vector)session.getAttribute("orgList");
%>
<FORM  method="post" name="frm" target="menu">
<TABLE cellpadding="0" cellspacing="0" bgcolor="#ffffff" width="100%"
	 background="image/frame/bgr1.gif">
	<TBODY>
		<TR class="WelcomeName">
			<TD width="259" height="47"><IMG src="image/frame/Header1.gif" width="259"
				height="47" border="0"></TD>
			<TD width="229" style="text-align: left;"><A href="Fms1Servlet?reqType=<%=Constants.VIEW_USER_PROFILE%>" target="_blank">
			<%=strName%><BR><%=(workunit!=null)?workunit:""%></A></TD>
			<TD width="150" style="text-align: right;vertical-align: top;"> <%=languageChoose.getMessage("fi.jsp.header.version")%>  <%=FMS1version%></TD>
			<TD width="141"   style="text-align: right;"><A href="Fms1Servlet?reqType=<%=Constants.LOGOUT%>" target="_top"><IMG src="<%=languageChoose.getMessage("fi.img.image.logout")%>" border="0" style="vertical-align: top;"></A><A HREF="help/index.htm" target="help">&nbsp;&nbsp;<IMG src="<%=languageChoose.getMessage("fi.img.image.help")%>" border="0" style="vertical-align: top;"></A></TD>
			<TD width="21"></TD>
		</TR>
	</TBODY>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" bgcolor="#ffffff" width="100%"
	 background="image/frame/bgr1.gif">
	<TBODY> 
		<TR>
			<TD valign="top"><IMG src="image/frame/Header2.gif" width="143" height="45" border="0"></TD>
			<TD width="77" valign="top">
				<%boolean orgDisab=(orgList==null ||orgList.size()==0);
				if (orgDisab){%>
					<IMG src="<%=languageChoose.getMessage("fi.img.image.Organization1disab")%>" width="77" height="45" border="0" name="imgOrg" onclick="notAllowed()">
				<%}else{%>
				<A href="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=organizationHome.jsp" target="main">
				<IMG  src="<%=((home==WorkUnitInfo.TYPE_ORGANIZATION)? languageChoose.getMessage("fi.img.image.Organization"):languageChoose.getMessage("fi.img.image.Organization1"))%>" width="77" height="45" border="0" name="imgOrg" onclick="doAction('menuOrg.jsp');">
				</A>
				<%}%>
			</TD>
			<TD width="77" valign="top">
				<%boolean grpDisab=(grpList==null ||grpList.size()==0);
				if (grpDisab){%>
					<IMG src="<%=languageChoose.getMessage("fi.img.image.Group1disab")%>" width="77" height="45" border="0" name="imgGroup" onclick="notAllowed()">
				<%}else{%>
					<A href="Fms1Servlet?reqType=<%=Constants.HEADER_GROUP%>" target="main">
					<IMG src="<%=((home==WorkUnitInfo.TYPE_GROUP)?languageChoose.getMessage("fi.img.image.Group"):languageChoose.getMessage("fi.img.image.Group1"))%>" width="77" height="45" border="0" name="imgGroup" onclick="doAction('menuGroup.jsp');">
					</A>
				<%}%>
			</TD>

			<TD width="77" valign="top">
				<A href="Fms1Servlet?reqType=<%=Constants.HEADER_PRJ%>" target="main">
				<IMG src="<%=((home==WorkUnitInfo.TYPE_PROJECT)? languageChoose.getMessage("fi.img.image.Project"):languageChoose.getMessage("fi.img.image.Project1"))%>" width="77" height="45" border="0" name="imgProject" onclick="doAction('menuProject.jsp');">
				</A>
			</TD>
			<TD width="77" valign="top">
				<A href="Fms1Servlet?reqType=<%=Constants.HEADER_ADM%>" target="main">
				<IMG src="<%=((home==WorkUnitInfo.TYPE_ADMIN)? languageChoose.getMessage("fi.img.image.Default"):languageChoose.getMessage("fi.img.image.Default1"))%>" width="77" height="45" border="0" name="imgDefault" onclick="doAction('menuDefault.jsp');">
				</A>
			</TD> 
			<!-- bo di RMS
			<TD width="65" valign="top">
				<IMG src="<%=languageChoose.getMessage("fi.img.image.RMS")%>" width="65" height="45" border="0" name="imgRMS" onclick="doAction('RMS2');">
			</TD>
			-->
			<TD width="65" valign="top">
				<IMG src="<%=languageChoose.getMessage("fi.img.image.DMS1")%>" width="65" height="45" border="0" name="imgDMS" onclick="doAction('DMS');">
			</TD>
			<TD width="65" valign="top">
				<IMG src="<%=languageChoose.getMessage("fi.img.image.Timesheet1")%>" width="65" height="45" border="0" name="imgTimesheet" onclick="doAction('timesheet');" >
			</TD>
			<TD width="65" valign="top">
				<IMG src="<%=languageChoose.getMessage("fi.img.image.Dashboard1")%>" width="65" border="0" name="imgDashboard" onclick="doAction('dashboard');">
			</TD>
			<TD width="65" valign="top">
				<IMG src="<%=languageChoose.getMessage("fi.img.image.NCMS1")%>" width="65" height="45" border="0" name="imgNCMS" onclick="doAction('NCMS');">
			</TD>

			<TD width="99%" ></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<SCRIPT language="JavaScript">
function doAction(strHref)
{
    /*if (strHref=='RMS2'){
		window.open('/rms2');
    }*/
	if (strHref=='DMS'){
		window.open('/dms');
	}
    if (strHref=='timesheet'){
       	frm.action = "Fms1Servlet?reqType="+<%=Constants.GET_TIMESHEET%>;
        frm.target="timesheet";
        frm.submit();
        return;
    }
    if (strHref=='dashboard'){
    	frm.action = "Fms1Servlet?reqType="+<%=Constants.GET_DASHBOARD%>;
        frm.target="dashboard";
        frm.submit();
        return;
    }
    if (strHref=='NCMS'){
    	frm.action = "Fms1Servlet?reqType="+<%=Constants.GET_NCMS%>;
    	frm.target="NCMS";
        frm.submit();
        return;
    }

	if (strHref=='menuDefault.jsp'){
        document.images("imgDefault").src	= "<%=languageChoose.getMessage("fi.img.image.Default")%>";
    }
    else{
    	document.images("imgDefault").src	= "<%=languageChoose.getMessage("fi.img.image.Default1")%>";
    }
    
    if (strHref=='menuProject.jsp'){
        document.images("imgProject").src	= "<%=languageChoose.getMessage("fi.img.image.Project")%>";
    }
    else{
    	document.images("imgProject").src	= "<%=languageChoose.getMessage("fi.img.image.Project1")%>";
    }
	<%if (!orgDisab){%>
	    if (strHref=='menuOrg.jsp'){
	        document.images("imgOrg").src	= "<%=languageChoose.getMessage("fi.img.image.Organization")%>";
	    }
	    else{
	    	document.images("imgOrg").src	= "<%=languageChoose.getMessage("fi.img.image.Organization1")%>";
	    }  
    <%}
    if (!grpDisab){%>
	    if (strHref=='menuGroup.jsp'){
	        document.images("imgGroup").src	= "<%=languageChoose.getMessage("fi.img.image.Group")%>";
	    }
	    else{
	    	document.images("imgGroup").src	= "<%=languageChoose.getMessage("fi.img.image.Group1")%>";
	    } 
    <%}%>
}
function mycursor()
{
	//document.images('imgRMS').style.cursor="hand";
	document.images('imgDMS').style.cursor="hand";
	document.images('imgTimesheet').style.cursor="hand";
	document.images('imgNCMS').style.cursor="hand";
	document.images('imgDashboard').style.cursor="hand";
}
function notAllowed(){
alert('<%=languageChoose.getMessage("fi.jsp.header.SorryYouAreNotAllowedToAccessThisSection")%>');
}
</SCRIPT>
</BODY>
</HTML>
