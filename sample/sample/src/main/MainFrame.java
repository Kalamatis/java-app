package main;

import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;

import database.DatabaseManager;
import database.PCRepository;
import handler.HTTPHandler;
import handler.TabEvent;

public class MainFrame extends JFrame{
	private final int FRAME_WIDTH = 1000;
	private final int FRAME_HEIGHT = 800;
	DatabaseManager db = DatabaseManager.getInstance();
	MainPanel mainPanel;
	HTTPHandler httpHandler;
//	Vector<String> list = new Vector<>();
	
//	public void addPCNumber(TabEvent tabEvent) {
//		DefaultComboBoxModel<String> model = mainPanel.getFilterPanel().getSpecificDeviceModel();
//		model.removeAllElements(); // optional: clear old items before adding new ones
//		Connection conn = db.getConnection();
//		synchronized(conn) {
//			try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM pcs");
//			         ResultSet rs = stmt.executeQuery()) {
//
//			        while (rs.next()) {
//			            String id = rs.getString("pc_id");
//
//			            boolean exists = false;
//			            for (int i = 0; i < model.getSize(); i++) {
//			                if (model.getElementAt(i).equals(id)) {
//			                    exists = true;
//			                    break;
//			                }
//			            }
//
//			            if (!exists) {
//			                model.addElement(id);
//			            }
//			        }
//
//			    } catch (SQLException e) {
//			        e.printStackTrace();
//			    }
//		}
//	    
//	}

//	public Vector<String> getPCNumbers() {
//		return list;
//	}
	
	public MainFrame() throws IOException {
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		components();
		setVisible(true);
	}
	
	private void components() throws IOException {
		
		mainPanel = new MainPanel(this);
		httpHandler = new HTTPHandler(this);
		
		add(mainPanel, BorderLayout.CENTER);
	}
	
	public MainPanel getMainPanel() {
		return mainPanel;
	}
	
	public int getFrameWidth() {
		return FRAME_WIDTH;
	}
	public int getFrameHeight() {
		return FRAME_HEIGHT;
	}
}
