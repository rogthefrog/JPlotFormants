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
// Source File Name:   Point.java

import java.io.PrintStream;

public class Point
{

    public Point()
    {
        setPoint(0, 0);
    }

    public Point(int i, int j)
    {
        setPoint(i, j);
    }

    public void setPoint(int i, int j)
    {
        x = i;
        y = j;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String pointToString()
    {
        return "[" + x + "," + y + "]";
    }

    public static Point extractFormattedPoint(String s)
    {
        int i = 0;
        int j = 0;
        try
        {
            i = Integer.parseInt(s.substring(1, s.indexOf(',')).trim());
            j = Integer.parseInt(s.substring(s.indexOf(',') + 1, s.indexOf(']')).trim());
        }
        catch(NumberFormatException numberformatexception)
        {
            System.out.println("String to int conversion error.");
        }
        finally
        {
            return new Point(i, j);
        }
    }

    private int x;
    private int y;
}