<%@ page import = "java.io.*,java.util.*,java.sql.*"%>

<%
String cid = request.getParameter("cid");
String prize_id = request.getParameter("prizeid");

DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn = DriverManager.getConnection(url, "kvanka", "uwhufoon");

Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT P.p_description, P.points_needed, RH.r_date, EC.center_name FROM Prizes P INNER JOIN Redemption_History RH ON P.prize_id = RH.prize_id INNER JOIN ExchgCenters EC ON RH.center_id = EC.center_id WHERE RH.cid =" + cid + "AND RH.prize_id =" + prize_id);

String output = "";

while(rs.next()) {
    output += rs.getObject("p_description") + "," + rs.getObject("points_needed") + "," + rs.getObject("r_date") + "," + rs.getObject("center_name") + "#";
}
out.print(output);
conn.close();
%>
