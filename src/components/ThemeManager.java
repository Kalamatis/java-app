package components;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;

import main.MainFrame;

public class ThemeManager {

    private static boolean darkMode = false;

    public static void toggleTheme(MainFrame frame) {
        darkMode = !darkMode;
        applyTheme(darkMode, frame);
    }

    public static void applyTheme(boolean dark, MainFrame frame) {
        try {
            if (dark) {
                UIManager.setLookAndFeel(new FlatDraculaIJTheme());
//                frame.getMainPanel().getFilterPanel().setDropdownIcon(dark);
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
//                frame.getMainPanel().getFilterPanel().setDropdownIcon(dark);
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
