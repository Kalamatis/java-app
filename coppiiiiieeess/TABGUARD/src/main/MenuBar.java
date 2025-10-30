package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import components.ThemeManager;

public class MenuBar extends JMenuBar {
    MainFrame frame;

    CardLayout cards;
    JPanel cardPanel;

    JMenu windowMenu, viewMenu, themeMenu;
    JMenuItem mainWindow, window2, window3;
    JRadioButtonMenuItem lightMode, darkMode;

    // 🎨 Define sky blue theme colors
    private final Color skyBlue = new Color(135, 206, 235);
    private final Color hoverBlue = new Color(100, 180, 220);
    private final Color textColor = Color.WHITE;

    public MenuBar(MainFrame frame) {
        this.frame = frame;

        // --- CREATE MENUS ---
        windowMenu = new JMenu("Window");
        viewMenu = new JMenu("View");
        themeMenu = new JMenu("Actions");

        // --- MENU ITEMS ---
        mainWindow = new JMenuItem("Home");
        window2 = new JMenuItem("Restriction Settings");
        window3 = new JMenuItem("Logs");

        // --- THEME RADIO BUTTONS ---
        ButtonGroup themeGroup = new ButtonGroup();
        lightMode = new JRadioButtonMenuItem("Light Mode");
        darkMode = new JRadioButtonMenuItem("Dark Mode");
        themeGroup.add(lightMode);
        themeGroup.add(darkMode);
        lightMode.setSelected(true);
        addRadioListeners();

        themeMenu.add(lightMode);
        themeMenu.add(darkMode);
        viewMenu.add(themeMenu);

        windowMenu.add(mainWindow);
        windowMenu.add(window2);
        windowMenu.add(window3);

        add(windowMenu);
        add(viewMenu);

        // --- STYLE MENU BAR ---
        setBackground(skyBlue);
        setBorderPainted(false);
        setOpaque(true);

        // --- Apply hover + color to menus and menu items ---
        styleMenu(windowMenu);
        styleMenu(viewMenu);
        styleMenu(themeMenu);

        for (JMenuItem item : new JMenuItem[]{mainWindow, window2, window3, lightMode, darkMode}) {
            styleMenuItem(item);
        }
    }

    private void addRadioListeners() {
        lightMode.addActionListener(e -> {
        	ThemeManager.toggleTheme(false,frame); 
        	frame.getRestrictedSitesPanel().getLeftPanel().refreshList(); 
        	frame.getLogsPanel().refreshCards();
        
        });
	        darkMode.addActionListener(e -> {ThemeManager.toggleTheme(true,frame); 
	        frame.getRestrictedSitesPanel().getLeftPanel().refreshList(); 
	        frame.getLogsPanel().refreshCards();
	    });
    }

    // 🧭 Menu (top-level button: "Window", "View")
    private void styleMenu(JMenu menu) {
        menu.setOpaque(true);
        menu.setBackground(skyBlue);
        menu.setForeground(textColor);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menu.setBackground(hoverBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menu.setBackground(skyBlue);
            }
        });
    }

    // 📋 Dropdown items ("Home", "Window2", etc.)
    private void styleMenuItem(JMenuItem item) {
        item.setOpaque(true);
        item.setBackground(Color.WHITE);
        item.setForeground(Color.BLACK);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(230, 245, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(Color.WHITE);
            }
        });
    }

    public void attachFrame(MainFrame frame) {
        this.cards = frame.getCardLayout();
        this.cardPanel = frame.getCardPanel();

        mainWindow.addActionListener(e -> cards.show(cardPanel, "MainPanel"));
        window2.addActionListener(e -> cards.show(cardPanel, "Restricted Panel"));
        window3.addActionListener(e -> cards.show(cardPanel, "LogsPanel"));
    }
}
