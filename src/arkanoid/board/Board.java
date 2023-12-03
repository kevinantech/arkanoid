/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid.board;

import arkanoid.ball.GameBall;
import arkanoid.pad.GamePad;
import arkanoid.panel.GamePanel;
import arkanoid.thread.GameThread;
import arkanoid.thread.TimerThread;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import constants.Constants;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import arkanoid.block.Block;
import arkanoid.block.ShapeBlock;
import arkanoid.thread.BlockThread;

/**
 *
 * http://code-stream.blogspot.com/2012/03/arkanoid-like-java-based-game.html
 * https://github.com/j-c-garciao/academics/tree/master/ProgrammingCourses/OOP/Arkanoid
public class Board extends javax.swing.JFrame implements ActionListener {

 */
public class Board extends javax.swing.JFrame implements ActionListener {
    private GamePanel panel = new GamePanel();
    private GamePad pad = new GamePad(580, 100, 600 - 2, 200, Color.ORANGE, this);
    private ArrayList<GameThread> gameThreads = new ArrayList<>();
    private ArrayList<Block> blocks=new ArrayList<>();
    private int speed = Constants.SPEED_SLOW;
    private int ballsPrefences = Constants.BALLS_ONE;
    private boolean pause = false;
    private JMenuItem pauseItem;
    private TimerThread timer;
    private boolean playing = false;
    
    public Board() {
        panel.setBackground(Color.WHITE);
        JMenuBar menuBar = new JMenuBar();  
        
        /**
         * Menu "Game"
         */
        JMenu gameMenu = new JMenu("Game");
        JMenuItem playItem = new JMenuItem("Play");
        this.pauseItem = new JMenuItem("Pause");
        JMenuItem restartItem = new JMenuItem("Restart");
        JMenuItem closeItem = new JMenuItem("Close");
        gameMenu.add(playItem);
        gameMenu.addSeparator();
        gameMenu.add(this.pauseItem);
        gameMenu.addSeparator();
        gameMenu.add(restartItem);
        gameMenu.addSeparator();
        gameMenu.add(closeItem);
        playItem.setActionCommand(Constants.ACTION_PLAY_GAME);
        playItem.addActionListener(this);
        this.pauseItem.setActionCommand(Constants.ACTION_PAUSE_GAME);
        this.pauseItem.addActionListener(this);
        restartItem.setActionCommand(Constants.ACTION_RESTART_GAME);
        restartItem.addActionListener(this);
        closeItem.setActionCommand(Constants.ACTION_CLOSE_GAME);
        closeItem.addActionListener(this);
        menuBar.add(gameMenu);
        
        /**
         * Menu "Options"
         */
        JMenu optionsMenu = new JMenu("Options");
        menuBar.add(optionsMenu);
        
        /**
         * Submenu "Speed"
         */
        JMenu speedSubmenu = new JMenu("Speed");
        ButtonGroup speedBtnGroup = new ButtonGroup();
        JRadioButtonMenuItem verySlowOption = new JRadioButtonMenuItem("Very slow");
        verySlowOption.setActionCommand(Constants.ACTION_SPEED_VERY_SLOW);
        verySlowOption.addActionListener(this);
        speedBtnGroup.add(verySlowOption);
        speedSubmenu.add(verySlowOption);
        JRadioButtonMenuItem slowOption = new JRadioButtonMenuItem("Slow");
        slowOption.setActionCommand(Constants.ACTION_SPEED_SLOW);
        slowOption.addActionListener(this);
        speedBtnGroup.add(slowOption);
        speedSubmenu.add(slowOption);
        JRadioButtonMenuItem fastOption = new JRadioButtonMenuItem("Fast");
        fastOption.setActionCommand(Constants.ACTION_SPEED_NORMAL);
        fastOption.addActionListener(this);
        speedBtnGroup.add(fastOption);
        speedSubmenu.add(fastOption);
        JRadioButtonMenuItem veryFastOption = new JRadioButtonMenuItem("Very Fast");
        veryFastOption.setActionCommand(Constants.ACTION_SPEED_FAST);
        veryFastOption.addActionListener(this);
        speedBtnGroup.add(veryFastOption);
        speedSubmenu.add(veryFastOption);
        optionsMenu.add(speedSubmenu);
        
        // Establece por defecto la velocidad lenta (UI).
        speedBtnGroup.setSelected(slowOption.getModel(), true);

        /**
         * Submenu "Balls"
         */
        JMenu ballsSubmenu = new JMenu("Balls");
        ButtonGroup ballsBtnGroup = new ButtonGroup();
        JRadioButtonMenuItem oneBallOption = new JRadioButtonMenuItem("One");
        oneBallOption.setActionCommand(Constants.ACTION_BALLS_ONE);
        oneBallOption.addActionListener(this);
        ballsBtnGroup.add(oneBallOption);
        ballsSubmenu.add(oneBallOption);
        JRadioButtonMenuItem twoBallsOption = new JRadioButtonMenuItem("Two");
        twoBallsOption.setActionCommand(Constants.ACTION_BALLS_TWO);
        twoBallsOption.addActionListener(this);
        ballsBtnGroup.add(twoBallsOption);
        ballsSubmenu.add(twoBallsOption);
        JRadioButtonMenuItem threeBallsOption = new JRadioButtonMenuItem("Three");
        threeBallsOption.setActionCommand(Constants.ACTION_BALLS_THREE);
        threeBallsOption.addActionListener(this);
        ballsBtnGroup.add(threeBallsOption);
        ballsSubmenu.add(threeBallsOption);
        optionsMenu.add(ballsSubmenu);
        
        // Establece por defecto 1 pelota en el juego. (UI)
        ballsBtnGroup.setSelected(oneBallOption.getModel(), true);
        
        /**
         * Menu "Records"
         */
        JMenu recordsMenu = new JMenu("Records");
        JMenuItem menuItemShowBestTimes = new JMenuItem("Show best times");
        recordsMenu.add(menuItemShowBestTimes);
        menuBar.add(recordsMenu);
        
        //-----------------------------------------------------------------------------------
        
        panel.add(pad);
        this.add(panel, BorderLayout.CENTER);
        this.add(menuBar, BorderLayout.NORTH);
        
        //Jlabel del cronometro
        JLabel cronometroLabel = new JLabel("00:00:0000");
        cronometroLabel.setHorizontalAlignment(JLabel.CENTER); // Centrar el texto
        cronometroLabel.setFont(new Font("Arial", Font.BOLD, 32)); // Fuente y tamaño
        panel.add(cronometroLabel);

        // Establecer el layout del panel como FlowLayout para centrar el JLabel
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        timer = new TimerThread(0,0,0,cronometroLabel);
        this.add(panel, BorderLayout.CENTER);
        this.add(menuBar, BorderLayout.NORTH);
        
        // Movilidad del GamePad
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {                
                int keyCode = e.getKeyCode();
                if(!pause) {
                    switch(keyCode) {
                        case 39:
                            pad.moveRight(panel.getGraphics());
                            repaint();
                            break;
                        case 37:
                           pad.moveLeft(panel.getGraphics());
                           repaint();
                            break;
                        case 68:
                            pad.moveRight(panel.getGraphics());
                            repaint();
                            break;
                        case 65: 
                            pad.moveLeft(panel.getGraphics());
                            repaint();
                            break;
                    }       
                } 
            }
        });
        //initComponents();
        this.setTitle("Arkanoid");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(Constants.WIDTH, Constants.HEIGHT);
        this.setResizable(false); 
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()) {
            case Constants.ACTION_PLAY_GAME: {
            if(!playing) {
                ArrayList<GameBall> balls =new ArrayList<>();
                Random r = new Random();
                int aux = 10;
                int width = 41, height = 20;
                int py = 10;
                
                for(int k = 0; k < 5; k++){
                    int px = 16;
                    for(int j = 0; j < 12; j++){
                        Point p = new Point(px,py);
                        Block b = new Block(p, new Color(255, 73, 73), width, height, panel);
                        panel.add(b);
                        blocks.add(b);
                        BlockThread block=new BlockThread(b,balls,speed);
                        initBlock(block);
                        px += 16 + width;
                    }
                    py += 10 + height;
                }
                
                for(int i = 0; i < ballsPrefences; i++) {
                    GameBall gameBall = new GameBall(new Point(r.nextInt(650)+aux, 300), Color.RED, 1, 1, 10, panel, pad, balls, blocks);
                    balls.add(gameBall);
                    panel.add(gameBall);
                    GameThread gameThread = new GameThread(gameBall, speed, "GameThread_" + i+1);
                    gameThreads.add(gameThread);
                    initGame(gameThread);
                    aux += 10;
                    
                }
                initTime(timer);
                playing = true;
            } else System.out.println("YA SE ESTA JUGANDO!");
                break;
            }
            case Constants.ACTION_PAUSE_GAME: {                
                this.pause = !this.pause;
                if(timer!=null){
                    if(this.pause) timer.pauseGame();
                    else timer.resumeGame();
                }
                gameThreads.forEach((g) -> {
                    if(g != null) {
                        if(this.pause) g.pauseGame();
                        else g.resumeGame();
                    }
                });
                this.pauseItem.setText( this.pause ? "Resume" : "Pause");
                break;
            }
            case Constants.ACTION_RESTART_GAME: {
                break;
            }
            case Constants.ACTION_CLOSE_GAME: {
                break;
            }
            case Constants.ACTION_SPEED_VERY_SLOW: {
                this.speed = Constants.SPEED_VERY_SLOW;
                gameThreads.forEach((g) -> {
                    if(g != null) { 
                        System.out.println("Velocidad Cambiada: " + speed);
                        g.setSpeed(speed);
                    }
                });
                break;
            }
            case Constants.ACTION_SPEED_SLOW: {
                this.speed = Constants.SPEED_SLOW;
                gameThreads.forEach((g) -> {
                    if(g != null) { 
                        System.out.println("Velocidad Cambiada: " + speed);
                        g.setSpeed(speed);
                    }
                });
                break;
            }
            case Constants.ACTION_SPEED_NORMAL: {
                this.speed = Constants.SPEED_FAST;
                gameThreads.forEach((g) -> {
                    if(g != null) { 
                        System.out.println("Velocidad Cambiada: " + speed);
                        g.setSpeed(speed);
                    }
                });
                break;
            }
            case Constants.ACTION_SPEED_FAST: {
                this.speed = Constants.SPEED_VERY_FAST;
                gameThreads.forEach((g) -> {
                    if(g != null) { 
                        System.out.println("Velocidad Cambiada: " + speed);
                        g.setSpeed(speed);
                    }
                });
                break;
            }
            case Constants.ACTION_BALLS_ONE: {
                this.ballsPrefences = Constants.BALLS_ONE;
                break;
            }
            case Constants.ACTION_BALLS_TWO: {
                this.ballsPrefences = Constants.BALLS_TWO;
                break;
            }
            case Constants.ACTION_BALLS_THREE: {
                this.ballsPrefences = Constants.BALLS_THREE;
                break;
            }
        }
    }

    private void initGame(GameThread gt){
        gt.start();
    }
    private void initTime(TimerThread gr){
        gr.start();
    }
    private void initBlock(BlockThread gb){
        gb.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Board().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}