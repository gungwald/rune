package com.alteredmechanism.javax.swing;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import com.alteredmechanism.notepad.Messenger;

public class ImageIconLoader {

    protected int[] sizes = {16,20,24,32,48,64,128,256,512};
    protected Messenger messenger = null;
    
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
}
