package database;

import java.sql.*;

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

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPCs);
            stmt.execute(createTabs);
            stmt.execute(createWarnings);
            System.out.println("[DB] Tables initialized.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
