package handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.json.JSONObject;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.DatabaseManager;
import database.SessionHandler;

public class TabEventHandler implements HttpHandler {
    private final Gson gson = new Gson();
    private final HTTPHandler httpHandler;
    private final DatabaseManager db = DatabaseManager.getInstance();

    public TabEventHandler(HTTPHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("📡 Tab event detected.");

        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        try (InputStream is = exchange.getRequestBody()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(body);

            String uuid = json.getString("uuid");
            int id = json.optInt("id", -1);
            int sessionId = SessionHandler.getCurrentSessionId();
            String name = json.optString("name", "Unknown Device");
            String url = json.optString("url", "");
            String eventType = json.optString("eventType", "");
            String timestamp = json.optString("timestamp", "");

            // ⚠️ Ensure a session exists
            if (sessionId == -1) {
                System.out.println("⚠️ No active session. Starting a new one...");
                sessionId = SessionHandler.startSession();
            }

            // Get PC ID by UUID
            try (Connection conn = db.getConnection()) {
                Optional<Integer> pcIdOpt = getIdByUuid(conn, uuid);
                if (pcIdOpt.isEmpty()) {
                    sendJson(exchange, 400, new JSONObject()
                        .put("success", false)
                        .put("error", "Unknown UUID").toString());
                    return;
                }

                int pcId = pcIdOpt.get();

                // 🧠 Insert the tab event into database
                String sql = """
                    INSERT INTO tab_events (pc_id, session_id, tab_url, title, event_type, timestamp)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, pcId);
                    ps.setInt(2, sessionId);
                    ps.setString(3, url);
                    ps.setString(4, name);
                    ps.setString(5, eventType);
                    ps.setString(6, timestamp);
                    ps.executeUpdate();
                    System.out.println("💾 Tab event saved for session " + sessionId + " (PC " + pcId + ")");
                }

                // ✅ Also show it on the TablePanel live
                addTableItem(new TabEvent(id, name, url, timestamp));

                sendJson(exchange, 200, new JSONObject()
                        .put("success", true)
                        .put("message", "Tab event recorded").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJson(exchange, 500, new JSONObject()
                    .put("success", false)
                    .put("error", e.getMessage()).toString());
        }
    }

    /** Retrieves a PC ID by its UUID */
    private Optional<Integer> getIdByUuid(Connection conn, String uuid) throws SQLException {
        String query = "SELECT id FROM pcs WHERE uuid = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, uuid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(rs.getInt("id"));
            }
        }
        return Optional.empty();
    }

    /** Sends plain text response */
    private static void sendResponse(HttpExchange ex, int code, String msg) throws IOException {
        ex.sendResponseHeaders(code, msg.getBytes().length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(msg.getBytes());
        }
    }

    /** Sends JSON response */
    private static void sendJson(HttpExchange ex, int code, String json) throws IOException {
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(code, json.getBytes().length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(json.getBytes());
        }
    }

    /** Adds a table row to the UI */
    private void addTableItem(TabEvent tabEvent) {
        if (httpHandler.getMainFrame() != null) {
            httpHandler.getMainFrame()
                .getMainPanel()
                .getTablePanel()
                .addTableItem(tabEvent.getEventData());
        }
    }
}
