<%@ page contentType="application/msword;charset=UTF-8" %><%@ page language="java" 
         import="javax.servlet.*,
                 fpt.ncms.bean.*,
                 fpt.ncms.constant.NCMS,
                 fpt.ncms.util.StringUtil.*,
                 fpt.ncms.util.DateUtil.DateUtil,
                 fpt.ncms.model.NCModel"%><%@
    page isThreadSafe="true" errorPage="error.jsp" %><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCAddBean beanNCAdd = (NCAddBean)session.getAttribute("beanNCAdd");
    String strCurrentGroup = beanNCAdd.getNCModel().getGroupName();
    String strCurrentProjectID = beanNCAdd.getNCModel().getProjectID();
    NCModel modelNC = beanNCAdd.getNCModel();
%>
<html xmlns:v="urn:schemas-microsoft-com:vml"
xmlns:o="urn:schemas-microsoft-com:office:office"
xmlns:w="urn:schemas-microsoft-com:office:word"
xmlns="http://www.w3.org/TR/REC-html40">

<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title> </title>
<style>
 /* Font Definitions */
@font-face
	{font-family:Wingdings;
	panose-1:5 0 0 0 0 0 0 0 0 0;
	mso-font-charset:2;
	mso-generic-font-family:auto;
	mso-font-pitch:variable;
	mso-font-signature:0 268435456 0 0 -2147483648 0;}
@font-face
	{font-family:Tahoma;
	panose-1:2 11 6 4 3 5 4 4 2 4;
	mso-font-charset:0;
	mso-generic-font-family:swiss;
	mso-font-pitch:variable;
	mso-font-signature:553679495 -2147483648 8 0 66047 0;}
@font-face
	{font-family:"Arial Black";
	panose-1:2 11 10 4 2 1 2 2 2 4;
	mso-font-charset:0;
	mso-generic-font-family:swiss;
	mso-font-pitch:variable;
	mso-font-signature:647 0 0 0 159 0;}
@font-face
	{font-family:Verdana;
	panose-1:2 11 6 4 3 5 4 4 2 4;
	mso-font-charset:0;
	mso-generic-font-family:swiss;
	mso-font-pitch:variable;
	mso-font-signature:536871559 0 0 0 415 0;}
 /* Style Definitions */
p.MsoNormal, li.MsoNormal, div.MsoNormal
	{mso-style-parent:"";
	margin-top:6.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:0in;
	mso-pagination:widow-orphan;
	font-size:10.0pt;
	mso-bidi-font-size:12.0pt;
	font-family:Tahoma;
	mso-fareast-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";}
p.MsoFootnoteText, li.MsoFootnoteText, div.MsoFootnoteText
	{margin-top:6.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:0in;
	mso-pagination:widow-orphan;
	font-size:8.0pt;
	mso-bidi-font-size:10.0pt;
	font-family:Tahoma;
	mso-fareast-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";}
p.MsoHeader, li.MsoHeader, div.MsoHeader
	{margin-top:6.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:0in;
	mso-pagination:widow-orphan;
	tab-stops:center 3.0in right 6.0in;
	font-size:10.0pt;
	mso-bidi-font-size:12.0pt;
	font-family:Tahoma;
	mso-fareast-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";}
p.MsoFooter, li.MsoFooter, div.MsoFooter
	{margin-top:6.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:0in;
	mso-pagination:widow-orphan;
	tab-stops:right 7.1in;
	border:none;
	mso-border-top-alt:solid windowtext .5pt;
	padding:0in;
	mso-padding-alt:1.0pt 0in 0in 0in;
	font-size:10.0pt;
	mso-bidi-font-size:12.0pt;
	font-family:Tahoma;
	mso-fareast-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";}
span.MsoFootnoteReference
	{vertical-align:super;}
p.HeaderTitle, li.HeaderTitle, div.HeaderTitle
	{mso-style-name:"Header Title";
	mso-style-next:Normal;
	margin-top:6.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:0in;
	text-align:center;
	mso-pagination:widow-orphan;
	font-size:12.0pt;
	font-family:"Arial Black";
	mso-fareast-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";
	letter-spacing:3.0pt;
	font-weight:bold;
	mso-bidi-font-weight:normal;}
p.SectionTitle, li.SectionTitle, div.SectionTitle
	{mso-style-name:"Section Title";
	margin-top:9.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:0in;
	mso-pagination:widow-orphan;
	font-size:10.0pt;
	mso-bidi-font-size:12.0pt;
	font-family:Verdana;
	mso-fareast-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";
	color:#6E2500;
	font-weight:bold;
	mso-bidi-font-weight:normal;}
@page Section1
	{size:8.5in 11.0in;
	margin:.5in .7in .5in .7in;
	mso-header-margin:.3in;
	mso-footer-margin:.3in;
	mso-footer:url("./inc/cc_header.htm") f1;
	mso-paper-source:0;}
div.Section1
	{page:Section1;}
</style>
</head>

<body lang=EN-US style='tab-interval:.5in'>

<div class=Section1>

<table border=1 cellspacing=0 cellpadding=0 style='border-collapse:collapse;
 border:none;mso-border-alt:solid windowtext .5pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr>
  <td width=870 colspan=6 valign=top style='width:7.25in;border:solid windowtext .5pt;
  border-bottom:none;padding:0in 5.4pt 0in 5.4pt'>
  <p class=HeaderTitle><img width=158 height=61 src="inc/cc_image002.gif"></p>
  <p class=HeaderTitle>RECORD OF CUSTOMER COMPLAINT SETTLEMENT<a
  style='mso-footnote-id:ftn1' href="#_ftn1" name="_ftnref1" title=""><span
  class=MsoFootnoteReference><span style='mso-special-character:footnote'><![if !supportFootnotes]>[1]<![endif]></span></span></a></p>
  <p class=MsoNormal align=center style='text-align:center'><b>No:&nbsp;<%=modelNC.getCode()%></b></p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Received place:&nbsp;<%=modelNC.getProjectID()%></p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=208 valign=top style='width:124.65pt;border:none;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Settlement level:</p>
  </td>
  <td width=208 valign=top style='width:124.65pt;border:none;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Department</p>
  </td>
  <td width=208 colspan=2 valign=top style='width:124.65pt;border:none;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Branch</p>
  </td>
  <td width=208 valign=top style='width:124.65pt;border:none;border-right:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Company</p>
  </td>
 </tr>
 <tr>
  <td width=870 colspan=6 valign=top style='width:7.25in;border-top:none;
  border-left:solid windowtext .5pt;border-bottom:none;border-right:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle>I. Complaint information</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=465 colspan=3 valign=top style='width:279.0pt;border:none;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>1. Customer:</p>
  </td>
  <td width=366 colspan=2 valign=top style='width:3.05in;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>2. Contact information:</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>3. Complaint content (write down <b>complaint content</b>
  and <b>related records</b>)</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><b><%=modelNC.getDescription() != null ? modelNC.getDescription() : ""%></b></p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=465 colspan=3 valign=top style='width:279.0pt;border:none;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Receiver:</p>
  </td>
  <td width=366 colspan=2 valign=top style='width:3.05in;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Date:</p>
  </td>
 </tr>
 <tr>
  <td width=870 colspan=6 valign=top style='width:7.25in;border-top:none;
  border-left:solid windowtext .5pt;border-bottom:none;border-right:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle>II. Cause and resolution identification</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Cause of complaint (describe <b>cause</b> of the complaint
  and <b>related records</b>)</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><%=modelNC.getCause() != null ? modelNC.getCause() : ""%></p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Suggestion</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><%=modelNC.getCPAction() != null ? modelNC.getCPAction() : ""%></p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=465 colspan=3 valign=top style='width:279.0pt;border:none;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Responsible person:&nbsp;<%=modelNC.getAssignee() != null ? modelNC.getAssignee() : ""%></p>
  </td>
  <td width=366 colspan=2 valign=top style='width:3.05in;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Deadline:&nbsp;<%=modelNC.getDeadLineString() != null ? modelNC.getDeadLineString() : ""%></p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=465 colspan=3 valign=top style='width:279.0pt;border:none;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Complaint settler:</p>
  </td>
  <td width=366 colspan=2 valign=top style='width:3.05in;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Date:</p>
  </td>
 </tr>
 <tr>
  <td width=870 colspan=6 valign=top style='width:7.25in;border-top:none;
  border-left:solid windowtext .5pt;border-bottom:none;border-right:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle>III. Settlement and follow-up</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Action (Describe of <b>action taken</b> and <b>related
  records</b>, if any)</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=465 colspan=3 valign=top style='width:279.0pt;border:none;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Taken by:</p>
  </td>
  <td width=366 colspan=2 valign=top style='width:3.05in;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Completion date:</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Opinion of customer</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=208 valign=top style='width:124.65pt;border:none;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Very good</p>
  </td>
  <td width=208 valign=top style='width:124.65pt;border:none;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  <span style='mso-bidi-font-family:Tahoma'>Good</span></p>
  </td>
  <td width=208 colspan=2 valign=top style='width:124.65pt;border:none;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  <span style='mso-bidi-font-family:Tahoma'>Accept</span></p>
  </td>
  <td width=208 valign=top style='width:124.65pt;border:none;border-right:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  <span style='mso-bidi-font-family:Tahoma'>Unaccepted</span></p>
  </td>
 </tr>
 <tr>
  <td width=870 colspan=6 valign=top style='width:7.25in;border-top:none;
  border-left:solid windowtext .5pt;border-bottom:none;border-right:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle>Note:</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Comment of follow-up person</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border:none;border-left:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=465 colspan=3 valign=top style='width:279.0pt;border:none;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Follow-up person:</p>
  </td>
  <td width=366 colspan=2 valign=top style='width:3.05in;border:none;
  border-right:solid windowtext .5pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Checked date:</p>
  </td>
 </tr>
 <tr>
  <td width=39 valign=top style='width:23.4pt;border-top:none;border-left:solid windowtext .5pt;
  border-bottom:solid windowtext .5pt;border-right:none;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=831 colspan=5 valign=top style='width:498.6pt;border-top:none;
  border-left:none;border-bottom:solid windowtext .5pt;border-right:solid windowtext .5pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
 </tr>
 <![if !supportMisalignedColumns]>
 <tr height=0>
  <td width=39 style='border:none'></td>
  <td width=208 style='border:none'></td>
  <td width=208 style='border:none'></td>
  <td width=50 style='border:none'></td>
  <td width=158 style='border:none'></td>
  <td width=208 style='border:none'></td>
 </tr>
 <![endif]>
</table>

<p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>

</div>

<div style='mso-element:footnote-list'><![if !supportFootnotes]><br clear=all>

<hr align=left size=1 width="33%">

<![endif]>

<div style='mso-element:footnote' id=ftn1>

<p class=MsoFootnoteText><a style='mso-footnote-id:ftn1' href="#_ftnref1"
name="_ftn1" title=""><span class=MsoFootnoteReference><span style='mso-special-character:
footnote'><![if !supportFootnotes]>[1]<![endif]></span></span></a> It is used
in case of no specific form of business field is necessary to be created</p>

</div>

</div>

</body>

</html>
