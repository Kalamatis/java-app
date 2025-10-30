package windows;

import main.MainFrame;
import java.awt.*;
import javax.swing.*;
import java.io.IOException;

public class LandingPage extends JFrame {

    public LandingPage() {
        setTitle("Welcome | TAB-GUARD Monitoring System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main container
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(230, 240, 255));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // Title label
        JLabel titleLabel = new JLabel("Welcome to TAB-GUARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(new Color(25, 118, 210));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("A smart monitoring and restriction system");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add spacing
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(60));

        // Start button
        JButton startButton = new JButton("Launch System");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startButton.setBackground(new Color(60, 120, 200));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.setPreferredSize(new Dimension(180, 45));
        startButton.setMaximumSize(new Dimension(180, 45));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(40, 100, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(60, 120, 200));
            }
        });

        // Action when clicking "Launch System"
        startButton.addActionListener(e -> {
            try {
                MainFrame mainFrame = new MainFrame(); // Opens your main system window
                mainFrame.setVisible(true);
                dispose(); // Close the landing page
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to launch system:\n" + ex.getMessage(),
                    "Launch Error",
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        // Add button
        mainPanel.add(startButton);
        add(mainPanel);
    }
}
