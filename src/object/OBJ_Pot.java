package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Pot extends SuperObject{
    GamePanel gp;
    public OBJ_Pot(GamePanel gp){
        this.gp = gp;

        name = "Null Item";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/assets/objects/knife.png"));
            utool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) { e.printStackTrace(); }
        collision = true;
        solidArea.x = 5;
    }
}
