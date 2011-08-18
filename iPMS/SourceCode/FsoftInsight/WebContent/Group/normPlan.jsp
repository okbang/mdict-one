 <%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.* ,com.fms1.common.group.* ,java.util.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="../javaFns.jsp"%>
</SCRIPT>
<TITLE>normPlan.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
int right = Security.securiPage("Organization norms",request,response);
NormPlanInfo normPlanInfo=(NormPlanInfo)session.getAttribute("normPlan");
Vector allOperationGroup = (Vector)session.getAttribute("allOperationGroup");
Vector prevAllOperationGroup = (Vector)session.getAttribute("prevAllOperationGroup");

String error=request.getParameter("error");
String effProc="4.6.";
String effStageold="4.7.";
String effStage="4.9.";
String defOri="5.8.";
String defQcAct="5.9.";
String defQcActStg="5.10.";
String defWorkPro="5.11.";
String defEffWorkPro="5.12.";
Vector effProcV =new Vector();
Vector effStageV =new Vector();
Vector defOriV =new Vector();
Vector defQcV =new Vector();
Vector defQcStageV =new Vector();
Vector defWorkProV =new Vector();
Vector defEffWorkProV =new Vector();
%>
<%!
//if the norm is null then we use prev value
String formatNorm(double curVal,double prevVal){
if (Double.isNaN(curVal)&& !Double.isNaN(prevVal))
    return CommonTools.updateDouble(prevVal)+"\" style='font-weight: bold;color: red'";
return CommonTools.updateDouble(curVal);
}

%>
<BODY class="BD" onload="loadOrgMenu();">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.normPlan.Softwarenorms")%></P>
<P class="ERROR"><%=languageChoose.getMessage(error)%></P>
<FORM action="Fms1Servlet?reqType=<%=Constants.LOADNORMS+"&type="+MetricDescInfo.GR_SOFTWARE%>" name="frm" method="post">
<TABLE >
    <TBODY>
        <TR class="NormalText">
            <TD><%=languageChoose.getMessage("fi.jsp.normPlan.Year")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.normPlan.Term")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.normPlan.Lifecycle")%></TD>
        </TR>
        <TR>
            <TD>
                <SELECT name="txtYear" class="COMBO" >
                    <%
                    int startYear = 2000;
                    java.text.SimpleDateFormat  yearFrmt= new java.text.SimpleDateFormat("yyyy");
                    int endYear = Integer.parseInt(yearFrmt.format(new java.util.Date()));
                    String selected;
                    for (int year =endYear;year>=startYear;year--){
                        selected=(normPlanInfo.year==year)?" selected":"";
                        %><OPTION value=<%=year+selected%>><%=year%></OPTION>
                    <%}%>
                </SELECT>
            </TD>
            <TD>
                <SELECT name="txtPeriod" class="COMBO">
                    <OPTION value ="S1"<%=((normPlanInfo.term.equals("S1"))?" selected":"")%>>S1</OPTION>
                    <OPTION value ="S2"<%=((normPlanInfo.term.equals("S2"))?" selected":"")%>>S2</OPTION>
                </SELECT>
            </TD>
            <TD>
                <SELECT name="txtLifecycle" class="COMBO">
                    <OPTION value=<%=ProjectInfo.LIFECYCLE_DEVELOPMENT+ ((normPlanInfo.lifecycleID==ProjectInfo.LIFECYCLE_DEVELOPMENT)?" selected":"")%>>Development</OPTION>
                    <OPTION value=<%=ProjectInfo.LIFECYCLE_MAINTENANCE+ ((normPlanInfo.lifecycleID==ProjectInfo.LIFECYCLE_MAINTENANCE)?" selected":"")%>>Maintenance</OPTION>
                    <OPTION value=<%=ProjectInfo.LIFECYCLE_OTHER+ ((normPlanInfo.lifecycleID==ProjectInfo.LIFECYCLE_OTHER)?" selected":"")%>>Other</OPTION>
                </SELECT>
            </TD>
            <TD>
                <INPUT type="submit" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.normPlan.Go")%> ">
                <INPUT type="submit" name="refreshCache" class="BUTTONWIDTH" value="<%=languageChoose.getMessage("fi.jsp.normPlan.RefreshPCB")%>"><!--do not change the name of the button, it  is used in the servlet-->
            </TD>
            </TR>
            <%if (right==3){%>
            <TR>
            <TD class="NormalText" colspan=4>
                <%=languageChoose.getMessage("fi.jsp.normPlan.NoteSuggestedmetricsinredshoul")%>
            </TD>
            </TR>
            <%}%>
    </TBODY>
</TABLE>
</FORM>
<BR>

<FORM action="Fms1Servlet?reqType=<%=Constants.SAVENORMS+"&type="+MetricDescInfo.GR_SOFTWARE%>" name="frmN" method="post">
<TABLE cellspacing="1" class="Table" >
<CAPTION class="TableCaption"><%=languageChoose.paramText(new String[]{"fi.jsp.normPlan.LIFE_CYCLE~PARAM1_LIFECYCCLE_ID~",languageChoose.getMessage(ProjectInfo.parseLifecycle(normPlanInfo.lifecycleID))})%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD rowspan="2"> ID </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.normPlan.Name")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.normPlan.Unit")%> </TD>
            <% String prevPeriod =PCB.formatPrevPeriod(normPlanInfo.term,normPlanInfo.year);%>
            <TD colspan="3" class="SimpleColumn" bgcolor="#d7e3f2" align="center"> PCB  <BR><%=prevPeriod%></TD>
            <TD colspan="3" class="SimpleColumn" bgcolor="#AEC9E6" align="center"> <%=languageChoose.getMessage("fi.jsp.normPlan.norm")%> <BR><%=prevPeriod%></TD>
            <TD colspan="3" class="SimpleColumn" bgcolor="#89AFDA" align="center"> <%=languageChoose.getMessage("fi.jsp.normPlan.norm1")%> <BR><%=normPlanInfo.term+"-"+normPlanInfo.year%></TD>
        </TR>
        <TR class="SimpleColumn">
            <TD bgcolor="#d7e3f2"> LCL </TD>
            <TD bgcolor="#d7e3f2"> Avg </TD>
            <TD bgcolor="#d7e3f2"> UCL </TD>
            <TD bgcolor="#AEC9E6"> LCL </TD>
            <TD bgcolor="#AEC9E6"> <%=languageChoose.getMessage("fi.jsp.normPlan.Norm")%> </TD>
            <TD bgcolor="#AEC9E6"> UCL </TD>
            <TD bgcolor="#89AFDA"> LCL </TD>
            <TD bgcolor="#89AFDA"> <%=languageChoose.getMessage("fi.jsp.normPlan.Norm1")%> </TD>
            <TD bgcolor="#89AFDA"> UCL </TD>
        </TR>
    <%
        NormPlanInfo.Row row;
        for (int i=0;i<normPlanInfo.rows.size();i++){
            row=(NormPlanInfo.Row)normPlanInfo.rows.elementAt(i);
            //skip special display norms
            if (!row.isMetricGroup){
                if(row.strMetricID.startsWith(effProc)){effProcV.add(row);continue;}
                else if(row.strMetricID.startsWith(effStage)){effStageV.add(row);continue;}
                else if(row.strMetricID.startsWith(defOri)){defOriV.add(row);continue;}
                else if(row.strMetricID.startsWith(defWorkPro)){defWorkProV.add(row);continue;}
                else if(row.strMetricID.startsWith(defEffWorkPro)){defEffWorkProV.add(row);continue;}
                else if(row.strMetricID.startsWith(effStageold)){continue;}
                else if(row.strMetricID.startsWith(defQcAct)){defQcV.add(row);continue;}
                else if(row.strMetricID.startsWith(defQcActStg)){defQcStageV.add(row);continue;} 
            
            if(row.metricID == MetricDescInfo.CUSTOMER_SATISFACTION){	%>
            	<TR class="CellBGRnews">
	                <TD><%=row.strMetricID%></TD>
	                <TD><%=languageChoose.getMessage(row.metricName)%></TD>
	                <TD><%=row.metricUnit%></TD>
	                <TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(row.prevCalcLCL)%></TD>
	                <TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(row.prevAverage)%></TD>
	                <TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(row.prevCalcUCL)%></TD>
	                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(row.prevLCL)%></TD>
	                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(row.prevNorm)%></TD>
	                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(row.prevUCL)%></TD>
	                <%if (right==3){	%>
	                <TD bgcolor="#89AFDA">
	                <INPUT type="hidden" name="uval" value="<%=CommonTools.formatDouble(MetricDescInfo.getBoundary(row.metricID,false))%>" disabled>
	                <INPUT type="hidden" name="lval" value="<%=CommonTools.formatDouble(MetricDescInfo.getBoundary(row.metricID,true))%>" disabled>
	                <INPUT name ="lcl"  maxlength="11" size = "5" value="<%=formatNorm(row.LCL,row.prevLCL)%>">
	                </TD>
	                <TD bgcolor="#89AFDA"><INPUT name ="norm" maxlength="11" size = "5" value="<%=formatNorm(row.norm,row.prevNorm)%>"></TD>
	                <TD bgcolor="#89AFDA"><INPUT name ="ucl" maxlength="11" size = 5" value="<%=formatNorm(row.UCL,row.prevUCL)%>"></TD>
	                <%} else {%>
	                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(row.LCL)%></TD>
	                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(row.norm)%></TD>
	                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(row.UCL)%></TD>
	                <%} %>
            	</TR>
            <%
			CSSNormInfo cssNormInfo = new CSSNormInfo();
			CSSNormInfo prevCSSNormInfo = new CSSNormInfo();
            for(int l = 0; l < allOperationGroup.size();l++) {
            	cssNormInfo = (CSSNormInfo)allOperationGroup.elementAt(l);
            	prevCSSNormInfo = (CSSNormInfo)prevAllOperationGroup.elementAt(l);
            %>
                <TR class="CellBGRnews">
	                <TD></TD>
	                <TD colspan="5"><%=cssNormInfo.groupName%></TD>
	                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(prevCSSNormInfo.lcl)%></TD>
	                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(prevCSSNormInfo.average)%></TD>
	                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(prevCSSNormInfo.ucl)%></TD>
	                <%if (right==3){	%>
	                <TD bgcolor="#89AFDA">
	                <INPUT type="hidden" name="guval" value="<%=CommonTools.formatDouble(MetricDescInfo.getBoundary(row.metricID,false))%>" disabled>
	                <INPUT type="hidden" name="glval" value="<%=CommonTools.formatDouble(MetricDescInfo.getBoundary(row.metricID,true))%>" disabled>
	                <INPUT name ="glcl"  maxlength="11" size = "5" value="<%=formatNorm(cssNormInfo.lcl,prevCSSNormInfo.lcl)%>">
	                </TD>
	                <TD bgcolor="#89AFDA"><INPUT name ="gaverage" maxlength="11" size = "5" value="<%=formatNorm(cssNormInfo.average,prevCSSNormInfo.average)%>"></TD>
	                <TD bgcolor="#89AFDA"><INPUT name ="gucl" maxlength="11" size = 5" value="<%=formatNorm(cssNormInfo.ucl,prevCSSNormInfo.ucl)%>"></TD>
	                <%} else{%>
	                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(cssNormInfo.lcl)%></TD>
	                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(cssNormInfo.average)%></TD>
	                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(cssNormInfo.ucl)%></TD>
	                <%}%>
                </TR>
            <%}
            }else{
            %><TR class="CellBGRnews">
                <TD><%=row.strMetricID%></TD>
                <TD><%=languageChoose.getMessage(row.metricName)%></TD>
                <TD><%=row.metricUnit%></TD>
                <TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(row.prevCalcLCL)%></TD>
                <TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(row.prevAverage)%></TD>
                <TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(row.prevCalcUCL)%></TD>
                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(row.prevLCL)%></TD>
                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(row.prevNorm)%></TD>
                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(row.prevUCL)%></TD>
                <%if (right==3){%>
                <TD bgcolor="#89AFDA">
                <INPUT type="hidden" name="uval" value="<%=CommonTools.formatDouble(MetricDescInfo.getBoundary(row.metricID,false))%>" disabled>
                <INPUT type="hidden" name="lval" value="<%=CommonTools.formatDouble(MetricDescInfo.getBoundary(row.metricID,true))%>" disabled>
                <INPUT name ="lcl"  maxlength="11" size = "5" value="<%=formatNorm(row.LCL,row.prevLCL)%>">
                </TD>
                <TD bgcolor="#89AFDA"><INPUT name ="norm" maxlength="11" size = "5" value="<%=formatNorm(row.norm,row.prevNorm)%>"></TD>
                <TD bgcolor="#89AFDA"><INPUT name ="ucl" maxlength="11" size = 5" value="<%=formatNorm(row.UCL,row.prevUCL)%>"></TD>
                <%} else{%>
                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(row.LCL)%></TD>
                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(row.norm)%></TD>
                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(row.UCL)%></TD>
                <%}
                }%>
            </TR>
            <%}else {%>
            <TR class="ColumnLabel">
                <TD ><%=row.strMetricID%></TD>
                <TD colspan= 12><%=languageChoose.getMessage(row.metricName)%></TD>
            </TR>
            <%}
        	}
        %>
    </TBODY>
</TABLE>
<BR>
<%
Vector temp=null;
String name=null;
NormPlanInfo.Row val;
int size;
for (int i=0;i<2;i++){
    switch(i){
        case 0:temp=effProcV;name=languageChoose.getMessage("fi.jsp.normPlan.EffortDistributionByProcess");break;
        case 1:temp=defOriV;name=languageChoose.getMessage("fi.jsp.normPlan.DefectDistributionByProcess");break;
    }
size=temp.size();
if (size>0){
%>
<TABLE cellspacing="1" class="Table" width="<%=(size+1)*80%>">
    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan= <%=size+1%>><%=name%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel" width="80"></TD>
            <%for (int j=0;j<size;j++){
            %><TD width="70" class="ColumnLabel"><%=languageChoose.getMessage(((NormPlanInfo.Row)temp.elementAt(j)).metricName)%></TD>
            <%}%>
        </TR>
         <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.normPlan.PCB")%> <%=prevPeriod%></TD>
            <%for (int j=0;j<size;j++){
            %><TD><%=CommonTools.formatDouble(((NormPlanInfo.Row)temp.elementAt(j)).prevAverage)%></TD>
            <%}%>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.normPlan.Norm2")%> <%=prevPeriod%></TD>
            <%for (int j=0;j<size;j++){
            %><TD><%=CommonTools.formatDouble(((NormPlanInfo.Row)temp.elementAt(j)).prevNorm)%></TD>
            <%}%>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.normPlan.Norm3")%> <%=normPlanInfo.term+"-"+normPlanInfo.year%></TD>
            <%
            for (int j=0;j<size;j++){
                val=(NormPlanInfo.Row)temp.elementAt(j);
                if (right==3){%><TD><INPUT name ="m<%=val.strMetricID%>" maxlength="11" size = 7" value="<%=formatNorm(val.norm,val.prevNorm)%>">
                    <%}else{%><TD><%=CommonTools.formatDouble(val.norm)%><%}%>
                </TD>
            <%}%>
        </TR>
    </TBODY>
</TABLE>
<P><BR></P>
<% }
}
temp=effStageV;
CommonTools.sortVector(effStageV,"metricID");
size=temp.size();
if (size>0){
%>
<TABLE cellspacing="1" class="Table">
    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan= 9><%=languageChoose.getMessage("fi.jsp.normPlan.Effortdistributionbystageandpr")%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel" width="160" colspan=2></TD>
            <%for(int i=0;i<StageInfo.stageNames.length;i++){%>
            <TD class="ColumnLabel" width="80"><%=languageChoose.getMessage(StageInfo.stageNames[i])%></TD>
            <%}%>
            <TD class="ColumnLabel" width="70"><%=languageChoose.getMessage("fi.jsp.normPlan.Total")%></TD>
        </TR>
        <%
        String processName;
        double sum=0;
        for (int i = 0 ;i<size;i+=StageInfo.stageNames.length){
            row=(NormPlanInfo.Row)temp.elementAt(i);
            processName= row.metricName.substring(0,row.metricName.indexOf(" Effort"));
        %>
         <TR class="CellBGR3">
            <TD class="ColumnLabel" width="90" rowspan=3><%=languageChoose.getMessage(processName)%></TD>
            <TD class="ColumnLabel" width="110"><%=languageChoose.getMessage("fi.jsp.normPlan.PCB")%> <%=prevPeriod%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(NormPlanInfo.Row)temp.elementAt(j);
                sum+=val.prevAverage;
                %><TD><%=CommonTools.formatDouble(val.prevAverage)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel" width="110"><%=languageChoose.getMessage("fi.jsp.normPlan.Norm4")%> <%=prevPeriod%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(NormPlanInfo.Row)temp.elementAt(j);
                sum+=val.prevNorm;
                %><TD><%=CommonTools.formatDouble(val.prevNorm)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel" width="110"><%=languageChoose.getMessage("fi.jsp.normPlan.Norm5")%> <%=normPlanInfo.term+"-"+normPlanInfo.year%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(NormPlanInfo.Row)temp.elementAt(j);
                sum+=val.norm;
                if (right==3){%><TD><INPUT name ="m<%=val.strMetricID%>" maxlength="11" size = 7" value="<%=formatNorm(val.norm,val.prevNorm)%>">
                    <%}else{%><TD><%=CommonTools.formatDouble(val.norm)%><%}%>
                </TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<P><BR></P>

<TABLE cellspacing="1" class="Table" >
    <TBODY>
        <TR class="ColumnLabel">
            <TD rowspan="2"> ID </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.normPlan.Name1")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.normPlan.Unit1")%> </TD>            
            <TD colspan="3" class="SimpleColumn" bgcolor="#d7e3f2" align="center"> PCB <BR><%=prevPeriod%></TD>
            <TD colspan="3" class="SimpleColumn" bgcolor="#AEC9E6" align="center"> <%=languageChoose.getMessage("fi.jsp.normPlan.norm2")%> <BR><%=prevPeriod%></TD>
            <TD colspan="3" class="SimpleColumn" bgcolor="#89AFDA" align="center"> <%=languageChoose.getMessage("fi.jsp.normPlan.norm3")%> <BR><%=normPlanInfo.term+"-"+normPlanInfo.year%></TD>
        </TR>
        <TR class="SimpleColumn">
            <TD bgcolor="#d7e3f2"> LCL </TD>
            <TD bgcolor="#d7e3f2"> Avg </TD>
            <TD bgcolor="#d7e3f2"> UCL</TD>
            <TD bgcolor="#AEC9E6"> LCL </TD>
            <TD bgcolor="#AEC9E6"> <%=languageChoose.getMessage("fi.jsp.normPlan.Norm6")%> </TD>
            <TD bgcolor="#AEC9E6"> UCL </TD>
            <TD bgcolor="#89AFDA"> LCL </TD>
            <TD bgcolor="#89AFDA"> <%=languageChoose.getMessage("fi.jsp.normPlan.Norm7")%> </TD>
            <TD bgcolor="#89AFDA"> UCL </TD>
        </TR>
        <%
        Vector vTemp=null;
		String GMetricID=null;
		String GMetricName=null;
        NormPlanInfo.Row rowWP;
        for (int k=0;k<2;k++){
        	if (k==0) {
        		vTemp=defWorkProV;
        		GMetricID="5.11";
        		GMetricName=languageChoose.getMessage("fi.jsp.normPlan.DefectRateByWorkProducts");
        	}else{
        		vTemp=defEffWorkProV;
        		GMetricID="5.12";
        		GMetricName=languageChoose.getMessage("fi.jsp.normPlan.DefectRemovalEfficiencyByWorkProducts");
                }
		%>
        <TR class="ColumnLabel">
	            <TD><%=GMetricID%></TD>
	            <TD colspan= 12><%=GMetricName%></TD>
        </TR>
        <%
	        for (int i=0;i<vTemp.size();i++){
	            rowWP=(NormPlanInfo.Row)vTemp.elementAt(i);        
	        %><TR class="CellBGRnews">
                <TD><%=rowWP.strMetricID%></TD>
                <TD><%=rowWP.metricName%></TD>
                <TD><%=rowWP.metricUnit%></TD>
                <TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(rowWP.prevCalcLCL)%></TD>
                <TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(rowWP.prevAverage)%></TD>
                <TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(rowWP.prevCalcUCL)%></TD>
                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(rowWP.prevLCL)%></TD>
                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(rowWP.prevNorm)%></TD>
                <TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(rowWP.prevUCL)%></TD>
                <%if (right==3){%>
                <TD bgcolor="#89AFDA">
                <INPUT type="hidden" name="wp_uval" value="<%=CommonTools.formatDouble(MetricDescInfo.getBoundary(rowWP.metricID,false))%>" disabled>
                <INPUT type="hidden" name="wp_lval" value="<%=CommonTools.formatDouble(MetricDescInfo.getBoundary(rowWP.metricID,true))%>" disabled>
                <INPUT name ="wp_lcl"  maxlength="11" size = "5" value="<%=formatNorm(rowWP.LCL,rowWP.prevLCL)%>">
                </TD>
                <TD bgcolor="#89AFDA"><INPUT name ="wp_norm" maxlength="11" size = "5" value="<%=formatNorm(rowWP.norm,rowWP.prevNorm)%>"></TD>
                <TD bgcolor="#89AFDA"><INPUT name ="wp_ucl" maxlength="11" size = 5" value="<%=formatNorm(rowWP.UCL,rowWP.prevUCL)%>"></TD>
                <%} else{%>
                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(rowWP.LCL)%></TD>
                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(rowWP.norm)%></TD>
                <TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(rowWP.UCL)%></TD>
            <%}%>
        </TR>
	        <%}
	    }%>
    </TBODY>
</TABLE>
<%}%>

<P><BR></P>

<%
temp=defQcV;
name=languageChoose.getMessage("fi.jsp.normPlan.DefectRemovalEfficiencyByQCActivities");
size=temp.size();
if (size>0){
%>
<TABLE cellspacing="1" class="Table" width="<%=(size+3)*80%>">
    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan= <%=size+2%>><%=name%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel" width="120"></TD>
            <%
            double sum=0;
            for (int j=0;j<size;j++){
            %><TD width="100" class="ColumnLabel"><%=languageChoose.getMessage(((NormPlanInfo.Row)temp.elementAt(j)).metricName)%></TD>
            <%}%>
            <TD width="60" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.normPlan.Total1")%></TD>
        </TR>
         <TR class="CellBGR3">
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.normPlan.PCB")%> <%=prevPeriod%></TD>
            <%sum = 0;
            for (int j=0;j<size;j++){
                val=(NormPlanInfo.Row)temp.elementAt(j);
                sum+=val.prevAverage;
            %><TD><%=CommonTools.formatDouble(val.prevAverage)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.normPlan.Norm8")%> <%=prevPeriod%></TD>
            <%sum = 0;
            for (int j=0;j<size;j++){
                val=(NormPlanInfo.Row)temp.elementAt(j);
                sum+=val.prevNorm;
            %><TD><%=CommonTools.formatDouble(val.prevNorm)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.normPlan.Norm9")%> <%=normPlanInfo.term+"-"+normPlanInfo.year%></TD>
            <%sum = 0;
            for (int j=0;j<size;j++){
                val=(NormPlanInfo.Row)temp.elementAt(j);
                if (! Double.isNaN(val.norm)) {
                    sum+=val.norm;
                }
                if (right==3){%><TD><INPUT name ="m<%=val.strMetricID%>" maxlength="11" size = 7" value="<%=formatNorm(val.norm,val.prevNorm)%>">
                    <%}else{%><TD><%=CommonTools.formatDouble(val.norm)%><%}%>
                </TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
    </TBODY>
</TABLE>
<P><BR></P>
<% }
temp=defQcStageV;
size=temp.size();
if (size>0){
%>
<TABLE cellspacing="1" class="Table">
    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan= 9><%=languageChoose.getMessage("fi.jsp.normPlan.DefectRemovalEfficiencybyQCAct")%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel" width="160" colspan=2></TD>
            <%for(int i=0;i<StageInfo.stageNames.length;i++){%>
            <TD class="ColumnLabel" width="80"><%=languageChoose.getMessage(StageInfo.stageNames[i])%></TD>
            <%}%>
            <TD class="ColumnLabel" width="60"><%=languageChoose.getMessage("fi.jsp.normPlan.Total2")%></TD>
        </TR>
        <%
        String qcActName;
        double sum=0;
        for (int i = 0 ;i<size;i+=StageInfo.stageNames.length){
            row=(NormPlanInfo.Row)temp.elementAt(i);
            qcActName = row.metricName.substring(0,row.metricName.indexOf(" for "));
        %>
         <TR class="CellBGR3">

            <TD class="ColumnLabel" width="140" rowspan=3><%=languageChoose.getMessage(qcActName)%></TD>
            <TD class="ColumnLabel" width="120"><%=languageChoose.getMessage("fi.jsp.normPlan.PCB")%> <%=prevPeriod%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(NormPlanInfo.Row)temp.elementAt(j);
                sum+=val.prevAverage;
                %><TD><%=CommonTools.formatDouble(val.prevAverage)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.normPlan.Norm10")%> <%=prevPeriod%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(NormPlanInfo.Row)temp.elementAt(j);
                sum+=val.prevNorm;
                %><TD><%=CommonTools.formatDouble(val.prevNorm)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.normPlan.Norm11")%> <%=normPlanInfo.term+"-"+normPlanInfo.year%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(NormPlanInfo.Row)temp.elementAt(j);
                if (! Double.isNaN(val.norm)) {
                    sum+=val.norm;
                }
                if (right==3){%><TD><INPUT name ="m<%=val.strMetricID%>" maxlength="11" size = 7" value="<%=formatNorm(val.norm,val.prevNorm)%>">
                    <%}else{%><TD><%=CommonTools.formatDouble(val.norm)%><%}%>
                </TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<P><BR></P>
<%}%>

<%if (right==3){%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.normPlan.Save")%>" onclick="check()">
<SCRIPT language="JavaScript">

var effStageArr={};

function checkBounds(){
}
function check(){
    if(frmN.lcl != null)
    {
    
	    for (var w = 0; w < frmN.lcl.length; w++ ){
	        if (!numberFld(frmN.lcl[w], "LCL") || !numberFld(frmN.norm[w], "<%=languageChoose.getMessage("fi.jsp.normPlan.Norm")%>") ||!numberFld(frmN.ucl[w], "UCL"))
	            return;
	        if (!positiveFld(frmN.lcl[w], "LCL") || !positiveFld(frmN.norm[w], "<%=languageChoose.getMessage("fi.jsp.normPlan.Norm")%>") || !positiveFld(frmN.ucl[w], "UCL")){
	        	return;
	        }
	        if (Number(frmN.lcl[w].value) >Number(frmN.norm[w].value) ||Number(frmN.norm[w].value)>Number(frmN.ucl[w].value)){
	            alert("<%=languageChoose.getMessage("fi.jsp.normPlan.NormValueMustBeGreaterThanLCLAndSmallerThanUCL")%>");
	            frmN.norm[w].focus();
	            return;
	            }
	        if (!isNaN(frmN.uval[w].value) && (Number(frmN.ucl[w].value)>Number(frmN.uval[w].value))){
	            alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlan.UCL__of__this__metric__must__be__less__or__equal__to__~PARAM1_VALUE~")%>',frmN.uval[w].value)));
	            frmN.ucl[w].focus();
	            return;
	        }
	        if (!isNaN(frmN.lval[w].value) && (Number(frmN.lcl[w].value)<Number(frmN.lval[w].value))){
	            alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlan.LCL__of__this__metric__must__be__above__or__equal__to__~PARAM1_VALUE~")%>',frmN.lval[w].value)));
	            frmN.lcl[w].focus();
	            return;
	        }
	    }
    }
    if(frmN.glcl !=null)
    {
	    
	    for (var w = 0; w < frmN.glcl.length; w++ ){
	        if (!numberFld(frmN.glcl[w], "LCL") || !numberFld(frmN.gaverage[w], "<%=languageChoose.getMessage("fi.jsp.normPlan.Norm")%>") ||!numberFld(frmN.gucl[w], "UCL"))
	            return;
	        if (!positiveFld(frmN.glcl[w], "LCL") || !positiveFld(frmN.gaverage[w], "<%=languageChoose.getMessage("fi.jsp.normPlan.Norm")%>") || !positiveFld(frmN.gucl[w], "UCL")){
	        	return;
	        }
	        if (Number(frmN.glcl[w].value) >Number(frmN.gaverage[w].value) ||Number(frmN.gaverage[w].value)>Number(frmN.gucl[w].value)){
	            alert("<%=languageChoose.getMessage("fi.jsp.normPlan.NormValueMustBeGreaterThanLCLAndSmallerThanUCL")%>");
	            frmN.gaverage[w].focus();
	            return;
	            }
	        if (!isNaN(frmN.guval[w].value) && (Number(frmN.gucl[w].value)>Number(frmN.guval[w].value))){
	            alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlan.UCL__of__this__metric__must__be__less__or__equal__to__~PARAM1_VALUE~")%>',frmN.guval[w].value)));
	            frmN.gucl[w].focus();
	            return;
	        }
	        if (!isNaN(frmN.glval[w].value) && (Number(frmN.glcl[w].value)<Number(frmN.glval[w].value))){
	            alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlan.LCL__of__this__metric__must__be__above__or__equal__to__~PARAM1_VALUE~")%>',frmN.glval[w].value)));
	            frmN.glcl[w].focus();
	            return;
	        }
	    }
    
    }
    if(frmN.wp_lcl != null)
    {
    
	    for (var w=0;w<frmN.wp_lcl.length;w++ ){
	        if (!numberFld(frmN.wp_lcl[w], "LCL") || !numberFld(frmN.wp_norm[w], "<%=languageChoose.getMessage("fi.jsp.normPlan.Norm")%>") ||!numberFld(frmN.wp_ucl[w], "UCL"))
	            return;
	        if (Number(frmN.wp_lcl[w].value) >Number(frmN.wp_norm[w].value) ||Number(frmN.wp_norm[w].value)>Number(frmN.wp_ucl[w].value)){
	            alert("<%=languageChoose.getMessage("fi.jsp.normPlan.NormValueMustBeGreaterThanLCLAndSmallerThanUCL")%>");
	            frmN.wp_norm[w].focus();
	            return;
	            }
	        if (!isNaN(frmN.wp_uval[w].value) && (Number(frmN.wp_ucl[w].value)>Number(frmN.wp_uval[w].value))){
	            alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlan.UCL__of__this__metric__must__be__less__or__equal__to__~PARAM1_VALUE~")%>',frmN.wp_uval[w].value)));
	            frmN.wp_ucl[w].focus();
	            return;
	        }
	        if (!isNaN(frmN.wp_lval[w].value) && (Number(frmN.wp_lcl[w].value)<Number(frmN.wp_lval[w].value))){
	            alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlan.LCL__of__this__metric__must__be__above__or__equal__to__~PARAM1_VALUE~")%>',frmN.wp_lval[w].value)));
	            frmN.wp_lcl[w].focus();
	            return;
	        }
	    }
    }
<%for (int i=0;i<3;i++){
    switch(i){
        case 0:temp=effProcV;break;
        case 1:temp=defOriV;break;
        case 2:temp=defQcV;break;
    }
    size=temp.size();
    if (size>0){%>

    var total=0;
    var tempNum;
    var ids= new Array(<%=size%>);
    <%for (int j=0;j<size;j++){%>
        ids[<%=j%>]="m<%=((NormPlanInfo.Row)temp.elementAt(j)).strMetricID%>";
    <%}%>
    for (i=0;i<ids.length;i++){
        tempNum=Number(frmN[ids[i]].value);
        if (!isNaN(tempNum) && tempNum>=0)
            total+=tempNum;
        else{
            alert("<%=languageChoose.getMessage("fi.jsp.normPlan.ThisFieldMustBeAPositiveNumber")%>");
            frmN[ids[i]].focus();
            return;
        }

        }
        if (total!=100){
            alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlan.The__total__of__distribution__values__must__be__equal__to__100__currently__~PARAM1_NUMBER~")%>',total)));
            frmN[ids[0]].focus();
            return;
        }
<%}
}%>
    if (checkSummary("m<%=effStage%>") && checkSummary("m<%=defQcActStg%>"))
        frmN.submit();
}
// Prefix: "m4.9." is Effort by Stage, "m5.9." is QC Activity by Stage
function checkSummary(prefix) {
    var test="";
    var temp;
    var effProcIndex=0;
    var bkup="";
    //var prefix="m<!--%=effStage%-->";
    var size=prefix.length+2;
    var sum=0;
    for (i=0;i<document.all.length;i++){
        temp=document.all[i].name;
        if (temp && temp.indexOf(prefix)>=0){
            if (mandatoryFld(document.all[i],"<%= languageChoose.getMessage("fi.jsp.normPlan.Distribution")%>") && percentageFld(document.all[i],"<%= languageChoose.getMessage("fi.jsp.normPlan.Distribution") %>")){
                if(temp.indexOf(bkup.substr(0,size))<0){
                    if (sum!=0 && sum!=100){
                        alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlan.The__total__of__distribution__values__must__be__equal__to__100__currently__~PARAM1_NUMBER~")%>',sum)));
                        document.all[bkup].focus();
                        return false;
                    }
                    sum=Number(document.all[i].value);
                }
                else{
                    sum +=Number(document.all[i].value);
                }
                bkup= temp;
            }
            else
                return false;
        }
    }
    //one more time for the last row
    if (sum!=0 && sum!=100){
        alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.normPlan.The__total__of__distribution__values__must__be__equal__to__100__currently__~PARAM1_NUMBER~")%>',sum)));
        document.all[bkup].focus();
        return false;
    }
    return true;
}
</SCRIPT>
<%}%>
<SCRIPT language="JavaScript">
var objToHide=new Array(frm.txtYear,frm.txtPeriod,frm.txtLifecycle);
</SCRIPT>
</FORM>
<P></P>
</BODY>
</HTML>