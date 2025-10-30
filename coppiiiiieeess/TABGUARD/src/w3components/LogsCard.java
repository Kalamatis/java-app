package w3components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import components.ThemeManager;

public class LogsCard extends JPanel{
	
	private JLabel batchLabel;
    private JLabel dateLabel;
    private JLabel timeLabel;

	public final int CARD_HEIGHT = 200;
	public final int CARD_WIDTH = 200;	
	
	private int batch;
	private String date;
	private String time;


	public LogsCard(int batch, String date, String time) {
		this.batch = batch;
		this.date = date;
		this.time = time;
		
		
		setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT)); // hard upper bound
		setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT)); // your design size
		setMinimumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
		
		initComponents();
		applyTheme();
	}
	
	public void applyTheme() {
		setBackground(ThemeManager.getCardBackground());
		batchLabel.setForeground(ThemeManager.getCardTextColor());
		dateLabel.setForeground(ThemeManager.getCardTextColor());
		timeLabel.setForeground(ThemeManager.getCardTextColor());
		
		
	}

	private void initComponents() {
		loadLabels();
	}

	private void loadLabels() {
		// Batch label — emphasize visually
        batchLabel = new JLabel("Batch #"+batch, SwingConstants.CENTER);
        batchLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        batchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        batchLabel.setPreferredSize(new Dimension(200, 50));

        // Date label — smaller, lighter
        dateLabel = new JLabel(date, SwingConstants.CENTER);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateLabel.setPreferredSize(new Dimension(200, 26));

        // Time label — smaller, lighter
        timeLabel = new JLabel(time, SwingConstants.CENTER);
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setPreferredSize(new Dimension(200, 26));

        // Add everything with proper spacing
        add(Box.createVerticalStrut(10));
        add(batchLabel);
        add(Box.createVerticalStrut(5));
        add(dateLabel);
        add(Box.createVerticalStrut(2));
        add(timeLabel);
        add(Box.createVerticalGlue());
	}
	
	
	
}
