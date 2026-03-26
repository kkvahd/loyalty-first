<%@ page import = "java.io.*,java.util.*,java.sql.*"%>

<%
String fid = request.getParameter("fid");
String cid = request.getParameter("cid");
String npoints = request.getParameter("npoints");

DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn = DriverManager.getConnection(url, "kvanka", "uwhufoon");

Statement stmt = conn.createStatement();
int results = stmt.executeUpdate("UPDATE Point_Accounts SET num_of_points = num_of_points +" + npoints + " WHERE family_id =" + fid + " AND cid != " + cid);

if (results > 0) {
    out.print("Point accounts of family members are updated successfully");
} else {
    out.print("No rows updated");
}

conn.close();
%>

