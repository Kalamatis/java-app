package components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import database.PCRepository;
import windows.MainPanel;

public class FilterPanel extends JPanel{
	private int PANEL_WIDTH;
	private int PANEL_HEIGHT = 100;
	
	private final int CB_HEIGHT = 30;
	private final int CB_WIDTH = 150;
	
	private final int BUTTON_HEIGHT = 70;
	private final int BUTTON_WIDTH = 100;
	
	private boolean darkMode = false;
	
	private Font CB_FONT = new Font("Arial", Font.PLAIN, 20);
	ImageIcon blackDropdown;
	ImageIcon whiteDropdown;
	
	MainPanel mainPanel;
	PCRepository PCRepo = new PCRepository();
	DefaultComboBoxModel<String> specificDeviceModel;
	JTextField searchField;
	JComboBox<String> specificDevice;
	JButton clearLogButton, warnButton, DLToggleButton, Dropdown;
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
		specificDevice.addActionListener(e -> setSorterFunction());
		addDevicesToFilter();
		
		
		cbPanel.add(searchField);
		cbPanel.add(specificDevice);
		add(cbPanel);
	}
	
	private void addDevicesToFilter() {
		Vector<Integer> ids = PCRepository.getAllPcId();
		specificDeviceModel.removeAllElements();
		specificDeviceModel.addElement("All PCs");
		for(int id : ids) {
			specificDeviceModel.addElement(Integer.toString(id));
		}
	}
	
	private void setSorterFunction() {
		TableRowSorter<TableModel> sorter = mainPanel.getTablePanel().getTableRowSorter();
		String selected = (String) specificDevice.getSelectedItem();

	    if (selected == null || selected.equals("All PCs")) {
	        sorter.setRowFilter(null); // Show all
	    } else {
	        sorter.setRowFilter(RowFilter.regexFilter("^" + selected + "$", 0)); 
	        // '0' means filter based on the first column (PC Name)
	    }
	}
	
	private void upperSectionPanel() {
		whiteDropdown = new ImageIcon("light-dropdown.png");
		blackDropdown = new ImageIcon("black-dropdown.png");
		JPanel upperPanel = new JPanel();
		upperPanel.setPreferredSize(new Dimension(PANEL_WIDTH - 400, PANEL_HEIGHT - CB_HEIGHT));
		upperPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		

		cbPanel.add(upperPanel);
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













