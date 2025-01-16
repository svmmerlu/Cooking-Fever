/*
 * Manages drawing cola machine, colas, cola dispensing, and moving/grabbing
 */
import java.awt.*;
import javax.swing.*;
class ColaDispenser{
    public static final int COLADISPENSER = 0, COLA = 1, EMPTYCOLA = 2; // indices
    public static final int GRABBED = 0, DISPENSED = 1; // indices
    private Image [] imgs; 
    private int [] strs;
    private Rectangle grabbed;
    private int grabbedtype; // type grabbed
    private long timer[]; // timer[x] = time cola[x] started dispensing
    private long []airtime; // amount of time the cola at spot X is grabbed for
    private Rectangle colaRects[][]; // colaRects[DIPENSER STARS][COLA AT SPOT X] 
    private Point mousePressPoint;
    private boolean mouseHeld, served; // served is same as that in other classes
    private boolean [][] cola; // cola[SPOT X][DISPENSED OR GRABBED]
    public ColaDispenser(){
        // initialize variables
        airtime = new long[]{-1, -1, -1}; // none of them are grabbed
        grabbedtype = 1; // cola has no toppings
        served = false;
        grabbed = null;
        timer = new long[]{0, 0, 0}; // colas start dispensing immediately when level starts
        strs = null; 
        imgs = null;
        mouseHeld = false;
        mousePressPoint = null;
        colaRects = new Rectangle[4][4];
        colaRects[0][0] = new Rectangle(165, 385, 32, 43);
        colaRects[1][0] = new Rectangle(165, 385, 32, 43);
        colaRects[2][0] = new Rectangle(140, 378, 32, 43);
        colaRects[2][1] = new Rectangle(190, 378, 32, 43);
        colaRects[3][0] = new Rectangle(125, 382, 32, 43);
        colaRects[3][1] = new Rectangle(170, 384, 32, 43);
        colaRects[3][2] = new Rectangle(216, 384, 32, 43);

        cola = new boolean[4][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 2; j++){
                cola[i][j] = false;
            }
        }
    }

    public void reinitialize(){
        airtime = new long[]{-1, -1, -1}; // none of them are grabbed
        grabbedtype = 1; // cola has no toppings
        served = false;
        grabbed = null;
        timer = new long[]{0, 0, 0}; // colas start dispensing immediately when level starts
        mouseHeld = false;
        mousePressPoint = null;

        cola = new boolean[4][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 2; j++){
                cola[i][j] = false;
            }
        }
    }

    public int getGrabbedType(){return grabbedtype;}

    public void setImgs(int[] stars) {
        strs = stars;
        timer = new long[]{0, 0, 0, 0};
        for(int i = 0; i <= stars[COLADISPENSER]; i++){
            timer[i] = System.currentTimeMillis();
        }

        imgs = new Image[]{new ImageIcon("coladispenser" + stars[COLADISPENSER] + ".png").getImage(), 
                           new ImageIcon("cola" + stars[COLA] + ".png").getImage(),
                           new ImageIcon("emptycola" + stars[COLA] + ".png").getImage()}; // empty cola
    }

    public int getCost(){
        if(strs[COLA] == 0) return 5;
        if(strs[COLA] == 1) return 6;
        if(strs[COLA] == 2) return 7;
        else return 8;
    }
    public void removeCola(){
        served = true;
    }
    
    public Rectangle getGrabbedRect(){return grabbed;} // used in Setup
    

    public void setMousePressPoint(Point point) { 
        // don't need to check if point is null bc already checked when setMousePressPoint was called in Setup
        mousePressPoint = point;
        mouseHeld = true;
    }

    public void setMouseReleasePoint(Point point){ mouseHeld = false;} // if mouse was released then mouseHeld = false

    // used in level classes
    public void drawCola(Graphics g, int x, int y){ g.drawImage(imgs[COLA], (int)x-20, (int)y-20, null);}

    public void draw(Graphics g){
        g.drawImage(imgs[COLADISPENSER], 90, 248, null);

        for(int i = 0; i <= strs[COLADISPENSER]; i++){  // iterate thru each cola. the # of colas correlates to the dispenser stars
            long elapsed = System.currentTimeMillis() - timer[i];
            if(cola[i][GRABBED] && airtime[i] == -1){
                airtime[i] = System.currentTimeMillis();
            }
            if(strs[COLADISPENSER] == 0){ // has 1 cola and takes 10s to dispense
                if(elapsed >= 10000 && !cola[i][DISPENSED] && !cola[i][GRABBED]){
                    g.drawImage(imgs[EMPTYCOLA], 160, 385, null); 
                    cola[i][DISPENSED] = true;
                }
                else if(!cola[i][GRABBED]){ // if cola is grabbed it is no longer drawn on the dispenser 
                    g.drawImage(imgs[COLA], 160, 385, null); 
                }
            }
            
            if(strs[COLADISPENSER] == 1){
                if(elapsed >= 9000 && !cola[i][DISPENSED] && !cola[i][GRABBED]){
                    g.drawImage(imgs[EMPTYCOLA], 160, 385, null); 
                    cola[i][DISPENSED] = true;
                }
                else if(!cola[i][GRABBED]){
                    g.drawImage(imgs[COLA], 160, 385, null); 
                }
            }

            if(strs[COLADISPENSER] == 2){
                if(elapsed >= 7000 && !cola[i][DISPENSED] && !cola[i][GRABBED]){
                    if(i == 0) g.drawImage(imgs[EMPTYCOLA], 135, 378, null);
                    if(i == 1) g.drawImage(imgs[EMPTYCOLA], 185, 378, null);
                    cola[i][DISPENSED] = true;
                }
                else if(!cola[i][GRABBED]){
                    if(i == 0) g.drawImage(imgs[COLA], 135, 378, null);
                    if(i == 1) g.drawImage(imgs[COLA], 185, 378, null);
                }
            }

            if(strs[COLADISPENSER] == 3){
                if(elapsed >= 4000 && !cola[i][DISPENSED] && !cola[i][GRABBED]){
                    if(i == 0) g.drawImage(imgs[EMPTYCOLA], 120, 382, null);
                    if(i == 1) g.drawImage(imgs[EMPTYCOLA], 165, 384, null);
                    if(i == 2) g.drawImage(imgs[EMPTYCOLA], 211, 384, null);
                    cola[i][DISPENSED] = true;  
                }
                else if(!cola[i][GRABBED]){
                    if(i == 0) g.drawImage(imgs[COLA], 120, 382, null);
                    if(i == 1) g.drawImage(imgs[COLA], 165, 384, null);
                    if(i == 2) g.drawImage(imgs[COLA], 211, 384, null);
                }
            }
        }
        int x = 0;
        if(strs[COLADISPENSER] == 0 || strs[COLADISPENSER] == 1) x = 1;
        if(strs[COLADISPENSER] == 2) x = 2;
        if(strs[COLADISPENSER] == 3) x = 3;
        for(int i = 0; i < x; i++){
            if(cola[i][DISPENSED] && mouseHeld && colaRects[strs[COLADISPENSER]][i].contains(mousePressPoint)){
                cola[i][GRABBED] = true;
                grabbed = colaRects[strs[COLADISPENSER]][i];
            }

            if(cola[i][GRABBED] && !mouseHeld){
                if(served){
                    cola[i][DISPENSED] = false;
                    timer[i] = System.currentTimeMillis(); //reset time
                }
                cola[i][GRABBED] = false;
                timer[i] = timer[i] - (System.currentTimeMillis() - airtime[i]); // subtract airtime from dispensed time
                airtime[i] = -1; // airtime = -1
                grabbed = null;
            }
        }
    }
}