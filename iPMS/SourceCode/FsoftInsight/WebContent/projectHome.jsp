<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" errorPage="error.jsp?error=Please re-login" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>projectHome.jsp</TITLE>
<LINK rel="stylesheet" type="text/css" href="stylesheet/fms.css">
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<SCRIPT language="javascript" src='jscript/validate.js'></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY  class="BD" onload="loadBlankMenu()">
<%
    Vector vtProjectInfo = (Vector)session.getAttribute("ProjectFilterInfo");
    Vector vtGroupRole = (Vector)session.getAttribute("groupList");
    //Vector vtRankInfo = (Vector)session.getAttribute("RankList");
    String strGroup, strStatus, strLifecycle, strRank;
    String FromDate, ToDate;
    int status = 0;
    
    //Save filter parameters in the session
	if (request.getParameter("selGroup")!=null)
		session.setAttribute("selGroup",request.getParameter("selGroup"));		
	if (request.getParameter("selStatus")!=null)
		session.setAttribute("selStatus",request.getParameter("selStatus"));		
	if(request.getParameter("selLifecycle")!=null)
		session.setAttribute("selLifecycle",request.getParameter("selLifecycle"));	
	if(request.getParameter("selRank")!=null)
		session.setAttribute("selRank",request.getParameter("selRank"));	
	if(request.getParameter("FromDate")!=null)
		session.setAttribute("FromDate",request.getParameter("FromDate"));
	if(request.getParameter("ToDate")!=null)
		session.setAttribute("ToDate",request.getParameter("ToDate"));
	
	strGroup = (String)session.getAttribute("selGroup");	
	strStatus = (String)session.getAttribute("selStatus");		
	strLifecycle = (String)session.getAttribute("selLifecycle");		      
	strRank = (String)session.getAttribute("selRank");
	FromDate = (String)session.getAttribute("FromDate");
	ToDate = (String)session.getAttribute("ToDate");
	//int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	// get status of data migration
	
%>

<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.projectHome.ProjectHome")%></p>
<%if ((vtProjectInfo == null)||(vtProjectInfo.size()==0)){ %>
<P class="ERROR"><%=languageChoose.getMessage("fi.jsp.projectHome.Youhavenorightinthispage") %></P>
<%}else {
String strClassStyle;
if (vtProjectInfo.size() > 1) {
	long wuGroup=(strGroup==null||"".equals(strGroup))?0:CommonTools.parseLong(strGroup);
	if (strStatus!=null&&!"".equals(strStatus))
		status=Integer.parseInt(strStatus);
	int iLifecycleID=(strLifecycle==null||"".equals(strLifecycle))?-1:CommonTools.parseInt(strLifecycle);
	if (strRank==null) strRank = "All";
%>
<FORM action="projectHome.jsp" name="frm" method="get">
<TABLE >
	<%--<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.projectHome.Filterprojects")%> </CAPTION>--%>
	<TBODY>
		<TR class="TableCaption">
		<TD colspan="6"><%=languageChoose.getMessage("fi.jsp.projectHome.Filterprojects")%></TD>
		</TR>
		<TR class="NormalText">
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Group")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Status")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Lifecycle")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Rank")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.FromDate")%><BR>(yyyy-mm-dd) </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.ToDate")%><BR>(yyyy-mm-dd)</TD>
		</TR>
		<TR>
			<TD>
				<SELECT name="selGroup" class="COMBO">
				<OPTION value="0" <%=((wuGroup==0)?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.All")%> </OPTION>
				<%for (int i = 0; i < vtGroupRole.size(); i++){
					RolesInfo groupInfo = (RolesInfo) vtGroupRole.elementAt(i);
					%><OPTION value="<%=groupInfo.workUnitID%>"<%=((groupInfo.workUnitID == wuGroup)? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
				<%}%>
				</SELECT>
			</TD>
			<TD> 
				<SELECT name="selStatus" class="COMBO">
					<OPTION value ="-1" <%=((status==-1)?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.All")%> </OPTION>
					<OPTION value="1"<%=((status==1)?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.Closed")%> </OPTION>
                    <OPTION value="2"<%=((status==2)?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.Cancelled")%> </OPTION>
                    <OPTION value="3"<%=((status==3)?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.Tentative")%> </OPTION>
                    <OPTION value="0"<%=((status==0)?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.Ongoing")%> </OPTION>
				</SELECT>
			</TD>
			<TD>
				<SELECT name="selLifecycle" class="COMBO">
					<OPTION value="-1" <%=((iLifecycleID==-1)?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.All")%> </OPTION>
					<OPTION value=<%=ProjectInfo.LIFECYCLE_DEVELOPMENT+" "+((iLifecycleID==ProjectInfo.LIFECYCLE_DEVELOPMENT)?"selected":"")%> > <%=languageChoose.getMessage("fi.jsp.projectHome.Development")%> </OPTION>
					<OPTION value=<%=ProjectInfo.LIFECYCLE_MAINTENANCE+" "+((iLifecycleID==ProjectInfo.LIFECYCLE_MAINTENANCE)?"selected":"")%> > <%=languageChoose.getMessage("fi.jsp.projectHome.Maintenance")%> </OPTION>
					<OPTION value=<%=ProjectInfo.LIFECYCLE_OTHER+" "+((iLifecycleID==ProjectInfo.LIFECYCLE_OTHER)?"selected":"")%> > <%=languageChoose.getMessage("fi.jsp.projectHome.Other")%> </OPTION>
				</SELECT>
			</TD>
			<TD>
				<SELECT name="selRank" class="COMBO">
					<OPTION value="All" <%=(("All".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.All")%> </OPTION>
					<OPTION value="N/A" <%=(("N/A".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.NA")%> </OPTION>
					<OPTION value="?" <%=(("?".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.NR")%> </OPTION>
					<OPTION value="A" <%=(("A".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.A")%> </OPTION>
					<OPTION value="B" <%=(("B".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.B")%> </OPTION>
					<OPTION value="C" <%=(("C".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.C")%> </OPTION>
					<OPTION value="D" <%=(("D".equals(strRank))?"selected":"")%>> <%=languageChoose.getMessage("fi.jsp.projectHome.D")%> </OPTION>
					<%--
						for (int i = 0; i < vtRankInfo.size(); i++)
						{
							RankInfo rankInfo = (RankInfo)vtRankInfo.elementAt(i);
					%>
							<OPTION value="<!%=rankInfo.rank%>"<!%=(strRank.equals(rankInfo.rank))?"selected":""%>><!%=rankInfo.rank==null?"N/A":rankInfo.rank%></OPTION>
					<!%
						}
					--%>
				</SELECT>
			</TD>
			<TD>
				<INPUT type="text" name="FromDate" size="12" maxlength="10" value = "<%=((FromDate == null)? "" : FromDate)%>" class="SmallTextbox">
            	<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showFromDate()'>
			</TD>
			<TD>
				<INPUT type="text" name="ToDate" size="12" maxlength="10" value = "<%=((ToDate == null)? "" : ToDate)%>" class="SmallTextbox">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showToDate()'>
			</TD>
			<TD>
				<INPUT type="button" class="BUTTON" onclick="doSubmit()" value=" <%=languageChoose.getMessage("fi.jsp.projectHome.Filter")%> ">
				

			</TD>
			
			
		</TR>
	</TBODY>
</TABLE>

</FORM>
<P></P>
<TABLE cellspacing="1" width="95%" class="Table">
    <%--<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.projectHome.Pleaseselectaproject") %></CAPTION>--%>
    <TBODY>
    	<TR class="TableCaption">
		<TD colspan="7"><%=languageChoose.getMessage("fi.jsp.projectHome.Pleaseselectaproject") %></TD>
		</TR>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Projectcode")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Group1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Customer")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Leader")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Rank")%> </TD>   
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.ActualStartDate")%> </TD> 
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.ActualEndDate")%> </TD>          
        </TR>
        <%
        	int k=0;
        	boolean bl=false;
      		boolean dateSearch = true;
      		boolean FromDateSearch = true;
      		boolean ToDateSearch = true;
        	for (int i = 0; i < vtProjectInfo.size(); i++) 
        	{
            	ProjectInfo prjInfo = (ProjectInfo)vtProjectInfo.elementAt(i);
            	
	      		String start_Date = "";
	      		try{
	      			if(prjInfo.getActualStartDate().getYear() > 0){
	      				start_Date = prjInfo.getActualStartDate().toString();
	      			}
	      		}
	      		catch (Exception e){
	      			start_Date = "1950-01-01";
	      		}
	      		String end_Date = "";
	      		try{
	      			if(prjInfo.getActualFinishDate().getYear() > 0){
	      				end_Date = prjInfo.getActualFinishDate().toString();
	      			}
	      		}
	      		catch (Exception e){
	      			end_Date = "2050-12-31";
	      		}
				if(FromDate != null || ToDate != null){
					if(CommonTools.convertStringToDate("yyyy-MM-dd",start_Date)!= null && CommonTools.convertStringToDate("yyyy-MM-dd",ToDate) != null)
						if(CommonTools.convertStringToDate("yyyy-MM-dd",ToDate).compareTo(CommonTools.convertStringToDate("yyyy-MM-dd",start_Date)) >=0){
							ToDateSearch = true;
						}else{
							ToDateSearch = false;
						}
					if(CommonTools.convertStringToDate("yyyy-MM-dd",FromDate)!= null && CommonTools.convertStringToDate("yyyy-MM-dd",end_Date) != null)
						if(CommonTools.convertStringToDate("yyyy-MM-dd",end_Date).compareTo(CommonTools.convertStringToDate("yyyy-MM-dd",FromDate)) >= 0){
							FromDateSearch = true;
						}else{
							FromDateSearch = false;
						}
					if(FromDateSearch == true && ToDateSearch == true){
						dateSearch = true;
					}else{
						dateSearch = false;
					}
				}
				
            	if ((wuGroup==0||wuGroup==prjInfo.getParent())
            		&&(status==-1||(status==prjInfo.getStatus()))
            		&&(iLifecycleID==-1||iLifecycleID==prjInfo.getLifecycleId())
       				&&("All".equals(strRank)||strRank.equals(prjInfo.getProjectRank())||(prjInfo.getProjectRank()==null && "N/A".equals(strRank)))
       				&&(dateSearch == true))
            	{
            		k++;
            		strClassStyle =(bl)? "CellBGRnews":"CellBGR3";
            		bl=!bl;
        %>
        
        <TR class="<%=strClassStyle%>">
            <TD><A href="Fms1Servlet?reqType=<%=Constants.WORKUNIT_HOME%>&workUnitID=<%=prjInfo.getWorkUnitId()%><%=prjInfo.getExternalStatus() ? "&ex=true":""%>"><%=prjInfo.getProjectCode()%></A></TD>
            <TD><%=prjInfo.getGroupName()%></TD>
            <TD><%=CommonTools.formatString(prjInfo.getCustomer())%></TD>
            <TD><%=prjInfo.getLeader()%></TD>
      		<TD>
      			<%
      				String rank;
      				if (prjInfo.getProjectRank() == null)
      					rank = "N/A";
      				else if ("?".equals(prjInfo.getProjectRank()))
						rank = "Not Rank";
					else 
						rank = prjInfo.getProjectRank();
      			%>
      			<%=rank%>
      		</TD>
      		<TD>
      		<%=((start_Date == "1950-01-01")? "" : start_Date)%>
      		</TD> 
      		<TD>
      		<%=((end_Date == "2050-12-31")? "" : end_Date)%>
      		</TD> 
        </TR>
		<% 		}
			}
			if (k == 0)
			{
		%>
		<TR>
			<TD colspan="4">
				<P class="ERROR"> <%=languageChoose.getMessage("fi.jsp.projectHome.Noprojectmatchesyourcriteria")%> </P>
			</TD>
		</TR>
		<%
			}
    	}
    	else 
    	{
        	ProjectInfo prjInfo = (ProjectInfo)vtProjectInfo.elementAt(0);
        	Fms1Servlet.callPage("Fms1Servlet?reqType=" + Constants.WORKUNIT_HOME + "&workUnitID=" + prjInfo.getWorkUnitId(),request,response);
    	}
		%>
		<TR>
            <TD colspan="7" class="TableLeft">&nbsp;</TD>
        </TR>
    </TBODY>
</TABLE>
<%}%>

<SCRIPT language="javascript">

function doSubmit(){
	if (!isValidForm()) {
        return;
    }
    frm.submit();
}
function doMigrate()
{
	migrate.submit();
}
function isValidForm() {
    if (frm.FromDate.value.length > 0 ) {
    	if (isValidate(frm.FromDate.value)==false) {
    		frm.FromDate.value = "";
			frm.FromDate.focus();
			return false;
		}
    }
    if (frm.ToDate.value.length > 0 ) {
        if (isValidate(frm.ToDate.value)==false) {
        	frm.ToDate.value = "";
    		frm.ToDate.focus();
    		return false;
    	}
    }
    if ((frm.FromDate.value.length > 0) && (frm.ToDate.value.length > 0)) {
        if (CompareValue(frm.FromDate.value , frm.ToDate.value) > 0) {
            alert("From date must be lower than or equal to To date");
            frm.FromDate.focus();
            return false
        }
    }
    return true;
}

function showFromDate(){
	if(frm.FromDate.value == null || frm.FromDate.value ==""){
		frm.FromDate.value = "2008-01-01";
	}
	showCalendar(frm.FromDate, frm.FromDate, "yyyy-mm-dd",null,1,-1,-1,true);
}

function showToDate(){
	if(frm.ToDate.value == null || frm.ToDate.value ==""){
		frm.ToDate.value = "2008-01-01";
	}
	showCalendar(frm.ToDate, frm.ToDate, "yyyy-mm-dd",null,1,-1,-1,true);
}

</SCRIPT>

</BODY>
</HTML>