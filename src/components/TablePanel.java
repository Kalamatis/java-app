package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import database.DatabaseManager;
import database.PCRepository;
import handler.CheckWarningHandler;
import windows.LogsPanel;
import windows.MainPanel;

public class TablePanel extends JPanel{
	private int PANEL_WIDTH;
	private int PANEL_HEIGHT;
	
	JTable table;
	DefaultTableModel model;
	
	Vector<String> colName;
	TableRowSorter<TableModel> sorter;
	DatabaseManager db = DatabaseManager.getInstance();
	
	private Map<String, Integer> restrictions;

	public void addTableItem(Vector<Object> item) {
		model.addRow(item);	
	}
	
	public void clearTableData() {
	    model.setRowCount(0);
	}
	
	public TableRowSorter<TableModel> getTableRowSorter() {
		return sorter;
	}
	
	public void sendWarning() {
		int[] selectedRows = table.getSelectedRows();
		 if (selectedRows.length == 0) {
		        JOptionPane.showMessageDialog(this, "Please select at least one PC first.", "No Selection", JOptionPane.WARNING_MESSAGE);
		        return;
		    }
		 
		 // Collect selected PC names for confirmation
		    StringBuilder pcList = new StringBuilder();
		    for (int row : selectedRows) {
		        String pcName = (String) table.getValueAt(row, 1);
		        pcList.append("- ").append(pcName).append("\n");
		    }

		    // Confirm action
		    int confirm = JOptionPane.showConfirmDialog(
		        this,
		        "Send warning to the following PCs?\n\n" + pcList,
		        "Confirm Warning",
		        JOptionPane.YES_NO_OPTION
		    );

		    if (confirm != JOptionPane.YES_OPTION) {
		        return; // user canceled
		    }

		    // Ask for warning message
		    String message = JOptionPane.showInputDialog(
		        this,
		        "Enter warning message for selected PCs:",
		        "Send Warning",
		        JOptionPane.PLAIN_MESSAGE
		    );

		    // Use default message if empty or canceled
		    if (message == null || message.trim().isEmpty()) {
		        message = "⚠️ Warning: Please check your computer. The administrator needs your attention.";
		    }
		    Connection conn = db.getConnection();
		    synchronized(conn) {
		    	try (
			    	     PreparedStatement ps = conn.prepareStatement(
			    	         "INSERT INTO warnings (pc_id, message) VALUES (?, ?)"
			    	     )) {

			    	    for (int row : selectedRows) {
			    	        int pcId = (int) table.getValueAt(row, 0); // still store pc_id
			    	        String messageToSend = message.trim();

			    	        // Fetch UUID for this ID
			    	        String uuid = PCRepository.getUuidById(pcId).orElse("");
			    	        if (!uuid.isEmpty()) {
			    	            CheckWarningHandler.addWarning(uuid, message);
			    	        } else {
			    	            System.out.println("[SERVER] Warning skipped: UUID not found for PC ID " + pcId);
			    	        }

			    	        // Optionally, you could also keep a temporary map for warnings by UUID
			    	        CheckWarningHandler.addWarning(uuid, messageToSend);

			    	        ps.setInt(1, pcId);
			    	        ps.setString(2, messageToSend);
			    	        ps.addBatch();

			    	        System.out.println("[SERVER] Warning queued for PC ID " + pcId + " (UUID=" + uuid + ")");
			    	    }

			    	    ps.executeBatch();
			    	    JOptionPane.showMessageDialog(this,
			    	        "Warning sent to " + selectedRows.length + " PC(s)!",
			    	        "Success",
			    	        JOptionPane.INFORMATION_MESSAGE
			    	    );

			    	} catch (SQLException ex) {
			    	    ex.printStackTrace();
			    	    JOptionPane.showMessageDialog(this,
			    	        "Failed to send warning: " + ex.getMessage(),
			    	        "Error",
			    	        JOptionPane.ERROR_MESSAGE
			    	    );
			    	}
		    }
		    
	}
	
	
	//MainWindow
	public TablePanel(MainPanel mainPanel, Vector<String> colName) {
		this.colName = colName;
		this.PANEL_WIDTH = mainPanel.getMainPanelWidth();
		this.PANEL_HEIGHT = mainPanel.getMainPanelHeight();
		
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		loadRestrictions();
		initComponents();
	}
	
	//Window 3
	public TablePanel(LogsPanel logsPanel, Vector<String> colName) {
		this.colName = colName;

		this.PANEL_WIDTH = logsPanel.getMainFrame().getFrameWidth();
		this.PANEL_HEIGHT = logsPanel.getMainFrame().getFrameHeight();
		
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		loadRestrictions();
		initComponents();
	}
	
	public void loadRestrictions() {
		if(restrictions != null)
			restrictions.clear();
        // Example: Fetch from DB
        restrictions = db.getRestrictedWebsites(); 
        // Implement this method to return Map<String, Integer>
    }
	
	private void initComponents() {
		createTable();
	}
	
	private void setTableSorter() {
		sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);

	}

	private void createTable() {
		
		model = new DefaultTableModel(null, colName){
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // no cell is editable
		    }
		};
		table = new JTable(model)
		{
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                String website = (String) getValueAt(row, 2);
                Integer level = restrictions.get(website);

                if (level != null) {
                    switch (level) {
                        case 3 -> c.setBackground(Color.RED);
                        case 2 -> c.setBackground(Color.YELLOW);
                        default -> c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(Color.WHITE);
                }

                // Ensure text remains visible
                if (isRowSelected(row)) {
                    c.setBackground(c.getBackground().darker());
                }

                return c;
            }
        };
        table.setFillsViewportHeight(true);
		tableLayout();
		setTableSorter();
		
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(PANEL_WIDTH - 100, 500));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		add(scrollPane);
		
		
	}
	
	public void filter(String text) {
		if (text.trim().length() == 0) {
			sorter.setRowFilter(null);
		} else {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // (?i) = ignore case
		}
	}
	
	private void tableLayout() {
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(500);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
	}
	
	private void addCol(Vector<String> colName) {
		
	}
}























