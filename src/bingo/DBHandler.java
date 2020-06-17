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

	public void insert(String table, int id, String name, String... values) {
		String SQL = "INSERT INTO " + table + " VALUES(" + id + ",'" + name + "'";
		for (int i = 0; i < 25; i++) {
			SQL += ",'" + values[i] + "'";
		}
		SQL += ")";

		executeSQL(SQL);
	}

	public void delete(String table, int id) {
		String SQL = "DELETE FROM " + table + " WHERE id = " + id;
		executeSQL(SQL);
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

	public void update(String table, int id, String column, String value) {
		String SQL = "UPDATE " + table + " SET " + column + " = " + value + " WHERE id = " + id;
		executeSQL(SQL);
	}

	public void createTable(String table) {

		String SQL = "CREATE TABLE " + table + "(id INTEGER, name VARCHAR(20), "
				+ "value0 VARCHAR(2), value1 VARCHAR(2), value2 VARCHAR(2), value3 VARCHAR(2), value4 VARCHAR(2),"
				+ "value5 VARCHAR(2), value6 VARCHAR(2), value7 VARCHAR(2), value8 VARCHAR(2), value9 VARCHAR(2),"
				+ "value10 VARCHAR(2), value11 VARCHAR(2), value12 VARCHAR(2), value13 VARCHAR(2), value14 VARCHAR(2),"
				+ "value15 VARCHAR(2), value16 VARCHAR(2), value17 VARCHAR(2), value18 VARCHAR(2), value19 VARCHAR(2),"
				+ "value20 VARCHAR(2), value21 VARCHAR(2), value22 VARCHAR(2), value23 VARCHAR(2), value24 VARCHAR(2),"
				+ " PRIMARY KEY (id))";

		executeSQL(SQL);
	}

	public void dropTable(String table) {
		String SQL = "DROP TABLE " + table;
		executeSQL(SQL);
	}
}
