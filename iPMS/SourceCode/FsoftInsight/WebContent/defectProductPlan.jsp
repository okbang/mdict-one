<%@page import="com.fms1.infoclass.*,com.fms1.tools.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<%!
DefectProductPlanInfo defectInfo;
int [] cboMapping;//array for combo synchronization
int countNotReleased=0;
/*
*We create anchor so that when the user selects a value in a combo,
* the corresponding values are copied from the list to the input 
*fields for update
*/
String anchor(String type,String ratePlan){	
	return "<A name='MoDuLe"+defectInfo.moduleName+type+"' type='"+defectInfo.released+"' id='"+ratePlan+"' "+(("".equals(type))? "href=\"javascript:clickMod('"+defectInfo.moduleName+"')\"":"")+">";//add letter M for module anchors	
}
%>
<%
	Vector defectInfoVector = (Vector)session.getAttribute("defectProductPlan");
	Vector wpVector = (Vector)session.getAttribute("wpList");
	Vector moduleVector = (Vector)session.getAttribute("moduleList");
	int right=Security.securiPage("Defects ",request,response);	
	long prjID = Long.parseLong((String) session.getAttribute("projectID"));
    double normDefectRE[]=(double[])session.getAttribute("normDefectRE");    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>defectProductPlan.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");

	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>
<BODY class="BD" onLoad="loadPrjMenu()">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.defectProductPlan.Plandefectbyproduct")%></P>
<TABLE cellspacing="1" class="Table" width="150%">
    <TR class="ColumnLabel" align="center" >
        <%=(right==3 && !isArchive)?"<TD  rowspan=3>Del.</TD>":""%>
        <TD rowspan=3 width="12%"><B> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Product")%> </B></TD> 
        <TD colspan=40><B> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.PlanbyQCActivity")%> </B></TD>
    </TR>
    <TR class="ColumnLabel" align="center">
        <TD colspan=4> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.RReview")%> </TD>
        <TD colspan=4> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.DReview")%> </TD>
        <TD colspan=4> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.CReview")%> </TD>
        <TD colspan=4> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.UTest")%> </TD>
        <TD colspan=4> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.ITest")%> </TD>
        <TD colspan=4> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.STest")%> </TD>
        <TD colspan=4> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.OReview")%> </TD>
        <TD colspan=4> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.OTest")%> </TD>
        <TD colspan=4> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Others")%> </TD>
        <TD colspan=4><B> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Total")%> </B></TD>
    </TR>
    <TR class="ColumnLabel" align="center">
        <TD> P </TD>
        <TD> R </TD>
        <TD> A </TD>
        <TD> D </TD>
        <TD> P </TD>
        <TD> R </TD>
        <TD> A </TD>
        <TD> D </TD>
        <TD> P </TD>
        <TD> R </TD>        
        <TD> A </TD>
        <TD> D </TD>
        <TD> P </TD>
        <TD> R </TD>        
        <TD> A </TD>
        <TD> D </TD>
        <TD> P </TD>
        <TD> R </TD>        
        <TD> A </TD>
        <TD> D </TD>
        <TD> P </TD>
        <TD> R </TD>        
        <TD> A </TD>
        <TD> D </TD>
        <TD> P </TD>
        <TD> R </TD>        
        <TD> A </TD>
        <TD> D </TD>
        <TD> P </TD>
        <TD> R </TD>        
        <TD> A </TD>
        <TD> D </TD>
        <TD> P </TD>
        <TD> R </TD>        
        <TD> A </TD>
        <TD> D </TD>
        <TD> P </TD>
        <TD> R </TD>        
        <TD> A </TD>
        <TD> D </TD>
    </TR>
   <%
	double Total_RR[]={Double.NaN,Double.NaN,Double.NaN,Double.NaN};
	double Total_DR[]={Double.NaN,Double.NaN,Double.NaN,Double.NaN};
	double Total_CR[]={Double.NaN,Double.NaN,Double.NaN,Double.NaN};
	double Total_UT[]={Double.NaN,Double.NaN,Double.NaN,Double.NaN};
	double Total_IT[]={Double.NaN,Double.NaN,Double.NaN,Double.NaN};
	double Total_ST[]={Double.NaN,Double.NaN,Double.NaN,Double.NaN};
	double Total_OR[]={Double.NaN,Double.NaN,Double.NaN,Double.NaN};
	double Total_OT2[]={Double.NaN,Double.NaN,Double.NaN,Double.NaN};
	double Total_OT[]={Double.NaN,Double.NaN,Double.NaN,Double.NaN};
	double Total_TT[]={Double.NaN,Double.NaN,Double.NaN,Double.NaN};
    String strCurrentItem = "";    
    String boldTag;
    String firstRow="";
    String classCell="";
    for (int i = 0; i < defectInfoVector.size(); i++) {
    	defectInfo=(DefectProductPlanInfo)defectInfoVector.elementAt(i);        		   	        	
		firstRow="<TD></TD>";
		if (right==3 && !isArchive){
			if (!defectInfo.released){	
				countNotReleased++;
				classCell="CellBGRnews";
			}else{
				classCell="CellBGRdark";
			}	

			firstRow="<TD style='text-align: center'><A HREF='javascript:deleteMe("+i+")'>Del.</A></TD>";				
		}
		
		for (int n=0;n<3;n++){
			if (!Double.isNaN(defectInfo.requirementReview[n+1]))
				Total_RR[n]=Double.isNaN(Total_RR[n])?defectInfo.requirementReview[n+1]:Total_RR[n]+defectInfo.requirementReview[n+1];
			if (!Double.isNaN(defectInfo.designReview[n+1]))
				Total_DR[n]=Double.isNaN(Total_DR[n])?defectInfo.designReview[n+1]:Total_DR[n]+defectInfo.designReview[n+1];
			if (!Double.isNaN(defectInfo.codeReview[n+1]))
				Total_CR[n]=Double.isNaN(Total_CR[n])?defectInfo.codeReview[n+1]:Total_CR[n]+defectInfo.codeReview[n+1];		
			if (!Double.isNaN(defectInfo.unitTest[n+1]))
				Total_UT[n]=Double.isNaN(Total_UT[n])?defectInfo.unitTest[n+1]:Total_UT[n]+defectInfo.unitTest[n+1];			
			if (!Double.isNaN(defectInfo.integrationTest[n+1]))
				Total_IT[n]=Double.isNaN(Total_IT[n])?defectInfo.integrationTest[n+1]:Total_IT[n]+defectInfo.integrationTest[n+1];				
			if (!Double.isNaN(defectInfo.systemTest[n+1]))
				Total_ST[n]=Double.isNaN(Total_ST[n])?defectInfo.systemTest[n+1]:Total_ST[n]+defectInfo.systemTest[n+1];		
			if (!Double.isNaN(defectInfo.otherReview[n+1]))
				Total_OR[n]=Double.isNaN(Total_OR[n])?defectInfo.otherReview[n+1]:Total_OR[n]+defectInfo.otherReview[n+1];		
			if (!Double.isNaN(defectInfo.otherTest[n+1]))
				Total_OT2[n]=Double.isNaN(Total_OT2[n])?defectInfo.otherTest[n+1]:Total_OT2[n]+defectInfo.otherTest[n+1];					
			if (!Double.isNaN(defectInfo.others[n+1]))
				Total_OT[n]=Double.isNaN(Total_OT[n])?defectInfo.others[n+1]:Total_OT[n]+defectInfo.others[n+1];
			if (!Double.isNaN(defectInfo.total[n+1]))
				Total_TT[n]=Double.isNaN(Total_TT[n])?defectInfo.total[n+1]:Total_TT[n]+defectInfo.total[n+1];							
		}
	
	%>
    <TR class=<%=classCell%>>   
        <%=firstRow%>
        <TD><%=((defectInfo.moduleName==null)?"":anchor("","")+defectInfo.moduleName)%></TD>        
        <TD><%=anchor("RR",CommonTools.updateDouble(defectInfo.requirementReview[0]))+CommonTools.formatNumber(defectInfo.requirementReview[1], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.requirementReview[2], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.requirementReview[3], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.requirementReview[4], false)%></TD>        
                
        <TD><%=anchor("DR",CommonTools.updateDouble(defectInfo.designReview[0]))+CommonTools.formatNumber(defectInfo.designReview[1], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.designReview[2], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.designReview[3], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.designReview[4], false)%></TD>        
       
        <TD><%=anchor("CR",CommonTools.updateDouble(defectInfo.codeReview[0]))+CommonTools.formatNumber(defectInfo.codeReview[1], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.codeReview[2], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.codeReview[3], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.codeReview[4], false)%></TD>        
        
        <TD><%=anchor("UT",CommonTools.updateDouble(defectInfo.unitTest[0]))+CommonTools.formatNumber(defectInfo.unitTest[1], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.unitTest[2], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.unitTest[3], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.unitTest[4], false)%></TD>        
        
        <TD><%=anchor("IT",CommonTools.updateDouble(defectInfo.integrationTest[0]))+CommonTools.formatNumber(defectInfo.integrationTest[1], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.integrationTest[2], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.integrationTest[3], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.integrationTest[4], false)%></TD>        

        <TD><%=anchor("ST",CommonTools.updateDouble(defectInfo.systemTest[0]))+CommonTools.formatNumber(defectInfo.systemTest[1], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.systemTest[2], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.systemTest[3], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.systemTest[4], false)%></TD>       
        
        <TD><%=anchor("OR",CommonTools.updateDouble(defectInfo.otherReview[0]))+CommonTools.formatNumber(defectInfo.otherReview[1], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.otherReview[2], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.otherReview[3], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.otherReview[4], false)%></TD>        
        
        <TD><%=anchor("OT2",CommonTools.updateDouble(defectInfo.otherTest[0]))+CommonTools.formatNumber(defectInfo.otherTest[1], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.otherTest[2], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.otherTest[3], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.otherTest[4], false)%></TD>        
        
        <TD><%=anchor("OT",CommonTools.updateDouble(defectInfo.others[0]))+CommonTools.formatNumber(defectInfo.others[1],false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.others[2], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.others[3], false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.others[4], false)%></TD>        
        
        <TD><%=CommonTools.formatNumber(defectInfo.total[1],false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.total[2],false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.total[3],false)%></TD>
        <TD><%=CommonTools.formatNumber(defectInfo.total[4],false)%></TD>
     </TR>
     <%
    }    
    
%>
     <TR class="CellBGRdark">
	     <TD>&nbsp;</TD>
	     <TD><B> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Total1")%> </B></TD>
	     <%double dbltemp;
	       for (int k=0;k<3;k++){%>
		     <TD><B><%=CommonTools.formatNumber(Total_RR[k],false)%></B></TD>
		 <%}%>	 	
			<TD><B><%=CommonTools.formatNumber(CommonTools.metricDeviation(Total_RR[0], Total_RR[1],Total_RR[2]),false)%></B></TD>		
				
		 <%for (int k=0;k<3;k++){%>
		     <TD><B><%=CommonTools.formatNumber(Total_DR[k],false)%></B></TD>
		 <%}%>	 	    
			<TD><B><%=CommonTools.formatNumber(CommonTools.metricDeviation(Total_DR[0],Total_DR[1],Total_DR[2]),false)%></B></TD>
 		 <%for (int k=0;k<3;k++){%>
	   	     <TD><B><%=CommonTools.formatNumber(Total_CR[k],false)%></B></TD>
		 <%}%>	 	    
			<TD><B><%=CommonTools.formatNumber(CommonTools.metricDeviation(Total_CR[0],Total_CR[1],Total_CR[2]),false)%></B></TD>
 		 <%for (int k=0;k<3;k++){%>
		     <TD><B><%=CommonTools.formatNumber(Total_UT[k],false)%></B></TD>	     
		 <%}%>	 	    	
			<TD><B><%=CommonTools.formatNumber(CommonTools.metricDeviation(Total_UT[0],Total_UT[1],Total_UT[2]),false)%></B></TD>				           
 		 <%for (int k=0;k<3;k++){%>
		     <TD><B><%=CommonTools.formatNumber(Total_IT[k],false)%></B></TD>		     
		 <%}%>
			<TD><B><%=CommonTools.formatNumber(CommonTools.metricDeviation(Total_IT[0],Total_IT[1],Total_IT[2]),false)%></B></TD>							
 		 <%for (int k=0;k<3;k++){%>
		     <TD><B><%=CommonTools.formatNumber(Total_ST[k],false)%></B></TD>
		 <%}%>
			<TD><B><%=CommonTools.formatNumber(CommonTools.metricDeviation(Total_ST[0],Total_ST[1],Total_ST[2]),false)%></B></TD>			
 		 <!-- Add new -->
 		 <%for (int k=0;k<3;k++){%>
		     <TD><B><%=CommonTools.formatNumber(Total_OR[k],false)%></B></TD>	     
		 <%}%>
			<TD><B><%=CommonTools.formatNumber(CommonTools.metricDeviation(Total_OR[0],Total_OR[1],Total_OR[2]),false)%></B></TD>										          		 
		 <%for (int k=0;k<3;k++){%>
		     <TD><B><%=CommonTools.formatNumber(Total_OT2[k],false)%></B></TD>	     
		 <%}%>
			<TD><B><%=CommonTools.formatNumber(CommonTools.metricDeviation(Total_OT2[0],Total_OT2[1],Total_OT2[2]),false)%></B></TD>		
 		 <!-- End -->
 		 <%for (int k=0;k<3;k++){%>
		     <TD><B><%=CommonTools.formatNumber(Total_OT[k],false)%></B></TD>	     
		 <%}%>
			<TD><B><%=CommonTools.formatNumber(CommonTools.metricDeviation(Total_OT[0],Total_OT[1],Total_OT[2]),false)%></B></TD>										         
 		 <%for (int k=0;k<3;k++){%>
		     <TD><B><%=CommonTools.formatNumber(Total_TT[k],false)%></B></TD>
		 <%}%>
			<TD><B><%=CommonTools.formatNumber(CommonTools.metricDeviation(Total_TT[0],Total_TT[1],Total_TT[2]),false)%></B></TD>         
	</TR>
</TABLE>
<P><font color="green">- <B> P: </B> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Plan")%> &nbsp;&nbsp;<B> R: </B> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Replan")%> &nbsp;&nbsp;<B> A: </B> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Actual")%> &nbsp;&nbsp;<B> D: </B> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Deviation")%></font></P>
<P><font color="green"> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Releasedproductisingreycolor.")%> </font></P>
<BR>
<P><font color="blue"> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Pleaseselectaproducttoplanford")%> </font></P>
<BR>
<FORM action="Fms1Servlet" name="frmDefectPlan" method="POST" onkeydown="ignoreESCkey();">
<INPUT type="hidden" name="reqType" value="<%=Constants.ADD_WP_DEFECT_PLAN%>">
<INPUT type="hidden" name="deleteID">
<TABLE border="0" width="80%" cellspacing="0" cellpadding="0" class="BlankTable">
	<TR>
		<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Product1")%> </TD>
		<TD width="60%"><SELECT name="cboModule" class="COMBO" onchange="selectModule(this.selectedIndex)" tabindex="1">
	        <OPTION value = "0" selected></OPTION>
			<%
			WorkProductInfo wpInfo;
			WPSizeInfo moduleInfo;
			cboMapping=new int[moduleVector.size()];
			for (int i =0;i<moduleVector.size();i++){
				moduleInfo=(WPSizeInfo)moduleVector.elementAt(i);
				//create Index mapping between WP and module combo
				for (int j =0;j<wpVector.size();j++){
					wpInfo=(WorkProductInfo)wpVector.elementAt(j);
					if (wpInfo.workProductID==moduleInfo.workProductID){
						cboMapping[i]=j+1;//the cbo of work product has one more row for blank choice
						break;
					}	
				}
			%>
			<OPTION value = "<%=i+1%>"><%=moduleInfo.name%></OPTION>
			<%}%>
			</SELECT></TD>
	</TR>
	
	<TR>
		<TD width="20%"><%=languageChoose.getMessage("fi.jsp.defectProductPlan.Workproduct")%></TD>
		<TD width="60%">
		    <SELECT name="cboWorkProduct" class="COMBO" onchange="selectWO(this.selectedIndex)"  tabindex="1">
		        <OPTION value = "0" selected></OPTION>
				<%for (int i =0;i<wpVector.size();i++){
					wpInfo=(WorkProductInfo)wpVector.elementAt(i);%>				
				<OPTION value ="<%=i+1%>"><%=languageChoose.getMessage(wpInfo.workProductName)%></OPTION>
				<%}%>
			</SELECT>
		</TD>
	</TR>    
</TABLE>
<BR>
<TABLE border="0" width="80%" cellspacing="0" cellpadding="0" class="BlankTable">
    <COLGROUP>
    	<COL width="20%">
        <COL width="20%">
        <COL width="20%">
        <COL width="20%">
     <TR>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.RequirementReview")%> </TD>
        <TD><INPUT type="text" name="RR" size="5" maxlength="5" tabindex="2">&nbsp;&nbsp;%</TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.UnitTest")%> </TD>
        <TD><INPUT type="text" name="UT" size="5" maxlength="5" tabindex="3">&nbsp;&nbsp;%</TD>
    </TR>
    <TR>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.DesignReview")%> </TD>
        <TD><INPUT type="text" name="DR" size="5" maxlength="5" tabindex="4">&nbsp;&nbsp;%</TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.IntegrationTest")%> </TD>
        <TD><INPUT type="text" name="IT" size="5" maxlength="5" tabindex="5">&nbsp;&nbsp;%</TD>
    </TR>
    <TR>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.CodeReview")%> </TD>
        <TD><INPUT type="text" name="CR" size="5" maxlength="5" tabindex="6">&nbsp;&nbsp;%</TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.SystemTest")%> </TD>
        <TD><INPUT type="text" name="ST" size="5" maxlength="5" tabindex="7">&nbsp;&nbsp;%</TD>
    </TR>
    <!-- Add Other Test and Other Review -->
    <TR>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.OtherReview")%> </TD>
        <TD><INPUT type="text" name="OR" size="5" maxlength="5" tabindex="8">&nbsp;&nbsp;%</TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.OtherTest")%> </TD>
        <TD><INPUT type="text" name="OT2" size="5" maxlength="5" tabindex="9">&nbsp;&nbsp;%</TD>
    </TR>
    <!-- End -->
    <TR>
        <TD colspan="2">&nbsp;</TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Others1")%> </TD>
        <TD><INPUT type="text" name="OT" size="5" maxlength="5"  tabindex="10">&nbsp;&nbsp;%</TD>
    </TR>
</TABLE>
<BR>
<P>
<%if (right==3 && !isArchive){%>
<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.defectProductPlan.Update")%>" name="btonAdd" class="BUTTONWIDTH" onclick="validate()" tabindex="9">
<%}%>
<INPUT type="button" value=" <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Usenorms")%> " name="btUseNorm" class="BUTTON" onclick="useNorm()" tabindex="10">
<%if (defectInfoVector.size()>0){%>
	<INPUT type="button" value=" <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Replan1")%> " name="btReplan" class="BUTTON" onclick="doIt(<%=Constants.DETAIL_DEFECT_REPLAN%>)" tabindex="11">
<%}%>
<INPUT type="button" value=" <%=languageChoose.getMessage("fi.jsp.defectProductPlan.Defectrate")%> " name="Defect rate" class="BUTTON" onclick="doIt(<%=Constants.DETAIL_DEFECT_RATE%>)" tabindex="12">
<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.defectProductPlan.Back")%>" class="BUTTON" onclick="doIt(<%=Constants.DEFECT_VIEW%>)" tabindex="13">

<%if(right==3 && !isArchive){%>
</FORM>

<SCRIPT language="javascript">
var fields=["RR","DR","CR","UT","IT","ST","OR","OT2","OT"];
var cboMapping=new Array();
cboMapping[0]=0;	
<%for (int i=0;i<cboMapping.length;i++){
%>cboMapping[<%=i+1%>]=<%=cboMapping[i]%>;
<%}%>
function clickWP(name){	
	for ( var j=0;j < frmDefectPlan.cboWorkProduct.options.length;j++){
		if (frmDefectPlan.cboWorkProduct.options[j].text==name){
			frmDefectPlan.cboWorkProduct.selectedIndex=j;
			selectWO(j);
			return;
		}
	}
}
function clickMod(name){	
	
	for ( var j=0;j < frmDefectPlan.cboModule.options.length;j++){
		if (frmDefectPlan.cboModule.options[j].text==name){
			frmDefectPlan.cboModule.selectedIndex=j;
			selectModule(j);
			return;
		}
	}
}
function selectWO(index){	
	if (index==0){
		clearAll();
	}
	else{
		hideObj('btonAdd');
		frmDefectPlan.cboModule.selectedIndex=0;
		var anchorName=frmDefectPlan.cboWorkProduct.options[index].text;
		if (document.all[anchorName]==null){
			//the data doesn't exist in the table
			for (i=0;i<fields.length;i++){
				frmDefectPlan[fields[i]].value="";
			}	
			frmDefectPlan.btonAdd.value="<%=languageChoose.getMessage("fi.jsp.defectProductPlan.Addnewworkproduct")%>";
		}else{
			//the data exist in the table, we copy the values in the input fields						
			for (i=0;i<fields.length;i++){
				frmDefectPlan[fields[i]].value=document.all[anchorName+fields[i]].innerText;
			}	
			frmDefectPlan.btonAdd.value="<%=languageChoose.getMessage("fi.jsp.defectProductPlan.Updateworkproduct")%>";
		}		
		
	}
}

function selectModule(index){
	if (index==0){
		clearAll();
	}else{
		frmDefectPlan.cboWorkProduct.selectedIndex=cboMapping[index];		
		var anchorName="MoDuLe"+frmDefectPlan.cboModule.options[index].text;
		var isReleased=false;		
		showObj("btonAdd");			
	
		if (document.all[anchorName]==null){
			for (i=0;i<fields.length;i++){
				frmDefectPlan[fields[i]].value="";
			}	
			
			frmDefectPlan.btonAdd.value="<%=languageChoose.getMessage("fi.jsp.defectProductPlan.Addnewproduct")%>";
		}else{
			for (i=0;i<fields.length;i++){		
				if (trim(document.all[anchorName+fields[i]].id)!="N/A"){
					frmDefectPlan[fields[i]].value=document.all[anchorName+fields[i]].id;
				}else{
					frmDefectPlan[fields[i]].value="";
				}
					
				if (document.all[anchorName+fields[i]].type=="true")
					isReleased=true;				
			}
				
			frmDefectPlan.btonAdd.value="<%=languageChoose.getMessage("fi.jsp.defectProductPlan.Updateproduct")%>";
			
			if (isReleased)
				hideObj('btonAdd');

		}	
	}
	
	return;
}

function validate(){		
	var bl=true;
	for (k=0;k<fields.length;k++){		
		if (trim(frmDefectPlan[fields[k]].value)!=""){
			bl=false;
		}
	}
	if (bl){
		alert('<%=languageChoose.getMessage("fi.jsp.defectProductPlan.PleaseInputAtLeastAPlanValue")%>');
		return;
	}
	
	if (!checkNumber()){
		alert ('<%=languageChoose.getMessage("fi.jsp.defectProductPlan.PleaseInputAnPositiveNumber")%>');
		return;	
	}
	
	totalPlan=0;
	for (i=0;i<fields.length;i++)
		totalPlan+=Number(frmDefectPlan[fields[i]].value);
	
	if (totalPlan!=100){
		alert ('<%=languageChoose.getMessage("fi.jsp.defectProductPlan.TotalOfPlanByQCActivitiesMustEqual100")%>');
		return;
	}
		
	frmDefectPlan.submit();
}
function clearAll(){
	frmDefectPlan.reset();
	hideObj("btonAdd");
}
function deleteMe(id){
	if (window.confirm("<%=languageChoose.getMessage("fi.jsp.defectProductPlan.Areyousuretodeletethisitem")%>")){
		frmDefectPlan.reqType.value=<%=Constants.DELETE_DEFECT_PLAN%>;
		frmDefectPlan.deleteID.value=id;
		frmDefectPlan.submit();
	} 
}

function useNorm(){
	frmDefectPlan.RR.value="<%=CommonTools.formatNumber(normDefectRE[0],false)%>";
	frmDefectPlan.DR.value="<%=CommonTools.formatNumber(normDefectRE[1],false)%>";	
	frmDefectPlan.CR.value="<%=CommonTools.formatNumber(normDefectRE[2],false)%>";	
	frmDefectPlan.UT.value="<%=CommonTools.formatNumber(normDefectRE[3],false)%>";	
	frmDefectPlan.IT.value="<%=CommonTools.formatNumber(normDefectRE[4],false)%>";	
	frmDefectPlan.ST.value="<%=CommonTools.formatNumber(normDefectRE[5],false)%>";	
	frmDefectPlan.OT.value="<%=CommonTools.formatNumber(normDefectRE[6],false)%>";	
	frmDefectPlan.OR.value = "<%=CommonTools.formatNumber(normDefectRE[7],false)%>";	
	frmDefectPlan.OT2.value = "<%=CommonTools.formatNumber(normDefectRE[8],false)%>";
 }

function checkNumber(){	
	
	if (trim(frmDefectPlan.RR.value).length>0){
		if (parseFloat(frmDefectPlan.RR.value)<0){
			frmDefectPlan.RR.value="";
			frmDefectPlan.RR.focus();
			return false;
		}
	}

	if (trim(frmDefectPlan.DR.value).length>0){	
		if (parseFloat(frmDefectPlan.DR.value)<0){
			frmDefectPlan.DR.value="";
			frmDefectPlan.DR.focus();
			return false;
		}	
	}

	if (trim(frmDefectPlan.CR.value).length>0){	
		if (parseFloat(frmDefectPlan.CR.value)<0){
			frmDefectPlan.CR.value="";
			frmDefectPlan.CR.focus();
			return false;
		}
	}
	
	if (trim(frmDefectPlan.UT.value).length>0){		
		if (parseFloat(frmDefectPlan.UT.value)<0){
			frmDefectPlan.UT.value="";
			frmDefectPlan.UT.focus();
			return false;
		}
	}
	
	if (trim(frmDefectPlan.IT.value).length>0){			
		if (parseFloat(frmDefectPlan.IT.value)<0){
			frmDefectPlan.IT.value="";
			frmDefectPlan.IT.focus();
			return false;
		}	
	}
	
	if (trim(frmDefectPlan.ST.value).length>0){			
		if (parseFloat(frmDefectPlan.ST.value)<0){
			frmDefectPlan.ST.value="";
			frmDefectPlan.ST.focus();
			return false;
		}
	}
	
	if (trim(frmDefectPlan.OT.value).length>0){			
		if (parseFloat(frmDefectPlan.OT.value)<0){
			frmDefectPlan.OT.value="";
			frmDefectPlan.OT.focus();
			return false;
		}
	}	
	
	return true;			
}

</SCRIPT>
<%}%>
</BODY>
</HTML>




