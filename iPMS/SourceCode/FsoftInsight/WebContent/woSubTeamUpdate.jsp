<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<style type="text/css">
.ajaxtooltip{
position: absolute; /*leave this alone*/
display: none; /*leave this alone*/
width: 300px;
left: 0; /*leave this alone*/
top: 0; /*leave this alone*/
background: lightyellow;
border: 2px solid gray;
border-width: 1px 2px 2px 1px;
padding: 5px;
}
</style>
<script type="text/javascript" src="jscript/ajaxtooltip.js">
</script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>woSubTeamUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 0;
	boolean isOver = false;
	String rowStyle;
	int subTeamDisplayed;
	
	Vector vSubTeamList = (Vector) session.getAttribute("WOSubTeamList");
	Vector vErrSubTeamList = (Vector) request.getAttribute("WOErrSubTeamList");
	
	if (vErrSubTeamList != null) {
		isOver = true;
     	request.removeAttribute("WOErrSubTeamList");
	}
	
	SubTeamInfo subTeamInfo = null;	
	
	String strErr = (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
	
	int right = Security.securiPage("Work Order",request,response); 
	String archiveStatus = (String)session.getAttribute("archiveStatus");
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.UpdateSubTeams")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">System error</P>
<%
	} 
	if ("2".equals(strErr)) {	
		Vector vAssignedSubTeamData = (Vector) request.getAttribute("WOAssSubTeam");
		AssignSubTeamInfo assignSubTeamInfo = new AssignSubTeamInfo();
%>
<P class="ERROR">Unable to delete! Sub team has member </P>
<br/>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> Assignment </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD valign = "middle" width = 200> Name </TD>
			<TD width = 300> Account </TD>
		</TR>
	</THEAD>
	<TBODY>	
<%		
		// Display current list (last updated data)
		int count = vAssignedSubTeamData.size();
		for (int i = 0; i < count; i++) {
			assignSubTeamInfo = (AssignSubTeamInfo) vAssignedSubTeamData.elementAt(i);
			rowStyle = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR class="<%=rowStyle%>" valign="top">
			<TD><%=assignSubTeamInfo.subTeamName%></TD>
			<TD><%if(right == 3 && !isArchive){%><a href="Fms1Servlet?reqType=<%=Constants.WO_TEAM_MNG%>&woTeam_assID=<%=assignSubTeamInfo.assID%>"><%}%><%=ConvertString.toHtml(assignSubTeamInfo.account)%><%if(right == 3 && !isArchive ){%></a><%}%></TD>
		</TR>
<%
		}
%>		
	</TBODY>
</TABLE>
<%	
	}
%>
<BR>
<form name ="frmSubTeamUpdate" method= "Post" action ="Fms1Servlet#SubTeam">
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width ="24" align = "center"> &nbsp; </TD>
			<TD width = "24" align = "center"> # </TD>
			<TD width = "150"> <%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.SubTeamName")%>* </TD>
			<TD width = "300"> <%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.Note")%> </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrSubTeamList;
	else vTemp = vSubTeamList;
	
	nRow = vTemp.size();
	for (; row < nRow; row++) {
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		subTeamInfo = (SubTeamInfo) vTemp.elementAt(row);
%>
		<TR id="subTeam_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD width = "24" align = "center"> <A href="javascript:OnDelete(<%=subTeamInfo.teamID%>)"><IMG src="image/delete1.gif" title="Delete"></img></A></TD>
			<TD width = "24" align = "center"> <%=row + 1%> <INPUT type="hidden" name="subteam_id" value="<%=subTeamInfo.teamID %>"> </TD>
			<TD> <input type ="text" name="subteam_name" maxlength ="20" value="<%=subTeamInfo.teamName%>"></TD>
			<TD height="57"> <TEXTAREA rows="3" cols="50" name="subteam_note" ><%=(subTeamInfo.teamNote==null)?"":subTeamInfo.teamNote%></TEXTAREA> </TD>
		</TR>
<%
	}
	subTeamDisplayed = row;	// Indicate numbers of lines displayed	
%>		
	</TBODY>
</TABLE>
<BR>
<input type = "hidden" name = "reqType" value = ""/>
<input type = "hidden" name = "subTeamID" value = ""/>

<INPUT type="button" name="woSubTeamUpdateSave" value="<%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.Save")%>" class="BUTTON" onclick="updateSubmit();">
<INPUT type="button" name="woSubTeamUpdateCancel" value="<%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.Cancel")%>" class="BUTTON" onclick="doBack()">
</form>

<SCRIPT language="JavaScript">
function OnDelete(paraSubTeamID){
	if (window.confirm("Are you sure to delete this sub team ?")!=0) {
		frmSubTeamUpdate.reqType.value =<%=Constants.WO_SUB_TEAM_DELETE%>;
		frmSubTeamUpdate.subTeamID.value =paraSubTeamID;
		frmSubTeamUpdate.submit();
	}
}
function updateSubmit(){	
	if (checkValid()) {
		frmSubTeamUpdate.reqType.value =<%=Constants.WO_SUB_TEAM_UPDATE%>;
		frmSubTeamUpdate.submit();
	} else return false;	
}

function doBack(){
	frmSubTeamUpdate.reqType.value =<%=Constants.WO_TEAM_GET_LIST%>;
	frmSubTeamUpdate.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("subteam_name");
	var arrTxt1= document.getElementsByName("subteam_note");
	for(i=0; i < arrTxt.length;i++) {
		if (trim(arrTxt[i].value) =='')  {			
			alert("Name is mandatory !");
			arrTxt[i].focus();
			arrTxt[i].select();
			return false;			
		} else {
			idx=contains(arrTxt,arrTxt[i],i);
			if (idx != -1) {
				alert("Name must be unique !");
				arrTxt[idx].focus();
				arrTxt[idx].select();
				return false;
			}
		}
		if(!maxLength(arrTxt[i],"Name must be less than 20 characters",20))
		return false;
		
		if(!maxLength(arrTxt1[i],"Note must be less than 200 characters",200))
		return false;
	}
	return true;
}

function trim(sString)
{
	while (sString.substring(0,1) == ' ')
	{
		sString = sString.substring(1, sString.length);
	}
	while (sString.substring(sString.length-1, sString.length) == ' ')
	{ 
		sString = sString.substring(0,sString.length-1);
	}
	return sString;
}

function contains(a,e,idx) {
	for(j=a.length-1;j>=0;j--) {
		if (j==idx) continue;
		if (a[j].value==e.value) return j;  
	}
	return -1;
}
</SCRIPT>
</BODY>
</HTML>
