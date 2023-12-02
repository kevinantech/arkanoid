/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.block;

import arkanoid.ball.ShapeBall;
import interfaces.Paintable;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class ShapeBlock implements Paintable {
    Point b;
    Color c;
    int width;
    int height;
    public Container parent;
    private boolean isActive; // Nuevo atributo para indicar si el bloque está activo

    public ShapeBlock(Point p, Color c, int width, int height, Container parent) {
        this.b = p;
        this.c = c;
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.isActive = true; // Inicialmente, el bloque está activo
    }

    @Override
    abstract public void paint(Graphics2D g);

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }

    public boolean intersects(ShapeBall ball) {
        return isActive && b.x < ball.p.x + ball.width &&
               b.x + width > ball.p.x &&
               b.y < ball.p.y + ball.height &&
               b.y + height > ball.p.y;
    }
}


