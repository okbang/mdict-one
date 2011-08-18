<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML><%@
    page import = "java.util.*, java.io.*, java.sql.*, java.sql.Connection,
        fpt.dashboard.framework.connection.*"
%><%
    String pid = request.getParameter("ProjectID");

    WSConnectionPooling myPool = new WSConnectionPooling();
    javax.sql.DataSource ds = null;
    Connection con = null;
    PreparedStatement  prepStmt = null;
    ResultSet rs = null;

    try {
        ds = myPool.getDS();
        con = ds.getConnection();

        String selectStatement ="";
        selectStatement = "SELECT Excel_file from project_database where project_id= ?";
        prepStmt = con.prepareStatement(selectStatement);
        prepStmt.setString(1,pid);
        rs = prepStmt.executeQuery();
        byte [] myByte=null;
        while (rs.next()) {
            myByte = rs.getBytes(1);
        }
        if (myByte != null) {
            response.setContentType("application/vnd.ms-excel");
            out.clear();
            out = null;
            ServletOutputStream st = response.getOutputStream();
            st.write(myByte);

            rs.close();
            con.close();
            st.close();
            out = pageContext.getOut();
        }
        else {
%>
<META http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<FONT color="red" size="3">This project does not have Project Database.</FONT>
</HTML><%
        }//end else
    }//end try
    catch (Exception ex) {
        System.out.println("Exception occurs in ExcelFile.jsp");
    }
    finally {
        if (rs != null) {
            rs.close();
        }
        if (prepStmt != null) {
            prepStmt.close();
        }
        if (con != null) {
            con.close();
        }
    }
%>