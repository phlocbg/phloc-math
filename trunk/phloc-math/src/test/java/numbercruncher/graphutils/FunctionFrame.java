package numbercruncher.graphutils;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/**
 * The window that displays the image of all the functions to plot. The user
 * clicks on a function to select it.
 */
public class FunctionFrame extends Frame
{
  /** image of functions */
  private Image image;
  /** image width */
  private int imageWidth;
  /** image height */
  private int imageHeight;
  /** true if image error occurred */
  private boolean imageError = false;
  /** error label */
  private TextArea errorText;

  /** image buffer */
  private Image buffer;
  /** buffer grapics context */
  private Graphics bg;

  /** function selection index */
  private int xSelection = 0;

  /** array of functions to plot */
  private final IPlottable m_aFunctions[];
  /** root finder panel */
  private final GraphPanel m_aGraphPanel;

  /**
   * Constructor.
   * 
   * @param functions
   *        the array of functions to plot
   * @param functionImageFileName
   *        the name of the function image file
   * @param title
   *        the frame title
   * @param graphPanel
   *        the graph panel
   */
  public FunctionFrame (final IPlottable [] functions,
                        final String functionImageFileName,
                        final String title,
                        final GraphPanel graphPanel)
  {
    this.m_aFunctions = functions;
    this.m_aGraphPanel = graphPanel;

    initFrame (title);
    loadImage (functionImageFileName);
  }

  /**
   * Return the image.
   * 
   * @return the image
   */
  public Image getImage ()
  {
    return image;
  }

  /**
   * Return the array of functions to plot.
   * 
   * @return the function array
   */
  public IPlottable [] getFunctions ()
  {
    return m_aFunctions;
  }

  /**
   * Initialize the function frame.
   */
  private void initFrame (final String title)
  {
    setTitle (title);
    setResizable (false);
    setBackground (Color.white);
    setLocation (25, 25);

    // Window event handlers.
    addWindowListener (new WindowAdapter ()
    {
      @Override
      public void windowOpened (final WindowEvent ev)
      {
        // repaint();
        paint (FunctionFrame.this.getGraphics ());
      }
    });

    // Mouse event handlers.
    addMouseListener (new MouseAdapter ()
    {
      // Mouse click: Select a function.
      @Override
      public void mouseClicked (final MouseEvent ev)
      {
        final Insets in = getInsets ();
        final int x = ev.getX () - in.left;
        final int y = ev.getY () - in.top;

        // Loop to find which function (if any) was clicked on.
        for (int i = 0; i < m_aFunctions.length; ++i)
        {
          final Rectangle r = m_aFunctions[i].getRectangle ();
          if (r.contains (x, y) && (i != xSelection))
          {
            xSelection = i;
            repaint ();

            m_aGraphPanel.chooseFunction (i); // callback
            break;
          }
        }
      }
    });
  }

  /**
   * Load the function image file.
   * 
   * @param functionImageFileName
   *        the file name of the image file
   */
  private void loadImage (final String functionImageFileName)
  {
    final Container parent = m_aGraphPanel.getParent ();

    // Construct the URL string for the function image file. For
    // a standalone application, the file should be in the current
    // working directory. For an applet, the file should be in
    // the images subdirectory in the code base.
    final String imageBase = (parent instanceof Frame) ? "file:///" + System.getProperty ("user.dir") + "/"
                                                      : ((Applet) parent).getCodeBase () + "images/";
    final String urlString = imageBase + functionImageFileName;

    try
    {
      // Load the function image file.
      final URL url = new URL (urlString);
      image = Toolkit.getDefaultToolkit ().getImage (url);

      // Wait for the image to load.
      final MediaTracker tracker = new MediaTracker (this);
      tracker.addImage (image, 0);
      tracker.waitForID (0);

      imageWidth = image.getWidth (null);
      imageHeight = image.getHeight (null);

      // Did the image load sucessfully?
      if ((imageWidth <= 0) && (imageHeight <= 0))
      {
        throw new Exception ("Could not find \"" + urlString + "\"");
      }

      repaint ();
    }
    catch (final Exception ex)
    {
      imageError = true;

      // Load the error message into the frame instead.
      errorText = new TextArea ("Error loading function image file:\n");
      errorText.append (ex.getMessage ());
      errorText.setForeground (GraphPanel.MAROON);
      errorText.setEditable (false);

      setLayout (new BorderLayout ());
      add (errorText, BorderLayout.CENTER);

      // Display the error message.
      setSize (520, 100);
      setTitle ("***** ERROR *****");
      setVisible (true);
    }
  }

  /**
   * Update the display without repainting the background.
   * 
   * @param g
   *        the graphics context
   */
  @Override
  public void update (final Graphics g)
  {
    paint (g);
  }

  /**
   * Paint the function frame.
   * 
   * @param g
   *        the graphics context
   */
  @Override
  public void paint (final Graphics g)
  {
    // If there was an error, display the error message.
    if (imageError)
    {
      super.paint (g);
      return;
    }

    final Insets in = getInsets ();
    g.translate (in.left, in.top);

    // Create the image buffer.
    if (buffer == null)
    {
      buffer = createImage (imageWidth, imageHeight);
      bg = buffer.getGraphics ();
      bg.setColor (Color.white);
    }

    // Set the frame size to fit the image.
    setSize (imageWidth + in.left + in.right, imageHeight + in.top + in.bottom);

    // Draw the image into the buffer.
    bg.setPaintMode ();
    bg.fillRect (0, 0, imageWidth, imageHeight);
    bg.drawImage (image, 0, 0, this);

    // Highlight the selected function.
    if (xSelection > -1)
    {
      final Rectangle r = m_aFunctions[xSelection].getRectangle ();

      bg.setXORMode (Color.blue);
      bg.fillRect (r.x, r.y, r.width, r.height);
    }

    // Display the buffer.
    g.drawImage (buffer, 0, 0, this);
  }
}
