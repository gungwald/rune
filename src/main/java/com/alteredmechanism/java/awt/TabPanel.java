package com.alteredmechanism.java.awt;

// (c)1996 JRad Technologies
// Free to use, can't redistribute for profit
//
// Original by Pete Yared.
// Modified by Jean-Guy Speton (speton@cs.orst.edu).
// Version 1.0.
// * Changed name to TabPanel to differentiate modification tree
// from original.
// * JDK 1.1 API compliant.
// * CardLayout used to simplify implementation.
// * Added getMinimumSize() and getPreferredSize().
// * Tab Font stored in state and created only once.
// * Tab in index 0 now default selected.
// * No longer repaints when user reselects selected tab.
// Still free to use, still can't redistribute for profit.

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class TabPanel extends Panel implements MouseListener {
    private static final long serialVersionUID = 1L;
    protected Vector labels = new Vector();
    protected Vector panels = new Vector();

    protected Panel cardPanel = new Panel();

    protected int[] tabWidths;
    protected String selectedPanel;

    protected Font tabFont = new Font("Dialog", 12, Font.PLAIN);

    protected Insets insets;
    protected int tabHeight = 21;
    protected int tabWidthBuffer = 13;

    protected int insetPadding = 14;

    public TabPanel() {
        insets = new Insets(insetPadding + tabHeight, insetPadding, insetPadding, insetPadding);
        setLayout(null);
        addMouseListener(this);

        cardPanel.setLayout(new CardLayout());
        add(cardPanel);
    }

    //
    // MouseListener interface.
    //

    public void mousePressed(MouseEvent event) {
        if (event.getY() < tabHeight) {
            int xLoc = 0;
            for (int i = 0; i < labels.size(); i++) {
                xLoc += tabWidths[i];
                if (event.getX() < xLoc) {
                    setPanel((String) labels.elementAt(i));
                    return;
                }
            }
        }
    }

    public void mouseReleased(MouseEvent event) {}

    public void mouseEntered(MouseEvent event) {}

    public void mouseExited(MouseEvent event) {}

    public void mouseClicked(MouseEvent event) {}

    //
    // Methods.
    //

    public Dimension getMinimumSize() {
        int w = 0;
        int h = 0;

        FontMetrics metrics = getFontMetrics(tabFont);
        for (int i = 0; i < labels.size(); i++) {
            w += metrics.stringWidth((String) labels.elementAt(i)) + tabWidthBuffer;
        }

        for (int i = 0; i < panels.size(); i++) {
            Dimension d = ((Panel) panels.elementAt(i)).getMinimumSize();
            w = Math.max(w, d.width + insets.left + insets.right);
            h = Math.max(h, d.height);
        }

        h += insets.top + insets.bottom;

        return new Dimension(w, h);
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public void setBounds(int nx, int ny, int nw, int nh) {
        cardPanel.setBounds(insets.left, insets.top, nw - insets.left - insets.right, nh - insets.top - insets.bottom);
        super.setBounds(nx, ny, nw, nh);
    }

    public void addPanel(Panel panel, String label) {
        labels.addElement(label);
        panels.addElement(panel);
        cardPanel.add(label, panel);

        if (panels.size() == 1) {
            selectedPanel = label;
        }
    }

    public void removePanel(String label) {
        int index = labels.indexOf(label);
        if (index == -1) {
            throw new IllegalArgumentException(label + " is not a panel in this TabPanel");
        }
        labels.removeElementAt(index);
        panels.removeElementAt(index);
        if (label.equals(selectedPanel)) {
            if (labels.size() > 0)
                setPanel((String) labels.elementAt(0));
            else
                selectedPanel = null;
        }
    }

    public void setPanel(String label) {
        if (label.equals(selectedPanel)) {
            return;
        }

        selectedPanel = label;
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, label);

        repaint();
    }

    public String getSelectedPanelName() {
        return this.selectedPanel;
    }

    public Panel getSelectedPanel() {
        return (Panel) panels.elementAt(labels.indexOf(selectedPanel));
    }

    public Insets getInsets() {
        return insets;
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    public int getTabHeight() {
        return this.tabHeight;
    }

    public void setTabHeight(int tabHeight) {
        this.tabHeight = tabHeight;
    }

    public int getTabWidthBuffer() {
        return this.tabWidthBuffer;
    }

    public void setTabWidthBuffer(int tabWidthBuffer) {
        this.tabWidthBuffer = tabWidthBuffer;
    }

    public void paint(Graphics g) {
        super.paint(g);

        int top = tabHeight - 1;
        int left = 0;
        int bottom = getSize().height - 1;
        int right = left + getSize().width - 1;

        g.setColor(Color.darkGray);
        g.drawLine(left, bottom, right - 1, bottom); // Bottom left to bottom right
        g.drawLine(right, top, right, bottom); // Topright to bottomright

        g.setColor(Color.gray);
        g.drawLine(left + 1, bottom - 1, right - 2, bottom - 1); // Bottom left to bottom right
        g.drawLine(right - 1, top + 1, right - 1, bottom - 1); // Topright to bottomright

        g.setColor(Color.white);
        g.drawLine(left, top, left, bottom - 1); // Topleft to bottomleft

        tabWidths = new int[labels.size()];
        int selected = -1;
        int selectedLoc = 0;
        int xLoc = 2;
        for (int i = 0; i < labels.size(); i++) {
            String label = (String) labels.elementAt(i);
            FontMetrics metrics = getFontMetrics(tabFont);
            tabWidths[i] = metrics.stringWidth(label) + tabWidthBuffer;
            if (labels.elementAt(i).equals(selectedPanel)) {
                selected = i;
                selectedLoc = xLoc;
            } else {
                paintTab(g, false, xLoc, 0, tabWidths[i], top, label);
            }
            xLoc += tabWidths[i] - 1;
        }
        if (selected > -1) {
            paintTab(g, true, selectedLoc, 0, tabWidths[selected], top, (String) labels.elementAt(selected));
            g.setColor(Color.white);
            g.drawLine(left, top, selectedLoc - 2, top); // Topleft to topright - left side
            g.drawLine(selectedLoc + tabWidths[selected] + 2, top, right - 1, top); // Topleft to
                                                                                    // topright -
                                                                                    // right side
        } else {
            g.setColor(Color.white);
            g.drawLine(left, top, right - 1, top); // Topleft to topright
        }
    }

    private void paintTab(Graphics g, boolean selected, int x, int y, int width, int height, String label) {
        int left = x;
        int top = y + 2;
        int right = x + width - 1;
        int bottom = y + height - 1;

        height -= 2;

        if (selected) {
            top -= 2;
            left -= 2;
            right += 2;
            bottom += 1;
        }

        g.setColor(Color.darkGray);
        g.drawLine(right - 1, top + 2, right - 1, bottom); // Topright to bottomright
        g.drawRect(right - 2, top + 1, 0, 0); // Topright corner

        g.setColor(Color.gray);
        g.drawLine(right - 2, top + 2, right - 2, bottom); // Topright to bottomright

        g.setColor(Color.white);
        g.drawLine(left, top + 2, left, bottom); // Topleft to bottomleft
        g.drawLine(left + 2, top, right - 3, top); // Topleft to topright
        g.drawRect(left + 1, top + 1, 0, 0); // Topleft corner

        g.setColor(Color.black);

        FontMetrics metrics = g.getFontMetrics(tabFont);
        g.setFont(tabFont);
        g.drawString(label, x + ((width - metrics.stringWidth(label)) / 2), metrics.getHeight() + top + 1);

        if (selected) {
            g.setColor(getBackground());
            g.drawLine(left + 1, bottom, right - 3, bottom); // Bottomleft to bottomright
            g.drawLine(left + 1, top + 3, left + 1, bottom); // Topleft to bottomleft indented by
                                                             // one
        }
    }
}
