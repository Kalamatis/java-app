package w2components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import windows.RestrictedSitesPanel;

public class RightPanel extends JPanel{
	RestrictedSitesPanel resSitesPanel;
	JScrollPane scrollPane;
	JPanel rightWrapper;
	
	public RightPanel getRightPanel() {
		return this;
	}
	
	public JPanel getRightWrapper() {
		return rightWrapper;
	}
	
	public RightPanel(RestrictedSitesPanel resSitesPanel) {
		this.resSitesPanel = resSitesPanel;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		initComponents();
		
	}
	
	private void initComponents() {
		addScrollPane();
		addWrapper();
	}
	
	private void addScrollPane() {
		scrollPane = new JScrollPane(this);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Restricted Sites"));
        scrollPane.setPreferredSize(new Dimension(500, 400));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
	}
	
	private void addWrapper() {
		// Create a wrapper panel to control right-side width
        rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 200)); // leave empty space
        scrollPane.setMaximumSize(new Dimension(500, 400));
        scrollPane.setPreferredSize(new Dimension(500, 400));
        scrollPane.setMinimumSize(new Dimension(500, 400));
        rightWrapper.add(scrollPane, BorderLayout.CENTER);
	}
	
}
