package Dining_3146;
/**
 *
 * @author Eli Solomon
 */
//import packages
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;

// Class UI is the user interface.  It displays a Table canvas above
// a row of buttons.  Actions (event handlers) are defined for each of
// the buttons.  Depending on the state of the UI, either the "run" or
// the "pause" button is the default (highlighted in most window
// systems); it will often self-push if you hit carriage return.
class UI extends JPanel {

    //declare local varables
    private final Table master;

    private final JRootPane root;

    //declare and initiate local varables
    private static final int externalBorder = 6;

    // Constructor
    public UI(RootPaneContainer pane, Table T, boolean isApplet) {
        master = T;

        final JPanel b = new JPanel();   // button panel

        final JButton runButton = new JButton("Run");
        final JButton pauseButton = new JButton("Pause");
        final JButton resetButton = new JButton("Reset");
        final JButton quitButton = new JButton("Quit");

        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                master.resume();
                root.setDefaultButton(pauseButton);
            }//end actionPerformed method
        });
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                master.pause();
                root.setDefaultButton(runButton);
            }//end actionPerfromed method
        });
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                master.reset();
                root.setDefaultButton(runButton);
            }//end actionPerformed methos
        });
        // Applets can't quit, so don't show the quit button in applet mode
        if (!isApplet) {
            quitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }//end actionPerformed method
            });
        }//end if

        // put the buttons into the button panel:
        b.setLayout(new FlowLayout());
        b.add(runButton);
        b.add(pauseButton);
        b.add(resetButton);
        if (!isApplet) {
            b.add(quitButton);
        }//end if

        // put the Table canvas and the button panel into the UI:
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(
            externalBorder, externalBorder, externalBorder, externalBorder));
        add(master);
        add(b);

        // put the UI into the Frame or Applet:
        pane.getContentPane().add(this);
        root = getRootPane();
        root.setDefaultButton(runButton);
    }//end constructor method
}//end UI class