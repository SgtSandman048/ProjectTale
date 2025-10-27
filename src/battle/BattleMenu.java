package battle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;

public class BattleMenu {
    GamePanel gp;

    List<String> options = new ArrayList<>();
    public int selectedIndex = 0;

    public void addOption(String text) { 
        options.add(text); 
    }
    public void moveUp() { 
        selectedIndex = (selectedIndex + options.size() - 1) % options.size(); 
    }
    public void moveDown() { selectedIndex = (selectedIndex + 1) % options.size(); }
    public String getSelected() { return options.get(selectedIndex); }

    public void draw(Graphics2D g2, int x, int y) {
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
        for (int i = 0; i < options.size(); i++) {
            if (i == selectedIndex) {
                g2.setColor(Color.YELLOW);
                g2.drawString("> " + options.get(i), x, y + i * 30);
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString(options.get(i), x + 20, y + i * 30);
            }
        }
    }
}