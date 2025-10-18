package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code DatabaseManager} class is responsible for managing the
 * SQLite database connection and initializing the database schema.
 * <p>
 * This class follows the Singleton pattern to ensure that only
 * one database connection exists throughout the entire application.
 * <p>
 * It should be accessed using {@link #getInstance()} and provides
 * access to the shared {@link Connection} via {@link #getConnection()}.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private static Connection connection;
    private static final String DB_URL = "jdbc:sqlite:server.db";

    /** Private constructor initializes the connection and creates tables if needed. */
    private DatabaseManager() {
        connect();
        createTables();
    }

    /**
     * Returns the singleton instance of the {@code DatabaseManager}.
     * Creates it if it doesn’t exist.
     *
     * @return the single {@code DatabaseManager} instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /** Establishes a connection to the SQLite database. */
    private void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;");
            }
            System.out.println("[DB] Connected to SQLite database (WAL mode).");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to DB: " + e.getMessage());
        }
    }

    /**
     * Returns the shared SQLite {@link Connection}.
     * @return active database connection
     */
    public synchronized Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes all required tables in the database.
     * Tables:
     * <ul>
     *     <li><b>pcs</b>: registered computers</li>
     *     <li><b>tab_events</b>: recorded tab activities</li>
     *     <li><b>warnings</b>: messages sent to users</li>
     * </ul>
     */
    private void createTables() {
        String createPCs = """
            CREATE TABLE IF NOT EXISTS pcs (
              id INTEGER PRIMARY KEY,
              uuid TEXT UNIQUE,
              name TEXT NOT NULL,
              ip TEXT,
              registered_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """;

        String createTabs = """
            CREATE TABLE IF NOT EXISTS tab_events (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              pc_id INTEGER,
              tab_url TEXT,
              title TEXT,
              event_type TEXT,
              session_id TEXT,
              timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
              FOREIGN KEY (pc_id) REFERENCES pcs(id)
            );
        """;

        String createWarnings = """
            CREATE TABLE IF NOT EXISTS warnings (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              pc_id INTEGER,
              message TEXT,
              timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
              FOREIGN KEY (pc_id) REFERENCES pcs(id)
            );
        """;
        
        String createRestrictedSites = """
        	CREATE TABLE IF NOT EXISTS restricted (
        	  id INTEGER PRIMARY KEY AUTOINCREMENT,
        	  website TEXT NOT NULL UNIQUE,
        	  level INTEGER
        	);
        		""";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPCs);
            stmt.execute(createTabs);
            stmt.execute(createWarnings);
            stmt.execute(createRestrictedSites);
            System.out.println("[DB] Tables initialized.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // ---------------- CRUD for Restricted Sites ----------------

    public synchronized void addRestrictedSite(String website, String level) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT OR REPLACE INTO restricted (website, level) VALUES (?, ?)")) {
            ps.setString(1, website);
            ps.setString(2, level);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void removeRestrictedSite(String website) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM restricted WHERE website = ?")) {
            ps.setString(1, website);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<String[]> getAllRestrictedSites() {
        List<String[]> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT website, level FROM restricted ORDER BY website ASC")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{rs.getString("website"), rs.getString("level")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public synchronized Map<String, Integer> getRestrictedWebsites() {
        Map<String, Integer> map = new HashMap<>();
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT website, level FROM restricted")) {
            while (rs.next()) {
                map.put(rs.getString("website"), rs.getInt("level"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

//    /** Closes the database connection. */
//    public static void close() {
//        try {
//            if (connection != null && !connection.isClosed()) {
//                connection.close();
//                System.out.println("[DB] Connection closed.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
