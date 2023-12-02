/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.ball;

import arkanoid.pad.GamePad;

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

    public GameBall(Point p, Color c, int dx, int dy, int diameter, Container parent, GamePad padBottom,ArrayList<GameBall> balls ) {
        super(p, c, dx, dy, diameter, parent);
        this.padBottom = padBottom;
        this.balls = balls;
    }

    @Override
    public void move() {
        // Colisiones con los bordes
        if (p.y + dy < 0 || p.y + dy > parent.getHeight() - parent.getInsets().bottom - height) {
            dy = -dy; // Rebote en el eje Y
        }

        if (p.x + dx < 0 || p.x + dx > parent.getWidth() - parent.getInsets().right - width) {
            dx = -dx; // Rebote en el eje X
        }

        // Colisión con la paleta inferior (GamePad)
        if (p.y + height + dy > padBottom.top && p.x + width > padBottom.left && p.x < padBottom.right) {
            dy = -dy; // Rebote en el eje Y

            // Aquí se ha detectado una colisión con la paleta inferior, puedes agregar lógica adicional si es necesario.

        }

        // Colisión con otras pelotas
        for (GameBall otherBall : balls) { 
            if(otherBall!=null){
                if (otherBall != this && intersects(otherBall)) {
                    // Rebote entre pelotas
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