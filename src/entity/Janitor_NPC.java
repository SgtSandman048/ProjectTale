package entity;

import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Janitor_NPC extends Entity {

    public Janitor_NPC(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 1;

        name = "Janitor";
        maxHp = 20;
        isHostile = false;

        getImage();
        setDialogue();
    }
    public void getImage(){     // I still haven't draw it out yet :(
        up1 = setup("/npc/janitor1");
        up2 = setup("/npc/janitor1");
        down1 = setup("/npc/janitor1");
        down2 = setup("/npc/janitor1");
        left1 = setup("/npc/janitor1");
        left2 = setup("/npc/janitor1");
        right1 = setup("/npc/janitor1");
        right2 = setup("/npc/janitor1");

        try {
            spriteEnemy = ImageIO.read(getClass().getResourceAsStream("/assets/npc/janitor2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDialogue(){
        dialogue[0] = "Hello, Lad.";
        dialogue[1] = "Welcome to arena of valor!";
        dialogue[2] = "You should go back to sleep right now \nbefore your body can't take that anymore";
    }

    public void setAction(){
        actionLockCounter++;
        if(actionLockCounter == 120){
            Random random = new Random();
            int i = random.nextInt(100)+1;

            if(i <= 25){
                direction = "up";
            }
            if(i > 25 && i <= 50){
                direction = "down";
            }
            if(i > 50 && i <= 75){
                direction = "left";
            }
            if(i > 75 && i <= 100){
                direction = "right";
            }

            actionLockCounter = 0;
        }

    }
    public void speak(){
        super.speak();
    }
}
