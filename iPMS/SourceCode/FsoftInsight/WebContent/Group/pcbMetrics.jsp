<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.* ,com.fms1.common.group.*,java.util.Vector " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<TITLE>PCB Metrics</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
Vector metricList=(Vector)session.getAttribute("pcbMetrics");
PCBGalInfo pcbGalInfo = (PCBGalInfo)session.getAttribute("pcbGalInfo");
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

String prePeriod=PCB.formatPrevPeriod(pcbGalInfo.period,pcbGalInfo.year);
String actualPeriod=pcbGalInfo.period+"-"+pcbGalInfo.year;
int nWorkUnitType = Integer.parseInt((String)session.getAttribute("workUnitType"));
%>
<BODY class="BD" onload="<%=CommonTools.getMnuFunc(session)%>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.PCBreportMetrics")%> </P>

<TABLE cellspacing="1" class="Table" width="95%">
	<TBODY>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Period")%> </TD>
			<TD class="CellBGRnews"> <%=languageChoose.paramText(new String[]{"fi.jsp.pcbMetrics.From~PARAM1_START_DATE~to~PARAM2_END_DATE~",CommonTools.dateFormat(pcbGalInfo.startDate),CommonTools.dateFormat(pcbGalInfo.endDate)})%> </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Reporttype")%> </TD>
			<TD class="CellBGRnews"><%=pcbGalInfo.reportTypeComment%></TD>
		</TR>
	</TBODY>
</TABLE>
</BODY>
<BR>
<TABLE cellspacing="1" class="Table" >
	<TBODY>
		<TR class="ColumnLabel">
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.ID")%> </TD>
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Name")%> </TD>
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Unit")%> </TD>
			<TD colspan="3"  class="SimpleColumn" bgcolor="#d7e3f2" align="center"> PCB <BR><%=languageChoose.getMessage(prePeriod)%></TD>
			<TD colspan="3" class="SimpleColumn" bgcolor="#AEC9E6" align="center"> PCB <BR><%=languageChoose.getMessage(actualPeriod)%></TD>
			<TD colspan="3" class="SimpleColumn" bgcolor="#89AFDA" align="center"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Fsoftnorm")%> <BR><%=actualPeriod%></TD>
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Achievement")%> </TD>
		</TR>
		<TR class="SimpleColumn">
			<TD bgcolor="#d7e3f2" > LCL </TD>
			<TD bgcolor="#d7e3f2"> Avg </TD>
			<TD bgcolor="#d7e3f2" > UCL </TD>
			<TD bgcolor="#AEC9E6"> LCL </TD>
			<TD bgcolor="#AEC9E6"> Avg </TD>
			<TD bgcolor="#AEC9E6"> UCL </TD>
			<TD bgcolor="#89AFDA" > LCL </TD>
			<TD bgcolor="#89AFDA"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Norm")%> </TD>
			<TD bgcolor="#89AFDA" > UCL </TD>
		</TR>
	<%
		PCBMetricInfo metricInfo;
		for (int i=0;i<metricList.size();i++){
			metricInfo=(PCBMetricInfo)metricList.elementAt(i);
			if (!metricInfo.isMetricGroup){ 
				if(metricInfo.metricID.startsWith(effProc)){effProcV.add(metricInfo);continue;}
                else if(metricInfo.metricID.startsWith(effStage)){effStageV.add(metricInfo);continue;}
                else if(metricInfo.metricID.startsWith(defOri)){defOriV.add(metricInfo);continue;}
                else if(metricInfo.metricID.startsWith(effStageold)){continue;}
                else if(metricInfo.metricID.startsWith(defQcAct)){defQcV.add(metricInfo); continue;}
                else if(metricInfo.metricID.startsWith(defQcActStg)){defQcStageV.add(metricInfo); continue;}
                else if(metricInfo.metricID.startsWith(defWorkPro)){defWorkProV.add(metricInfo); continue;}
                else if(metricInfo.metricID.startsWith(defEffWorkPro)){defEffWorkProV.add(metricInfo); continue;}
		%>	<TR class="CellBGRnews">
				<TD><%=metricInfo.metricID%></TD>
				<TD><A target="_self" href='Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=Group/pcbDetail.jsp&vtID=<%=i%>'><%=languageChoose.getMessage(metricInfo.name)%></A></TD>
				<TD><%=metricInfo.unit%></TD>
				<TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(metricInfo.prevLCL)%></TD>
				<TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(metricInfo.prevAvg)%></TD>
				<TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(metricInfo.prevUCL)%></TD>
				<TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(metricInfo.LCL)%></TD>
				<TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(metricInfo.actualAvg)%></TD>
				<TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(metricInfo.UCL)%></TD>
				<TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(metricInfo.normLCL)%></TD>
				<TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(metricInfo.normValue)%></TD>
				<TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(metricInfo.normUCL)%></TD>
				<TD  NOWRAP ><%=CommonTools.formatDouble(metricInfo.deviation)%></TD>
			</TR>
		<%}else {%>
        <TR class="ColumnLabel">
            <TD ><%=metricInfo.metricID%></TD>
            <TD colspan= 12><%=languageChoose.getMessage(metricInfo.name)%></TD>
        </TR>
		<%}
	}%>
	</TBODY>
</TABLE>
<BR>
<% 
Vector temp=null;
String name=null;
int size;
for (int i=0;i<2;i++){
	switch(i){
		case 0:temp=effProcV;name=languageChoose.getMessage("fi.jsp.pcbMetrics.Effortdistributionbyprocess");break;
		case 1:temp=defOriV;name=languageChoose.getMessage("fi.jsp.pcbMetrics.Defectdistributionbyprocess");break;
	}
size=temp.size();
if (size>0){
%>
<TABLE cellspacing="1" class="Table" width="<%=(size+1)*80%>">

	<TBODY>
		<TR class="ColumnLabel">
            <TD colspan= <%=size+1%>><%=languageChoose.getMessage(name)%></TD>
        </TR>
        <TR class="CellBGRnews">
        	<TD class="ColumnLabel" width="80"></TD>
	        <%for (int j=0;j<size;j++){
	        %><TD width="70" ><%=languageChoose.getMessage(((PCBMetricInfo)temp.elementAt(j)).name)%></TD>
	        <%}%>
        </TR>
         <TR class="CellBGR3">
         	<TD class="ColumnLabel"> PCB <BR><%=languageChoose.getMessage(prePeriod)%></TD>
	        <%for (int j=0;j<size;j++){
	        %><TD><%=CommonTools.formatDouble(((PCBMetricInfo)temp.elementAt(j)).prevAvg)%></TD>
	        <%}%>
        </TR>
        <TR class="CellBGRnews">
         	<TD class="ColumnLabel"> PCB <BR><%=languageChoose.getMessage(actualPeriod)%></TD>
	        <%for (int j=0;j<size;j++){
	        %><TD><%=CommonTools.formatDouble(((PCBMetricInfo)temp.elementAt(j)).actualAvg)%></TD>
	        <%}%>
        </TR>
        <TR class="CellBGR3">
         	<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Norm1")%> <BR><%=languageChoose.getMessage(actualPeriod)%></TD>
	        <%for (int j=0;j<size;j++){
	        %><TD><%=CommonTools.formatDouble(((PCBMetricInfo)temp.elementAt(j)).normValue)%></TD>
	        <%}%>
        </TR>
	</TBODY>
</TABLE>
<P></P>
<%	}
}
PCBMetricInfo val;
temp=effStageV;
size=temp.size();
if (size>0){
%>
<TABLE cellspacing="1" class="Table" width="<%=9*80%>">

    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan= 9> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Effortdistributionbystageandpr")%> </TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel" width="160" colspan=2></TD>
            <%for(int i=0;i<StageInfo.stageNames.length;i++){%>
            <TD class="ColumnLabel" width="80"><%=languageChoose.getMessage(StageInfo.stageNames[i])%></TD>
            <%}%>
            <TD class="ColumnLabel" width="80"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Total")%> </TD>
        </TR>
        <%
        String processName;
        double sum=0;
        for (int i = 0 ;i<size;i+=StageInfo.stageNames.length){
            metricInfo=(PCBMetricInfo)temp.elementAt(i);
            processName= metricInfo.name.substring(0,metricInfo.name.indexOf(" effort"));

        %>
         <TR class="CellBGR3">

            <TD class="ColumnLabel" rowspan=3><%=languageChoose.getMessage(processName)%></TD>
            <TD class="ColumnLabel"> PCB <BR><%=prePeriod%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(PCBMetricInfo)temp.elementAt(j);
                sum+=val.prevAvg;
                %><TD><%=CommonTools.formatDouble(val.prevAvg)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel"> PCB <BR><%=actualPeriod%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(PCBMetricInfo)temp.elementAt(j);
                sum+=val.actualAvg;
                %><TD><%=CommonTools.formatDouble(val.actualAvg)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.norm")%> <BR><%=actualPeriod%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(PCBMetricInfo)temp.elementAt(j);
                sum+=val.normValue;
                %><TD><%=CommonTools.formatDouble(val.normValue)%>
                </TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<P><BR></P>
<TABLE cellspacing="1" class="Table" width="720">
	<TBODY>
		<TR class="ColumnLabel">
			<TD rowspan="2">ID</TD>
			<TD rowspan="2">Name</TD>
			<TD rowspan="2">Unit</TD>
			<TD colspan="3"  class="SimpleColumn" bgcolor="#d7e3f2" align="center">PCB<BR><%=prePeriod%></TD>
			<TD colspan="3" class="SimpleColumn" bgcolor="#AEC9E6" align="center">PCB<BR><%=actualPeriod%></TD>
			<TD colspan="3" class="SimpleColumn" bgcolor="#89AFDA" align="center">Fsoft norm<BR><%=actualPeriod%></TD>
			<TD rowspan="2">Achievement (%)</TD>
		</TR>
		<TR class="SimpleColumn">
			<TD bgcolor="#d7e3f2" >LCL</TD>
			<TD bgcolor="#d7e3f2">Avg</TD>
			<TD bgcolor="#d7e3f2" >UCL</TD>
			<TD bgcolor="#AEC9E6">LCL</TD>
			<TD bgcolor="#AEC9E6">Avg</TD>
			<TD bgcolor="#AEC9E6">UCL</TD>
			<TD bgcolor="#89AFDA" >LCL</TD>
			<TD bgcolor="#89AFDA">Norm</TD>
			<TD bgcolor="#89AFDA" >UCL</TD>
		</TR>
	<%
	Vector vTemp=null;
	String GMetricID=null;
	String GMetricName=null;    
    for (int k=0;k<2;k++){
		if (k==0) {
			vTemp=defWorkProV;
			GMetricID="5.11";
			GMetricName="Defect rate by work products";
		}else{
			vTemp=defEffWorkProV;
			GMetricID="5.12";
			GMetricName="Defect removal efficiency by work products";
        }
		%>
		<TR class="ColumnLabel">
            <TD ><%=GMetricID%></TD>
            <TD colspan= 12><%=GMetricName%></TD>
        </TR>
		<%
		for (int i=0;i<vTemp.size();i++){
			metricInfo=(PCBMetricInfo)vTemp.elementAt(i);									
		%>	
		<TR class="CellBGRnews">
				<TD><%=metricInfo.metricID%></TD>
				<TD><%=metricInfo.name%></TD>
				<TD><%=metricInfo.unit%></TD>
				<TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(metricInfo.prevLCL)%></TD>
				<TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(metricInfo.prevAvg)%></TD>
				<TD bgcolor="#d7e3f2" NOWRAP ><%=CommonTools.formatDouble(metricInfo.prevUCL)%></TD>
				<TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(metricInfo.LCL)%></TD>
				<TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(metricInfo.actualAvg)%></TD>
				<TD bgcolor="#AEC9E6" NOWRAP ><%=CommonTools.formatDouble(metricInfo.UCL)%></TD>
				<TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(metricInfo.normLCL)%></TD>
				<TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(metricInfo.normValue)%></TD>
				<TD bgcolor="#89AFDA" NOWRAP ><%=CommonTools.formatDouble(metricInfo.normUCL)%></TD>
				<TD  NOWRAP ><%=CommonTools.formatDouble(metricInfo.deviation)%></TD>
			</TR>
    <%  }
	}%>	
	</TBODY>
</TABLE>
<P><BR></P>
<%}
temp=defQcV;
name = languageChoose.getMessage("fi.jsp.pcbMetrics.DefectRemovalEfficiencybyQCActivities");
size=temp.size();
if (size>0){
%>
<TABLE cellspacing="1" class="Table" width="<%=(size+1)*80%>">

    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan= <%=size+2%>><%=name%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel" width="80"></TD>
            <%
            double sum=0;
            for (int j=0;j<size;j++){
            %><TD width="70" class="ColumnLabel"><%=languageChoose.getMessage(((PCBMetricInfo)temp.elementAt(j)).name)%></TD>
            <%}%>
            <TD width="70" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Total1")%> </TD>
        </TR>
         <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.PCB6")%> <BR><%=prePeriod%></TD>
            <%sum = 0;
            for (int j=0;j<size;j++){
                val=(PCBMetricInfo)temp.elementAt(j);
                sum+=val.prevAvg;
            %><TD><%=CommonTools.formatDouble(val.prevAvg)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel"> PCB <BR><%=actualPeriod%></TD>
            <%sum = 0;
            for (int j=0;j<size;j++){
                val=(PCBMetricInfo)temp.elementAt(j);
                sum+=val.actualAvg;
            %><TD><%=CommonTools.formatDouble(val.actualAvg)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.norm1")%> <BR><%=actualPeriod%></TD>
            <%sum = 0;
            for (int j=0;j<size;j++){
                val=(PCBMetricInfo)temp.elementAt(j);
                if (! Double.isNaN(val.normValue)) {
                    sum+=val.normValue;
                }
                %><TD><%=CommonTools.formatDouble(val.normValue)%>
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
<TABLE cellspacing="1" class="Table" width="<%=9*80%>">

    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan= 9> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.DefectRemovalEfficiencybyQCAct")%> </TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel" width="160" colspan=2></TD>
            <%for(int i=0;i<StageInfo.stageNames.length;i++){%>
            <TD class="ColumnLabel" width="80"><%=languageChoose.getMessage(StageInfo.stageNames[i])%></TD>
            <%}%>
            <TD class="ColumnLabel" width="80"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.Total2")%> </TD>
        </TR>
        <%
        String qcActName;
        double sum=0;
        for (int i = 0 ;i<size;i+=StageInfo.stageNames.length){
            metricInfo=(PCBMetricInfo)temp.elementAt(i);
            qcActName = metricInfo.name.substring(0,metricInfo.name.indexOf(" for "));

        %>
         <TR class="CellBGR3">

            <TD class="ColumnLabel" rowspan=3><%=languageChoose.getMessage(qcActName)%></TD>
            <TD class="ColumnLabel"> PCB <BR><%=prePeriod%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(PCBMetricInfo)temp.elementAt(j);
                sum+=val.prevAvg;
                %><TD><%=CommonTools.formatDouble(val.prevAvg)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel"> PCB <BR><%=actualPeriod%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(PCBMetricInfo)temp.elementAt(j);
                sum+=val.actualAvg;
                %><TD><%=CommonTools.formatDouble(val.actualAvg)%></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbMetrics.norm2")%> <BR><%=actualPeriod%></TD>
            <%sum=0;
            for (int j=i;j<StageInfo.stageNames.length+i;j++){
                val=(PCBMetricInfo)temp.elementAt(j);
                if (! Double.isNaN(val.normValue)) {
                    sum+=val.normValue;
                }
                %><TD><%=CommonTools.formatDouble(val.normValue)%>
                </TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<P><BR></P>
<%}%>

</HTML>
