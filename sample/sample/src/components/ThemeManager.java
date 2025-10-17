package components;

import java.awt.Color;
import javax.swing.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme;

public class ThemeManager {

    private static boolean darkMode = false;

    public static void toggleTheme(JFrame frame) {
        darkMode = !darkMode;
        applyTheme(darkMode, frame);
    }

    public static void applyTheme(boolean dark, JFrame frame) {
        try {
            if (dark) {
                UIManager.setLookAndFeel(new FlatGradiantoMidnightBlueIJTheme());

//                UIManager.put("Panel.background", new Color(43, 43, 48));
//                UIManager.put("Label.foreground", new Color(220, 220, 220));
//                UIManager.put("Button.background", new Color(60, 63, 65));
//                UIManager.put("Button.foreground", new Color(235, 235, 235));
//                UIManager.put("Table.background", new Color(45, 45, 50));
//                UIManager.put("Table.foreground", new Color(230, 230, 230));
//                UIManager.put("Table.selectionBackground", new Color(90, 120, 190));
//                UIManager.put("Table.selectionForeground", Color.WHITE);
//                UIManager.put("Table.gridColor", new Color(70, 70, 70));

            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());

//                UIManager.put("Panel.background", new Color(245, 246, 250));
//                UIManager.put("Label.foreground", new Color(35, 35, 35));
//                UIManager.put("Button.background", new Color(230, 232, 235));
//                UIManager.put("Button.foreground", new Color(30, 30, 30));
//                UIManager.put("Button.borderColor", new Color(200, 200, 200));
//                UIManager.put("Button.hoverBackground", new Color(210, 212, 215));
//                UIManager.put("Button.pressedBackground", new Color(190, 192, 195));
//                UIManager.put("Table.background", new Color(250, 250, 250));
//                UIManager.put("Table.foreground", new Color(30, 30, 30));
//                UIManager.put("Table.selectionBackground", new Color(60, 120, 200));
//                UIManager.put("Table.selectionForeground", Color.WHITE);
//                UIManager.put("Table.gridColor", new Color(220, 220, 220));
//                UIManager.put("Table.alternateRowColor", new Color(245, 245, 245));
            }

            // 🔁 Refresh the entire frame (recursively updates everything inside)
            SwingUtilities.updateComponentTreeUI(frame);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isDarkMode() {
        return darkMode;
    }
}
