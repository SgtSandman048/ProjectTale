package main;

import battle.BattleManager.BattleState;
import battle.Bullet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    public Font Haettenschweiler, Stencil;
    //BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public String currentDialogue = "";
    public int cmdNum = 0;
    public int entityIndex = 0;

    public UI(GamePanel gp){
        this.gp = gp;

        // Define Font
        // Stencil Could be great but not with dialogue
        try {
            InputStream is =  getClass().getResourceAsStream("/assets/font/HATTEN.TTF");
            Haettenschweiler = Font.createFont(Font.TRUETYPE_FONT, is);
            
            is = getClass().getResourceAsStream("/assets/font/STENCIL.TTF");
            Stencil = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) { e.printStackTrace();
        } catch (IOException e){ e.printStackTrace(); }

        //OBJ_Key key = new OBJ_Key(gp);
        //keyImage = key.image;
    }
    public void showMessage(String text){
        message = text;
        messageOn = true;
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(Haettenschweiler);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);

        if(gp.gameState == gp.menuState){
            drawMenuScreen();
        }
        if(gp.gameState == gp.playState) {

        }
        if(gp.gameState == gp.pauseState){
            drawPauseScreen();
        }
        if(gp.gameState == gp.dialogueState){
            drawDialogueScreen();
        }
        if(gp.gameState == gp.battleState){
            drawBattleScreen();
        }
    }

    public void drawMenuScreen(){
        g2.setColor(new Color(0,0,0));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        // Title Name
        g2.setFont(Stencil);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,80F));
        String text = "UniTale";
        int x = getXforCenteredText(text);
        int y = gp.tileSize*3;

        g2.setColor(Color.RED);
        g2.drawString(text,x+5,y+5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // Set Font
        g2.setFont(Haettenschweiler);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));

        text = "New Game";
        x = getXforCenteredText(text);
        y = gp.tileSize*7;
        g2.drawString(text, x, y);
        if(cmdNum == 0){
            g2.drawString(">",x=gp.tileSize,y);
        }

        text = "Load Game";
        x = getXforCenteredText(text);
        y = gp.tileSize*8;
        g2.drawString(text, x, y);
        if(cmdNum == 1){
            g2.drawString(">",x=gp.tileSize,y);
        }

        text = "Settings";
        x = getXforCenteredText(text);
        y = gp.tileSize*9;
        g2.drawString(text, x, y);
        if(cmdNum == 2){
            g2.drawString(">",x=gp.tileSize,y);
        }

        text = "Quit Game";
        x = getXforCenteredText(text);
        y = gp.tileSize*10;
        g2.drawString(text, x, y);
        if(cmdNum == 3){
            g2.drawString(">",x=gp.tileSize,y);
        }

    }

    public void drawPauseScreen(){
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 8;
        int x = (gp.screenWidth / 2) - (width / 2);
        int y = (gp.screenHeight / 2) - (height / 2);

        drawSubWindow(x, y, width, height);

        // Text: Paused
        g2.setColor(Color.WHITE);
        g2.setFont(Haettenschweiler);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
        String text = "Paused";
        int textX = getXforCenteredText(text);
        int textY = gp.screenHeight/3;

        g2.drawString(text, textX, textY);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
        // Choice: Continue
        text = "Continue";
        textX = getXforCenteredText(text);
        textY += gp.tileSize * 1.5;
        g2.drawString(text, textX, textY);
        if (cmdNum == 0) {
            g2.drawString(">", textX - (int)(gp.tileSize * 0.7), textY);
        }

        // Choice: Quit
        text = "Quit";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (cmdNum == 1) {
            g2.drawString(">", textX - (int)(gp.tileSize * 0.7), textY); //
        }
        
        // Guideline
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
        text = "Press Space or Enter to Select";
        textX = getXforCenteredText(text);
        textY += gp.tileSize * 2;

        g2.drawString(text, textX, textY);
    }
    public int getXforCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }

    public void drawDialogueScreen(){
        // Window
        int x = gp.tileSize*2;
        int y = gp.tileSize*7;  // Want this to be down instead
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        x += gp.tileSize;
        y += gp.tileSize;

        entityIndex = gp.player.npcIndex;
        g2.drawString(gp.npc[entityIndex].name, x, y);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,24F));
        y += gp.tileSize;
        
        for(String line: currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }
        
    }

    public void drawBattleScreen(){
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x = gp.battleManager.battleBoxX;
        int y = gp.battleManager.battleBoxY;
        int height = gp.battleManager.battleBoxHeight;
        int width = gp.battleManager.battleBoxWidth;

        int playerHp = gp.battleManager.player.hp;
        int playerMaxHp = gp.player.maxHp;

        String enemyName = gp.battleManager.enemy.name;
        int enemyHp = gp.battleManager.enemy.hp;
        int enemyMaxHp = gp.battleManager.enemy.maxHp;

        gp.battleManager.enemy.draw(g2, gp.screenWidth / 2 - 50, 100);

        g2.setColor(Color.WHITE);
        g2.setFont(Haettenschweiler);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20));
        
        g2.drawString(String.format("Your HP %d/%d", playerHp, playerMaxHp), 50, gp.screenHeight - 50);
        g2.drawString(String.format("%s HP: %d/%d", enemyName, enemyHp, enemyMaxHp), gp.screenWidth - 200, gp.screenHeight - 50);

        switch (gp.battleManager.state) {
            case MENU:
                g2.drawRect(x, y, width, height);
                gp.battleManager.menu.draw(g2, 120, 310);
                break;
            case DIALOGUE:
                // 1. วาดกรอบเมนู (พื้นหลัง)
                g2.drawRect(x, y, width, height);
                // 2. วาดเมนู (เพื่อให้เห็นว่าอยู่ข้างใต้)
                if (gp.battleManager.stateAfterDialogue == BattleState.MENU) {
                    gp.battleManager.menu.draw(g2, 120, 310);
                }
                
                // 3. วาดกล่อง Dialogue ทับ โดยเรียกใช้จาก UI.java
                gp.ui.drawSubWindow(x, y, width, height);
                
                // 4. วาดข้อความ (Text) ทับกล่อง
                g2.setColor(Color.WHITE);
                // (พยายามใช้ Font ที่โหลดมาใน UI.java)
                g2.setFont(gp.ui.Haettenschweiler != null ? gp.ui.Haettenschweiler.deriveFont(Font.PLAIN, 28F) : new Font("Arial", Font.PLAIN, 20)); 
                
                int textX = x + 25;
                int textY = y + 40;
                
                for(String line: gp.battleManager.currentBattleDialogue.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 30; // ระยะห่างระหว่างบรรทัด
                }
                break;
            case DODGING:
                g2.setColor(Color.WHITE);
                g2.drawRect(x, y, width, height);
                for (Bullet b : gp.battleManager.bullets) {
                    b.draw(g2);
                }
                gp.battleManager.player.draw(g2);
                break;
            case RESULT:
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
                String text = "YOU WON";
                g2.drawString(text, gp.ui.getXforCenteredText(text), gp.screenHeight / 2);
                g2.setFont(new Font("Arial", Font.PLAIN, 20));
                g2.drawString("Press ENTER to continue", gp.screenWidth / 2 - 100, gp.screenHeight / 2 + 40);
                break;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0,0,0,210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
}
