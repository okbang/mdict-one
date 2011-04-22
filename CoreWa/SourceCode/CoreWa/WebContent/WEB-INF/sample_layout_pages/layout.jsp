<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CoreWa demo - Add</title>
<script type="text/javascript" src='pages/scripts/common.js'></script>
</head>
<body>
Index layout
    <table border="1" width="100%" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="3">
            <jsp:include page="/header.part"/>
        </td>
      </tr>
      <tr>
        <td width="13%">
            Left
        </td>
        <td width="68%" height="200"><jsp:include page="/main.part"/>
        </td>
        <td width="18%">Right
        </td>
      </tr>
      <tr>
        <td colspan="3">Footer
        </td>
      </tr>
    </table>
</body>
</html>