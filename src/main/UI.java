package main;

import battle.BattleManager.BattleState;
import battle.Bullet;
import entity.Entity;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    public int slotCol = 0;
    public int slotRow = 0;

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
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory();
        }
        if(gp.gameState == gp.optionsState){
            drawOptionsScreen();
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

        text = "Control";
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

    public int getXforAlignToRightText(String text, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }

    public void drawDialogueScreen(){
        // Window
        int x = gp.tileSize*2;
        int y = gp.tileSize*7;
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

    public void drawCharacterScreen(){
        final int frameX = gp.tileSize*2;
        final int frameY = gp.tileSize*2;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text
        g2.setColor(Color.white);
        g2.setFont(Haettenschweiler);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,24F));

        int textX = frameX + 30;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 32;

        // Value
        int tail = (frameX + frameWidth) - 30;
        String value;
        int valueTextX;

        // Name
        g2.drawString("Level", textX, textY);
        value = String.valueOf(gp.player.level);
        valueTextX = getXforAlignToRightText(value, tail);
        g2.drawString(value, valueTextX, textY);
        textY += lineHeight;

        g2.drawString("HP", textX, textY);
        value = String.valueOf(gp.player.maxHp);
        valueTextX = getXforAlignToRightText(value, tail);
        g2.drawString(value, valueTextX, textY);
        textY += lineHeight;

        g2.drawString("Strength", textX, textY);
        value = String.valueOf(gp.player.strength);
        valueTextX = getXforAlignToRightText(value, tail);
        g2.drawString(value, valueTextX, textY);
        textY += lineHeight;

        g2.drawString("Attack", textX, textY);
        value = String.valueOf(gp.player.attack);
        valueTextX = getXforAlignToRightText(value, tail);
        g2.drawString(value, valueTextX, textY);
        textY += lineHeight;

        g2.drawString("EXP", textX, textY);
        value = String.valueOf(gp.player.exp+"/"+gp.player.nextLevelExp);
        valueTextX = getXforAlignToRightText(value, tail);
        g2.drawString(value, valueTextX, textY);
        textY += lineHeight;

        g2.drawString("Equipped", textX, textY);
        value = String.valueOf(gp.player.currentWeapon.name);
        valueTextX = getXforAlignToRightText(value, tail);
        g2.drawString(value, valueTextX, textY);
        textY += lineHeight;
    }
    public void drawInventory(){
        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize*2;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize*4;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        int textX = frameX + 30;
        int textY = frameY + gp.tileSize;
        g2.drawString("Item", textX, textY);

        final int slotXstart = frameX + 20;
        final int slotYstart = textY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize*1;

        for(int i=0; i<gp.player.inventory.size(); i++){
            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += gp.tileSize;
            if(i==4 || i==9){
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        int cursorX = slotXstart + (gp.tileSize * slotCol);
        int cursorY = slotYstart + (gp.tileSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // Description
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize*3;
        drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

        textX = dFrameX + 20;
        textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(20F));

        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size()){
            for(String line: gp.player.inventory.get(itemIndex).description.split("\n")){
                g2.drawString(line, textX, textY);
                textY += 24;
            }
        }
    }

    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + (slotRow*5);
        return  itemIndex;
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
                g2.drawRect(x, y, width, height);
                if (gp.battleManager.nextState == BattleState.MENU) {
                    gp.battleManager.menu.draw(g2, 120, 310);
                }
                
                gp.ui.drawSubWindow(x, y, width, height);
                
                g2.setColor(Color.WHITE);
                g2.setFont(Haettenschweiler != null ? Haettenschweiler.deriveFont(Font.PLAIN, 28F) : new Font("Arial", Font.PLAIN, 20)); 
                
                int textX = x + 25;
                int textY = y + 40;
                
                for(String line: gp.battleManager.currentBattleDialogue.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 30;
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

                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20));
                text = "+ 350 XP";
                g2.drawString(text, gp.ui.getXforCenteredText(text), gp.screenHeight / 2 + gp.tileSize / 2);
                text = "Press ENTER to continue";
                g2.drawString(text, gp.ui.getXforCenteredText(text), gp.screenHeight / 2+ gp.tileSize * 2);
                break;
            case GAMEOVER:
                drawGameOverScreen();
                break;
            case ITEMSELECT:
                drawItemMenu(g2);
                break;
            case ACTSELECT:
                g2.drawRect(gp.battleManager.battleBoxX, gp.battleManager.battleBoxY, 
                            gp.battleManager.battleBoxWidth, gp.battleManager.battleBoxHeight);
                drawActMenu(g2);
                break;
        }
    }
    
    public void drawItemMenu(Graphics2D g2) {
        ArrayList<Entity> inventory = gp.player.inventory;
        
        int x = gp.battleManager.battleBoxX + 25;
        int y = gp.battleManager.battleBoxY + 35;
        int spacing = 30; // ระยะห่างระหว่างบรรทัด

        g2.setFont(Haettenschweiler != null ? Haettenschweiler.deriveFont(Font.PLAIN, 24F) : new Font("Arial", Font.PLAIN, 24));
        
        if (inventory.isEmpty()) {
            g2.setColor(Color.WHITE);
            g2.drawString("* Empty", x, y);
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                Entity item = inventory.get(i);
                
                // --- ตั้งค่าสี ---
                if (i == gp.battleManager.itemSelectIndex) {
                    g2.setColor(Color.YELLOW); // สีเหลืองถ้าถูกเลือก
                    g2.drawString("> " + item.name, x, y + (i * spacing));
                } else {
                    g2.setColor(Color.WHITE);
                    g2.drawString(item.name, x + 20, y + (i * spacing));
                }
                
                // (ทางเลือก) ถ้าใช้ไม่ได้ ให้วาดทับด้วยสีเทา
                if (!item.isUsableInBattle) {
                    Color dimGray = new Color(100, 100, 100); // สีเทา
                    if (i == gp.battleManager.itemSelectIndex) {
                         g2.setColor(dimGray.brighter()); // สีเทาเหลือง (ถ้าเลือก)
                         g2.drawString("> " + item.name, x, y + (i * spacing));
                    } else {
                        g2.setColor(dimGray);
                        g2.drawString(item.name, x + 20, y + (i * spacing));
                    }
                }
            }
            
            // (ทางเลือก) วาดคำอธิบายไอเท็มที่มุมขวา
            Entity selectedItem = inventory.get(gp.battleManager.itemSelectIndex);
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
            int descX = gp.battleManager.battleBoxX + (gp.battleManager.battleBoxWidth / 2);
            int descY = gp.battleManager.battleBoxY + 40;
            if (selectedItem.description != null) {
                for(String line: selectedItem.description.split("\n")) {
                    g2.drawString(line, descX, descY);
                    descY += 25;
                }
            }
        }
    }
    
    public void drawActMenu(Graphics2D g2) {
        int x = gp.battleManager.battleBoxX + 25;
        int y = gp.battleManager.battleBoxY + 35;
        int spacing = 30;

        g2.setFont(Haettenschweiler != null ? Haettenschweiler.deriveFont(Font.PLAIN, 24F) : new Font("Arial", Font.PLAIN, 24));
        
        List<String> options = gp.battleManager.enemy.actOptions;
        
        for (int i = 0; i < options.size(); i++) {
            if (i == gp.battleManager.actCmdIndex) {
                g2.setColor(Color.YELLOW);
                g2.drawString("> * " + options.get(i), x, y + (i * spacing));
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString("* " + options.get(i), x + 20, y + (i * spacing));
            }
        }
    }

    public void drawOptionsScreen(){
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(32F));
        System.out.print("Test");

        int frameX = gp.tileSize*6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*8;
        int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
    }

    public void drawGameOverScreen() {
        g2.setColor(new Color(0, 0, 0)); 
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(Haettenschweiler != null ? Haettenschweiler.deriveFont(Font.BOLD, 100F) : new Font("Arial", Font.BOLD, 100));
        g2.setColor(Color.RED);
        String text = "GAME OVER";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2 - gp.tileSize;
        g2.drawString(text, x, y);

        g2.setFont(Haettenschweiler != null ? Haettenschweiler.deriveFont(Font.PLAIN, 40F) : new Font("Arial", Font.PLAIN, 40));
        g2.setColor(Color.WHITE);

        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.tileSize * 3;
        g2.drawString(text, x, y);
        if (cmdNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Quit to Menu";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (cmdNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
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
