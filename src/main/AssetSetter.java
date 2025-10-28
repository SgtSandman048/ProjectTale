package main;

import entity.Janitor;
import entity.Janitor_NPC;
import entity.ProfessorGunner;
import object.OBJ_Book;
import object.OBJ_Knife;
import object.OBJ_Money;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }
    public void setObject(){
        gp.obj[0] = new OBJ_Book(gp);

        gp.obj[1] = new OBJ_Knife(gp);

        gp.obj[2] = new OBJ_Money(gp);
    }
    public void setNPC(){
        gp.npc[0] = new Janitor(gp);
        gp.npc[0].worldX = gp.tileSize*12;
        gp.npc[0].worldY = gp.tileSize*4;

        gp.npc[1] = new ProfessorGunner(gp);
        gp.npc[1].worldX = gp.tileSize*20;
        gp.npc[1].worldY = gp.tileSize*5;

        gp.npc[2] = new Janitor_NPC(gp);
        gp.npc[2].worldX = gp.tileSize*30;
        gp.npc[2].worldY = gp.tileSize*4;
    }
}
