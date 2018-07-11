package com.alteredmechanism.notepad;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class LookAndFeelActionListener implements ActionListener {

    private Map lookMap;
    private Component[] componentsToUpdate;

    public LookAndFeelActionListener(Map lookMap, Component[] componentsToUpdate) {
        this.lookMap = lookMap;
        this.componentsToUpdate = componentsToUpdate;
    }

    public void actionPerformed(ActionEvent event) {
        System.out.println(event.paramString());
        try {
            UIManager.setLookAndFeel(((LookAndFeelInfo)lookMap.get(event.getActionCommand())).getClassName());
            for (int i = 0; i < componentsToUpdate.length; i++) {
                Component c = componentsToUpdate[i];
                SwingUtilities.updateComponentTreeUI(c);
                if (c instanceof Window) {
                    ((Window) c).pack();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
