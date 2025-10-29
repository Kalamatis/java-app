package w3components;

import java.awt.Dimension;

import javax.swing.JPanel;

import windows.LogsPanel;

public class W3FilterPanel extends JPanel{
	private int PANEL_WIDTH;
	private int PANEL_HEIGHT = 100;
	
	public W3FilterPanel(LogsPanel logsPanel) {
		 this.PANEL_WIDTH = logsPanel.getMainFrame().getFrameWidth();
		 
		 setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		 
		 initComponents();
	}

	private void initComponents() {
		
		
	}
	
}
