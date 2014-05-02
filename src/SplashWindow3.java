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
// Source File Name:   SplashWindow3.java

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class SplashWindow3 extends JWindow
{

    public SplashWindow3(String s, Frame frame, int i)
    {
        super(frame);
        JLabel jlabel = new JLabel(new ImageIcon(s));
        getContentPane().add(jlabel, "Center");
        pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dimension1 = jlabel.getPreferredSize();
        setLocation(dimension.width / 2 - dimension1.width / 2, dimension.height / 2 - dimension1.height / 2);
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent mouseevent)
            {
                setVisible(false);
                dispose();
            }

        });
        final int pause = i;
        final Runnable closerRunner = new Runnable() {

            public void run()
            {
                setVisible(false);
                dispose();
            }

        };
        Runnable runnable = new Runnable() {

            public void run()
            {
                try
                {
                    Thread.sleep(pause);
                    SwingUtilities.invokeAndWait(closerRunner);
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }

        };
        setVisible(true);
        Thread thread = new Thread(runnable, "SplashThread");
        thread.start();
    }
}