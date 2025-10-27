package battle;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

public class AttackSpawner {
    private double spiralAngle = 0;
    private int wallSpawnCounter = 0;
    private int interval = 0;

    public void resetPatternState() {
        spiralAngle = 0;
        wallSpawnCounter = 0;
    }

    public void spawnAimed(PlayerHeart player, List<Bullet> bullets, Random rand, Rectangle box) {
        if (rand.nextInt(15) == 0) { // ประมาณ 0.25 วิ (ที่ 60fps)
            double dx = player.x - (box.x + box.width / 2);
            double dy = player.y - box.y; // เริ่มจากกลางบนของกล่อง
            double len = Math.sqrt(dx * dx + dy * dy);
            double vx = dx / len * 3; // ความเร็ว 3
            double vy = dy / len * 3;
            bullets.add(new Bullet(box.x + box.width / 2, box.y, vx, vy, 8, 1, Color.MAGENTA));
        }
    }

    public void spawnRain(List<Bullet> bullets, Random rand, Rectangle box) {
        if (rand.nextInt(10) < 3) { // 30%
            int x = box.x + rand.nextInt(box.width); // สุ่มตำแหน่ง X ในกรอบ
            bullets.add(new Bullet(x, box.y, 0, 2 + rand.nextDouble() * 2, 6, 1, Color.YELLOW));
        }
    }

    public void spawnWall(List<Bullet> bullets, Random rand, Rectangle box) {
        if (interval == 30) { // ประมาณ 0.5 วิ (ที่ 60fps)
            int gapY = rand.nextInt(box.height - 40) + box.y + 30; // สุ่มตำแหน่งช่องว่าง
            int gapSize = 40; // ขนาดช่องว่าง 30px
            
            for (int y = box.y; y < box.y + box.height; y += 10) { // สร้างกระสุนทุก 10px
                if (y < gapY || y > gapY + gapSize) { 
                    bullets.add(new Bullet(box.x, y, 3, 0, 10, 1, Color.CYAN)); // ยิงจากซ้ายไปขวา
                }
            }
            interval = 0;
        }
        else{ interval++; }
    }

    public void spawnCrossfire(List<Bullet> bullets, Random rand, Rectangle box) {
        if (rand.nextInt(5) == 0) {
            // จากมุมบนซ้าย
            bullets.add(new Bullet(box.x, box.y, 2, 1.5, 14, 1, Color.ORANGE));
            // จากมุมบนขวา
            bullets.add(new Bullet(box.x + box.width - 7, box.y, -2, 1.5, 14, 1, Color.ORANGE));
        }
    }

    public void spawnSpiral(List<Bullet> bullets, Random rand, Rectangle box) {
        // อัปเดตมุม (เก็บค่าไว้ใน spiralAngle)
        spiralAngle += 0.25; // ความเร็วในการหมุน

        // ยิงถี่ๆ
        if (rand.nextInt(2) == 0) {
            double spawnX = box.x + box.width / 2;
            double spawnY = box.y + box.height / 2;

            // คำนวณความเร็ว (ทิศทาง)
            double vx = Math.cos(spiralAngle) * 2;
            double vy = Math.sin(spiralAngle) * 2;

            // สร้าง 2 เส้น วิ่งสวนทางกัน
            bullets.add(new Bullet(spawnX, spawnY, vx, vy, 7, 1, Color.GREEN));
            bullets.add(new Bullet(spawnX, spawnY, -vx, -vy, 7, 1, Color.GREEN));
        }
    }

    public void spawnFountain(List<Bullet> bullets, Random rand, Rectangle box) {
        if (rand.nextInt(5) == 0) { // ความถี่ในการยิง
            double spawnX = box.x + box.width / 2 + (rand.nextDouble() - 0.5) * 20; // สุ่มเล็กน้อยรอบๆ กลางล่าง
            double spawnY = box.y + box.height - 10; // ฐานของกล่อง

            double vx = (rand.nextDouble() - 0.5) * 3; // สเปรย์ออกซ้าย/ขวา
            double vy = -rand.nextDouble() * 2 - 2; // พุ่งขึ้น (ความเร็วสุ่ม)

            bullets.add(new Bullet(spawnX, spawnY, vx, vy, 8, 1, new Color(100, 100, 255))); // สีน้ำเงิน
        }
    }
    public void spawnInwardWalls(List<Bullet> bullets, Random rand, Rectangle box) {
        wallSpawnCounter++; // นับเฟรม

        // ยิงกำแพงใหม่ทุก 0.75 วินาที
        if (wallSpawnCounter % 45 == 0) { 
            int side = (wallSpawnCounter / 45) % 4; // 0=ซ้าย, 1=ขวา, 2=บน, 3=ล่าง
            int gapSize = 35; // ขนาดช่องว่าง
            
            if (side == 0) { // --- ผนังซ้าย (ไปขวา) ---
                int gapY = box.y + rand.nextInt(box.height - gapSize);
                for (int y = box.y; y < box.y + box.height; y += 10) {
                    if (y < gapY || y > gapY + gapSize) {
                        bullets.add(new Bullet(box.x, y, 1.5, 0, 10, 1, Color.WHITE));
                    }
                }
            } else if (side == 1) { // --- ผนังขวา (ไปซ้าย) ---
                int gapY = box.y + rand.nextInt(box.height - gapSize);
                for (int y = box.y; y < box.y + box.height; y += 10) {
                    if (y < gapY || y > gapY + gapSize) {
                        bullets.add(new Bullet(box.x + box.width - 10, y, -1.5, 0, 10, 1, Color.WHITE));
                    }
                }
            } else if (side == 2) { // --- ผนังบน (ไปล่าง) ---
                int gapX = box.x + rand.nextInt(box.width - gapSize);
                for (int x = box.x; x < box.x + box.width; x += 10) {
                    if (x < gapX || x > gapX + gapSize) {
                        bullets.add(new Bullet(x, box.y, 0, 1.5, 10, 1, Color.WHITE));
                    }
                }
            } else { // --- ผนังล่าง (ไปบน) ---
                int gapX = box.x + rand.nextInt(box.width - gapSize);
                for (int x = box.x; x < box.x + box.width; x += 10) {
                    if (x < gapX || x > gapX + gapSize) {
                        bullets.add(new Bullet(x, box.y + box.height - 10, 0, -1.5, 10, 1, Color.WHITE));
                    }
                }
            }
        }
    }
}
