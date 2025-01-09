import java.awt.*;
import javax.swing.*;

class Hamburger{
    public static final int BURGERTOP = 0, BURGERBOTTOM = 1, GRABTOMATO = 2, GRABLETTUCE = 3, TOMATO = 4;
    public static final int LETTUCE = 5, PATTY = 6, COOKEDPATTY = 7, PLATE = 8, PANS = 9, BUN = 10;
    public static final int HASPLATE = 0, HASPATTY = 1, HASLETTUCE = 2, HASTOMATO = 3;
    public static final int TIME = 0, FALSE = -1, RAW = 1, DONE = 2, STATE = 1, BURNT = 3;

    public static final int BURGER = 0, LETTUCEBURGER = 4, TOMATOBURGER = 5, LETTUCETOMATOBURGER = 6;
    public static final Rectangle trashRect = new Rectangle(43, 472, 70, 60);
    public static final Rectangle plateRect[] = new Rectangle[]{new Rectangle(307, 396, 65, 40), 
                                                                new Rectangle(322, 341, 65, 40),
                                                                new Rectangle(330, 295, 65, 40)};
    public static final Rectangle bunbasket = new Rectangle(300, 470, 80, 55);
    public static final Rectangle rawpattybasket = new Rectangle(632, 526, 70, 39);
    public static final Rectangle[] tomatobasket = new Rectangle[]{new Rectangle(522, 425, 57, 68),
                                                                   new Rectangle(519, 430, 65, 55),
                                                                   new Rectangle(516, 425, 70, 65),
                                                                   new Rectangle(516, 425, 70, 65)};
    public static final Rectangle[] lettucebasket = new Rectangle[]{new Rectangle(505, 355, 57, 55),
                                                                    new Rectangle(505, 357, 62, 51),
                                                                    new Rectangle(505, 357, 65, 55),
                                                                    new Rectangle(505, 355, 63, 58)};
    private Image[] imgs, burnedpatty;
    private Image trash;
    private int[] strs;
    private int plates, grabbedtype, grabbedPlate, skip;
    private boolean [][] table;
    private Point mousePressPoint, mouseReleasePoint, p;
    private boolean mouseHeld, mouseClicked, lettucegrabbed, tomatograbbed, served;
    private boolean [] pattygrabbed;
    private long[][] patties;
    private long pause;
    private Rectangle grabbed;

    public Hamburger() {
        skip = 0;
        served = false;
        grabbedPlate = -1;
        grabbed = null;
        grabbedtype = -1;
        pause = 0;
        imgs = new Image[9];
        imgs[8] = new ImageIcon("plate.png").getImage();
        trash = new ImageIcon("trash.png").getImage();
        burnedpatty = new Image[]{new ImageIcon("burntpatty.png").getImage(),
                                  new ImageIcon("burntpatty2.png").getImage(),
                                  new ImageIcon("burntpatty.png").getImage(),
                                  new ImageIcon("burntpatty.png").getImage()};
        strs = null;
        plates = 0;
        mouseHeld = false;
        mousePressPoint = null;
        mouseReleasePoint = null;
        p = null;
        mouseClicked = false;
        pattygrabbed = new boolean[]{false, false, false, false};
        lettucegrabbed = false;
        tomatograbbed = false;
        table = new boolean[3][4];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++) table[i][j] = false;
        }

        patties = new long[4][2];
        for(int i = 0; i < 4; i++){
            patties[i][TIME] = FALSE;
            patties[i][RAW] = RAW;
        }
    }

    public int getCost(boolean t, boolean l){
        int [][]costs = new int[20][20];
        costs[LETTUCE][0] = 2;
        costs[LETTUCE][1] = 3;
        costs[LETTUCE][2] = 4;
        costs[LETTUCE][3] = 5;

        costs[TOMATO][0] = 2;
        costs[TOMATO][1] = 3;
        costs[TOMATO][2] = 4;
        costs[TOMATO][3] = 5;
        
        costs[BUN][0] = 5;
        costs[BUN][1] = 6;
        costs[BUN][2] = 7;
        costs[BUN][3] = 8;
        
        costs[PATTY][0] = 10;
        costs[PATTY][1] = 11;
        costs[PATTY][2] = 12;
        costs[PATTY][3] = 13;
        int cost = 0;
        if(l) cost += costs[LETTUCE][strs[LETTUCE]];
        if(t) cost += costs[TOMATO][strs[TOMATO]];
        cost += costs[PATTY][strs[PATTY]];
        cost += costs[BUN][strs[BURGERTOP]];
        return cost;
    }


    public void removeBurger(){
        served = true;
    }
    public Rectangle getGrabbedRect(){return grabbed;}
    public int getGrabbedType(){return grabbedtype;}
    public void setMousePressPoint(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null");
        }
        mousePressPoint = point;
        mouseHeld = true;
        mouseClicked = true;
    }
    

    public void setMouse(Point point){
        p = point;
    }

    public void setMouseReleasePoint(Point point){
        mouseReleasePoint = point;
        mouseHeld = false;
    }

    public void setImgs(int[] stars) {
        System.out.print("HAMBURGER");
        for(int star : stars){
            System.out.print(star + " ");
        }
        System.out.println();
        strs = stars;
        String[] names = { "burgertop", "burgerbottom", "grabtomato", "grablettuce", "burgertomato", "burgerlettuce",
                "patty", "cookedpatty" };

        for (int i = 0; i <= 7; i++) {
            String fileName;
            fileName = names[i] + (stars[i]) + ".png";
            imgs[i] = new ImageIcon(fileName).getImage();
            if (imgs[i] == null) {
                System.err.println("Failed to load: " + fileName);
            }
        }
    }

    private void drawBurger(Graphics g, int x, int y, int tble){
        if(grabbedPlate != tble && table[tble][HASPLATE]){
            g.drawImage(imgs[PLATE], x, y, null);
            if(!table[tble][HASLETTUCE] && !table[tble][HASTOMATO] && !table[tble][HASPATTY]){
                if(strs[BURGERTOP] == 0){
                    g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+54, null);
                }
                if(strs[BURGERTOP] == 1 || strs[BURGERTOP] == 2){
                    g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+53, null);
                }
                if(strs[BURGERTOP] == 3){
                    g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+51, null);
                }
            }
            else if(table[tble][HASPATTY]){
                if(!table[tble][HASLETTUCE] && !table[tble][HASTOMATO]){
                    if(strs[BURGERTOP] == 0){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    }
                    if(strs[BURGERTOP] == 1){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+57, null);
                    }
                    if(strs[BURGERTOP] == 2){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    }
                    if(strs[BURGERTOP] == 3){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+41, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+56, null);
                    }
                }

                else if(table[tble][HASLETTUCE] && !table[tble][HASTOMATO]){
                    if(strs[BURGERTOP] == 0 || strs[BURGERTOP] == 2){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[LETTUCE], x+32, y+45, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+31, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    }
                    if(strs[BURGERTOP] == 1){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[LETTUCE], x+33, y+42, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+28, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+57, null);
                    }
                    if(strs[BURGERTOP] == 3){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+40, null);
                        g.drawImage(imgs[LETTUCE], x+33, y+35, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+31, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+56, null);
                    }
                }

                else if(!table[tble][HASLETTUCE] && table[tble][HASTOMATO]){
                    if(strs[BURGERTOP] == 0 || strs[BURGERTOP] == 2){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[TOMATO], x+35, y+46, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+30, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    }
                    if(strs[BURGERTOP] == 1){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[TOMATO], x+35, y+43, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+29, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+57, null);
                    }
                    if(strs[BURGERTOP] == 3){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[TOMATO], x+35, y+45, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+31, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+57, null);
                    }
                }

                else{
                    if(strs[BURGERTOP] == 0 || strs[BURGERTOP] == 2){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[LETTUCE], x+32, y+45, null);
                        g.drawImage(imgs[TOMATO], x+35, y+42, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+26, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    }
                    if(strs[BURGERTOP] == 1){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[LETTUCE], x+33, y+42, null);
                        g.drawImage(imgs[TOMATO], x+35, y+39, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+25, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+57, null);
                    }
                    if(strs[BURGERTOP] == 3){
                        g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                        g.drawImage(imgs[LETTUCE], x+33, y+39, null);
                        g.drawImage(imgs[TOMATO], x+35, y+41, null);
                        g.drawImage(imgs[BURGERTOP], x+37, y+27, null);
                        g.drawImage(imgs[BURGERBOTTOM], x+37, y+57, null);
                    }
                }

            }

            if(table[tble][HASTOMATO] && !table[tble][HASLETTUCE] && !table[tble][HASPATTY]){
                if(strs[BURGERTOP] == 0 || strs[BURGERTOP] == 2){
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    g.drawImage(imgs[TOMATO], x+35, y+53, null);
                    g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                }
                if(strs[BURGERTOP] == 1){
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    g.drawImage(imgs[TOMATO], x+35, y+51, null);
                    g.drawImage(imgs[BURGERTOP], x+37, y+37, null);
                }
                if(strs[BURGERTOP] == 3){
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    g.drawImage(imgs[TOMATO], x+35, y+51, null);
                    g.drawImage(imgs[BURGERTOP], x+37, y+36, null);
                }
            }

            if(!table[tble][HASTOMATO] && table[tble][HASLETTUCE] && !table[tble][HASPATTY]){
                if(strs[BURGERTOP] == 0 || strs[BURGERTOP] == 2){
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    g.drawImage(imgs[LETTUCE], x+32, y+53, null);
                    g.drawImage(imgs[BURGERTOP], x+37, y+37, null);
                }
                if(strs[BURGERTOP] == 1){
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    g.drawImage(imgs[LETTUCE], x+35, y+51, null);
                    g.drawImage(imgs[BURGERTOP], x+37, y+37, null);
                }
                if(strs[BURGERTOP] == 3){
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    g.drawImage(imgs[LETTUCE], x+33, y+43, null);
                    g.drawImage(imgs[BURGERTOP], x+37, y+36, null);
                }
            }

            if(table[tble][HASTOMATO] && table[tble][HASLETTUCE] && !table[tble][HASPATTY]){
                if(strs[BURGERTOP] == 0 || strs[BURGERTOP] == 2){
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    g.drawImage(imgs[LETTUCE], x+33, y+52, null);
                    g.drawImage(imgs[TOMATO], x+35, y+47, null);
                    g.drawImage(imgs[BURGERTOP], x+37, y+31, null);
                }
                if(strs[BURGERTOP] == 1){
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
                    g.drawImage(imgs[LETTUCE], x+33, y+52, null);
                    g.drawImage(imgs[TOMATO], x+35, y+47, null);
                    g.drawImage(imgs[BURGERTOP], x+37, y+33, null);
                }
                if(strs[BURGERTOP] == 3){
                    g.drawImage(imgs[LETTUCE], x+33, y+42, null);
                    g.drawImage(imgs[TOMATO], x+35, y+44, null);
                    g.drawImage(imgs[BURGERTOP], x+37, y+30, null);
                    g.drawImage(imgs[BURGERBOTTOM], x+37, y+57, null);
                }
            }
        }
    }


    

    public void drawBurger(Graphics g, int x, int y, boolean lettuce, boolean tomato){
        if(!lettuce && !tomato){
            if(strs[BURGERTOP] == 0){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
            }
            if(strs[BURGERTOP] == 1){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+57, null);
            }
            if(strs[BURGERTOP] == 2){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+58, null);
            }
            if(strs[BURGERTOP] == 3){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+41, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+35, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+56, null);
            }
        }

        if(lettuce && !tomato){
            if(strs[BURGERTOP] == 0 || strs[BURGERTOP] == 2){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43+4, null);
                g.drawImage(imgs[LETTUCE], x+32, y+45+4, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+31+4, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+58+4, null);
            }
            if(strs[BURGERTOP] == 1){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43+7, null);
                g.drawImage(imgs[LETTUCE], x+33, y+42+7, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+28+7, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+57+7, null);
            }
            if(strs[BURGERTOP] == 3){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+40+4, null);
                g.drawImage(imgs[LETTUCE], x+33, y+35+4, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+31+4, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+56+4, null);
            }
        }


        if(!lettuce && tomato){
            if(strs[BURGERTOP] == 0 || strs[BURGERTOP] == 2){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43+5, null);
                g.drawImage(imgs[TOMATO], x+35, y+46+5, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+30+5, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+58+5, null);
            }
            if(strs[BURGERTOP] == 1){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43+6, null);
                g.drawImage(imgs[TOMATO], x+35, y+43+6, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+29+6, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+57+6, null);
            }
            if(strs[BURGERTOP] == 3){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43+4, null);
                g.drawImage(imgs[TOMATO], x+35, y+45+4, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+31+4, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+57+4, null);
            }
        }

        
        if(lettuce && tomato){
            if(strs[BURGERTOP] == 0 || strs[BURGERTOP] == 2){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43+9, null);
                g.drawImage(imgs[LETTUCE], x+32, y+45+9, null);
                g.drawImage(imgs[TOMATO], x+35, y+42+9, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+26+9, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+58+9, null);
            }
            if(strs[BURGERTOP] == 1){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43+10, null);
                g.drawImage(imgs[LETTUCE], x+33, y+42+10, null);
                g.drawImage(imgs[TOMATO], x+35, y+39+10, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+25+10, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+57+10, null);
            }
            if(strs[BURGERTOP] == 3){
                g.drawImage(imgs[COOKEDPATTY], x+37, y+43+8, null);
                g.drawImage(imgs[LETTUCE], x+33, y+39+8, null);
                g.drawImage(imgs[TOMATO], x+35, y+41+8, null);
                g.drawImage(imgs[BURGERTOP], x+37, y+27+8, null);
                g.drawImage(imgs[BURGERBOTTOM], x+37, y+57+8, null);
            }
        }
    }


    private void drawCooking(Graphics g, int x, int y, int pan){
        if(patties[pan][TIME]!=FALSE && !pattygrabbed[pan]){
            long elapsed = System.currentTimeMillis() - patties[pan][TIME];
            if(patties[pan][STATE] == RAW){
                g.drawImage(imgs[PATTY], x, y, null);
                if(strs[PANS] == 0 && elapsed >= 10000){
                    patties[pan][STATE] = DONE;
                    patties[pan][TIME] = System.currentTimeMillis();
                }
                if(strs[PANS] == 1 && elapsed >= 9000){
                    patties[pan][STATE] = DONE;
                    patties[pan][TIME] = System.currentTimeMillis();
                }
                if(strs[PANS] == 2 && elapsed >= 7000){
                    patties[pan][STATE] = DONE;
                    patties[pan][TIME] = System.currentTimeMillis();
                }
                if(strs[PANS] == 3 && elapsed >= 10){//change to 4000
                    patties[pan][STATE] = DONE;
                    patties[pan][TIME] = System.currentTimeMillis();
                }
            }
            else if(patties[pan][STATE] == DONE){
                g.drawImage(imgs[COOKEDPATTY], x, y, null);
                if(elapsed >= 7000){
                    patties[pan][STATE] = BURNT;
                }
            }
            else{
                g.drawImage(burnedpatty[strs[PATTY]], x, y, null);
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

    private void checkPatty(Rectangle sausageRect, int pan, Graphics g){
        if(patties[pan][STATE] != RAW && mouseHeld && sausageRect.contains(mousePressPoint)){
            pattygrabbed[pan] = true;
            pause = System.currentTimeMillis();
        }
        if(pattygrabbed[pan] && patties[pan][STATE] == DONE) g.drawImage(imgs[COOKEDPATTY], (int) p.getX() - 20, (int) p.getY() - 14, null);
        if(pattygrabbed[pan] && patties[pan][STATE] == BURNT) g.drawImage(burnedpatty[strs[PATTY]], (int) p.getX() - 20, (int) p.getY() - 14, null);
        if(!mouseHeld && pattygrabbed[pan] && patties[pan][STATE] == DONE){
            int plateNum = getPlate(mouseReleasePoint);
            if(plateNum != -1 && !table[plateNum][HASPATTY]){
                patties[pan][TIME] = FALSE;
                table[plateNum][HASPATTY] = true;
                patties[pan][STATE] = RAW;
            }else{
                long elapsed = System.currentTimeMillis() - pause;
                patties[pan][TIME] -= elapsed;
                patties[pan][STATE] = DONE;
            }
            pattygrabbed[pan] = false;
        }
        if(patties[pan][STATE] == BURNT && pattygrabbed[pan] && trashRect.contains(p)) g.drawImage(trash, 0, 0, null);
        if(!mouseHeld && pattygrabbed[pan] && patties[pan][STATE] == BURNT){
            if(trashRect.contains(p)){
                patties[pan][TIME] = FALSE;
                patties[pan][STATE] = RAW;
                
            }else{
                long elapsed = System.currentTimeMillis() - pause;
                patties[pan][TIME] -= elapsed;
                patties[pan][STATE] = BURNT;
            }
            pattygrabbed[pan] = false;
        }
    }

    public void draw(Graphics g) {
        if(plates < strs[PLATE] && mousePressPoint != null && bunbasket.contains(mousePressPoint) && mouseClicked){
            table[plates][HASPLATE] = true;
            plates++;
            mouseClicked = false;
            System.out.println("PLATE ADDED");
        }

        if(mousePressPoint != null && rawpattybasket.contains(mousePressPoint) && mouseClicked){
            for(int i = 0; i <= strs[PANS]; i++){
                if(patties[i][TIME] == FALSE){
                    patties[i][TIME] = System.currentTimeMillis();
                    break;
                }
            }
            mouseClicked = false;
        }

        drawBurger(g, 282, 355, 0);
        drawBurger(g, 296, 301, 1);
        drawBurger(g, 305, 255, 2);

        if(strs[PANS] == 0){
            Rectangle pattyRect = new Rectangle(628, 450, 40, 28);
            checkPatty(pattyRect, 0, g);
            drawCooking(g, 628, 450, 0);
        }

        if(strs[PANS] == 1){
            Rectangle pattyRect [] = new Rectangle[]{new Rectangle(628, 449, 40, 28),
                                                     new Rectangle(608, 399, 40, 28)};
            for(int i = 0; i < 2; i++){
                checkPatty(pattyRect[i], i, g);
            }
            drawCooking(g, 628, 449, 0);
            drawCooking(g, 608, 399, 1);
        }

        if(strs[PANS] == 2){
            Rectangle pattyRect [] = new Rectangle[]{new Rectangle(628, 449, 40, 28),
                                                     new Rectangle(608, 399, 40, 28),
                                                     new Rectangle(588, 344,40, 28)};
            for(int i = 0; i < 3; i++){
                checkPatty(pattyRect[i], i, g);
            }
            
            drawCooking(g, 628, 449, 0);
            drawCooking(g, 608, 399, 1);
            drawCooking(g, 588, 344, 2);
        }

        if(strs[PANS] == 3){
            Rectangle pattyRect [] = new Rectangle[]{new Rectangle(635, 457, 40, 28),
                                                     new Rectangle(613, 404, 40, 28),
                                                     new Rectangle(593, 358,40, 28),
                                                     new Rectangle(573, 307,40, 28)};
            for(int i = 0; i < 4; i++){
                checkPatty(pattyRect[i], i, g);
            }
            drawCooking(g, 635, 457, 0);
            drawCooking(g, 613, 404, 1);
            drawCooking(g, 593, 358, 2);
            drawCooking(g, 573, 307, 3);
        }

        if(strs[LETTUCE] != -1){
            if(mouseHeld && lettucebasket[strs[LETTUCE]].contains(mousePressPoint)){
                lettucegrabbed = true;
                g.drawImage(imgs[GRABLETTUCE], (int) p.getX() - 20, (int) p.getY() - 15, null);
            }

            if(lettucegrabbed && !mouseHeld){
                int plateNum = getPlate(mouseReleasePoint);
                if(plateNum != -1){
                    table[plateNum][HASLETTUCE] = true;
                }
                lettucegrabbed = false;
            }
        }

        if(strs[TOMATO] != -1){
            if(mouseHeld && tomatobasket[strs[TOMATO]].contains(mousePressPoint)){
                tomatograbbed = true;
                g.drawImage(imgs[GRABTOMATO], (int) p.getX() - 20, (int) p.getY() - 15, null);
            }

            if(tomatograbbed && !mouseHeld){
                int plateNum = getPlate(mouseReleasePoint);
                if(plateNum != -1){
                    table[plateNum][HASTOMATO] = true;
                }
                tomatograbbed = false;
            }
        }

        if(mouseHeld){
            for(int i = 0; i < 3; i++){
                if(plateRect[i].contains(mousePressPoint) && table[i][HASPATTY]){
                    grabbed = plateRect[i];
                    grabbedPlate = i;
                    if(table[i][HASLETTUCE] && table[i][HASTOMATO]){
                        grabbedtype = LETTUCETOMATOBURGER;
                    }
                    else if(table[i][HASLETTUCE] && !table[i][HASTOMATO]){
                        grabbedtype = LETTUCEBURGER;
                    }
                    else if(!table[i][HASLETTUCE] && table[i][HASTOMATO]){
                        grabbedtype = TOMATOBURGER;
                    }
                    else{
                        grabbedtype = BURGER;
                    }
                }
            }
        }
        if(served){
            if(grabbed==null) System.out.println("GRABBED");
            if(grabbedtype==-1) System.out.println("GRABBEDTYPE");
            if(grabbedPlate==-1) System.out.println("GRABBEDPLATE");
            if(grabbed!=null && grabbedtype !=-1 && grabbedPlate!=-1){
                for(int i = 0; i <= 3; i++){
                    table[grabbedPlate][i] = false;
                }
                plates--;
                grabbed = null;
                grabbedtype = -1;
                grabbedPlate = -1;
    
            }
            served = false;
        }
        else if(!mouseHeld){
            if(skip < 2){
                skip ++;
            }else{
                grabbed = null;
                grabbedtype = -1;
                grabbedPlate = -1;
                skip = 0;
            }
        }
    }
}
