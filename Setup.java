/*
 * Helper class for creating levels. Draws all items and has them ready to use since the basic setup for all 
 * levels is the same, just the people and what the order change.
 */
import java.awt.*;
import javax.swing.*;

public class Setup {
    // constants for item indices
    public static final int FRIES = 0, FRYER = 1, TOMATOES = 2, LETTUCE = 3, KETCHUP = 4, WARMER = 5, SAUSAGES = 6;
    public static final int HOTDOGBUNS = 7, HOTDOGTABLE = 8, HOTDOGPAN = 9, BURGERTABLE = 10, BURGERBUNS = 11, DISPENSER = 12;
    public static final int COLA = 13, BURGERPAN = 14, PATTIES = 15;
    public static final Rectangle menuRect = new Rectangle(11, 12, 119, 29); // menu btnd
    public static final Rectangle timeRect = new Rectangle(214, 15, 140, 22); // rectangle that fills as time passes

    // once starRect[0] is filled as the player earns more money, the first star is earned and so on
    public static final Rectangle[] starRect = new Rectangle[]{new Rectangle(414, 15, 100, 24),
                                                               new Rectangle(414, 15, 120, 24),
                                                               new Rectangle(414, 15, 140, 24)};

    private static Setup instance; 

    private Image bg, playtopbar, playtopbartop;
    private Image menuhover;
    private Image[] imgs, starimgs;
    private int[] strs; // strs[x] = # of stars item x has
    private Point p; // mouse point
    private Boolean [][]item; // whether item[LEVEL][x] exists in this level
    private Boolean mouseReleased;
    private Boolean itemupgraded; // to tell level classes to update images since an item was upgraded
    private int level; // the lvl we are setting up
    // goal: amount of money that will earn player 3 stars
    // money: money player has earned rn
    // starachieved: star player has achieved
    private int goal, money, starachieved;
    private long starttime; // when the level starts
    private Hamburger hamburger;
    private Hotdog hotdog;
    private ColaDispenser coladispenser;

    private Setup() {
        // initialize variables
        itemupgraded = false;
        starimgs = new Image[]{new ImageIcon("1star.png").getImage(),
                               new ImageIcon("2star.png").getImage(),
                               new ImageIcon("3star.png").getImage()};
        mouseReleased = false; // used in levels to see where mouse was released (when/if to hand off items)
        starttime = -1;
        goal = -1;
        money = 0;
        starachieved = 0;
        level = 0;
        playtopbar = new ImageIcon("playtopbar.png").getImage();
        playtopbartop = new ImageIcon("playtopbartop.png").getImage();
        bg = new ImageIcon("play.png").getImage();
        menuhover = new ImageIcon("menuhover.png").getImage();
        imgs = new Image[16];
        strs = null;
        p = null;
        hamburger = new Hamburger();
        hotdog = new Hotdog();
        coladispenser = new ColaDispenser();
        item = new Boolean[][]{{false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, true},
                               {false, false, false, false, false, false, true, true, true, true, true, true, true, true, true, true},
                               {false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, true},
                               {false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, true},
                               {false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true},
                               {false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
                               {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}};
    }

    // startime is set in Panel once a level has been clicked
    public void setStarttime(){ starttime = System.currentTimeMillis();}

    // used in level classes to add money to setup whenever money is earned
    public void addMoney(int m){ money += m;}

    // method to get the single instance of Setup
    public static Setup getInstance() {
        if (instance == null) {
            instance = new Setup();
        }
        return instance;
    }

    // used in level classes to set the level Setup is being used for
    public void setLevel(int lvl){
        level = lvl - 1;
        setImgs(strs);
    }

    // used in level classes to get how many stars x item has 
    public int[] getBurgerStars(){
        itemupgraded = false; // bc new stars r set
        return new int[]{strs[BURGERBUNS], strs[BURGERBUNS], strs[TOMATOES], strs[LETTUCE], strs[TOMATOES], strs[LETTUCE], strs[PATTIES], strs[PATTIES]};
    }

    public int[] getColaStars(){
        itemupgraded = false;
        return new int[]{strs[DISPENSER], strs[COLA], strs[COLA]};
    }

    public boolean getItemUpgraded(){return itemupgraded;};

    // used in level classes to set the goal
    public void setGoal(int g){goal = g;}

    // gets rectangle of item grabbed 
    // used in level classes
    public Rectangle getGrabbedRect(){
        if(hamburger.getGrabbedRect()!=null) return hamburger.getGrabbedRect();
        if(coladispenser.getGrabbedRect()!=null) return coladispenser.getGrabbedRect();
        else return null;
    } // gets type of item grabbed
    public int getGrabbedType(){
        if(hamburger.getGrabbedType() != -1) return hamburger.getGrabbedType();
        // cola & fries doesn't need it bc there's no toppings
        else return -1;
    }

    // the grabbed item was handed to a customer and it was the customers order meaning it should no longer exist
    public void removeBurger(){hamburger.removeBurger();}
    public void removeCola(){coladispenser.removeCola();}



    // used in Upgrades, reloads imgs and updates stars whenever an item is upgraded
    public void setImgs(int[] stars) {
        itemupgraded = true;
        stars[BURGERPAN] = 3; // DELETE LTR

        // reset stars in other classes
        if(!item[level][KETCHUP]) hotdog.setImgs(new int [] {stars[HOTDOGBUNS], stars[SAUSAGES], stars[SAUSAGES], stars[SAUSAGES], -1, -1, -1, stars[HOTDOGTABLE], stars[HOTDOGPAN]});
        else hotdog.setImgs(new int [] {stars[HOTDOGBUNS], stars[SAUSAGES], stars[SAUSAGES], stars[SAUSAGES], stars[KETCHUP], stars[KETCHUP], stars[KETCHUP], stars[HOTDOGTABLE], stars[HOTDOGPAN]});
        if(!item[level][TOMATOES] && !item[level][LETTUCE]) hamburger.setImgs(new int [] {stars[BURGERBUNS], stars[BURGERBUNS], -1, -1, -1, -1, stars[PATTIES], stars[PATTIES], stars[BURGERTABLE], stars[BURGERPAN]});
        else if(!item[level][TOMATOES] && item[level][LETTUCE]) hamburger.setImgs(new int [] {stars[BURGERBUNS], stars[BURGERBUNS], -1, stars[LETTUCE], -1, stars[LETTUCE], stars[PATTIES], stars[PATTIES], stars[BURGERTABLE], stars[BURGERPAN]});
        else if(item[level][TOMATOES] && !item[level][LETTUCE]) hamburger.setImgs(new int [] {stars[BURGERBUNS], stars[BURGERBUNS], stars[TOMATOES], -1, stars[TOMATOES], -1, stars[PATTIES], stars[PATTIES], stars[BURGERTABLE], stars[BURGERPAN]});
        else hamburger.setImgs(new int [] {stars[BURGERBUNS], stars[BURGERBUNS], stars[TOMATOES], stars[LETTUCE], stars[TOMATOES], stars[LETTUCE], stars[PATTIES], stars[PATTIES], stars[BURGERTABLE], stars[BURGERPAN]});
        coladispenser.setImgs(new int [] {stars[DISPENSER], stars[COLA]});

        strs = stars;
        String[] names = {"fries", "fryer", "tomatoes", "lettuce", "ketchup", "warmer", "sausages", "hotdogbuns", 
                          "hotdogtable", "hotdogpans", "burgertable", "burgerbuns", "cokemachine", "coke", 
                          "burgerpan", "patties"};

        // load all imgs, indices 0-15
        for (int i = 0; i <= 15; i++) {
            String fileName;
            if(i == HOTDOGTABLE && !item[level][HOTDOGTABLE]) fileName = "hotdogtablesetup0.png"; // if this isn't unlocked yet, load the locked version
            else if(i == HOTDOGTABLE || i == BURGERTABLE) fileName = names[i] + "setup" + (stars[i]) + ".png"; // stars start at 1 not 0
            else fileName = names[i] + "setup" + (stars[i]+1) + ".png";
            imgs[i] = new ImageIcon(fileName).getImage();
        }

    }
 
    // used in Panel to set where the cursor is
    public void setMouse(Point mousePoint) {
        if (mousePoint != null) {
            p = mousePoint;
            // set to all other classes
            hamburger.setMouse(p);
            hotdog.setMouse(p);
            coladispenser.setMouse(p);
        }
    }

    public boolean getMouseReleased(){return mouseReleased;}
    public Point getMouse(){return p;} // used in level classes to get where the cursor is

    public void setMousePressPoint(Point point){ // point where mouse was pressed
        if(point != null) {
            hamburger.setMousePressPoint(point);
            hotdog.setMousePressPoint(point);
            coladispenser.setMousePressPoint(point);
            mouseReleased = false;
        }
    }

    public void setMouseReleasePoint(Point point){ // point where mouse was released
        if(point != null) {
            hamburger.setMouseReleasePoint(point);
            hotdog.setMouseReleasePoint(point);
            coladispenser.setMouseReleasePoint(point);
            mouseReleased = true;
        }
    }

    public Boolean menuHover(){return menuRect.contains(p);} // used in Panel to switch screens


    // draw the rect that shows player how much time they have left
    public void fillRectangle(Graphics g, Rectangle r, int seconds, Color c){
        seconds*=1000; // convert to milis
        int x = r.x, y = r.y, w = r.width, h = r.height;
        long elapsed = System.currentTimeMillis() - starttime;
        g.setColor(c);
        if(elapsed < seconds){
            w = (int)((w*elapsed)/seconds); // get width of rect correlated to the time passed
            g.fillRect(x, y, w, h);
        }
        else{
            g.fillRect(r.x, r.y, r.width, r.height);
        }
    }

    // Draw method to render the images
    public void draw(Graphics g) {
        money += hamburger.getLoses();
        money += hotdog.getLoses();
        g.drawImage(bg, 0, 0, null);
        if(item[level][TOMATOES]) g.drawImage(imgs[TOMATOES], 513, 420, null); // tomato basket
        if(item[level][LETTUCE]) g.drawImage(imgs[LETTUCE], 503, 350, null);  // lettuce basket
          
        // bun baskets
        if(item[level][HOTDOGBUNS]) g.drawImage(imgs[HOTDOGBUNS], 413, 460, null); 
        if(item[level][BURGERBUNS]) g.drawImage(imgs[BURGERBUNS], 303, 460, null);
        
        // sausage/patty baskets
        if(item[level][SAUSAGES]) g.drawImage(imgs[SAUSAGES], 720, 500, null); 
        if(item[level][PATTIES]) g.drawImage(imgs[PATTIES], 630, 508, null);

        // fryer
        if(item[level][FRYER]) g.drawImage(imgs[FRYER], 155, 468, null);  
        //if(item[level][FRIES]) g.drawImage(imgs[FRIES], 221, 382, null);  

        // pans
        if(strs[BURGERPAN] == 0 && item[level][BURGERPAN]) g.drawImage(imgs[BURGERPAN], 612, 415, null);
        if(strs[HOTDOGPAN] == 0 && item[level][HOTDOGPAN]) g.drawImage(imgs[HOTDOGPAN], 688, 417, null);
        if(strs[BURGERPAN] == 1 && item[level][BURGERPAN]){
            g.drawImage(imgs[BURGERPAN], 612, 415, null);
            g.drawImage(imgs[BURGERPAN], 592, 365, null);
        }
        if(strs[HOTDOGPAN] == 1 && item[level][HOTDOGPAN]){
            g.drawImage(imgs[HOTDOGPAN], 688, 417, null);
            g.drawImage(imgs[HOTDOGPAN], 660, 362, null);
        }
        if(strs[BURGERPAN] == 2 && item[level][BURGERPAN]){
            g.drawImage(imgs[BURGERPAN], 612, 415, null);
            g.drawImage(imgs[BURGERPAN], 592, 365, null);
            g.drawImage(imgs[BURGERPAN], 572, 312, null);
        }
        if(strs[HOTDOGPAN] == 2 && item[level][HOTDOGPAN]){
            g.drawImage(imgs[HOTDOGPAN], 688, 417, null);
            g.drawImage(imgs[HOTDOGPAN], 660, 362, null);
            g.drawImage(imgs[HOTDOGPAN], 640, 309, null);
        }
        if(strs[BURGERPAN] == 3 && item[level][BURGERPAN]){
            g.drawImage(imgs[BURGERPAN], 623, 430, null);
            g.drawImage(imgs[BURGERPAN], 602, 378, null);
            g.drawImage(imgs[BURGERPAN], 581, 332, null);
            g.drawImage(imgs[BURGERPAN], 562, 282, null);
        }
        if(strs[HOTDOGPAN] == 3 && item[level][HOTDOGPAN]){
            g.drawImage(imgs[HOTDOGPAN], 695, 424, null);
            g.drawImage(imgs[HOTDOGPAN], 670, 377, null);
            g.drawImage(imgs[HOTDOGPAN], 644, 329, null);
            g.drawImage(imgs[HOTDOGPAN], 623, 281, null);
        }

        // table (locks)
        g.drawImage(imgs[HOTDOGTABLE], 0, 0, null);
        g.drawImage(imgs[BURGERTABLE], 0, 0, null);

        // drawing the rectangle that shows how much time player has left
        g.setColor(Color.GRAY); // background full rectangle is gray
        g.fillRect(timeRect.x, timeRect.y, timeRect.width, timeRect.height);

        fillRectangle(g, timeRect, 5, Color.GREEN); // draw the rect
        g.drawImage(playtopbar, 0, 0, null);  
        goal = 10;
        int w = starRect[2].width;
        g.setColor(Color.YELLOW);
        if(money < goal){
            w = (int)((w*money)/goal);
            if(w >= starRect[0].width) starachieved = 1;
            if(w >= starRect[1].width) starachieved = 2;
            g.fillRect(starRect[2].x, starRect[2].y, w, starRect[2].height);
        }
        else{
            starachieved = 3;
            g.fillRect(starRect[2].x, starRect[2].y, starRect[2].width, starRect[2].height);
        }
        if(starachieved!=0) g.drawImage(starimgs[starachieved-1], 0, 0, null);

        g.drawImage(playtopbartop, 0, 0, null);  
        if(menuHover()) g.drawImage(menuhover, 0, 0, null);
        if(item[level][HOTDOGTABLE])hotdog.draw(g); // only draw if this level has hotdogs unlocked
        hamburger.draw(g);
        coladispenser.draw(g);
    }
}
