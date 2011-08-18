<%@page import="com.fms1.tools.*"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.fms1.infoclass.*,java.util.Vector, com.fms1.web.*" contentType="text/html;charset=UTF-8"%>
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>UserProfileView.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadAdminMenu()" class="BD">
<%

	int right = Security.securiPage("User Profiles",request,response);

	UserInfo userInfo  = (UserInfo)session.getAttribute("userInfo");
	UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");

	String id = userInfo.account;
	String loginID = userLoginInfo.account;

	String name = userInfo.Name;
	
	String designation = userInfo.designation;
	String group = userInfo.group;
	String status = userInfo.status;
	
	Vector rightVector = (Vector)session.getAttribute("rightList");
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.UserProfileView.UserProfileDetail")%></P>
<TABLE width="80%" cellspacing="1" class="Table">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.UserProfileView.UserProfileDetail")%></CAPTION>
    <TBODY>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileView.UserID")%></P>
            </TD>
            <TD width="307" class="CellBGR3"><%=id%> &nbsp;</TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileView.UserName")%></P>
            </TD>
            <TD width="307" class="CellBGR3"><%=name%> &nbsp;</TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileView.Position")%></P>
            </TD>
            <TD width="307" class="CellBGR3"><%=((designation==null)?"N/A":designation)%>&nbsp;</TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileView.Group")%> </P>
            </TD>
            <TD width="307" class="CellBGR3"><%=((group==null)?"N/A":group)%>&nbsp;</TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileView.Status") %></P>
            </TD>
            <TD width="307" class="CellBGR3"><%
            	switch (Integer.parseInt(status)) {
            		case 1:%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.Staff")%> <%break;
            		case 2:%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.Collaborator")%> <%break;
            		case 3:%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.ExternalViewer")%> <%break;
            		case 4:%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.OutPlaced")%> <%break;
            	}%>&nbsp;</TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileView.ToolsRole")%></P>
            </TD>
            <TD width="307" class="CellBGR3">
            <%
            	if (userInfo.role.equals("1000000000")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.Developer")%> <%}
            	if (userInfo.role.equals("1100000000")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.ProjectLeader")%> <%}
            	if (userInfo.role.equals("1110000000")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.GroupLeader")%> <%}
            	if (userInfo.role.equals("1000100000")){%> PQA <%}
            	if (userInfo.role.equals("1111110000")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.Manager")%> <%}
				if (userInfo.role.equals("0000001000")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.Externalofprojectlevel")%> <%}
				if (userInfo.role.equals("0000001100")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.Externalofgrouplevel")%> <%}
				if (userInfo.role.equals("0000000010")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileView.Communicator")%> <%}
			%>
            </TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Email")%></P>
            </TD>
            <TD width="307" class="CellBGR3"><%=userInfo.userEmail == null ? "" : userInfo.userEmail%> &nbsp;</TD>
        </TR>
         <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.StartDate")%></P>
            </TD>
            <TD width="307" class="CellBGR3"><%=CommonTools.dateFormat(userInfo.userStartDate)%> &nbsp;</TD>
        </TR> 
         <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.QuitDate")%></P>
            </TD>
            <TD width="307" class="CellBGR3"><%=CommonTools.dateFormat(userInfo.userQuitDate)%> &nbsp;</TD>
        </TR>
    </TBODY>
</TABLE>
<P><BR>
</P>
<TABLE width="80%" cellspacing="1" class="Table">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.UserProfileView.Rightgroupforuserbyworkunit")%></CAPTION>
    <TBODY>
        <TR>
            <TD align="center" class="ColumnLabel">
            <P><%=languageChoose.getMessage("fi.jsp.UserProfileView.Workunitname")%>
            
            </P>
            </TD>
            <TD align="center" class="ColumnLabel">
            <P><%=languageChoose.getMessage("fi.jsp.UserProfileView.Rolename")%></P>
            </TD>
        </TR>
        <%
			for (int i = 0; i < rightVector.size(); i++)
			{
				RolesInfo rightInfo = (RolesInfo) rightVector.elementAt(i);
				String gr = rightInfo.rightGroupID;
				String wu = rightInfo.workunitName;
				String className = "";
  				if (i%2==0) className="CellBGRnews";
  				else className = "CellBGR3";
				%>
        <TR>
            <TD align="center" class="<%=className%>"><%=wu%></TD>
            <TD align="center" class="<%=className%>"><%=gr%></TD>
        </TR>
        <%}
		%>
    </TBODY>
</TABLE>
<P><BR>
</P>
<TABLE >
    <TBODY>
		<TR>
			<TD colspan=2>
			<P class="ERROR">To delete a user, please use correlative functions of <a href="/rms2" target="/rms2">RMS</a></P>
			</TD>
		</TR>
        <TR>
		    <TD align="left">
            <%
if((id.trim().equals("admin")) && (!loginID.trim().equals("admin"))){
	right = 2;
}
if(right == 3){%>
		    <INPUT type="button" name="update" value="<%=languageChoose.getMessage("fi.jsp.UserProfileView.Update") %>" class="BUTTON" onClick="jumpURL('Fms1Servlet?reqType=<%=Constants.USER_PROFILE_DETAIL%>')">
<%
}
%>
            <INPUT type="button" name="back2" value="<%=languageChoose.getMessage("fi.jsp.UserProfileView.Back")%>" onclick="doIt(<%=Constants.GET_USER_PROFILES_LIST%>);" class="BUTTON">
			</TD>
        </TR>
    </TBODY>
</TABLE>
</BODY>
</HTML>
