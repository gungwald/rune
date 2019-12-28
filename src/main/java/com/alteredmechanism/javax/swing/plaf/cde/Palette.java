package com.alteredmechanism.javax.swing.plaf.cde;

/**
 * Represents a Common Desktop Environment (CDE) color palette.
 * The 8 lines in a palette file like Cabernet.dp are as follows:
 * <ol>
 * <li>Active Window Border</li>
 * <li>Inactive Window Border</li>
 * <li>Background for workspace switcher One. Also the backdrop becomes this color as well.</li>
 * <li>Background for text entries.</li>
 * <li>Background for applications.</li>
 * <li>Background for Menus, panes, workspace switcher Three and Dtterm background.</li>
 * <li>Background for workspace Four.</li>
 * <li>Front Panel background, workspace switcher Two and Dtfile background.</li>
 * </ol>
 * @author bill.chatfield
 *
 * @see <a href="https://sourceforge.net/p/cdesktopenv/wiki/Creating%20A%20Palette/">Definition</a>
 */
public class Palette {

    private String activeWindowBorder;
    private String inactiveWindowBorder;
    private String backdrop;
    private String textEntryBackground;
    private String applicationBackground;
    private String menuAndPaneBackground;
    private String workspaceFourBackground;
    private String frontPanelBackground;
    
    public Palette() {
        // TODO Auto-generated constructor stub
    }

}
