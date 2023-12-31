/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.ball;

import arkanoid.block.Block;
import arkanoid.pad.GamePad;
import arkanoid.score.Score;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Represents a game ball in Arkanoid.
 *
 * @author jc_go
 * http://code-stream.blogspot.com/2012/03/arkanoid-like-java-based-game.html
 */
public class GameBall extends Ball {
    private GamePad padBottom;
    private ArrayList<GameBall> balls;
    private ArrayList<Block> blocks;

    public GameBall(Point p, Color c, int dx, int dy, int diameter, Container parent, GamePad padBottom, ArrayList<GameBall> balls, ArrayList<Block> blocks,Score score) {
        super(p, c, dx, dy, diameter, parent,score);
        this.padBottom = padBottom;
        this.balls = balls;
        this.blocks = blocks;
    }

    @Override
    public void move() {
        // Colisiones con los bordes
        if (p.y + dy > parent.getHeight() - parent.getInsets().bottom) {
           this.isOut = true;
        }
        
        if (p.y + dy < 0) {
        dy = -dy; // Invertir la dirección en el eje Y para simular el rebote en el límite superior
        }

        if (p.x + dx < 0 || p.x + dx > parent.getWidth() - parent.getInsets().right - width) {
            dx = -dx; // Rebote en el eje X
        }

        // Colisión con la paleta inferior (GamePad)
        if (p.y + height + dy > padBottom.top && p.x + width > padBottom.left && p.x < padBottom.right) {
            dy = -dy; // Rebote en el eje Y

            // Aquí se ha detectado una colisión con la paleta inferior, puedes agregar lógica adicional si es necesario.

        }
        
        
        for (Block block : blocks) {
            if (block != null && block.isActive() && block.intersects(this)) {
                score.increaseScore();
                // Rebote en el eje Y
                if (dy > 0 && p.y + dy <= block.b.y) {
                    dy = -dy;
                }
                // Rebote en el eje X
                else if (dy < 0 && p.y + dy + height >= block.b.y + block.height) {
                    dy = -dy;
                }
                // Rebote en el eje X
                if (dx > 0 && p.x + dx <= block.b.x) {
                    dx = -dx;
                }
                // Rebote en el eje X
                else if (dx < 0 && p.x + dx + width >= block.b.x + block.width) {
                    dx = -dx;
                }
                block.desactivate();
            }    
        }
        // Colisión con otras pelotas
        for (GameBall otherBall : balls) { 
            if(otherBall!=null){
                if (otherBall != this && intersects(otherBall)) {
                    int tempDx = dx;
                    int tempDy = dy;
                    dx = otherBall.dx;
                    dy = otherBall.dy;
                    otherBall.dx = tempDx;
                    otherBall.dy = tempDy;
                }
            }
        }
        
        

        // Mover la pelota
        p.x += dx;
        p.y += dy;
    }
}
