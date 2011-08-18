<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<TITLE>subcontractAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%

	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");	
	int caller = Integer.parseInt((String)session.getAttribute("caller"));
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtDeliverable.focus();">
<P class="TITLE"><%=(caller==Constants.SCHEDULE_CALLER)? languageChoose.getMessage("fi.jsp.subcontractAdd.ScheduleSupplier_N_SubContractor"): languageChoose.getMessage("fi.jsp.subcontractAdd.ProjectplanSupplier_N_SubContractor") %></p>
<BR>
<FORM method="post" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_SUBCONTRACT%>#Subcontracts" name="frm">
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.subcontractAdd.AddnewSupplier_N_SubContractor")%> </CAPTION>
    <TBODY>    
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.subcontractAdd.WorkProduct")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtDeliverable"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" > <%=languageChoose.getMessage("fi.jsp.subcontractAdd.SupplierSubContractorName")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="sName"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" > 
            	<%=languageChoose.getMessage("fi.jsp.subcontractAdd.ContactPerson")%><BR>
				<%=languageChoose.getMessage("fi.jsp.subcontractAdd.ContactPersonNote")%>
            </TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="sContactP"></TEXTAREA></TD>
        </TR>
        		
        <TR>
            <TD class="ColumnLabel" > <%=languageChoose.getMessage("fi.jsp.subcontractAdd.ExpectedDeliveryDate")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtPDeliveryD">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPDeliveryD()'>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" > <%=languageChoose.getMessage("fi.jsp.subcontractAdd.Actualdeliverydate")%> </TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtADeliveryD">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showADeliveryD()'>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" > <%=languageChoose.getMessage("fi.jsp.subcontractAdd.RefToContract")%> </TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="refToContract"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" ><%=languageChoose.getMessage("fi.jsp.subcontractAdd.Note")%></TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtNote"></TEXTAREA></TD>
        </TR>
    </TBODY> 
</TABLE>
<INPUT  type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT  type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">

<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.subcontractAdd.OK")%>" class="BUTTON" onclick="add();">
<INPUT type="button" name="btnBack" value=" <%=languageChoose.getMessage("fi.jsp.subcontractAdd.Cancel")%> " class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_SUBCONTRACT_GET_LIST:Constants.PL_STRUCTURE_GET_PAGE%>);">
</FORM>
<SCRIPT language="javascript">
	function showPDeliveryD(){
		if(frm.txtPDeliveryD.value == null || frm.txtPDeliveryD.value ==""){
			frm.txtPDeliveryD.value = "01-01-08";
		}
		showCalendar(frm.txtPDeliveryD, frm.txtPDeliveryD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showADeliveryD(){
		if(frm.txtADeliveryD.value == null || frm.txtADeliveryD.value ==""){
			frm.txtADeliveryD.value = "01-01-08";
		}
		showCalendar(frm.txtADeliveryD, frm.txtADeliveryD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
  function add()
  {	  
  
  	  if(trim(frm.sName.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.subcontractUpdate.Thisfieldismandatory")%>"); 
  	  	frm.sName.focus();  	
  	  	return;  		
  	  		
  	  }
  	  if(frm.sName.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.subcontractUpdate.Lengthofthisfield")%>");
  			frm.sName.focus();
  			return;
  	  }
  	  
  	  if(frm.sContactP.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.subcontractUpdate.Lengthofthisfield")%>");
  			frm.sContactP.focus();
  			return;
  	  }
  
  
  	  if(frm.txtDeliverable.value==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.subcontractAdd.Thisfieldismandatory")%>"); 
  	  	frm.txtDeliverable.focus();  	
  	  	return;  		
  	  		
  	  }
  	  if(frm.txtDeliverable.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.subcontractAdd.Lengthofthisfield")%>");
  			frm.txtDeliverable.focus();
  			return;
  	  }	 	
  	  
  	  
  	   if(frm.txtPDeliveryD.value==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.subcontractAdd.Thisfieldismandatory")%>");
  	  	frm.txtPDeliveryD.focus();
  	  	return;  		
  	 }	  
  
  
  	  if(!(isDate(frm.txtPDeliveryD.value))){  	  
  		  window.alert("<%=languageChoose.getMessage("fi.jsp.subcontractAdd.Invaliddateformat")%>");
  		  frm.txtPDeliveryD.focus();
  		  return;
  	  }
  	  if(compareDate(frm.txtPDeliveryD.value,frm.txtPrjStartD.value)==1){
			 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.subcontractAdd.Planned__delivery__date__must__be__after__planned__start__date__of__project__~PARAM1_PJSDATE~")%>', frm.txtPrjStartD.value)));  
  		 	 frm.txtPDeliveryD.focus();
  		 	 return;
  	  }
  	  
  	  if(compareDate(frm.txtPDeliveryD.value,frm.txtPrjEndD.value)==-1){
			 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.subcontractAdd.Planned__delivery__date__must__be__before__planned__end__date__of__project__~PARAM1_PJEDATE~")%>',frm.txtPrjEndD.value)));
  		 	 frm.txtPDeliveryD.focus();
  		 	 return;
  	  }
  	  
  	  if(frm.txtADeliveryD.value!=""){
  	  		if(!(isDate(frm.txtADeliveryD.value))){  	  
  		  		window.alert("<%=languageChoose.getMessage("fi.jsp.subcontractAdd.Invaliddateformat")%>");
  		  		frm.txtADeliveryD.focus();
  		  		return;
  		  	}  		  	
  	  }
  	  
  	  if(frm.refToContract.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.subcontractUpdate.Lengthofthisfield")%>");
  			frm.refToContract.focus();
  			return;
  	  }
  	  
  	  if(frm.txtNote.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.subcontractAdd.Lengthofthisfield")%>");
  			frm.txtNote.focus();
  			return;
  	  }	  	  
  	  
  	  frm.submit();	    	    	
  }
 </SCRIPT> 
</BODY>
</HTML>
