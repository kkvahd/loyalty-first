<%@ page import = "java.io.*,java.util.*,java.sql.*"%>

<%
String cid = request.getParameter("cid");

DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn = DriverManager.getConnection(url, "kvanka", "uwhufoon");

Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT cname, num_of_points FROM customers c INNER JOIN point_accounts pa ON c.cid = pa.cid WHERE c.cid=" + cid);

String output = "";

while(rs.next()) {
  output += rs.getObject("cname") + "," + rs.getObject("num_of_points") + "#";
}
out.print(output);
conn.close();
%>
