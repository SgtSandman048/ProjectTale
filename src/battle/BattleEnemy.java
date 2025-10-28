package battle;

import java.util.Random;

//import battle.BattleManager.BattleState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;

public class BattleEnemy {
    GamePanel gp;
    Random rand = new Random();
    public String name;
    public int hp, maxHp;
    BufferedImage sprite;

    private List<String> introDialogue = new ArrayList<>();

    public List<String> actOptions = new ArrayList<>();
    private List<String> actResponses = new ArrayList<>();

    public List<String> mercyDialogue = new ArrayList<>();
    private List<String> mercyDialogueFailed = new ArrayList<>();

    public BattleEnemy(String name, int hp, BufferedImage sprite, GamePanel gp) {
        this.name = name; this.hp = hp; this.maxHp = hp; this.sprite = sprite; this.gp = gp;

        if (name.equals("Professor Gunner")) { 
            introDialogue.add("*...Professor Gunner Appeared");

            actOptions.add("Check");
            actResponses.add
            ("*...You checked Professor Gunner.\n*...He's determinating your grade.\n*...He feels very happy.");

            actOptions.add("Flatter");
            actResponses.add("*...You said your class very funny.\n*...He didn't response.");
        } 
        else if (name.equals("Janitor")) {
            if(rand.nextInt(2) == 1){
                introDialogue.add
            ("*...Janitor Appeared!\nLet's see what will happen.");
            } else { introDialogue.add
            ("* Janitor Appeared!\nShow me what you got!");}
            

            actOptions.add("Talk");
            actResponses.add
            ("*...You asked for a reason of battle.\nHA HA HA!\nI want to see if you wanna actually die.\nOr just CRYING for a hour and throw it away.");
            
            actOptions.add("Check");
            actResponses.add("*...HP: " + hp + " DEF: 0");

            actOptions.add("Escape");
            actResponses.add
            ("*...You tried to escape but failed.\nSTOP TRYING TO ESCAPE, YOU FOOL!");

            actOptions.add("Hug");
            actResponses.add
            ("*...You hugged janitor.\nHA HA HA!\nAnd in the end, you don't wanna actually die.\nBut you think that gonna be easy, huh?");
            
            mercyDialogue.add
            ("*...You mercied him.\nAt least you showed me something.\nTake this gift.");
            mercyDialogueFailed.add
            ("*...You tried to mercy him.\nLMAO FACE IT, YOU IDIOT!");
            
        }
        else {
            introDialogue.add("* " + name + " Appeared!");

            actOptions.add("Check");
            actResponses.add("* " + name + " - HP: " + hp);
        }
    }

    public String getIntroDialogue() {
        return String.join("\n", introDialogue);
    }

    public String getMercyDialogueFailed() {
        return String.join("\n", mercyDialogueFailed);
    }

    public String getMercyDialogue() {
        return String.join("\n", mercyDialogue);
    }

    public String getActResponse(int index) {
        if (index >= 0 && index < actResponses.size()) {
            return actResponses.get(index);
        }
        return "* ...";
    }

    
    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    public void draw(Graphics2D g2, int x, int y) {
        if (sprite != null) {
                int spriteWidth = gp.tileSize * 3; // (96px)
                int spriteHeight = gp.tileSize * 3; // (96px)
                g2.drawImage(sprite, x, y, spriteWidth, spriteHeight, null);
            }
        else{
            g2.setColor(Color.WHITE);
            g2.fillOval(x, y, 100, 100);
            g2.setColor(Color.BLACK);
            g2.fillOval(x + 20, y + 30, 15, 15);
            g2.fillOval(x + 65, y + 30, 15, 15);
            g2.drawArc(x + 25, y + 50, 50, 30, 0, -180);
        }
    }
}