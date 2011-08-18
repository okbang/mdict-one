<%@page import="com.fms1.tools.*"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.fms1.infoclass.*,java.util.Vector, com.fms1.common.*, com.fms1.web.*" contentType="text/html;charset=UTF-8"%>
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>UserProfileDetail.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload = "loadAdminMenu();"class="BD">
<%
	UserInfo userInfo  = (UserInfo)session.getAttribute("userInfo");
	UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
	String id = userInfo.account;
	String loginID = userLoginInfo.account;
	String password = userInfo.Password;
	String name = userInfo.Name;

	String designation = userInfo.designation;
	String group = userInfo.group;
	String status = userInfo.status;

	Vector rightVector = (Vector)session.getAttribute("rightList");
	Vector wuVector =  (Vector)session.getAttribute("wuList");
	Vector rgVector = (Vector)session.getAttribute("rgList");
    Vector grpVector=(Vector)session.getAttribute("grpVector");

	String numRow = request.getParameter("numRow");

	int intNumRow = 5;
	if (rightVector != null) {
		intNumRow = rightVector.size() + 1;
	}
	if (numRow != null) {
		intNumRow = Integer.parseInt(numRow) + 1;
	}

%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.UserProfileDetail.UpdateUserProfile")%></P>
<FORM name = "frmUpdate" action="Fms1Servlet?reqType=<%=Constants.UPDATE_USER_PROFILE%>" method = "post">
<DIV align="left">
<TABLE width="100%" cellspacing="1" class="Table">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.UserProfileDetail.UpdateUserProfile")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.UserID")%></P>
            </TD>
            <TD class="CellBGR3"><FONT face="Verdana" size="2"><INPUT size="22" type="text" name="userID" value="<%=id.trim()%>" readonly maxlength="20"></FONT><FONT size="2">
		</FONT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Username")%></P>
            </TD>
            <TD class="CellBGR3"><INPUT size="33" type="text" name="userName" value="<%=name.trim()%>" maxlength="30">
		</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Position") %></P>
            </TD>
            <TD class="CellBGR3"><INPUT size="33" type="text" name="userDesignation" value="<%=((designation==null)?"":designation.trim())%>" maxlength="30">
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Group")%> </P>
            </TD>
            <TD class="CellBGR3">
            <SELECT name="userGroup" class="COMBO">
            <%for(int i = 0; i < grpVector.size(); i++){
					WorkUnitInfo wuInfoTemp = (WorkUnitInfo)grpVector.get(i);
			%><OPTION value="<%=wuInfoTemp.workUnitName%>" <%if (group != null){if(group.equalsIgnoreCase(wuInfoTemp.workUnitName)) {%>selected<%}}%>><%=wuInfoTemp.workUnitName%></OPTION>
            <%}%>
            </SELECT>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Status")%></P>
            </TD>
            <TD class="CellBGR3"><SELECT name="userStatus" class="COMBO" onchange="on_change();">
                <OPTION value="1" <% if (status.equals("1")) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Staff")%> </OPTION>
                <OPTION value="2" <% if (status.equals("2")) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Collaborator")%> </OPTION>
                <OPTION value="3" <% if (status.equals("3")) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.ExternalViewer")%> </OPTION>
                <OPTION value="4" <% if (status.equals("4")) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Outplaced")%> </OPTION>
            </SELECT>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Toolsrole")%></P>
            </TD>
            <TD class="CellBGR3"><SELECT name="toolsRole" class="COMBO">
                <option value="1000000000" <%=userInfo.role.equals("1000000000")?"selected":""%> > <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Developer")%> </option>
				<option value="1100000000" <%=userInfo.role.equals("1100000000")?"selected":""%> > <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.ProjectLeader")%> </option>
				<option value="1110000000" <%=userInfo.role.equals("1110000000")?"selected":""%> > <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.GroupLeader")%> </option>
				<option value="1000100000" <%=userInfo.role.equals("1000100000")?"selected":""%> > PQA </option>
				<option value="1111110000" <%=userInfo.role.equals("1111110000")?"selected":""%> > <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Manager")%> </option>
                <option value="0000001000" <%=userInfo.role.equals("0000001000")?"selected":""%> > <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Externalofprojectlevel")%> </option>
                <option value="0000001100" <%=userInfo.role.equals("0000001100")?"selected":""%> > <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Externalofgrouplevel")%> </option>
                <option value="0000000010" <%=userInfo.role.equals("0000000010")?"selected":""%> > <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Communicator")%> </option>
            </SELECT>
			</TD>
        </TR>
        
         <TR>
            <TD class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Email")%>*</P>
            </TD>
            <TD class="CellBGR3"><INPUT size="33" type="text" name="userEmail" value="<%=userInfo.userEmail == null ? "" : userInfo.userEmail%>" maxlength="80">
			</TD>
        </TR>
         <TR>
            <TD class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.StartDate")%>*</P>
            </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text"  maxlength="9"  name="userStartDate" value="<%=userInfo.userStartDate== null ? "" : CommonTools.dateFormat(userInfo.userStartDate)%>" >(DD-MMM-YY)
			</TD>
        </TR>
         <TR>
            <TD class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.QuitDate")%></P>
            </TD>
            <%if (status.equals("4")) {%>
            	 <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="userQuitDate" value="<%=userInfo.userQuitDate == null ? "" : CommonTools.dateFormat(userInfo.userQuitDate)%>">(DD-MMM-YY)
            <%} else {%> 
            	<TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="userQuitDate" value="<%=userInfo.userQuitDate == null ? "" : CommonTools.dateFormat(userInfo.userQuitDate)%>" disabled="disabled" >(DD-MMM-YY)
             <%}%>
			</TD>
        </TR>
        
    </TBODY>
</TABLE>
</DIV>
<br>
<DIV align="left">
<TABLE width="100%" cellspacing="1" class="Table">
    <TBODY>
        <TR class="ColumnLabel">
            <TD align="center">
            <P><%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Workunitname")%></P>
            </TD>
            <TD align="center">
            <P><%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Rolename")%></P>
            </TD>
        </TR>
        <%
	String className = "";        
    if(!((loginID.trim().equals("admin")) &&(id.trim().equals("admin"))) ){    
	for(int i = 0; i < intNumRow; i++){
  		className=(i%2==0)?"CellBGRnews":"CellBGR3";
	%>
        <TR>
            <TD align="center" class="<%=className%>"><SELECT name="workUnit" class="COMBO">
                <OPTION value="-1">N/A</OPTION>
                <%	
					for(int j = 0; j < wuVector.size(); j++){
						WorkUnitInfo wuInfo = (WorkUnitInfo)wuVector.elementAt(j);
						long wuID = wuInfo.workUnitID;
						//if (wuID.trim().equals("admin")) continue;
						String wuName = wuInfo.workUnitName;
						if(i < rightVector.size()){
							RolesInfo rightInfo = (RolesInfo) rightVector.elementAt(i);
							long rightWUID = rightInfo.workUnitID;
							if(rightWUID== wuID){
							%><OPTION value="<%=wuID%>" selected><%=wuName%></OPTION>
                			<%}else{
							%><OPTION value="<%=wuID%>"><%=wuName%></OPTION>
                			<%}
						}
						else{
						
					%>
                <OPTION value="<%=wuID%>"><%=wuName%></OPTION>
                <%}	
            		}
				%>
            </SELECT></TD>
            <TD align="center" class="<%=className%>"><SELECT name="rightGroup" class="COMBO">
                <OPTION value="-1">N/A</OPTION>
                <%	
					for(int j = 0; j < rgVector.size(); j++){
						RightGroupInfor rgInfo = (RightGroupInfor)rgVector.elementAt(j);
						String rgID = rgInfo.rightGroupID.trim();
						
						if(i < rightVector.size()){
							RolesInfo rightInfo = (RolesInfo) rightVector.elementAt(i);
							String rightRGID = rightInfo.rightGroupID.trim();
							
								if(rightRGID.equalsIgnoreCase(rgID)){
							%>
				                <OPTION value="<%=rgID%>" selected><%=rgID%></OPTION>
			                <%}else{
			                %>
				                <OPTION value="<%=rgID%>"><%=rgID%></OPTION>
                			<%
                				}					
                		}else{

                			%>
				                <OPTION value="<%=rgID%>"><%=rgID%></OPTION>
                <%
                	}
            	}
				%>
            </SELECT></TD>
        </TR>
        <%
        }
        }
        else
        {
        %>
        <TR>
            <TD align="center" class="CellBGRnews"> <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Administrator")%> </TD>
            <TD align="center" class="CellBGRnews"> <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.admin")%> </TD>
        </TR>
        <INPUT type="hidden" name="workUnit" value="admin">
			<INPUT type="hidden" name="rightGroup" value="admin">	
        <%for(int i = 1; i < intNumRow; i++){%>
			<INPUT type="hidden" name="workUnit" value="N/A">
			<INPUT type="hidden" name="rightGroup" value="N/A">			
			<%}
		}%>
        <TR align="center" class="<%=className%>">
        <TD><A href="UserProfileDetail.jsp?numRow=<%=intNumRow%>"> <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.AddnewaWorkUnitRole")%> </A></TD>
        <TD></TD>
        </TR>
    </TBODY>
</TABLE>
</DIV>
<INPUT type="hidden" name="updateUserProfile" value="update"></FORM>
<BR>
<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.UserProfileDetail.OK")%> " onclick="on_Submit();" class="BUTTON">
<INPUT type="button" name="back2" value="<%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Cancel")%>" onclick="jumpURL('UserProfiles.jsp');" class="BUTTON">


<script language = "javascript">
	
	var varNumRow = <%=intNumRow%>;
	function on_change(){
		var stringTemp;
		stringTemp = document.frmUpdate.userStatus.value;
		if( stringTemp == "4") {	    	
	    	document.frmUpdate.userQuitDate.disabled="";	    	
	    	document.frmUpdate.userQuitDate.focus();
	    }  else {
	    	document.frmUpdate.userQuitDate.disabled="disabled";
 	    }
	}
	
	function on_Submit()
	{
		var stringTemp;
		
		stringTemp = document.frmUpdate.userName.value;
		if((stringTemp == null) || (trim(stringTemp) == ""))
		{
			alert("<%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Youmustenterusername")%>");
			document.frmUpdate.userName.focus();
			return;
		}
		if (!mandatoryFld(frmUpdate.userDesignation,"<%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Position")%>")) {
			return;
		}
		stringTemp = document.frmUpdate.userEmail.value;
		if (!emailCheck(stringTemp)) {
			document.frmUpdate.userEmail.focus();
			return;
		}
		var sStartDate=frmUpdate.userStartDate.value; 
		var sQuitDate=frmUpdate.userQuitDate.value;
		if(!mandatoryFld(frmUpdate.userStartDate,"<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.StartDate")%> ")){
	  	  	return;  			  	  		
	  	}
	  	if(!dateFld(frmUpdate.userStartDate, "<%=languageChoose.getMessage("fi.jsp.FinanAdd.Invaliddateformat")%> ")){  	  
	  		  return;
	  	}
	  	if (document.frmUpdate.userStatus.value == "4") {
	  		if(!mandatoryFld(frmUpdate.userQuitDate,"<%=languageChoose.getMessage("fi.jsp.UserProfileAddNew.QuitDate")%> ")){
		  	  	return;  			  	  		
	  		}
	  		if (!dateFld(frmUpdate.userQuitDate, "<%=languageChoose.getMessage("fi.jsp.FinanAdd.Invaliddateformat")%> ") ){  	  
	  		   return;
	  		}
		  	if (compareDate(sStartDate,sQuitDate)==-1) {
		  		window.alert("<%=languageChoose.getMessage("fi.jsp.FinanAdd.CompareDate")%> ");
		  		frmUpdate.userQuitDate.focus();
		  		return;
		  	}
	  	}
	  	
		var stringTemp2;
		for(i = 1; i <= varNumRow; i++){
			for(j = i+1; j <=varNumRow; j++){
				stringTemp = document.frmUpdate.elements[i*2 + 7].value;
				stringTemp2 = document.frmUpdate.elements[j*2 + 7].value;
				if((stringTemp != "-1") && (stringTemp2 != "-1")){
					if(stringTemp == stringTemp2){
						alert("<%=languageChoose.getMessage("fi.jsp.UserProfileDetail.Youcannotchose2similarWorkunit")%>");
						return;
					}
				}
			}
		}
		
		for(i = 1; i <= varNumRow; i++){
			stringTemp = document.frmUpdate.elements[i*2 + 7].value;
			stringTemp2 = document.frmUpdate.elements[i*2 + 8].value;
			if(((stringTemp == "-1") && (stringTemp2 != "-1")) || ((stringTemp != "-1") && (stringTemp2 == "-1"))){
				alert("<%=languageChoose.getMessage("fi.jsp.UserProfileDetail.YoumustchooseoneWork")%>");
				return;
			}
		}		
	
		document.frmUpdate.submit();
	}	
</script> 
</BODY>
</HTML>
