import java.awt.*;
import javax.swing.*;

class Home {
    public static final Rectangle playRECT = new Rectangle(631, 449, 225, 65);
    public static final Rectangle upgradesRECT = new Rectangle(632, 532, 227, 65);
    private Image home, upgradesHover, playHover;
    private Point p;

    public Home() {
        home = new ImageIcon("home.png").getImage();
        upgradesHover = new ImageIcon("upgradesHover.png").getImage();
        playHover = new ImageIcon("playHover.png").getImage();
        p = new Point(0, 0);
    }

    public void setMouse(Point mousePoint) {
        if (mousePoint != null) {
            p = mousePoint;
        }
    }

    public boolean playHover(){ return (playRECT.contains(p));}
    public boolean upgradesHover(){ return (upgradesRECT.contains(p));}

    public void draw(Graphics g) {
        g.drawImage(home, 0, 0, null);
        if(playRECT.contains(p)) g.drawImage(playHover, playRECT.x, playRECT.y, null);

        if(upgradesRECT.contains(p)) g.drawImage(upgradesHover, upgradesRECT.x, upgradesRECT.y, null);
    }
}
