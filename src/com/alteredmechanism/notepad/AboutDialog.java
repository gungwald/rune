package com.alteredmechanism.notepad;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutDialog dialog = new AboutDialog();
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setTitle("About Notepad");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JLabel lblNotepadVersion = new JLabel("Notepad - Version 1.0");
			lblNotepadVersion.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNotepadVersion);
		}
		{
			JLabel lblByBillChatfield = new JLabel("Created by Bill Chatfield");
			contentPanel.add(lblByBillChatfield);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.setModel(new DefaultTableModel(getSystemProperties(),
					new String[] {
						"Property Name", "Property Value"
					}
				));
				table.getColumnModel().getColumn(0).setPreferredWidth(137);
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	private Object[][] getSystemProperties() {
		int propIndex = 0;
		Properties props = System.getProperties();
		String[][] propsArray = new String[props.size()][2];
		Enumeration propsEnum = props.keys();
		while (propsEnum.hasMoreElements()) {
			String name = (String) propsEnum.nextElement();
			String value = props.getProperty(name);
			propsArray[propIndex][0] = name;
			propsArray[propIndex][1] = value;
			propIndex++;
		}
		return propsArray;
	}

}
