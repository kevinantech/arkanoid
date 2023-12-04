/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.gamestate;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class GameWin {
    JFrame parent;

    public GameWin(JFrame parent) {
        this.parent = parent;
    }
    
    public void show(){
        JOptionPane.showMessageDialog(parent, "YOU WIN");
    }
    
    
}
