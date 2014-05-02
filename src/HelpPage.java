// JPlotFormants (c) 2001 Roger Billerey-Mosier
// https://github.com/rogthefrog

// MIT License 
// http://opensource.org/licenses/MIT

// No guarantees of any kind. This is ancient software with a good deal
// of bit rot and very little quality to begin with. I hope it's useful to somebody.
// Please submit improvements, bug fixes, patches, etc. via pull requests via GitHub.

// This source code was lost and reconstructed by decompiling binary class files.
// This explains the lack of comments and bad variable names. Feel free to fix that.

// Very much inspired by Peter Ladefoged's Plot Formants (with Peter's blessing).

// Thanks to:
// Peter Ladefoged (initial Plot Formants program, support, awesomeness)
// Marek Przezdziecki (ellipse drawing code)
// Juerg Bigler (restoring the source)
// Hugh Paterson III (suggesting github to share this code)

// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 13.12.2006 23:08:53
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   HelpPage.java

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;

public class HelpPage
    implements HyperlinkListener, PropertyChangeListener, ActionListener
{

    public HelpPage()
    {
    }

    public HelpPage(boolean flag)
    {
        errorMsg = "with the help module.";
        int i = Toolkit.getDefaultToolkit().getScreenSize().width;
        int j = Toolkit.getDefaultToolkit().getScreenSize().height;
        int k = (int)((double)i * 0.75D);
        int l = (int)((double)j * 0.80000000000000004D);
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(this);
        JScrollPane jscrollpane = new JScrollPane(editorPane);
        closeHelpBtn = new JButton("Close window");
        closeHelpBtn.setMnemonic('C');
        closeHelpBtn.setAlignmentX(0.5F);
        closeHelpBtn.addActionListener(this);
        helpContentPane = new JPanel();
        helpContentPane.setLayout(new BoxLayout(helpContentPane, 1));
        helpContentPane.add(jscrollpane);
        helpContentPane.add(closeHelpBtn);
        helpFrame = new JFrame();
        helpFrame.setContentPane(helpContentPane);
        helpFrame.pack();
        helpFrame.setSize(k, l);
        helpFrame.setLocation(i - k, 0);
        helpFrame.setTitle("JPlotFormants Help");
        helpFrame.setDefaultCloseOperation(2);
        try
        {
            if(flag)
            {
                errorMsg = "trying to open your browser.";
                launchProgram();
            } else
            {
                String s = "file:///" + ((new File(".")).getCanonicalPath() + "/JPlotFormants.htm").replace('\\', '/');
                url = new URL(s);
                errorMsg = "opening the user's manual.";
                editorPane.setPage(url);
                helpFrame.show();
            }
        }
        catch(IOException ioexception)
        {
            JOptionPane.showMessageDialog(null, "There was an error " + errorMsg, "Help error", 0);
            helpFrame.dispose();
        }
        catch(InterruptedException interruptedexception)
        {
            System.out.println("Interr exc.");
        }
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        helpFrame.dispose();
    }

    public void propertyChange(PropertyChangeEvent propertychangeevent)
    {
    }

    public void hyperlinkUpdate(HyperlinkEvent hyperlinkevent)
    {
        javax.swing.event.HyperlinkEvent.EventType eventtype = hyperlinkevent.getEventType();
        if(eventtype == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED)
            try
            {
                editorPane.setPage(hyperlinkevent.getURL());
            }
            catch(MalformedURLException malformedurlexception)
            {
                System.out.println("Bad URL");
            }
            catch(IOException ioexception)
            {
                System.out.println("IO Exception");
            }
    }

    private void launchProgram()
        throws IOException, InterruptedException
    {
        String as[] = new String[2];
        as[0] = "C:/Program Files/Internet Explorer/IEXPLORE.EXE";
        as[1] = "http://www.linguistics.ucla.edu/people/grads/billerey/PlotFrog.htm";
        Runtime runtime = Runtime.getRuntime();
        runtime.exec(as);
    }

    public static void main(String args[])
    {
        HelpPage helppage = new HelpPage(true);
    }

    JEditorPane editorPane;
    JFrame helpFrame;
    JButton closeHelpBtn;
    JPanel helpContentPane;
    URL url;
    String errorMsg;
}