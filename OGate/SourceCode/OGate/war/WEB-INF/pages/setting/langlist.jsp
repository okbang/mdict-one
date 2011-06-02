<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK"%>
<script type="text/javascript">
<!--
/**
 * Submit delete.
 * @param frmName
 * @param screenId
 * @param eventId
 * @return
 */
function submitDelete(frmName, screenId, eventId, objectKey) {
    var frm = document.forms[frmName];
    var msg = "Bạn thực sự muốn xóa dữ liệu?";
    
    if ("-1" == objectKey) {
        msg = "Bạn thực sự muốn xóa TẤT CẢ các dòng dữ liệu?"
    }
    
    if (confirm(msg)) {
      frm.screenId.value = screenId;
      frm.eventId.value = eventId;
      frm.objectKey.value = objectKey;
      frm.submit();
    }
}
//-->
</script>
<form name="frmLang" action="/setting.mod" method="post">
  <input type="hidden" name="screenId" value="LangSetting"/>
  <%-- Default event: save --%>
  <input type="hidden" name="eventId" value="save"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
</table>
<%-- Save contentId will be deleted --%>
<input type="hidden" name="objectKey"/>

<table border="1" cellpadding="0" cellspacing="0" width="100%">
    <tr>
    <td>No.</td><td>Key</td> <td>Code</td> <td>Language name</td>
    <td>
      <a href="#" title="Xóa tất cả" onclick='submitDelete("frmLang","LangSetting", "delete", "-1")'><img border="0" src="pages/images/DeleteAll.gif" width="18" height="18"></a>
    </td>
    
    </tr>
    <%-- intro: IntroOutForm --%>
    <c:forEach var="lang" items="${form.langList}">
     <tr>
       <td>&nbsp;</td>
       <td></td>
       <td>${lang.cd}</td>
       <td>${lang.name}</td>
       <td><a href="#" onclick='submitDelete("frmLang","LangSetting", "delete", "${lang.key}")'><img border="0" src="pages/images/delete.gif" width="16" height="16"></a></td>
     </tr>
     </c:forEach>
</table>
</form>