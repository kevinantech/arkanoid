/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.score;

import javax.swing.JLabel;

public class Score {
    private int value;
    private JLabel scoreLabel;

    public Score(int score, JLabel scoreLabel) {
        this.value = score;
        this.scoreLabel=scoreLabel;
    }

    public int getScore() {
        return value;
    }
    
    
    public synchronized void increaseScore(){
        value+=100;
        System.out.println(value);
        scoreLabel.setText(String.valueOf(value));
    }
    
}
