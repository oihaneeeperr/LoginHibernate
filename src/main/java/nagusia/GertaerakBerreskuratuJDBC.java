package nagusia;

import java.sql.*;
import java.util.Date;

public class GertaerakBerreskuratuJDBC {
	public static void main(String[] args) {
		Connection c;
		Statement s;
		ResultSet rs;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mariadb://localhost/gertaerak", "root", "admin");
			s = c.createStatement();
			rs = s.executeQuery("SELECT * FROM LOGINGERTAERA");
			System.out.println("LOGINGERTAERA (ID, DESKRIBAPENA, DATA)");
			while (rs.next()) {
				Long id = rs.getLong("ID");
				String deskribapena = rs.getString("DESKRIBAPENA");
				Date data = rs.getDate("DATA");
				System.out.println(id + " / " + deskribapena + " / " + data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}