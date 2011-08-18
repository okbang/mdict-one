<%@ page import="java.lang.*,javax.servlet.*,javax.servlet.http.*" %>
<%
    String strAction = request.getParameter("action");
    String strTitle, strSize;
    boolean isImportingMode = false;
    if ("upload".equals(strAction)) {
        strTitle = "Upload Image";
        strSize = "1M";
    }
    else {
        strTitle  = "Import Defect";
        strSize = "200k";
        isImportingMode = true;
    }
%>
<HTML>
<HEAD>
<TITLE><%=strTitle%></TITLE>
<SCRIPT>
function conjunction(ext) {
    if (ext != '') {
        var mdate = new Date();
        document.tablefrm.filename.value = mdate.getTime() + "." + ext;
        window.returnValue = document.tablefrm.filename.value;
<%
	    if (!isImportingMode) {
%>
	        document.tablefrm.action = "upload.jsp?filename2=" + document.tablefrm.filename.value;
<%
	    }
	    else {
	        String strUser = request.getParameter("user");
	        String strProject = request.getParameter("project");
%>
	        document.tablefrm.action = "upload.jsp?filename2=" + document.tablefrm.filename.value + "&action=import&user=<%=strUser%>&project=<%=strProject%>";
<%
	    }
%>
        document.tablefrm.submit();
        self.close();
    }
    else {
        window.returnValue = '';
        self.close();
    }
}
<%
if (!isImportingMode) {
%>
	function checkName(){
	    var name = document.tablefrm.imgFile.value;
	    if (name == "") {
	        alert("Select picture file please!");
	        document.tablefrm.imgFile.focus();
	        return;
	    }
	    else {
	        var ext = name.substring(name.lastIndexOf(".") + 1, name.length);
	        ext = ext.toLowerCase();
	        if (ext != "jpg" && ext != "bmp" && ext != "gif") {
	            alert("Please select an image file(*.gif, *.jpg ,*.bmp)");
	            return;
	        }
	        else {
	            conjunction(ext);
	        }
	    }
	}
<%
}
else {
%>
	function checkName() {
	    var name = document.tablefrm.imgFile.value;
	    if (name == "") {
	        alert("Select MS Excel file!");
	        document.tablefrm.imgFile.focus();
	        return;
	    }
	    else {
	        var ext = name.substring(name.lastIndexOf(".") + 1, name.length);
	        ext = ext.toLowerCase();
	        if (ext != "xls") {
	            alert("Select MS Excel file!");
	            return;
	        }
	        else {
	            conjunction(ext);
	        }
	    }
	}
<%
}
%>
</SCRIPT>
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
</HEAD>
<BODY>
<STYLE>
BODY {
    font-family: Arial;
    font-size: 8;
    font-weight: bold;
}
</STYLE>
<FORM name="tablefrm" enctype="multipart/form-data" method="post">
<TABLE>
    <TR>
        <TD><FONT size="2"><B>File name:</B></FONT></TD>
        <TD><INPUT type="file" name="imgFile"> <INPUT type="hidden" name="filename"> (File size &lt; <%=strSize%>)</TD>
    </TR>
    <TR>
        <TD colspan="2"><BR>
        <CENTER><INPUT type="button" value="Upload" onclick="checkName()"> &nbsp;&nbsp;&nbsp;
        <INPUT type="button" value="Cancel" onclick="conjunction('')"></CENTER>
        </TD>
    </TR>
</TABLE>
</FORM>
</BODY>
</HTML>