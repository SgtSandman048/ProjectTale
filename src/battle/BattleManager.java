package battle;

import entity.Entity;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList; // Import จาก package entity
import java.util.List;
import java.util.Random;
import main.GamePanel;

public class BattleManager {

    public enum BattleState {
        MENU, DIALOGUE, DODGING, RESULT
    }
    public BattleState state = BattleState.MENU;

    GamePanel gp;
    public PlayerHeart player;
    public BattleEnemy enemy;
    public BattleMenu menu;

    public List<Bullet> bullets = new ArrayList<>();
    Random rand = new Random();

    AttackSpawner spawner; // ตัวจัดการการสร้างกระสุน (คลาสใหม่)
    Rectangle battleBox; // กรอบสี่เหลี่ยม (สำหรับส่งให้ Spawner)

    long dodgeStartTime; // เวลารวมของเทิร์น
    int dodgeDuration = 10000; // 10 วินาที (เพิ่มเวลารวม)

    int currentPattern = 0; // รูปแบบการโจมตีปัจจุบัน
    long patternStartTime; // เวลาที่เริ่ม pattern ปัจจุบัน
    int currentPatternDuration; // pattern นี้จะอยู่นานแค่ไหน

    public String currentBattleDialogue = "";
    public BattleState stateAfterDialogue;

    // Input
    boolean enterPressed = false;

    // ขนาดของกรอบต่อสู้
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
        enemy = new BattleEnemy(enemyEntity.name, enemyEntity.maxHp, enemyEntity.); 
        
        player.maxHp = gp.player.maxHp;
        player.hp = player.maxHp; 
        
        player.x = gp.screenWidth / 2;
        player.y = battleBoxY + battleBoxHeight / 2;
        
        state = BattleState.MENU;
        bullets.clear();
        menu.selectedIndex = 0;
        enterPressed = false;

        currentBattleDialogue = enemy.getIntroDialogue(); // ดึง Dialogue ต้อนรับ
        stateAfterDialogue = BattleState.MENU; // บอกว่าหลัง Dialogue จบ ให้ไปที่ MENU
        state = BattleState.DIALOGUE; // เริ่มที่ State DIALOGUE

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
                    state = stateAfterDialogue; // กลับไปที่ State ที่เก็บไว้ (เช่น MENU)
                    enterPressed = false;
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
                    state = BattleState.RESULT;
                }

                if (System.currentTimeMillis() - dodgeStartTime > dodgeDuration) {
                    if (player.hp > 0) {
                        state = BattleState.MENU;
                    }
                }
                break;
            case RESULT:
                if (enterPressed) {
                    gp.gameState = gp.playState;
                    enterPressed = false;
                    gp.inCombat = false;
                }
                break;
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
        }
    }


    private void handleMenuChoice(String choice) {
        switch (choice) {
            case "FIGHT":
                enemy.takeDamage(5);
                if (enemy.hp <= 0) {
                    state = BattleState.RESULT; // ชนะ
                } else {
                    startDodgingPhase(); //
                }
                break;
            case "ACT":
                currentBattleDialogue = enemy.getActDialogue(); // ดึง Dialogue ของ ACT
                stateAfterDialogue = BattleState.MENU; // หลังอ่านจบ กลับไปที่ MENU
                state = BattleState.DIALOGUE; // เปลี่ยน State เป็น DIALOGUE
                
                
                break;
            case "ITEM":
                startDodgingPhase();
                break;
            case "MERCY":
                state = BattleState.RESULT; // สมมติว่าหนี/ชนะ
                break;
        }
    }

    public void startDodgingPhase() {
        state = BattleState.DODGING;
        dodgeStartTime = System.currentTimeMillis(); // << เวลารวมของ Turn
        bullets.clear();
        selectNextPattern(); 
    }

    public void selectNextPattern() {
        currentPattern = rand.nextInt(6); // <<< สุ่ม Pattern 0-3 (เพราะเราจะสร้าง 4 แบบ)
        patternStartTime = System.currentTimeMillis(); // << รีเซ็ตเวลาของ Pattern นี้

        // กำหนดระยะเวลาของแต่ละ Pattern
        switch(currentPattern) {
            case 0: currentPatternDuration = 3000; break; // 0: Aimed (3 วิ)
            case 1: currentPatternDuration = 3000; break; // 1: Rain (3 วิ)
            case 2: currentPatternDuration = 4000; break; // 2: Wall (4 วิ)
            case 3: currentPatternDuration = 3000; break; // 3: Crossfire (3 วิ)
            case 4: currentPatternDuration = 4000; break; // 4: Spiral (4 วิ)
            case 5: currentPatternDuration = 3000; break; // 5: Fountain (3 วิ)
            case 6: currentPatternDuration = 5000; break; // 6: Inward Walls (5 วิ)
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