package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Knife;

public class Player extends Entity{
    KeyHandler keyH;

    public final int screenX; 
    public final int screenY;

    public int npcIndex;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        // Set Collision
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 7;
        speed = 4;
        direction = "down";

        // Player Status
        level = 1;
        maxHp = 20;
        strength = 2;
        exp = 0;
        nextLevelExp = 5;
        currentWeapon = new OBJ_Knife(gp);
        currentShield = null;
        attack = getAttack();
        defense = getDefense();
    }
    public int getAttack(){
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefense(){
        if (currentShield != null) return defense * currentShield.defenseValue;
        return 0;
    }
    public void getPlayerImage(){     // I still haven't draw it out yet :(
        up1 = setup("/player/up_1");
        up2 = setup("/player/up_2");
        down1 = setup("/player/down_1");
        down2 = setup("/player/down_2");
        left1 = setup("/player/left_1");
        left2 = setup("/player/left_2");
        right1 = setup("/player/right_1");
        right2 = setup("/player/right_2");
    }
    public void update(){
        
        if(keyH.upPressed == true || keyH.downPressed == true 
        || keyH.leftPressed == true || keyH.rightPressed == true){
            if(keyH.upPressed == true){
                direction = "up";
            }
            if(keyH.downPressed == true){
                direction = "down";
            }
            if(keyH.leftPressed == true){
                direction = "left";
            }
            if(keyH.rightPressed == true){
                direction = "right";
            }

            // Check Tile Collision
            collsionOn = false;
            gp.cChecker.checkTile(this);
            // Check Object Collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            // Check NPC Collision
            npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            if(collsionOn == false){
                switch(direction){
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
                }
            }

            spriteCounter++;
            if(spriteCounter > 10){
                if(spriteNum == 1){
                    spriteNum = 2;
                }
                else if(spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

    }
    public void pickUpObject(int i){
        if(i != 999){
           
        }
    }

    public void interactNPC(int i){
        if(i != 999){
            if(gp.keyH.interactPressed == true){

                if(gp.npc[i].isHostile){
                    gp.startBattle(gp.npc[i]);
                } else {
                    gp.gameState = gp.dialogueState;
                    gp.npc[i].speak();
                }
                
            }
           
        }
        gp.keyH.interactPressed = false;
    }
    
    public void inCombat(){

    }

    public void draw(Graphics2D g2){
        //g2.setColor(Color.white);
        //g2.fillRect(worldX, worldY, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch(direction){
        case "up":
            if(spriteNum == 1){
                image = up1;
            }
            if(spriteNum == 2){
                image = up2;
            }
            break;
        case "down":
            if(spriteNum == 1){
                image = down1;
            }
            if(spriteNum == 2){
                image = down2;
            }
            break;
        case "left":
            if(spriteNum == 1){
                image = left1;
            }
            if(spriteNum == 2){
                image = left2;
            }
            break;
        case "right":
            if(spriteNum == 1){
                image = right1;
            }
            if(spriteNum == 2){
                image = right2;
            }
            break;
        }
        g2.drawImage(image, screenX, screenY, null);
    }
}
