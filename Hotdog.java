import java.awt.*;
import javax.swing.*;

class Hotdog{
    public static final int BUN = 0, SAUSAGE = 1, COOKEDSAUSAGE = 2, PANCOOKEDSAUSAGE = 3, GRABKETCHUP = 4, KETCHUPBOTTLE = 5, KETCHUPSQUIRT = 6, PLATE = 7, PANS = 8;
    public static final int HASPLATE = 0, HASSAUSAGE = 1, HASKETCHUP = 2;
    public static final int TIME = 0, FALSE = -1, RAW = 1, DONE = 2, STATE = 1, BURNT = 3;
    public static final int HOTDOG = 2, KETCHUPHOTDOG = 3; // used in level classes
    public static final Rectangle trashRect = new Rectangle(43, 472, 70, 60);
    public static final Rectangle plateRect[] = new Rectangle[]{new Rectangle(412, 398, 65, 40), 
                                                                new Rectangle(414, 343, 65, 40),
                                                                new Rectangle(415, 295, 65, 40)};

    public static final Rectangle [] hotdogRects = new Rectangle[]{new Rectangle(418,392, 57,38),
                                                                   new Rectangle(421,336, 57,38),
                                                                   new Rectangle(421,291, 57,38)};
    public static final Rectangle bunbasket = new Rectangle(415, 473, 80, 55);
    public static final Rectangle rawsausagebasket = new Rectangle(722, 520, 70, 39);
    public static final Rectangle[] ketchupbottle = new Rectangle[]{new Rectangle(515, 260, 25, 80),
                                                                    new Rectangle(515, 260, 36, 80),
                                                                    new Rectangle(519, 260, 29, 80),
                                                                    new Rectangle(515, 260, 38, 80)};
    private Image[] imgs, burnedsausage;
    private Image trash;
    private int[] strs; 
    private int [][] costs; // the costs of each component of hotdogs
    private int plates;
    private int loses; // how much money lost from burnt sausages
    private int grabbedtype, grabbedPlate; // to be used in level classes
    private boolean served; // if the grabbed hotdog was served to a customer
    private Rectangle grabbed;
    private boolean [][] table;
    private Point mousePressPoint, mouseReleasePoint, p;
    private boolean mouseHeld, mouseClicked, ketchupgrabbed;
    private boolean [] sausagegrabbed;
    private long[][] sausages;
    private long pause;

    public Hotdog() {
        served = false;
        grabbed = null;
        grabbedtype = -1;
        grabbedPlate = -1;
        loses = 0;
        pause = 0;
        trash = new ImageIcon("trash.png").getImage();
        imgs = new Image[9];
        imgs[6] = new ImageIcon("ketchupsquirt.png").getImage();
        imgs[7] = new ImageIcon("plate.png").getImage();
        burnedsausage = new Image[]{new ImageIcon("burnedsausage0.png").getImage(),
                                    new ImageIcon("burnedsausage1.png").getImage(),
                                    new ImageIcon("burnedsausage2.png").getImage(),
                                    new ImageIcon("burnedsausage3.png").getImage()};
        strs = null;
        plates = 0;
        mouseHeld = false;
        mousePressPoint = null;
        mouseReleasePoint = null;
        p = null;
        mouseClicked = false;
        sausagegrabbed = new boolean[]{false, false, false, false};
        ketchupgrabbed = false;
        table = new boolean[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++) table[i][j] = false;
        }

        sausages = new long[4][2];
        for(int i = 0; i < 4; i++){
            sausages[i][TIME] = FALSE;
            sausages[i][STATE] = RAW;
        }

        costs = new int[20][20];
        costs[KETCHUPSQUIRT][0] = 2;
        costs[KETCHUPSQUIRT][1] = 3;
        costs[KETCHUPSQUIRT][2] = 4;
        costs[KETCHUPSQUIRT][3] = 5;
        
        costs[BUN][0] = 5;
        costs[BUN][1] = 6;
        costs[BUN][2] = 7;
        costs[BUN][3] = 8;
        
        costs[SAUSAGE][0] = 10;
        costs[SAUSAGE][1] = 11;
        costs[SAUSAGE][2] = 12;
        costs[SAUSAGE][3] = 13;
    }


    public void removeHotdog(){served = true;}
    public Rectangle getGrabbedRect(){return grabbed;}
    public int getGrabbedType(){return grabbedtype;}

    public int getCost(){
        int cost = 0;
        if(strs[KETCHUPSQUIRT] != -1) cost += costs[KETCHUPSQUIRT][strs[KETCHUPBOTTLE]];
        cost += costs[SAUSAGE][strs[SAUSAGE]];
        cost += costs[BUN][strs[BUN]];
        return cost;
    }
    
    public int getLoses(){return loses;}

    public void setMousePressPoint(Point point) {
        if(point != null){
            mousePressPoint = point;
            mouseHeld = true;
            mouseClicked = true;
        }
    }
    

    public void setMouse(Point point){
        p = point;
    }

    public void setMouseReleasePoint(Point point){
        mouseReleasePoint = point;
        mouseHeld = false;
    }

    public void setImgs(int[] stars) {
        strs = stars;
        String[] names = {"hotdogbun", "sausage", "cookedsausage", "pancookedsausage", "grabketchup", "ketchupbottle"};

        for (int i = 0; i <= 5; i++) {
            String fileName;
            fileName = names[i] + (stars[i]) + ".png";
            imgs[i] = new ImageIcon(fileName).getImage();
            if (imgs[i] == null) {
                System.err.println("Failed to load: " + fileName);
            }
        }
    }

    private void drawHotdog(Graphics g, int x, int y, int tble){
        if(table[tble][HASPLATE] && grabbedPlate != tble){
            g.drawImage(imgs[PLATE], x, y, null);
            g.drawImage(imgs[BUN], x+28, y+35, null);
            if(table[tble][HASSAUSAGE]){
                g.drawImage(imgs[COOKEDSAUSAGE], x+28, y+35, null);
            }
            if(table[tble][HASKETCHUP]){
                g.drawImage(imgs[KETCHUPSQUIRT], x+28, y+35, null);
            }
        }
    }
    
    public void drawHotdog(Graphics g, int x, int y, boolean hasketchup){
        g.drawImage(imgs[BUN], x, y, null);
        g.drawImage(imgs[COOKEDSAUSAGE], x, y, null);
        if(hasketchup) g.drawImage(imgs[KETCHUPSQUIRT], x, y, null);
    }

    private void drawCooking(Graphics g, int x, int y, int pan){
        if(sausages[pan][TIME]!=FALSE && !sausagegrabbed[pan]){
            long elapsed = System.currentTimeMillis() - sausages[pan][TIME];
            if(sausages[pan][STATE] == RAW){
                g.drawImage(imgs[SAUSAGE], x, y, null);
                if(strs[PANS] == 0 && elapsed >= 10){ // 10000
                    sausages[pan][STATE] = DONE;
                    sausages[pan][TIME] = System.currentTimeMillis();
                }
                if(strs[PANS] == 1 && elapsed >= 9000){
                    sausages[pan][STATE] = DONE;
                    sausages[pan][TIME] = System.currentTimeMillis();
                }
                if(strs[PANS] == 2 && elapsed >= 7000){
                    sausages[pan][STATE] = DONE;
                    sausages[pan][TIME] = System.currentTimeMillis();
                }
                if(strs[PANS] == 3 && elapsed >= 4000){
                    sausages[pan][STATE] = DONE;
                    sausages[pan][TIME] = System.currentTimeMillis();
                }
            }
            else if(sausages[pan][STATE] == DONE){
                g.drawImage(imgs[PANCOOKEDSAUSAGE], x, y, null);
                if(elapsed >= 7000){
                    sausages[pan][STATE] = BURNT;
                }
            }
            else{
                g.drawImage(burnedsausage[strs[SAUSAGE]], x, y, null);
            }
        }
    }

    private int getPlate(Point mouse){
        int plateNum = -1;
        for(int i = 0; i <= 2; i++){
            if(plateRect[i].contains(mouse)){
                plateNum = i;
            }
        }
        return plateNum;
    }

    private void checkSausage(Rectangle sausageRect, int pan, Graphics g){
        if(sausages[pan][STATE] != RAW && mouseHeld && sausageRect.contains(mousePressPoint)){
            sausagegrabbed[pan] = true;
            pause = System.currentTimeMillis();
        }
        if(sausagegrabbed[pan] && sausages[pan][STATE] == DONE) g.drawImage(imgs[COOKEDSAUSAGE], (int) p.getX() - 20, (int) p.getY() - 14, null);
        if(sausagegrabbed[pan] && sausages[pan][STATE] == BURNT) g.drawImage(burnedsausage[strs[SAUSAGE]], (int) p.getX() - 20, (int) p.getY() - 14, null);
        if(!mouseHeld && sausagegrabbed[pan] && sausages[pan][STATE] == DONE){
            int plateNum = getPlate(mouseReleasePoint);
            if(plateNum != -1 && !table[plateNum][HASSAUSAGE]){
                sausages[pan][TIME] = FALSE;
                table[plateNum][HASSAUSAGE] = true;
                sausages[pan][STATE] = RAW;
            }else{
                long elapsed = System.currentTimeMillis() - pause;
                sausages[pan][TIME] -= elapsed;
                sausages[pan][STATE] = DONE;
            }
            sausagegrabbed[pan] = false;
        }
        if(sausages[pan][STATE] == BURNT && sausagegrabbed[pan] && trashRect.contains(p)) g.drawImage(trash, 0, 0, null);
        if(!mouseHeld && sausagegrabbed[pan] && sausages[pan][STATE] == BURNT){
            if(trashRect.contains(p)){
                sausages[pan][TIME] = FALSE;
                sausages[pan][STATE] = RAW;
                loses -= costs[SAUSAGE][strs[SAUSAGE]];
                
            }else{
                long elapsed = System.currentTimeMillis() - pause;
                sausages[pan][TIME] -= elapsed;
                sausages[pan][STATE] = BURNT;
            }
            sausagegrabbed[pan] = false;
        }
    }

    public void draw(Graphics g) {
        if(plates < strs[PLATE] && mousePressPoint != null && bunbasket.contains(mousePressPoint) && mouseClicked){
            table[plates][HASPLATE] = true;
            plates++;
            mouseClicked = false;
        }

        if(mousePressPoint != null && rawsausagebasket.contains(mousePressPoint) && mouseClicked){
            for(int i = 0; i <= strs[PANS]; i++){
                if(sausages[i][TIME] == FALSE){
                    sausages[i][TIME] = System.currentTimeMillis();
                    break;
                }
            }
            mouseClicked = false;
        }

        drawHotdog(g, 387, 357, 0);
        drawHotdog(g,  389, 301, 1);
        drawHotdog(g, 389, 255, 2);

        if(strs[PANS] == 0){
            Rectangle sausageRect = new Rectangle(700, 440, 54, 32);
            checkSausage(sausageRect, 0, g);
            drawCooking(g, 702, 444, 0);
        }

        if(strs[PANS] == 1){
            Rectangle sausageRect [] = new Rectangle[]{new Rectangle(703, 447, 59, 32),
                                                       new Rectangle(675, 390, 59, 32)};
            for(int i = 0; i < 2; i++){
                checkSausage(sausageRect[i], i, g);
            }
            drawCooking(g, 709, 453, 0);
            drawCooking(g, 681, 396, 1);
        }

        if(strs[PANS] == 2){
            Rectangle sausageRect [] = new Rectangle[]{new Rectangle(705, 447, 59, 32),
                                                       new Rectangle(680, 390, 59, 32),
                                                       new Rectangle(660, 337, 59, 32)};
            for(int i = 0; i < 3; i++){
                checkSausage(sausageRect[i], i, g);
            }
            
            drawCooking(g, 708, 450, 0);
            drawCooking(g, 683, 393, 1);
            drawCooking(g, 663, 340, 2);
        }

        if(strs[PANS] == 3){
            Rectangle sausageRect [] = new Rectangle[]{new Rectangle(705, 447, 59, 32),
                                                       new Rectangle(682, 400, 59, 32),
                                                       new Rectangle(658, 352, 59, 32),
                                                       new Rectangle(640, 303, 59, 32)};
            for(int i = 0; i < 4; i++){
                checkSausage(sausageRect[i], i, g);
            }
            drawCooking(g, 712, 449, 0);
            drawCooking(g, 689, 402, 1);
            drawCooking(g, 663, 352, 2);
            drawCooking(g, 642, 303, 3);
        }

        if(strs[KETCHUPBOTTLE] != -1){
            if(!ketchupgrabbed) g.drawImage(imgs[KETCHUPBOTTLE], 493, 260, null); 
            else g.drawImage(imgs[GRABKETCHUP], (int)p.getX() - 45, (int)p.getY() - 65, null);
            
            if(mouseHeld && ketchupbottle[strs[KETCHUPBOTTLE]].contains(mousePressPoint)){
                ketchupgrabbed = true;
            }

            if(ketchupgrabbed && !mouseHeld){
                int plateNum = getPlate(mouseReleasePoint);
                if(plateNum != -1){
                    table[plateNum][HASKETCHUP] = true;
                }
                ketchupgrabbed = false;
            }
        }


        for(int i = 0; i < 3; i++){
            if(table[i][HASPLATE] && table[i][BUN] && table[i][HASSAUSAGE] && mouseHeld && plateRect[i].contains(mousePressPoint)){
                grabbed = hotdogRects[i];
                grabbedPlate = i;
                if(table[i][HASKETCHUP]) grabbedtype = KETCHUPHOTDOG;
                else grabbedtype = HOTDOG;
            }
            if(served){
                grabbed = null;
                grabbedPlate = -1;
                grabbedtype = -1;
                plates --;
                table[grabbedPlate][HASSAUSAGE] = false;
                table[grabbedPlate][HASPLATE] = false;
                table[grabbedPlate][HASKETCHUP] = false;
            }

            if(!mouseHeld){
                grabbed = null;
                grabbedPlate = -1;
                grabbedtype = -1;
            }
        }
    }
}
