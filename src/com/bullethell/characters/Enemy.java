/*
 * Code latest updated 07/05/18 14:42.
 * Written  By Elias Renman.
 * Copyright © 2018.
 */
/*the enemy class object handles everything related to the enemy*/
package com.bullethell.characters;

import com.bullethell.bulletTypes.BouncingBullet;
import com.bullethell.bulletTypes.Bullet;
import com.bullethell.bulletTypes.SplittingBullet;
import com.bullethell.main.Main;

import javax.swing.*;
import java.awt.*;
public class Enemy extends HittableObjects {
    //boolean for breaking enemy patterns on game reset
    public boolean patternBreak = false;
    //Global Variables
    private  Image eIcon = new ImageIcon("resource/characters/Enemy.png").getImage();
    private int damageNormal = 1;
    public Rectangle newCoordinates = null;
    private Main main;
    private int xOffset = 25;
    private int yOffset = 50;
    public int splittingShotReady = 10;
    //Enemy object
    public Enemy(int enemyX, int enemyY, Main main) {
        resetHealth();
        this.main = main;
        //Creates a new Enemy Rectangle
        coordinates = new Rectangle(enemyX, enemyY,50,50);
        }

    public void startThread() {
        Runnable run = () -> {
            while(main.gameState.gameRunning) {
                if (!main.gameState.gamePaused) {
                    move();
                }
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(run).start();
    }
    public void resetHealth() {
        setHealth(100);
    }
    //Picks a random Coordinate to move to.
    private Rectangle pickMoveLocation() {
        Rectangle newLocation = new Rectangle();
         newLocation.x = 50 + (int) Math.floor(Math.random() * 365);
         newLocation.y = 50 + (int) Math.floor(Math.random() * 280);
         newLocation.setLocation(newLocation.x,newLocation.y);
         return newLocation;
    }
    //moves to a coordinate picked by pickMoveLocation
    private void move() {
        if(newCoordinates == null) {
            newCoordinates = pickMoveLocation();

        }else if (coordinates.x == newCoordinates.x && coordinates.y == newCoordinates.y){

            newCoordinates = pickMoveLocation();
            chooseAttack();
        } else {
            int xDirection;
            xDirection = Integer.compare(newCoordinates.x, coordinates.x);
            int yDirection;
            yDirection = Integer.compare(newCoordinates.y, coordinates.y);
            coordinates.y += yDirection;
            coordinates.x += xDirection;
        }
    }
    private void chooseAttack() {
        if (splittingShotReady != 0) {
            splittingShotReady--;
            for (int i = 0; i <= 1; i++)
                chooseAttackPattern();
        } else {
            splittingShotReady = 150;
            splittingAttack();
        }
    }
    public void draw(Graphics g){
        g.drawImage(eIcon, coordinates.x, coordinates.y,50,50, null);
        g.setColor(new Color(255,255,255,0));
        g.fillOval(coordinates.x, coordinates.y, coordinates.width, coordinates.height);
    }
    private void chooseAttackPattern() {

        int randomNumber = (int) (Math.random() * 2);
        if (randomNumber == 0) {
            strongAttack();
        } else if (randomNumber == 1) {
            normalAttack();
        }
    }
    private void strongAttack() {
        int bSizeXY = 12;
        int upYOffset = -3;
        Color color[] = {new Color(255, 153, 51,255),new Color(255,51,0,255),new Color(102,0,102,255)};
        for (int i = 0; i <= 5; i++) {
            int cModify = (int) Math.floor(Math.random() * 3);
            int damageStrong = 1;
            //bullet directions
            int[] xDirA = {3,2,1,0,-1,-2,-3,2,1,0,0,0,-1,-2};
            int[] yDirA ={1,2,3,4,3,2,1,1,2,3,4,3,2,1};
            //sends out bouncing bullets in different directions
            for (int j= 0; j < xDirA.length; j++) {
                main.bulletManager.addBullet(new BouncingBullet(coordinates.x + xOffset-(bSizeXY/2), coordinates.y + yOffset, xDirA[j], yDirA[j], bSizeXY, bSizeXY, color[cModify], true, false, this, damageStrong));
                main.bulletManager.addBullet(new BouncingBullet(coordinates.x + xOffset-(bSizeXY/2), coordinates.y + upYOffset, xDirA[j], yDirA[j]*-1, bSizeXY, bSizeXY, color[cModify], true, false, this, damageStrong));
            }
            int xDir = 4;
            //creates 4 regular bullets that go sideways to avoid bullets that bounce on the walls forever
            for (int j= 0; j <= 1; j++) {
                main.bulletManager.addBullet(new Bullet(coordinates.x + xOffset-(bSizeXY/2), coordinates.y + yOffset, xDir, 0, bSizeXY, bSizeXY, color[cModify], this, damageStrong));
                main.bulletManager.addBullet(new Bullet(coordinates.x + xOffset-(bSizeXY/2), coordinates.y + upYOffset, xDir, 0, bSizeXY, bSizeXY, color[cModify], this, damageStrong));
                xDir = xDir*-1;
            }
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //makes the enemy stop shooting if its in the middle of a pattern
            if (main.gameState.gamePaused && patternBreak) {
                break;
            }
        }
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void normalAttack() {
        int bSizeXY = 8;
        //Shoot in a random direction
        for (int i = 0; i < 7;i++) {

            main.bulletManager.addBullet(coordinates.x + xOffset-(bSizeXY/2), coordinates.y + yOffset, 1, 3, bSizeXY, bSizeXY, Color.RED, this,damageNormal);
            main.bulletManager.addBullet(coordinates.x + xOffset-(bSizeXY/2), coordinates.y + yOffset, 0, 2, bSizeXY, bSizeXY, Color.RED, this,damageNormal);
            main.bulletManager.addBullet(coordinates.x + xOffset-(bSizeXY/2), coordinates.y + yOffset, -1, 3, bSizeXY, bSizeXY, Color.RED, this,damageNormal);
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (main.gameState.gamePaused && patternBreak) {
                break;
            }
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void splittingAttack(){
        int bSizeXY = 20;
        main.bulletManager.addBullet(new SplittingBullet(coordinates.x+xOffset-(bSizeXY/2),coordinates.y + yOffset,0,3,bSizeXY,bSizeXY,Color.magenta,this,damageNormal,main,15));
    }
}
