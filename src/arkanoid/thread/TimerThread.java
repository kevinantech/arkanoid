
package arkanoid.thread;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class TimerThread extends Thread {
    private int minutes;
    private int seconds;
    private int miliseconds;
    private JLabel jLabel;
    private volatile boolean pause = false;

    public TimerThread(int minutes, int seconds, int miliseconds, JLabel jLabel) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.miliseconds = miliseconds;
        this.jLabel = jLabel;
    }

    public synchronized void pauseGame() {
        pause = true;
    }

    public synchronized void resumeGame() {
        pause = false;
        notify();
    }

    @Override
    public void run() {
        long previousTime = System.currentTimeMillis();

        while (true) {
            synchronized (this) {
                while (pause) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            long currentTime=  System.currentTimeMillis();
            long timeElapsed = currentTime - previousTime;

            // Actualizar tiempo basándonos en el tiempo transcurrido
            miliseconds += timeElapsed;
            previousTime = currentTime;

            if (miliseconds >= 1000) {
                miliseconds =0;
                seconds++;
            }
            if (seconds >= 60) {
                seconds = 0;
                minutes++;
            }

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    jLabel.setText(String.format("%02d : %02d : %04d", minutes, seconds, miliseconds));
                }
            });

            try {
                // Ajustar el tiempo de espera para controlar la velocidad del bucle
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

