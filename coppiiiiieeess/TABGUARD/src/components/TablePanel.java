package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.Cursor;


import java.util.Map;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import database.DatabaseManager;
import database.PCRepository;
import handler.CheckWarningHandler;
import windows.LogsPanel;
import windows.MainPanel;
import javax.swing.JComponent;

public class TablePanel extends JPanel{
	private int PANEL_WIDTH;
	private int PANEL_HEIGHT;
	
	JTable table;
	DefaultTableModel model;
	JScrollPane scrollPane;
	Vector<String> colName = new Vector<>();
	TableRowSorter<TableModel> sorter;
	DatabaseManager db = DatabaseManager.getInstance();
	
	private Map<String, Integer> restrictions;
	
	private int hoveredRow = -1;
	
	

	public void addTableItem(Vector<Object> item) {
		model.addRow(item);
	}
	
	public void clearTableData() {
	    model.setRowCount(0);
	}
	
	public TableRowSorter<TableModel> getTableRowSorter() {
		return sorter;
	}
	
	public void saveTableToDatabase(int sessionId) {
	    Connection conn = db.getConnection();
	    synchronized (conn) {
	        try (PreparedStatement ps = conn.prepareStatement(
	            "INSERT INTO tab_events (pc_id, session_id, tab_url, title, event_type, timestamp) VALUES (?, ?, ?, ?, ?, ?)"
	        )) {
	            for (int i = 0; i < model.getRowCount(); i++) {
	                int pcId = Integer.parseInt(model.getValueAt(i, 0).toString()); // column 0 = pc_id
	                String title = model.getValueAt(i, 1).toString();
	                String url = model.getValueAt(i, 2).toString();
	                String timestamp = model.getValueAt(i, 3).toString();
	                String eventType = "recorded"; // or "switch", depending on your logic

	                ps.setInt(1, pcId);
	                ps.setInt(2, sessionId);
	                ps.setString(3, url);
	                ps.setString(4, title);
	                ps.setString(5, eventType);
	                ps.setString(6, timestamp);
	                ps.addBatch();
	            }

	            ps.executeBatch();
	            System.out.println("✅ TablePanel data saved to tab_events successfully!");
	        } catch (SQLException e) {
	            System.err.println("❌ Failed to save TablePanel data: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
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
			applyTheme();
		}
		
	//Window 3
		public TablePanel(LogsPanel logsPanel, Vector<String> colName) {
			this.colName = colName;

			this.PANEL_WIDTH = logsPanel.getMainFrame().getFrameWidth();
			this.PANEL_HEIGHT = logsPanel.getMainFrame().getFrameHeight();
			
			setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
			loadRestrictions();
			initComponents();
			applyTheme();
		}
		
		public void applyTheme() {
		    setBackground(ThemeManager.getPanelColor());
		    table.setBackground(ThemeManager.getTableBackground());
		    table.setForeground(ThemeManager.getTableTextColor());
		    table.getTableHeader().setBackground(ThemeManager.getTableHeaderColor());
		    table.getTableHeader().setForeground(ThemeManager.getTableHeaderTextColor());
		    scrollPane.setBackground(ThemeManager.getPanelColor());
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
	    model = new DefaultTableModel(null, colName) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false; // no cell is editable
	        }
	    };

	    table = new JTable(model) {
	        @Override
	        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	            Component c = super.prepareRenderer(renderer, row, column);
	            ((JComponent) c).setOpaque(true);

	            String website = (String) getValueAt(row, 2);
	            if (website != null) {
	                website = website.replaceAll("https?://", "")
	                                 .replaceAll("www\\.", "")
	                                 .toLowerCase();
	            }

	            // Restriction check
	            Integer level = null;
	            for (String restrictedSite : restrictions.keySet()) {
	                if (website != null && website.contains(restrictedSite.toLowerCase())) {
	                    level = restrictions.get(restrictedSite);
	                    break;
	                }
	            }

	            // Default text color
	            c.setForeground(ThemeManager.getTableTextColor());

	            if (level != null) {
	                switch (level) {
	                    case 1 -> c.setBackground(ThemeManager.getRowYellowColor()); // Yellow
	                    case 2 -> c.setBackground(ThemeManager.getRowOrangeColor()); // Orange
	                    case 3 -> {
	                        c.setBackground(ThemeManager.getRowRedColor()); // Red
	                        c.setForeground(Color.WHITE);
	                    }
	                    default -> c.setBackground(ThemeManager.getRowDefaultColor());
	                }
	            } else {
	                c.setBackground(ThemeManager.getRowDefaultColor()); // Blue default
	            }

	            // Darker when selected
	            if (isRowSelected(row)) {
	                c.setBackground(c.getBackground().darker());
	            }

	            // Darker when hovering
	            if (row == hoveredRow) {
	                c.setBackground(c.getBackground().darker());
	            }

	            return c;
	        }
	    };

	    // 👇 Hover + hand cursor logic
	    table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
	        @Override
	        public void mouseMoved(java.awt.event.MouseEvent e) {
	            int row = table.rowAtPoint(e.getPoint());
	            if (row != hoveredRow) {
	                hoveredRow = row;
	                table.repaint();
	            }
	            table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        }
	    });

	    table.addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override
	        public void mouseExited(java.awt.event.MouseEvent e) {
	            hoveredRow = -1;
	            table.repaint();
	            table.setCursor(Cursor.getDefaultCursor());
	        }
	    });

	    table.setFillsViewportHeight(true);
	    tableLayout();
	    setTableSorter();

	    scrollPane = new JScrollPane(table);
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
	
	
}























