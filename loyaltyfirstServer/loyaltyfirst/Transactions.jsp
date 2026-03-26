<%@ page import = "java.io.*,java.util.*,java.sql.*"%>

<%
String cid = request.getParameter("cid");

DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn = DriverManager.getConnection(url, "kvanka", "uwhufoon");

Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT tref, t_date, t_points, amount FROM transactions WHERE cid=" + cid);

String output = "";

while(rs.next()) {
  output += rs.getObject("tref") + "," + rs.getObject("t_date") + "," + rs.getObject("t_points") + "," + rs.getObject("amount") + "#";
}
out.print(output);
conn.close();
%>
