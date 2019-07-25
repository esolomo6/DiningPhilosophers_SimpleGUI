package Dining_3146;
/**
 *
 * @author Eli Solomon
 */


//import packages
import java.awt.Color;
import java.awt.Graphics;

class Philosopher extends Thread {
    
    //delcare and initialize varables, color, timers, and size
    private static final Color START_COLOR = Color.WHITE;
    private static final Color THINK_COLOR = Color.BLUE;
    private static final Color WAIT_COLOR = Color.RED;
    private static final Color EAT_COLOR = Color.GREEN;

    private static final double DELAY_TIME = 0.5;
    private static final double THINK_TIME = 4.0;
    private static final double FUMBLE_TIME = 2.0;
    private static final double EAT_TIME = 3.0;

    
    private static final int XSIZE = 50;
    private static final int YSIZE = 50;

    //delcare varables that will be initialized in the constructor
    private Table master;
    private int x;
    private int y;
    private Utensil leftUtensil;
    private Utensil rightUtensil;
    private Color color;
    private final boolean lefty;

    // Constructor, cx and cy indicate coordinates of center
    public Philosopher(Table table, int cx, int cy, Utensil lf, Utensil rf, boolean lefty) {
        this.master = table;
        this.x = cx;
        this.y = cy;
        this.leftUtensil = lf;
        this.rightUtensil = rf;
        this.lefty = lefty;
        this.color = START_COLOR;
    }//end constructor

    // start method of Thread calls run
    public void run() {
        for (;;) {
            try {
                master.sleep(DELAY_TIME);
                think();
                master.sleep(DELAY_TIME);
                hunger();
                master.sleep(DELAY_TIME);
                eat();
            } catch(ResetException e) {
            master.onRestart();
                color = START_COLOR;
                master.repaint();
            }//end try-catch block
        }//end for loop
    }//end run method

    // render self
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x-XSIZE/2, y-YSIZE/2, XSIZE, YSIZE);
    }//end draw method

    private void think() throws ResetException {
        color = THINK_COLOR;
        master.repaint();
        master.sleep(THINK_TIME);
    }//end think method

    private void hunger() throws ResetException {
        color = WAIT_COLOR;
        master.repaint();

        leftUtensil.want();
        rightUtensil.want();

        master.sleep(FUMBLE_TIME);
        
        //if the philosopher is a lefty (one should be diffrent to prevent a deadlock) then pick up utensil on indicated side
        if (lefty) leftUtensil.pickUp(x, y); else rightUtensil.pickUp(x, y);
        yield();    // you aren't allowed to remove this
        if (lefty) rightUtensil.pickUp(x, y); else leftUtensil.pickUp(x, y);
        
    }//end hunger method

    private void eat() throws ResetException {
        color = EAT_COLOR;
        master.repaint();
        master.sleep(EAT_TIME);
        leftUtensil.putDown();
        yield();    // you aren't allowed to remove this
        rightUtensil.putDown();
    }//end eat method
    
}//end philosoper class

