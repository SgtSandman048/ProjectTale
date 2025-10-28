package battle;

import entity.Entity;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.GamePanel;

public class BattleManager {

    public enum BattleState {
        MENU, DIALOGUE, DODGING, RESULT, GAMEOVER, ITEMSELECT, ACTSELECT
    }
    public int itemSelectIndex = 0;
    public BattleState state = BattleState.MENU;

    GamePanel gp;
    public PlayerHeart player;
    public BattleEnemy enemy;
    public BattleMenu menu;

    public List<Bullet> bullets = new ArrayList<>();
    Random rand = new Random();

    // Battle Zone
    AttackSpawner spawner;
    Rectangle battleBox;

    long dodgeStartTime;
    int dodgeDuration = 10000;

    // Attack Pattern
    int currentPattern = 0;
    long patternStartTime;
    int currentPatternDuration;

    // Dialogue
    public String currentBattleDialogue = "";
    public BattleState nextState;
    public int actCmdIndex = 0;

    // Input
    boolean enterPressed = false;

    // Battle Area
    public int battleBoxX = 100;
    public int battleBoxY = 280;
    public int battleBoxWidth = 440;
    public int battleBoxHeight = 150;

    public BattleManager(GamePanel gp) {
        this.gp = gp;

        player = new PlayerHeart(gp.screenWidth / 2, battleBoxY + battleBoxHeight / 2);
        
        enemy = new BattleEnemy("Froggy", 30, null, gp);
        
        menu = new BattleMenu();
        menu.addOption("FIGHT");
        menu.addOption("ACT");
        menu.addOption("ITEM");
        menu.addOption("MERCY");

        this.battleBox = new Rectangle(battleBoxX, battleBoxY, battleBoxWidth, battleBoxHeight);
        this.spawner = new AttackSpawner();
        
    }

    /**
    *@param enemyEntity
    */
    public void setupBattle(Entity enemyEntity) {
        enemy = new BattleEnemy(enemyEntity.name, enemyEntity.maxHp, enemyEntity.spriteEnemy, gp); 
        
        player.maxHp = gp.player.maxHp;
        player.hp = gp.player.Hp; 
        
        player.x = gp.screenWidth / 2;
        player.y = battleBoxY + battleBoxHeight / 2;
        
        state = BattleState.MENU;
        bullets.clear();
        menu.selectedIndex = 0;
        enterPressed = false;

        currentBattleDialogue = enemy.getIntroDialogue();
        nextState = BattleState.MENU;
        state = BattleState.DIALOGUE;
        actCmdIndex = 0;

    }

    public void update() {
        switch (state) {
            case MENU:
                if (enterPressed) {
                    handleMenuChoice(menu.getSelected());
                    enterPressed = false;
                }
                break;
            case DIALOGUE:
                if (enterPressed) {
                    state = nextState;
                    enterPressed = false;
                
                    if (nextState == BattleState.DODGING) {
                        startDodgingPhase();
                    }
                }
                break;
            
            case DODGING:
                player.dx = 0;
                player.dy = 0;
                if (gp.keyH.upPressed) player.dy = -player.speed;
                if (gp.keyH.downPressed) player.dy = player.speed;
                if (gp.keyH.leftPressed) player.dx = -player.speed;
                if (gp.keyH.rightPressed) player.dx = player.speed;
                player.update(battleBoxX, battleBoxY, battleBoxWidth, battleBoxHeight);

                for (int i = bullets.size() - 1; i >= 0; i--) {
                    Bullet b = bullets.get(i);
                    b.update();
                    if (b.isOffScreen(gp.screenWidth, gp.screenHeight)) {
                        bullets.remove(i);
                    } else if (player.intersects(b)) {
                        player.takeDamage(b.damage);
                        bullets.remove(i);
                    }
                }
                if (System.currentTimeMillis() - patternStartTime > currentPatternDuration) {
                    selectNextPattern();
                }

                spawnBullets();
                
                if (player.hp <= 0){
                    state = BattleState.GAMEOVER;
                    gp.ui.cmdNum = 0;
                }

                if (System.currentTimeMillis() - dodgeStartTime > dodgeDuration) {
                    if (player.hp > 0) {
                        state = BattleState.MENU;
                    } else { state = BattleState.GAMEOVER; gp.ui.cmdNum = 0;}
                }
                break;
            case RESULT:
                if (enterPressed) {
                    gp.gameState = gp.playState;
                    enterPressed = false;
                    gp.inCombat = false;
                }
                break;
            case GAMEOVER:
                if (enterPressed) {
                    if (gp.ui.cmdNum == 0) {
                        gp.gameState = gp.menuState;
                        gp.ui.cmdNum = 0;
                    } else if (gp.ui.cmdNum == 1) {
                        gp.gameState = gp.menuState;
                        gp.ui.cmdNum = 0;
                    }
                    enterPressed = false;
                    gp.inCombat = false;
                    
                }
                break;
            case ITEMSELECT:
            if (enterPressed) {
                    if (gp.player.inventory.isEmpty()) {
                        state = BattleState.MENU;
                    } else {
                        Entity selectedItem = gp.player.inventory.get(itemSelectIndex);

                        if (selectedItem.isUsableInBattle) {
                            useItem(selectedItem);
                            gp.player.inventory.remove(itemSelectIndex);
                            startDodgingPhase();
                        } else {
                            state = BattleState.MENU;
                        }
                    }
                    enterPressed = false;
                }
                break;
            case ACTSELECT:
                if (enterPressed) {
                    handleActChoice();
                    enterPressed = false;
                break;
            }
        }
    }

 
    public void draw(Graphics2D g2) {
        gp.ui.draw(g2);
    }


    public void handleKeyPress(int code) {
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
            enterPressed = true;
        }

        switch (state) {
            case MENU:
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    menu.moveUp();
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    menu.moveDown();
                }
                break;
            
            case DODGING:
                break;
            case RESULT:
                break;
            case GAMEOVER:
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    gp.ui.cmdNum--;
                    if (gp.ui.cmdNum < 0) { gp.ui.cmdNum = 1; }
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    gp.ui.cmdNum++;
                    if (gp.ui.cmdNum > 1) { gp.ui.cmdNum = 0; }
                }
                break;
            case ITEMSELECT:
                if (gp.player.inventory.size() > 0) {
                    if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                        itemSelectIndex--;
                        if (itemSelectIndex < 0) {
                            itemSelectIndex = gp.player.inventory.size() - 1; // วนกลับไปท้ายสุด
                        }
                    }
                    if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                        itemSelectIndex++;
                        if (itemSelectIndex >= gp.player.inventory.size()) {
                            itemSelectIndex = 0; // วนกลับไปอันแรก
                        }
                    }
                }
                if (code == KeyEvent.VK_ESCAPE) { // <<< เพิ่ม: กด ESC เพื่อยกเลิก
                    state = BattleState.MENU;
                }
                break;
            case ACTSELECT:
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    actCmdIndex--;
                    if (actCmdIndex < 0) { actCmdIndex = enemy.actOptions.size() - 1; }
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    actCmdIndex++;
                    if (actCmdIndex >= enemy.actOptions.size()) { actCmdIndex = 0; }
                }
                if (code == KeyEvent.VK_ESCAPE) {
                    state = BattleState.MENU;
                }
                break;
        }
    }


    private void handleMenuChoice(String choice) {
        switch (choice) {
            case "FIGHT":
                enemy.takeDamage(gp.player.attack);
                if (enemy.hp <= 0) {
                    state = BattleState.RESULT;
                    gp.player.gainXp(350);
                } else {
                    startDodgingPhase();
                }
                break;
            case "ACT":
                state = BattleState.ACTSELECT;
                actCmdIndex = 0;
                break;
            case "ITEM":
                itemSelectIndex = 0;
                state = BattleState.ITEMSELECT;
                break;
            case "MERCY":
                boolean canSpare = (enemy.hp <= (enemy.maxHp * 0.3)); 
                
                if (canSpare) {
                    currentBattleDialogue = enemy.getMercyDialogue();
                    nextState = BattleState.RESULT;
                    state = BattleState.DIALOGUE;
                } else {
                    currentBattleDialogue = enemy.getMercyDialogueFailed();
                    nextState = BattleState.DODGING;
                    state = BattleState.DIALOGUE;
                }
                break;
        }
    }

    public void handleActChoice() {
        String response = enemy.getActResponse(actCmdIndex);
        
        currentBattleDialogue = response;
        
        nextState = BattleState.DODGING; 
        state = BattleState.DIALOGUE;
        
    }

    public void useItem(Entity item) {
        
        if (item.healValue > 0) {
            player.hp += item.healValue;
            if (player.hp > player.maxHp) {
                player.hp = player.maxHp;
            }
        }
        
    }

    public void startDodgingPhase() {
        state = BattleState.DODGING;
        dodgeStartTime = System.currentTimeMillis(); 
        bullets.clear();
        selectNextPattern(); 
    }

    public void selectNextPattern() {
        currentPattern = rand.nextInt(7); 
        patternStartTime = System.currentTimeMillis(); 

        switch(currentPattern) {
            case 0: currentPatternDuration = 3000; break; // 0: Aimed Target
            case 1: currentPatternDuration = 3000; break; // 1: Rain
            case 2: currentPatternDuration = 4000; break; // 2: Wall
            case 3: currentPatternDuration = 3000; break; // 3: Crossfire
            case 4: currentPatternDuration = 4000; break; // 4: Spiral
            case 5: currentPatternDuration = 3000; break; // 5: Fountain
            case 6: currentPatternDuration = 5000; break; // 6: Inward Walls
            default: currentPatternDuration = 3000;
        }
        
    }

    public void spawnBullets() {
        switch(currentPattern) {
            case 0:
                spawner.spawnAimed(player, bullets, rand, battleBox);
                break;
            case 1:
                spawner.spawnRain(bullets, rand, battleBox);
                break;
            case 2:
                spawner.spawnWall(bullets, rand, battleBox);
                break;
            case 3:
                spawner.spawnCrossfire(bullets, rand, battleBox);
                break;
            case 4:
                spawner.spawnSpiral(bullets, rand, battleBox);
                break;
            case 5:
                spawner.spawnFountain(bullets, rand, battleBox);
                break;
            case 6:
                spawner.spawnInwardWalls(bullets, rand, battleBox);
                break;
        }
    }
}