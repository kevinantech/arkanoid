/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.thread;

import arkanoid.ball.ShapeBall;

/**
 *
 * @author jc_go
 * http://code-stream.blogspot.com/2012/03/arkanoid-like-java-based-game.html
 */
public class GameThread extends Thread{
    ShapeBall shape;
    int speed;
    private volatile boolean pause = false;
 
    public GameThread(ShapeBall shape, int speed, String name){
        super(name);
        this.shape = shape;
        this.speed = speed;
    }
 
    public void setSpeed(int speed){
        this.speed = speed;
    }
     
    public synchronized void pauseGame() {
        pause = true;
    }
    
    public synchronized void resumeGame() {
        notify();
        pause = false;
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (pause) {
                    try {
                        wait(); // el hilo espera si está pausado
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
                
            shape.parent.repaint();
            shape.move();
            try {
                Thread.sleep(speed);
            }
            catch(InterruptedException e) {
                System.out.println(e);
            }

        }
    }
}