<%@page import="com.fms1.infoclass.*,com.fms1.tools.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<%!
DefectPlanInfo defectInfo;
int [] cboMapping;//array for combo synchronization
/*
*We create anchor so that when the user selects a value in a combo,
* the corresponding values are copied from the list to the input 
*fields for update
*/
String anchor(String type){
	if (defectInfo.moduleName==null){
			return "<A name='"+defectInfo.wpName+type+"' "+(("".equals(type)&&!Double.isNaN(defectInfo.wpID))? "href=\"javascript:clickWP('"+defectInfo.wpName+"')\"":"")+">";
	}
	else
		return "<A name='MoDuLe"+defectInfo.moduleName+type+"' "+(("".equals(type)&&!Double.isNaN(defectInfo.wpID))? "href=\"javascript:clickMod('"+defectInfo.moduleName+"')\"":"")+">";//add letter M for module anchors
}
%>
<%
	Vector defectInfoVector = (Vector)session.getAttribute("defectPlanDetail");
	Vector wpVector = (Vector)session.getAttribute("wpList");
	Vector moduleVector = (Vector)session.getAttribute("moduleList");
	int right=Security.securiPage("Defects ",request,response);
	DefectInfo summaryDefectInfo = (DefectInfo)session.getAttribute("defectInfo");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

<TITLE>defectPlanDetail.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<BODY class="BD" 
	<%if (right==3&& !isArchive){%>
		onLoad="loadPrjMenu();hideObj('btonAdd');"
	<%}else{%>
		onLoad="loadPrjMenu();"
	<%}%>	
>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.DefectPlanDetail.Planneddefectlisting")%></P>
<TABLE  cellspacing="1" class="Table" width="95%" >
    <COLGROUP>
        <%=(right==3 && !isArchive)?"<COL width='5%'>":""%>
        <COL width="21%">
        <COL width="21%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
    <TR class="ColumnLabel" align="center" >
        <%=(right==3 && !isArchive)?"<TD  rowspan=2>Del.</TD>":""%>
        <TD rowspan=2><B> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.WProduct")%> </B></TD>
        <TD rowspan=2><B> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.Product")%> </B></TD>
        <TD rowspan=2><B> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.PlannedDefect")%> </B></TD>
        <TD rowspan=2><B> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.ReplannedDefect")%> </B></TD>
        <TD colspan=9><B> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.PlanbyQCActivity")%> </B></TD>
    </TR>
    <TR class="ColumnLabel" align="center">
        <TD> DR </TD>
        <TD> PR </TD>
        <TD> CR </TD>
        <TD> UT </TD>
        <TD> IT </TD>
        <TD> ST </TD>
        <TD> AT </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.Others")%> </TD>
        <TD><B> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.Total")%> </B></TD>
    </TR>
   <%
    String strCurrentItem = "";
    
    boolean styleFlag=false;
    String boldTag;
    String style;
    String displayWPName;
    String firstRow="";
    for (int i = 0; i < defectInfoVector.size(); i++) {
    	defectInfo=(DefectPlanInfo)defectInfoVector.elementAt(i);
        if (strCurrentItem.equals(defectInfo.wpName)) {
			displayWPName="";
			styleFlag=!styleFlag;
        }
        else{
        	strCurrentItem = defectInfo.wpName;
        	displayWPName= defectInfo.wpName;
        }
        style =(styleFlag)? "CellBGRnews":"CellBGR3";
		styleFlag=!styleFlag;
		//no work product id, then it is the summary rows, must be bold
		boldTag=(Double.isNaN(defectInfo.wpID))?"<B>":"";
		if (right==3 && !isArchive){
			if (!Double.isNaN(defectInfo.wpID))
				firstRow="<TD style='text-align: center'><A HREF='javascript:deleteMe("+i+")'>D</A></TD>";
			else
				firstRow="<TD></TD>";//the summary rows can't be deleted
		}
%>
    <TR class="<%=style%>">
        <%=firstRow%>
    	<%if(!isArchive){%>
        <TD><%=boldTag+anchor("")+languageChoose.getMessage(displayWPName)%></TD> 
        <TD><%=boldTag+((defectInfo.moduleName==null)?"":anchor("")+defectInfo.moduleName)%></TD>
        <%}else{%>	
        <TD><%=boldTag+languageChoose.getMessage(displayWPName)%></TD> 
        <TD><%=boldTag+((defectInfo.moduleName==null)?"":defectInfo.moduleName)%></TD>
        <%}%>	
        <TD><%=boldTag+anchor("PD")+CommonTools.updateDouble(defectInfo.plannedWDefect)%></TD>
        <TD><%=boldTag+anchor("RPD")+CommonTools.updateDouble(defectInfo.rePlannedWDefect)%></TD>
        <TD><%=boldTag+anchor("DR")+CommonTools.updateDouble(defectInfo.documentReview)%></TD>
        <TD><%=boldTag+anchor("PR")+CommonTools.updateDouble(defectInfo.prototypeReview)%></TD>
        <TD><%=boldTag+anchor("CR")+CommonTools.updateDouble(defectInfo.codeReview)%></TD>
        <TD><%=boldTag+anchor("UT")+CommonTools.updateDouble(defectInfo.unitTest)%></TD>
        <TD><%=boldTag+anchor("IT")+CommonTools.updateDouble(defectInfo.integrationTest)%></TD>
        <TD><%=boldTag+anchor("ST")+CommonTools.updateDouble(defectInfo.systemTest)%></TD>
        <TD><%=boldTag+anchor("AT")+CommonTools.updateDouble(defectInfo.acceptanceTest)%></TD>
        <TD><%=boldTag+anchor("OT")+CommonTools.updateDouble(defectInfo.others)%></TD>
        <TD><%=boldTag+CommonTools.updateDouble(defectInfo.total)%></TD>
     </TR>
     <%
    }
%>
</TABLE>
<BR><BR>

<%if (right==3 && !isArchive){%>
<P>  <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.PleaseselectEITHERaworkproduct")%> </P>
<FORM action="Fms1Servlet" name="frmDefectPlan" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.ADD_WP_DEFECT_PLAN%>">
<INPUT type="hidden" name="deleteID">
<TABLE border="0" width="95%" cellspacing="0" cellpadding="0" class="BlankTable">
<TR>
	<TD width="20%"><%=languageChoose.getMessage("fi.jsp.DefectPlanDetail.Workproduct")%></TD>
	<TD width="80%"><SELECT name="cboWorkProduct" class="COMBO" onchange="selectWO(this.selectedIndex)">
        <OPTION value = "0" selected></OPTION>
		<%
		WorkProductInfo wpInfo;
		for (int i =0;i<wpVector.size();i++){
			wpInfo=(WorkProductInfo)wpVector.elementAt(i);
		%>
		<OPTION value = "<%=i+1%>"><%=languageChoose.getMessage(wpInfo.workProductName)%></OPTION><%
		}%>
		</SELECT></TD> 
</TR>    
<TR>
	<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.Product1")%> </TD>
	<TD width="80%"><SELECT name="cboModule" class="COMBO" onchange="selectModule(this.selectedIndex)">
        <OPTION value = "0" selected></OPTION>
		<%
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
		<OPTION value = "<%=i+1%>"><%=moduleInfo.name%></OPTION><%
		}%>
		</SELECT></TD>
</TR>    

</TABLE>
<BR>
<TABLE border="0" width="95%" cellspacing="0" cellpadding="0" class="BlankTable">
    <COLGROUP>
    	<COL width='20%'>
        <COL width="25%">
        <COL width="20%">
        <COL width="25%">

     <TR>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.Planneddefects")%>* </TD>
        <TD><INPUT type="text" name="PD" size="11" maxlength="9" ><A name='remainPD'></A></TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.Replanneddefects")%> </TD>
        <TD><INPUT type="text" name="RPD" size="11" maxlength="9"><A name='remainRPD'></A></TD>
    </TR>
    <TR>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.DocumentReview")%> </TD>
        <TD><INPUT type="text" name="DR" size="11" maxlength="9" ><A name='remainDR'></A></TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.UnitTest")%> </TD>
        <TD><INPUT type="text" name="UT" size="11" maxlength="9"><A name='remainUT'></A></TD>
    </TR>
    <TR>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.PrototypeReview")%> </TD>
        <TD><INPUT type="text" name="PR" size="11" maxlength="9" ><A name='remainPR'></A></TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.IntegrationTest")%> </TD>
        <TD><INPUT type="text" name="IT" size="11" maxlength="9"><A name='remainIT'></A></TD>
    </TR>
    <TR>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.CodeReview")%> </TD>
        <TD><INPUT type="text" name="CR" size="11" maxlength="9"><A name='remainCR'></A></TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.SystemTest")%> </TD>
        <TD><INPUT type="text" name="ST" size="11" maxlength="9" ><A name='remainST'></A></TD>
    </TR>
    <TR>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.Others1")%> </TD>
        <TD><INPUT type="text" name="OT" size="11" maxlength="9" ><A name='remainOT'></A></TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectPlanDetail.AcceptanceTest")%> </TD>
        <TD><INPUT type="text" name="AT" size="11" maxlength="9"><A name='remainAT'></A></TD>
    </TR>
</TABLE>
<BR>
<P>
<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.DefectPlanDetail.Update")%>" name="btonAdd" class="BUTTONWIDTH" onclick="validate();">
<%}%>
<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.DefectPlanDetail.Back")%>" class="BUTTON" onclick="doIt(<%=Constants.DEFECT_VIEW%>);">
<%if(right==3 && !isArchive){%>
</FORM>
<SCRIPT language="javascript">
var fields=["PD","RPD","DR","UT","PR","IT","CR","ST","OT","AT"];
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
		frmDefectPlan.cboModule.selectedIndex=0;
		var anchorName=frmDefectPlan.cboWorkProduct.options[index].text;
		showObj("btonAdd");
		if (document.all[anchorName]==null){
			//the data doesn't exist in the table
			for (i=0;i<fields.length;i++)
				frmDefectPlan[fields[i]].value="";
			frmDefectPlan.btonAdd.value="<%=languageChoose.getMessage("fi.jsp.DefectPlanDetail.AddnewWP")%>";
		}
		else{
			//the data exist in the table, we copy the values in the input fields
			for (i=0;i<fields.length;i++)
				frmDefectPlan[fields[i]].value=document.all[anchorName+fields[i]].innerText;
			frmDefectPlan.btonAdd.value="<%=languageChoose.getMessage("fi.jsp.DefectPlanDetail.UpdateWP")%>";
		}
		
		//calculate max values for wp planned and replanned defect
		var remainPD="<%=(Double.isNaN(summaryDefectInfo.estimatedDefect))?"NaN":Double.toString(summaryDefectInfo.estimatedDefect)%>";
		var remainRPD="<%=(Double.isNaN(summaryDefectInfo.reestimatedDefect))?"NaN":Double.toString(summaryDefectInfo.reestimatedDefect)%>";

		for (i=1;i<frmDefectPlan.cboWorkProduct.length;i++){
			if (i!=index){
				var wpAnchorName=frmDefectPlan.cboWorkProduct.options[i].text;
				if (document.all[wpAnchorName]!=null){
					var wpPDValue=document.all[wpAnchorName+"PD"].innerText;
					var wpRPDValue=document.all[wpAnchorName+"RPD"].innerText;
					if (!isNaN(wpPDValue)&&!isNaN(remainPD))
						remainPD -=Number(wpPDValue);
					if (!isNaN(wpRPDValue)&&!isNaN(remainRPD))
						remainRPD -=Number(wpRPDValue);
				}
			}
		}

		if (!isNaN(remainPD))
			document.all["remainPD"].innerText="("+remainPD+")";
		if (!isNaN(remainRPD))
			document.all["remainRPD"].innerText="("+remainRPD+")";

	}
}
function selectModule(index){
	if (index==0){
		clearAll();
	}
	else{
		frmDefectPlan.cboWorkProduct.selectedIndex=cboMapping[index];
		showObj("btonAdd");
		for (i=0;i<fields.length;i++){//clean all remain fields
			document.all["remain"+fields[i]].innerText="";
		}
		//calculate remaining
		//get value for the WP

		var wpAnchorName=frmDefectPlan.cboWorkProduct.options[cboMapping[index]].text;
		if (document.all[wpAnchorName]!=null){//the WP planning exists
			for (i=0;i<fields.length;i++){
				var wpFieldVal=document.all[wpAnchorName+fields[i]].innerText;
				if (!isNaN(wpFieldVal)){
					var remain=Number(wpFieldVal);
					for (j=1;j<cboMapping.length;j++){//browse the modules
						if ((cboMapping[index]==cboMapping[j])&&(j!=index)){
						//modules with the same WP
							var moduleAnchorName="MoDuLe"+frmDefectPlan.cboModule.options[j].text;
							if (document.all[moduleAnchorName]!=null){
								//the field is in the table
								var moduleFieldValue=document.all[moduleAnchorName+fields[i]].innerText;
								if (!isNaN(moduleFieldValue))
									remain -=Number(moduleFieldValue);
							}
						}
					}
					document.all["remain"+fields[i]].innerText="("+remain+")";
				}
			}
		}
		var anchorName="MoDuLe"+frmDefectPlan.cboModule.options[index].text;
		if (document.all[anchorName]==null){
			for (i=0;i<fields.length;i++)
				frmDefectPlan[fields[i]].value="";
			frmDefectPlan.btonAdd.value="<%=languageChoose.getMessage("fi.jsp.DefectPlanDetail.AddnewProduct")%>";
		}
		else{
			for (i=0;i<fields.length;i++)
				frmDefectPlan[fields[i]].value=document.all[anchorName+fields[i]].innerText;
			frmDefectPlan.btonAdd.value="<%=languageChoose.getMessage("fi.jsp.DefectPlanDetail.UpdateProduct")%>";
		}
	}

}
function validate(){
	if (!mandatoryFld(frmDefectPlan.PD,"<%=languageChoose.getMessage("fi.jsp.defectPlanDetail.Planneddefects")%>"))
		return;
	for (k=0;k<fields.length;k++){
		if (!positiveFld(frmDefectPlan[fields[k]],"<%=languageChoose.getMessage("fi.jsp.defectPlanDetail.Thisfield")%>"))
			return;
	}
	totalDetail=0;
	for (i=2;i<fields.length;i++)
		totalDetail+=Number(frmDefectPlan[fields[i]].value);
	var  totalReplanned=0;
	totalReplanned=Number((trim(frmDefectPlan.RPD.value)=="")? frmDefectPlan.PD.value : frmDefectPlan.RPD.value);
	if (totalDetail >totalReplanned){
		window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.DefectPlanDetail.The__sum__of__defect__by__activity__must__be__equal__or__below__Replanned__defects__~PARAM1_NUMBER~__~PARAM2_NUMBER~")%>', totalDetail,totalReplanned)));
		frmDefectPlan.RPD.focus();
		return;
	}
	frmDefectPlan.submit();
}
function clearAll(){
	frmDefectPlan.reset();
	hideObj("btonAdd");
	var remainAnchorName;
	for (i=0;i<fields.length;i++){
		remainAnchorName="remain"+fields[i];
		document.all[remainAnchorName].innerText="";
	}
}
function deleteMe(id){
	if (window.confirm("<%=languageChoose.getMessage("fi.jsp.DefectPlanDetail.DeleteGal")%>")){
		frmDefectPlan.reqType.value=<%=Constants.DELETE_DEFECT_PLAN%>;
		frmDefectPlan.deleteID.value=id;
		frmDefectPlan.submit();
	} 
}
</SCRIPT>
<%}%>

</BODY>



</HTML>
