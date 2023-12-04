/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.block;

import constants.Constants;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author HP
 */
public class Block extends ShapeBlock {

    public Block(Point p, Color c, int width, int height, Container parent) {
        super(p, c, width, height, parent);
    }
    

    @Override
    public void paint(Graphics2D g) {
        if(isActive()){
            g.setColor(this.c);
            g.setColor(Constants.secondaryColor);        
            g.fill(new Rectangle2D.Double(b.x, b.y, width,height));
            g.drawRect(b.x, b.y, width, height);
        }
    }
    
}
