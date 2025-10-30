package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.DatabaseManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles requests from the browser extension:
 * GET /checkWarning?uuid=<device_uuid>
 *
 * Returns:
 *   { "warning": "message text" }  OR  {}
 */
public class CheckWarningHandler implements HttpHandler {
	private static final Map<String, String> warnings = new HashMap<>();
	DatabaseManager db = DatabaseManager.getInstance();
	// Call this from server code
    public static void addWarning(String uuid, String message) {
        if (uuid != null && !uuid.isEmpty()) {
            warnings.put(uuid, message);
        }
    }
	
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (!"GET".equalsIgnoreCase(method)) {
            respond(exchange, 405, "{}");
            return;
        }
        // Example: /checkWarning?uuid=xxxx
        String query = exchange.getRequestURI().getQuery();
        String uuid = query != null && query.startsWith("uuid=") ? query.substring(5) : null;

        String message = "";

        if (uuid != null) {
        	Connection conn = db.getConnection();
        	
	    	synchronized(conn) {
	    		try {
	                // 1️⃣ Fetch the latest warning for this UUID
	                int warningId = -1;
	                try (PreparedStatement ps = conn.prepareStatement(
	                        "SELECT w.id, w.message FROM warnings w JOIN pcs p ON w.pc_id = p.id WHERE p.uuid = ? ORDER BY w.timestamp ASC LIMIT 1"
	                )) {
	                    ps.setString(1, uuid);
	                    ResultSet rs = ps.executeQuery();
	                    if (rs.next()) {
	                        warningId = rs.getInt("id"); // store the warning row ID
	                        message = rs.getString("message");
	                    }
	                }
	
	                // 2️⃣ Delete the warning row so it won't alert again
	                if (warningId != -1) {
	                    try (PreparedStatement del = conn.prepareStatement("DELETE FROM warnings WHERE id = ?")) {
	                        del.setInt(1, warningId);
	                        del.executeUpdate();
	                    }
	                }
	
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
        	}
            
        }

        respond(exchange, 200, "{\"warning\":\"" + message + "\"}");
    }

    private void respond(HttpExchange exchange, int status, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}