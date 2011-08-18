<%@ page import="java.text.SimpleDateFormat, java.util.*" %><%
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
%>
<DIV align="left">
<TABLE border="0" width="100%" cellpadding="0" cellspacing="0">
    <TR>
        <TD><IMG border="0" src="images/prjmanagement1.gif"></TD>
        <TD align="right"><A href="Dashboard.jsp"><IMG border="0" src="images/home.gif"></A></TD>
    </TR>
    <TR bgcolor="#003F5F">
        <TD><IMG border="0" src="images/prjmanagement2.gif" ></TD>
        <TD align="right"><P id="title"><%=strTitle%>&nbsp;&nbsp;</P></TD>
    </TR>
    <TR>
        <TD><IMG border="0" src="images/prjmanagement3.gif" ></TD>
        <TD align="right" valign="top"><%=formatter.format(new Date())%></TD>
    </TR>
</TABLE>
<HR color="#000000" noshade size="1">
</DIV>