package com.alteredmechanism.notepad;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AboutDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
    private JButton okButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
            JFrame frame = new JFrame();
			AboutDialog dialog = new AboutDialog(frame, new LookAndFeelManager(new Messenger(frame)));
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog(JFrame owner, LookAndFeelManager lookAndFeelManager) {
        super(owner);
		lookAndFeelManager.getLafActionListener().addComponentToUpdate(this);
		setTitle("About Notepad");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JLabel lblNotepadVersion = new JLabel("Notepad - Version 1.0");
			lblNotepadVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblNotepadVersion.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNotepadVersion);
		}
		{
			JLabel lblByBillChatfield = new JLabel("Created by Bill Chatfield");
			lblByBillChatfield.setAlignmentX(Component.CENTER_ALIGNMENT);
			contentPanel.add(lblByBillChatfield);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.setModel(new DefaultTableModel(
                        getSortedSystemProperties(),
				    new String[] {
				        "Property Name", "Property Value"
				    }
				));
				table.getColumnModel().getColumn(0).setPreferredWidth(80);
				scrollPane.setViewportView(table);
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
            this.hide();
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
