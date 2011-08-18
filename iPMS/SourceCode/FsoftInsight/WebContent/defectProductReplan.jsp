<%@page import="com.fms1.infoclass.*,com.fms1.tools.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<%
DefectProductReplanInfo defectInfo;
int right=Security.securiPage("Defects ",request,response);

Vector defectInfoVector = (Vector)session.getAttribute("defectProductReplan");
int countNotReleased=0;
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

<TITLE>defectProductReplan.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.defectProductReplan.ReplanDefectByProduct")%></P>
<FORM action="Fms1Servlet" name="frmDefectProductReplan" method="POST" onkeydown="ignoreESCkey();">
<INPUT type="hidden" name="reqType" value="<%=Constants.UPDATE_WP_DEFECT_REPLAN%>">
<TABLE  cellspacing="1" class="Table" width="150%" >
	<!-- 
    <COLGROUP>
        <COL width="4%">
        <COL width="28%"> 
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">
        <COL width="4%">  
    -->  
    <TR class="ColumnLabel" align="center" >
        <TD rowspan=3>#</TD>
        <TD rowspan=3 width="15%"><B> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Product")%> </B></TD> 
        <TD colspan=32><B> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.PlanbyQCActivity")%> </B></TD>
    </TR>
    <TR class="ColumnLabel" align="center">
        <TD colspan=2> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.RReview")%> </TD>
        <TD colspan=2> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.DReview")%> </TD>
        <TD colspan=2> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.CReview")%> </TD>
        <TD colspan=2> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.UTest")%> </TD>
        <TD colspan=2> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.ITest")%> </TD>
        <TD colspan=2> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.STest")%> </TD>
        <TD colspan=2> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.OReview")%> </TD>
        <TD colspan=2> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.OTest")%> </TD>                        
        <TD colspan=2> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Others")%> </TD>
        <TD colspan=2><B> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Total")%> </B></TD>
    </TR>
    <TR class="ColumnLabel" align="center">
        <TD> A/F </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Replan")%> </TD>
        <TD> A/F </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Replan1")%> </TD>
        <TD> A/F </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Replan2")%> </TD>
        <TD> A/F </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Replan3")%> </TD>
        <TD> A/F </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Replan4")%> </TD>        
        <TD> A/F </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Replan5")%> </TD>
        <TD> A/F </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Replan6")%> </TD>        
        <TD> A/F </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Replan7")%> </TD>
        <TD> A/F </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Replan7")%> </TD>
        <TD> A/F </TD>
        <TD> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Replan7")%> </TD>                
    </TR>
<%
double Total_RR[]={Double.NaN,Double.NaN};
double Total_DR[]={Double.NaN,Double.NaN};
double Total_CR[]={Double.NaN,Double.NaN};
double Total_UT[]={Double.NaN,Double.NaN};
double Total_IT[]={Double.NaN,Double.NaN};
double Total_ST[]={Double.NaN,Double.NaN};
double Total_OR[]={Double.NaN,Double.NaN};
double Total_OT2[]={Double.NaN,Double.NaN};
double Total_OT[]={Double.NaN,Double.NaN};
double Total_TT[]={Double.NaN,Double.NaN};
String classCell="";
for (int i = 0; i < defectInfoVector.size(); i++) {
   	defectInfo=(DefectProductReplanInfo)defectInfoVector.elementAt(i);   	
   	if (!defectInfo.released){
	   	countNotReleased++;
		classCell="CellBGRnews";
	}else{
		classCell="CellBGRdark";
	}	   					
%>
	<TR class=<%=classCell%>>
        <TD><%=i+1%></TD>  		
        <TD nowrap><%=defectInfo.moduleName%></TD>     
        <TD nowrap>
	        <%=CommonTools.formatDouble(defectInfo.requirementReview[0])%>
    	    <%Total_RR[0]=Double.isNaN(Total_RR[0])?defectInfo.requirementReview[0]:Total_RR[0]+defectInfo.requirementReview[0];%>
        </TD> 
        <TD nowrap>
        	<%if (!defectInfo.released){%>
        	<INPUT type="text" name="replan_RR" size="7" maxlength="7" value="<%=CommonTools.updateDouble(defectInfo.requirementReview[1])%>">
        	<%}else{%>
        	<%=CommonTools.formatDouble(defectInfo.requirementReview[1])%>
        	<%}        	
        	Total_RR[1]=Double.isNaN(Total_RR[1])?defectInfo.requirementReview[1]:Total_RR[1]+defectInfo.requirementReview[1];%>
        </TD>         
        <TD nowrap>        	
        	<%=CommonTools.formatDouble(defectInfo.designReview[0])%>
   	        <%Total_DR[0]=Double.isNaN(Total_DR[0])?defectInfo.designReview[0]:Total_DR[0]+defectInfo.designReview[0];%>
        </TD>  
        <TD nowrap>
	        <%if (!defectInfo.released){%>
        	<INPUT type="text" name="replan_DR" size="7" maxlength="7" value="<%=CommonTools.updateDouble(defectInfo.designReview[1])%>">
        	<%}else{%>
        	<%=CommonTools.formatDouble(defectInfo.designReview[1])%>
        	<%}
        	Total_DR[1]=Double.isNaN(Total_DR[1])?defectInfo.designReview[1]:Total_DR[1]+defectInfo.designReview[1];%>
        </TD>              
        <TD nowrap>
        	<%=CommonTools.formatDouble(defectInfo.codeReview[0])%>
   	        <%Total_CR[0]=Double.isNaN(Total_CR[0])?defectInfo.codeReview[0]:Total_CR[0]+defectInfo.codeReview[0];%>
        </TD>   
        <TD nowrap>
	        <%if (!defectInfo.released){%>
        	<INPUT type="text" name="replan_CR" size="7" maxlength="7" value="<%=CommonTools.updateDouble(defectInfo.codeReview[1])%>">
        	<%}else{%>
        	<%=CommonTools.formatDouble(defectInfo.codeReview[1])%>
        	<%}
        	Total_CR[1]=Double.isNaN(Total_CR[1])?defectInfo.codeReview[1]:Total_CR[1]+defectInfo.codeReview[1];%>
        </TD>           
        <TD nowrap>
        	<%=CommonTools.formatDouble(defectInfo.unitTest[0])%>
   	        <%Total_UT[0]=Double.isNaN(Total_UT[0])?defectInfo.unitTest[0]:Total_UT[0]+defectInfo.unitTest[0];%>
        </TD>   
        <TD nowrap>
	        <%if (!defectInfo.released){%>        
        	<INPUT type="text" name="replan_UT" size="7" maxlength="7" value="<%=CommonTools.updateDouble(defectInfo.unitTest[1])%>">
        	<%}else{%>
        	<%=CommonTools.formatDouble(defectInfo.unitTest[1])%>
   	        <%}
   	        Total_UT[1]=Double.isNaN(Total_UT[1])?defectInfo.unitTest[1]:Total_UT[1]+defectInfo.unitTest[1];%>
        </TD>     
        <TD nowrap>
        	<%=CommonTools.formatDouble(defectInfo.integrationTest[0])%>
   	        <%Total_IT[0]=Double.isNaN(Total_IT[0])?defectInfo.integrationTest[0]:Total_IT[0]+defectInfo.integrationTest[0];%>        	
        </TD>     
        <TD nowrap>
   	        <%if (!defectInfo.released){%>
        	<INPUT type="text" name="replan_IT" size="7" maxlength="7" value="<%=CommonTools.updateDouble(defectInfo.integrationTest[1])%>">
        	<%}else{%>
        	<%=CommonTools.formatDouble(defectInfo.integrationTest[1])%>
   	        <%}
   	        Total_IT[1]=Double.isNaN(Total_IT[1])?defectInfo.integrationTest[1]:Total_IT[1]+defectInfo.integrationTest[1];%>        	
        </TD>   
        <TD nowrap>
        	<%=CommonTools.formatDouble(defectInfo.systemTest[0])%>
   	        <%Total_ST[0]=Double.isNaN(Total_ST[0])?defectInfo.systemTest[0]:Total_ST[0]+defectInfo.systemTest[0];%>        	        	
        </TD> 
        <TD nowrap>
	        <%if (!defectInfo.released){%>
        	<INPUT type="text" name="replan_ST" size="7" maxlength="7" value="<%=CommonTools.updateDouble(defectInfo.systemTest[1])%>">
        	<%}else{%>
			<%=CommonTools.formatDouble(defectInfo.systemTest[1])%>        	
   	        <%}
   	        Total_ST[1]=Double.isNaN(Total_ST[1])?defectInfo.systemTest[1]:Total_ST[1]+defectInfo.systemTest[1];%>        	        	        
        </TD>              
        <!-- Add Other Review and Other Test -->
        <!-- Other Review -->
        <TD nowrap>
        	<%=CommonTools.formatDouble(defectInfo.otherReview[0])%>
   	        <%Total_OR[0]=Double.isNaN(Total_OR[0])?defectInfo.otherReview[0]:Total_OR[0]+defectInfo.otherReview[0];%>        	        	
        </TD>
        <TD nowrap>
	        <%if (!defectInfo.released){%>
        	<INPUT type="text" name="replan_OR" size="7" maxlength="7" value="<%=CommonTools.updateDouble(defectInfo.otherReview[1])%>">
        	<%}else{%>
        	<%=CommonTools.formatDouble(defectInfo.otherReview[1])%>
   	        <%}
   	        Total_OR[1]=Double.isNaN(Total_OR[1])?defectInfo.otherReview[1]:Total_OR[1]+defectInfo.otherReview[1];%>        	        
        </TD>
        <!-- Other Test -->        
        <TD nowrap>
        	<%=CommonTools.formatDouble(defectInfo.otherTest[0])%>
   	        <%Total_OT2[0]=Double.isNaN(Total_OT2[0])?defectInfo.otherTest[0]:Total_OT2[0]+defectInfo.otherTest[0];%>        	
        </TD>                
        <TD nowrap>
	        <%if (!defectInfo.released){%>
        	<INPUT type="text" name="replan_OT2" size="7" maxlength="7" value="<%=CommonTools.updateDouble(defectInfo.otherTest[1])%>">
        	<%}else{%>
        	<%=CommonTools.formatDouble(defectInfo.otherTest[1])%>
   	        <%}
   	        Total_OT2[1]=Double.isNaN(Total_OT2[1])?defectInfo.otherTest[1]:Total_OT2[1]+defectInfo.otherTest[1];%>        	        
        </TD>        
        <!-- End -->
        <TD nowrap>
        	<%=CommonTools.formatDouble(defectInfo.others[0])%>
   	        <%Total_OT[0]=Double.isNaN(Total_OT[0])?defectInfo.others[0]:Total_OT[0]+defectInfo.others[0];%>        	
        </TD>        
		<TD nowrap>
	        <%if (!defectInfo.released){%>
        	<INPUT type="text" name="replan_OT" size="7" maxlength="7" value="<%=CommonTools.updateDouble(defectInfo.others[1])%>">
        	<%}else{%>
        	<%=CommonTools.formatDouble(defectInfo.others[1])%>
   	        <%}
   	        Total_OT[1]=Double.isNaN(Total_OT[1])?defectInfo.others[1]:Total_OT[1]+defectInfo.others[1];%>        	        
        </TD>        
        <TD nowrap>
        	<%=CommonTools.formatDouble(defectInfo.total[0])%>
        	<%Total_TT[0]=Double.isNaN(Total_TT[0])?defectInfo.total[0]:Total_TT[0]+defectInfo.total[0];%>        	
        </TD>
        <TD nowrap>
        	<%=CommonTools.formatDouble(defectInfo.total[1])%>
            <%Total_TT[1]=Double.isNaN(Total_TT[1])?defectInfo.total[1]:Total_TT[1]+defectInfo.total[1];%>        	
        </TD>
    </TR>   
<%}%>
 <TR class="CellBGRdark">
     <TD colspan=2 align=center><B> <%=languageChoose.getMessage("fi.jsp.defectProductReplan.Total1")%> </B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_RR[0])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_RR[1])%></B></TD>    	
     <TD><B><%=CommonTools.formatDouble(Total_DR[0])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_DR[1])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_CR[0])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_CR[1])%></B></TD>    	
     <TD><B><%=CommonTools.formatDouble(Total_UT[0])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_UT[1])%></B></TD>          
     <TD><B><%=CommonTools.formatDouble(Total_IT[0])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_IT[1])%></B></TD>    	
     <TD><B><%=CommonTools.formatDouble(Total_ST[0])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_ST[1])%></B></TD>
     <!-- Add new  -->
     <TD><B><%=CommonTools.formatDouble(Total_OR[0])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_OR[1])%></B></TD>             
     <TD><B><%=CommonTools.formatDouble(Total_OT2[0])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_OT2[1])%></B></TD>     
     <!--The End-->
     <TD><B><%=CommonTools.formatDouble(Total_OT[0])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_OT[1])%></B></TD>    	
     <TD><B><%=CommonTools.formatDouble(Total_TT[0])%></B></TD>
     <TD><B><%=CommonTools.formatDouble(Total_TT[1])%></B></TD>                                   
 </TR>   
</TABLE>
<P></P>
<BR>
<%if (countNotReleased>0){%>
<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.defectProductReplan.Update")%>" name="btonUpdate" class="BUTTONWIDTH" onclick="checkSubmit();">
<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.defectProductReplan.UseForecast")%>" name="btonUseForecast" class="BUTTONWIDTH" onclick="useForecast();">
<%}%>
<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.defectProductReplan.Back")%>" class="BUTTON" onclick="doIt(<%=Constants.DETAIL_DEFECT_PLAN%>);">
</FORM>
</BODY>
<%if(right==3){%>
<SCRIPT language="javascript">

	function useForecast(){
		<%int n=0;			
		  for (int i = 0; i < defectInfoVector.size(); i++) {
		   	defectInfo=(DefectProductReplanInfo)defectInfoVector.elementAt(i);   	
		   	if (!defectInfo.released){
		   	    if (!Double.isNaN(defectInfo.requirementReview[0])){
		   			if (countNotReleased==1){%>    
						frmDefectProductReplan.replan_RR.value='<%=CommonTools.formatDouble(defectInfo.requirementReview[0])%>';
					<%}else{%>
						frmDefectProductReplan.replan_RR[<%=n%>].value='<%=CommonTools.formatDouble(defectInfo.requirementReview[0])%>';
					<%}
				}				
		   		if (!Double.isNaN(defectInfo.designReview[0])){
		   			if (countNotReleased==1){%>
						frmDefectProductReplan.replan_DR.value='<%=CommonTools.formatDouble(defectInfo.designReview[0])%>';
					<%}else{%>
						frmDefectProductReplan.replan_DR[<%=n%>].value='<%=CommonTools.formatDouble(defectInfo.designReview[0])%>';					
					<%}
				}				
		   		if (!Double.isNaN(defectInfo.codeReview[0])){
		   			if (countNotReleased==1){%>
						frmDefectProductReplan.replan_CR.value='<%=CommonTools.formatDouble(defectInfo.codeReview[0])%>';		
					<%}else{%>
						frmDefectProductReplan.replan_CR[<%=n%>].value='<%=CommonTools.formatDouble(defectInfo.codeReview[0])%>';					
					<%}
				}		
		   		if (!Double.isNaN(defectInfo.unitTest[0])){
		   			if (countNotReleased==1){%>
						frmDefectProductReplan.replan_UT.value='<%=CommonTools.formatDouble(defectInfo.unitTest[0])%>';			
					<%}else{%>
						frmDefectProductReplan.replan_UT[<%=n%>].value='<%=CommonTools.formatDouble(defectInfo.unitTest[0])%>';							
					<%}
				}
		   		if (!Double.isNaN(defectInfo.integrationTest[0])){
		   			if (countNotReleased==1){%>		   		
						frmDefectProductReplan.replan_IT.value='<%=CommonTools.formatDouble(defectInfo.integrationTest[0])%>';		
					<%}else{%>
						frmDefectProductReplan.replan_IT[<%=n%>].value='<%=CommonTools.formatDouble(defectInfo.integrationTest[0])%>';																
					<%}
				}
				
		   		if (!Double.isNaN(defectInfo.systemTest[0])){
		   			if (countNotReleased==1){%>
						frmDefectProductReplan.replan_ST.value='<%=CommonTools.formatDouble(defectInfo.systemTest[0])%>';		
					<%}else{%>
						frmDefectProductReplan.replan_ST[<%=n%>].value='<%=CommonTools.formatDouble(defectInfo.systemTest[0])%>';								
					<%}
				}
		   		if (!Double.isNaN(defectInfo.others[0])){
		   			if (countNotReleased==1){%>		   		
						frmDefectProductReplan.replan_OT.value='<%=CommonTools.formatDouble(defectInfo.others[0])%>';				
					<%}else{%>
						frmDefectProductReplan.replan_OT[<%=n%>].value='<%=CommonTools.formatDouble(defectInfo.others[0])%>';						   								
			    	<%}				
		 		}
		 		
		 	    n++;
		    }
	      }%>		 
	}
	
	function validate(){
		if (1==<%=countNotReleased%>){
			if (trim(frmDefectProductReplan.replan_RR.value).length>0){
					if (parseFloat(frmDefectProductReplan.replan_RR.value)!=frmDefectProductReplan.replan_RR.value 
					    || parseFloat(frmDefectProductReplan.replan_RR.value)<0){
						frmDefectProductReplan.replan_RR.value="";
						alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
						frmDefectProductReplan.replan_RR.focus();
						return false;
					}								
			}	
			
			if (trim(frmDefectProductReplan.replan_DR.value).length>0){
				if (parseFloat(frmDefectProductReplan.replan_DR.value)!=frmDefectProductReplan.replan_DR.value
					|| parseFloat(frmDefectProductReplan.replan_DR.value)<0){
					frmDefectProductReplan.replan_DR.value="";
					alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
					frmDefectProductReplan.replan_DR.focus();
						return false;
				}								
			}	
			
			if (trim(frmDefectProductReplan.replan_CR.value).length>0){
				if (parseFloat(frmDefectProductReplan.replan_CR.value)!=frmDefectProductReplan.replan_CR.value
					|| parseFloat(frmDefectProductReplan.replan_CR.value)<0){
					frmDefectProductReplan.replan_CR.value="";
					alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
					frmDefectProductReplan.replan_CR.focus();
					return false;
				}								
			}	
			
			if (trim(frmDefectProductReplan.replan_UT.value).length>0){
				if (parseFloat(frmDefectProductReplan.replan_UT.value)!=frmDefectProductReplan.replan_UT.value
					|| parseFloat(frmDefectProductReplan.replan_UT.value)<0){
					frmDefectProductReplan.replan_UT.value="";
					alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
					frmDefectProductReplan.replan_UT.focus();
					return false;
				}								
			}	
		
			if (trim(frmDefectProductReplan.replan_IT.value).length>0){
				if (parseFloat(frmDefectProductReplan.replan_IT.value)!=frmDefectProductReplan.replan_IT.value
					|| parseFloat(frmDefectProductReplan.replan_IT.value)<0){
					frmDefectProductReplan.replan_IT.value="";
					alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
					frmDefectProductReplan.replan_IT.focus();
					return false;
				}								
			}	
			
			if (trim(frmDefectProductReplan.replan_ST.value).length>0){
				if (parseFloat(frmDefectProductReplan.replan_ST.value)!=frmDefectProductReplan.replan_ST.value
					|| parseFloat(frmDefectProductReplan.replan_ST.value)<0){
					frmDefectProductReplan.replan_ST.value="";
					alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
					frmDefectProductReplan.replan_ST.focus();
					return false;
				}								
			}	
			
			if (trim(frmDefectProductReplan.replan_OT.value).length>0){
				if (parseFloat(frmDefectProductReplan.replan_OT.value)!=frmDefectProductReplan.replan_OT.value
					|| parseFloat(frmDefectProductReplan.replan_OT.value)<0){
					frmDefectProductReplan.replan_OT.value="";
					alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
					frmDefectProductReplan.replan_OT.focus();
					return false;
				}								
			}				
		}else{
			for (i=0;i<frmDefectProductReplan.replan_RR.length;i++){
				if (trim(frmDefectProductReplan.replan_RR[i].value).length>0){
					if (parseFloat(frmDefectProductReplan.replan_RR[i].value)!=frmDefectProductReplan.replan_RR[i].value 
					    || parseFloat(frmDefectProductReplan.replan_RR[i].value)<0){
						frmDefectProductReplan.replan_RR[i].value="";
						alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
						frmDefectProductReplan.replan_RR[i].focus();
						return false;
					}								
				}	
			
				if (trim(frmDefectProductReplan.replan_DR[i].value).length>0){
					if (parseFloat(frmDefectProductReplan.replan_DR[i].value)!=frmDefectProductReplan.replan_DR[i].value
						|| parseFloat(frmDefectProductReplan.replan_DR[i].value)<0){
						frmDefectProductReplan.replan_DR[i].value="";
						alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
						frmDefectProductReplan.replan_DR[i].focus();
						return false;
					}								
				}	
			
				if (trim(frmDefectProductReplan.replan_CR[i].value).length>0){
					if (parseFloat(frmDefectProductReplan.replan_CR[i].value)!=frmDefectProductReplan.replan_CR[i].value
						|| parseFloat(frmDefectProductReplan.replan_CR[i].value)<0){
						frmDefectProductReplan.replan_CR[i].value="";
						alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
						frmDefectProductReplan.replan_CR[i].focus();
						return false;
					}								
				}	
			
				if (trim(frmDefectProductReplan.replan_UT[i].value).length>0){
					if (parseFloat(frmDefectProductReplan.replan_UT[i].value)!=frmDefectProductReplan.replan_UT[i].value
						|| parseFloat(frmDefectProductReplan.replan_UT[i].value)<0){
						frmDefectProductReplan.replan_UT[i].value="";
						alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
						frmDefectProductReplan.replan_UT[i].focus();
						return false;
					}								
				}	
			
				if (trim(frmDefectProductReplan.replan_IT[i].value).length>0){
					if (parseFloat(frmDefectProductReplan.replan_IT[i].value)!=frmDefectProductReplan.replan_IT[i].value
						|| parseFloat(frmDefectProductReplan.replan_IT[i].value)<0){
						frmDefectProductReplan.replan_IT[i].value="";
						alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
						frmDefectProductReplan.replan_IT[i].focus();
						return false;
					}								
				}	
			
				if (trim(frmDefectProductReplan.replan_ST[i].value).length>0){
					if (parseFloat(frmDefectProductReplan.replan_ST[i].value)!=frmDefectProductReplan.replan_ST[i].value
						|| parseFloat(frmDefectProductReplan.replan_ST[i].value)<0){
						frmDefectProductReplan.replan_ST[i].value="";
						alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
						frmDefectProductReplan.replan_ST[i].focus();
						return false;
					}								
				}	
			
				if (trim(frmDefectProductReplan.replan_OT[i].value).length>0){
					if (parseFloat(frmDefectProductReplan.replan_OT[i].value)!=frmDefectProductReplan.replan_OT[i].value
						|| parseFloat(frmDefectProductReplan.replan_OT[i].value)<0){
						frmDefectProductReplan.replan_OT[i].value="";
						alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
						frmDefectProductReplan.replan_OT[i].focus();
						return false;
					}								
				}	
			}			
		}
		return true;	
	}
	
function checkSubmit(){
	if (validate()){				
		frmDefectProductReplan.submit();
	}

}	
</SCRIPT>
<%}%>
</HTML>


