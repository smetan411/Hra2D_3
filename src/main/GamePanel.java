package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //screen settings
    final int originalTileSize = 16; //16x16 pixel, základní velikost spritů
    final int scale = 3; // zvětšení spritů třikrát
    public final int tileSize = originalTileSize * scale; //48x48 pixel
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;// 48x16=768 pixels
    final int screenHeight = tileSize * maxScreenRow;// 48*12=576 pixels

    // FPS
    int FPS = 60;
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    //player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();//automaticky zavola metodu run()
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;//=1sec/60, prekreslujeme 60x za vterinu
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
//            long currentTime = System.nanoTime();//1 sec = 10^9 nanoseconds
//            System.out.println("Current time : " + currentTime);
//            System.out.println("Hra běží.");
            update();
            repaint();
//po provedeni update a repaint potrebujeme znat, jak dlouho bude system spat
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                //na radku remainingTime = remainingTime / 1000000; prevadime cas v nano- na milisekundy kvuli sleep
                //najedte kurzorem na .sleep a v popisu to uvidite
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval; // po probehnuti sleep nastavime znovu drawInterval
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (keyH.upPressed == true) {
            playerY -= playerSpeed;
        } else if (keyH.downPressed == true) {
            playerY += playerSpeed;
        } else if (keyH.leftPressed == true) {
            playerX -= playerSpeed;
        } else if (keyH.rightPressed == true) {
            playerX += playerSpeed;
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // menime graphics na 2d, ma vice funkci
        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, tileSize, tileSize);//poloha a velikost ctverce
        g2.dispose();  //usetri pamet
    }

}