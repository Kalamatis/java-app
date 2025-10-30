package windows;

import javax.swing.*;

import components.ThemeManager;

import java.awt.*;

import database.DatabaseManager;
import main.MainFrame;
import w2components.LeftPanel;
import w2components.RightPanel;

public class RestrictedSitesPanel extends JPanel {
    private DatabaseManager dbManager;
    private MainFrame frame;
    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    
    JSplitPane splitPane;

    // Dimension constants
    private static final Dimension LEFT_PANEL_SIZE = new Dimension(350, 350);
    private static final Dimension FIELD_SIZE = new Dimension(200, 28);
    private static final Dimension BUTTON_SIZE = new Dimension(100, 30);
    private static final Dimension COMBO_SIZE = new Dimension(80, 28);

    public RestrictedSitesPanel(MainFrame frame) {
        this.frame = frame;
        dbManager = DatabaseManager.getInstance();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------- LEFT PANEL ----------
        leftPanel = new LeftPanel(this);
        leftPanel.setPreferredSize(LEFT_PANEL_SIZE);
        leftPanel.setBackground(Color.WHITE);

        // ---------- RIGHT PANEL ----------
        rightPanel = new RightPanel(this);
        rightPanel.setBackground(Color.WHITE);

        // ---------- SPLIT PANE ----------
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel.getRightWrapper());
        splitPane.setDividerLocation(470); // left panel width
        splitPane.setEnabled(false);       // prevents user from moving divider
        splitPane.setDividerSize(3);
        splitPane.setResizeWeight(0);
        splitPane.setBorder(null);   // remove default border
        

        add(splitPane, BorderLayout.CENTER);

        // ---------- INITIAL REFRESH ----------
        leftPanel.refreshList();
        applyTheme();
    }
    
    public void applyTheme() {
    	setBackground(ThemeManager.getPanelColor());
    	splitPane.setBackground(ThemeManager.getPanelColor());
    	rightPanel.applyTheme();
    	leftPanel.applyTheme();
    }

    // ---------- GETTERS ----------
    public RightPanel getRightPanel() {
        return rightPanel;
    }

    public LeftPanel getLeftPanel() {
        return leftPanel;
    }

    public MainFrame getMainFrame() {
        return frame;
    }
}
