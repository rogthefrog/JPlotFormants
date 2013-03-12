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
// Source File Name:   PlotFrame.java

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.*;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.*;

public class PlotFrame extends JFrame
    implements ActionListener, Printable
{

    public PlotFrame()
    {
        jpegFc = new JFileChooser(System.getProperty("user.dir"));
        jpegFilter = new JPEGFilter();
    }

    public PlotFrame(int i, int j, Vector vector, int k, int l, int i1, int j1, 
            int k1, int l1, int i2, boolean flag, boolean flag1, String s, boolean flag2, 
            boolean flag3, boolean flag4, boolean flag5, boolean flag6, boolean flag7)
    {
        jpegFc = new JFileChooser(System.getProperty("user.dir"));
        jpegFilter = new JPEGFilter();
        plotContentPane = new JPanel();
        plotBtnPane = new JPanel();
        closeBtn = new JButton("Close window");
        closeBtn.setMnemonic('C');
        closeBtn.addActionListener(this);
        printBtn = new JButton("Print");
        printBtn.setMnemonic('P');
        printBtn.addActionListener(this);
        saveAsPictureBtn = new JButton("Save as JPEG");
        saveAsPictureBtn.setMnemonic('S');
        saveAsPictureBtn.addActionListener(this);
        plotBtnPane.setLayout(new BoxLayout(plotBtnPane, 0));
        plotBtnPane.add(printBtn);
        plotBtnPane.add(Box.createRigidArea(new Dimension(5, 0)));
        plotBtnPane.add(saveAsPictureBtn);
        plotBtnPane.add(Box.createHorizontalGlue());
        plotBtnPane.add(closeBtn);
        plotBtnPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        plotPanel = new PlotPanel(i, j, vector, k, l, i1, j1, k1, l1, i2, flag, flag1, s, flag2, flag3, flag4, flag5, flag6, flag7);
        plotContentPane.setLayout(new BorderLayout());
        plotContentPane.add(plotPanel, "Center");
        plotContentPane.add(plotBtnPane, "South");
        plotContentPane.setBackground(Color.white);
        setContentPane(plotContentPane);
        setSize(i, j + 70);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - i) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - (j + 70)) / 2);
        setResizable(false);
        setTitle("Formant plot");
        setIconImage((new ImageIcon("PlotFrogIcon.gif")).getImage());
        show();
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == closeBtn)
            dispose();
        if(obj == saveAsPictureBtn)
        {
            setVisible(false);
            int i = 0;
            jpegFc.setFileFilter(jpegFilter);
            int j = jpegFc.showSaveDialog(null);
            if(j == 0)
            {
                saveImage = jpegFc.getSelectedFile();
                if(JPEGFilter.getSuffix(saveImage).equals(""))
                    saveImage = new File(saveImage.toString() + ".jpg");
                if(saveImage != null)
                {
                    if(saveImage.exists())
                        i = JOptionPane.showConfirmDialog(null, "The file " + saveImage.getName() + " already exists." + "\n" + "Do you really wish to replace it?", "Save plot", 1, 3);
                    if(i == 0)
                    {
                        setVisible(true);
                        plotBtnPane.setVisible(false);
                        saveComponentAsJPEG(plotContentPane, saveImage.getAbsolutePath());
                        plotBtnPane.setVisible(true);
                    }
                }
            }
        }
        if(obj == printBtn)
        {
            setVisible(false);
            JOptionPane.showMessageDialog(null, "The printer box you're about to see is likely to display\na large number of pages in the 'Print pages 1 to xxx' field.\nThis is a NORMAL system glitch. The plot is only ONE page long\nand only that page will be printed.", "Don't worry", 1);
            setVisible(true);
            plotBtnPane.setVisible(false);
            PrinterJob printerjob = PrinterJob.getPrinterJob();
            printerjob.setPrintable(this);
            if(printerjob.printDialog())
                try
                {
                    printerjob.print();
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            plotBtnPane.setVisible(true);
        }
    }

    public int print(Graphics g, PageFormat pageformat, int i)
        throws PrinterException
    {
        if(i >= 1)
            return 1;
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.translate(pageformat.getImageableX(), pageformat.getImageableY());
        Dimension dimension = getSize();
        double d = pageformat.getImageableWidth();
        double d1 = pageformat.getImageableHeight();
        if((double)dimension.width > d)
        {
            double d2 = d / (double)dimension.width;
            graphics2d.scale(d2, d2);
            d /= d2;
            d1 /= d2;
        }
        if((double)dimension.height > d1)
        {
            double d3 = d1 / (double)dimension.height;
            graphics2d.scale(d3, d3);
            d /= d3;
            d1 /= d3;
        }
        graphics2d.translate((d - (double)dimension.width) / 2D, (d1 - (double)dimension.height) / 2D);
        graphics2d.setClip(0, 0, dimension.width, dimension.height);
        paint(g);
        return 0;
    }

    public void saveComponentAsJPEG(Component component, String s)
    {
        Dimension dimension = component.getSize();
        BufferedImage bufferedimage = new BufferedImage(dimension.width, dimension.height - 35, 1);
        Graphics2D graphics2d = bufferedimage.createGraphics();
        component.paint(graphics2d);
        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream(s);
            JPEGImageEncoder jpegimageencoder = JPEGCodec.createJPEGEncoder(fileoutputstream);
            jpegimageencoder.encode(bufferedimage);
            fileoutputstream.close();
        }
        catch(Exception exception)
        {
            System.out.println(exception);
        }
    }

    public static void main(String args[])
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception exception)
        {
            System.err.println("Can't set look and feel: " + exception);
        }
    }

    private JPanel plotContentPane;
    private JPanel plotBtnPane;
    private PlotPanel plotPanel;
    private JButton closeBtn;
    private JButton printBtn;
    private JButton saveAsPictureBtn;
    private final JFileChooser jpegFc;
    private final JPEGFilter jpegFilter;
    private static final String newline = "\n";
    private File saveImage;
    private static final int paddingForBtns = 70;
}