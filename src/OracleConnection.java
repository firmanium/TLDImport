//import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class OracleConnection {
	
	//HATI-HATI class ini akan meng-create table di database yang dituju
	private java.sql.Connection conn = null;
	private final String db_urlprefix = "jdbc:oracle:thin:@";
	private final String db_hostname= "10.252.82.156";
	private final String db_portnumber = "1521";
	private final String db_name= "DEVRNF";
	private final String db_username = "apps";
	private final String db_password = "apps";
	private final String db_driver = "oracle.jdbc.driver.OracleDriver";


	// Constructor
	public OracleConnection() throws Exception {
		//dua item dibawah harus aktif untuk parsing koneksi
		Class.forName(db_driver);
		conn = java.sql.DriverManager.getConnection(getConnectionUrl(),db_username,db_password); //nilai ini akan diparsing ke constring are
	}

	//method buat masukin url connection
	private String getConnectionUrl(){
		return db_urlprefix+db_hostname+":"+db_portnumber+":"+db_name;
	}

	//method buat menutup connection. Jangan dipanggil kecuali di akhir aktivitas
	protected void closeConnection() throws SQLException {
		if(conn!=null) conn.close(); 
		conn=null;
	}

	//method buat melakukan test connection
	public synchronized java.sql.Connection testConn () throws Exception { //lempar constring are
		try {
			if(conn!=null) {
				System.out.println("Connection Successful!");
				System.out.println("constring are : " + String.valueOf(conn));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Trace in getConnection() : " + e.getMessage());
		}
		//closeConnection(); //jangan dikasih closeConnection, ntar pas update ga bisa krn ilang session
		return conn;
	}

	//method buat shutdown database
	public void shutdown() throws SQLException {
		Statement st = conn.createStatement();
		st.execute("SHUTDOWN");
		closeConnection();
	}

	//method buat query
	public synchronized void query(String expression) throws SQLException {
		Statement st = null;
		ResultSet rs = null;
		st = conn.createStatement();
		rs = st.executeQuery(expression);
		dump(rs);
		st.close();
	}

	//method buat insert update delete
	public synchronized void update(String expression) throws SQLException {
		Statement st = null;
		st = conn.createStatement();
		System.out.println(expression);
		int i = st.executeUpdate(expression);
		if (i == -1) {
			System.out.println("db error : " + expression);
		}
		st.close();
	}

	//method buat nge-dump hasil query
	public static void dump(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int colmax = meta.getColumnCount();
		int i;
		Object o = null;
		for (; rs.next(); ) {
			for (i = 0; i < colmax; ++i) {
				o = rs.getObject(i + 1);  
				System.out.print(o.toString() + " ");
			}
			System.out.println(" ");
		}
	}

	public static void main(String[] args) {
		OracleConnection db = null;
		try {
			db = new OracleConnection();
		} catch (Exception ex1) {
			ex1.printStackTrace();
			return;
		}
		try {
			db.update("CREATE TABLE sample_table ( str_col VARCHAR(256), num_col numeric (4) )");
		} catch (SQLException ex2) {
			//ignore
			//ex2.printStackTrace();  
			// second time we run program should throw execption since table
			// already there this will have no effect on the db
		}
		try {
			// add some rows - will create duplicates if run more then once
			// the id column is automatically generated
			db.update("INSERT INTO sample_table(str_col,num_col) VALUES('Ford', 100)");
			db.update("INSERT INTO sample_table(str_col,num_col) VALUES('Toyota', 200)");
			db.update("INSERT INTO sample_table(str_col,num_col) VALUES('Honda', 300)");
			db.update("INSERT INTO sample_table(str_col,num_col) VALUES('GM', 400)");
			// do a query
			db.query("SELECT * FROM sample_table WHERE num_col < 250");
			// at end of program
			//db.shutdown(); //BAEK_BAEK LOE !!! ... Matiin database nih jangan macam2 loe
		} catch (SQLException ex3) {
			ex3.printStackTrace();
		}
	}
}
