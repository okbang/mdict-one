<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<form name="frmLang" action="/setting.mod" method="post">
  <input type="hidden" name="screenId" value="LangSetting"/>
  <input type="hidden" name="eventId" value="save"/>
  
    <table border="0" width="100%" cellspacing="0" cellpadding="0">
        <tr align=left>
            <td width="167">
            <textarea name="languages" cols="20">${form.languages}</textarea></td>
        </tr>
    </table>
    <input type="submit" value="Submit" name="Save">
</form>