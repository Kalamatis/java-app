package main;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import components.ThemeManager;
import database.DatabaseManager;
import handler.HTTPHandler;
import windows.MainPanel;
import windows.RestrictedSitesPanel;
import windows.LogsPanel;

public class MainFrame extends JFrame {
    private final int FRAME_WIDTH = 1000;
    private final int FRAME_HEIGHT = 700;

    RestrictedSitesPanel restrictedSitesPanel;
    LogsPanel logsPanel;
    
    private DatabaseManager db = DatabaseManager.getInstance();
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private MenuBar menuBar;
    private MainPanel mainPanel;
    private HTTPHandler httpHandler;

    public MainFrame() throws IOException {
        // ✅ Load modern theme
    	
        setTitle("TAB-GUARD - Monitoring System");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ✅ Enable FlatLaf custom title bar (lets us color it)
     // ✅ Enable FlatLaf custom title bar (lets us color it)
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_SHOW_ICON, true);
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_BACKGROUND, new Color(135, 206, 235)); // sky blue
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_FOREGROUND, Color.BLACK); // ✅ white title text

        // ✅ Initialize components
        components();

        // ✅ Rounded corners + subtle background
        getRootPane().putClientProperty(FlatClientProperties.STYLE, ""
            + "arc:20;"
            + "background:$Frame.background;"
        );

        setVisible(true);
    }

//    

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

        restrictedSitesPanel = new RestrictedSitesPanel(this);
        logsPanel = new LogsPanel(this);
        
        cardPanel.putClientProperty(FlatClientProperties.STYLE, ""
            + "background:$Panel.background;"
            + "arc:15;"
        );

        cardPanel.add(mainPanel, "MainPanel");
        cardPanel.add(restrictedSitesPanel, "Restricted Panel");
        cardPanel.add(logsPanel, "LogsPanel");
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
    
    public RestrictedSitesPanel getRestrictedSitesPanel() {
    	return restrictedSitesPanel;
    }
    
    public LogsPanel getLogsPanel() {
    	return logsPanel;
    }

    public int getFrameWidth() {
        return FRAME_WIDTH;
    }

    public int getFrameHeight() {
        return FRAME_HEIGHT;
    }
}

//private void setupTheme() {
//  try {
//      // 🌤️ Base light theme
//      FlatLaf.setup(new FlatLightLaf());
//
//      // 🎨 Core colors
//      Color skyBlue = new Color(135, 206, 235);
//      Color primaryBlue = new Color(25, 118, 210);
//      Color lightBlue = new Color(227, 242, 253);
//      Color borderGray = new Color(200, 200, 200);
//      Color white = Color.WHITE;
//      Color gray = Color.BLUE;
//
//      // ✅ UI Defaults
//      UIManager.put("Panel.background", white);
//      UIManager.put("Frame.background", gray);
//      UIManager.put("Button.background", white);
//      UIManager.put("Button.foreground", primaryBlue);
//      UIManager.put("Button.borderColor", borderGray);
//      UIManager.put("Button.hoverBackground", primaryBlue);
//      UIManager.put("Button.hoverForeground", white);
//      UIManager.put("Button.focusedBorderColor", primaryBlue);
//      UIManager.put("Component.focusColor", primaryBlue);
//      UIManager.put("Component.borderColor", borderGray);
//      UIManager.put("Label.foreground", new Color(33, 33, 33));
//      UIManager.put("TextField.background", lightBlue);
//      UIManager.put("TextField.foreground", Color.BLACK);
//      UIManager.put("Table.background", white);
//      UIManager.put("Table.foreground", Color.BLACK);
//      UIManager.put("Table.selectionBackground", primaryBlue);
//      UIManager.put("Table.selectionForeground", white);
//      UIManager.put("TableHeader.background", primaryBlue);
//      UIManager.put("TableHeader.foreground", white);
//
//      // 🟦 Rounded corners
//      UIManager.put("Button.arc", 15);
//      UIManager.put("Component.arc", 15);
//      UIManager.put("TextComponent.arc", 12);
//
//      // 🧊 Subtle shadows
//      UIManager.put("Popup.dropShadowPainted", true);
//      UIManager.put("Popup.dropShadowColor", new Color(0, 0, 0, 40));
//
//      // 🪟 Title bar colors (custom FlatLaf title bar)
//      UIManager.put("TitlePane.background", skyBlue);
//      UIManager.put("TitlePane.foreground", Color.BLACK);
//      UIManager.put("TitlePane.buttonHoverBackground", new Color(100, 180, 220));
//      UIManager.put("TitlePane.buttonPressedBackground", new Color(70, 160, 210));
//      UIManager.put("TitlePane.font", new Font("Segoe UI", Font.BOLD, 13));
//
//      // 🧭 Menu bar matches top color
//      UIManager.put("MenuBar.background", skyBlue);
//      UIManager.put("MenuBar.foreground", Color.BLUE);
//      UIManager.put("Menu.foreground", Color.WHITE);
//      UIManager.put("MenuItem.background", Color.WHITE);
//      UIManager.put("MenuItem.foreground", Color.BLACK);
//
//  } catch (Exception e) {
//      System.err.println("Failed to initialize blue-white theme: " + e.getMessage());
//  }
//}
