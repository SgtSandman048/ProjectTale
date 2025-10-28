package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Money extends Entity{
    public OBJ_Money(GamePanel gp){
        super(gp);

        name = "Money";
        down1 = setup("/objects/money");

        isUsableInBattle = true;
        description = "[" + name + "]\nWell yeah.... \nEveryone need it, No doubt";
    }
}
