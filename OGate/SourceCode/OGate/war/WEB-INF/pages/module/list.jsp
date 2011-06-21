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
Tab information:<br/>
Module Type: ${modules.module.type}<br/>
ModuleId (or Tab Code): ${modules.module.id}<br/>
Tab Name: ${modules.module.name}<br/>

<b>Danh sách các modules:</b>
<table border="1" cellpadding="0" cellspacing="0" width="100%">
    <tr>
    <td width="10px" nowrap="nowrap">No.</td><td>Type</td> <td>Date</td> <td>Content</td><td>LangCode</td>
    <td>
      <a href="#" title="Xóa tất cả" onclick='submitDelete("frmModuleEdit","ModuleIntroEditor", "delete", "-1")'><img border="0" src="pages/images/DeleteAll.gif" width="18" height="18"></a>
    </td>
    
    </tr>
    <%-- intro: IntroOutForm --%>
    <c:forEach var="module" items="${modules.moduleList}">
     <tr>
       <td>&nbsp;</td>
       <td>&nbsp;</td>
       <td>${module.created}</td>
       <td>${module.content}</td>
       <td>${module.lang}</td>
       <td><a href="#" onclick='submitDelete("frmModuleEdit","ModuleIntroEditor", "delete", "${module.key}")'><img border="0" src="pages/images/delete.gif" width="16" height="16"></a></td>
     </tr>
     </c:forEach>
</table>
