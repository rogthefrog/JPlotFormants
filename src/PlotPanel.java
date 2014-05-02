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
// Source File Name:   PlotPanel.java

import java.awt.*;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;

public class PlotPanel extends JPanel
{

    public PlotPanel()
    {
    }

    public PlotPanel(int i, int j, Vector vector, int k, int l, int i1, int j1, 
            int k1, int l1, int i2, boolean flag, boolean flag1, String s, boolean flag2, 
            boolean flag3, boolean flag4, boolean flag5, boolean flag6, boolean flag7)
    {
        vowelsToPlot = vector;
        numberOfVowelsToPlot = vowelsToPlot.size();
        F2MarginL = 30;
        F2MarginR = 50;
        F1Margin = 50;
        pointSize = k;
        blobSize = pointSize + 4;
        width = i;
        height = j;
        F2Min = k1;
        F2Max = l1;
        F2Step = i2;
        F1Min = l;
        F1Max = i1;
        F1Step = j1;
        drawAGrid = flag;
        drawTheSymbol = flag1;
        drawBlobOrSymbol = flag3;
        plotMean = flag2;
        plotEllipse = flag6;
        plotSDBars = flag7;
        inColor = flag4;
        dontDrawPointsOrSymbols = flag5;
        pltTtl = s;
    }

    private int xTransformed(int i)
    {
        return -(((i - F2Min) * F2AxisLength) / F2Max);
    }

    private int yTransformed(int i)
    {
        return ((i - F1Min) * F1AxisLength) / F1Max;
    }

    private double toBark(int i)
    {
        double d = i / 1000;
        double d1 = 13D * Math.atan(0.76000000000000001D * d) + 3.5D * Math.atan(d / 7.5D) * (d / 7.5D);
        return d1;
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.setFont(new Font("Serif", 0, 12));
        F2AxisLength = width - (F2MarginL + F2MarginR);
        F1AxisLength = height - 2 * F1Margin;
        graphics2d.drawString(pltTtl, (width - graphics2d.getFontMetrics().stringWidth(pltTtl)) / 2, height - graphics2d.getFontMetrics().getAscent());
        graphics2d.drawLine(F2MarginL, F1Margin, width - (F2MarginR - 10), F1Margin);
        graphics2d.drawLine(width - F2MarginR, F1Margin - 10, width - F2MarginR, height - F1Margin);
        graphics2d.drawString("F2", F2MarginL - 20, F1Margin);
        graphics2d.drawString("F1", width - F2MarginR, (height - F1Margin) + 25);
        int i = width - F2MarginR;
        int j = F1Margin;
        if(drawAGrid)
        {
            i = F2MarginL;
            j = height - F1Margin;
        }
        int k = F2Min;
        for(int l = width; l > F2MarginL + F2MarginR + 10;)
        {
            l = (width - F2MarginR) + xTransformed(k);
            graphics2d.drawLine(l, F1Margin - 5, l, j);
            graphics2d.drawString("" + k, l - 10, F1Margin - 15);
            k += F2Step;
        }

        int i1 = F1Min;
        for(int j1 = 0; j1 <= F1AxisLength;)
        {
            j1 = F1Margin + yTransformed(i1);
            graphics2d.drawLine(width - (F2MarginR - 5), j1, i, j1);
            graphics2d.drawString("" + i1, (width - F2MarginR) + 10, j1 + 5);
            i1 += F1Step;
        }

        graphics2d.translate(width - F2MarginR, F1Margin);
        for(vowelIndex = 0; vowelIndex < numberOfVowelsToPlot; vowelIndex++)
        {
            F1Mean = 0.0D;
            F2Mean = 0.0D;
            F1SumSq = 0.0D;
            F2SumSq = 0.0D;
            F1F2Product = 0.0D;
            colorIndex = vowelIndex >= colors.length ? vowelIndex % colors.length : vowelIndex;
            vowelBeingPlotted = (Vowel)vowelsToPlot.elementAt(vowelIndex);
            formantsBeingPlottedListModel = vowelBeingPlotted.getFormantListModel();
            numberOfPoints = formantsBeingPlottedListModel.getSize();
            if(!drawTheSymbol || vowelBeingPlotted.getSymbol().equals(""))
            {
                for(int k1 = 0; k1 < numberOfPoints; k1++)
                {
                    if(inColor)
                        graphics2d.setPaint(colors[colorIndex]);
                    pointBeingPlotted = Point.extractFormattedPoint((String)formantsBeingPlottedListModel.getElementAt(k1));
                    F1Mean += pointBeingPlotted.getX();
                    F2Mean += pointBeingPlotted.getY();
                    F1SumSq += pointBeingPlotted.getX() * pointBeingPlotted.getX();
                    F2SumSq += pointBeingPlotted.getY() * pointBeingPlotted.getY();
                    F1F2Product += pointBeingPlotted.getX() * pointBeingPlotted.getY();
                    if(!dontDrawPointsOrSymbols)
                        graphics2d.fillOval(xTransformed(pointBeingPlotted.getY()) - pointSize / 2, yTransformed(pointBeingPlotted.getX()) - pointSize / 2, pointSize, pointSize);
                }

            } else
            {
                symbolToDraw = vowelBeingPlotted.getSymbol();
                for(int l1 = 0; l1 < numberOfPoints; l1++)
                {
                    pointBeingPlotted = Point.extractFormattedPoint((String)formantsBeingPlottedListModel.getElementAt(l1));
                    F1Mean += pointBeingPlotted.getX();
                    F2Mean += pointBeingPlotted.getY();
                    F1SumSq += pointBeingPlotted.getX() * pointBeingPlotted.getX();
                    F2SumSq += pointBeingPlotted.getY() * pointBeingPlotted.getY();
                    F1F2Product += pointBeingPlotted.getX() * pointBeingPlotted.getY();
                    if(inColor)
                        graphics2d.setPaint(colors[colorIndex]);
                    if(!dontDrawPointsOrSymbols)
                        graphics2d.drawString(symbolToDraw, xTransformed(pointBeingPlotted.getY()) - (graphics2d.getFontMetrics().stringWidth(symbolToDraw) / 2 - 1), yTransformed(pointBeingPlotted.getX()) + graphics2d.getFontMetrics().getAscent() / 3);
                }

            }
            F1Mean /= numberOfPoints;
            F2Mean /= numberOfPoints;
            F1SumSq = F1SumSq / (double)numberOfPoints - F1Mean * F1Mean;
            F2SumSq = F2SumSq / (double)numberOfPoints - F2Mean * F2Mean;
            F1F2Product = F1F2Product / (double)numberOfPoints - F1Mean * F2Mean;
            if(plotMean && numberOfPoints > 0)
                if(drawBlobOrSymbol || vowelBeingPlotted.getSymbol().equals(""))
                {
                    graphics2d.drawLine(xTransformed((int)F2Mean) - blobSize / 2, yTransformed((int)F1Mean), xTransformed((int)F2Mean) + blobSize / 2, yTransformed((int)F1Mean));
                    graphics2d.drawLine(xTransformed((int)F2Mean), yTransformed((int)F1Mean) - blobSize / 2, xTransformed((int)F2Mean), yTransformed((int)F1Mean) + blobSize / 2);
                } else
                {
                    symbolToDraw = vowelBeingPlotted.getSymbol();
                    graphics2d.setFont(new Font("Serif", 1, 22));
                    graphics2d.drawString(symbolToDraw, xTransformed((int)F2Mean) - (graphics2d.getFontMetrics().stringWidth(symbolToDraw) / 2 - 1), yTransformed((int)F1Mean) + graphics2d.getFontMetrics().getAscent() / 3);
                    graphics2d.setFont(new Font("Serif", 0, 12));
                }
            if(plotSDBars && numberOfPoints > 1)
            {
                int i2 = 0;
                int j2 = 0;
                for(int k2 = 0; k2 < numberOfPoints; k2++)
                {
                    pointBeingPlotted = Point.extractFormattedPoint((String)formantsBeingPlottedListModel.getElementAt(k2));
                    i2 = (int)((double)i2 + ((double)pointBeingPlotted.getX() - F1Mean) * ((double)pointBeingPlotted.getX() - F1Mean));
                    j2 = (int)((double)j2 + ((double)pointBeingPlotted.getY() - F2Mean) * ((double)pointBeingPlotted.getY() - F2Mean));
                }

                double d = Math.sqrt(i2 / numberOfPoints);
                double d1 = Math.sqrt(j2 / numberOfPoints);
                int l2 = xTransformed((int)F2Mean + 2 * (int)d1);
                int i3 = xTransformed((int)F2Mean - 2 * (int)d1);
                int j3 = yTransformed((int)F1Mean + 2 * (int)d);
                int k3 = yTransformed((int)F1Mean - 2 * (int)d);
                byte byte0 = 40;
                graphics2d.drawLine(i3, yTransformed((int)F1Mean), l2, yTransformed((int)F1Mean));
                graphics2d.drawLine(xTransformed((int)F2Mean), k3, xTransformed((int)F2Mean), j3);
                graphics2d.drawLine(i3, yTransformed((int)F1Mean + byte0 / 2), i3, yTransformed((int)F1Mean - byte0 / 2));
                graphics2d.drawLine(l2, yTransformed((int)F1Mean + byte0 / 2), l2, yTransformed((int)F1Mean - byte0 / 2));
                graphics2d.drawLine(xTransformed((int)F2Mean + byte0 / 2), j3, xTransformed((int)F2Mean - byte0 / 2), j3);
                graphics2d.drawLine(xTransformed((int)F2Mean + byte0 / 2), k3, xTransformed((int)F2Mean - byte0 / 2), k3);
            }
            if(plotEllipse && numberOfPoints > 1)
            {
                int l3 = 2;
                int i4 = 250;
                int ai[] = new int[i4];
                int ai1[] = new int[i4];
                double d10 = 0.0D;
                if(F1SumSq != 0.0D && F2SumSq != 0.0D)
                {
                    double d2 = (F2SumSq - F1SumSq) / 2D;
                    double d3 = Math.sqrt(d2 * d2 + F1F2Product * F1F2Product);
                    double d4 = (F1SumSq + F2SumSq) / 2D;
                    double d5 = d4 + d3;
                    double d6 = d4 - d3;
                    double d7;
                    if(d5 > 0.0D)
                        d7 = Math.sqrt(d5);
                    else
                        d7 = 0.0D;
                    double d8;
                    if(d6 > 0.0D)
                        d8 = Math.sqrt(d6);
                    else
                        d8 = 0.0D;
                    if(F1F2Product != 0.0D)
                    {
                        d10 = (d2 + d3) / F1F2Product;
                        double d11;
                        if(d10 != 0.0D)
                            d11 = -1D / d10;
                        else
                            d11 = 0.0D;
                    }
                    double d9 = Math.atan(d10);
                    double d12 = d7 * (double)l3;
                    double d13 = d8 * (double)l3;
                    double d14 = d12 * d12;
                    double d15 = d13 * d13;
                    double d16 = Math.sqrt(1.0D - d15 / d14);
                    double d18 = 0.0D;
                    for(int j4 = 0; j4 < i4; j4++)
                    {
                        double d17 = d16 * Math.cos(d9 - d18);
                        double d19 = 1.0D - d17 * d17;
                        if(d19 != 0.0D)
                        {
                            double d20 = Math.sqrt(d15 / d19);
                            ai[j4] = xTransformed((int)(d20 * Math.sin(d18) + F2Mean));
                            ai1[j4] = yTransformed((int)(d20 * Math.cos(d18) + F1Mean));
                            d18 += 6.2831853071795862D / (double)i4;
                        }
                    }

                    graphics2d.drawPolygon(ai, ai1, i4);
                }
            }
        }

    }

    public static void main(String args[])
    {
    }

    private static int pointSize;
    private static int blobSize;
    private static int width;
    private static int height;
    private static Vector vowelsToPlot;
    private static int F2Min;
    private static int F2Max;
    private static int F1Min;
    private static int F1Max;
    private static int F2AxisLength;
    private static int F1AxisLength;
    private static int F2MarginL;
    private static int F2MarginR;
    private static int F1Margin;
    private static int F2Step;
    private static int F1Step;
    private static boolean drawAGrid;
    private static boolean drawTheSymbol;
    private static boolean drawBlobOrSymbol;
    private static boolean plotMean;
    private static boolean plotSDBars;
    private static boolean plotEllipse;
    private static boolean inColor;
    private static boolean dontDrawPointsOrSymbols;
    private String symbolToDraw;
    private static String pltTtl;
    private Vowel vowelBeingPlotted;
    private Point pointBeingPlotted;
    private static int numberOfVowelsToPlot;
    private int numberOfPoints;
    private int vowelIndex;
    private int colorIndex;
    private DefaultListModel formantsBeingPlottedListModel;
    private double F1Mean;
    private double F2Mean;
    private double F1SumSq;
    private double F2SumSq;
    private double F1F2Product;
    private static final Color black;
    private static final Color blue;
    private static final Color red;
    private static final Color green;
    private static final Color cyan;
    private static final Color darkGray;
    private static final Color magenta;
    private static final Color orange;
    private static final Color pink;
    private static final Color colors[];

    static 
    {
        black = Color.black;
        blue = Color.blue;
        red = Color.red;
        green = Color.green;
        cyan = Color.cyan;
        darkGray = Color.darkGray;
        magenta = Color.magenta;
        orange = Color.orange;
        pink = Color.pink;
        colors = (new Color[] {
            black, blue, red, green, cyan, darkGray, magenta, orange, pink
        });
    }
}