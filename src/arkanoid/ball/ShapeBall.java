/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.ball;

import arkanoid.score.Score;
import interfaces.Movable;
import interfaces.Paintable;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Represents a shape in the game.
 *
 * @author jc_go
 * http://code-stream.blogspot.com/2012/03/arkanoid-like-java-based-game.html
 */
public abstract class ShapeBall implements Paintable, Movable {
    public Point p;
    Color c;
    int dx;
    int dy;
    public int width;
    public int height;
    public Container parent;
    public boolean isOut;
    public Score score;

    ShapeBall(Point p, Color c, int dx, int dy, int width, int height, Container parent, Score score) {
        this.p = p;
        this.c = c;
        this.dx = dx;
        this.dy = dy;
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.isOut = false;
        this.score=score;
    }
    
    public boolean getIsOut() { 
        return isOut;
    }
    
    

    @Override
    public void move() {}

    @Override
    abstract public void paint(Graphics2D g);

    public boolean intersects(ShapeBall other) {
        return p.x < other.p.x + other.width &&
               p.x + width > other.p.x &&
               p.y < other.p.y + other.height &&
               p.y + height > other.p.y;
    }

    @Override
    public String toString() {
        return "Shape [p=" + p + ", c=" + c + ", dx=" + dx + ", dy=" + dy +
               ", width=" + width + ", height=" + height + ", parent=" + parent + "]";
    }
}
