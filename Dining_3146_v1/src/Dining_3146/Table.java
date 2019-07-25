package Dining_3146;
/**
 *
 * @author Eli Solomon
 */
//import packages
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;

// Graphics panel in which philosophers and forks appear.
class Table extends JPanel {

    //declare and initalize the number of philosophers at the table
    private static final int NUM_PHILS = 5;

    // following fields are set by construcctor:
    private Utensil[] utensil;
    private Philosopher[] philosophers;

    private boolean running = false;

    public synchronized void pause() {
        running = false;
        notifyAll();
    }//end pause method

    public synchronized void resume() {
        running = true;
        notifyAll();
    }//end resume method

    private final Random rnd = new Random();

    // sleep for secs +- FUDGE (%) seconds
    private static final double FUDGE = Dining.DEADLOCK ? 0.0 : 0.2;

    public synchronized void sleep(double secs) throws ResetException {
        double millis = 1000 * secs;
        int window = FUDGE == 0.0 ? 0 : rnd.nextInt((int) (2.0 * millis * FUDGE));
        long duration = (long) ((1.0 - FUDGE) * millis + window);
        try {
            for (;;) {
                if (running) {
                    if (duration <= 0) return;
                    long nanos = System.nanoTime();
                    wait(duration);
                    nanos = System.nanoTime() - nanos;
                    duration -= nanos / 1000000;
                }//end if
                else {
                    wait();
                }//end else
            }//end infinite for loop
        }//end try block
        catch (InterruptedException e) {
            throw new ResetException();
        }//end catch block
    }//end sleep method

    // Called by the UI when it wants to start over (reset buttom is pressed)
    private final Semaphore stopAll = new Semaphore(0);
    private final Semaphore startAll = new Semaphore(0);

    public void reset() {
        running = false;

        // force philosophers to notice change in coordinator state:
        for (int i = 0; i < NUM_PHILS; i++) {
            philosophers[i].interrupt();
        }//end for loop to cycle through the philosopor threads

        stopAll.acquireUninterruptibly(NUM_PHILS);

        for (int i = 0; i < NUM_PHILS; i++) {
            utensil[i].reset();
        }//end for loop to cycle through each utensil

        startAll.release(NUM_PHILS);
    }//end reset method

    public void onRestart() {
        stopAll.release();
        startAll.acquireUninterruptibly();
    }//end onRestart method

    // The following method is called automatically by the graphics system when it thinks the Table canvas needs to be re-displayed.
    // This can happen because code elsewhere in this program called repaint(),
    // or because of hiding/revealing or open/close 
    // operations in the surrounding window system.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < NUM_PHILS; i++) {
            utensil[i].draw(g);
            philosophers[i].draw(g);
        }//end for loop to cycle through each philosopher and utensil

        g.setColor(Color.black);
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);
    }//end paintComponent method


    // Constructor
    // Note that angles are measured in radians, not degrees.
    // The origin is the upper left corner of the frame.
    public Table(int canvasSize) {  

        utensil = new Utensil[NUM_PHILS];
        philosophers = new Philosopher[NUM_PHILS];
        setPreferredSize(new Dimension(canvasSize, canvasSize));

        for (int i = 0; i < NUM_PHILS; i++) {
            double angle = Math.PI/2 + 2*Math.PI/NUM_PHILS*(i-0.5);
            utensil[i] = new Utensil(this,
                (int) (canvasSize/2.0 + canvasSize/6.0 * Math.cos(angle)),
                (int) (canvasSize/2.0 - canvasSize/6.0 * Math.sin(angle)));
        }//for each philosopher populate the utensil array

        //the last field to be passed into the constructor is the lefty boolean so make one return false at i=0, 0>0==flase
        for (int i = 0; i < NUM_PHILS; i++) {
            double angle = Math.PI/2 + 2*Math.PI/NUM_PHILS*i;
            philosophers[i] = new Philosopher(this,
                (int) (canvasSize/2.0 + canvasSize/3.0 * Math.cos(angle)),
                (int) (canvasSize/2.0 - canvasSize/3.0 * Math.sin(angle)),
                utensil[i],
                utensil[(i+1) % NUM_PHILS],
                Dining.DEADLOCK || i > 0);
            philosophers[i].start();
        }//for each philosopher in the philosopher array initalize a philosopher
    }//end table constructor

}//end table class
