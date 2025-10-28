package entity;

import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;

public class ProfessorGunner extends Entity{
        public ProfessorGunner(GamePanel gp){
        super(gp);
        
        direction = "down";
        speed = 1;

        // Status
        name = "Professor Gunner";
        maxHp = 100;
        isHostile = true;

        getImage();
        setDialogue();
    }
    public void getImage(){ 
        up1 = setup("/npc/npc");
        up2 = setup("/npc/npc");
        down1 = setup("/npc/npc");
        down2 = setup("/npc/npc");
        left1 = setup("/npc/npc");
        left2 = setup("/npc/npc");
        right1 = setup("/npc/npc");
        right2 = setup("/npc/npc");

        try {
            spriteEnemy = ImageIO.read(getClass().getResourceAsStream("/assets/npc/professor1.png"));
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
