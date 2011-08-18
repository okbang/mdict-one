<%@page import="com.fms1.infoclass.*,com.fms1.tools.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<%
DefectRateInfo defectInfo;
int right=Security.securiPage("Defects ",request,response);

Vector defectInfoVector = (Vector)session.getAttribute("defectRateReplan");

double planValue=Double.NaN;
int countNotReleased=0;
String classCell="";
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

<TITLE>defectRate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.defectRate.ReplanDefectRate")%></P>
<FORM action="Fms1Servlet" name="frmDefectRate" method="POST" onkeydown="ignoreESCkey();">
<INPUT type="hidden" name="reqType" value="<%=Constants.UPDATE_DEFECT_RATE_REPLAN%>">
<TABLE  cellspacing="1" class="Table" width="95%" >
    <COLGROUP>
        <COL width="4%">
        <COL width="28%"> 
        <COL width="7">
        <COL width="7%">
        <COL width="7%">
        <COL width="7%">
        <COL width="7%">
        <COL width="26%">     
    <TR class="ColumnLabel" align="center">
        <TD><B>#</B></TD> 
        <TD><B> <%=languageChoose.getMessage("fi.jsp.defectRate.Product")%> </B></TD>
        <TD><B> <%=languageChoose.getMessage("fi.jsp.defectRate.Norm")%> </B>  (WD /UCP) </TD>          
		<TD><B> <%=languageChoose.getMessage("fi.jsp.defectRate.Plan")%> </B>  (WD /UCP) </TD>         
        <TD><B> <%=languageChoose.getMessage("fi.jsp.defectRate.Replan")%> </B>  (WD /UCP) </TD> 
        <TD><B> <%=languageChoose.getMessage("fi.jsp.defectRate.Actual")%> </B>  (WD /UCP) </TD> 
        <TD><B> <%=languageChoose.getMessage("fi.jsp.defectRate.Deviation")%> </B> (%)</TD> 
        <TD><B> <%=languageChoose.getMessage("fi.jsp.defectRate.Note")%> </B></TD>                                         
    </TR>

<%for (int i = 0; i < defectInfoVector.size(); i++) {
   	defectInfo=(DefectRateInfo)defectInfoVector.elementAt(i);   	
	if (!defectInfo.released){
	   	countNotReleased++;
		classCell="CellBGRnews";
	}else{
		classCell="CellBGRdark";
	}	  	
%>
	<TR class=<%=classCell%>>
        <TD nowrap><%=i+1%></TD>  		
        <TD nowrap><%=defectInfo.moduleName%></TD>     
        <TD nowrap><%=CommonTools.formatDouble(defectInfo.norm)%></TD> 
        <TD nowrap><%=CommonTools.formatDouble(defectInfo.plan)%></TD>
        <TD nowrap>
	        <%if (defectInfo.released){%>
   		        <%=CommonTools.formatDouble(defectInfo.replan)%>
	        <%}else{%>         
  	        	<INPUT type="text" name="Replan" size="7" maxlength="7" value="<%=CommonTools.updateDouble(defectInfo.replan)%>"  onblur="validate();">
		    <%}%>    
	    </TD>	    
        <TD nowrap><%=CommonTools.formatDouble(defectInfo.actual)%></TD>              
        <TD nowrap>
        	<%planValue=Double.isNaN(defectInfo.replan)?defectInfo.plan:defectInfo.replan;
        	if (CommonTools.parseDouble(CommonTools.formatNumber(planValue,true))!=0 && !Double.isNaN(defectInfo.actual)){%>
        		<%=CommonTools.formatDouble((defectInfo.actual-planValue)*100/planValue)%>
        	<%}else{%>
		         N/A 
		    <%}%>
        </TD> 
        <TD nowrap>
	        <%if (defectInfo.released){%>
   		        <%=CommonTools.updateString(defectInfo.note)%>
	        <%}else{%>         
	        <INPUT type="text" name="Note" size="30" maxlength="100" value="<%=CommonTools.updateString(defectInfo.note)%>">
		    <%}%>    
		</TD>  
    </TR>   
<%}%>
</TABLE>
<P></P>
<BR>
<%if (countNotReleased>0){%>
<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.defectRate.UpdatePlan")%>" name="btonUpdate" class="BUTTONWIDTH" onclick="checkSubmit();">
<%}%>
<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.defectRate.Back")%>" class="BUTTON" onclick="doIt(<%=Constants.DETAIL_DEFECT_PLAN%>);">
</FORM>
</BODY>
<%if(right==3){%>

<SCRIPT language="javascript">
	function validate(){									

		if (<%=countNotReleased%>==1){
			if (trim(frmDefectRate.Replan.value).length>0){
				if (parseFloat(frmDefectRate.Replan.value)!=frmDefectRate.Replan.value
					|| parseFloat(frmDefectRate.Replan.value)<0){
					frmDefectRate.Replan.value="";
					alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
					frmDefectRate.Replan.focus();
					return;
				}								
			}
		}else{
			for (i=0;i<frmDefectRate.Replan.length;i++){
				if (trim(frmDefectRate.Replan[i].value).length>0){
					if (parseFloat(frmDefectRate.Replan[i].value)!=frmDefectRate.Replan[i].value
						|| parseFloat(frmDefectRate.Replan[i].value)<0){
						frmDefectRate.Replan[i].value="";
						alert ('<%=languageChoose.getMessage("fi.jsp.defectProductReplan.PleaseInputAPositiveRealNumber")%>');
						frmDefectRate.Replan[i].focus();
						return;
					}								
				}	
			}
		}			
	}
	
	function checkSubmit(){
		<%if (countNotReleased>0){%>
			frmDefectRate.submit();
		<%}%>
	}
</SCRIPT>
<%}%>
</HTML>

