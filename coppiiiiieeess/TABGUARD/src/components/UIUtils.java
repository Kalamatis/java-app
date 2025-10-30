package components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class UIUtils {

    // 🔵 Button hover
    public static void addHoverEffect(JButton button) {

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ThemeManager.getButtonHoverColor());
                button.setForeground(ThemeManager.getTextColor());
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ThemeManager.getButtonColor());
                button.setForeground(ThemeManager.getTextColor());
                button.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    // 🖱️ ComboBox hover
    public static void addHoverEffect(JComboBox<?> comboBox) {

        comboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                comboBox.setBackground(ThemeManager.getCheckboxHoverColor());
                comboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                comboBox.setBackground(ThemeManager.getCheckboxColor());
                comboBox.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    // 📋 Panel hover
    public static void addHoverEffect(JPanel panel) {
        Color normal = panel.getBackground();
        Color hover = new Color(220, 235, 250);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(hover);
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(normal);
                panel.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    // 🧾 JTable hover — row highlight + hand cursor
    public static void addHoverEffect(JTable table) {
        final Color hoverColor = new Color(200, 220, 255); // darker blue highlight
        final Color evenRow = Color.WHITE;
        final Color oddRow = new Color(245, 245, 245);
        final Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        final Cursor defaultCursor = Cursor.getDefaultCursor();

        // variable to track hovered row
        final int[] hoveredRow = { -1 };

        // set basic table style
        table.setShowGrid(false);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(25, 118, 210));
        table.setSelectionForeground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        // mouse motion tracking
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != hoveredRow[0]) {
                    hoveredRow[0] = row;
                    table.repaint();
                }
                table.setCursor(row >= 0 ? handCursor : defaultCursor);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow[0] = -1;
                table.setCursor(defaultCursor);
                table.repaint();
            }
        });

        // custom renderer wrapper
        TableCellRenderer baseRenderer = table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class, (tbl, value, isSelected, hasFocus, row, col) -> {
            Component c = baseRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

            Color base = (row % 2 == 0) ? evenRow : oddRow;
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            } else if (row == hoveredRow[0]) {
                c.setBackground(hoverColor);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(base);
                c.setForeground(Color.BLACK);
            }
            return c;
        });
    }
}
