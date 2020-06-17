package bingo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBHandler {

	private final String url;
	private final String usr = "postgres";
	private final String passwd = "8eighTh3ignoRe4";

	public DBHandler(String dbname) {
		url = "jdbc:postgresql://localhost:5432/" + dbname;
	}

	public void insert(String table, int id, String value1, String value2) {
		String SQL = "INSERT INTO " + table + " VALUES(?,?,?)";

		try (Connection con = DriverManager.getConnection(url, usr, passwd)) {

			con.setAutoCommit(false);

			try (PreparedStatement ps = con.prepareStatement(SQL)) {
				ps.setInt(1, id);
				ps.setString(2, value1);
				ps.setString(3, value2);

				ps.executeUpdate();
				con.commit();
			} catch (Exception e) {
				con.rollback();
				System.out.println("rollback");
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(String table, int id) {
		String SQL = "DELETE FROM " + table + " WHERE id = ?";

		try (Connection con = DriverManager.getConnection(url, usr, passwd)) {

			con.setAutoCommit(false);

			try (PreparedStatement ps = con.prepareStatement(SQL)) {
				ps.setInt(1, id);

				ps.executeUpdate();
				con.commit();
			} catch (Exception e) {
				con.rollback();
				System.out.println("rollback");
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String select(String table, int id, String col) {
		String SQL = "SELECT * FROM " + table + " WHERE id = " + id;
		String res = null;

		try (Connection con = DriverManager.getConnection(url, usr, passwd)) {

			con.setAutoCommit(false);

			try (Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(SQL)) {

				rs.next();
				res = rs.getString(col);

			} catch (Exception e) {
				con.rollback();
				System.out.println("rollback");
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}
}
