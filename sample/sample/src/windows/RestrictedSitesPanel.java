package windows;
import javax.swing.*;

import database.DatabaseManager;
import main.MainFrame;
import w2components.LeftPanel;
import w2components.RightPanel;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class RestrictedSitesPanel extends JPanel {
    private DatabaseManager dbManager;
    MainFrame frame;
    private LeftPanel leftPanel;
    private JTextField siteField;
    private JComboBox<String> levelBox;
    private RightPanel rightPanel;
    
    Dimension leftPanelSize = new Dimension(350, 350);
    Dimension fieldSize = new Dimension(200, 28);
    Dimension buttonSize = new Dimension(100, 30);
    Dimension comboSize = new Dimension(80, 28);

    public RightPanel getRightPanel() {
    	return rightPanel;
    }
    
    public LeftPanel getLeftPanel() {
    	return leftPanel;
    }
    
    public MainFrame getMainFrame() {
    	return frame;
    }
    
    public RestrictedSitesPanel(MainFrame frame) {
    	this.frame = frame;
        dbManager = DatabaseManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------- LEFT SIDE: Centered Input Panel ----------
        leftPanel = new LeftPanel(this); // for vertical centering
        rightPanel = new RightPanel(this);

        // ---------- Split Pane ----------
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel.getRightWrapper());
        splitPane.setEnabled(false);
        splitPane.setDividerSize(3);
        splitPane.setDividerLocation(470);
        splitPane.setResizeWeight(0);

        add(splitPane, BorderLayout.CENTER);
        leftPanel.refreshList();
    }

    
}
