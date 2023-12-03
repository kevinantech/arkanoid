/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.thread;

import arkanoid.ball.GameBall;
import arkanoid.block.Block;
import constants.Constants;
import java.util.ArrayList;

public class BlockThread extends Thread {
    Block block;
    ArrayList<GameBall> balls;
    int speed;

    public BlockThread(Block block, ArrayList<GameBall> balls, int speed) {
        this.block = block;
        this.balls=balls;
        this.speed=speed;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    wait(); // el hilo espera si está pausado
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            

            try {
                Thread.sleep(speed); // Usa la constante de tiempo de espera
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}
