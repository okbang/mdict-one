<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>Non-Comfornity Management System</TITLE>

<SCRIPT language="javascript">
function checkLanguageCountry() {
    document.frmHome.submit();
}
</SCRIPT>
</HEAD>

<BODY onload="checkLanguageCountry()">

<FORM method="POST" name="frmHome" action="NcmsServlet">
	<INPUT type="hidden" name="hidAction" value="NCMSHomepage">
	<INPUT type="hidden" name="selectedProjectHidden" value="">
</FORM>
</BODY>
</HTML>
