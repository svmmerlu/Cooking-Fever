import java.awt.*;
import javax.swing.*;

class Levels{
    public static final Rectangle menuRect = new Rectangle(11, 12, 119, 29);
    public static final Rectangle [] levelRects = {new Rectangle(140, 108, 117, 110), 
                                                   new Rectangle(261, 108, 117, 110),
                                                   new Rectangle(382, 108, 117, 110),
                                                   new Rectangle(503, 108, 117, 110),
                                                   new Rectangle(624, 108, 117, 110),
                                                   new Rectangle(138, 224, 117, 110), 
                                                   new Rectangle(260, 224, 117, 110),
                                                   new Rectangle(381, 224, 117, 110),
                                                   new Rectangle(502, 224, 117, 110),
                                                   new Rectangle(623, 224, 117, 110)}; 
    
    public static final Rectangle [] levelbtn = {new Rectangle(154, 169, 89, 40), 
                                                 new Rectangle(275, 169, 89, 40),
                                                 new Rectangle(396, 169, 89, 40),
                                                 new Rectangle(517, 169, 89, 40),
                                                 new Rectangle(638, 169, 89, 40),
                                                 new Rectangle(152, 285, 89, 40), 
                                                 new Rectangle(274, 285, 89, 40),
                                                 new Rectangle(395, 285, 89, 40),
                                                 new Rectangle(516, 285, 89, 40),
                                                 new Rectangle(637, 285, 89, 40)};

    private Boolean []levelunlocked;
    private int []stars;
    private Image levels, menubtn, menuhover, levellocked, levelhover, starlvl0, starlvl1, starlvl2;
    private Point p;

    public Levels(){
        levels = new ImageIcon("levels.png").getImage();
        menubtn = new ImageIcon("menubtn.png").getImage();
        menuhover = new ImageIcon("menuhover.png").getImage();
        levellocked = new ImageIcon("levellocked.png").getImage();
        levelhover = new ImageIcon("levelhover.png").getImage();
        starlvl0 = new ImageIcon("0starlvl.png").getImage();
        starlvl1 = new ImageIcon("1starlvl.png").getImage();
        starlvl2 = new ImageIcon("2starlvl.png").getImage();

        levelunlocked = new Boolean[10];
        levelunlocked[0] = true;
        for(int i = 1; i < 10; i++) levelunlocked[i] = false;

        stars = new int[10];
        stars[0] = 0;
        for(int i = 1; i < 10; i++) stars[i] = 0;
    }

    public Boolean menuHover(){
        if(p!=null) return menuRect.contains(p);
        return false;
    }

    public void setMouse(Point mousePoint) {
        if (mousePoint != null) {
            p = mousePoint;
        }
    }

    public int getLevelHover(){
        for(int i = 0; i < 10; i++){
            if(levelunlocked[i] && levelbtn[i].contains(p)) return i+5;
        }
        return -1;
    }

    public void draw(Graphics g){
        g.drawImage(levels, 0, 0, null);
        g.drawImage(menubtn, 0, 0, null);
        if(menuHover()) g.drawImage(menuhover, 0, 0, null);

        for(int i = 0; i < 10; i++){
            if(stars[i] == 0) g.drawImage(starlvl0, levelRects[i].x + 1, levelRects[i].y + 4, null);
            else if(stars[i] == 1) g.drawImage(starlvl1, levelRects[i].x + 33, levelRects[i].y + 3, null);
            else if(stars[i] == 2) g.drawImage(starlvl2, levelRects[i].x + 75, levelRects[i].y + 18, null);
        }
        
        for(int i = 0; i < 10; i++){
            if(!levelunlocked[i]) g.drawImage(levellocked, levelRects[i].x, levelRects[i].y, null);
        }

        for(int i = 0; i < 10; i++){
            if(p!=null && levelunlocked[i] && levelbtn[i].contains(p)) g.drawImage(levelhover, levelbtn[i].x, levelbtn[i].y, null);
        }
    }
}