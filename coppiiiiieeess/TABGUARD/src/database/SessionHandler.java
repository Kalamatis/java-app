package database;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SessionHandler {
	private static int currentSessionId = -1;
	
	static DatabaseManager db = DatabaseManager.getInstance();
	
	
    public static int startSession() throws SQLException {
        String sql = "INSERT INTO sessions (started_at) VALUES (CURRENT_TIMESTAMP)";
        
        Connection conn = db.getConnection();
        synchronized(conn) {
        	try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    currentSessionId = rs.getInt(1);
                    System.out.println("✅ Session started with ID: " + currentSessionId);
                }
            }
            return currentSessionId;
        }
        
    }
    
    public static synchronized List<Session> getAllSessions() {
    	List<Session> sessions = new ArrayList<>();
    	String sql = """
    			SELECT id, started_at
    			FROM sessions
    			""";
    	Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
        	while (rs.next()) {
                sessions.add(new Session(
                		rs.getInt("id"),
                		rs.getString("started_at")
                		));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    	
		return sessions;
    	
    }
    private static String sanitizeUrl(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) return "unknown";
        try {
            URI uri = new URI(rawUrl);
            String host = uri.getHost();
            return (host != null) ? host.replace("www.", "") : rawUrl;
        } catch (URISyntaxException e) {
            // fallback if URL is malformed
            return rawUrl;
        }
    }
    public static synchronized Vector<Vector<Object>> getTabEventsBySession(int sessionId) {
        Vector<Vector<Object>> events = new Vector<>();
        String sql = """
            SELECT pcs.id AS pc_id, tab_url, title, event_type, timestamp
            FROM tab_events
            JOIN pcs ON pcs.id = tab_events.pc_id
            WHERE session_id = ?
            ORDER BY timestamp ASC
        """;

        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("pc_id"));
                row.add(rs.getString("title"));
                row.add(sanitizeUrl(rs.getString("tab_url")));
                row.add(rs.getString("timestamp"));
                events.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }


    public static void endSession() throws SQLException {
        if (currentSessionId == -1) return;
        String sql = "UPDATE sessions SET ended_at = CURRENT_TIMESTAMP WHERE id = ?";
        Connection conn = db.getConnection();
        
        synchronized(conn) {
        	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, currentSessionId);
                pstmt.executeUpdate();
                System.out.println("🛑 Session " + currentSessionId + " ended.");
            }
            currentSessionId = -1;
        }
        
    }

    public static int getCurrentSessionId() {
        return currentSessionId;
    }
}