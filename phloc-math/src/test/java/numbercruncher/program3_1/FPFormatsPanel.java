package numbercruncher.program3_1;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import numbercruncher.mathutils.IEEE754;
import numbercruncher.mathutils.IEEE754Constants;

/**
 * The demo panel for the IEEE 754 program and applet.
 */
class FPFormatsPanel extends Panel
{
  private static final Color MAROON = new Color (128, 0, 0);

  private boolean isFloatExponentOK = true;
  private boolean isDoubleExponentOK = true;

  // Main, float, and double panels.
  private final Panel mainPanel = new Panel ();
  private final Panel floatPanel = new Panel ();
  private final Panel doublePanel = new Panel ();

  // Float label and show value button.
  private final Label floatLabel = new Label ();
  private final Button fShowValueButton = new Button ();

  // Double label and show value button.
  private final Label doubleLabel = new Label ();
  private final Button dShowValueButton = new Button ();

  // Float sign and message.
  private final Label fSignBitLabel = new Label ();
  private final TextField fSignBitText = new TextField ();
  private final Label fMessageText = new Label ();

  // Double sign and message.
  private final Label dSignBitLabel = new Label ();
  private final TextField dSignBitText = new TextField ();
  private final Label dMessageText = new Label ();

  // Float exponent.
  private final Label fExponentBitsLabel = new Label ();
  private final TextField fExponentBitsText = new TextField ();
  private final Label fBiasedLabel = new Label ();
  private final TextField fBiasedText = new TextField ();
  private final Label fUnbiasedLabel = new Label ();
  private final TextField fUnbiasedText = new TextField ();

  // Double exponent.
  private final Label dExponentBitsLabel = new Label ();
  private final TextField dExponentBitsText = new TextField ();
  private final Label dBiasedLabel = new Label ();
  private final TextField dBiasedText = new TextField ();
  private final Label dUnbiasedLabel = new Label ();
  private final TextField dUnbiasedText = new TextField ();

  // Float fraction and significand.
  private final Label fFractionBitsLabel = new Label ();
  private final TextField fFractionBitsText = new TextField ();
  private final Label fSignificandLabel = new Label ();
  private final Label fSignificandText = new Label ();

  // Double fraction and significand.
  private final Label dFractionBitsLabel = new Label ();
  private final TextField dFractionBitsText = new TextField ();
  private final Label dSignificandLabel = new Label ();
  private final Label dSignificandText = new Label ();

  // Float value and decompose button.
  private final Panel fValuePanel = new Panel ();
  private final Label fValueLabel = new Label ();
  private final TextField fValueText = new TextField ();
  private final Button fDecomposeButton = new Button ();

  // Double value and decompose button.
  private final Panel dValuePanel = new Panel ();
  private final Label dvalueLabel = new Label ();
  private final TextField dValueText = new TextField ();
  private final Button dDecomposeButton = new Button ();

  // Layout managers.
  private final BorderLayout borderLayout1 = new BorderLayout ();
  GridLayout gridLayout1 = new GridLayout ();
  GridBagLayout gridBagLayout1 = new GridBagLayout ();
  GridBagLayout gridBagLayout2 = new GridBagLayout ();
  GridBagLayout gridBagLayout3 = new GridBagLayout ();
  GridBagLayout gridBagLayout4 = new GridBagLayout ();

  /**
   * Constructor.
   */
  FPFormatsPanel ()
  {
    jbInit ();
    initHandlers ();
  }

  /**
   * Initialize the GUI components.
   */
  private void jbInit ()
  {
    this.setBackground (Color.white);
    this.setSize (600, 460);
    this.setLayout (borderLayout1);

    // Main panel.
    mainPanel.setLayout (gridLayout1);

    // Float panel.
    floatPanel.setBackground (SystemColor.controlHighlight);
    floatPanel.setLayout (gridBagLayout1);

    // Double panel.
    doublePanel.setBackground (SystemColor.controlHighlight);
    doublePanel.setLayout (gridBagLayout2);

    // Float label and show value button.
    floatLabel.setForeground (Color.blue);
    floatLabel.setFont (new java.awt.Font ("Dialog", 1, 14));
    floatLabel.setText ("float");
    fShowValueButton.setLabel ("Show value");

    // Double label and show value button.
    doubleLabel.setForeground (Color.blue);
    doubleLabel.setFont (new java.awt.Font ("Dialog", 1, 14));
    doubleLabel.setText ("double");
    dShowValueButton.setLabel ("Show value");

    // Float sign and message.
    fSignBitLabel.setText ("Sign bit:");
    fSignBitText.setFont (new java.awt.Font ("Monospaced", 0, 12));
    fSignBitText.setText ("0");
    fMessageText.setFont (new java.awt.Font ("Dialog", 1, 12));
    fMessageText.setAlignment (2);
    displayFloatMessage ("Zero");

    // Double sign and message.
    dSignBitLabel.setText ("Sign bit:");
    dSignBitText.setFont (new java.awt.Font ("Monospaced", 0, 12));
    dSignBitText.setText ("0");
    dMessageText.setFont (new java.awt.Font ("Dialog", 1, 12));
    dMessageText.setAlignment (2);
    displayDoubleMessage ("Zero");

    // Float exponent.
    fExponentBitsLabel.setText ("Exponent bits:");
    fExponentBitsText.setFont (new java.awt.Font ("Monospaced", 0, 12));
    fExponentBitsText.setText ("00000000");
    fBiasedLabel.setText ("Biased value:");
    fBiasedText.setFont (new java.awt.Font ("Monospaced", 0, 12));
    fBiasedText.setText ("0");
    fUnbiasedLabel.setText ("Unbiased value:");
    fUnbiasedText.setFont (new java.awt.Font ("Monospaced", 0, 12));
    fUnbiasedText.setText (Integer.toString (-IEEE754Constants.FLOAT_EXPONENT_BIAS));
    fUnbiasedText.setEnabled (false);

    // Double exponent.
    dExponentBitsLabel.setText ("Exponent bits:");
    dExponentBitsText.setFont (new java.awt.Font ("Monospaced", 0, 12));
    dExponentBitsText.setText ("00000000000");
    dBiasedLabel.setText ("Biased value:");
    dBiasedText.setFont (new java.awt.Font ("Monospaced", 0, 12));
    dBiasedText.setText ("0");
    dUnbiasedLabel.setText ("Unbiased value:");
    dUnbiasedText.setFont (new java.awt.Font ("Monospaced", 0, 12));
    dUnbiasedText.setText (Integer.toString (-IEEE754Constants.DOUBLE_EXPONENT_BIAS));
    dUnbiasedText.setEnabled (false);

    // Float fraction and significand.
    fFractionBitsLabel.setText ("Fraction bits:");
    fFractionBitsText.setText ("00000000000000000000000");
    fSignificandLabel.setText ("Significand bits:");
    fSignificandText.setFont (new java.awt.Font ("Monospaced", 0, 12));
    fSignificandText.setText ("0.00000000000000000000000");

    // Double fraction and significand.
    dFractionBitsLabel.setText ("Fraction bits:");
    dFractionBitsText.setText ("0000000000000000000000000000000000000000000000000000");
    dSignificandLabel.setText ("Significand bits:");
    dSignificandText.setFont (new java.awt.Font ("Monospaced", 0, 12));
    dSignificandText.setText ("0.0000000000000000000000000000000000000000000000000000");

    // Float value and decompose button.
    fValuePanel.setBackground (SystemColor.scrollbar);
    fValuePanel.setLayout (gridBagLayout4);
    fValueLabel.setText ("Value:");
    fValueText.setFont (new java.awt.Font ("Monospaced", 1, 12));
    fValueText.setText ("0.0");
    fDecomposeButton.setLabel ("Decompose");

    // Double value and decompose button.
    dValuePanel.setBackground (SystemColor.scrollbar);
    dValuePanel.setLayout (gridBagLayout3);
    dValueText.setFont (new java.awt.Font ("Monospaced", 1, 12));
    dValueText.setText ("0.0");
    dvalueLabel.setText ("Value:");
    dDecomposeButton.setLabel ("Decompose");

    // Applet and main panel.
    gridLayout1.setColumns (1);
    gridLayout1.setRows (2);
    gridLayout1.setVgap (20);
    mainPanel.setBackground (Color.white);
    this.add (mainPanel, BorderLayout.CENTER);
    mainPanel.add (floatPanel, null);
    mainPanel.add (doublePanel, null);

    // Float panel.
    floatPanel.add (floatLabel, new GridBagConstraints (0,
                                                        0,
                                                        3,
                                                        1,
                                                        0.0,
                                                        0.0,
                                                        java.awt.GridBagConstraints.WEST,
                                                        java.awt.GridBagConstraints.NONE,
                                                        new Insets (10, 10, 0, 0),
                                                        3,
                                                        0));
    floatPanel.add (fShowValueButton, new GridBagConstraints (3,
                                                              0,
                                                              3,
                                                              1,
                                                              1.0,
                                                              0.0,
                                                              java.awt.GridBagConstraints.EAST,
                                                              java.awt.GridBagConstraints.NONE,
                                                              new Insets (10, 0, 0, 10),
                                                              0,
                                                              0));
    floatPanel.add (fSignBitLabel, new GridBagConstraints (0,
                                                           1,
                                                           1,
                                                           1,
                                                           0.0,
                                                           0.0,
                                                           java.awt.GridBagConstraints.EAST,
                                                           java.awt.GridBagConstraints.NONE,
                                                           new Insets (10, 10, 0, 0),
                                                           0,
                                                           0));
    floatPanel.add (fSignBitText, new GridBagConstraints (1,
                                                          1,
                                                          1,
                                                          1,
                                                          0.0,
                                                          0.0,
                                                          java.awt.GridBagConstraints.WEST,
                                                          java.awt.GridBagConstraints.NONE,
                                                          new Insets (10, 0, 0, 0),
                                                          0,
                                                          0));
    floatPanel.add (fMessageText, new GridBagConstraints (2,
                                                          1,
                                                          4,
                                                          1,
                                                          1.0,
                                                          0.0,
                                                          java.awt.GridBagConstraints.CENTER,
                                                          java.awt.GridBagConstraints.HORIZONTAL,
                                                          new Insets (10, 10, 0, 10),
                                                          0,
                                                          0));

    floatPanel.add (fExponentBitsLabel, new GridBagConstraints (0,
                                                                2,
                                                                1,
                                                                1,
                                                                0.0,
                                                                0.0,
                                                                java.awt.GridBagConstraints.EAST,
                                                                java.awt.GridBagConstraints.NONE,
                                                                new Insets (10, 10, 0, 0),
                                                                0,
                                                                0));
    floatPanel.add (fExponentBitsText, new GridBagConstraints (1,
                                                               2,
                                                               1,
                                                               1,
                                                               1.0,
                                                               0.0,
                                                               java.awt.GridBagConstraints.WEST,
                                                               java.awt.GridBagConstraints.HORIZONTAL,
                                                               new Insets (10, 0, 0, 0),
                                                               0,
                                                               0));
    floatPanel.add (fBiasedLabel, new GridBagConstraints (2,
                                                          2,
                                                          1,
                                                          1,
                                                          0.0,
                                                          0.0,
                                                          java.awt.GridBagConstraints.EAST,
                                                          java.awt.GridBagConstraints.NONE,
                                                          new Insets (10, 10, 0, 0),
                                                          0,
                                                          0));
    floatPanel.add (fBiasedText, new GridBagConstraints (3,
                                                         2,
                                                         1,
                                                         1,
                                                         1.0,
                                                         0.0,
                                                         java.awt.GridBagConstraints.WEST,
                                                         java.awt.GridBagConstraints.HORIZONTAL,
                                                         new Insets (10, 0, 0, 0),
                                                         0,
                                                         0));
    floatPanel.add (fUnbiasedLabel, new GridBagConstraints (4,
                                                            2,
                                                            1,
                                                            1,
                                                            0.0,
                                                            0.0,
                                                            java.awt.GridBagConstraints.EAST,
                                                            java.awt.GridBagConstraints.NONE,
                                                            new Insets (10, 10, 0, 0),
                                                            0,
                                                            0));
    floatPanel.add (fUnbiasedText, new GridBagConstraints (5,
                                                           2,
                                                           1,
                                                           1,
                                                           1.0,
                                                           0.0,
                                                           java.awt.GridBagConstraints.WEST,
                                                           java.awt.GridBagConstraints.HORIZONTAL,
                                                           new Insets (10, 0, 0, 10),
                                                           0,
                                                           0));
    floatPanel.add (fFractionBitsLabel, new GridBagConstraints (0,
                                                                3,
                                                                1,
                                                                1,
                                                                0.0,
                                                                0.0,
                                                                java.awt.GridBagConstraints.EAST,
                                                                java.awt.GridBagConstraints.NONE,
                                                                new Insets (10, 10, 0, 0),
                                                                0,
                                                                0));
    floatPanel.add (fFractionBitsText, new GridBagConstraints (1,
                                                               3,
                                                               5,
                                                               1,
                                                               1.0,
                                                               0.0,
                                                               java.awt.GridBagConstraints.WEST,
                                                               java.awt.GridBagConstraints.HORIZONTAL,
                                                               new Insets (10, 0, 0, 10),
                                                               0,
                                                               0));
    floatPanel.add (fSignificandLabel, new GridBagConstraints (0,
                                                               4,
                                                               1,
                                                               1,
                                                               0.0,
                                                               0.0,
                                                               java.awt.GridBagConstraints.EAST,
                                                               java.awt.GridBagConstraints.NONE,
                                                               new Insets (10, 10, 0, 0),
                                                               0,
                                                               0));
    floatPanel.add (fSignificandText, new GridBagConstraints (1,
                                                              4,
                                                              5,
                                                              1,
                                                              1.0,
                                                              0.0,
                                                              java.awt.GridBagConstraints.WEST,
                                                              java.awt.GridBagConstraints.HORIZONTAL,
                                                              new Insets (10, 10, 0, 10),
                                                              0,
                                                              0));
    floatPanel.add (fValuePanel, new GridBagConstraints (0,
                                                         5,
                                                         6,
                                                         1,
                                                         1.0,
                                                         0.0,
                                                         java.awt.GridBagConstraints.CENTER,
                                                         java.awt.GridBagConstraints.HORIZONTAL,
                                                         new Insets (10, 10, 10, 10),
                                                         0,
                                                         0));
    fValuePanel.add (fValueLabel, new GridBagConstraints (0,
                                                          0,
                                                          1,
                                                          1,
                                                          0.0,
                                                          0.0,
                                                          java.awt.GridBagConstraints.EAST,
                                                          java.awt.GridBagConstraints.NONE,
                                                          new Insets (5, 5, 5, 0),
                                                          0,
                                                          0));
    fValuePanel.add (fValueText, new GridBagConstraints (1,
                                                         0,
                                                         1,
                                                         1,
                                                         1.0,
                                                         0.0,
                                                         java.awt.GridBagConstraints.WEST,
                                                         java.awt.GridBagConstraints.HORIZONTAL,
                                                         new Insets (5, 0, 5, 0),
                                                         0,
                                                         0));
    fValuePanel.add (fDecomposeButton, new GridBagConstraints (2,
                                                               0,
                                                               1,
                                                               1,
                                                               0.0,
                                                               0.0,
                                                               java.awt.GridBagConstraints.CENTER,
                                                               java.awt.GridBagConstraints.NONE,
                                                               new Insets (5, 5, 5, 5),
                                                               0,
                                                               0));

    // Double panel.
    doublePanel.add (doubleLabel, new GridBagConstraints (0,
                                                          0,
                                                          3,
                                                          1,
                                                          0.0,
                                                          0.0,
                                                          java.awt.GridBagConstraints.WEST,
                                                          java.awt.GridBagConstraints.NONE,
                                                          new Insets (10, 10, 0, 0),
                                                          0,
                                                          0));
    doublePanel.add (dShowValueButton, new GridBagConstraints (3,
                                                               0,
                                                               3,
                                                               1,
                                                               0.0,
                                                               0.0,
                                                               java.awt.GridBagConstraints.EAST,
                                                               java.awt.GridBagConstraints.NONE,
                                                               new Insets (10, 0, 0, 10),
                                                               0,
                                                               0));
    doublePanel.add (dSignBitLabel, new GridBagConstraints (0,
                                                            1,
                                                            1,
                                                            1,
                                                            0.0,
                                                            0.0,
                                                            java.awt.GridBagConstraints.EAST,
                                                            java.awt.GridBagConstraints.NONE,
                                                            new Insets (10, 10, 0, 0),
                                                            0,
                                                            0));
    doublePanel.add (dSignBitText, new GridBagConstraints (1,
                                                           1,
                                                           1,
                                                           1,
                                                           0.0,
                                                           0.0,
                                                           java.awt.GridBagConstraints.WEST,
                                                           java.awt.GridBagConstraints.NONE,
                                                           new Insets (10, 0, 0, 0),
                                                           0,
                                                           0));
    doublePanel.add (dMessageText, new GridBagConstraints (2,
                                                           1,
                                                           4,
                                                           1,
                                                           1.0,
                                                           0.0,
                                                           java.awt.GridBagConstraints.CENTER,
                                                           java.awt.GridBagConstraints.HORIZONTAL,
                                                           new Insets (10, 10, 0, 10),
                                                           0,
                                                           0));
    doublePanel.add (dExponentBitsLabel, new GridBagConstraints (0,
                                                                 2,
                                                                 1,
                                                                 1,
                                                                 0.0,
                                                                 0.0,
                                                                 java.awt.GridBagConstraints.EAST,
                                                                 java.awt.GridBagConstraints.NONE,
                                                                 new Insets (10, 10, 0, 0),
                                                                 0,
                                                                 0));
    doublePanel.add (dExponentBitsText, new GridBagConstraints (1,
                                                                2,
                                                                1,
                                                                1,
                                                                1.0,
                                                                0.0,
                                                                java.awt.GridBagConstraints.WEST,
                                                                java.awt.GridBagConstraints.HORIZONTAL,
                                                                new Insets (10, 0, 0, 0),
                                                                0,
                                                                0));
    doublePanel.add (dBiasedLabel, new GridBagConstraints (2,
                                                           2,
                                                           1,
                                                           1,
                                                           0.0,
                                                           0.0,
                                                           java.awt.GridBagConstraints.EAST,
                                                           java.awt.GridBagConstraints.NONE,
                                                           new Insets (10, 10, 0, 0),
                                                           0,
                                                           0));
    doublePanel.add (dBiasedText, new GridBagConstraints (3,
                                                          2,
                                                          1,
                                                          1,
                                                          1.0,
                                                          0.0,
                                                          java.awt.GridBagConstraints.WEST,
                                                          java.awt.GridBagConstraints.HORIZONTAL,
                                                          new Insets (10, 0, 0, 0),
                                                          0,
                                                          0));
    doublePanel.add (dUnbiasedLabel, new GridBagConstraints (4,
                                                             2,
                                                             1,
                                                             1,
                                                             0.0,
                                                             0.0,
                                                             java.awt.GridBagConstraints.EAST,
                                                             java.awt.GridBagConstraints.NONE,
                                                             new Insets (10, 10, 0, 0),
                                                             0,
                                                             0));
    doublePanel.add (dUnbiasedText, new GridBagConstraints (5,
                                                            2,
                                                            1,
                                                            1,
                                                            1.0,
                                                            0.0,
                                                            java.awt.GridBagConstraints.WEST,
                                                            java.awt.GridBagConstraints.HORIZONTAL,
                                                            new Insets (10, 0, 0, 10),
                                                            0,
                                                            0));
    doublePanel.add (dFractionBitsLabel, new GridBagConstraints (0,
                                                                 3,
                                                                 1,
                                                                 1,
                                                                 0.0,
                                                                 0.0,
                                                                 java.awt.GridBagConstraints.EAST,
                                                                 java.awt.GridBagConstraints.NONE,
                                                                 new Insets (10, 10, 0, 0),
                                                                 0,
                                                                 0));
    doublePanel.add (dFractionBitsText, new GridBagConstraints (1,
                                                                3,
                                                                5,
                                                                1,
                                                                1.0,
                                                                0.0,
                                                                java.awt.GridBagConstraints.EAST,
                                                                java.awt.GridBagConstraints.HORIZONTAL,
                                                                new Insets (10, 0, 0, 10),
                                                                0,
                                                                0));
    doublePanel.add (dSignificandLabel, new GridBagConstraints (0,
                                                                4,
                                                                1,
                                                                1,
                                                                0.0,
                                                                0.0,
                                                                java.awt.GridBagConstraints.EAST,
                                                                java.awt.GridBagConstraints.NONE,
                                                                new Insets (10, 10, 0, 0),
                                                                0,
                                                                0));
    doublePanel.add (dSignificandText, new GridBagConstraints (1,
                                                               4,
                                                               5,
                                                               1,
                                                               0.0,
                                                               0.0,
                                                               java.awt.GridBagConstraints.EAST,
                                                               java.awt.GridBagConstraints.HORIZONTAL,
                                                               new Insets (10, 0, 0, 10),
                                                               0,
                                                               0));
    doublePanel.add (dValuePanel, new GridBagConstraints (0,
                                                          5,
                                                          6,
                                                          1,
                                                          1.0,
                                                          0.0,
                                                          java.awt.GridBagConstraints.CENTER,
                                                          java.awt.GridBagConstraints.HORIZONTAL,
                                                          new Insets (10, 10, 10, 10),
                                                          0,
                                                          0));
    dValuePanel.add (dvalueLabel, new GridBagConstraints (0,
                                                          0,
                                                          1,
                                                          1,
                                                          0.0,
                                                          0.0,
                                                          java.awt.GridBagConstraints.EAST,
                                                          java.awt.GridBagConstraints.NONE,
                                                          new Insets (5, 5, 5, 0),
                                                          0,
                                                          0));
    dValuePanel.add (dValueText, new GridBagConstraints (1,
                                                         0,
                                                         1,
                                                         1,
                                                         1.0,
                                                         0.0,
                                                         java.awt.GridBagConstraints.EAST,
                                                         java.awt.GridBagConstraints.HORIZONTAL,
                                                         new Insets (5, 0, 5, 0),
                                                         0,
                                                         0));
    dValuePanel.add (dDecomposeButton, new GridBagConstraints (2,
                                                               0,
                                                               1,
                                                               1,
                                                               0.0,
                                                               0.0,
                                                               java.awt.GridBagConstraints.CENTER,
                                                               java.awt.GridBagConstraints.NONE,
                                                               new Insets (5, 5, 5, 5),
                                                               0,
                                                               0));
  }

  /**
   * Initialize the input event handlers.
   */
  private void initHandlers ()
  {
    // Float show value button.
    fShowValueButton.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        if (isFloatExponentOK)
          showFloatValue ();
      }
    });

    // Double show value button.
    dShowValueButton.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        if (isDoubleExponentOK)
          showDoubleValue ();
      }
    });

    // Float decompose button.
    fDecomposeButton.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        decomposeFloatValue ();
      }
    });

    // Double decompose button.
    dDecomposeButton.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        decomposeDoubleValue ();
      }
    });

    // Float sign bit text: return key.
    fSignBitText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        showFloatValue ();
      }
    });

    // Float sign bit text: focus and tab key.
    fSignBitText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        showFloatValue ();
      }
    });

    // Double sign bit text: return key.
    dSignBitText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        showDoubleValue ();
      }
    });

    // Double sign bit text: focus and tab key.
    dSignBitText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        showDoubleValue ();
      }
    });

    // Float exponent bits text: return key.
    fExponentBitsText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        try
        {
          isFloatExponentOK = true;
          setFloatExponentFromBits ();
          showFloatValue ();
        }
        catch (final Exception ex)
        {
          isFloatExponentOK = false;
          displayFloatError (ex.getMessage ());
        }
      }
    });

    // Float exponent bits text: focus and tab key.
    fExponentBitsText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        try
        {
          isFloatExponentOK = true;
          setFloatExponentFromBits ();
          showFloatValue ();
        }
        catch (final Exception ex)
        {
          isFloatExponentOK = false;
          displayFloatError (ex.getMessage ());
        }
      }
    });

    // Double exponent bits text: return key.
    dExponentBitsText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        try
        {
          isDoubleExponentOK = true;
          setDoubleExponentFromBits ();
          showDoubleValue ();
        }
        catch (final Exception ex)
        {
          isDoubleExponentOK = false;
          displayDoubleError (ex.getMessage ());
        }
      }
    });

    // Double exponent bits text: focus and tab key.
    dExponentBitsText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        try
        {
          isDoubleExponentOK = true;
          setDoubleExponentFromBits ();
          showDoubleValue ();
        }
        catch (final Exception ex)
        {
          isDoubleExponentOK = false;
          displayDoubleError (ex.getMessage ());
        }
      }
    });

    // Float biased exponent text: return key.
    fBiasedText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        try
        {
          isFloatExponentOK = true;
          setFloatExponentFromBiased ();
          showFloatValue ();
        }
        catch (final Exception ex)
        {
          isFloatExponentOK = false;
          displayFloatError (ex.getMessage ());
        }
      }
    });

    // Float biased exponent text: focus and tab key.
    fBiasedText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        try
        {
          isFloatExponentOK = true;
          setFloatExponentFromBiased ();
          showFloatValue ();
        }
        catch (final Exception ex)
        {
          isFloatExponentOK = false;
          displayFloatError (ex.getMessage ());
        }
      }
    });

    // Double biased exponent text: return key.
    dBiasedText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        try
        {
          isDoubleExponentOK = true;
          setDoubleExponentFromBiased ();
          showDoubleValue ();
        }
        catch (final Exception ex)
        {
          isDoubleExponentOK = false;
          displayDoubleError (ex.getMessage ());
        }
      }
    });

    // Double biased exponent text: focus and tab key.
    dBiasedText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        try
        {
          isDoubleExponentOK = true;
          setDoubleExponentFromBiased ();
          showDoubleValue ();
        }
        catch (final Exception ex)
        {
          isDoubleExponentOK = false;
          displayDoubleError (ex.getMessage ());
        }
      }
    });

    // Float unbiased exponent text: return key.
    fUnbiasedText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        try
        {
          isFloatExponentOK = true;
          setFloatExponentFromUnbiased ();
          showFloatValue ();
        }
        catch (final Exception ex)
        {
          isFloatExponentOK = false;
          displayFloatError (ex.getMessage ());
        }
      }
    });

    // Float unbiased exponent text: focus and tab key.
    fUnbiasedText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        try
        {
          isFloatExponentOK = true;
          setFloatExponentFromUnbiased ();
          showFloatValue ();
        }
        catch (final Exception ex)
        {
          isFloatExponentOK = false;
          displayFloatError (ex.getMessage ());
        }
      }
    });

    // Double unbiased exponent text: return key.
    dUnbiasedText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        try
        {
          isDoubleExponentOK = true;
          setDoubleExponentFromUnbiased ();
          showDoubleValue ();
        }
        catch (final Exception ex)
        {
          isDoubleExponentOK = false;
          displayDoubleError (ex.getMessage ());
        }
      }
    });

    // Double unbiased exponent text: focus and tab key.
    dUnbiasedText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        try
        {
          isDoubleExponentOK = true;
          setDoubleExponentFromUnbiased ();
          showDoubleValue ();
        }
        catch (final Exception ex)
        {
          isDoubleExponentOK = false;
          displayDoubleError (ex.getMessage ());
        }
      }
    });

    // Float fraction bits text: return key.
    fFractionBitsText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        showFloatValue ();
      }
    });

    // Float fraction bits text: focus and tab key.
    fFractionBitsText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        showFloatValue ();
      }
    });

    // Double fraction bits text: return key.
    dFractionBitsText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        showDoubleValue ();
      }
    });

    // Double fraction bits text: focus and tab key.
    dFractionBitsText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        showDoubleValue ();
      }
    });

    // Float value text: return key.
    fValueText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        decomposeFloatValue ();
      }
    });

    // Float value text: focus and tab key.
    fValueText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        decomposeFloatValue ();
      }
    });

    // Double value text: return key.
    dValueText.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        decomposeDoubleValue ();
      }
    });

    // Double value text: focus and tab key.
    dValueText.addFocusListener (new FocusAdapter ()
    {
      @Override
      public void focusLost (final FocusEvent ev)
      {
        decomposeDoubleValue ();
      }
    });
  }

  /**
   * Construct a float value and display it.
   */
  private void showFloatValue ()
  {
    try
    {
      int sign;
      final int biased = floatBiasedExponent ();
      final String signBitText = fSignBitText.getText ();
      @SuppressWarnings ("unused")
      final String unbiasedText = fUnbiasedText.getText ();
      final String fractionBitsText = fFractionBitsText.getText ();

      try
      {
        sign = Integer.parseInt (signBitText);
      }
      catch (final NumberFormatException ex)
      {
        throw new Exception ("Invalid sign bit.");
      }

      // Construct the value and display it.
      final IEEE754.FloatFraction fraction = new IEEE754.FloatFraction (fractionBitsText);
      final IEEE754 ieee = new IEEE754 (sign, biased, fraction);
      updateFloat (ieee);
    }
    catch (final Exception ex)
    {
      displayFloatError (ex.getMessage ());
    }
  }

  /**
   * Construct a double value and display it.
   */
  private void showDoubleValue ()
  {
    try
    {
      int sign;
      final int biased = doubleBiasedExponent ();
      final String signBitText = dSignBitText.getText ();
      final String fractionBitsText = dFractionBitsText.getText ();

      try
      {
        sign = Integer.parseInt (signBitText);
      }
      catch (final NumberFormatException ex)
      {
        throw new Exception ("Invalid sign bit.");
      }

      // Construct the value and display it.
      final IEEE754.DoubleFraction fraction = new IEEE754.DoubleFraction (fractionBitsText);
      final IEEE754 ieee = new IEEE754 (sign, biased, fraction);
      updateDouble (ieee);
    }
    catch (final Exception ex)
    {
      displayDoubleError (ex.getMessage ());
    }
  }

  /**
   * Decompose a float value according to the IEEE 754 format.
   */
  private void decomposeFloatValue ()
  {
    try
    {
      float value;
      final String valueText = fValueText.getText ().trim ().toLowerCase ();

      // Check for the special keyword values.
      if (valueText.equals ("min") || valueText.equals ("+min"))
      {
        value = Float.MIN_VALUE;
      }
      else
        if (valueText.equals ("-min"))
        {
          value = -Float.MIN_VALUE;
        }
        else
          if (valueText.equals ("max") || valueText.equals ("+max"))
          {
            value = Float.MAX_VALUE;
          }
          else
            if (valueText.equals ("-max"))
            {
              value = -Float.MAX_VALUE;
            }
            else
              if (valueText.equals ("inf") ||
                  valueText.equals ("+inf") ||
                  valueText.equals ("infinity") ||
                  valueText.equals ("+infinity"))
              {
                value = Float.POSITIVE_INFINITY;
              }
              else
                if (valueText.equals ("-inf") || valueText.equals ("-infinity"))
                {
                  value = Float.NEGATIVE_INFINITY;
                }
                else
                  if (valueText.equals ("nan"))
                  {
                    value = Float.NaN;
                  }
                  else
                    try
                    {
                      value = Float.valueOf (valueText).floatValue ();
                    }
                    catch (final NumberFormatException ex)
                    {
                      throw new Exception ("Invalid float value.");
                    }

      // Decompose the value and display its parts.
      final IEEE754 ieee = new IEEE754 (value);
      updateFloat (ieee);
    }
    catch (final Exception ex)
    {
      displayFloatError (ex.getMessage ());
    }
  }

  /**
   * Decompose a double value according to the IEEE 754 format.
   */
  private void decomposeDoubleValue ()
  {
    try
    {
      double value;
      final String valueText = dValueText.getText ().toLowerCase ();

      // Check for the special keyword values.
      if (valueText.equals ("min") || valueText.equals ("+min"))
      {
        value = Double.MIN_VALUE;
      }
      else
        if (valueText.equals ("-min"))
        {
          value = -Double.MIN_VALUE;
        }
        else
          if (valueText.equals ("max") || valueText.equals ("+max"))
          {
            value = Double.MAX_VALUE;
          }
          else
            if (valueText.equals ("-max"))
            {
              value = -Double.MAX_VALUE;
            }
            else
              if (valueText.equals ("inf") ||
                  valueText.equals ("+inf") ||
                  valueText.equals ("infinity") ||
                  valueText.equals ("+infinity"))
              {
                value = Double.POSITIVE_INFINITY;
              }
              else
                if (valueText.equals ("-inf") || valueText.equals ("-infinity"))
                {
                  value = Double.NEGATIVE_INFINITY;
                }
                else
                  if (valueText.equals ("nan"))
                  {
                    value = Double.NaN;
                  }
                  else
                    try
                    {
                      value = Double.valueOf (valueText).doubleValue ();
                    }
                    catch (final NumberFormatException ex)
                    {
                      throw new Exception ("Invalid double value.");
                    }

      // Decompose the value and display its parts.
      final IEEE754 ieee = new IEEE754 (value);
      updateDouble (ieee);
    }
    catch (final Exception ex)
    {
      displayDoubleError (ex.getMessage ());
    }
  }

  /**
   * Update the float value display.
   */
  private void updateFloat (final IEEE754 ieee)
  {
    final float value = ieee.floatValue ();
    final int biased = ieee.biasedExponent ();

    fSignBitText.setText (ieee.signBit ());
    fExponentBitsText.setText (ieee.exponentBits ());
    fBiasedText.setText (Integer.toString (biased));
    fUnbiasedText.setText (Integer.toString (ieee.unbiasedExponent ()));
    fFractionBitsText.setText (ieee.fractionBits ());
    fSignificandText.setText (ieee.significandBits ());
    fValueText.setText (Float.toString (value));

    String text;
    boolean enabled = false;

    // Set the display message.
    if (ieee.isZero ())
    {
      text = "Zero";
    }
    else
      if (ieee.isExponentReserved ())
      {
        text = "Reserved";
      }
      else
        if (ieee.isDenormalized ())
        {
          text = "Denormalized";
        }
        else
        {
          text = "Normalized";
          enabled = true;
        }

    displayFloatMessage (text);
    fUnbiasedText.setEnabled (enabled);
  }

  /**
   * Update the double value display.
   */
  private void updateDouble (final IEEE754 ieee)
  {
    final double value = ieee.doubleValue ();
    final int biased = ieee.biasedExponent ();

    dSignBitText.setText (ieee.signBit ());
    dExponentBitsText.setText (ieee.exponentBits ());
    dBiasedText.setText (Integer.toString (biased));
    dUnbiasedText.setText (Integer.toString (ieee.unbiasedExponent ()));
    dFractionBitsText.setText (ieee.fractionBits ());
    dSignificandText.setText (ieee.significandBits ());
    dValueText.setText (Double.toString (value));

    String text;
    boolean enabled = false;

    // Set the display message.
    if (ieee.isZero ())
    {
      text = "Zero";
    }
    else
      if (ieee.isExponentReserved ())
      {
        text = "Reserved";
      }
      else
        if (ieee.isDenormalized ())
        {
          text = "Denormalized";
        }
        else
        {
          text = "Normalized";
          enabled = true;
        }

    displayDoubleMessage (text);
    dUnbiasedText.setEnabled (enabled);
  }

  /**
   * Compute the float biased exponent value from its bitfield.
   * 
   * @return the biased exponent value
   * @throws Exception
   *         if an error occurred
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   *         if format error
   */
  private int floatBitsExponent () throws Exception, IEEE754.IEEE754Exception
  {
    int biased;
    final String exponentBitsText = fExponentBitsText.getText ();

    checkExponentBits (exponentBitsText, IEEE754Constants.FLOAT_EXPONENT_SIZE);

    try
    {
      biased = Integer.parseInt (exponentBitsText, 2);
    }
    catch (final NumberFormatException ex)
    {
      throw new Exception ("Invalid exponent bit string.");
    }

    IEEE754.validateFloatBiasedExponent (biased);
    return biased;
  }

  /**
   * Compute the double biased exponent value from its bitfield.
   * 
   * @return the biased exponent value
   * @throws Exception
   *         if an error occurred
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   *         if format error
   */
  private int doubleBitsExponent () throws Exception, IEEE754.IEEE754Exception
  {
    int biased;
    final String exponentBitsText = dExponentBitsText.getText ();

    checkExponentBits (exponentBitsText, IEEE754Constants.DOUBLE_EXPONENT_SIZE);

    try
    {
      biased = Integer.parseInt (exponentBitsText, 2);
    }
    catch (final NumberFormatException ex)
    {
      throw new Exception ("Invalid exponent bit string.");
    }

    IEEE754.validateDoubleBiasedExponent (biased);
    return biased;
  }

  /**
   * Compute the float biased exponent value from the biased textfield.
   * 
   * @return the biased exponent value
   * @throws Exception
   *         if an error occurred
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   *         if format error
   */
  private int floatBiasedExponent () throws Exception, IEEE754.IEEE754Exception
  {
    int biased;
    final String biasedText = fBiasedText.getText ();

    try
    {
      biased = Integer.parseInt (biasedText);
    }
    catch (final NumberFormatException ex)
    {
      throw new Exception ("Invalid biased exponent value.");
    }

    IEEE754.validateFloatBiasedExponent (biased);
    return biased;
  }

  /**
   * Compute the double biased exponent value from the biased textfield.
   * 
   * @return the biased exponent value
   * @throws Exception
   *         if an error occurred
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   *         if format error
   */
  private int doubleBiasedExponent () throws Exception, IEEE754.IEEE754Exception
  {
    int biased;
    final String biasedText = dBiasedText.getText ();

    try
    {
      biased = Integer.parseInt (biasedText);
    }
    catch (final NumberFormatException ex)
    {
      throw new Exception ("Invalid biased exponent value.");
    }

    IEEE754.validateDoubleBiasedExponent (biased);
    return biased;
  }

  /**
   * Compute the float unbiased exponent value from the unbiased textfield.
   * 
   * @return the unbiased exponent value
   * @throws Exception
   *         if an error occurred
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   *         if format error
   */
  private int floatUnbiasedExponent () throws Exception, IEEE754.IEEE754Exception
  {
    int unbiased;
    final String unbiasedText = fUnbiasedText.getText ();

    try
    {
      unbiased = Integer.parseInt (unbiasedText);
    }
    catch (final NumberFormatException ex)
    {
      throw new Exception ("Invalid unbiased exponent value.");
    }

    IEEE754.validateFloatUnbiasedExponent (unbiased);
    return unbiased;
  }

  /**
   * Compute the double unbiased exponent value from the unbiased textfield.
   * 
   * @return the unbiased exponent value
   * @throws Exception
   *         if an error occurred
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   *         if format error
   */
  private int doubleUnbiasedExponent () throws Exception, IEEE754.IEEE754Exception
  {
    int unbiased;
    final String unbiasedText = dUnbiasedText.getText ();

    try
    {
      unbiased = Integer.parseInt (unbiasedText);
    }
    catch (final NumberFormatException ex)
    {
      throw new Exception ("Invalid unbiased exponent value.");
    }

    IEEE754.validateDoubleUnbiasedExponent (unbiased);
    return unbiased;
  }

  /**
   * Set the float biased and unbiased exponent fields from the bitfield.
   * 
   * @throws Exception
   *         if an error occurred
   */
  private void setFloatExponentFromBits () throws Exception
  {
    final int biased = floatBitsExponent ();

    fBiasedText.setText (Integer.toString (biased));
    setFloatUnbiased (biased);
  }

  /**
   * Set the double biased and unbiased exponent fields from the bitfield.
   * 
   * @throws Exception
   *         if an error occurred
   */
  private void setDoubleExponentFromBits () throws Exception
  {
    final int biased = doubleBitsExponent ();
    final int unbiased = biased - IEEE754Constants.DOUBLE_EXPONENT_BIAS;

    dBiasedText.setText (Integer.toString (biased));
    dUnbiasedText.setText (Integer.toString (unbiased));
  }

  /**
   * Set the float unbiased exponent field and bitfield from the biased field.
   * 
   * @throws Exception
   *         if an error occurred
   */
  private void setFloatExponentFromBiased () throws Exception
  {
    final int biased = floatBiasedExponent ();

    fExponentBitsText.setText (toBitString (biased, IEEE754Constants.FLOAT_EXPONENT_SIZE));
    setFloatUnbiased (biased);
  }

  /**
   * Set the double unbiased exponent field and bitfield from the biased field.
   * 
   * @throws Exception
   *         if an error occurred
   */
  private void setDoubleExponentFromBiased () throws Exception
  {
    final int biased = doubleBiasedExponent ();

    dExponentBitsText.setText (toBitString (biased, IEEE754Constants.DOUBLE_EXPONENT_SIZE));
    setDoubleUnbiased (biased);
  }

  /**
   * Set the float biased exponent field and bitfield from the unbiased field.
   * 
   * @throws Exception
   *         if an error occurred
   */
  private void setFloatExponentFromUnbiased () throws Exception
  {
    final int unbiased = floatUnbiasedExponent ();
    final int biased = IEEE754.toFloatBiasedExponent (unbiased);

    fExponentBitsText.setText (toBitString (biased, IEEE754Constants.FLOAT_EXPONENT_SIZE));
    fBiasedText.setText (Integer.toString (biased));
    displayFloatMessage ("Normalized");
  }

  /**
   * Set the double biased exponent field and bitfield from the unbiased field.
   * 
   * @throws Exception
   *         if an error occurred
   */
  private void setDoubleExponentFromUnbiased () throws Exception
  {
    final int unbiased = doubleUnbiasedExponent ();
    final int biased = IEEE754.toDoubleBiasedExponent (unbiased);

    dExponentBitsText.setText (toBitString (biased, IEEE754Constants.DOUBLE_EXPONENT_SIZE));
    dBiasedText.setText (Integer.toString (biased));
    displayFloatMessage ("Normalized");
  }

  /**
   * Set the float unbiased exponent field from the biased value.
   * 
   * @param biased
   *        the biased value
   */
  private void setFloatUnbiased (final int biased)
  {
    if (biased == 0)
    {
      fUnbiasedText.setEnabled (false);
      displayFloatMessage ("Denormalized");
    }
    else
    {
      fUnbiasedText.setEnabled (true);
      displayFloatMessage ("Normalized");
    }

    final int unbiased = IEEE754.toFloatUnbiasedExponent (biased);
    fUnbiasedText.setText (Integer.toString (unbiased));
  }

  /**
   * Set the double unbiased exponent field from the biased value.
   * 
   * @param biased
   *        the biased value
   */
  private void setDoubleUnbiased (final int biased)
  {
    if (biased == 0)
    {
      dUnbiasedText.setEnabled (false);
      displayFloatMessage ("Denormalized");
    }
    else
    {
      dUnbiasedText.setEnabled (true);
      displayFloatMessage ("Normalized");
    }

    final int unbiased = IEEE754.toDoubleUnbiasedExponent (biased);
    dUnbiasedText.setText (Integer.toString (unbiased));
  }

  /**
   * Convert a long value into a string of '0' and '1' that represents the value
   * in base 2.
   * 
   * @param pn
   *        the long value
   * @param size
   *        the array size
   * @return the character array
   */
  private String toBitString (final long pn, final int size)
  {
    long n = pn;
    final StringBuilder buffer = new StringBuilder (size);

    // Convert each bit from right to left.
    for (int i = size - 1; i >= 0; --i)
    {
      final char bit = (n & 1) == 0 ? '0' : '1';
      buffer.append (bit);
      n >>>= 1;
    }

    return buffer.reverse ().toString ();
  }

  /**
   * Check an exponent bitstring.
   * 
   * @param bits
   *        the exponent bits
   * @param size
   *        the exponent bitlength
   * @throws Exception
   *         if an error occurred
   */
  private void checkExponentBits (final String bits, final int size) throws Exception
  {
    final int bitLength = bits.length ();

    if (bitLength > size)
    {
      throw new Exception ("Too many exponent bits.");
    }
  }

  /**
   * Display a message in the float subpanel.
   */
  private void displayFloatMessage (final String message)
  {
    fMessageText.setForeground (Color.black);
    fMessageText.setText (message);
  }

  /**
   * Display a message in the double subpanel.
   */
  private void displayDoubleMessage (final String message)
  {
    dMessageText.setForeground (Color.black);
    dMessageText.setText (message);
  }

  /**
   * Display an error message in the float subpanel.
   */
  private void displayFloatError (final String message)
  {
    fMessageText.setForeground (MAROON);
    fMessageText.setText ("ERROR: " + message);
  }

  /**
   * Display an error message in the float subpanel.
   */
  private void displayDoubleError (final String message)
  {
    dMessageText.setForeground (MAROON);
    dMessageText.setText ("ERROR: " + message);
  }

  /**
   * Hack for older JVMs that support only the default constructor of
   * java.awt.GridBagConstraints.
   */
  private class GridBagConstraints extends java.awt.GridBagConstraints
  {
    GridBagConstraints (final int pgridx,
                        final int pgridy,
                        final int pgridwidth,
                        final int pgridheight,
                        final double pweightx,
                        final double pweighty,
                        final int panchor,
                        final int pfill,
                        final Insets pinsets,
                        final int pipadx,
                        final int pipady)
    {
      super.gridx = pgridx;
      super.gridy = pgridy;
      super.gridwidth = pgridwidth;
      super.gridheight = pgridheight;
      super.weightx = pweightx;
      super.weighty = pweighty;
      super.anchor = panchor;
      super.fill = pfill;
      super.insets = pinsets;
      super.ipadx = pipadx;
      super.ipady = pipady;
    }
  }
}
