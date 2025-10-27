package main;

import entity.Janitor;
import entity.ProfessorGunner;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }
    public void setObject(){
        
    }
    public void setNPC(){
        gp.npc[0] = new Janitor(gp);
        gp.npc[0].worldX = gp.tileSize*12;
        gp.npc[0].worldY = gp.tileSize*4;

        gp.npc[1] = new ProfessorGunner(gp);
        gp.npc[1].worldX = gp.tileSize*20;
        gp.npc[1].worldY = gp.tileSize*5;
    }
}
