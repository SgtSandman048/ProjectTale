package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Book extends Entity{
    public OBJ_Book(GamePanel gp){
        super(gp);

        name = "Book";
        down1 = setup("/objects/book");

        isUsableInBattle = true;
        description = "[" + name + "]\nSomething we don't care about.";
    }
}
