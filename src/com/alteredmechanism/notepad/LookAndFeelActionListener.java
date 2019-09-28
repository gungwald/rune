package com.alteredmechanism.notepad;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class LookAndFeelActionListener implements ActionListener {

    private Map lookMap;
    private List componentsToUpdate;
    private Messenger messenger;

    public LookAndFeelActionListener(Map lookMap, Component[] componentsToUpdate, Messenger messenger) {
        this.lookMap = lookMap;
        this.componentsToUpdate = new ArrayList(Arrays.asList(componentsToUpdate));
        this.messenger = messenger;
    }
    
    public void addComponentToUpdate(Component c) {
    	componentsToUpdate.add(c);
    }

    public void actionPerformed(ActionEvent event) {
        System.out.println(event.paramString());
        try {
            UIManager.setLookAndFeel(((LookAndFeelInfo)lookMap.get(event.getActionCommand())).getClassName());
            for (int i = 0; i < componentsToUpdate.size(); i++) {
                Component c = (Component) componentsToUpdate.get(i);
                SwingUtilities.updateComponentTreeUI(c);
                if (c instanceof Window) {
                    ((Window) c).pack();
                }
            }
        }
        catch (Exception e) {
        	messenger.showError(e);
        }
    }
}
