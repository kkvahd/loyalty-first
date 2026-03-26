<%@ page import = "java.io.*,java.util.*,java.sql.*"%>

<%
String cid = request.getParameter("cid");
String tref = request.getParameter("tref");

DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn = DriverManager.getConnection(url, "kvanka", "uwhufoon");

Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT C.family_id, PA.percent_added, SUM(T.t_points) AS sum_points FROM Customers C JOIN Point_Accounts PA ON C.cid = PA.cid JOIN Transactions T ON C.cid = T.cid WHERE T.tref ='" + tref + "' AND C.cid =" + cid + " GROUP BY C.family_id, PA.percent_added");

String output = "";

while(rs.next()) {
    output += rs.getObject("family_id") + "," + rs.getObject("percent_added") + "," + rs.getObject("sum_points") + "#";
}
out.print(output);
conn.close();
%>
