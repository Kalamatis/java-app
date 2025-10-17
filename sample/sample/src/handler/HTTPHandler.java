package handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.Connection;

import com.sun.net.httpserver.HttpServer;

import database.DatabaseManager;
import main.MainFrame;

public class HTTPHandler {
String lanIP = getLanIP();
MainFrame mainFrame;
	public HTTPHandler(MainFrame mainFrame) throws IOException{
		this.mainFrame = mainFrame;
		HttpServer server = HttpServer.create(new InetSocketAddress(lanIP, 3000), 0);
		System.out.println(lanIP);
		server.createContext("/registerPC", new RegisterHandler());
		server.createContext("/tab-event", new TabEventHandler(this));
		server.createContext("/checkWarning", new CheckWarningHandler());
		server.setExecutor(null);
		server.start();
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
