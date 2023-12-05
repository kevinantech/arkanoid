/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.records;

/**
 *
 * @author KEVIN ANDRES GOMEZ M
 */
public class Record {
    private String initials;
    private float score;
    private int numBalls;
    private String speedType;

    public Record(String initials, float score, int numBalls, String speedType) {
        this.initials = initials;
        this.score = score;
        this.numBalls = numBalls;
        this.speedType = speedType;
    }

    public String getInitials() {
        return initials;
    }

    public float getScore() {
        return score;
    }

    public int getNumBalls() {
        return numBalls;
    }

    public String getSpeedType() {
        return speedType;
    }
}
