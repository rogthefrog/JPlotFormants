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
// Source File Name:   Vowel.java

import javax.swing.DefaultListModel;

class Vowel
{

    public Vowel()
    {
        setDescription("");
        setSymbol("");
        setFormantListModel(new DefaultListModel());
    }

    public Vowel(String s, String s1, DefaultListModel defaultlistmodel)
    {
        setDescription(s);
        setSymbol(s1);
        setFormantListModel(defaultlistmodel);
    }

    public Vowel(String s, String s1)
    {
        setDescription(s);
        setSymbol(s1);
        setFormantListModel(new DefaultListModel());
    }

    public void setDescription(String s)
    {
        description = s;
    }

    public void setSymbol(String s)
    {
        symbol = s;
    }

    public void setFormantListModel(DefaultListModel defaultlistmodel)
    {
        formantListModel = defaultlistmodel;
    }

    public String getDescription()
    {
        return description;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public DefaultListModel getFormantListModel()
    {
        return formantListModel;
    }

    public void addFormant(String s)
    {
        formantListModel.addElement(s);
    }

    public void addFormant(Point point)
    {
        formantListModel.addElement(point.pointToString());
    }

    public boolean hasBeenCreated()
    {
        return !description.equals("") || !symbol.equals("");
    }

    private String description;
    private String symbol;
    private DefaultListModel formantListModel;
}