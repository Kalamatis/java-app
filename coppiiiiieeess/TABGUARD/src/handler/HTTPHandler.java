package handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;

import com.sun.net.httpserver.HttpServer;

import database.DatabaseManager;
import database.SessionHandler;
import main.MainFrame;

public class HTTPHandler {
String lanIP = getLanIP();
MainFrame mainFrame;
HttpServer server;

	public HTTPHandler(MainFrame mainFrame) throws IOException {
        this.mainFrame = mainFrame;

        // --- Initialize HTTP Server ---
        server = HttpServer.create(new InetSocketAddress(lanIP, 3000), 0);
        System.out.println("[SERVER] Started on: " + lanIP + ":3000");

        // --- Register Endpoints ---
        server.createContext("/registerPC", new RegisterHandler());
        server.createContext("/tab-event", new TabEventHandler(this));
        server.createContext("/checkWarning", new CheckWarningHandler());
        

        server.setExecutor(null);
        server.start();

        // --- Start Session Automatically ---
        try {
            int sessionId = SessionHandler.startSession();
            System.out.println("[SESSION] ✅ Started with ID: " + sessionId);
        } catch (SQLException e) {
            System.err.println("[SESSION] ❌ Failed to start session: " + e.getMessage());
            e.printStackTrace();
        }

        // --- Add Shutdown Hook to End Session Gracefully ---
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                int sessionId = SessionHandler.getCurrentSessionId();

                // Save table data before ending session
                if (mainFrame != null && mainFrame.getMainPanel() != null) {
                    System.out.println("💾 Saving current tab events before shutdown...");
                    mainFrame.getMainPanel().getTablePanel().saveTableToDatabase(sessionId);
                }

                SessionHandler.endSession();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (server != null) {
                server.stop(0);
                System.out.println("[SERVER] 🛑 Stopped cleanly.");
            }
        }));

    }
	private String getLanIP() {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			
			return localHost.getHostAddress();
			
		}catch(UnknownHostException e){
			System.out.println("Error" + e);
			
		}
		
		return "";
		
	}
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}
}
