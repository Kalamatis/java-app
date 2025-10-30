package components;

import java.awt.Color;
import javax.swing.SwingUtilities;
import main.MainFrame;

public class ThemeManager {
    private static boolean darkMode = false;

 // ============================== Light Theme ==============================
    private static final Color LIGHT_BACKGROUND = new Color(245, 245, 245);
    private static final Color LIGHT_PANEL = new Color(230, 230, 230);
    private static final Color LIGHT_TEXT = Color.BLACK;

    // ✅ Buttons
    private static final Color LIGHT_BUTTON = new Color(255, 255, 255);
    private static final Color LIGHT_BUTTON_HOVER = new Color(220, 220, 220);

    // ✅ Checkboxes
    private static final Color LIGHT_CHECKBOX = new Color(255, 255, 255);
    private static final Color LIGHT_CHECKBOX_HOVER = new Color(230, 230, 230);

    // ✅ JTable
    private static final Color LIGHT_TABLE_BG = new Color(250, 250, 250);
    private static final Color LIGHT_TABLE_TEXT = Color.BLACK;
    private static final Color LIGHT_TABLE_HEADER = new Color(250, 250, 250);
    private static final Color LIGHT_TABLE_HEADER_TEXT = Color.BLACK;

    // ✅ Table Rows
//    private static final Color LIGHT_ROW_DEFAULT = new Color(255, 255, 255);
    private static final Color LIGHT_ROW_HOVER = new Color(240, 240, 240);
    private static final Color LIGHT_ROW_SELECTED = new Color(210, 230, 255);

    // ✅ Table Row Accents
//    private static final Color LIGHT_ROW_RED = new Color(255, 204, 203);
//    private static final Color LIGHT_ROW_YELLOW = new Color(255, 249, 196);
//    private static final Color LIGHT_ROW_ORANGE = new Color(255, 224, 178);


    // ============================== Dark Theme ==============================
    private static final Color DARK_BACKGROUND = new Color(40, 40, 40);
    private static final Color DARK_PANEL = new Color(60, 60, 60);
    private static final Color DARK_TEXT = Color.WHITE;

    // ✅ Buttons
    private static final Color DARK_BUTTON = new Color(80, 80, 80);
    private static final Color DARK_BUTTON_HOVER = new Color(100, 100, 100);

    // ✅ Checkboxes
    private static final Color DARK_CHECKBOX = new Color(70, 70, 70);
    private static final Color DARK_CHECKBOX_HOVER = new Color(90, 90, 90);

    // ✅ JTable
    private static final Color DARK_TABLE_BG = new Color(50, 50, 50);
    private static final Color DARK_TABLE_TEXT = new Color(230, 230, 230);
    private static final Color DARK_TABLE_HEADER = new Color(50, 50, 50);
    private static final Color DARK_TABLE_HEADER_TEXT = new Color(230, 230, 230);

    // ✅ Table Rows
//    private static final Color DARK_ROW_DEFAULT = new Color(45, 45, 45);
    private static final Color DARK_ROW_HOVER = new Color(65, 65, 65);
    private static final Color DARK_ROW_SELECTED = new Color(75, 110, 175);

//    // ✅ Table Row Accents
//    private static final Color DARK_ROW_RED = new Color(139, 0, 0);
//    private static final Color DARK_ROW_YELLOW = new Color(204, 153, 0);
//    private static final Color DARK_ROW_ORANGE = new Color(255, 140, 0);

    
 // ========== CARDS LIGHT ==========
    private static final Color LIGHT_CARD_BACKGROUND = new Color(245, 245, 245); // slightly lighter than base
    private static final Color LIGHT_CARD_TEXT_COLOR = new Color(30, 30, 30);     // dark text for readability

    // ========== CARDS DARK ==========
    private static final Color DARK_CARD_BACKGROUND = new Color(70, 70, 70);      // slightly lighter than (60,60,60) for contrast
    private static final Color DARK_CARD_TEXT_COLOR = new Color(230, 230, 230);   // light gray-white text for visibility

    

 // ====== Table Rows Color "LIGHT" ======
    private static final Color LIGHT_RED_ROWS = new Color(255, 204, 203);// Light pastel red
    private static final Color LIGHT_YELLOW_ROWS = new Color(255, 249, 196);  // Light pastel yellow
    private static final Color LIGHT_ORANGE_ROWS = new Color(255, 224, 178);
    private static final Color LIGHT_DEFAULT_ROWS = new Color(255, 255, 255); // Pure white

    // ====== Table Rows Color "DARK" ======
    private static final Color DARK_RED_ROWS = new Color(139, 0, 0);          // Deep dark red
    private static final Color DARK_YELLOW_ROWS = new Color(204, 153, 0); 
    private static final Color DARK_ORANGE_ROWS = new Color(255, 140, 0);// Warm muted yellow
    private static final Color DARK_DEFAULT_ROWS = new Color(45, 45, 45);     // Dark gray background


    // ===== Apply Theme =====
    public static void toggleTheme(boolean dark, MainFrame frame) {
        darkMode = dark;
        applyTheme(frame);
    }

    public static void applyTheme(MainFrame frame) {
        // This will refresh all components visually
        SwingUtilities.updateComponentTreeUI(frame);
        frame.repaint();

        // Tell MainFrame to reapply panel-specific colors
        if (frame.getMainPanel() != null) {
            frame.getMainPanel().applyTheme();
        }
        if (frame.getRestrictedSitesPanel() != null) {
            frame.getRestrictedSitesPanel().applyTheme();
        }
        if (frame.getLogsPanel() != null) {
        	frame.getLogsPanel().applyTheme();
        }
    }

    // ===== Getters =====
    public static boolean isDarkMode() { return darkMode; }

    public static Color getBackgroundColor() {
        return isDarkMode() ? DARK_BACKGROUND : LIGHT_BACKGROUND;
    }

    public static Color getPanelColor() {
        return isDarkMode() ? DARK_PANEL : LIGHT_PANEL;
    }

    public static Color getTextColor() {
        return isDarkMode() ? DARK_TEXT : LIGHT_TEXT;
    }

    // ✅ Buttons
    public static Color getButtonColor() {
        return isDarkMode() ? DARK_BUTTON : LIGHT_BUTTON;
    }

    public static Color getButtonHoverColor() {
        return isDarkMode() ? DARK_BUTTON_HOVER : LIGHT_BUTTON_HOVER;
    }

    // ✅ Checkboxes
    public static Color getCheckboxColor() {
        return isDarkMode() ? DARK_CHECKBOX : LIGHT_CHECKBOX;
    }

    public static Color getCheckboxHoverColor() {
        return isDarkMode() ? DARK_CHECKBOX_HOVER : LIGHT_CHECKBOX_HOVER;
    }

    // ✅ JTable
    public static Color getTableBackground() {
        return isDarkMode() ? DARK_TABLE_BG : LIGHT_TABLE_BG;
    }

    public static Color getTableTextColor() {
        return isDarkMode() ? DARK_TABLE_TEXT : LIGHT_TABLE_TEXT;
    }
    public static Color getTableHeaderColor() {
    	return isDarkMode() ? DARK_TABLE_HEADER : LIGHT_TABLE_HEADER;
    }
    
    public static Color getTableHeaderTextColor() {
    	return isDarkMode() ? DARK_TABLE_HEADER_TEXT : LIGHT_TABLE_HEADER_TEXT;
    }

    // ✅ Table Rows
    public static Color getRowDefaultColor() {
        return isDarkMode() ? DARK_DEFAULT_ROWS : LIGHT_DEFAULT_ROWS;
    }

    public static Color getRowHoverColor() {
        return isDarkMode() ? DARK_ROW_HOVER : LIGHT_ROW_HOVER;
    }

    public static Color getRowSelectedColor() {
        return isDarkMode() ? DARK_ROW_SELECTED : LIGHT_ROW_SELECTED;
    }

    // ✅ Table Row Accents
    public static Color getRowRedColor() {
        return isDarkMode() ? DARK_RED_ROWS : LIGHT_RED_ROWS;
    }

    public static Color getRowYellowColor() {
        return isDarkMode() ? DARK_YELLOW_ROWS : LIGHT_YELLOW_ROWS;
    }

    public static Color getRowOrangeColor() {
        return isDarkMode() ? DARK_ORANGE_ROWS : LIGHT_ORANGE_ROWS;
    }
    
    // CARDS
    public static Color getCardBackground() {
        return isDarkMode() ? DARK_CARD_BACKGROUND : LIGHT_CARD_BACKGROUND;
    }
    public static Color getCardTextColor() {
    	return isDarkMode() ? DARK_CARD_TEXT_COLOR : LIGHT_CARD_TEXT_COLOR;
    }
}
