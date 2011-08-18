<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*,com.fms1.html.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>delilveryList.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	Vector customerList = (Vector)session.getAttribute("listCustomer");
	String check = (String)session.getAttribute("checkDelivery");
	Vector deliveryList = new Vector();
	String left="" ; 
	String right="" ; 
	String dateSearch="";
	String dateSearch1="";
	if (!check.equals("0")) {
		deliveryList = (Vector)session.getAttribute("deliveryList");
		left = (String)session.getAttribute("leftCustomer");
		right = (String)session.getAttribute("rightCustomer");
		session.removeAttribute("leftCustomer");
		session.removeAttribute("rightCustomer");
		dateSearch = (String)session.getAttribute("dateCustomerList");
		dateSearch1 = (String)session.getAttribute("dateCustomerList1");
		session.removeAttribute("dateCustomerList");
		session.removeAttribute("dateCustomerList1");
	}
%>
<BODY onLoad="loadOrgMenu('Home','');" class="BD">

<FORM method="post" name="frm">
<input type=hidden name="leftCustomer" value="">
<input type=hidden name="rightCustomer" value="">
<br>
		 Deliverable From Date :  
		
			<%
				if (check.equals("0")) {
			%>
				<input type="text" name="dateSearch" value="">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showStartDate()'>
				Deliverable To Date :
				<input type="text" name="dateSearch1" value="">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showStartDate1()'>
				
			<% 	}
				else {
			%>
				<input type="text" name="dateSearch" value="<%=dateSearch%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showStartDate()'>
				Deliverable To Date :
				<input type="text" name="dateSearch1" value="<%=dateSearch1%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showStartDate1()'>

			<%	}
			%>
<table class="Table" width="25%" cellspacing="1" style="margin-left: 150;display: inline">				
<br>
	<tr class="CellBGR3">
		<td colspan="3"> Customer List </td>
	</tr>
	<tr class="CellBGR3">
		<td > 
			<select multiple="multiple" style="width: 130 ; height: 200" id="left" name="left" ondblclick="moveSelectedOptions(this,document.getElementById('right'),true)">
			<%
			if (left.equals("")) {
				if (check.equals("0") ) 
				for (int i=0;i<customerList.size();i++){
					CustomerInfo temp = (CustomerInfo)customerList.get(i);
			%>
				<OPTION value="<%=temp.cusID%>" > <%=ConvertString.toHtml(temp.standardName)%>  </OPTION>
			<%  } }
				else {
					StringTokenizer st = new StringTokenizer(left,",");
					while(st.hasMoreTokens()){
						String tempCusName = st.nextToken();
			%>
				<OPTION value="<%=ConvertString.toHtml(tempCusName)%>" > <%=ConvertString.toHtml(tempCusName)%>  </OPTION>
			<%
					}
				}
			%>
			</select>
		</td>
		<td class="CellBGRnews">
			<BUTTON style="width: 40" onclick="moveSelectedOptions(document.getElementById('left'),document.getElementById('right'),true)">&gt;</BUTTON><br/>
			<BUTTON style="width: 40" onclick="moveAllOptions(document.getElementById('left'),document.getElementById('right'),true)">&gt;&gt;</BUTTON><br/>
			<BUTTON style="width: 40" onclick="moveSelectedOptions(document.getElementById('right'),document.getElementById('left'),true)">&lt;</BUTTON><br/>
			<BUTTON style="width: 40" onclick="moveAllOptions(document.getElementById('right'),document.getElementById('left'),true)" >&lt;&lt;</BUTTON><br/>
		</td>
		<td class="CellBGRnews"> 
			<select multiple="multiple" style="width: 130 ; height: 200" id="right" name="right" ondblclick="moveSelectedOptions(this,document.getElementById('left'),true)" >
				<%
					if (!right.equals("")){
						StringTokenizer st = new StringTokenizer(right,",");
						while(st.hasMoreTokens()){
							String tempCusName = st.nextToken();
				%>
					<OPTION value="<%=ConvertString.toHtml(tempCusName)%>" > <%=ConvertString.toHtml(tempCusName)%>  </OPTION>
				<%
						}
					}
				%>
			</select>
		</td>
</table>
<div style="margin-left: 500;margin-top: -24">
<input type="button" class="BUTTON" name="btnUpdate" value="Filter" onclick="doSearch()">
</div>
</FORM>

<p></p>
<%
	if(1==1){
	String className="";
	if(!check.equals("0")){
%>
<TABLE width="95%">
	<TR>
		<TD align="right" valign="bottom"><A href="exportDeliverable.jsp" target="about:blank"> Export Deliverable List </A></TD> 
	</TR>
</TABLE>
<%
	}
%>
<TABLE cellspacing="1" class="Table" width="100%">
	<CAPTION class="TableCaption"><A name="teamvaluation"> Delivery List
	</A></CAPTION>
	<TBODY>
		<TR>
			<TD class="ColumnLabel" width="24" align="center">#</TD>
			<TD class="ColumnLabel" align="center">Project Code</TD>
			<TD class="ColumnLabel" align="center">Customer</TD>
			<TD class="ColumnLabel" align="center">Deliverable</TD>
			<TD class="ColumnLabel" align="center">First Committed Date</TD>
			<TD class="ColumnLabel" align="center">Re Committed Date</TD>
			<TD class="ColumnLabel" align="center">Actual Date</TD>
			<TD class="ColumnLabel" align="center">Status</TD>
			<TD class="ColumnLabel" align="center">Note</TD>
		</TR>
		<%
			for (int i=0;i<deliveryList.size();i++) {
				Delivery temp = (Delivery)deliveryList.get(i);
				if (i%2==0) className = "CellBGRnews";
				else
					className = "CellBGR3";
		%>
		<TR class="<%=className%>">
			<TD><%=i+1%></TD>
			<TD><%=temp.getProjectCode()==null?"":temp.getProjectCode()%></TD>
			<TD><%=temp.getCustomerName()==null?"":temp.getCustomerName()%></TD>
			<TD><%=temp.getDeliverable()==null?"":temp.getDeliverable()%></TD>
			<TD><%=temp.getFirstCommitDate()==null?"":temp.getFirstCommitDate()%></TD>
			<TD><%=temp.getReCommitDate()==null?"":temp.getReCommitDate()%></TD>
			<TD><%=temp.getActualDate()==null?"":temp.getActualDate()%></TD>
			<TD><%=temp.getStatus()==null?"":temp.getStatus()%></TD>
			<TD width="300" ><%=temp.getNote()==null?"":ConvertString.breakString(temp.getNote(),20)%></TD>
		</TR>
		<%	}
		%>
		
	</TBODY>
</TABLE>
<%
	}
%>
<br><br>
</BODY>
</HTML>
<SCRIPT language="javascript">
	function doSearch(){
		if(!validate()){
			if (!document.getElementsByName("dateSearch")[0].value=='') {
				alert('Invalid date format');
				document.getElementsByName("dateSearch")[0].focus();
				return;
			}
		}
		if(!validate1()){
			if (!document.getElementsByName("dateSearch1")[0].value=='') {
				alert('Invalid date format');
				document.getElementsByName("dateSearch1")[0].focus();
				return;
			}
		}
		if (document.getElementsByName("right")[0].options.length == 0 ) {
			alert('You must add customer to search');
			return;
		}
		var lefthidden = '' ;
		var righthidden = '' ;
		
		var leftCustomer = document.getElementsByName("left")[0];
		var rightCustomer = document.getElementsByName("right")[0];
		for (var i=0;i<leftCustomer.length;i++) {
			if (i==0) lefthidden = leftCustomer.options[i].text;
			else {
				lefthidden = lefthidden + ',' + leftCustomer.options[i].text;
			}
		}
		for (var i=0;i<rightCustomer.length;i++) {
			if (i==0) righthidden = rightCustomer.options[i].text;
			else {
				righthidden = righthidden + ',' + rightCustomer.options[i].text;
			}
		}
		
		document.getElementsByName("leftCustomer")[0].value = lefthidden ;
		document.getElementsByName("rightCustomer")[0].value = righthidden;
		
		var valueDate = document.getElementsByName("dateSearch")[0].value;
		frm.action="Fms1Servlet?reqType=<%=Constants.DELIVERY_LIST_SEARCH%>&value="+valueDate;
		frm.submit();
		
	}

	function selectUnselectMatchingOptions(obj,regex,which,only){if(window.RegExp){if(which == "select"){var selected1=true;var selected2=false;}else if(which == "unselect"){var selected1=false;var selected2=true;}else{return;}var re = new RegExp(regex);for(var i=0;i<obj.options.length;i++){if(re.test(obj.options[i].text)){obj.options[i].selected = selected1;}else{if(only == true){obj.options[i].selected = selected2;}}}}}
	function selectMatchingOptions(obj,regex){selectUnselectMatchingOptions(obj,regex,"select",false);}
	function selectOnlyMatchingOptions(obj,regex){selectUnselectMatchingOptions(obj,regex,"select",true);}
	function unSelectMatchingOptions(obj,regex){selectUnselectMatchingOptions(obj,regex,"unselect",false);}
	function sortSelect(obj){var o = new Array();if(obj.options==null){return;}for(var i=0;i<obj.options.length;i++){o[o.length] = new Option( obj.options[i].text, obj.options[i].value, obj.options[i].defaultSelected, obj.options[i].selected) ;}if(o.length==0){return;}o = o.sort(
	function(a,b){if((a.text+"") <(b.text+"")){return -1;}if((a.text+"") >(b.text+"")){return 1;}return 0;});for(var i=0;i<o.length;i++){obj.options[i] = new Option(o[i].text, o[i].value, o[i].defaultSelected, o[i].selected);}}
	function selectAllOptions(obj){for(var i=0;i<obj.options.length;i++){obj.options[i].selected = true;}}
	function moveSelectedOptions(from,to){if(arguments.length>3){var regex = arguments[3];if(regex != ""){unSelectMatchingOptions(from,regex);}}for(var i=0;i<from.options.length;i++){var o = from.options[i];if(o.selected){to.options[to.options.length] = new Option( o.text, o.value, false, false);}}for(var i=(from.options.length-1);i>=0;i--){var o = from.options[i];if(o.selected){from.options[i] = null;}}if((arguments.length<3) ||(arguments[2]==true)){sortSelect(from);sortSelect(to);}from.selectedIndex = -1;to.selectedIndex = -1;}
	function copySelectedOptions(from,to){var options = new Object();for(var i=0;i<to.options.length;i++){options[to.options[i].text] = true;}for(var i=0;i<from.options.length;i++){var o = from.options[i];if(o.selected){if(options[o.text] == null || options[o.text] == "undefined"){to.options[to.options.length] = new Option( o.text, o.value, false, false);}}}if((arguments.length<3) ||(arguments[2]==true)){sortSelect(to);}from.selectedIndex = -1;to.selectedIndex = -1;}
	function moveAllOptions(from,to){selectAllOptions(from);if(arguments.length==2){moveSelectedOptions(from,to);}else if(arguments.length==3){moveSelectedOptions(from,to,arguments[2]);}else if(arguments.length==4){moveSelectedOptions(from,to,arguments[2],arguments[3]);}}
	function copyAllOptions(from,to){selectAllOptions(from);if(arguments.length==2){copySelectedOptions(from,to);}else if(arguments.length==3){copySelectedOptions(from,to,arguments[2]);}}
	function swapOptions(obj,i,j){var o = obj.options;var i_selected = o[i].selected;var j_selected = o[j].selected;var temp = new Option(o[i].text, o[i].value, o[i].defaultSelected, o[i].selected);var temp2= new Option(o[j].text, o[j].value, o[j].defaultSelected, o[j].selected);o[i] = temp2;o[j] = temp;o[i].selected = j_selected;o[j].selected = i_selected;}
	function moveOptionUp(obj){var selectedCount=0;for(i=0;i<obj.options.length;i++){if(obj.options[i].selected){selectedCount++;}}if(selectedCount!=1){return;}var i = obj.selectedIndex;if(i == 0){return;}swapOptions(obj,i,i-1);obj.options[i-1].selected = true;}
	function moveOptionDown(obj){var selectedCount=0;for(i=0;i<obj.options.length;i++){if(obj.options[i].selected){selectedCount++;}}if(selectedCount != 1){return;}var i = obj.selectedIndex;if(i ==(obj.options.length-1)){return;}swapOptions(obj,i,i+1);obj.options[i+1].selected = true;}
	function removeSelectedOptions(from){for(var i=(from.options.length-1);i>=0;i--){var o=from.options[i];if(o.selected){from.options[i] = null;}}from.selectedIndex = -1;}
	
	function showStartDate(){
		showCalendar(document.getElementById("dateSearch"), document.getElementById("dateSearch"), "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showStartDate1(){
		showCalendar(document.getElementById("dateSearch1"), document.getElementById("dateSearch1"), "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function validate(){
		var valueDate = document.getElementsByName("dateSearch")[0].value;
		if (valueDate=='')return false;
		return(isDate(valueDate));
	}
	
	function validate1(){
		var valueDate1 = document.getElementsByName("dateSearch1")[0].value;
		if (valueDate1=='')return false;
		return(isDate(valueDate1));
	}
	
	
</SCRIPT>
