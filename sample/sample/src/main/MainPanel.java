package main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import components.FilterPanel;
import components.TablePanel;

public class MainPanel extends JPanel{
	private int PANEL_WIDTH;
	private int PANEL_HEIGHT;
	
	MainFrame frame;
	TablePanel tablePanel;
	FilterPanel filterPanel;
	
	public MainPanel(MainFrame frame) {
		this.frame = frame;
		this.PANEL_WIDTH = frame.getFrameWidth();
		this.PANEL_HEIGHT = frame.getFrameHeight();
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setLayout(new BorderLayout());
		initComponents();
	}
	
	private void initComponents() {
		tablePanel = new TablePanel(this);
		filterPanel = new FilterPanel(this);
		
		add(tablePanel, BorderLayout.CENTER);
		add(filterPanel, BorderLayout.NORTH);
	}
	
	public MainFrame getMainFrame() {
		return frame;
	}
	
	public TablePanel getTablePanel() {
		return tablePanel;
	}
	
	public FilterPanel getFilterPanel() {
		return filterPanel;
	}
	
	public int getMainPanelWidth() {
		return PANEL_WIDTH;
	}
	public int getMainPanelHeight() {
		return PANEL_HEIGHT;
	}
	
}
