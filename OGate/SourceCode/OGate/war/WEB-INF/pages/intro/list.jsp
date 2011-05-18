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
function submitDelete(frmName, screenId, eventId, contentId) {
    var frm = document.forms[frmName];
    var msg = "Bạn thực sự muốn xóa dữ liệu?";
    
    if ("-1" == contentId) {
        msg = "Bạn thực sự muốn xóa TẤT CẢ các dòng dữ liệu?"
    }
    
    if (confirm(msg)) {
      frm.screenId.value = screenId;
      frm.eventId.value = eventId;
      frm.contentId.value = contentId;
      frm.submit();
    }
}
//-->
</script>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
</table>
<%-- Save contentId will be deleted --%>
<input type="hidden" name="contentId"/>

<table border="1" cellpadding="0" cellspacing="0" width="100%">
    <tr>
    <td>No.</td><td>Key</td> <td>Date</td> <td>Content</td>
    <td>
      <a href="#" title="Xóa tất cả" onclick='submitDelete("frmIntro","Intro", "delete", "-1")'><img border="0" src="pages/images/DeleteAll.gif" width="18" height="18"></a>
    </td>
    
    </tr>
    <c:forEach var="intro" items="${intros}">
     <tr>
       <td>&nbsp;</td>
       <td>${intro.key}</td>
       <td>${intro.modified}</td>
       <td>${intro.content}</td>
       <td><a href="#" onclick='submitDelete("frmIntro","Intro", "delete", "${intro.key}")'><img border="0" src="pages/images/delete.gif" width="16" height="16"></a></td>
     </tr>
     </c:forEach>
</table>
          
