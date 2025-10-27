package battle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;

public class BattleEnemy {
    GamePanel gp;
    public String name;
    public int hp, maxHp;
    BufferedImage sprite;

    private List<String> introDialogue = new ArrayList<>();
    private List<String> actDialogue = new ArrayList<>();
    // --- จบส่วนเพิ่ม ---

    public BattleEnemy(String name, int hp, BufferedImage sprite, GamePanel gp) {
        this.name = name; this.hp = hp; this.maxHp = hp; this.sprite = sprite; this.gp = gp;

        // --- 2. เพิ่ม: ตั้งค่า Dialogue ตามชื่อศัตรู ---
        // (ผมอ้างอิงชื่อจากไฟล์ Enemy.java และ NPC.java ของคุณ)
        if (name.equals("Professor Kung")) { 
            introDialogue.add("* Professor Kung Appeared");
            actDialogue.add("* You checked Professor Kung");
            actDialogue.add("* ...He's determinating your score");
            actDialogue.add("He look dispappointed on you");
        } 
        else if (name.equals("Janitor")) { 
            introDialogue.add("* Janitor Appeared!");
            actDialogue.add("* You checked Janitor");
            actDialogue.add("* HP: " + hp + " DEF: 0");
            actDialogue.add("* He's so tired");
        }
        else { // ศัตรูเริ่มต้น (ถ้าชื่อไม่ตรง)
            introDialogue.add("* " + name + " Appeared!");
            actDialogue.add("* You checked " + name);
        }
        // --- จบส่วนเพิ่ม ---
    }

    // --- 3. เพิ่มเมธอดใหม่สำหรับดึง Dialogue ---
    public String getIntroDialogue() {
        return String.join("\n", introDialogue); // ส่ง Dialogue ทั้งหมด (ถ้ามีหลายบรรทัด)
    }

    public String getActDialogue() {
        return String.join("\n", actDialogue);
    }

    

    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    public void draw(Graphics2D g2, int x, int y) {
        if (sprite != null) {
                // วาด sprite ที่รับมา (ขยายขนาดให้ดูใหญ่หน่อย)
                int spriteWidth = gp.tileSize * 2; // (96px)
                int spriteHeight = gp.tileSize * 2; // (96px)
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