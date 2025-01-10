import java.awt.*;
import javax.swing.*;
class Menu{
    private Image menu, mainmenuhover, upgradeshover, backhover;
    public static final Rectangle mainmenuRect = new Rectangle(245, 206, 420, 50);
    public static final Rectangle upgradesRect = new Rectangle(243, 270, 420, 50);
    public static final Rectangle backRect = new Rectangle(367, 388, 178, 37);

    private int prevScreen;
 
    Point p;

    public Menu(int screen){
        menu =  new ImageIcon("menu.png").getImage();
        mainmenuhover =  new ImageIcon("mainmenuhover.png").getImage();
        upgradeshover =  new ImageIcon("interiorupgradeshover.png").getImage();
        backhover =  new ImageIcon("backhover.png").getImage();
        p = null;
        prevScreen = screen;
    }

    public void setMouse(Point point){p = point;}
    public void setPrevScreen(int s){prevScreen = s;}
    public Boolean mainMenuHover(){return mainmenuRect.contains(p);}
    public Boolean upgradesHover(){return upgradesRect.contains(p);}
    public Boolean backHover(){return backRect.contains(p);}

    public int getPrevScreen(){return prevScreen;}

    public void draw(Graphics g){
        g.drawImage(menu, 0, 0, null);
        if(mainMenuHover()) g.drawImage(mainmenuhover, mainmenuRect.x, mainmenuRect.y, null);
        if(upgradesHover()) g.drawImage(upgradeshover, upgradesRect.x, upgradesRect.y, null);
        if(backHover()) g.drawImage(backhover, backRect.x, backRect.y, null);
    }
}