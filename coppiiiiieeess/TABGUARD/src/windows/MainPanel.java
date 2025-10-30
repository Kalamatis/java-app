package windows;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import components.FilterPanel;
import components.TablePanel;
import components.ThemeManager;
import main.MainFrame;
import com.formdev.flatlaf.FlatClientProperties;

public class MainPanel extends JPanel {
    private int PANEL_WIDTH;
    private int PANEL_HEIGHT;

    private MainFrame frame;
    private TablePanel tablePanel;
    private FilterPanel filterPanel;
    JPanel tableContainer;

    public MainPanel(MainFrame frame) {
        this.frame = frame;
        this.PANEL_WIDTH = frame.getFrameWidth();
        this.PANEL_HEIGHT = frame.getFrameHeight();

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new BorderLayout());
//        setBackground(Color.red); // Light neutral background

        // FlatLaf styling (safe for all versions)
        putClientProperty(FlatClientProperties.STYLE, ""
            + "arc:15;"
            + "background:$Panel.background;"
        );

        initComponents();
        applyTheme();
    }
    public void applyTheme() {
        setBackground(ThemeManager.getBackgroundColor());
        tableContainer.setBackground(ThemeManager.getBackgroundColor());
        filterPanel.applyTheme();
        tablePanel.applyTheme();
    }
    private void initComponents() {
        tablePanel = new TablePanel(this, setCol());
        filterPanel = new FilterPanel(this);

        // Add padding + simple background
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add subtle border container around table
        tableContainer = new JPanel(new BorderLayout());
        
        tableContainer.add(tablePanel, BorderLayout.CENTER);

        add(filterPanel, BorderLayout.NORTH);
        add(tableContainer, BorderLayout.CENTER);
    }
    
    private Vector<String> setCol(){
		Vector<String> vec = new Vector<>();
		vec.add("ID");
		vec.add("PC Name");
		vec.add("Current Website");
		vec.add("Timestamp");
		return vec;
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
