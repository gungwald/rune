package com.alteredmechanism.javax.swing;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.alteredmechanism.notepad.Messenger;

public class ImageIconLoader {

    protected int[] sizes = {16,20,24,32,48,64,128,256,512};
    protected Messenger messenger = null;
    
    private Map toolbarIcons = new HashMap();
    
    public ImageIconLoader(Messenger messenger) {
        this.messenger = messenger;
    }
    
    public List loadAll(String baseName) {
        List icons = new ArrayList();
        StringBuffer name = new StringBuffer();
        boolean foundOneIcon = false;
        List missingIcons = new ArrayList();
        for (int i=0; i < sizes.length; i++) {
            int size = sizes[i];
            name.setLength(0);
            name.append("icons/");
            name.append(baseName);
            name.append('-');
            name.append(size);
            name.append('x');
            name.append(size);
            name.append(".png");
            
            URL iconLocation = getClass().getClassLoader().getResource(name.toString());
            if (iconLocation == null) {
                missingIcons.add(name.toString());
            }
            else {
                foundOneIcon = true;
                ImageIcon icon = new ImageIcon(iconLocation);
                icons.add(icon.getImage());
            }
        }
        if (! foundOneIcon) {
            messenger.showError("Resource not found: " + missingIcons.get(0));
        }
        return icons;
    }
    
    public Icon getToolbarIcon(String name) throws FileNotFoundException {
        Icon iConeOfSilence = null;
        if (toolbarIcons.containsKey(name)) {
            iConeOfSilence = (Icon) toolbarIcons.get(name);
        } else {
            URL url = getClass().getClassLoader().getResource("toolbarButtonGraphics/general/" + name + "16.gif");
            if (url == null) {
                throw new FileNotFoundException(name);
            } else {
                iConeOfSilence = new ImageIcon(url);
            }
        }
        return iConeOfSilence;
    }
    
    public Icon getNewIcon() throws FileNotFoundException {
        return getToolbarIcon("New");
    }
    
    public Icon getOpenIcon() throws FileNotFoundException {
        return getToolbarIcon("Open");
    }
    
    public Icon getSaveIcon() throws FileNotFoundException {
        return getToolbarIcon("Save");
    }
    
    public Icon getSaveAsIcon() throws FileNotFoundException {
        return getToolbarIcon("SaveAs");
    }

    public Icon getCloseIcon() throws FileNotFoundException {
        return getToolbarIcon("Stop");
    }
    
    public Icon getExitIcon() throws FileNotFoundException {
        return getToolbarIcon("Exit");
    }
    
    public Icon getCutIcon() throws FileNotFoundException {
        return getToolbarIcon("Cut");
    }
    
    public Icon getCopyIcon() throws FileNotFoundException {
        return getToolbarIcon("Copy");
    }
    
    public Icon getPasteIcon() throws FileNotFoundException {
        return getToolbarIcon("Paste");
    }
    
    public Icon getPreferencesIcon() throws FileNotFoundException {
        return getToolbarIcon("Preferences");
    }
    
}
