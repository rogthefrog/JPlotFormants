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
// Source File Name:   JPlotFormants.java

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

public class JPlotFormants extends JFrame
    implements ActionListener, ItemListener, ListSelectionListener, WindowListener
{

    public JPlotFormants()
    {
        super("JPlotFormants");
        MAX_PLOT_WIDTH = (int)((double)Toolkit.getDefaultToolkit().getScreenSize().width * 0.90000000000000002D);
        MAX_PLOT_HEIGHT = (int)((double)Toolkit.getDefaultToolkit().getScreenSize().height * 0.80000000000000004D);
        defaultPlotTitle = "";
        resetDefaultPlotOptions(2);
        initializeVariables();
        mainWindowSetup();
        setDefaultCloseOperation(0);
        addWindowListener(this);
        show();
    }

    private void mainWindowSetup()
    {
        mainPane = new JPanel();
        mainPane.add(new JLabel("Welcome to JPlotFormants v1.4"));
        setContentPane(mainPane);
        menuSetup();
        pack();
        setSize(225, 90);
        setIconImage((new ImageIcon("JPlotFormantsIcon.gif")).getImage());
        setResizable(false);
    }

    private void menuSetup()
    {
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenuItems = new JMenuItem[fileMenuItemNames.length];
        for(int i = 0; i < fileMenuItemNames.length; i++)
        {
            fileMenuItems[i] = new JMenuItem(fileMenuItemNames[i]);
            fileMenuItems[i].setMnemonic(fileMenuItemMnemonic[i]);
            fileMenuItems[i].addActionListener(this);
            fileMenu.add(fileMenuItems[i]);
        }

        fileMenuItems[0].setAccelerator(KeyStroke.getKeyStroke(78, 2));
        fileMenuItems[2].setAccelerator(KeyStroke.getKeyStroke(79, 2));
        fileMenuItems[3].setAccelerator(KeyStroke.getKeyStroke(83, 2));
        fileMenuItems[5].setAccelerator(KeyStroke.getKeyStroke(88, 2));
        plotMenu = new JMenu("Plot");
        plotMenu.setMnemonic('P');
        plotMenuItems = new JMenuItem[plotMenuItemNames.length];
        for(int j = 0; j < plotMenuItemNames.length; j++)
        {
            plotMenuItems[j] = new JMenuItem(plotMenuItemNames[j]);
            plotMenuItems[j].setMnemonic(plotMenuItemMnemonic[j]);
            plotMenuItems[j].addActionListener(this);
            plotMenu.add(plotMenuItems[j]);
        }

        plotMenuItems[0].setEnabled(false);
        fileMenuItems[3].setEnabled(false);
        fileMenuItems[1].setEnabled(false);
        fileMenuItems[4].setEnabled(false);
        plotMenuItems[0].setAccelerator(KeyStroke.getKeyStroke(80, 2));
        plotMenuItems[1].setAccelerator(KeyStroke.getKeyStroke(84, 2));
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        helpMenuItems = new JMenuItem[helpMenuItemNames.length];
        for(int k = 0; k < helpMenuItemNames.length; k++)
        {
            helpMenuItems[k] = new JMenuItem(helpMenuItemNames[k]);
            helpMenuItems[k].setMnemonic(helpMenuItemMnemonic[k]);
            helpMenuItems[k].addActionListener(this);
            helpMenu.add(helpMenuItems[k]);
        }

        helpMenuItems[0].setAccelerator(KeyStroke.getKeyStroke(72, 2));
        helpMenuItems[1].setAccelerator(KeyStroke.getKeyStroke(87, 2));
        if(!System.getProperty("os.name").startsWith("Windows"))
            helpMenuItems[1].setEnabled(false);
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(plotMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void enterFormants()
    {
        createDialog = new JDialog();
        JPanel jpanel = new JPanel();
        JPanel jpanel1 = new JPanel();
        JPanel jpanel2 = new JPanel();
        JPanel jpanel3 = new JPanel();
        Dimension dimension = new Dimension(0, 3);
        vowelBoxLabel = new JLabel("Available vowels:");
        vowelDescriptionLabel = new JLabel("Vowel description:");
        vowelSymbolLabel = new JLabel("Vowel symbol:");
        f1Label = new JLabel("F1 frequency:");
        f2Label = new JLabel("F2 frequency:");
        listLabel = new JLabel("Current formant pairs:");
        showSymbolsBtn = new JButton("Show IPA symbols");
        showSymbolsBtn.setMnemonic('I');
        showSymbolsBtn.addActionListener(this);
        showSymbolsBtn.setEnabled(false);
        editSelectedBtn = new JButton("Edit selected formant pair");
        editSelectedBtn.setMnemonic('E');
        editSelectedBtn.addActionListener(this);
        deleteSelectedBtn = new JButton("Delete selected formant pair");
        deleteSelectedBtn.setMnemonic('D');
        deleteSelectedBtn.addActionListener(this);
        doneBtn = new JButton("Done");
        doneBtn.setMnemonic('o');
        doneBtn.addActionListener(this);
        saveFormantBtn = new JButton("Accept");
        saveFormantBtn.setMnemonic('A');
        saveFormantBtn.addActionListener(this);
        createNewVowelBtn = new JButton("Create new vowel");
        createNewVowelBtn.setMnemonic('n');
        createNewVowelBtn.addActionListener(this);
        deleteVowelBtn = new JButton("Delete selected vowel");
        deleteVowelBtn.setMnemonic('l');
        deleteVowelBtn.addActionListener(this);
        enableMenuCommandsIfEmptySet(areThereFormantsInMemory());
        enableButtonsIfEmptySet(areThereFormantsInMemory());
        vowelDescriptionField = new JTextField();
        vowelDescriptionField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                vowelSymbolField.grabFocus();
            }

        });
        vowelSymbolField = new JTextField();
        vowelSymbolField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                f1Field.grabFocus();
            }

        });
        f1Field = new JTextField();
        f1Field.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                if(!JPlotFormantsHelper.isNumber(f1Field.getText()))
                    createStatusTextArea.setText("F1 value invalid. Please enter a number between 200 and 800.");
                f2Field.grabFocus();
            }

        });
        f2Field = new JTextField();
        f2Field.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                if(JPlotFormantsHelper.isNumber(f1Field.getText()) && JPlotFormantsHelper.isNumber(f2Field.getText()))
                    saveFormantBtn.grabFocus();
                else
                if(!JPlotFormantsHelper.isNumber(f1Field.getText()))
                {
                    createStatusTextArea.setText("Please enter a value between 200 and 800.");
                    f1Field.grabFocus();
                } else
                {
                    createStatusTextArea.setText("Please enter a value between 500 and 1800.");
                    f2Field.grabFocus();
                }
            }

        });
        currentFormantList = new JList(currentFormantListModel);
        currentFormantList.setVisibleRowCount(3);
        currentFormantList.setFixedCellHeight(15);
        currentFormantList.setFixedCellWidth(100);
        currentFormantList.setSelectionMode(0);
        currentFormantListScrollPane = new JScrollPane(currentFormantList);
        vowelDescriptionList = new JList(vowelDescriptionListModel);
        vowelDescriptionList.setVisibleRowCount(3);
        vowelDescriptionList.setFixedCellHeight(15);
        vowelDescriptionList.setFixedCellWidth(100);
        vowelDescriptionList.addListSelectionListener(this);
        vowelDescriptionList.setSelectionMode(0);
        vowelDescriptionListScrollPane = new JScrollPane(vowelDescriptionList);
        createStatusTextArea = new JTextArea(4, 20);
        createStatusTextArea.setWrapStyleWord(true);
        createStatusTextArea.setLineWrap(true);
        createStatusTextArea.setForeground(Color.white);
        createStatusTextArea.setBackground(Color.blue);
        createStatusTextArea.setEditable(false);
        vowelDescriptionLabel.setAlignmentX(0.0F);
        vowelSymbolLabel.setAlignmentX(0.0F);
        f1Label.setAlignmentX(0.0F);
        f2Label.setAlignmentX(0.0F);
        listLabel.setAlignmentX(0.0F);
        vowelDescriptionListScrollPane.setAlignmentX(0.0F);
        currentFormantListScrollPane.setAlignmentX(0.0F);
        jpanel2.setLayout(new BoxLayout(jpanel2, 1));
        jpanel2.add(vowelBoxLabel);
        jpanel2.add(vowelDescriptionListScrollPane);
        jpanel2.add(vowelDescriptionLabel);
        jpanel2.add(vowelDescriptionField);
        jpanel2.add(vowelSymbolLabel);
        jpanel2.add(vowelSymbolField);
        jpanel2.add(f1Label);
        jpanel2.add(f1Field);
        jpanel2.add(f2Label);
        jpanel2.add(f2Field);
        jpanel2.add(listLabel);
        jpanel2.add(currentFormantListScrollPane);
        jpanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        showSymbolsBtn.setAlignmentX(0.5F);
        editSelectedBtn.setAlignmentX(0.5F);
        deleteSelectedBtn.setAlignmentX(0.5F);
        doneBtn.setAlignmentX(0.5F);
        saveFormantBtn.setAlignmentX(0.5F);
        createNewVowelBtn.setAlignmentX(0.5F);
        deleteVowelBtn.setAlignmentX(0.5F);
        jpanel1.setLayout(new BoxLayout(jpanel1, 1));
        jpanel1.add(editSelectedBtn);
        jpanel1.add(Box.createRigidArea(dimension));
        jpanel1.add(deleteSelectedBtn);
        jpanel1.add(Box.createRigidArea(dimension));
        jpanel1.add(saveFormantBtn);
        jpanel1.add(Box.createRigidArea(dimension));
        jpanel1.add(createNewVowelBtn);
        jpanel1.add(Box.createRigidArea(dimension));
        jpanel1.add(deleteVowelBtn);
        jpanel1.add(Box.createRigidArea(dimension));
        jpanel1.add(doneBtn);
        jpanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jpanel3.setLayout(new BorderLayout());
        jpanel3.add(createStatusTextArea);
        jpanel3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jpanel.setLayout(new BorderLayout());
        jpanel.add(jpanel2, "North");
        jpanel.add(jpanel1, "Center");
        jpanel.add(jpanel3, "South");
        if(vowelDescriptionListModel.isEmpty())
        {
            currentVowel = new Vowel();
            creatingNewVowel = true;
            currentVowelIndexInList = 0;
        } else
        {
            currentVowel = (Vowel)vowelVector.elementAt(vowelDescriptionListModel.getSize() - 1);
            creatingNewVowel = false;
            currentVowelIndexInList = vowelDescriptionListModel.getSize() - 1;
        }
        createDialog.setContentPane(jpanel);
        createDialog.setLocation(200, 0);
        createDialog.setDefaultCloseOperation(2);
        createDialog.setTitle("Enter formants");
        createDialog.setResizable(false);
        createDialog.setModal(true);
        displayVowel(currentVowel);
        createDialog.pack();
        createDialog.show();
    }

    private void displayVowel(Vowel vowel)
    {
        vowelDescriptionField.setText(vowel.getDescription());
        vowelSymbolField.setText(vowel.getSymbol());
        currentFormantListModel = vowel.getFormantListModel();
    }

    private void plotSet()
    {
        if(!vowelVector.isEmpty())
        {
            Object aobj[] = currentFormantListModel.toArray();
            Point apoint[] = new Point[aobj.length];
            for(int i = 0; i < aobj.length; i++)
                apoint[i] = Point.extractFormattedPoint(aobj[i].toString());

            PlotFrame plotframe = new PlotFrame(plotWidth, plotHeight, vowelVector, ptSize, f1Min, f1Max, f1Step, f2Min, f2Max, f2Step, drawGrid, drawSymbols, plotTitle, drawMean, drawBlob, inColor, drawNeither, drawEllipses, drawSDBars);
        }
    }

    private void plotOptions()
    {
        int i = Toolkit.getDefaultToolkit().getScreenSize().width;
        int j = Toolkit.getDefaultToolkit().getScreenSize().height;
        defaultPlotWidth = i / 2;
        defaultPlotHeight = j / 2;
        optionsTabPane = new JTabbedPane();
        optionDialog = new JDialog();
        JPanel jpanel = new JPanel();
        JPanel jpanel1 = new JPanel();
        JPanel jpanel2 = new JPanel();
        JPanel jpanel3 = new JPanel();
        jpanel.setLayout(new BoxLayout(jpanel, 1));
        jpanel1.setLayout(new BoxLayout(jpanel1, 1));
        gridYesNo = new JCheckBox("Draw grid");
        gridYesNo.setSelected(drawGrid);
        pointSizeLabel = new JLabel("Point size:");
        pointSizeSlider = new JSlider(0, 3, 9, ptSize);
        Hashtable hashtable = new Hashtable();
        hashtable.put(new Integer(3), new JLabel("Small"));
        hashtable.put(new Integer(5), new JLabel("Medium"));
        hashtable.put(new Integer(7), new JLabel("Large"));
        hashtable.put(new Integer(9), new JLabel("Very large"));
        pointSizeSlider.setLabelTable(hashtable);
        pointSizeSlider.setMajorTickSpacing(2);
        pointSizeSlider.setPaintTicks(true);
        pointSizeSlider.setPaintLabels(true);
        pointSizeSlider.setPaintTrack(true);
        pointSizeSlider.setSnapToTicks(true);
        pointSizeSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        pointSizeSlider.setAlignmentX(0.0F);
        inColorYesNo = new JCheckBox("Draw in color");
        inColorYesNo.setSelected(inColor);
        drawSymbolsGroup = new ButtonGroup();
        drawSymbolsYesNo = new JRadioButton("Draw symbols");
        drawPointsYesNo = new JRadioButton("Draw points");
        drawNeitherYesNo = new JRadioButton("Draw neither points nor symbols");
        drawSymbolsGroup.add(drawSymbolsYesNo);
        drawSymbolsGroup.add(drawPointsYesNo);
        drawSymbolsGroup.add(drawNeitherYesNo);
        drawSymbolsYesNo.setSelected(drawSymbols && !drawNeither);
        drawPointsYesNo.setSelected(!drawSymbols && !drawNeither);
        drawNeitherYesNo.setSelected(drawNeither);
        drawEllipsesYesNo = new JCheckBox("Draw ellipses");
        drawEllipsesYesNo.setSelected(drawEllipses);
        drawEllipsesYesNo.addItemListener(this);
        drawSDBarsYesNo = new JCheckBox("Draw SD bars");
        drawSDBarsYesNo.setSelected(drawSDBars);
        drawSDBarsYesNo.addItemListener(this);
        drawMeanYesNo = new JCheckBox("Draw mean");
        drawMeanYesNo.setSelected(drawMean);
        drawMeanYesNo.addItemListener(this);
        drawBlobGroup = new ButtonGroup();
        drawBlobYesNo = new JRadioButton("Draw blob at mean", drawBlob);
        drawBigSymbolYesNo = new JRadioButton("Draw large symbol at mean", !drawBlob);
        drawBlobGroup.add(drawBlobYesNo);
        drawBlobGroup.add(drawBigSymbolYesNo);
        drawBlobYesNo.setEnabled(drawMean);
        drawBigSymbolYesNo.setEnabled(drawMean);
        minF1Label = new JLabel("Min. F1 (50 Hz min.):");
        maxF1Label = new JLabel("Max. F1 (1400 Hz max.):");
        stepF1Label = new JLabel("F1 step (Hz):");
        minF2Label = new JLabel("Min. F2 (300 Hz min.):");
        maxF2Label = new JLabel("Max. F2 (4000 Hz max.):");
        stepF2Label = new JLabel("F2 step (Hz):");
        plotWidthLabel = new JLabel("Plot width (" + defaultPlotWidth + " pixels = half screen, max = " + MAX_PLOT_WIDTH + "):");
        plotHeightLabel = new JLabel("Plot height (" + defaultPlotHeight + " pixels = half screen, max = " + MAX_PLOT_HEIGHT + "):");
        plotTitleLabel = new JLabel("Plot title:");
        minF1Field = new JTextField("" + f1Min);
        minF1Field.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                maxF1Field.grabFocus();
            }

        });
        maxF1Field = new JTextField("" + f1Max);
        maxF1Field.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                stepF1Field.grabFocus();
            }

        });
        stepF1Field = new JTextField("" + f1Step);
        stepF1Field.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                minF2Field.grabFocus();
            }

        });
        minF2Field = new JTextField("" + f2Min);
        minF2Field.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                maxF2Field.grabFocus();
            }

        });
        maxF2Field = new JTextField("" + f2Max);
        maxF2Field.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                stepF2Field.grabFocus();
            }

        });
        stepF2Field = new JTextField("" + f2Step);
        stepF2Field.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                plotWidthField.grabFocus();
            }

        });
        plotWidthField = new JTextField("" + plotWidth);
        plotWidthField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                plotHeightField.grabFocus();
            }

        });
        plotHeightField = new JTextField("" + plotHeight);
        plotHeightField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                okBtn.grabFocus();
            }

        });
        plotTitleField = new JTextField(plotTitle);
        plotTitleField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                gridYesNo.grabFocus();
            }

        });
        jpanel.add(minF1Label);
        jpanel.add(minF1Field);
        jpanel.add(maxF1Label);
        jpanel.add(maxF1Field);
        jpanel.add(stepF1Label);
        jpanel.add(stepF1Field);
        jpanel.add(minF2Label);
        jpanel.add(minF2Field);
        jpanel.add(maxF2Label);
        jpanel.add(maxF2Field);
        jpanel.add(stepF2Label);
        jpanel.add(stepF2Field);
        jpanel.add(plotWidthLabel);
        jpanel.add(plotWidthField);
        jpanel.add(plotHeightLabel);
        jpanel.add(plotHeightField);
        jpanel1.add(plotTitleLabel);
        jpanel1.add(plotTitleField);
        jpanel1.add(gridYesNo);
        jpanel1.add(drawNeitherYesNo);
        jpanel1.add(drawSymbolsYesNo);
        jpanel1.add(drawPointsYesNo);
        jpanel1.add(pointSizeLabel);
        jpanel1.add(pointSizeSlider);
        jpanel1.add(inColorYesNo);
        jpanel1.add(drawEllipsesYesNo);
        jpanel1.add(drawSDBarsYesNo);
        jpanel1.add(drawMeanYesNo);
        jpanel1.add(drawBlobYesNo);
        jpanel1.add(drawBigSymbolYesNo);
        jpanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jpanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        optionsTabPane.addTab("Dimensions", jpanel);
        optionsTabPane.addTab("Drawing", jpanel1);
        resetDefaultBtn = new JButton("Reset default values");
        resetDefaultBtn.setMnemonic('d');
        resetDefaultBtn.addActionListener(this);
        okBtn = new JButton("OK");
        okBtn.setMnemonic('O');
        okBtn.addActionListener(this);
        jpanel2.setLayout(new BoxLayout(jpanel2, 0));
        jpanel2.add(resetDefaultBtn);
        jpanel2.add(Box.createHorizontalGlue());
        jpanel2.add(okBtn);
        jpanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jpanel3.setLayout(new BorderLayout());
        jpanel3.add(optionsTabPane, "Center");
        jpanel3.add(jpanel2, "South");
        optionDialog.setContentPane(jpanel3);
        optionDialog.setLocation(200, 0);
        optionDialog.setDefaultCloseOperation(2);
        optionDialog.setTitle("Plot options");
        optionDialog.setResizable(false);
        optionDialog.setModal(true);
        optionDialog.pack();
        optionDialog.show();
    }

    private void resetDefaultPlotOptions(int i)
    {
        if(i != 1)
        {
            f1Min = 200;
            f1Max = 800;
            f1Step = 100;
            f2Min = 500;
            f2Max = 1800;
            f2Step = 200;
            defaultPlotWidth = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
            defaultPlotHeight = Toolkit.getDefaultToolkit().getScreenSize().height / 2;
        }
        if(i != 0)
        {
            ptSize = 7;
            drawGrid = false;
            drawMean = true;
            drawNeither = false;
            drawSymbols = false;
            drawEllipses = true;
            drawSDBars = false;
            drawBlob = true;
            inColor = false;
            plotWidth = defaultPlotWidth;
            plotHeight = defaultPlotHeight;
            plotTitle = "";
        }
    }

    private void refreshOptionDialog(int i)
    {
        if(i != 1)
        {
            minF1Field.setText("" + f1Min);
            maxF1Field.setText("" + f1Max);
            stepF1Field.setText("" + f1Step);
            minF2Field.setText("" + f2Min);
            maxF2Field.setText("" + f2Max);
            stepF2Field.setText("" + f2Step);
            plotHeightField.setText("" + plotHeight);
            plotWidthField.setText("" + plotWidth);
        }
        if(i != 0)
        {
            gridYesNo.setSelected(drawGrid);
            drawSymbolsYesNo.setSelected(drawSymbols && !drawNeither);
            drawPointsYesNo.setSelected(!drawSymbols && !drawNeither);
            drawNeitherYesNo.setSelected(drawNeither);
            drawMeanYesNo.setSelected(drawMean);
            drawSDBarsYesNo.setSelected(drawSDBars);
            drawEllipsesYesNo.setSelected(drawEllipses);
            pointSizeSlider.setValue(ptSize);
            plotTitleField.setText(plotTitle);
            inColorYesNo.setSelected(inColor);
            drawBlobYesNo.setEnabled(drawMean);
            drawBigSymbolYesNo.setEnabled(drawMean);
        }
    }

    private void refreshCreateDialog()
    {
        vowelDescriptionField.setText(currentVowel.getDescription());
        vowelSymbolField.setText(currentVowel.getSymbol());
        f1Field.setText("");
        f2Field.setText("");
        currentFormantListModel = currentVowel.getFormantListModel();
        currentFormantList.setModel(currentFormantListModel);
        currentFormantList.setSelectedIndex(currentFormantListModel.getSize() - 1);
        currentFormantList.ensureIndexIsVisible(currentFormantListModel.getSize() - 1);
        if(currentVowel.getDescription().equals(""))
            vowelDescriptionField.grabFocus();
        else
            f1Field.grabFocus();
    }

    private void initializeVariables()
    {
        ipaSymbol = "";
        vowelDescription = "";
        currentFormantListModel = new DefaultListModel();
        currentFormantList = new JList(currentFormantListModel);
        editExistingPair = false;
        creatingNewVowel = true;
        editedPairIndex = 0;
        setHasBeenModified = false;
        saveSet = null;
        vowelVector = new Vector();
        vowelDescriptionListModel = new DefaultListModel();
        currentVowelIndexInList = 0;
        stopShowingValuesChangedConfirmDialog = 0;
    }

    private void resetVariables()
    {
        ipaSymbol = "";
        vowelDescription = "";
        editExistingPair = false;
        editedPairIndex = 0;
        setHasBeenModified = false;
        saveSet = null;
    }

    private void setSelectedOptions()
    {
        boolean flag = true;
        try
        {
            drawGrid = gridYesNo.isSelected();
            drawNeither = drawNeitherYesNo.isSelected();
            drawSymbols = drawSymbolsYesNo.isSelected() && !drawNeither;
            drawBlob = drawBlobYesNo.isSelected();
            inColor = inColorYesNo.isSelected();
            drawMean = drawMeanYesNo.isSelected();
            drawSDBars = drawSDBarsYesNo.isSelected();
            drawEllipses = drawEllipsesYesNo.isSelected();
            f1Min = Integer.parseInt(minF1Field.getText());
            f1Max = Integer.parseInt(maxF1Field.getText());
            f1Step = Integer.parseInt(stepF1Field.getText());
            f2Min = Integer.parseInt(minF2Field.getText());
            f2Max = Integer.parseInt(maxF2Field.getText());
            f2Step = Integer.parseInt(stepF2Field.getText());
            plotHeight = Integer.parseInt(plotHeightField.getText());
            plotWidth = Integer.parseInt(plotWidthField.getText());
            plotTitle = (plotTitleField.getText() + " ").trim();
            ptSize = pointSizeSlider.getValue();
            flag = true;
        }
        catch(NumberFormatException numberformatexception)
        {
            flag = false;
            JOptionPane.showMessageDialog(null, "You have entered one or more invalid values.\nPlease check your values.", "Invalid value(s) entered", 0);
            minF1Field.grabFocus();
        }
        finally
        {
            boolean flag1 = false;
            if(f1Min < 200)
            {
                f1Min = 200;
                flag1 = true;
            }
            if(f1Max > 800)
            {
                f1Max = 800;
                flag1 = true;
            }
            if(f2Min < 500)
            {
                f2Min = 500;
                flag1 = true;
            }
            if(f2Max > 1800)
            {
                f2Max = 1800;
                flag1 = true;
            }
            if(f1Step < 50)
            {
                f1Step = 50;
                flag1 = true;
            }
            if(f1Step > 1000)
            {
                f1Step = 1000;
                flag1 = true;
            }
            if(f2Step < 100)
            {
                f2Step = 100;
                flag1 = true;
            }
            if(f2Step > 1000)
            {
                f2Step = 1000;
                flag1 = true;
            }
            if(plotHeight < 200)
            {
                plotHeight = 200;
                flag1 = true;
            }
            if(plotHeight > MAX_PLOT_HEIGHT)
            {
                plotHeight = MAX_PLOT_HEIGHT;
                flag1 = true;
            }
            if(plotWidth < 300)
            {
                plotWidth = 300;
                flag1 = true;
            }
            if(plotWidth > MAX_PLOT_WIDTH)
            {
                plotWidth = MAX_PLOT_WIDTH;
                flag1 = true;
            }
            if(flag1 && stopShowingValuesChangedConfirmDialog == 0)
                stopShowingValuesChangedConfirmDialog = JOptionPane.showOptionDialog(null, "One or more of the values you entered were too high or too low.\nThey have been automatically adjusted to the maximum or minimum possible values.", "Values have been adjusted", 0, 3, null, stopShowingValuesChangedOptions, stopShowingValuesChangedOptions[0]);
            if(flag)
                optionDialog.dispose();
        }
    }

    private void exitProgram(boolean flag)
    {
        boolean flag1 = false;
        if(flag)
        {
            int i = JOptionPane.showOptionDialog(null, "The set has been modified.Would you like to save the changes?", "Exit JPlotFormants", 1, 3, null, exitOptions, exitOptions[0]);
            if(i == 0)
            {
                saveDialog();
                System.exit(0);
            } else
            if(i == 1)
                System.exit(0);
        } else
        {
            int j = JOptionPane.showConfirmDialog(null, "Do you really want to exit JPlotFormants?", "Exit JPlotFormants", 0, 3);
            if(j == 0)
                System.exit(0);
        }
    }

    private int offerToAdjustOptions(String s, String s1, String s2)
    {
        String as[] = {
            "Yes, adjust the plot options", "No, let me change the value I entered"
        };
        return JOptionPane.showOptionDialog(null, "The " + s + " value you have entered is rather " + s1 + "." + "\n" + "Would you like to use this value as the minimum " + s + " in the plot," + "\n" + "or would you rather change the " + s + " you have entered?", "Unexpected " + s + " entered", 0, 3, null, as, as[0]);
    }

    private void enableMenuCommandsIfEmptySet(boolean flag)
    {
        plotMenuItems[0].setEnabled(flag);
        fileMenuItems[3].setEnabled(flag);
        fileMenuItems[1].setEnabled(flag);
        fileMenuItems[4].setEnabled(flag);
    }

    private void enableButtonsIfEmptySet(boolean flag)
    {
        deleteSelectedBtn.setEnabled(flag);
        editSelectedBtn.setEnabled(flag);
    }

    private boolean areThereFormantsInMemory()
    {
        boolean flag = false;
        for(int i = 0; i < vowelVector.size();)
        {
            if(!((Vowel)vowelVector.elementAt(i)).getFormantListModel().isEmpty())
                flag = true;
            break;
        }

        return flag;
    }

    private void openFormantSet(File file)
    {
        boolean flag = false;
        Vowel vowel = null;
        try
        {
            initializeVariables();
            FileReader filereader = new FileReader(file.getPath());
            BufferedReader bufferedreader = new BufferedReader(filereader);
            boolean flag1 = false;
            String s = "";
            boolean flag2 = false;
            while(!flag && !flag1) 
            {
                String s1 = bufferedreader.readLine().toString();
                if(!s1.startsWith("JPlotFormants data file"))
                {
                    flag = true;
                    break;
                }
                for(; !s1.startsWith("[END OF FILE]") && !flag1; vowelDescriptionListModel.addElement(vowel.getDescription()))
                {
                    s1 = bufferedreader.readLine().toString();
                    if(s1.startsWith("[END OF FILE]"))
                    {
                        flag1 = true;
                        break;
                    }
                    if(!s1.startsWith("[Vowel description"))
                    {
                        flag = true;
                        break;
                    }
                    vowelDescription = s1.substring(s1.indexOf('=') + 2, s1.length());
                    s1 = bufferedreader.readLine().toString();
                    if(!s1.startsWith("[Vowel symbol"))
                    {
                        flag = true;
                        break;
                    }
                    ipaSymbol = s1.substring(s1.indexOf('=') + 2, s1.length());
                    if(!flag)
                    {
                        vowel = new Vowel(vowelDescription, ipaSymbol);
                        for(; !s1.startsWith("[END OF THIS VOWEL"); vowel.addFormant(s1))
                        {
                            String s2 = bufferedreader.readLine();
                            if(s2 == null)
                            {
                                flag1 = true;
                                break;
                            }
                            s1 = s2.toString();
                            if(!s1.equals("") && !s1.startsWith("["))
                            {
                                flag1 = true;
                                flag = true;
                                break;
                            }
                            if(s1.startsWith("[END OF THIS VOWEL"))
                                break;
                        }

                    }
                    vowelVector.add(vowel);
                }

            }
            bufferedreader.close();
        }
        catch(IOException ioexception)
        {
            System.out.println("IOException " + ioexception);
        }
        catch(NullPointerException nullpointerexception)
        {
            System.out.println("NullPointerException " + nullpointerexception);
        }
        finally
        {
            if(flag)
                JOptionPane.showMessageDialog(null, "The file may have been corrupted,\nor it may not be a JPlotFormants file.", "Error opening the file", 2);
            enableMenuCommandsIfEmptySet(areThereFormantsInMemory());
        }
    }

    private void displayFormants()
    {
        final JDialog displayDialog = new JDialog();
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout());
        JTextArea jtextarea = new JTextArea(20, 15);
        jtextarea.setWrapStyleWord(true);
        jtextarea.setLineWrap(true);
        jtextarea.setFont(new Font("Serif", 0, 12));
        jtextarea.setEditable(false);
        for(int i = 0; i < vowelVector.size(); i++)
        {
            Vowel vowel = (Vowel)vowelVector.elementAt(i);
            jtextarea.append("Vowel description = " + vowel.getDescription() + "\n");
            jtextarea.append("Vowel symbol = " + vowel.getSymbol() + "\n");
            for(int j = 0; j < vowel.getFormantListModel().getSize(); j++)
                jtextarea.append(vowel.getFormantListModel().getElementAt(j).toString() + "\n");

            jtextarea.append("\n");
        }

        JScrollPane jscrollpane = new JScrollPane(jtextarea);
        JButton jbutton = new JButton("Close window");
        jbutton.setMnemonic('C');
        jbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                displayDialog.dispose();
            }

        });
        jpanel.add(jscrollpane, "Center");
        jpanel.add(jbutton, "South");
        jpanel.setPreferredSize(new Dimension(300, 350));
        displayDialog.setContentPane(jpanel);
        displayDialog.setLocation(100, 50);
        displayDialog.setModal(true);
        displayDialog.setTitle("Formants in memory");
        displayDialog.pack();
        displayDialog.show();
    }

    private void saveDialog()
    {
        int i = 0;
        fc.setFileFilter(fFilter);
        int j = fc.showSaveDialog(null);
        if(j == 0)
        {
            saveSet = fc.getSelectedFile();
            if(FormantFilter.getSuffix(saveSet).equals(""))
                saveSet = new File(saveSet.toString() + ".for");
            if(saveSet != null)
            {
                if(saveSet.exists())
                    i = JOptionPane.showConfirmDialog(null, "The file " + saveSet.getName() + " already exists." + "\n" + "Do you really wish to replace it?", "Save file", 1, 3);
                if(i == 0)
                    saveFormantSet(saveSet);
            }
        }
    }

    private void saveFormantSet(File file)
    {
        try
        {
            FileWriter filewriter = new FileWriter(file.getPath());
            BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
            bufferedwriter.write("JPlotFormants data file\n");
            for(int i = 0; i < vowelVector.size(); i++)
            {
                Vowel vowel = (Vowel)vowelVector.elementAt(i);
                bufferedwriter.write("[Vowel description] := " + vowel.getDescription() + "\n");
                bufferedwriter.write("[Vowel symbol] := " + vowel.getSymbol() + "\n");
                for(int j = 0; j < vowel.getFormantListModel().getSize(); j++)
                    bufferedwriter.write(vowel.getFormantListModel().getElementAt(j) + "\n");

                bufferedwriter.write("[END OF THIS VOWEL]\n");
            }

            bufferedwriter.write("[END OF FILE]\n");
            bufferedwriter.close();
            setHasBeenModified = false;
        }
        catch(IOException ioexception)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while saving the formant data.", "Error saving the file", 2);
        }
        catch(NullPointerException nullpointerexception)
        {
            System.out.println("NullPointerException " + nullpointerexception + " while saving.");
        }
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        if(listselectionevent.getValueIsAdjusting())
            return;
        if(vowelDescriptionList.isSelectionEmpty())
        {
            return;
        } else
        {
            int i = ((JList)listselectionevent.getSource()).getSelectedIndex();
            currentVowel = (Vowel)vowelVector.get(i);
            currentVowelIndexInList = i;
            creatingNewVowel = false;
            refreshCreateDialog();
            return;
        }
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        HelpPage helppage;
        if(obj == fileMenuItems[0])
            enterFormants();
        else
        if(obj == fileMenuItems[2])
        {
            int i = 1;
            if(setHasBeenModified && areThereFormantsInMemory())
                i = JOptionPane.showOptionDialog(null, "The set has been modified.\nWould you like to save the changes\nbefore opening a new set of formant pairs?", "Open set of formant pairs", 1, 3, null, openOptions, openOptions[0]);
            if(i == 0)
                if(openSet != null)
                    saveFormantSet(openSet);
                else
                if(saveSet != null)
                    saveFormantSet(saveSet);
                else
                    saveDialog();
            if(i != 2)
            {
                fc.setFileFilter(fFilter);
                int i1 = fc.showOpenDialog(null);
                if(i1 == 0)
                {
                    openSet = fc.getSelectedFile();
                    if(openSet != null)
                        openFormantSet(openSet);
                }
            }
        } else
        if(obj == fileMenuItems[1])
            displayFormants();
        else
        if(obj == fileMenuItems[3])
            saveDialog();
        else
        if(obj == fileMenuItems[4])
        {
            boolean flag = false;
            if(setHasBeenModified && areThereFormantsInMemory())
            {
                int j = JOptionPane.showOptionDialog(null, "The set has been modified.Would you like to save the changes\nbefore erasing the formant pairs in memory?", "Erase formants in memory", 1, 3, null, closeOptions, closeOptions[0]);
                if(j == 0)
                {
                    if(openSet != null)
                        saveFormantSet(openSet);
                    else
                    if(saveSet != null)
                        saveFormantSet(saveSet);
                    else
                        saveDialog();
                    initializeVariables();
                    enableMenuCommandsIfEmptySet(false);
                } else
                if(j == 1)
                {
                    initializeVariables();
                    enableMenuCommandsIfEmptySet(false);
                }
            } else
            if(areThereFormantsInMemory())
            {
                int k = JOptionPane.showConfirmDialog(null, "This will erase all the formant pairs in memory.\nDo you really wish to continue?", "Open file", 1, 3);
                if(k == 0)
                    initializeVariables();
                enableMenuCommandsIfEmptySet(false);
            }
        } else
        if(obj == plotMenuItems[0])
            plotSet();
        else
        if(obj == plotMenuItems[1])
            plotOptions();
        else
        if(obj == helpMenuItems[0])
            helppage = new HelpPage(false);
        else
        if(obj == helpMenuItems[1])
            helppage = new HelpPage(true);
        else
        if(obj == helpMenuItems[2])
            JOptionPane.showMessageDialog(null, "JPlotFormants v1.4\n(c) Roger Billerey-Mosier 2001\nrogthefrog@gmail.com\nAfter Plot Formants by P. Ladefoged\nEllipse code help by Marek Przezdziecki", "About JPlotFormants", 1);
        else
        if(obj == fileMenuItems[5])
            exitProgram(setHasBeenModified);
        else
        if(obj == deleteVowelBtn && !vowelDescriptionList.isSelectionEmpty() && JOptionPane.showConfirmDialog(null, "Do you really want to delete \"" + vowelDescriptionListModel.getElementAt(vowelDescriptionList.getSelectedIndex()) + "\"?", "Delete vowel?", 0, 3) == 0)
        {
            vowelVector.removeElementAt(vowelDescriptionList.getSelectedIndex());
            vowelDescriptionListModel.removeElementAt(vowelDescriptionList.getSelectedIndex());
            createStatusTextArea.setText("Vowel deleted.");
            if(vowelDescriptionListModel.isEmpty())
            {
                creatingNewVowel = true;
                currentVowelIndexInList = 0;
                currentVowel = new Vowel();
            } else
            {
                creatingNewVowel = false;
                currentVowelIndexInList = vowelDescriptionListModel.getSize() - 1;
                currentVowel = (Vowel)vowelVector.elementAt(currentVowelIndexInList);
            }
            setHasBeenModified = true;
            refreshCreateDialog();
        } else
        if(obj == createNewVowelBtn)
        {
            currentVowel = new Vowel();
            currentVowelFormantListModel = currentVowel.getFormantListModel();
            creatingNewVowel = true;
            vowelDescriptionList.clearSelection();
            refreshCreateDialog();
            vowelDescriptionField.grabFocus();
        } else
        if(obj == doneBtn)
            createDialog.dispose();
        else
        if(obj == editSelectedBtn && !currentFormantList.isSelectionEmpty())
        {
            editedPairIndex = currentFormantList.getSelectedIndex();
            editExistingPair = true;
            Point point = Point.extractFormattedPoint(currentFormantListModel.getElementAt(editedPairIndex).toString());
            f1Field.setText("" + point.getX());
            f2Field.setText("" + point.getY());
            createStatusTextArea.setText("Editing an existing formant pair.");
            f1Field.grabFocus();
        } else
        if(obj == deleteSelectedBtn && !currentFormantList.isSelectionEmpty())
        {
            boolean flag1 = false;
            try
            {
                int l = currentFormantList.getSelectedIndex();
                createStatusTextArea.setText("Formant pair deleted.");
                currentFormantListModel.removeElementAt(l);
                if(currentFormantListModel.getSize() == 0)
                {
                    enableMenuCommandsIfEmptySet(areThereFormantsInMemory());
                    enableButtonsIfEmptySet(areThereFormantsInMemory());
                }
                setHasBeenModified = true;
                f1Field.grabFocus();
            }
            catch(NullPointerException nullpointerexception)
            {
                createStatusTextArea.setText("NullPointerException " + nullpointerexception);
            }
            catch(ArrayIndexOutOfBoundsException arrayindexoutofboundsexception)
            {
                createStatusTextArea.setText("ArrayOutOfBoundsException " + arrayindexoutofboundsexception);
            }
            finally
            {
                f1Field.grabFocus();
            }
        } else
        if(obj == saveFormantBtn && (vowelDescriptionField.getText() + " ").trim().equals("") && (vowelSymbolField.getText() + " ").trim().equals(""))
        {
            createStatusTextArea.setText("Please enter at least a description or a symbol to identify this formant set.");
            vowelDescriptionField.grabFocus();
        } else
        if(obj == saveFormantBtn && currentVowel.hasBeenCreated() && !(vowelSymbolField.getText() + " ").trim().equals("") && (!(vowelDescriptionField.getText() + " ").trim().equals(currentVowel.getDescription()) || !(vowelSymbolField.getText() + " ").trim().substring(0, (vowelSymbolField.getText() + " ").trim().length() < currentVowel.getSymbol().length() ? (vowelSymbolField.getText() + " ").trim().length() : currentVowel.getSymbol().length()).equals(currentVowel.getSymbol())))
        {
            currentVowel.setDescription((vowelDescriptionField.getText() + " ").trim());
            currentVowel.setSymbol((vowelSymbolField.getText() + " ").trim());
            if(vowelDescriptionListModel.isEmpty())
                vowelDescriptionListModel.addElement(vowelDescription);
            else
                vowelDescriptionListModel.setElementAt(currentVowel.getDescription(), currentVowelIndexInList);
            f1Field.grabFocus();
        } else
        if(obj == saveFormantBtn && (!JPlotFormantsHelper.isNumber(f1Field.getText()) || !JPlotFormantsHelper.isNumber(f2Field.getText())))
        {
            if(f1Field.getText().equals(""))
            {
                createStatusTextArea.setText("Please enter a value for F1.");
                f1Field.grabFocus();
            } else
            if(!JPlotFormantsHelper.isNumber(f1Field.getText()))
            {
                createStatusTextArea.setText("You have entered an invalid value for F1.");
                f1Field.grabFocus();
            } else
            if(f2Field.getText().equals(""))
            {
                createStatusTextArea.setText("Please enter a value for F2.");
                f2Field.grabFocus();
            } else
            if(!JPlotFormantsHelper.isNumber(f2Field.getText()))
            {
                createStatusTextArea.setText("You have entered an invalid value for F2.");
                f2Field.grabFocus();
            }
        } else
        if(obj == saveFormantBtn && JPlotFormantsHelper.isNumber(f1Field.getText()) && JPlotFormantsHelper.isNumber(f2Field.getText()))
        {
            boolean flag2 = true;
            Point point1 = null;
            String s = (f1Field.getText() + " ").trim();
            String s1 = (f2Field.getText() + " ").trim();
            String s2 = (vowelDescriptionField.getText() + " ").trim();
            String s3 = (vowelSymbolField.getText() + " ").trim();
            try
            {
                point1 = new Point(Integer.parseInt(s.trim()), Integer.parseInt(s1.trim()));
            }
            catch(NumberFormatException numberformatexception)
            {
                createStatusTextArea.setText("NumberFormatException " + numberformatexception + "\n" + "Please check your F1 and F2 values.");
                flag2 = false;
                f1Field.grabFocus();
            }
            finally
            {
                if(flag2)
                {
                    if(point1.getX() < f1Min && point1.getX() >= 50)
                    {
                        if(offerToAdjustOptions("F1", "low", "minimum") == 0)
                        {
                            f1Min = point1.getX();
                        } else
                        {
                            flag2 = false;
                            f1Field.grabFocus();
                        }
                    } else
                    if(point1.getX() < 50)
                    {
                        flag2 = false;
                        JOptionPane.showMessageDialog(null, "This F1 value is too low.\nPlease enter a value between " + f1Min + " and " + f1Max + ".", "F1 value too low", 2);
                        f1Field.grabFocus();
                    } else
                    if(point1.getX() > f1Max && point1.getX() <= 1400)
                    {
                        if(offerToAdjustOptions("F1", "high", "maximum") == 0)
                        {
                            f1Max = point1.getX();
                        } else
                        {
                            flag2 = false;
                            f1Field.grabFocus();
                        }
                    } else
                    if(point1.getX() > 1400)
                    {
                        flag2 = false;
                        JOptionPane.showMessageDialog(null, "This F1 value is too high.\nPlease enter a value between " + f1Min + " and " + f1Max + ".", "F1 value too high", 2);
                        f1Field.grabFocus();
                    }
                    if(flag2)
                        if(point1.getY() < f2Min && point1.getY() >= 300)
                        {
                            if(offerToAdjustOptions("F2", "low", "minimum") == 0)
                            {
                                f2Min = point1.getY();
                            } else
                            {
                                flag2 = false;
                                f2Field.grabFocus();
                            }
                        } else
                        if(point1.getY() < 300)
                        {
                            flag2 = false;
                            JOptionPane.showMessageDialog(null, "This F2 value is too low.\nPlease enter a value between " + f2Min + " and " + f2Max + ".", "F2 value too low", 2);
                            f2Field.grabFocus();
                        } else
                        if(point1.getY() > f2Max && point1.getY() <= 4000)
                        {
                            if(offerToAdjustOptions("F2", "high", "maximum") == 0)
                            {
                                f2Max = point1.getY();
                            } else
                            {
                                flag2 = false;
                                f2Field.grabFocus();
                            }
                        } else
                        if(point1.getY() > 4000)
                        {
                            flag2 = false;
                            JOptionPane.showMessageDialog(null, "This F2 value is too high.\nPlease enter a value between " + f2Min + " and " + f2Max + ".", "F2 value too high", 2);
                            f2Field.grabFocus();
                        }
                }
            }
            if(flag2)
            {
                enableMenuCommandsIfEmptySet(true);
                enableButtonsIfEmptySet(true);
                if(!editExistingPair)
                {
                    if(!(vowelSymbolField.getText() + " ").trim().equals(""))
                        ipaSymbol = (vowelSymbolField.getText() + " ").trim().substring(0, (vowelSymbolField.getText() + " ").trim().length() < 2 ? 1 : 2);
                    else
                        ipaSymbol = "";
                    if(!(vowelDescriptionField.getText() + " ").trim().equals(""))
                        vowelDescription = (vowelDescriptionField.getText() + " ").trim();
                    currentVowel.setDescription(vowelDescription);
                    currentVowel.setSymbol(ipaSymbol);
                    currentVowel.addFormant(point1);
                    if(creatingNewVowel)
                    {
                        vowelDescriptionListModel.addElement(vowelDescription);
                        vowelVector.add(currentVowel);
                        currentVowelIndexInList = vowelVector.size() - 1;
                        creatingNewVowel = false;
                    } else
                    {
                        vowelDescriptionListModel.setElementAt(vowelDescription, currentVowelIndexInList);
                        vowelVector.setElementAt(currentVowel, currentVowelIndexInList);
                    }
                } else
                {
                    currentFormantListModel.setElementAt(point1.pointToString(), editedPairIndex);
                    currentFormantList.setSelectedIndex(editedPairIndex);
                    editExistingPair = false;
                    editedPairIndex = 0;
                }
                setHasBeenModified = true;
                refreshCreateDialog();
                createStatusTextArea.setText("Formant pair saved.");
            }
        } else
        if(obj == resetDefaultBtn)
        {
            resetDefaultPlotOptions(optionsTabPane.getSelectedIndex());
            refreshOptionDialog(optionsTabPane.getSelectedIndex());
            minF1Field.grabFocus();
        } else
        if(obj == okBtn)
            setSelectedOptions();
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        Object obj = itemevent.getSource();
        if(obj == drawMeanYesNo)
        {
            drawBlobYesNo.setEnabled(drawMeanYesNo.isSelected());
            drawBigSymbolYesNo.setEnabled(drawMeanYesNo.isSelected());
        }
    }

    public void windowClosing(WindowEvent windowevent)
    {
        exitProgram(setHasBeenModified);
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
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
        SplashWindow3 splashwindow3 = new SplashWindow3("JPlotFormantsSplash.gif", null, 4000);
        JPlotFormants jplotformants = new JPlotFormants();
    }

    private JPanel mainPane;
    private JDialog createDialog;
    private JTextArea createStatusTextArea;
    private JList currentFormantList;
    private static JList vowelDescriptionList;
    private JScrollPane vowelDescriptionListScrollPane;
    private JScrollPane currentFormantListScrollPane;
    private JLabel vowelDescriptionLabel;
    private JLabel vowelBoxLabel;
    private JLabel vowelSymbolLabel;
    private JLabel f1Label;
    private JLabel f2Label;
    private JLabel listLabel;
    private JTextField vowelDescriptionField;
    private JTextField vowelSymbolField;
    private JTextField f1Field;
    private JTextField f2Field;
    private JButton editSelectedBtn;
    private JButton deleteSelectedBtn;
    private JButton doneBtn;
    private JButton createNewVowelBtn;
    private JButton showSymbolsBtn;
    private JButton saveFormantBtn;
    private JButton deleteVowelBtn;
    private JDialog optionDialog;
    private JTabbedPane optionsTabPane;
    private JCheckBox gridYesNo;
    private JCheckBox drawEllipsesYesNo;
    private JCheckBox drawSDBarsYesNo;
    private JCheckBox drawMeanYesNo;
    private JCheckBox inColorYesNo;
    private JRadioButton drawSymbolsYesNo;
    private JRadioButton drawPointsYesNo;
    private JRadioButton drawNeitherYesNo;
    private JRadioButton drawBlobYesNo;
    private JRadioButton drawBigSymbolYesNo;
    private ButtonGroup drawSymbolsGroup;
    private ButtonGroup drawBlobGroup;
    private JLabel minF1Label;
    private JLabel maxF1Label;
    private JLabel stepF1Label;
    private JLabel minF2Label;
    private JLabel maxF2Label;
    private JLabel stepF2Label;
    private JLabel plotWidthLabel;
    private JLabel plotHeightLabel;
    private JLabel plotTitleLabel;
    private JLabel pointSizeLabel;
    private JSlider pointSizeSlider;
    private JTextField minF1Field;
    private JTextField maxF1Field;
    private JTextField stepF1Field;
    private JTextField minF2Field;
    private JTextField maxF2Field;
    private JTextField stepF2Field;
    private JTextField plotWidthField;
    private JTextField plotHeightField;
    private JTextField plotTitleField;
    private JButton resetDefaultBtn;
    private JButton okBtn;
    private static int stopShowingValuesChangedConfirmDialog;
    private static final String stopShowingValuesChangedOptions[] = {
        "All right", "Fine, stop telling me about it"
    };
    private JMenu fileMenu;
    private JMenu plotMenu;
    private JMenu helpMenu;
    private JMenuItem fileMenuItems[];
    private JMenuItem plotMenuItems[];
    private JMenuItem helpMenuItems[];
    private JMenuBar menuBar;
    private final String fileMenuItemNames[] = {
        "Enter/edit formant pairs", "Show formants in memory", "Open formant pair set", "Save formant pair set as...", "Close current formant pair set", "Exit"
    };
    private final char fileMenuItemMnemonic[] = {
        'E', 'h', 'O', 'S', 'C', 'x'
    };
    private static final int CREATE_MENU_ITEM = 0;
    private static final int DISPLAY_MENU_ITEM = 1;
    private static final int OPEN_MENU_ITEM = 2;
    private static final int SAVE_MENU_ITEM = 3;
    private static final int CLOSE_MENU_ITEM = 4;
    private static final int EXIT_MENU_ITEM = 5;
    private final String plotMenuItemNames[] = {
        "Plot current set", "Plot options"
    };
    private final char plotMenuItemMnemonic[] = {
        'P', 'o'
    };
    private final String helpMenuItemNames[] = {
        "User's manual", "Go to website (updates & help)", "About JPlotFormants"
    };
    private final char helpMenuItemMnemonic[] = {
        'm', 'w', 'A'
    };
    private static final int HELP_MENU_ITEM = 0;
    private static final int WEBSITE_MENU_ITEM = 1;
    private static final int ABOUT_MENU_ITEM = 2;
    private static final int DEFAULT_POINT_SIZE = 7;
    private static final int DEFAULT_F1_MIN = 200;
    private static final int DEFAULT_F1_MAX = 800;
    private static final int ABSOLUTE_F1_MIN = 50;
    private static final int ABSOLUTE_F1_MAX = 1400;
    private static final int DEFAULT_F2_MIN = 500;
    private static final int DEFAULT_F2_MAX = 1800;
    private static final int ABSOLUTE_F2_MIN = 300;
    private static final int ABSOLUTE_F2_MAX = 4000;
    private static final int DEFAULT_F1_STEP = 100;
    private static final int DEFAULT_F2_STEP = 200;
    private static final int ABSOLUTE_F1_STEP_MIN = 50;
    private static final int ABSOLUTE_F1_STEP_MAX = 1000;
    private static final int ABSOLUTE_F2_STEP_MIN = 100;
    private static final int ABSOLUTE_F2_STEP_MAX = 1000;
    private static final int MIN_PLOT_WIDTH = 300;
    private static final int MIN_PLOT_HEIGHT = 200;
    private final int MAX_PLOT_WIDTH;
    private final int MAX_PLOT_HEIGHT;
    private static final boolean DEFAULT_DRAW_GRID = false;
    private static final boolean DEFAULT_DRAW_SYMBOLS = false;
    private static final boolean DEFAULT_DRAW_MEAN = true;
    private static final boolean DEFAULT_DRAW_NEITHER = false;
    private static final boolean DEFAULT_DRAW_ELLIPSES = true;
    private static final boolean DEFAULT_DRAW_SD_BARS = false;
    private static final boolean DEFAULT_DRAW_BLOB = true;
    private static final boolean DEFAULT_IN_COLOR = false;
    private int defaultPlotWidth;
    private int defaultPlotHeight;
    private String defaultPlotTitle;
    private int origin;
    private int orientation;
    private final int TOP_RIGHT = 0;
    private final int BOTTOM_RIGHT = 1;
    private final int BOTTOM_LEFT = 2;
    private final int TOP_LEFT = 3;
    private static int f1Min;
    private static int f1Max;
    private static int f2Min;
    private static int f2Max;
    private static int f1Step;
    private static int f2Step;
    private static int ptSize;
    private static boolean drawGrid;
    private static boolean drawSymbols;
    private static boolean drawNeither;
    private static boolean drawMean = true;
    private static boolean drawEllipses = true;
    private static boolean drawSDBars = false;
    private static boolean drawBlob = true;
    private static boolean inColor;
    private static int plotWidth;
    private static int plotHeight;
    private static String plotTitle;
    private String ipaSymbol;
    private String vowelDescription;
    private DefaultListModel currentFormantListModel;
    private static boolean editExistingPair;
    private static boolean creatingNewVowel;
    private static int editedPairIndex;
    private static boolean setHasBeenModified;
    private static File saveSet = null;
    private static File openSet = null;
    private static Vector vowelVector;
    private static DefaultListModel vowelDescriptionListModel;
    private Vowel currentVowel;
    private DefaultListModel currentVowelFormantListModel;
    private String currentVowelDescription;
    private String currentVowelSymbol;
    private static int currentVowelIndexInList;
    private final String exitOptions[] = {
        "Save and exit", "Exit without saving", "Cancel"
    };
    private final String closeOptions[] = {
        "Save and clear", "Clear without saving", "Cancel"
    };
    private final String openOptions[] = {
        "Save and open new", "Open without saving", "Cancel"
    };
    private final String newline = "\n";
    private final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
    private final FormantFilter fFilter = new FormantFilter();
    private final int DIMENSIONS_ONLY = 0;
    private final int DRAWING_OPTIONS_ONLY = 1;















}