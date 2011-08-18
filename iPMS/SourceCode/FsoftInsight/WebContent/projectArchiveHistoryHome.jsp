<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" errorPage="error.jsp?error=Please re-login" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>projectArchiveHome.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<SCRIPT>
	function checkValiData(){
		var startDate;
		var endDate;
		startDate = document.frm.txtCloseFrom.value;
		endDate = document.frm.txtCloseUntil.value;
		if(startDate != "" ){
			if(!checkDateWithFullYear(startDate)){			
				document.frm.txtCloseFrom.focus();
				return false;
			}
		}
		if(endDate != "" ){
			if(!checkDateWithFullYear(endDate)){
				document.frm.txtCloseUntil.focus();
				return false;
			}
		}
		if(endDate != "" && startDate != ""){
			if(compareDate( startDate, endDate) < 0){
				alert("Archive from must be before Archive until ");
				document.frm.txtCloseFrom.focus();
				return false;
			}		
		}	
		return true;
	}
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY  class="BD" onload="loadAdminMenu()">
<%
    Vector vtProjectInfo = (Vector)session.getAttribute("ProjectArchiveHistoryFilterInfo");
    Vector vtGroupRole = (Vector)session.getAttribute("groupList");
    String strGroup, strStatus, strRank;
    Vector customerList;
    int currentPage;
    // default select for status combo
    int status = -1;
    //Save filter parameters in the session
    
    if(session.getAttribute("txtCloseUntilSession")==null)
    	session.setAttribute("txtCloseUntilSession","");
	if(session.getAttribute("txtCloseFromSession")==null)    	
		session.setAttribute("txtCloseFromSession","");
    
	if (request.getParameter("selGroup")!=null)
		session.setAttribute("selGroupSession",request.getParameter("selGroup"));		
	if (request.getParameter("selStatus")!=null)
		session.setAttribute("selStatusSession",request.getParameter("selStatus"));		
	if(request.getParameter("selRank")!=null)
		session.setAttribute("selRankSession",request.getParameter("selRank"));	
	if(request.getParameter("txtCloseFrom")!=null)
		session.setAttribute("txtCloseFromSession",request.getParameter("txtCloseFrom"));	
	if(request.getParameter("txtCloseUntil")!=null)
		session.setAttribute("txtCloseUntilSession",request.getParameter("txtCloseUntil"));	
	if(request.getParameter("pageCombobox") != null){
		session.setAttribute("pageComboboxSession",request.getParameter("pageCombobox"));		
	}
	else{
		if(session.getAttribute("pageComboboxSession") == null)
			session.setAttribute("pageComboboxSession","1");		
	}
	if(request.getParameter("selCustomer")!=null){
		session.setAttribute("selCustomerSession",request.getParameter("selCustomer"));	
	}else{
		if(session.getAttribute("selCustomerSession")==null)
			session.setAttribute("selCustomerSession","-1");	
	}
	if(session.getAttribute("pageComboboxSession")==null){
		session.setAttribute("pageComboboxSession","1");	
	}	
	
	strGroup = (String)session.getAttribute("selGroupSession");	
	strStatus = (String)session.getAttribute("selStatusSession");		
	strRank = (String)session.getAttribute("selRankSession");
	currentPage = Integer.parseInt((String)session.getAttribute("pageComboboxSession"));
	customerList = (Vector)session.getAttribute("CustomerArchiveList");
	String strCustomer = (String)session.getAttribute("selCustomerSession");		
%>

<p class="TITLE"><%="Archive History"%></p>
<%if ((vtProjectInfo == null)||(vtProjectInfo.size()==0)){ %>
<P class="ERROR"><%="no result for project archived history"%></P>
<%}else {
String strClassStyle;
String pageCombobox = "";
if (vtProjectInfo.size() >= 1) {
		long wuGroup=(strGroup==null||"".equals(strGroup))?0:CommonTools.parseLong(strGroup);
	if (strStatus!=null&&!"".equals(strStatus))
		status=Integer.parseInt(strStatus);
	if (strRank==null) strRank = "All";
	//System.out.println("projectArchiveHistoryHome.jsp, size of vtProjectInfo = "+vtProjectInfo.size());
	//HuyNH2 add code for count size of compatible condition.
	int resultSize = 0;
   	for (int i = 0; i < vtProjectInfo.size(); i++) 
   	{
       	ProjectInfo prjInfo = (ProjectInfo)vtProjectInfo.elementAt(i);
       	if ((wuGroup == 0||wuGroup == prjInfo.getParent())
       		&&(status == -1||(status == prjInfo.getArchiveStatus()))
			&&("All".equals(strRank)||strRank.equals(prjInfo.getProjectRank())||(prjInfo.getProjectRank()==null && "N/A".equals(strRank)))
  			&& CommonTools.dateCompare((String)session.getAttribute("txtCloseFromSession"),(String)session.getAttribute("txtCloseUntilSession"),prjInfo.getActualFinishDate())
  			&&(strCustomer.equals("-1")|| strCustomer.toUpperCase().trim().equals(prjInfo.getCustomer().toUpperCase().trim()))
  			)
       	{
       		resultSize++;
       	}
    }
    // number of page
    //int numberPage = resultSize % Constants.PROJECT_ARCHIVE_MAX_NUMBER_ARCHIVE==0?(resultSize/Constants.PROJECT_ARCHIVE_MAX_NUMBER_ARCHIVE):(resultSize/Constants.PROJECT_ARCHIVE_MAX_NUMBER_ARCHIVE)+1;
    int numberPage = resultSize/Constants.PROJECT_ARCHIVE_MAX_NUMBER_ARCHIVE;
    if(numberPage * Constants.PROJECT_ARCHIVE_MAX_NUMBER_ARCHIVE < resultSize)
    	numberPage = numberPage+1;
    while(currentPage > numberPage){
    	currentPage--;
    }
    String goNext = "";
    String goBack = "";
    if(currentPage < numberPage){
    	int pageNext = 	currentPage + 1;
    	goNext = " <a href='projectArchiveHistoryHome.jsp?pageCombobox="+ pageNext+"'>Next</a>";
    }
    if(currentPage > 1){
    	int pageBack = currentPage - 1;
		goBack = " <a href='projectArchiveHistoryHome.jsp?pageCombobox="+pageBack+"'>Prev</a>";    
    }
	if(numberPage > 0){
		pageCombobox += "<select name=\"pageCombobox\" onChange=\"this.form.submit();\" class=\"COMBO\">";
		for(int i = 1; i <= numberPage; i++){
			if(currentPage==i)
				pageCombobox += "<option value='"+i+"' selected>"+i+"</option>";
			else
				pageCombobox += "<option value='"+i+"'>"+i+"</option>";
		}
		pageCombobox += "</select> of "+numberPage;		
	}
	pageCombobox =  pageCombobox + goBack + goNext;
%>
<FORM action="projectArchiveHistoryHome.jsp" name="frm" method="post" onsubmit="return checkValiData();">
<TABLE>
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.projectHome.Filterprojects")%> </CAPTION>
	<TBODY>
		<TR class="NormalText">
			<TD><%=languageChoose.getMessage("fi.jsp.projectHome.Group")%> </TD>
			<TD>
				<SELECT name="selGroup" class="COMBO">
				<OPTION value="0" <%=((wuGroup==0)?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.All")%> </OPTION>
				<%for (int i = 0; i < vtGroupRole.size(); i++){
					RolesInfo groupInfo = (RolesInfo) vtGroupRole.elementAt(i);
					%><OPTION value="<%=groupInfo.workUnitID%>"<%=((groupInfo.workUnitID == wuGroup)? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
				<%}%>
				</SELECT>
			</TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Rank")%> </TD>
			<TD>
				<SELECT name="selRank" class="COMBO">
					<OPTION value="All" <%=(("All".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.All")%> </OPTION>
					<OPTION value="N/A" <%=(("N/A".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.NA")%> </OPTION>
					<OPTION value="?" <%=(("?".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.NR")%> </OPTION>
					<OPTION value="A" <%=(("A".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.A")%> </OPTION>
					<OPTION value="B" <%=(("B".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.B")%> </OPTION>
					<OPTION value="C" <%=(("C".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.C")%> </OPTION>
					<OPTION value="D" <%=(("D".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.D")%> </OPTION>
				</SELECT>
			</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.projectHome.ArchiveFrom")%></TD>
			<TD><input name="txtCloseFrom" class="COMBO" value="<%=session.getAttribute("txtCloseFromSession")%>" maxlength="11" size="11"><%="(dd-mmm-yyyy)"%></TD>
		</TR>
		<TR class="NormalText">
			<TD><%=languageChoose.getMessage("fi.jsp.projectHome.Customer")%></TD>
			<TD>
				<SELECT name="selCustomer" class="COMBO">
					<OPTION value="-1">All</OPTION>
				<%
					for(int i = 0; i < customerList.size(); i++){
						String txtCustomer = (String)customerList.elementAt(i) ; 
				%>	
						<OPTION value="<%=txtCustomer %>" <%=strCustomer.equals(txtCustomer)?"selected":""%>> 
						<%
							if(txtCustomer.length() > 20){
						%>
								<%=txtCustomer.substring(0,20)%>
						<%
							}
							else{
						%>
								<%=txtCustomer%>	
						<%
							}
						%>
						</OPTION>
				<%
					}
				%>
				</SELECT>
			</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.projectHome.ArchivedStatus")%></TD>
			<TD>
				<SELECT name="selStatus" class="COMBO">
					<OPTION value = "-1"<%=((status == -1)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.projectHome.All")%></OPTION>
                    <OPTION value = "4"<%=((status == 4)?"selected":"")%>><%="Archive"%></OPTION>
					<OPTION value = "0"<%=((status == 0)?"selected":"")%>><%="Not Archive"%></OPTION>
				</SELECT>
			</TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.ArchiveUntil")%> </TD>
			<TD><input name="txtCloseUntil" class="COMBO" value="<%=session.getAttribute("txtCloseUntilSession")%>" maxlength="11" size="11"><%="(dd-mmm-yyyy)"%></TD>
			<TD>
				<INPUT type="submit" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.projectHome.Filter")%> ">
			</TD>
		</TR>
	</TBODY>
</TABLE>
<P></P>
<TABLE cellspacing="1" width="95%" class="Table">
    <CAPTION align="left" class="TableCaption"></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Projectcode")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Group1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Customer")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.ProjectMangage")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.ArchivedStatus")%> </TD>          
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.ArchivedDate")%> </TD>          
        </TR>
        <%
        	int k=0;
        	boolean bl=false;
        	for (int i = 0; i < vtProjectInfo.size(); i++) {
            	ProjectInfo prjInfo = (ProjectInfo)vtProjectInfo.elementAt(i);
            	if ((wuGroup == 0||wuGroup == prjInfo.getParent())
            		&&(status == -1||(status == prjInfo.getArchiveStatus()))
       				&&("All".equals(strRank)||strRank.equals(prjInfo.getProjectRank())||(prjInfo.getProjectRank() == null && "N/A".equals(strRank)))
       				&& CommonTools.dateCompare((String)session.getAttribute("txtCloseFromSession"),(String)session.getAttribute("txtCloseUntilSession"),prjInfo.getActualFinishDate())
       				&&(strCustomer.equals("-1")|| strCustomer.toUpperCase().trim().equals(prjInfo.getCustomer().toUpperCase().trim()))
       				)
            	{
            		k++;
            		if( k <= (currentPage-1)*Constants.PROJECT_ARCHIVE_MAX_NUMBER_ARCHIVE)
            			continue;
            		if(k > (currentPage)*Constants.PROJECT_ARCHIVE_MAX_NUMBER_ARCHIVE)
            			break;
            		strClassStyle =(bl)? "CellBGRnews":"CellBGR3";
            		bl=!bl;
        %>
        <TR class="<%=strClassStyle%>">
            <TD><A href="Fms1Servlet?reqType=<%=Constants.PROJECT_ARCHIVE_HISTORY_DETAIL%>&workUnitID=<%=prjInfo.getProjectId()%>&prev=2"><%=prjInfo.getProjectCode()%></A></TD>
            <TD><%=prjInfo.getGroupName()%></TD>
            <TD><%=CommonTools.formatString(prjInfo.getCustomer())%></TD>
            <TD><%=prjInfo.getLeader()%></TD>
      		<TD>
      			<%
	      			if(prjInfo.getArchiveStatus() == 4){
	      			%>
	      				<%=languageChoose.getMessage("fi.jsp.projectHome.Archive")%>
	      			<%
	      			}
	      			else{
	      			%>
	      				<%=languageChoose.getMessage("fi.jsp.projectHome.NotArchive")%>
	      			<%	
	      			}
      			%>
      		</TD>
      		<TD>
      			<%=CommonTools.dateFormat(prjInfo.getActualFinishDate())%>
      		</TD>
        </TR>
		<% 		}
			//break;
			}
			if (k == 0)
			{
		%>
		<TR>
			<TD colspan="6">
				<P class="ERROR"> <%=languageChoose.getMessage("fi.jsp.projectHome.Noprojectmatchesyourcriteria")%> </P>
			</TD>
		</TR>
		<%
			}
    	}
    	else {
        	ProjectInfo prjInfo = (ProjectInfo)vtProjectInfo.elementAt(0);
        	Fms1Servlet.callPage("Fms1Servlet?reqType=" + Constants.WORKUNIT_HOME + "&workUnitID=" + prjInfo.getProjectId(),request,response);
    	}
		%>
		<TR>
            <TD colspan="3" class="TableLeft" align='left'>
            </TD>
            <TD colspan="3" class="TableLeft" align='right'>&nbsp;
            <%if(pageCombobox != "")%>
				<%="Page "+pageCombobox%>
            </TD>
        </TR>
    </TBODY>
</TABLE>
</FORM>
<script language="JavaScript">	
	//objs to hide when submenu is displayed
	var objToHide=new Array(frm.selGroup,frm.selCustomer);
</script>
<%}%>
</BODY>
</HTML>