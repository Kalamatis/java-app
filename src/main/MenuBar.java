package main;

import java.awt.CardLayout;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import components.ThemeManager;

public class MenuBar extends JMenuBar{
	MainFrame frame;
	
	CardLayout cards;
	JPanel cardPanel;
	
	JMenu windowMenu, viewMenu, themeMenu;
	JMenuItem mainWindow,window2,window3;
	JRadioButtonMenuItem lightMode, darkMode;
	public MenuBar(MainFrame frame) {
		this.frame = frame;
        // ---  CREATE "Window" MENU ---
        windowMenu = new JMenu("Window");
        themeMenu = new JMenu("Actions");
        viewMenu = new JMenu("View");

        // ---  CREATE WINDOW ITEMS ---
        mainWindow = new JMenuItem("Home");
        window2 = new JMenuItem("Add Restriction");
        window3 = new JMenuItem("Window3");
        
        // --- CREATE ACTION ITEMS ---
        
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
        
	}
	
	private void addRadioListeners() {
		lightMode.addActionListener(e -> ThemeManager.toggleTheme(frame));
		darkMode.addActionListener(e -> ThemeManager.toggleTheme(frame));
	}

	/**
     * Call this *after* MainFrame has initialized its card layout.
     */
    public void attachFrame(MainFrame frame) {
        this.cards = frame.getCardLayout();
        this.cardPanel = frame.getCardPanel();

        mainWindow.addActionListener(e -> cards.show(cardPanel, "MainPanel"));
        window2.addActionListener(e -> cards.show(cardPanel, "Restricted Panel"));
        window3.addActionListener(e -> cards.show(cardPanel, "Window3"));
    }
	
	
}
