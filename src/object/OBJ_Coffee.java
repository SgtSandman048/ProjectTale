package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coffee extends Entity{
    public OBJ_Coffee(GamePanel gp){
        super(gp);

        name = "Coffee";
        down1 = setup("/objects/coffee");

        healValue = 5;

        isUsableInBattle = true;
        description = "[" + name + "]\n+5 HP\nHeal yourself with caffeine.";
    }
}
