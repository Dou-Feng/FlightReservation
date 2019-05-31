package hust.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private final static String URL = "jdbc:mysql://localhost:3306/FlightReservation?autoReconnect=true&useSSL=false";
    private final static String driver = "com.mysql.jdbc.Driver";
    private final static String user = "root";
    private final static String password = "famine";
    private static Connection connection = null;
    static {
        try {
            System.out.println("Connection...");
            Class.forName(driver);
            connection = DriverManager.getConnection(URL, user, password);
            if (!connection.isClosed())
                System.out.println("Connection finished!");
        } catch (Exception e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            System.err.println("Connection is shutdown");
            reconnection();
        }
        return connection;
    }

    private static void reconnection() {
        System.out.println("reconnection...");
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, user, password);
                System.out.println("reconnection finished!");
            }
        } catch (Exception e) {
            System.out.println("reconnection failed!");
            e.printStackTrace();
        }
    }
}
