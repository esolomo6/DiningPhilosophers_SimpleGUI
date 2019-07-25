package Dining_3146;
/**
 *
 * @author Eli Solomon
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.Semaphore;

//utensil class
class Utensil {

    //local varables declared
    private Table t;
    private int origX;
    private int origY;
    private int x;
    private int y;

    //create a semaphore with a lock permit of 1, one utensil is allowed to be used buy 1 philosoper at at time
    private volatile Semaphore sem = new Semaphore(1);
    
    //local varables declared and initalized
    private static final int XSIZE = 20;
    private static final int YSIZE = 2;
    private static final int YBOLD = 5;

    private int xsize = XSIZE;
    private int ysize = YSIZE;

    // Constructor.
    // cx and cy indicate coordinates of center.
    public Utensil(Table T, int cx, int cy) {
        t = T;
        origX = cx;
        origY = cy;
        x = cx;
        y = cy;
    }//end utensil Constructor

    public void reset() {
        resetPosition();
        sem = new Semaphore(1);
    }//end reset method

    private void resetPosition() {
        clear();
        x = origX;
        y = origY;
        ysize = YSIZE;
        t.repaint();
    }//end reset position methos

    private void setPosition(int px, int py) {
        clear();
        x = (origX + px)/2;
        y = (origY + py)/2;
        ysize = YSIZE;
        t.repaint();
    }//end setPosition method

    //this method makes the utensils bold if a philosoper is hungry and prone to pick it up
    public void want() {
        clear();
        ysize = YBOLD;
        t.repaint();
    }//end want method

    // arguments are coordinates of acquiring philosopher's center
    public void pickUp(int px, int py) throws ResetException {
        try {
            //if a utensil is picked up aquire the lock from the semaphore
            sem.acquire();
        }//end try block
        catch (InterruptedException e) {
            throw new ResetException();
        }//end catch
        setPosition(px, py);
    }//end pickUp method

    public void putDown() {
        resetPosition();
        //if the utensil is put down release the semaphore
        sem.release();
    }//end put down method

    // draw self
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(x-xsize/2, y-ysize/2, xsize, ysize);
    }//end draw method

    // clear self
    private void clear() {
        Graphics g = t.getGraphics();
        g.setColor(t.getBackground());
        g.fillRect(x-xsize/2, y-ysize/2, xsize, ysize);
    }//end clear method
}//end utensil class
