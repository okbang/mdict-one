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
	margin-bottom:0in;
	margin-left:0in;
	margin-bottom:.0001pt;
	mso-pagination:widow-orphan;
	font-size:10.0pt;
	mso-bidi-font-size:12.0pt;
	font-family:Tahoma;
	mso-fareast-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";}
p.MsoHeader, li.MsoHeader, div.MsoHeader
	{margin-top:6.0pt;
	margin-right:0in;
	margin-bottom:0in;
	margin-left:0in;
	margin-bottom:.0001pt;
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
	margin-bottom:0in;
	margin-left:0in;
	margin-bottom:.0001pt;
	mso-pagination:widow-orphan;
	tab-stops:right 6.0in;
	border:none;
	mso-border-top-alt:solid windowtext .5pt;
	padding:0in;
	mso-padding-alt:1.0pt 0in 0in 0in;
	font-size:10.0pt;
	mso-bidi-font-size:12.0pt;
	font-family:Tahoma;
	mso-fareast-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";}
p.HeaderTitle, li.HeaderTitle, div.HeaderTitle
	{mso-style-name:"Header Title";
	margin-top:6.0pt;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:0in;
	text-align:center;
	mso-pagination:widow-orphan;
	font-size:11.0pt;
	mso-bidi-font-size:12.0pt;
	font-family:"Arial Black";
	mso-fareast-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";
	letter-spacing:3.0pt;
	font-weight:bold;
	mso-bidi-font-weight:normal;}
p.SectionTitle, li.SectionTitle, div.SectionTitle
	{mso-style-name:"Section Title";
	margin-top:.25in;
	margin-right:0in;
	margin-bottom:6.0pt;
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
	margin:.3in 1.0in .7in 1.25in;
	mso-header-margin:.3in;
	mso-footer-margin:.3in;
	mso-footer:url("nc_header.htm") f1;
	mso-paper-source:0;}
div.Section1
	{page:Section1;}
</style>
</head>

<body lang=EN-US style='tab-interval:.5in'>

<div class=Section1>

<p class=HeaderTitle><img width=192 height=84 src="inc/nc_image002.gif"></p>

<p class=HeaderTitle>REQUEST FOR CORRECTIVE/PREVENTIVE ACTION</p>

<p class=MsoNormal align=center style='text-align:center'><b>No:&nbsp;<%=modelNC.getCode()%></b></p>

<table border=0 cellspacing=0 cellpadding=0 style='border-collapse:collapse;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=240 valign=top style='width:2.0in;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Unit/department:&nbsp;<%=modelNC.getGroupName()%></p>
  </td>
  <td width=239 colspan=4 valign=top style='width:143.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Receiver:&nbsp;<%=modelNC.getAssignee() != null ? modelNC.getAssignee() : ""%></p>
  </td>
  <td width=90 colspan=2 valign=top style='width:.75in;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Type:</p>
  </td>
  <td width=60 valign=top style='width:.5in;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>þ</span></span>
  NC</p>
  </td>
  <td width=99 valign=top style='width:59.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  OB</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=240 valign=top style='width:2.0in;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Level of non-conformity:</p>
  </td>
  <td width=122 colspan=2 valign=top style='width:73.2pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Company</p>
  </td>
  <td width=117 colspan=2 valign=top style='width:70.2pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Branch</p>
  </td>
  <td width=150 colspan=3 valign=top style='width:1.25in;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Department</p>
  </td>
  <td width=99 valign=top style='width:59.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>þ</span></span>
  Project</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle><span style='font-family:Symbol;mso-ascii-font-family:
  Verdana;mso-hansi-font-family:Verdana;mso-char-type:symbol;mso-symbol-font-family:
  Symbol'><span style='mso-char-type:symbol;mso-symbol-font-family:Symbol'>¨</span></span></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle>Request for corrective/preventive action</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Based on</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=364 colspan=4 valign=top style='width:218.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Internal Audit:</p>
  </td>
  <td width=364 colspan=5 valign=top style='width:218.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Customer's complaint:</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=364 colspan=4 valign=top style='width:218.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Review and analysis:</p>
  </td>
  <td width=364 colspan=5 valign=top style='width:218.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Improvement requirement:</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=364 colspan=4 valign=top style='width:218.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Other:</p>
  </td>
  <td width=364 colspan=5 valign=top style='width:218.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Reference:</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Non-conformity description:&nbsp;<%=modelNC.getDescription() != null ? modelNC.getDescription() : ""%></p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Requested by:&nbsp;<%=modelNC.getCreator()%></p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Date:&nbsp;<%=modelNC.getCreateDateString()%></p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle><span style='font-family:Symbol;mso-ascii-font-family:
  Verdana;mso-hansi-font-family:Verdana;mso-char-type:symbol;mso-symbol-font-family:
  Symbol'><span style='mso-char-type:symbol;mso-symbol-font-family:Symbol'>¨</span></span></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle>Non-conformity causal identification and impact
  analysis</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Cause:&nbsp;<%=modelNC.getCause() != null ? modelNC.getCause() : ""%></p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Impact:&nbsp;<%=modelNC.getImpact() != null ? modelNC.getImpact() : ""%></p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=364 colspan=4 valign=top style='width:218.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Identified by:&nbsp;<%=modelNC.getReviewer() != null ? modelNC.getReviewer() : ""%></p>
  </td>
  <td width=364 colspan=5 valign=top style='width:218.4pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Date:&nbsp;<%=modelNC.getReviewDate() != null ? modelNC.getReviewDateString() : ""%></p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle><span style='font-family:Symbol;mso-ascii-font-family:
  Verdana;mso-hansi-font-family:Verdana;mso-char-type:symbol;mso-symbol-font-family:
  Symbol'><span style='mso-char-type:symbol;mso-symbol-font-family:Symbol'>¨</span></span></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle>Corrective/preventive action</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Action description and responsible person:&nbsp;<%=modelNC.getCPAction() != null ? modelNC.getCPAction() : ""%></p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Deadline:&nbsp;<%=modelNC.getDeadLine() != null ? modelNC.getDeadLineString() : ""%></p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=243 colspan=2 valign=top style='width:145.6pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Suggested by:</p>
  </td>
  <td width=243 colspan=4 valign=top style='width:145.6pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Reviewed by:</p>
  </td>
  <td width=243 colspan=3 valign=top style='width:145.6pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Approved by:</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Suggested date:</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle><span style='font-family:Symbol;mso-ascii-font-family:
  Verdana;mso-hansi-font-family:Verdana;mso-char-type:symbol;mso-symbol-font-family:
  Symbol'><span style='mso-char-type:symbol;mso-symbol-font-family:Symbol'>¨</span></span></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=SectionTitle>Follow-up action</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Satisfactory</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><span style='font-family:Wingdings;mso-ascii-font-family:
  Tahoma;mso-hansi-font-family:Tahoma;mso-char-type:symbol;mso-symbol-font-family:
  Wingdings'><span style='mso-char-type:symbol;mso-symbol-font-family:Wingdings'>o</span></span>
  Unsatisfactory, the number of new request for corrective/preventive action:</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=728 colspan=9 valign=top style='width:436.8pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Note:</p>
  </td>
 </tr>
 <tr>
  <td width=40 valign=top style='width:24.0pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td width=243 colspan=2 valign=top style='width:145.6pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Close date:</p>
  </td>
  <td width=243 colspan=4 valign=top style='width:145.6pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Closed by:</p>
  </td>
  <td width=243 colspan=3 valign=top style='width:145.6pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=MsoNormal>Approved by:</p>
  </td>
 </tr>
 <![if !supportMisalignedColumns]>
 <tr height=0>
  <td width=40 style='border:none'></td>
  <td width=240 style='border:none'></td>
  <td width=3 style='border:none'></td>
  <td width=119 style='border:none'></td>
  <td width=2 style='border:none'></td>
  <td width=115 style='border:none'></td>
  <td width=6 style='border:none'></td>
  <td width=84 style='border:none'></td>
  <td width=60 style='border:none'></td>
  <td width=99 style='border:none'></td>
 </tr>
 <![endif]>
</table>

<p class=MsoNormal><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>

</div>

</body>

</html>
