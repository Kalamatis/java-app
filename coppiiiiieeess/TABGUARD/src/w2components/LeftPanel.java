package w2components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import components.ThemeManager;
import database.DatabaseManager;
import windows.RestrictedSitesPanel;

public class LeftPanel extends JPanel{
	RestrictedSitesPanel resSitesPanel;
	DatabaseManager dbManager;
	
	JPanel leftPanel, siteLabelPanel, levelLabelPanel;
	
	JLabel siteLabel, levelJLabel, siteJLabel;
	JTextField siteField;
	JComboBox<String> levelBox;
	JButton addButton;
	
	Dimension removeBtnSize = new Dimension(22, 22);
	
	Dimension leftPanelSize = new Dimension(350, 350);
    Dimension fieldSize = new Dimension(200, 28);
    Dimension buttonSize = new Dimension(100, 30);
    Dimension comboSize = new Dimension(80, 28);
    
 // ---- Add Site ----
    public void addRestrictedSite() {
        String site = siteField.getText().trim();
        String level = (String) levelBox.getSelectedItem();

        if (site.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Website cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dbManager.addRestrictedSite(site, level);
        siteField.setText("");
        refreshList();
        resSitesPanel.getMainFrame().getMainPanel().getTablePanel().loadRestrictions();
    }

    // ---- Remove Site ----
    private void removeRestrictedSite(String website) {
        dbManager.removeRestrictedSite(website);
        refreshList();
    }

    // ---- Refresh List ----
    public void refreshList() {
    	resSitesPanel.getRightPanel().removeAll();
        List<String[]> sites = dbManager.getAllRestrictedSites();

        for (String[] entry : sites) {
            String website = entry[0];
            String level = entry[1];

            JPanel restrictedRowPanel = new JPanel();
            restrictedRowPanel.setLayout(new BoxLayout(restrictedRowPanel, BoxLayout.X_AXIS));
            restrictedRowPanel.setPreferredSize(new Dimension(280, 30));
            restrictedRowPanel.setMaximumSize(new Dimension(280, 30));
            restrictedRowPanel.setBackground(ThemeManager.getPanelColor());

            // Label
            JLabel levelLabel = new JLabel(website + " (Level " + level + ")");
            levelLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
            levelLabel.setForeground(ThemeManager.getTextColor());

            // Remove button (fixed size)
            JButton removeBtn = new JButton("x");
            
            removeBtn.setPreferredSize(removeBtnSize);
            removeBtn.setMaximumSize(removeBtnSize);
            removeBtn.setMinimumSize(removeBtnSize);
            removeBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
            removeBtn.addActionListener(e -> removeRestrictedSite(website));
            removeBtn.setBackground(ThemeManager.getButtonColor());
    	    removeBtn.setForeground(ThemeManager.getTextColor());

            // Add components
            restrictedRowPanel.add(Box.createHorizontalStrut(5));  // left padding
            restrictedRowPanel.add(levelLabel);
            restrictedRowPanel.add(Box.createHorizontalGlue());    // push button to the right
            restrictedRowPanel.add(removeBtn);
            restrictedRowPanel.add(Box.createHorizontalStrut(5));  // right padding

            resSitesPanel.getRightPanel().add(restrictedRowPanel);
        }

        resSitesPanel.getRightPanel().revalidate();
        resSitesPanel.getRightPanel().repaint();
        
    }
    
	public LeftPanel(RestrictedSitesPanel resSitesPanel) {
		this.resSitesPanel = resSitesPanel;
		dbManager = DatabaseManager.getInstance();
		
		setLayout(new GridBagLayout());
		initComponents();
		applyTheme();
        
        setPreferredSize(leftPanelSize);
        setMaximumSize(leftPanelSize);
        
	}
	
	public void applyTheme() {
	    setBackground(ThemeManager.getPanelColor());
	    //Panels
	    leftPanel.setBackground(ThemeManager.getPanelColor());
	    leftPanel.setForeground(ThemeManager.getTextColor());
	    siteLabelPanel.setBackground(ThemeManager.getPanelColor());
	    levelLabelPanel.setBackground(ThemeManager.getPanelColor());
//	    restrictedRowPanel.setBackground(ThemeManager.getPanelColor());
	    
	    //JLabels
	    levelJLabel.setForeground(ThemeManager.getTextColor());
	    siteJLabel.setForeground(ThemeManager.getTextColor());
	    
	    //Text Field
	    siteField.setBackground(ThemeManager.getButtonColor());
	    
	    //Buttons
	    
	    addButton.setBackground(ThemeManager.getButtonColor());
	    addButton.setForeground(ThemeManager.getTextColor());
	    
	    //ComboBox
	    levelBox.setBackground(ThemeManager.getCheckboxColor());
	    levelBox.setForeground(ThemeManager.getTextColor());
	   
	    
	 // ✅ change border title color
	    if (leftPanel.getBorder() instanceof javax.swing.border.TitledBorder titledBorder) {
	        titledBorder.setTitleColor(ThemeManager.getTextColor());
	    }

	    // refresh UI
	    leftPanel.repaint();
	    
	}
	
	private void initComponents(){
		createLeftPanel();
	}
	
	private void createLeftPanel() {
		leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Add Restricted Site"));
        leftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        createSiteField();
        createLabels();
        createBoxNButton();
        
        leftPanel.add(siteLabelPanel);
        leftPanel.add(siteField);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(levelLabelPanel);
        leftPanel.add(levelBox);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(addButton);
        add(leftPanel);
	}
	
	private void createBoxNButton() {
		levelBox = new JComboBox<>(new String[]{"1", "2", "3"});
        levelBox.setMaximumSize(comboSize);
        levelBox.setPreferredSize(comboSize);
        levelBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton = new JButton("Add");
        addButton.setMaximumSize(buttonSize);
        addButton.setPreferredSize(buttonSize);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> addRestrictedSite());
	}
	
	private void createLabels() {
		siteJLabel = new JLabel("Website:");
        levelJLabel = new JLabel("Level:");
		
		siteLabelPanel = new JPanel();
        siteLabelPanel.setLayout(new BoxLayout(siteLabelPanel, BoxLayout.X_AXIS));
        siteLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        siteLabelPanel.add(siteJLabel);
        siteLabelPanel.add(Box.createHorizontalGlue());

        levelLabelPanel = new JPanel();
        levelLabelPanel.setLayout(new BoxLayout(levelLabelPanel, BoxLayout.X_AXIS));
        levelLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelLabelPanel.add(levelJLabel);
        levelLabelPanel.add(Box.createHorizontalGlue());
        
	}
	
	private void createSiteField() {
		siteField = new JTextField();
        siteField.setMaximumSize(fieldSize);
        siteField.setPreferredSize(fieldSize);
        siteField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
	}
	
}
