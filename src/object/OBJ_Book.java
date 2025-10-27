package object;

import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Book extends SuperObject{
    GamePanel gp;
    public OBJ_Book(GamePanel gp){
        this.gp = gp;

        name = "Book";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/assets/objects/knife.png"));
            utool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) { e.printStackTrace(); }
        collision = true;
        solidArea.x = 5;
    }
}
