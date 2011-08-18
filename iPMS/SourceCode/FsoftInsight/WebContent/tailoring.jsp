<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>tailoring.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = true;
	if(archiveStatus == null || "".equals(archiveStatus) || !"4".equals(archiveStatus))
		isArchive = false;
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
int right =Security.securiPage("Tailoring Deviation",request,response);
Vector tailoringList = (Vector) session.getAttribute("tailoringList");
int tailoringCnt = Integer.parseInt((String)session.getAttribute("tailoringCnt"));
int deviationCnt = Integer.parseInt((String)session.getAttribute("deviationCnt"));
int tailoringPageNumber = Integer.parseInt((String)session.getAttribute("tailoringPageNumber"));
int tailoringPageCnt = Integer.parseInt((String)session.getAttribute("tailoringPageCnt"));
int apply_PPM=Integer.parseInt((String)session.getAttribute("applyPPM")); 
int applyDate=Integer.parseInt((String)session.getAttribute("applyDate")); 
String reason=(String) session.getAttribute("reason"); 
String updateAction=(String)session.getAttribute("updateAction"); 
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.tailoring.TailoringDeviation")%></P>
<TABLE class="HDR">
    <TBODY>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Numberoftailoringdeviation")%></TD>
            <TD>  </TD>
            <TD><%=(tailoringCnt+deviationCnt)%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Numberoftailoring")%></TD>
            <TD>  </TD>
            <TD><%=tailoringCnt%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Numberofdeviation")%></TD>
            <TD>  </TD>
            <TD><%=deviationCnt%></TD>
        </TR>
    </TBODY>
</TABLE>
<br>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.tailoring.TailoringDeviation")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width = "24" align = "center"># </TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Description")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.AppCri")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Reason")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Action")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.tailoring.Type")%></TD>
        </TR>
        <%
for(int i = (tailoringPageNumber-1)*10 ;i < (tailoringPageNumber)*10 ; i++){
	String className;
	if(i < tailoringList.size()){	
 		className=(i%2==0)?"CellBGRnews":"CellBGR3";
 		TailoringDeviationInfo info = (TailoringDeviationInfo) tailoringList.elementAt(i);
%>
<TR class="<%=className%>">
<TD width = "24" align = "center"><%=i+1%></TD>
<TD><A <%if ((right == 3 && !isArchive)&&(info.type==2)){%>href="Fms1Servlet?reqType=<%=Constants.DEVIATION_UPDATE_PRE%>&deviation_ID=<%=info.tailoringID%>"<%}else {%>
<%if ((right == 3 && !isArchive)&&(info.type==1)){%>href="Fms1Servlet?reqType=<%=Constants.TAILORING_UPDATE_PRE%>&pro_tail_ID=<%=info.process_tail_ID%>"<%}}%>><%=((info.modification == null)?"N/A":ConvertString.toHtml(info.modification))%></a></TD>
<TD><%=((info.reason == null)?"N/A":ConvertString.toHtml(info.reason))%></TD>
<TD><%=((info.note == null)?"N/A":ConvertString.toHtml(info.note))%></TD>
<TD>
<%switch(info.action){
	case 1:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Added")%> <%break; 
	case 2:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Modified")%> <%break;
	case 3:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Deleted")%> <%break; 
}%>
</TD>

<TD>
<%switch(info.type){
	case 1:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Tailoring")%> <%break; 
	case 2:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Deviation")%> <%break;	 
}%>
</TD>
</tr>
<%	}
}
%>
<tr>
	<TD align="right" colspan="7" class="TableLeft">
 <%=languageChoose.getMessage("fi.jsp.tailoring.Page")%><%=tailoringPageNumber%>/<%=tailoringPageCnt%>
	<%if(tailoringPageNumber != 1){%> 
	<a href="Fms1Servlet?reqType=<%=Constants.TAILORING_PREV%>"> <%=languageChoose.getMessage("fi.jsp.tailoring.Prev")%> </a>
	<%}if(tailoringPageNumber != tailoringPageCnt){%>
	<a href="Fms1Servlet?reqType=<%=Constants.TAILORING_NEXT%>"> <%=languageChoose.getMessage("fi.jsp.tailoring.Next")%> </a>
	<%}%>
	</TD>
</tr>
</table>
<br>
<%if (right==3 && !isArchive){%>
<form name="frm_plTailoringAddPrep" action="Fms1Servlet" method = "get">
<input type = "hidden" name="reqType1" value="<%=Constants.TAILORING_GET_LIST%>">
<input type="submit" name="add" value="<%=languageChoose.getMessage("fi.jsp.tailoring.Addtailoring")%>" class="BUTTONWIDTH" onClick="OnAddTailoring();" >
<input type="submit" name="add" value="<%=languageChoose.getMessage("fi.jsp.tailoring.Adddeviation")%>" class="BUTTONWIDTH" onClick="OnAddDeviation();">
<INPUT type="button" name="tailRef" value="Tailoring reference" onclick="tailoringRef()" class="BUTTONWIDTH">
<%}
%> 
<input type = "hidden" name="reqType">
        <%if (applyDate==1){
                    String sel1=""; 
                    String sel2=""; 
                    if (apply_PPM==1){ 
                            sel1="checked";                          
                    }else{ 
                            sel2="checked"; 
                    }                        
            %> 
                    <BR> 
                    <BR> 
                    <font color="red"><%=((updateAction==null)? "":updateAction)%></font>            
                    <TABLE class="Table" width="95%"><TR class="CellBGR2"><TD> 
                    <TABLE cellspacing="0" cellpadding="1" class="Table" width="100%"> 
                    <TR class="ColumnLabel"> 
                    <TD colspan="2"><%=languageChoose.getMessage("fi.jsp.tailoring.ApplyPPMfeaturestotheProject")%></TD> 
                    </TR> 
                    <TR class="CellBGR3"> 
                            <TD colspan="2"> 
                                    <input type = "hidden" name="updateAction" value=""> 
                                    <input type="radio" name="applyPPM" value="1" <%=sel1%> <%=(right==3 && !isArchive) ? "": "disabled"%> onclick="hideObj('reason');"><font color="#000080"><%=languageChoose.getMessage("fi.jsp.tailoring.Apply")%></font></input> 
                            </TD>    
                    </TR>    
                    <TR class="CellBGR3"> 
                            <TD valign="top" width="22%">            
                                    <input type="radio" name="applyPPM" value="0" <%=sel2%> <%=(right==3 && !isArchive) ? "": "disabled"%> onclick="showObj('reason');frm_plTailoringAddPrep.reason.focus();"><font color="#000080"><%=languageChoose.getMessage("fi.jsp.tailoring.NotApply")%></font><font color="red">(<%=languageChoose.getMessage("fi.jsp.tailoring.forreason")%>)</font></input><BR><BR>
<%if (right==3&& !isArchive){%>
                                    &nbsp;<input type="button" name="Update" value="<%=languageChoose.getMessage("fi.jsp.tailoring.Update")%>" class="BUTTONWIDTH" onclick="checkUpdate();">
                                    <BR>
<%}
%> 
                            </TD>    
                            <TD> 
                                    <textarea name="reason" rows="4" cols="74" <%=(right==3 && !isArchive) ? "": "disabled"%>><%=(reason==null? "":reason)%></textarea> 
                            </TD>    
                    </TR> 
                    </TABLE> 
                    </TD></TR></TABLE>                               
</form> 
<BR><%}
%> 
</BODY> 
<SCRIPT language="JavaScript"> 

function tailoringRef() {
	frm_plTailoringAddPrep.reqType.value="<%=Constants.TAILORING_REF%>";
 	frm_plTailoringAddPrep.submit();
}

function OnAddTailoring(){
	frm_plTailoringAddPrep.reqType.value=<%=Constants.PROCESS_TAILORING%>;
	frm_plTailoringAddPrep.submit;
}

function OnAddDeviation(){
	frm_plTailoringAddPrep.reqType.value=<%=Constants.DEVIATION_ADD_PREPARE %>;
	frm_plTailoringAddPrep.submit;
}
<%if (applyDate==1){%> 
            if ("1"==<%=apply_PPM%>){ 
            	hideObj('reason'); 
            }else{ 
                showObj('reason'); 
           } 
	function checkUpdate(){ 
    	if (frm_plTailoringAddPrep.applyPPM[1].checked){ 
	        if (trim(frm_plTailoringAddPrep.reason.value)==""){                      
	        	alert ("<%=languageChoose.getMessage("fi.jsp.tailoring.Pleaseinputreason")%>"); 
	            frm_plTailoringAddPrep.reason.value=""; 
	            frm_plTailoringAddPrep.reason.focus(); 
	            return; 
	        }else{ 
	        	if (trim(frm_plTailoringAddPrep.reason.value).length>=200){ 
	            	alert ("<%=languageChoose.getMessage("fi.jsp.tailoring.Numberofcharactersmustbelessthan200")%>"); 
		            frm_plTailoringAddPrep.reason.focus(); 
	    	        return;                          
	            }  
	        }        
		} 
        frm_plTailoringAddPrep.updateAction.value="<%=languageChoose.getMessage("fi.jsp.tailoring.UpdateSuccessfully")%>"; 
        frm_plTailoringAddPrep.reqType.value=<%=Constants.APPLY_PPM_FEATURE%>; 
        frm_plTailoringAddPrep.submit(); 
   } 
      
<%}%> 
</SCRIPT>
</HTML>
