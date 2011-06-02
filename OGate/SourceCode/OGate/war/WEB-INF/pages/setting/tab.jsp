<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<form name="tabSetting" action="menu.do" method="post">
  <input type="hidden" name="screenId" value="Menu"/>
  <input type="hidden" name="eventId" value=""/>
  
    <table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
        <td>Tabs:</td>
        <td>&nbsp;</td>
        <td>Edit permission</td>
        <td>&nbsp;</td>
    </tr>
        <tr align=left>
            <td width="132" rowspan="6">
            <select size="10" name="D1" le>
    <option>Giới thiệu</option>
    <option>Dịch vụ</option>
    <option>Hoạt động</option>
    <option>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </option>
    </select></td>
            <td width="35">&nbsp;</td>
            <td width="167" rowspan="6">
            <select size="10" name="D2" le>
    <option>openonesadm@gmail.com</option>
    <option>huan@gmail.com</option>
    <option>tanln@yahoo.com</option>
    <option>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </option>
    </select></td>
            <td>&nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35">
            <img border="0" src="pages/images/to-top.png" width="24" height="24"></td>
            <td>
            &nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35"><img border="0" src="pages/images/up.png" width="24" height="24"></td>
            <td>&nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35"><img border="0" src="pages/images/down.png" width="24" height="24"></td>
            <td>&nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35">
            <img border="0" src="pages/images/to-bottom.png" width="24" height="24"></td>
            <td>
            &nbsp;</td>
        </tr>
        <tr align=left>
            <td width="35">&nbsp; </td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td colspan=2><input type="text" name="T1" size="20"><input type="button" value="Add Tab" name="Add"></td>
            <td colspan=2><input type="text" name="T2" size="20"><input type="button" value="Add Manager" name="AddManager"></td>
        </tr>
    </table>
    <p style="text-align: left">
    <input type="button" value="Submit" name="Submit">
    <input type="button" value="Cancel" name="Cancel"></p>
</form>