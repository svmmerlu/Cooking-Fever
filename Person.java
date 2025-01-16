/*
 * Manages the people, how long they wait, their orders, and animating their walking
 */
import java.awt.*;
import javax.swing.*;

class Person{
    public static final int PURPLEHATWOMAN = 0, GREYHOODIEKID = 1, PONYTAILWOMAN = 2, BLONDEPIXIEWOMAN = 3, EARMUFFMAN = 4;
    public static final int GREENVESTKID = 5, BLONDEMAN = 6, OLDWOMAN = 7, FLUFFYHATMAN = 8;
    public static final int NONE = -1, WALKING = 0, ORDERING = 1, WALKINGAWAY = 2;
    private static final int [] pati = new int[]{23000, 33000, 14000, 23000, 28000, 33000, 31400, 33000, 28000}; // patience / time customer will wait before leaving
    public static final String[] names = new String[]{"purplewoman", "greyhoodie", "ponytail", 
                                                      "pixie", "earmuff", "green",
                                                      "blondeman", "oldwoman", "hat"};
    public static final int[][] frames = new int[][]{{5, 9}, {5, 6}, {5, 7}, {5, 6}, {5, 6}, {5, 6}, {5, 8}, {5, 6}, {4, 8}};
    public static final int W = 0, O = 1;
    Rectangle []rects = new Rectangle[]{new Rectangle(49, 119, 145, 139), 
                                        new Rectangle(269, 119, 145, 139),
                                        new Rectangle(489, 119, 145, 139),
                                        new Rectangle(709, 119, 145, 139)};
    private Image[] walking, ordering;
    private int [] frame;
    private Rectangle rect;
    private int person, speed, x, framedelay, state, patience, rectNum;
    private long starttime;
    public Person(int p){
        starttime = -1;
        patience = pati[p];
        rect = null;
        state = NONE;
        x = -150;
        speed = 5;
        frame = new int[]{0, 0};
        framedelay = 0;
        person = p;
        walking = new Image[frames[person][W]];
        ordering = new Image[frames[person][O]];
        for(int i = 0; i < frames[person][W]; i++){
            String fileName;
            fileName = names[person] + "W" + i + ".png";
            walking[i] = new ImageIcon(fileName).getImage();
        }

        for(int i = 0; i < frames[person][O]; i++){
            String fileName;
            fileName = names[person] + "O" + i + ".png";
            ordering[i] = new ImageIcon(fileName).getImage();
        }
    }

    public int getRectNum(){return rectNum;}

    public void setOrderCompleted(){ state = WALKINGAWAY;}

    public Rectangle getRect(){return rect;}
    public int getState(){return state;}

    public void moveTo(int r){
        rectNum = r;
        rect = rects[r];
        if(x < rect.getX() - 39){
            state = WALKING;
            x+=speed;
        }
        else if(state == WALKING){
            state = ORDERING;
            starttime = System.currentTimeMillis();
        }
    }

    public void moveAway(){
        if(x < 902 + 150 && state == WALKINGAWAY){
            x+=speed;
        }
    }

    public void draw(Graphics g){
        if(state == WALKING || state == WALKINGAWAY){
            framedelay++;
            if(framedelay > 5){
                frame[W]++;
                frame[W] %= frames[person][W];
                framedelay = 0;
            }
            g.drawImage(walking[frame[W]], x, 88, null);
        }
        
        if(state == ORDERING){
            framedelay++;
            if(framedelay > 5 && frame[O] < 4){
                frame[O]++;
                frame[O] %= frames[person][O];
                framedelay = 0;
            }
            g.drawImage(ordering[frame[O]], x, 88, null);
        
            int x = rect.x + 63, y = rect.y + 10, h = rect.height -30, newh;
            g.setColor(Color.GRAY);
            g.fillRect(rect.x + 63, rect.y + 10, 5, rect.height - 30);
            long elapsed = System.currentTimeMillis() - starttime;
            g.setColor(Color.GREEN);
            if(elapsed < patience){
                newh = (int)((h*elapsed)/patience);
                newh = h - newh;
                if(newh < h/2) g.setColor(Color.ORANGE);
                if(newh < h/4) g.setColor(Color.RED);
                g.fillRect(x, y + (h-newh), 5, newh);
            }
            else{
                state = WALKINGAWAY;
            }
        }
    }
}