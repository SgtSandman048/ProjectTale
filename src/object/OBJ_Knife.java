package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Knife extends Entity{
    public OBJ_Knife(GamePanel gp){
        super(gp);

        name = "Knife";
        down1 = setup("/objects/knife");

        attackValue = 5;
        isUsableInBattle = false;
        description = "[" + name + "]\n+5 Damage\nDuh, the 'Janitor's gift' I guess?";
    }
}
