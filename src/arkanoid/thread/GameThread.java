/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.thread;

import arkanoid.ball.ShapeBall;
import arkanoid.gamestate.*;
import java.util.ArrayList;

/**
 *
 * @author jc_go
 * http://code-stream.blogspot.com/2012/03/arkanoid-like-java-based-game.html
 */
public class GameThread extends Thread{
    ShapeBall shape;
    int speed;
    private volatile boolean pause = false;
    private boolean stop = false;
    private ArrayList<GameThread> gameThreads;
    private TimerThread timerThread;
    private GameOver gameOver;
    private GameWin gamewin;
 
    public GameThread(ShapeBall shape, int speed, String name, ArrayList<GameThread> gameThreads, TimerThread timerThread, GameOver gameOver, GameWin gamewin){
        super(name);
        this.shape = shape;
        this.speed = speed;
        this.gameThreads = gameThreads;
        this.timerThread = timerThread;
        this.gameOver = gameOver;
        this.gamewin=gamewin;
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
        while (!stop) {
            synchronized (this) {
                while (pause) {
                    try {
                        wait(); 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
                
            shape.parent.repaint();
            shape.move();
            
            if(shape.getIsOut()) {
                stop = true;
                gameThreads.remove(this);
                if(gameThreads.size() == 0) {
                    timerThread.pauseGame();
                    gameOver.show();
                }
            }
            if(shape.score.getScore()==6000){
                pause = true;
                timerThread.pauseGame();
                gamewin.show();
            }
            try {
                Thread.sleep(speed);
            }
            catch(InterruptedException e) {
                System.out.println(e);
            }

        }
    }
}