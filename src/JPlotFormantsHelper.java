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
// Source File Name:   JPlotFormantsHelper.java

public class JPlotFormantsHelper
{

    public JPlotFormantsHelper()
    {
    }

    public static boolean isNumber(String s)
    {
        boolean flag = true;
        s = (s + " ").trim();
        if(s.equals(""))
            return false;
        char ac[] = s.toCharArray();
        for(int i = 0; i < ac.length; i++)
        {
            if(Character.isDigit(ac[i]))
                continue;
            flag = false;
            break;
        }

        return flag;
    }
}