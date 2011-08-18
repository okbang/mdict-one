<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>Project Requirement Chart</TITLE>
<LINK rel="stylesheet" type="text/css" href="styles/StyleSheet.css">
</HEAD>
<BODY bgcolor="#C0C0C0"><%
    if (request.getParameterValues("rV") != null) {
    }
    else {
%>
<SCRIPT>
    window.close();
</SCRIPT><%
    }
    String[] arrReqLabel = {"Accepted",
            "Deployed",
            "Tested",
            "Coded",
            "Designed",
            "Commited",
            "Total"};
    String[] arrReqValue = request.getParameterValues("rV");
%>
<P align="left"><APPLET codebase="." code="ProjectRequirementChartApplet.class" width="600" height="500" align="center"><%
    int i;
    String columnLabel = "";
    String columnValue = "";

    for (i = 0; i < arrReqLabel.length; i++) {
        columnLabel = "c" +  Integer.toString(i+1).trim() + "_label";
        columnValue = "c" +  Integer.toString(i+1).trim();
%>
    <PARAM name="<%=columnLabel%>" value='<%=arrReqLabel[i]%>'>
    <PARAM name="<%=columnValue%>" value='<%=arrReqValue[i]%>'><%
    }
%>
    <PARAM name="columns" value="<%=i%>">
</APPLET></P>
</BODY>
</HTML>