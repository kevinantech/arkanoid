/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.gameover;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author KEVIN ANDRES GOMEZ M
 */
public class GameOver {
    JFrame parent;
    
    public GameOver(JFrame parent) {
        this.parent = parent;
    }
    
    public void show() {
        JOptionPane.showMessageDialog(parent, "GAME OVER");
    }
}
