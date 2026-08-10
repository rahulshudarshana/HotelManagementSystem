package com.hotelmanagement.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;

public final class UIUtils {

    public static final Color ACCENT = new Color(0x2F6FED);
    public static final Color ACCENT_HOVER = new Color(0x1B5FD7);
    public static final Color DANGER = new Color(0xE5533C);
    public static final Color WARNING = new Color(0xD98300);
    public static final Color SUCCESS = new Color(0x2DA44E);
    public static final Color GHOST = new Color(0xE7EAEE);
    public static final Color TEXT = new Color(0x1C1C1C);
    public static final Color TEXT_MUTED = new Color(0x6B7280);
    public static final Color PANEL_BG = new Color(0xF2F4F7);
    public static final Color TABLE_HEADER = new Color(0xF0F2F5);

    private UIUtils() {
    }

    public static Font defaultFont() {
        Font f = UIManager.getFont("defaultFont");
        return f != null ? f : new Font("Segoe UI", Font.PLAIN, 14);
    }

    public static void applyTheme(Container root) {
        if (root == null) {
            return;
        }
        if (root instanceof javax.swing.JPanel) {
            root.setBackground(UIManager.getColor("Panel.background"));
        }
        applyThemeToContainer(root);
    }

    private static void applyThemeToContainer(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                styleButton((JButton) comp);
            } else if (comp instanceof JLabel) {
                styleLabel((JLabel) comp);
            } else if (comp instanceof javax.swing.JTextField
                    || comp instanceof javax.swing.JPasswordField
                    || comp instanceof javax.swing.JFormattedTextField
                    || comp instanceof javax.swing.JTextArea
                    || comp instanceof JComboBox
                    || comp instanceof javax.swing.JSpinner
                    || comp instanceof javax.swing.JCheckBox
                    || comp instanceof javax.swing.JRadioButton) {
                comp.setFont(defaultFont());
            } else if (comp instanceof JTable) {
                styleTable((JTable) comp);
            } else if (comp instanceof javax.swing.JPanel) {
                comp.setBackground(UIManager.getColor("Panel.background"));
            }
            if (comp instanceof Container) {
                applyThemeToContainer((Container) comp);
            }
        }
    }

    private static void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        String text = b.getText();
        if (text == null || text.isEmpty()) {
            return;
        }
        String t = text.toLowerCase();
        if (t.contains("delete") || t.contains("cancel") || t.contains("remove")) {
            paintButton(b, DANGER, Color.WHITE);
        } else if (t.contains("clear") || t.contains("back") || t.contains("close")
                || t.contains("logout") || t.contains("reset")) {
            paintGhost(b);
        } else {
            paintButton(b, ACCENT, Color.WHITE);
        }
    }

    private static void paintButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setMargin(new Insets(8, 18, 8, 18));
        b.setFont(defaultFont().deriveFont(Font.BOLD));
    }

    private static void paintGhost(JButton b) {
        b.setBackground(GHOST);
        b.setForeground(TEXT);
        b.setMargin(new Insets(8, 18, 8, 18));
        b.setFont(defaultFont().deriveFont(Font.BOLD));
    }

    private static void styleLabel(JLabel label) {
        Font f = label.getFont();
        if (f != null && f.getSize() >= 18) {
            label.setFont(f.deriveFont(Font.BOLD, Math.max(f.getSize(), 20)));
            label.setForeground(TEXT);
        } else {
            label.setFont(defaultFont());
            label.setForeground(UIManager.getColor("Label.foreground"));
        }
    }

    private static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(0xE5E7EB));
        table.setSelectionBackground(UIManager.getColor("Table.selectionBackground"));
        table.setSelectionForeground(TEXT);
        table.setBackground(UIManager.getColor("Table.background"));
        table.setForeground(TEXT);
        table.setFillsViewportHeight(true);
        if (table.getTableHeader() != null) {
            table.getTableHeader().setFont(defaultFont().deriveFont(Font.BOLD));
            table.getTableHeader().setBackground(TABLE_HEADER);
            table.getTableHeader().setForeground(TEXT);
            table.getTableHeader().setReorderingAllowed(false);
        }
    }
}
