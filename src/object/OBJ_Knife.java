package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Knife extends SuperObject{
    GamePanel gp;
    public OBJ_Knife(GamePanel gp){
        this.gp = gp;

        name = "Knife";
        int attackValue = 1;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/assets/objects/key.png"));
            utool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) { e.printStackTrace(); }
        collision = true;
        solidArea.x = 5;
    }
}
