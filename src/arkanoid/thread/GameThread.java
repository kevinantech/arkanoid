/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.thread;

import arkanoid.ball.ShapeBall;
import arkanoid.gamestate.*;
import arkanoid.records.RecordManager;
import java.util.ArrayList;

/**
 *
 * @author jc_go
 * http://code-stream.blogspot.com/2012/03/arkanoid-like-java-based-game.html
 */
public class GameThread extends Thread{
    ShapeBall shape;
    int speed;
    private String typeSpeed;
    private volatile boolean pause = false;
    private boolean stop = false;
    private ArrayList<GameThread> gameThreads;
    private TimerThread timerThread;
    private GameOver gameOver;
    private GameWin gamewin;
 
    public GameThread(
            ShapeBall shape, 
            int speed, 
            String typeSpeed, 
            String name, 
            ArrayList<GameThread> gameThreads, 
            TimerThread timerThread, 
            GameOver gameOver, 
            GameWin gamewin) {
        super(name);
        this.shape = shape;
        this.speed = speed;
        this.typeSpeed = typeSpeed;
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
            if(shape.score.getScore()==300){
                timerThread.pauseGame();
                gamewin.show();
                
                // En caso de victoria gestiona si el puntaje amerita de nuevo record.
                new RecordManager(
                        shape.score.getScore(), 
                        gameThreads.size(), 
                        typeSpeed, 
                        timerThread.getTimeInMinutes()
                    ).manageScore();
                
                pause = true;
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