package battle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class PlayerHeart {
    

    double x, y, dx, dy;
    int size = 16;
    int speed = 4;
    public int maxHp;
    public int hp = maxHp; // ใช้ public เพื่อให้ BattleManager ตั้งค่าได้

    public PlayerHeart(double x, double y) {
        this.x = x; 
        this.y = y;
    }

    public void update(int boxX, int boxY, int boxW, int boxH) {
        x += dx;
        y += dy;
        // จำกัดพื้นที่การเคลื่อนที่
        if (x < boxX) x = boxX;
        if (y < boxY) y = boxY;
        if (x + size > boxX + boxW) x = boxX + boxW - size;
        if (y + size > boxY + boxH) y = boxY + boxH - size;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillRect((int) x, (int) y, size, size);
    }

    public boolean intersects(Bullet b) {
        Rectangle r1 = new Rectangle((int) x, (int) y, size, size);
        Rectangle r2 = new Rectangle((int) b.x, (int) b.y, b.size, b.size);
        return r1.intersects(r2);
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }
}