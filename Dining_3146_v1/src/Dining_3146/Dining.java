package Dining_3146;
/**
 *
 * @author Eli Solomon
 */
/**
 * NOTE there is a delay once the Run button is hit until the philosophers get hungry
 * the eating utensils get BOLD once philosopher gets hungry and wants to pick up the utensil to eat
 * 
 * Simple Java implementation of the classic Dining Philosophers problem.
 * Can be run stand-alone or executed inside a web browser or applet viewer.
 */
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
/**
 * This code has six main classes:
  Dining
      The public, "main" class.  Set up so the code can run either
      stand-alone or as an applet in a web page or in appletviewer.
  Philosopher
     Active and extends Thread
  Utensil
      Passive
  Table
      Manages the philosophers and forks and their physical layout.
  Coordinator
      Provides mechanisms to suspend, resume, and reset the state of
      worker threads (philosophers).
  UI
      Manages graphical layout and button presses.
 */

public class Dining extends JApplet {

    //boolean as a gate to prevent deadlock, change to true to get the full deadlock experience
    public static final boolean DEADLOCK = false;

    private static final int CANVAS_SIZE = 360;
        // pixels in each direction;
        // needs to agree with size in dining.html

    private void start(final RootPaneContainer pane, final boolean isApplet) {
        final Table t = new Table(CANVAS_SIZE);
        // arrange to call graphical setup from GUI thread
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    new UI(pane, t, isApplet);
                }//end run menthod
            });
        } catch (Exception e) {
            System.err.println("unable to create GUI");
        }//end catch block
    }//end start

    // called only when this is run as an applet:
    public void init() {
        start(this, true);
    }//end initiation method

    // called only when this is run as an application:
    public static void main(String[] args) {
        JFrame f = new JFrame("Dining");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dining me = new Dining();
        me.start(f, false);
        f.pack();            // calculate size of frame
        f.setVisible(true);
    }//end main methodz
    
}//end dining class
