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
<TITLE>woSubTeamAdd.jsp</TITLE>
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
	int nRow = 1;
	int maxRow = SubTeams.NUMBER_OF_ROW_ADDABLE;
	int subTeamDisplayed;
	String rowStyle;	
	
	SubTeamInfo subTeamInfo = null;
	AssignSubTeamInfo assignSubTeamInfo = null;
	
	Vector vErrSubTeam = (Vector) request.getAttribute("WOErrSubTeamList");
	if (vErrSubTeam != null) {
		nRow = vErrSubTeam.size();
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
%>
<BODY onLoad="loadPrjMenu();frmSubTeamAdd.subteam_name[0].focus();" class="BD">

<form name ="frmSubTeamAdd" method= "post" action = "Fms1Servlet#SubTeam">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.AddNewSubTeams")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
	if ( "2".equals(strErr) ) {
%>
<P class="ERROR">Added subteam(s) in failure! The name(s) have already existed. </P>
<%	
	}
%>	

<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width = "24" align = "center"> # </TD>
			<TD width="150" style="width:150px"> <%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.SubTeamName")%>* </TD>
			<TD width="300" style="width:300px"> <%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.Note")%> </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (vErrSubTeam != null)  subTeamInfo = (SubTeamInfo) vErrSubTeam.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="subTeam_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD align = "center" width = "24"> <%=row + 1%> </TD>
			<TD> 
				<INPUT name="subteam_name" value ="<%=(vErrSubTeam == null)? "":subTeamInfo.teamName%>" size="30"/> 
			</TD>
			<TD height="50">
			 <TEXTAREA rows="3" cols = "50" name="subteam_note"><%=(vErrSubTeam == null)? "":subTeamInfo.teamNote%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	subTeamDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="subTeam_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD valign="top"> <%=row + 1%></TD>
			<TD> <input type ="text" name="subteam_name" size="30"> </TD>			
			<TD height="50"> <TEXTAREA rows="3" cols = "50" name="subteam_note" ></TEXTAREA> </TD>			
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="subTeam_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.AddMoreSubTeams")%> </a></p>
<BR>
<input type ="hidden" name = "reqType" value = ""/>
<INPUT type="button" name="woSave" value="<%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.Save")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="woCancel" value="<%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.Cancel")%>" class="BUTTON" onclick="doCancel();">
</form>

<SCRIPT language="JavaScript">

var subTeam_nextHiddenIndex = <%=subTeamDisplayed + 1%>;
function addMoreRow() {
     getFormElement("subTeam_row" + subTeam_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 subTeam_nextHiddenIndex++;
	 if(subTeam_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("subTeam_addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(subTeam_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("subTeam_addMoreLink").style.display = "none";    
}

function addSubmit(){	
	if (checkValid()) {
		frmSubTeamAdd.reqType.value=<%=Constants.WO_SUB_TEAM_ADD%>;	
		frmSubTeamAdd.submit();
	} else return false;	
}

function doCancel(){
	frmSubTeamAdd.reqType.value =<%=Constants.WO_TEAM_GET_LIST%>;
	frmSubTeamAdd.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("subteam_name");
	var arrTxt1= document.getElementsByName("subteam_note");
	var length = subTeam_nextHiddenIndex-1;
	var checkAllBlank = 0;
	
	for(i=0; i < length;i++) {
		if (trim(arrTxt[i].value) =='')  {
			if (trim(arrTxt1[i].value) !='') {
				alert("Name is mandatory !");
				arrTxt[i].focus();
				arrTxt[i].select();
				return false;
			} else checkAllBlank++;
		} else {
			if(!maxLength(arrTxt[i],"Name must be less than 20 characters",20))
			return false;
			
			if(!maxLength(arrTxt1[i],"Note must be less than 200 characters",200))
			return false;
			
			idx=contains(arrTxt,arrTxt[i],i);
			if (idx != -1) {
				alert("Name must be unique !");
				arrTxt[idx].focus();
				arrTxt[idx].select();
				return false;
			}
		}
	}
	
	if (checkAllBlank==length) {
		alert("Please input data");
		arrTxt[0].focus();
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
