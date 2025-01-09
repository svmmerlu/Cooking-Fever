import java.awt.*;
import java.io.File;

import javax.swing.*;

class Upgrades {
    public static final int FRIES = 0, FRYER = 1, TOMATOES = 2, LETTUCE = 3, KETCHUP = 4, WARMER = 5, SAUSAGES = 6;
    public static final int HOTDOGBUNS = 7, HOTDOGTABLE = 8, HOTDOGPAN = 9, BURGERTABLE = 10, BURGERBUNS = 11, DISPENSER = 12;
    public static final int COLA = 13, BURGERPAN = 14, PATTIES = 15;
    public static final Rectangle menuRect = new Rectangle(11, 12, 119, 29);

    private int money, diamonds;

    private Item[] items;
    private Image upgrades, topbar, menuhover;
    
    private Setup setup;

    private Boolean clicked;
    private Point p;

    private Font font;

    public Upgrades() {
        setup = Setup.getInstance();

        clicked = false;
        font = loadNunitoFont(15);
        upgrades = new ImageIcon("upgrades.png").getImage();
        topbar = new ImageIcon("topbar.png").getImage();
        menuhover = new ImageIcon("menuhover.png").getImage();

        money = 0;
        diamonds = 0;

        p = new Point(0, 0);

        items = new Item[] {
            new Item(getImages("fries"), 0, FRIES, new Rectangle(76, 115, 131, 145)),
            new Item(getImages("fryer"), 0, FRYER, new Rectangle(232, 115, 131, 145)),
            new Item(getImages("tomatoes"), 0, TOMATOES, new Rectangle(386, 116, 131, 145)),
            new Item(getImages("lettuce"), 0, LETTUCE, new Rectangle(542, 115, 131, 145)),
            new Item(getImages("ketchup"), 0, KETCHUP, new Rectangle(698, 115, 131, 145)),
            new Item(getImages("warmer"), 0, WARMER, new Rectangle(18, 278, 131, 145)),
            new Item(getImages("sausages"), 0, SAUSAGES, new Rectangle(167, 278, 131, 145)),
            new Item(getImages("hotdogbuns"), 0, HOTDOGBUNS, new Rectangle(316, 278, 131, 145)),
            new Item(getImages("hotdogtable"), 1, HOTDOGTABLE, new Rectangle(465, 278, 131, 145)),
            new Item(getImages("hotdogpans"), 0, HOTDOGPAN, new Rectangle(613, 278, 131, 145)),
            new Item(getImages("burgertable"), 1, BURGERTABLE, new Rectangle(760, 278, 131, 145)),
            new Item(getImages("burgerbuns"), 0, BURGERBUNS, new Rectangle(79, 441, 131, 145)),
            new Item(getImages("cokemachine"), 0, DISPENSER, new Rectangle(233, 441, 131, 145)),
            new Item(getImages("coke"), 0, COLA, new Rectangle(389, 440, 131, 145)),
            new Item(getImages("burgerpan"), 0, BURGERPAN, new Rectangle(546, 441, 131, 145)),
            new Item(getImages("patties"), 0, PATTIES, new Rectangle(700, 440, 131, 145))
        };

        setup.setImgs(getStars());
    }
    
    private Font loadNunitoFont(float size) {
         try {
             // load and return the font with the specified size
             Font font = Font.createFont(Font.TRUETYPE_FONT, new File("nunitoSemiBold.ttf")).deriveFont(size);
             GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
             return font;
         } catch (Exception e) {
             e.printStackTrace(); // print any exceptions (FontFormatException, IOException)
             return null; // return null if loading fails
         }
    }

    private Image[] getImages(String name) {
        Image[] images = new Image[4];
        for (int i = 0; i < 4; i++) {
            if(name.equals("burgertable") || name.equals("hotdogtable")){
                if(i != 0) images[i] = new ImageIcon(name + (i) + ".png").getImage();
            }
            else images[i] = new ImageIcon(name + (i+1) + ".png").getImage();
        }
        return images;
    }

    private int[] getStars(){
        int[] stars = new int[16];
        for(int i = 0; i <= 15; i++){
            stars[i] = items[i].getStars();
        }
        return stars;
    }

    public void setMouse(Point mousePoint) {
        if (mousePoint != null) {
            p = mousePoint;
        }
    }

    public void setClicked(Boolean click){clicked = click;} 
    public void setMoney(int m){money = m;} 
    public void setDiamonds(int d){diamonds = d;} 

    public Boolean menuHover(){return menuRect.contains(p);}

    public void draw(Graphics g) {
        g.drawImage(upgrades, 0, 0, null);
        g.drawImage(topbar, 0, 0, null);
        if(menuHover()) g.drawImage(menuhover, 0, 0, null);
        g.setColor(Color.WHITE);	
        g.setFont(font);
        g.drawString(Integer.toString(money), 196, 31);
        g.drawString(Integer.toString(diamonds), 361, 31);
        
        for (Item item : items) {
            item.setClicked(clicked);
            item.setMouse(p);
            item.draw(g);
            if(item.getUpgraded()){
                setup.setImgs(getStars());
            }
        }
    }
}
