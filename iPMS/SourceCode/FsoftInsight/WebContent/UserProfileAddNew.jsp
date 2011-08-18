<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.Vector, com.fms1.common.*, com.fms1.web.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>UserProfileAddNew.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload = "loadAdminMenu();document.frmAdd.userID.focus();" class="BD">
<%

Vector grpVector=(Vector)session.getAttribute("grpVector");
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.AddUserProfile")%></P>
<% 
final Vector wuVector = WorkUnit.getWUList(null);
final Vector rgVector = RightGroup.getRightGroupVector();
String errorAdd = request.getParameter("error");
if((errorAdd != null) && (errorAdd != "")){
%>
<p class = "ERROR"><%=languageChoose.getMessage(errorAdd)%></p>
<%
}
%>    

<FORM name = "frmAdd" action="Fms1Servlet?reqType=<%=Constants.ADD_USER_PROFILE%>" method = "post">
<DIV align="left">
<TABLE width="80%" cellspacing="1" class="Table">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Userinformation")%></CAPTION>
    <TBODY>
        <TR>
            <TD width="111" class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.UserID")%>*</P>
            </TD>
            <TD width="330" class="CellBGR3"><INPUT size="22" type="text" name="userID" maxlength="20">
		</TD>
        </TR>
        <TR>
            <TD width="111" class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Password")%>*</P>
            </TD>
            <TD width="330" class="CellBGR3"><INPUT size="22" type="password" name="userPassword" maxlength="20">
		</TD>
        </TR>
        <TR>
            <TD width="111" class="ColumnLabel">&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Username")%>*</TD>
            <TD width="330" class="CellBGR3"><INPUT size="33" type="text" name="userName" maxlength="30">
		</TD>
        </TR>
        <TR>
            <TD width="111" class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Position")%>*</P>
            </TD>
            <TD width="330" class="CellBGR3"><INPUT size="33" type="text" name="userDesignation" maxlength="30">
		</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">
            <P>&nbsp;  <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Group")%> </P>
            </TD>
            <TD class="CellBGR3">
            <SELECT name="userGroup" class="COMBO">
            <%
				for(int i = 0; i < grpVector.size(); i++)
				{
					WorkUnitInfo wuInfoTemp = (WorkUnitInfo)grpVector.get(i);
			%>
            	<OPTION value="<%=wuInfoTemp.workUnitName%>"><%=wuInfoTemp.workUnitName%></OPTION>
            <%
            	}
            %>
            </SELECT>
			</TD>
        </TR>
        <TR>
            <TD width="111" class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Status")%></P>
            </TD>
            <TD width="330" class="CellBGR3" align="left"><SELECT name="userStatus" class="COMBO">
                <OPTION value="1" selected> <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Staff")%> </OPTION>
                <OPTION value="2"> <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Collaborator")%> </OPTION>
                <OPTION value="3"> <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Externalviewer")%> </OPTION>
                <OPTION value="4"> <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Outplaced")%> </OPTION>
            </SELECT>
		</TD>
        </TR>
                <TR>
            <TD class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Toolsrole")%></P>
            </TD>
            <TD class="CellBGR3"><SELECT name="toolsRole" class="COMBO">
                <option value="1000000000" > <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Developer")%> </option>
				<option value="1100000000" > <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.ProjectLeader")%> </option>
				<option value="1110000000" > <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.GroupLeader")%> </option>
				<option value="1000100000" > PQA </option>
				<option value="1111110000" > <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Manager")%> </option>
                <option value="0000001000" > <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Externalofprojectlevel")%> </option>
                <option value="0000001100" > <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Externalofgrouplevel")%> </option>
                <option value="0000000010" > <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Communicator")%> </option>
            </SELECT>
			</TD>
        </TR>
         <TR>
            <TD width="111" class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Email")%>*</P>
            </TD>
            <TD width="330" class="CellBGR3"><INPUT size="33" type="text" name="userEmail" maxlength="80"></TD>
		</TR>
		<TR>
            <TD width="111" class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.StartDate")%>*</P>
            </TD>
            <TD width="330" class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="userStartDate" >(DD-MMM-YY)</TD>
		</TR> 
    </TBODY>
</TABLE>
<BR>
</DIV>
<DIV align="left">
<TABLE width="80%" cellspacing="1" class="Table">
    <TBODY>
        <TR>
            <TD align="center" class="ColumnLabel">
            <P><B><FONT size="2" face="Verdana"><%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Workunitname")%></FONT></B> </P>
            </TD>
            <TD align="center" class="ColumnLabel">
            <P><%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Rolename")%></P>
            </TD>
        </TR>
        <%
	for(int i = 0; i < 5; i++)
	{
		String className = "";
  		if (i%2==0) className="CellBGRnews";
  		else className = "CellBGR3";	
			
	%>
        <TR>
            <TD align="center" class="<%=className%>"><SELECT name="workUnit" class="COMBO">
                <OPTION value="-1">N/A</OPTION>
                <%	
					for(int j = 0; j < wuVector.size(); j++)
					{	
					 
						
						WorkUnitInfo wuInfo = (WorkUnitInfo)wuVector.elementAt(j);
						long wuID = wuInfo.workUnitID; 
						//if (wuID.trim().equals("admin")) continue;
						String wuName = wuInfo.workUnitName;
						
						
					%><OPTION value=<%=wuID%>><%=wuName%></OPTION>
                <%	}
				%>
            </SELECT></TD>
            <TD align="center" class="<%=className%>"><SELECT name="rightGroup" class="COMBO">
                <OPTION value="-1">N/A</OPTION>
                <%	
					for(int j = 0; j < rgVector.size(); j++)
					{	
					
						
						RightGroupInfor rgInfo = (RightGroupInfor)rgVector.elementAt(j);
						String rgID = rgInfo.rightGroupID;
						//if (rgID.trim().equals("admin")) continue;
						//String wuName = wuInfo.workUnitName;
						
						
					%>
                <OPTION value="<%=rgID%>"><%=rgID%></OPTION>
                <%	}
				%>
            </SELECT></TD>
        </TR>
        <%}
%>
    </TBODY>
</TABLE>
&nbsp; </DIV>

</FORM>
<INPUT type="button" name="update2" value="<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.OK")%>" onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="back2" value="<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Cancel")%>" onclick="jumpURL('UserProfiles.jsp');" class="BUTTON">



</BODY>

<script language = "javascript">
	function on_Submit()
	{
		var stringTemp;
		stringTemp = document.frmAdd.userID.value;
		if((stringTemp == null) || (trim(stringTemp) == ""))
		{
			alert("<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.YoumustenterUserID")%>");
			document.frmAdd.userID.focus();
			return;
		}
		
		stringTemp = document.frmAdd.userPassword.value;
		if((stringTemp == null) || (trim(stringTemp) == ""))
		{
			alert("<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Youmustenterpassword")%>");
			document.frmAdd.userPassword.focus();
			return;
		}
		
		stringTemp = document.frmAdd.userName.value;
		if((stringTemp == null) || (trim(stringTemp) == ""))
		{
			alert("<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Youmustenterusername")%> ");
			document.frmAdd.userName.focus();
			return;
		}
		if (!mandatoryFld(frmAdd.userDesignation,"<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Position")%> ")) {
			return;
		}
		stringTemp = document.frmAdd.userEmail.value;
		if (!emailCheck(stringTemp)) {
			document.frmAdd.userEmail.focus();
			return;
		}
		if (!mandatoryFld(frmAdd.userStartDate,"<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.StartDate")%> ")) {
			return;
		}
		
		if(!dateFld(frmAdd.userStartDate, "<%=languageChoose.getMessage("fi.jsp.FinanAdd.Invaliddateformat")%> ")) {  	  
	  		return;
	  	}
		var stringTemp2;
		for(i = 1; i <= 4; i++){
			for(j = i+1; j <=5; j++){
				stringTemp = document.frmAdd.elements[i*2 + 7].value;
				stringTemp2 = document.frmAdd.elements[j*2 + 7].value;
				if((stringTemp != "-1") && (stringTemp2 != "-1")){
					if(stringTemp == stringTemp2){
						alert("<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Youcannotchose2similarWorkunit")%>");
						return;
					}
				}
			}
		}
		
		for(i = 1; i <= 5; i++){
			stringTemp = document.frmAdd.elements[i*2 + 7].value;
			stringTemp2 = document.frmAdd.elements[i*2 + 8].value;
			if(((stringTemp == "-1") && (stringTemp2 != "-1")) || ((stringTemp != "-1") && (stringTemp2 == "-1"))){
				alert("<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.YoumustchooseoneWork")%>");
				return;
			}
		}
		
		for(i = 1; i <= 5; i++){
			stringTemp = document.frmAdd.elements[i*2 + 7].value;
			stringTemp2 = document.frmAdd.elements[i*2 + 8].value;
			if((stringTemp == "admin") ||(stringTemp2 == "admin")){
				alert("<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.Itisimposibleto")%>");
				return;
			}
		}
		
		
		document.frmAdd.submit();
	}	
</script> 
</HTML>
