package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean  upPressed, downPressed, leftPressed, rightPressed, interactPressed;

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        // Menu State
        if(gp.gameState == gp.menuState){
            if(code == KeyEvent.VK_W && gp.ui.cmdNum > 0){
                gp.ui.cmdNum--;
            }
            if(code == KeyEvent.VK_S && gp.ui.cmdNum < 3){
                gp.ui.cmdNum++;
            }
            if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER){
                if(gp.ui.cmdNum == 0){
                    gp.gameState = gp.playState;
                    //gp.playMusic(0);
                }
                if(gp.ui.cmdNum == 1){  // Load Game
                    
                }
                if(gp.ui.cmdNum == 2){  // Control
                    gp.gameState = gp.optionsState;
                }
                if(gp.ui.cmdNum == 3){
                    System.exit(0);
                }
            }
        }
        // Options State
        if(gp.gameState == gp.optionsState){
            if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
                gp.gameState = gp.menuState;
            }
        }
        // Play State
        if(gp.gameState == gp.playState || gp.battleManager.state == gp.battleManager.state.DODGING){
           if(code == KeyEvent.VK_W){
                upPressed = true;
            }
            if(code == KeyEvent.VK_S){
                downPressed = true;
            }
            if(code == KeyEvent.VK_A){
                leftPressed = true;
            }
            if(code == KeyEvent.VK_D){
                rightPressed = true;
            }
            if(code == KeyEvent.VK_C){
                if (gp.gameState != gp.battleState){
                    gp.gameState = gp.characterState;
                }
            }
            if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_P){
                if (gp.gameState != gp.battleState) {
                    gp.gameState = gp.pauseState;
                    gp.ui.cmdNum = 0;
                }                
            }
            if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
                interactPressed = true;
            }
        }

        // Pause State
        else if(gp.gameState == gp.pauseState) { 
            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.cmdNum--;
                if (gp.ui.cmdNum < 0) { // Back to 1
                    gp.ui.cmdNum = 1;
                }
            }
            if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.cmdNum++;
                if (gp.ui.cmdNum > 1) { // Back to 0
                    gp.ui.cmdNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (gp.ui.cmdNum == 0) {    // Continue
                    if(gp.inCombat == true){ gp.gameState = gp.battleState; }
                    else{ gp.gameState = gp.playState; }
                }
                if (gp.ui.cmdNum == 1) {    // Back to Menu
                    gp.gameState = gp.menuState;
                    gp.ui.cmdNum = 0;
                }
            }
            if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_P){
                if(gp.inCombat == true){ gp.gameState = gp.battleState; }
                else{ gp.gameState = gp.playState; }
            }}

        // Dialogue State
        else if(gp.gameState == gp.dialogueState){
            if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
                gp.gameState = gp.playState;
            }
        }

        // Character State
        else if(gp.gameState == gp.characterState){
            if(code == KeyEvent.VK_C || code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_P){
                gp.gameState = gp.playState;
            }
            if(code == KeyEvent.VK_W && gp.ui.slotRow != 0){
                gp.ui.slotRow--;
            }
            if(code == KeyEvent.VK_S && gp.ui.slotRow != 1){
                gp.ui.slotRow++;
            }
            if(code == KeyEvent.VK_A && gp.ui.slotCol != 0){
                gp.ui.slotCol--;
            }
            if(code == KeyEvent.VK_D && gp.ui.slotCol != 4){
                gp.ui.slotCol++;
            }
        }

        // Battle State
        else if(gp.gameState == gp.battleState) {
            gp.battleManager.handleKeyPress(code);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        /*if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE){
            interactPressed = false;
        }*/
    }
    
}
