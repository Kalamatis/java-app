package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;

import database.DatabaseManager;
import database.PCRepository;
import main.MainPanel;

public class FilterPanel extends JPanel{
	private int PANEL_WIDTH;
	private int PANEL_HEIGHT = 100;
	
	private final int CB_HEIGHT = 30;
	private final int CB_WIDTH = 150;
	
	private final int BUTTON_HEIGHT = 70;
	private final int BUTTON_WIDTH = 100;
	
	private boolean darkMode = false;
	
	private Font CB_FONT = new Font("Arial", Font.PLAIN, 20);
	
	MainPanel mainPanel;
	PCRepository PCRepo = new PCRepository();
	DefaultComboBoxModel<String> specificDeviceModel;
	JTextField searchField;
	JComboBox<String> specificDevice;
	JButton clearLogButton, warnButton, DLToggleButton;
	JPanel cbPanel;
	
	
	public FilterPanel(MainPanel mainPanel) {
		this.PANEL_WIDTH = mainPanel.getMainFrame().getFrameWidth();
		this.mainPanel = mainPanel;
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
		initComponents();
	}
	
	public void initComponents() {
		Combobox();
		Buttons();
	}

	
	private void Combobox() {
		cbPanel = new JPanel();
		cbPanel.setPreferredSize(new Dimension(PANEL_WIDTH - 400, PANEL_HEIGHT));
		cbPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 0));
		
		
		
		upperSectionPanel();
		
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(CB_WIDTH, CB_HEIGHT));
		searchField.setFont(CB_FONT);
		setDocumentListener(searchField);
		
		specificDeviceModel = new DefaultComboBoxModel<>();
		specificDevice = new JComboBox<>(specificDeviceModel);
		specificDevice.setPreferredSize(new Dimension(CB_WIDTH, CB_HEIGHT));
		specificDevice.setFocusable(false);
		
		
		cbPanel.add(searchField);
		cbPanel.add(specificDevice);
		add(cbPanel);
	}
	
	private void upperSectionPanel() {
		
		JPanel upperPanel = new JPanel();
		upperPanel.setPreferredSize(new Dimension(PANEL_WIDTH - 400, PANEL_HEIGHT - CB_HEIGHT));
		upperPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		DLToggleButton = new JButton();
		DLToggleButton.setPreferredSize(new Dimension(50, CB_HEIGHT));
		DLToggleButton.addActionListener(e -> ThemeManager.toggleTheme(mainPanel.getMainFrame()));
		
		upperPanel.add(DLToggleButton);
		cbPanel.add(upperPanel);
	}

	
	public DefaultComboBoxModel<String> getSpecificDeviceModel() {
		return specificDeviceModel;
	}

	private void Buttons() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(PANEL_WIDTH - (PANEL_WIDTH - 300), PANEL_HEIGHT));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, PANEL_HEIGHT - BUTTON_HEIGHT));
		
		
		clearLogButton = new JButton("Clear Log");
		clearLogButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		clearLogButton.setFocusable(false);
		clearLogButton.addActionListener(e -> mainPanel.getTablePanel().clearTableData());
		
		warnButton = new JButton("Warn");
		warnButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		warnButton.setFocusable(false);
		warnButton.addActionListener(e -> mainPanel.getTablePanel().sendWarning());
		
		panel.add(clearLogButton);
		panel.add(warnButton);
		add(panel, BorderLayout.EAST);
			
	}
	
	private void setDocumentListener(JTextField sf) {
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				mainPanel.getTablePanel().filter(searchField.getText());
			}

			public void removeUpdate(DocumentEvent e) {
				mainPanel.getTablePanel().filter(searchField.getText());
			}

			public void changedUpdate(DocumentEvent e) {
				mainPanel.getTablePanel().filter(searchField.getText());
			}

		});
	}
}













