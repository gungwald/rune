package com.alteredmechanism.rune;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageBox extends Dialog implements ActionListener {
    private static final long serialVersionUID = 1L;

    private Button ok, can;

    public boolean isOk = false;

    /*
     * @param frame parent frame @param msg message to be displayed @param okcan
     * true : ok cancel buttons, false : ok button only
     */
    public MessageBox(Frame frame, String title, String msg, boolean okcan) {
        super(frame, title, true);
        setLayout(new BorderLayout());
        add("Center", new Label(msg));
        addOKCancelPanel(okcan);
        createFrame();
        pack();
        setVisible(true);
    }

    public MessageBox(Frame frame, String title, String message) {
        this(frame, title, message, false);
    }
    
    public MessageBox(Frame frame, String msg) {
        this(frame, "Message", msg, false);
    }

    void addOKCancelPanel(boolean okcan) {
        Panel p = new Panel();
        p.setLayout(new FlowLayout());
        createOKButton(p);
        if (okcan == true)
            createCancelButton(p);
        add("South", p);
    }

    void createOKButton(Panel p) {
        p.add(ok = new Button("OK"));
        ok.addActionListener(this);
    }

    void createCancelButton(Panel p) {
        p.add(can = new Button("Cancel"));
        can.addActionListener(this);
    }

    void createFrame() {
        Dimension d = getToolkit().getScreenSize();
        setLocation(d.width / 3, d.height / 3);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == ok) {
            isOk = true;
            setVisible(false);
        }
        else if (ae.getSource() == can) {
            setVisible(false);
        }
    }

    public static void blah(String args[]) {
        Frame f = new Frame();
        f.setSize(200, 200);
        f.setVisible(true);
        MessageBox message = new MessageBox(f, "Error", "Hey you user, are you sure ?", true);

        if (message.isOk)
            System.out.println("Ok pressed");

        if (!message.isOk)
            System.out.println("Cancel pressed");

        message.dispose();
    }
}
