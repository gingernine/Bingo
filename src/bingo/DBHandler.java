package bingo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBHandler {

	private final String url = "jdbc:postgresql://localhost:5432/test";
	private final String usr = "postgres";
	private final String passwd = "8eighTh3ignoRe4";

	public void executeSQL(String SQL) {
		try (Connection con = DriverManager.getConnection(url, usr, passwd)) {

			con.setAutoCommit(false);

			try (Statement st = con.createStatement()) {

				st.executeUpdate(SQL);
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

	public String getResultString(String SQL, String col) {

		String res = null;

		try (Connection con = DriverManager.getConnection(url, usr, passwd)) {

			con.setAutoCommit(false);

			try (Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(SQL)) {

				rs.next();
				res = rs.getString(col);
				con.commit();

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

	public int getResultInt(String SQL, String col) {

		int res = 0;

		try (Connection con = DriverManager.getConnection(url, usr, passwd)) {

			con.setAutoCommit(false);

			try (Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(SQL)) {

				rs.next();
				res = rs.getInt(col);
				con.commit();

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
