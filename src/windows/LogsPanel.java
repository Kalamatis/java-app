package windows;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.*;

import components.TablePanel;
import database.Session;
import database.SessionHandler;
import main.MainFrame;
import main.WrapLayout;
import w3components.LogsCard;
import w3components.W3FilterPanel;

public class LogsPanel extends JPanel {
    private List<Session> sessions;
    
    
    private MainFrame frame;
    private TablePanel tablePanel;
    private W3FilterPanel filterPanel;
    
    private JScrollPane scrollPane;
    private JPanel cardsContainer, tablePanelContainer;
    private CardLayout cardLayout;

    
    public LogsPanel(MainFrame frame) {
        this.frame = frame;
        
        cardLayout = new CardLayout();
        
        
        
        setLayout(cardLayout);
        setBackground(Color.green);
        initComponents();
    }

    private void initComponents() {
    	createCardsContainer();
    	createTablePanel();
    }

    private void createFilterPanel() {
    	filterPanel = new W3FilterPanel(this);
    	
    	tablePanelContainer.add(filterPanel);
    }
    
    private void createBackButton() {
    	JPanel backButtonPanel = new JPanel();
    	backButtonPanel.setPreferredSize(new Dimension(frame.getFrameWidth(), 50));
    	backButtonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 0));
    	
    	JButton backButton = new JButton("Back");
    	backButton.addActionListener(e -> cardLayout.show(this, "CardsPanel"));
    	backButton.setPreferredSize(new Dimension(100, 50));
    	
    	backButtonPanel.add(backButton);
    	tablePanelContainer.add(backButtonPanel);
    }
    
    private void createTablePanel() {
    	tablePanelContainer = new JPanel();
    	tablePanel = new TablePanel(this, setColName());
    	
    	tablePanelContainer.setPreferredSize(new Dimension(frame.getFrameWidth(), frame.getFrameHeight()));
    	
    	createBackButton();
    	tablePanelContainer.add(tablePanel);
    	
    	add(tablePanelContainer, "TablePanel");
    }
    
    private Vector<String> setColName(){
		Vector<String> vec = new Vector<>();
		vec.add("ID");
		vec.add("Name");
		vec.add("Website Url");
		vec.add("Timestamp");
		return vec;
	}
    
    private void showTablePanel() {
    	cardLayout.show(this, "TablePanel");
    	
    }
    
    private void createCardsContainer() {
        // FlowLayout with LEADING alignment and gaps between cards
    	cardsContainer = new JPanel(new WrapLayout(FlowLayout.LEADING, 15, 15));
//        cardsContainer.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        refreshCards();
        add(scrollPane, "CardsPanel");
    }
    
    public void setTableValues(int id) {
    	Vector<Vector<Object>> values = SessionHandler.getTabEventsBySession(id);
    	tablePanel.clearTableData();
    	
    	for(Vector<Object> items : values) {
    		tablePanel.addTableItem(items);
    	}
    }
    
    public void refreshCards() {
        updateList();
        cardsContainer.removeAll();

        for (Session session : sessions) {
            LogsCard card = new LogsCard(session.getId(), session.getDate(), session.getTime());
            card.setPreferredSize(new Dimension(180, 200)); // ← fixed card size
            
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	showTablePanel();
                	setTableValues(session.getId());
                	
                }
            });
            
            cardsContainer.add(card);
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private void updateList() {
        sessions = SessionHandler.getAllSessions();
    }
    
    public MainFrame getMainFrame() {
    	return frame;
    }
}
