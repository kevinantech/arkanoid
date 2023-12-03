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
    
    
    public void increaseScore(){
        value+=500;
        scoreLabel.setText(String.valueOf(value));
    }
    
}
