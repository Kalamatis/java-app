package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;
import windows.LandingPage;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            new LandingPage().setVisible(true); // ✅ Show landing page first
        });
    }
}
