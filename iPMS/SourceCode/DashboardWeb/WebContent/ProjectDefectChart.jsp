<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>Project Defect Chart</TITLE>
<LINK rel="stylesheet" type="text/css" href="styles/StyleSheet.css">
</HEAD>
<BODY bgcolor="#C0C0C0"><%
    String[] arrDefectLabel = {"Cosmetic", "Cosmetic2",
            "Medium", "Medium2",
            "Serious", "Serious2",
            "Fatal", "Fatal2",
            "Total", "Total2"};
    String[] arrDefectValue = request.getParameterValues("dV");
%>
<P align="left"><APPLET codebase="." code="ProjectDefectChartApplet.class" width="600" height="300" align="left">
    <PARAM name="batch_nums" value="2">
    <PARAM name="c1_note_label" value='Closed'>
    <PARAM name="c2_note_label" value='Pending'><%
    int i;
    String columnLabel = "";
    String columnValue = "";
    for (i = 0; i < arrDefectLabel.length; i++) {
        columnLabel = "c" +  Integer.toString(i + 1).trim() + "_label";
        columnValue = "c" +  Integer.toString(i + 1).trim();
%>
    <PARAM name="<%=columnLabel%>" value='<%=arrDefectLabel[i]%>'>
    <PARAM name="<%=columnValue%>" value='<%=arrDefectValue[i]%>'><%
    }
%>
    <PARAM name="columns" value="<%=i%>">
</APPLET></P>
</BODY>
</HTML>