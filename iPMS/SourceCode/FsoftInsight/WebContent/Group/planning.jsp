<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.* ,com.fms1.common.group.* ,java.util.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="../javaFns.jsp"%>
</SCRIPT>
<TITLE>Norm Plan</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
int right = Security.securiPage("Organization plan",request,response);
PlanningInfo planningInfo=(PlanningInfo)session.getAttribute("planningInfo");

String error=request.getParameter("error");
if (error==null)error="";
int i;
%>
<BODY class="BD" onload="loadOrgMenu()">
<P class="TITLE"> <%=languageChoose.paramText(new String[]{"fi.jsp.planning.~PARAM1_PLAN_TYPE~Plan",planningInfo.planType})%></P>
<P class="ERROR"><%=languageChoose.getMessage(error)%></P>
<FORM action="Fms1Servlet?reqType=<%=Constants.LOADPLANNING%>&section=<%=planningInfo.planType%>" name="frm" method="post">
<TABLE >
	<TBODY>
		<TR class="NormalText">
			<TD> <%=languageChoose.getMessage("fi.jsp.planning.Year2")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.planning.Version")%> </TD>
			<TD></TD>
			<TD width ="500%"></TD>
		</TR>
		<TR>
			<TD>
				<SELECT name="txtYear" class="COMBO" >
					<%
					int startYear = 2000;
					java.text.SimpleDateFormat  yearFrmt= new java.text.SimpleDateFormat("yyyy");
					int endYear = Integer.parseInt(yearFrmt.format(new java.util.Date()))+1;
					String selected;
	            	for (int year =endYear;year>=startYear;year--){
	            		selected=(planningInfo.year==year)?" selected":"";
	            		%><OPTION value=<%=year+selected%>><%=year%></OPTION>
	                <%}%>
				</SELECT>
			</TD>
			<TD> 
				<SELECT name="txtVersion" class="COMBO">
					<OPTION value ="1"<%=((planningInfo.version==1f)?" selected":"")%>> 1.0 </OPTION>
					<OPTION value ="1.1"<%=((planningInfo.version==1.1f)?" selected":"")%>> 1.1 </OPTION>
				</SELECT>
			</TD>
			<TD>
				<INPUT type="submit" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.planning.Go")%> ">
			</TD>
			<TD class="NormalText" style="text-align: right" ><%if (right==3){%><INPUT type="checkbox" name="chkAuto">  <%=languageChoose.getMessage("fi.jsp.planning.AutomaticcalculationBeta")%> <%}%></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>

<BR>
<FORM action="Fms1Servlet?reqType=<%=Constants.SAVEPLANNING%>" name="frmSave" method="post">
<INPUT type="hidden" name="section" value="<%=planningInfo.planType%>">
<TABLE cellspacing="1" class="Table" >
<CAPTION class="TableCaption"> <%= languageChoose.paramText(new String[]{"fi.jsp.planning.Year~PARAM1_YEAR~version~PARAM2_NUMBER~last__update~PARAM3_DATE~", String.valueOf(planningInfo.year), String.valueOf(planningInfo.version), CommonTools.dateFormat(planningInfo.lastUpdate)})%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD> <%=languageChoose.getMessage("fi.jsp.planning.ID")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.planning.Metricgroup")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.planning.Unit")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.planning.Year2")%> </TD>
			<%for (i = 1; i < 13; i++){
			%><TD><%=CommonTools.getMonth(i)%></TD>
			<%}%>
			<TD> <%=languageChoose.getMessage("fi.jsp.planning.Assumption")%> </TD>
		</TR>
	<%
		PlanningInfo.Row row;
		for (i=0;i<planningInfo.rows.size();i++){
			row=(PlanningInfo.Row)planningInfo.rows.elementAt(i);
			if (row.groupID==-2){%>
				<TR class="ColumnLabel">
					<TD colspan="17"><%=languageChoose.getMessage(row.metricName)%></TD>
				</TR>
			<%}else{
			
				if (row.groupID==-1){%>
				
				<TR class="CellBGR3">
					<TD><%=row.strMetricID%></TD>
					<TD><%=languageChoose.getMessage(row.metricName)%></TD>
					<TD><%=row.unit%></TD>
				<%}else{%>
				<TR class="CellBGRnews">
					<TD colspan="3">&nbsp;&nbsp;<i><%=row.groupName%><i></TD>
				<%}
				if (right==3){%>
					<TD><INPUT name ="yeartotal<%=i%>"  maxlength="11" size = "5" value="<%=CommonTools.updateDouble(row.yearTotal)%>"></TD>
				<%}else{%>
					<TD><%=CommonTools.formatDouble(row.yearTotal)%></TD>
				<%}
				for (int k = 0; k < 12; k++){
					if (right==3){%><TD><INPUT name ="val<%=CommonTools.getMonth(k+1)+i%>" onChange="<%=row.colFormula+row.rowFormula+row.yearFormula%>" maxlength="11" size = "5" value="<%=CommonTools.updateDouble(row.values[k])%>"></TD>
					<%}else{%><TD><%=CommonTools.formatDouble(row.values[k])%><%}%></TD>
				<%}
				if (right==3){%><TD><INPUT name ="ass<%=i%>"  maxlength="200" size = "30" value="<%=((row.assumption==null)?"":row.assumption)%>"></TD>
				<%}else{%><TD><%=ConvertString.toHtml((row.assumption==null)?"":row.assumption)%></TD><%}%>
				</TR>

			<%}
		}%>
	</TBODY>
</TABLE>
<BR>
<%if (right==3){%>
	<TABLE >
		<TBODY>
			<TR class="NormalText">
				<TD></TD>
				<TD> <%=languageChoose.getMessage("fi.jsp.planning.Year3")%> </TD>
				<TD> <%=languageChoose.getMessage("fi.jsp.planning.Version1")%> </TD>
			</TR>
			<TR>
				<TD>
					<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.planning.Saveas")%> " onClick="check()">
				</TD>
				<TD>
					<SELECT name="txtYear" class="COMBO" >
						<%
		            	for (int year =endYear;year>=startYear;year--){
		            		selected=(planningInfo.year==year)?" selected":"";
		            		%><OPTION value=<%=year+selected%>><%=year%></OPTION>
		                <%}%>
					</SELECT>
				</TD>
				<TD> 
					<SELECT name="txtVersion" class="COMBO">
						<OPTION value ="1"<%=((planningInfo.version==1f)?" selected":"")%>> 1.0 </OPTION>
						<OPTION value ="1.1"<%=((planningInfo.version==1.1f)?" selected":"")%>> 1.1 </OPTION>
					</SELECT>
				</TD>
	
			</TR>
		</TBODY>
	</TABLE>
	<SCRIPT language="JavaScript">
		var monthArray=new Array();
		var inputConst=new Array();
		var positive="Positive";
		var integer="Integer";
		<%
		
		for (i=0;i<12;i++){
		%>monthArray[<%=i%>]='<%=CommonTools.getMonth(i+1)%>';
		<%}%>
		var objToHide=new Array(frm.txtYear,frm.txtVersion,frmSave.txtYear,frmSave.txtVersion);
		//relations between rows
		var rowArray=new Array();
		<%
		int parentID=-1;
		for (i=0;i<planningInfo.rows.size();i++){
			row=(PlanningInfo.Row)planningInfo.rows.elementAt(i);
			if (row.groupID==-2)
				continue;
			else if (row.groupID==-1){
				parentID=i;
				%>inputConst[<%=i%>]="<%=row.inputConstraint%>";<%
				continue;
			}
			%>rowArray[<%=i%>]=<%=parentID%>;
		<%}%>
		
		function check(){
			var field;
			var constraint;
			var isInteger;
			var isPositive;
			for (var w=0;w< <%=planningInfo.rows.size()%>;w++ ){
				field=eval('frmSave.yeartotal'+w);
				if (field){
					constraint= (rowArray[w])?inputConst[rowArray[w]]:inputConst[w];
					isInteger=(constraint.indexOf(integer)>-1);
					isPositive=(constraint.indexOf(positive)>-1);
					if (isPositive && !positiveFld(field, "<%=languageChoose.getMessage("fi.jsp.planning.Value")%>") )
						return;
					if (isInteger && !integerFld(field, "<%=languageChoose.getMessage("fi.jsp.planning.Value")%>") )
						return;
					for (var k=0;k<12;k++){
						field=eval('frmSave.val'+monthArray[k]+w);
						if (isPositive && !positiveFld(field, "<%=languageChoose.getMessage("fi.jsp.planning.Value")%>") )
							return;
						if (isInteger && !integerFld(field, "<%=languageChoose.getMessage("fi.jsp.planning.Value")%>") )
							return;
					}
				}
				
			}
			frmSave.submit();
		}
		function colSum(text){
			if (!frm.chkAuto.checked)return;
			var fname =new String(text.name);
			var rownum=fname.substr(6);
			var colname=fname.substr(0,6);
			var parent=rowArray[rownum];
			var sum=0;
			var rowVal;
			for (var i =0;i<rowArray.length;i++){
				if (rowArray[i]==parent){
					rowVal=Number(eval('frmSave.'+colname+i+'.value'));
					if ( !isNaN(rowVal))
						sum+=rowVal;
				}
			
			}
			eval('frmSave.'+colname+parent+'.value ='+sum);
		}
		function rowSum(text){
			if (!frm.chkAuto.checked)return;
			var fname =new String(text.name);
			var rownum=fname.substr(6);
			var sum=0;
			var rowVal;
			for (var k=0;k<12;k++){
				rowVal=Number(eval('frmSave.val'+monthArray[k]+rownum+'.value'));
				if ( !isNaN(rowVal))
					sum+=rowVal;
			}
			eval('frmSave.yeartotal'+rownum+'.value ='+sum);
		}
		function rowMax(text){
			if (!frm.chkAuto.checked)return;
			var fname =new String(text.name);
			var rownum=fname.substr(6);
			var max=0;
			var rowVal;
			for (var k=0;k<12;k++){
				rowVal=Number(eval('frmSave.val'+monthArray[k]+rownum+'.value'));
				if ( !isNaN(rowVal) &&rowVal>max)
					max=rowVal;
			}
			eval('frmSave.yeartotal'+rownum+'.value ='+max);
		}
	</SCRIPT>
<%}else{%>
	<SCRIPT language="JavaScript">
		var objToHide=new Array(frm.txtYear,frm.txtVersion);
	</SCRIPT>
<%}%>
</FORM>

<P></P>
</BODY>
</HTML>

