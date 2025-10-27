package battle;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet {
    double x, y, vx, vy;
    int size;
    int damage;
    Color color;

    public Bullet(double x, double y, double vx, double vy, int size, int dmg, Color c) {
        this.x = x; this.y = y; this.vx = vx; this.vy = vy;
        this.size = size; this.damage = dmg; this.color = c;
    }

    public void update() { x += vx; y += vy; }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect((int) x, (int) y, size, size);
    }

    public boolean isOffScreen(int w, int h) {
        return (x + size < 0 || x > w || y + size < 0 || y > h);
    }
}