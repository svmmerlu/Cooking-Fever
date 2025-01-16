import java.awt.*;
import java.io.File;

import javax.swing.*;
class Gameover {
    public static final Rectangle nextRect = new Rectangle(695, 483, 55, 55);
    public static final Rectangle restartRect = new Rectangle(618, 483, 55, 55);
    private Image bg, restartHover, nextHover; // bg img and hovered btn images
    private Image[] starimg; // imgs of how many stars the player finishes w
    private Point p; // mousepoint
    private Setup setup;
    private int stars; // # stars player finishes w
    private int money; // # money player earned during the game
    private Font font; 

    public Gameover() {
        font = loadNunitoFont(20);
        restartHover = new ImageIcon("restartHover.png").getImage();
        nextHover = new ImageIcon("nextHover.png").getImage();
        starimg = new Image[]{new ImageIcon("gameover0star.png").getImage(), 
                              new ImageIcon("gameover1star.png").getImage(), 
                              new ImageIcon("gameover2star.png").getImage(), 
                              new ImageIcon("gameover3star.png").getImage()};
        setup = Setup.getInstance();
        stars = setup.getDiamondsAfterLvl(); // # diamonds = # stars
        money = setup.getMoneyAfterLvl();
        bg = new ImageIcon("gameover.png").getImage();
        p = new Point(0, 0);
    }
    private Font loadNunitoFont(float size) {
         try {
             // load and return the font in size
             Font font = Font.createFont(Font.TRUETYPE_FONT, new File("nunitoSemiBold.ttf")).deriveFont(size);
             GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
             return font;
         } catch (Exception e) {
             e.printStackTrace(); // print exceptions 
             return null; // return null if loading fails
         }
    }

    public void setMouse(Point mousePoint) {
        if (mousePoint != null) {
            p = mousePoint;
        }
    }

    public boolean getNextHover(){return nextRect.contains(p);}

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1000, 1000);
        g.drawImage(bg, 0, 0, null);
        g.drawImage(starimg[stars], 0, 0, null);
        g.setColor(Color.WHITE);	
        g.setFont(font);
        g.drawString(Integer.toString(money), 432, 315);
        if(nextRect.contains(p)) g.drawImage(nextHover, nextRect.x, nextRect.y, null);
        if(restartRect.contains(p)) g.drawImage(restartHover, restartRect.x + 2, restartRect.y, null);
    }
}
