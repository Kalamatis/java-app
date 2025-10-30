package windows;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.*;

import components.TablePanel;
import components.ThemeManager;
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

    private JPanel cardsContainer, tablePanelContainer, backButtonPanel;
    private JButton backButton;
    
    private JScrollPane scrollPane,tableScrollPane;
    
    private CardLayout cardLayout;

    public LogsPanel(MainFrame frame) {
        this.frame = frame;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        initComponents();
        cardLayout.show(this, "CardsPanel");
        applyTheme();
    }
    public void applyTheme() {
        setBackground(ThemeManager.getPanelColor());
        tablePanelContainer.setBackground(ThemeManager.getPanelColor());
        cardsContainer.setBackground(ThemeManager.getPanelColor());
        backButtonPanel.setBackground(ThemeManager.getPanelColor());
//        tableScrollPane.setBackground(ThemeManager.getPanelColor());
        tablePanel.applyTheme();
        
        backButton.setBackground(ThemeManager.getButtonColor());
        backButton.setForeground(ThemeManager.getTextColor());
        
        
        refreshCards();
        
    }
    private void initComponents() {
        createCardsContainer();
        createTablePanel();
    }

    private void createFilterPanel() {
        filterPanel = new W3FilterPanel(this);
        tablePanelContainer.add(filterPanel, BorderLayout.NORTH);
    }

    private void createBackButton() {
        backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 10));
        backButtonPanel.setOpaque(false);
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(e -> cardLayout.show(this, "CardsPanel"));
        backButtonPanel.add(backButton);
        tablePanelContainer.add(backButtonPanel, BorderLayout.NORTH);
    }

    private void createTablePanel() {
        tablePanelContainer = new JPanel(new BorderLayout());
        tablePanel = new TablePanel(this, setColName());
        createBackButton();

        tablePanelContainer.add(tablePanel, BorderLayout.CENTER);
        add(tablePanelContainer, "TablePanel");
    }

    private Vector<String> setColName() {
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
        cardsContainer = new JPanel(new WrapLayout(FlowLayout.LEADING, 15, 15));
        

        scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // 🔹 Make sure the cards resize properly
        cardsContainer.setPreferredSize(null);
        

        refreshCards();
        add(scrollPane, "CardsPanel");
    }

    public void setTableValues(int id) {
        Vector<Vector<Object>> values = SessionHandler.getTabEventsBySession(id);
        tablePanel.clearTableData();
        for (Vector<Object> items : values) {
            tablePanel.addTableItem(items);
        }
    }

    public void refreshCards() {
        updateList();
        cardsContainer.removeAll();

        if (sessions == null || sessions.isEmpty()) {
            JLabel emptyLabel = new JLabel("No sessions available.");
            emptyLabel.setForeground(Color.GRAY);
            cardsContainer.add(emptyLabel);
        } else {
            for (Session session : sessions) {
                LogsCard card = new LogsCard(session.getId(), session.getDate(), session.getTime());
                
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showTablePanel();
                        setTableValues(session.getId());
                    }
                });
                
                cardsContainer.add(card);
            }
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
