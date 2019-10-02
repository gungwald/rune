package com.alteredmechanism.notepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Cursor;
import java.awt.Desktop;

public class AboutDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String SOURCE_URL = "http://github.com/gungwald/notepad";
	private final JPanel contentPanel = new JPanel();
	private JTable table;
    private JButton okButton;
    private JButton sourceLinkButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
            JFrame frame = new JFrame();
			AboutDialog dialog = new AboutDialog(frame);
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog(JFrame owner) {
        super(owner);
		setTitle("About Notepad");
		setSize(550, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.SOUTH);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
		    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		    getContentPane().add(tabbedPane, BorderLayout.CENTER);
		    {
		        JPanel infoPanel = new JPanel();
		        tabbedPane.addTab("Info", null, infoPanel, null);
		        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		        {
		        	JLabel lblNotepadVersion = new JLabel("Notepad - Version 1.0");
		        	infoPanel.add(lblNotepadVersion);
		        	lblNotepadVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
		        	lblNotepadVersion.setHorizontalAlignment(SwingConstants.CENTER);
		        }
		        {
		        	JLabel lblByBillChatfield = new JLabel("Created by Bill Chatfield");
		        	infoPanel.add(lblByBillChatfield);
		        	lblByBillChatfield.setAlignmentX(Component.CENTER_ALIGNMENT);
		        }
		        {
		        	sourceLinkButton = new JButton("<html><font color=\"#000099\"><u>" + SOURCE_URL + "</u></font></html>");
		        	sourceLinkButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		        	sourceLinkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		        	sourceLinkButton.setBorderPainted(false);
		        	sourceLinkButton.setOpaque(false);
		        	sourceLinkButton.setBackground(Color.WHITE);
		        	sourceLinkButton.addActionListener(this);
		        	infoPanel.add(sourceLinkButton);
		        }
		    }
		    {
		        AntiAliasedJTextArea licenseTextArea = new AntiAliasedJTextArea(80, 30);
		        licenseTextArea.setMargin(new Insets(5,5,5,5));
		        licenseTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		        licenseTextArea.setText(readLicenseFile(licenseTextArea));
		        licenseTextArea.setCaretPosition(0);
		        JScrollPane licenseScroller = new JScrollPane(licenseTextArea);
		        tabbedPane.addTab("License", null, licenseScroller, null);
		    }
		    {
		    	JScrollPane propertiesPane = new JScrollPane();
		    	tabbedPane.addTab("System Properties", null, propertiesPane, null);
		    	{
		    		table = new JTable();
		    		table.setModel(new DefaultTableModel(
                        getSortedSystemProperties(),
		    		    new String[] {
		    		        "Property Name", "Property Value"
		    		    }
		    		));
		    		table.getColumnModel().getColumn(0).setWidth(60);
		    		propertiesPane.setViewportView(table);
		    	}
		    }
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Close");
				okButton.setActionCommand("Close");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(this);
			}
		}
	}
    
	private String readLicenseFile(JTextArea text) {
        BufferedReader reader = null;
        InputStream stream = null;
        StringBuilder s = new StringBuilder();
        try {
            stream = ClassLoader.getSystemClassLoader().getResourceAsStream("LICENSE");
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                s.append(line + "\n");
            }
            text.setCaretPosition(0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            close(reader);
        }
        return s.toString();
	}

	private void close(BufferedReader reader) {
		if (reader != null) {
			try {
				reader.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Object[][] getSortedSystemProperties() {
		int propIndex = 0;
		TreeSet keys = new TreeSet(System.getProperties().keySet());
		String[][] propsArray = new String[keys.size()][2];
        Iterator keysIterator = keys.iterator();
		while (keysIterator.hasNext()) {
            String key = (String) keysIterator.next();
			String value = System.getProperty(key);
			propsArray[propIndex][0] = key;
			propsArray[propIndex][1] = value;
			propIndex++;
		}
		return propsArray;
	}

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            this.setVisible(false);
        } else if (e.getSource() == sourceLinkButton) {
        	if (Desktop.isDesktopSupported()) {
        		try {
        			Desktop.getDesktop().browse(new URI(SOURCE_URL));
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}
        	}
        }
    }

    /**
     * @deprecated - Use the built-in setLocationRelativeTo instead
     */
    public void center() {
        Point centerPoint = new Point();
        Point parentPosition = getOwner().getLocation();
        Dimension parentDimension = getOwner().getSize();
        centerPoint.x = parentPosition.x + parentDimension.width/2 - this.getWidth()/2;
        centerPoint.y = parentPosition.y + parentDimension.height/2 - this.getHeight()/2;
        this.setLocation(centerPoint);
    }
}
