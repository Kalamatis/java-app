package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import database.DatabaseManager;
import database.SessionHandler;
import handler.HTTPHandler;
import windows.MainPanel;
import windows.RestrictedSitesPanel;
import windows.LogsPanel;

public class MainFrame extends JFrame{
	private final int FRAME_WIDTH = 1000;
	private final int FRAME_HEIGHT = 700;
	DatabaseManager db = DatabaseManager.getInstance();
	CardLayout cardLayout;
	JPanel cardPanel;
	MenuBar menuBar;
	MainPanel mainPanel;
	HTTPHandler httpHandler;
//	Vector<String> list = new Vector<>();
	

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
		menuBar = new MenuBar(this);
		mainPanel = new MainPanel(this);
		httpHandler = new HTTPHandler(this);
		
		createCardLayout();
		
		menuBar.attachFrame(this);
		
		setJMenuBar(menuBar);
		add(cardPanel, BorderLayout.CENTER);
	}
	
	private void createCardLayout() {
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		
		cardPanel.add(mainPanel, "MainPanel");
        cardPanel.add(new RestrictedSitesPanel(this), "Restricted Panel");
        cardPanel.add(new LogsPanel(this), "Window3");
		
	}
	
	public CardLayout getCardLayout() {
		return cardLayout;
	}
	
	public JPanel getCardPanel() {
		return cardPanel;
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
