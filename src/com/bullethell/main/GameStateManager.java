/*
 * Code latest updated 28/04/18 18:01.
 * Written  By Elias Renman.
 * Copyright © 2018.
 */

package com.bullethell.main;
import com.bullethell.bulletTypes.Bullet;
import com.bullethell.characters.Enemy;
import com.bullethell.characters.Player;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameStateManager {
    Main main;
    public GameStateManager(Main main) {
        this.main = main;
    }
    private void gameResetVariables() {
        main.bulletTrackerKilled.removeAll(main.bulletTrackerKilled);
        main.bulletTracker.removeAll(main.bulletTracker);
        main.bulletNew.removeAll(main.bulletNew);
        main.player1.coordinates.x = 246;
        main.player1.coordinates.y = 550;
        main.player1.resetHealth();
        main.enemy1.resetHealth();
        main.enemy1.coordinates.x = 225;
        main.enemy1.coordinates.y = 200;
        main.enemy1.newCoordinates = null;
        main.gamePaused = false;
    }
    protected void gameReset() {
        main.enemy1 = new Enemy(225, 200, main);
        main.player1 = new Player(246, 550, main);
        //Create player thread
        Thread playerThread = new Thread(main.player1);
        //Start player Related threads
        playerThread.start();
        main.player1.startPlayerBulletThread();
        //initially draw the canvas
        main.draw();
        //Creates enemy
        main.enemy1.startThread();
    }
    //Main loop
    protected void gameRunning() {
        while (main.running) {
            try {
                Thread.sleep(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            main.draw();
            if (!main.gamePaused) {
                //main.draw();
                /**Checks colision, the array is to avoid concurrent modification.*/
                ArrayList<Bullet> collisionTracker = new ArrayList<>();
                collisionTracker.addAll(main.bulletTracker);
                main.collision(main.enemy1, collisionTracker);
                main.collision(main.player1, collisionTracker);

                //update bullets coordinates
                main.bulletManager.updateBullet();
            }
            try {
                Thread.sleep(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
