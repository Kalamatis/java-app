package components;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JOptionPane;

import database.PCRepository;
import windows.MainPanel;

public class FilterPanel extends JPanel {
    private int PANEL_WIDTH;
    private int PANEL_HEIGHT = 100;

    private final int CB_HEIGHT = 30;
    private final int CB_WIDTH = 150;

    private final int BUTTON_HEIGHT = 70;
    private final int BUTTON_WIDTH = 100;

    private Font CB_FONT = new Font("Arial", Font.PLAIN, 20);
    ImageIcon blackDropdown;
    ImageIcon whiteDropdown;

    MainPanel mainPanel;
    PCRepository PCRepo = new PCRepository();
    DefaultComboBoxModel<String> specificDeviceModel;
    JTextField searchField;
    JComboBox<String> specificDevice;
    JButton clearLogButton, warnButton, DLToggleButton, Dropdown;
    JPanel cbPanel, upperPanel, buttonContainer;

    public FilterPanel(MainPanel mainPanel) {
        this.PANEL_WIDTH = mainPanel.getMainFrame().getFrameWidth();
        this.mainPanel = mainPanel;

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        initComponents();
        applyTheme();
    }
    
    public void applyTheme() {
        setBackground(ThemeManager.getPanelColor());
        cbPanel.setBackground(ThemeManager.getPanelColor());

        upperPanel.setBackground(ThemeManager.getPanelColor());
        buttonContainer.setBackground(ThemeManager.getPanelColor());
        
        searchField.setBackground(ThemeManager.getButtonColor());
        searchField.setForeground(ThemeManager.getTextColor());

        specificDevice.setBackground(ThemeManager.getCheckboxColor());
        specificDevice.setForeground(ThemeManager.getTextColor());

        clearLogButton.setBackground(ThemeManager.getButtonColor());
        clearLogButton.setForeground(ThemeManager.getTextColor());

        warnButton.setBackground(ThemeManager.getButtonColor());
        warnButton.setForeground(ThemeManager.getTextColor());
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

        // 🔹 Search Field
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(CB_WIDTH, CB_HEIGHT));
        searchField.setFont(CB_FONT);
        setDocumentListener(searchField);
        addSearchHover(searchField); // 👈 add hover to text field

        // 🔹 Combo Box
        specificDeviceModel = new DefaultComboBoxModel<>();
        specificDevice = new JComboBox<>(specificDeviceModel);
        specificDevice.setPreferredSize(new Dimension(CB_WIDTH, CB_HEIGHT));
        specificDevice.setFocusable(false);
        specificDevice.addActionListener(e -> setSorterFunction());
        addDevicesToFilter();

        // 🔹 Add hover effect to ComboBox
        UIUtils.addHoverEffect(specificDevice);

        cbPanel.add(searchField);
        cbPanel.add(specificDevice);
        add(cbPanel);
    }

    private void addDevicesToFilter() {
        Vector<Integer> ids = PCRepository.getAllPcId();
        specificDeviceModel.removeAllElements();
        specificDeviceModel.addElement("All PCs");
        for (int id : ids) {
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
        upperPanel = new JPanel();
        upperPanel.setPreferredSize(new Dimension(PANEL_WIDTH - 400, PANEL_HEIGHT - CB_HEIGHT));
        upperPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        cbPanel.add(upperPanel);
    }

    private void Buttons() {
        buttonContainer = new JPanel();
        buttonContainer.setPreferredSize(new Dimension(PANEL_WIDTH - (PANEL_WIDTH - 300), PANEL_HEIGHT));
        buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, PANEL_HEIGHT - BUTTON_HEIGHT));

        clearLogButton = new JButton("Clear Log");
        clearLogButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        clearLogButton.setFocusable(false);
        clearLogButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to clear all logs?",
                    "Confirm Clear",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                mainPanel.getTablePanel().clearTableData();
            }
        });

        warnButton = new JButton("Warn");
        warnButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        warnButton.setFocusable(false);
        warnButton.addActionListener(e -> mainPanel.getTablePanel().sendWarning());

        // 🔹 Hover highlight effect
        UIUtils.addHoverEffect(clearLogButton);
        UIUtils.addHoverEffect(warnButton);

        buttonContainer.add(clearLogButton);
        buttonContainer.add(warnButton);
        add(buttonContainer, BorderLayout.EAST);
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

    // 🔹 Simple hover for search text field
    private void addSearchHover(JTextField field) {
        Color normal = Color.WHITE;
        Color hover = new Color(230, 240, 255);
        field.setBackground(normal);

        field.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                field.setBackground(hover);
                field.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                field.setBackground(ThemeManager.getButtonColor());
                field.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });
    }
}
