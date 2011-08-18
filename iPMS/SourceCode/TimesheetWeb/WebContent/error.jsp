<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.*, java.io.PrintWriter" %>
<%@ page isErrorPage="true" %>
<HTML>
<HEAD>
<TITLE>Timesheet Error Page</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META content="no-cache" http-equiv="Pragma">
<LINK rel="StyleSheet" href="styles/tsStyleSheet.css" type="text/css">
<STYLE type="text/css">
BODY {
    BACKGROUND-COLOR: #D6E7EF;
    BORDER-BOTTOM: medium none;
    BORDER-LEFT: medium none;
    BORDER-RIGHT: medium none;
    BORDER-TOP: medium none;
    FONT-FAMILY: Verdana;
    FONT-SIZE: xx-small;
    LEFT: 0pt;
    MARGIN: 0px;
    PADDING-BOTTOM: 0px;
    PADDING-LEFT: 0px;
    PADDING-RIGHT: 0px;
    PADDING-TOP: 0px;
    TOP: 0pt
}

.Content {
    COLOR: black;
    FONT-FAMILY: Verdana;
    FONT-SIZE: xx-small;
    FONT-WEIGHT: normal
}

.GeneralTermBigest {
    BACKGROUND-COLOR: #CC0000;
    HEIGHT: 66px;
    WIDTH: 40px;
    font: normal 40px Verdana, Geneva, Arial, Helvetica, sans-serif;
    color: White;
    Font-Weight: Bold
}

.HistoryTitleRedBig {
    COLOR: #cc0000;
    FONT-FAMILY: Arial, Verdana, Geneva, Helvetica;
    FONT-SIZE: 22px;
    FONT-WEIGHT: bold
}

.NewsTitleRedM {
    COLOR: #cc0000;
    FONT-FAMILY: Arial, Verdana, Geneva, Helvetica;
    FONT-SIZE: 17px;
    FONT-WEIGHT: Bold;
}

.NewsTitleGreyM {
    COLOR: #31659C;
    FONT-FAMILY: Arial, Verdana, Geneva, Helvetica;
    FONT-SIZE: 17px;
    FONT-WEIGHT: Bold;
}

A:link {
    COLOR: #666666;
    FONT-WEIGHT: normal;
    TEXT-DECORATION: none;
    font-size: 11px;
    font-family: Verdana, Arial, Helvetica, sans-serif;
}

A:visited {
    COLOR: #666666;
    FONT-WEIGHT: normal;
    TEXT-DECORATION: none;
    font-size: 11px;
    font-family: Verdana, Arial, Helvetica, sans-serif;
}

A:hover {
    COLOR: #CC0000;
    FONT-WEIGHT: normal;
    TEXT-DECORATION: none;
    font-size: 11px;
    font-family: Verdana, Arial, Helvetica, sans-serif;
}
</STYLE>
</HEAD>
<BODY alink="#007862" leftmargin="0" link="#007862" topmargin="0" vlink="#777777" marginwidth="0" marginheight="0">
<DIV align="left">
<TABLE border="0" width="760" cellspacing="0" cellpadding="0" style="border-collapse: collapse" bordercolor="#111111">
    <TR>
        <TD width="53" height="13" class="GeneralTermBigest" style="height: 66px">
        <P align="right">E
        </TD>
        <TD width="700" class="HistoryTitleRedBig">rror page</TD>
    </TR>
</TABLE>
<TABLE border="0" width="760" cellspacing="0" cellpadding="0" style="border-collapse: collapse" bordercolor="#111111">
    <TR>
        <TD width="52" height="13"></TD>
        <TD width="126" class="NewsTitleRedM">Error occurs:</TD>
        <TD width="562" class="NewsTitleGreyM"><%=exception.toString() %></TD>
    </TR>
</TABLE>
<TABLE border="0" width="95%" cellspacing="0" cellpadding="0" style="border-collapse: collapse" bordercolor="#111111">
    <COLGROUP>
        <COL width="8%">
        <COL width="7%">
        <COL width="85%">
    <TR>
        <TD valign="top" height="51" align="left" class="Content"></TD>
        <TD valign="bottom" height="51" align="left" class="Content"></TD>
        <TD valign="top" height="51" align="left" class="Content">
        <P align="left"><BR><%
    Enumeration e = request.getAttributeNames();
    while (e.hasMoreElements()) {
        String name = (String)e.nextElement();
        Object attribute = request.getAttribute(name);
        if (attribute instanceof Throwable) {
            Throwable error = (Throwable)attribute;
%>
        <SPAN lang="EN-GB"><%
            error.printStackTrace(new PrintWriter(out));
%>
        </SPAN><%
        }
    }
%>
        <BR>&nbsp;
        </TD>
        <TD valign="top" height="51" align="left" class="Content"></TD>
        <TD valign="top" height="51" align="left" class="Content">&nbsp;</TD>
        <TD valign="top" height="51">&nbsp;</TD>
    </TR>
    <TR>
        <TD valign="top" height="51" align="left" class="Content"></TD>
        <TD valign="top" height="51" align="left" class="Content" colspan="4"><A class="Back" href="Javascript:window.history.go(-1);"> &lt;&lt; Back</A></TD>
        <TD valign="top" height="51">&nbsp;</TD>
    </TR>
</TABLE>
</DIV>
</BODY>
</HTML>