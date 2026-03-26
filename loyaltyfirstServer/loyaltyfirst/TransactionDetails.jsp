<%@ page import = "java.io.*,java.util.*,java.sql.*"%>

<%
String tref = request.getParameter("tref");

DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn = DriverManager.getConnection(url, "kvanka", "uwhufoon");

Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("select t.t_date, t.t_points, p.prod_name, p.prod_points, tp.quantity from transactions t, products p, transactions_products tp where t.tref='" + tref + "' and t.tref = tp.tref and tp.prod_id = p.prod_id");

String output = "";

while(rs.next()) {
  output += rs.getObject("t_date") + "," + rs.getObject("t_points") + "," + rs.getObject("prod_name") + "," + rs.getObject("quantity") + "," + rs.getObject("prod_points") + "#";
}
out.print(output);
conn.close();
%>
