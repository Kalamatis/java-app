package main;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;

import database.DatabaseManager;

public class Main {

	public static void main(String[] args) {
		
		System.setProperty("flatlaf.animation", "true");
		
		try {
            // Choose your theme
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
        	try {
				new MainFrame();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		

	}

}
