<%@ page import="java.util.*,java.io.*,java.lang.*,javax.servlet.*,javax.servlet.http.*,org.apache.commons.fileupload.*,org.apache.commons.fileupload.disk.*,org.apache.commons.fileupload.servlet.*" %>
<!-- jsp:useBean id="myUpload" scope="page" class="com.jspsmart.upload.SmartUpload"/ -->
<%
	FileItemFactory factory = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(factory);
	List items = upload.parseRequest(request);
	Iterator iter = items.iterator();
	List files = new ArrayList();
	while (iter.hasNext()) {
		FileItem item = (FileItem) iter.next();
		if (!item.isFormField()) {
			files.add(item);
		}
	}
			
    //myUpload.initialize(pageContext);
    //myUpload.setMaxFileSize(1000000);
    upload.setFileSizeMax(1000000);
    String strFile_name = request.getParameter("filename2");
    String error = "";
    boolean isImportingMode = ("import".equals(request.getParameter("action")));
    boolean flag = false;
    File nFile = new File("");
    String strRealPath = request.getRealPath("\\upload");
    String strUser = request.getParameter("user");
    String strProject = request.getParameter("project");

    File mFile = new File(strRealPath);

    if (mFile.exists() == false) {
        mFile.mkdir();
    }

    try {
        //myUpload.upload();
        //com.jspsmart.upload.File myFile = myUpload.getFiles().getFile(0);
        
        if (files.get(0) != null/*!myUpload.getFiles().getFile(0).isMissing()*/) {
            //myFile.saveAs(strRealPath + "\\" + strFile_name, myUpload.SAVE_PHYSICAL);
            ((FileItem)files.get(0)).write(new File(strRealPath + File.separator + strFile_name));
            flag = true;
        }
    }
    catch(Exception e) {
        error = e.toString();
        e.printStackTrace();
    }
    if (!isImportingMode) {
%>
<HTML>
<HEAD>
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language="javascript">
function wClose() {
    window.close();
}

function ViewPicture(imagename) {
    var qrStr = "upload/" + imagename;
    var sFeature = "status=no,menu=no,scrollbars=yes";
    var requirementchart_wd = window.open(qrStr, "Picture", sFeature);
}
</SCRIPT>
</HEAD>
<BODY onunload "return"><%
        if (flag) {
%>
Upload this file OK. Click top button(X) to close this window.
<BR>
<INPUT class="Button" type="Button" value="Preview Picture" onclick=ViewPicture('<%=strFile_name%>')><%
        }
        else {
%>
Error:<%=error%>
Can not upload this file. Please try again.<%
        }
%>
</BODY>
</HTML><%
    }
    else {
        if (flag) { %>
<HTML>
File was uploaded. Click top button(X) to close this window.
<FORM name="import" action="DMSServlet">
<INPUT type="hidden" name="hidExcelFile">
<INPUT type="hidden" name="hidAction">
<INPUT type="hidden" name="hidActionDetail">
<INPUT type="hidden" name="hidUser" value="<%=strUser%>">
<INPUT type="hidden" name="hidProject" value="<%=strProject%>">
</FORM>
<SCRIPT>
    document.forms[0].hidExcelFile.value = "<%=strFile_name%>";
    document.forms[0].hidAction.value = "DM";
    document.forms[0].hidActionDetail.value = "ImportDefect";
    document.forms[0].submit();
//    window.close();
</SCRIPT>

</HTML><%
        }
    }
%>