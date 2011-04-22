<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CoreWa demo</title>
    <script type="text/javascript">
        function submitForm(frm, eventId) {
            //alert("" + eventId);
            frm.eventId.value = eventId;
            frm.submit();
        }
    </script>
</head>
<body>
Menu page<br/>
<b>Sample of action by link:</b>
<br/>
<a href="main.do?screenId=Menu&eventId=List">List</a>
<br/>
<b>Sample of action by submit form:</b>
    <form action="main.do">
        <input type="hidden" name="screenId" value="Menu">
        <!-- 
        <input type="hidden" name="eventId" value="">
         -->
        <!-- 
        <input type="button" name="Add" value="eventId" onclick='submitForm(document.forms[0], "Add");'>
         -->
         <input type="submit" name="eventId" value="Add">
    </form>
 <b>Sample of action by submit form by JavaScript</b>
 <form name"frm2" action="main.do" method="POST">
        <input type="hidden" name="screenId" value="Menu">
        <input type="hidden" name="eventId" value="">
        
        <a href="#" onclick='submitForm(document.forms[1], "List");'>List</a>
        <br/> 
        <input type="button" name="Add" value="Add" onclick='submitForm(document.forms[1], "Add");'>
    </form>
</body>
</html>