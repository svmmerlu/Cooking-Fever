import java.awt.*;
import java.io.File;
import javax.swing.*;

class Item{
    public static final int FRIES = 0, FRYER = 1, TOMATOES = 2, LETTUCE = 3, KETCHUP = 4, WARMER = 5, SAUSAGES = 6; // no magic numbers
    public static final int HOTDOGBUNS = 7, HOTDOGTABLE = 8, HOTDOGPAN = 9, BURGERTABLE = 10, BURGERBUNS = 11, DISPENSER = 12; // no magic numbers
    public static final int COLA = 13, BURGERPAN = 14, PATTIES = 15; // no magic numbers
    private int[][] moneyCosts = new int[16][3];
    private int[][] diamondCosts = new int[16][3];
    private Image [] imgs;
    private Image upgradebtn, upgradeHoverbtn, star, emptystar, diamond, coin;
    private int stars, type, money, diamonds;
    private Rectangle card, upgradebtnRECT;
    private Point p;
    private Font font;
    private Boolean clicked, upgraded;


 
    public Item(Image [] mgs, int strs, int typ, Rectangle c){
        upgraded = false;
        imgs = mgs;
        stars = strs;
        type = typ;
        card = c;
        money = 0;
        diamonds = 0;
        font = loadNunitoFont(15);
        clicked = false;

        p = new Point(0, 0);

        diamond = new ImageIcon("diamond.png").getImage();
        coin = new ImageIcon("coin.png").getImage();

        star = new ImageIcon("star.png").getImage();
        emptystar = new ImageIcon("emptystar.png").getImage();

        upgradebtn = new ImageIcon("upgradebtn.png").getImage();
        upgradeHoverbtn = new ImageIcon("upgradebtnhover.png").getImage();
        upgradebtnRECT = new Rectangle(c.x + 19, c.y + 118, 95, 20);

        // money costs
        moneyCosts[DISPENSER] = new int[]{150, 600, 700};
        moneyCosts[COLA] = new int[]{800, 900, 1000};
        moneyCosts[BURGERPAN] = new int[]{600, 700, 800};
        moneyCosts[PATTIES] = new int[]{700, 800, 900};
        moneyCosts[BURGERTABLE] = new int[]{0, 400, 600};
        moneyCosts[BURGERBUNS] = new int[]{700, 800, 900};
        moneyCosts[HOTDOGPAN] = new int[]{600, 700, 800};
        moneyCosts[HOTDOGTABLE] = new int[]{0, 400, 600};
        moneyCosts[HOTDOGBUNS] = new int[]{700, 800, 900};
        moneyCosts[SAUSAGES] = new int[]{700, 800, 900};
        moneyCosts[WARMER] = new int[]{300, 400, 500};
        moneyCosts[KETCHUP] = new int[]{400, 500, 600};
        moneyCosts[LETTUCE] = new int[]{400, 500, 600};
        moneyCosts[TOMATOES] = new int[]{400, 500, 600};
        moneyCosts[FRYER] = new int[]{500, 800, 900};
        moneyCosts[FRIES] = new int[]{800, 900, 1000};

        // diamond costs
        diamondCosts[DISPENSER] = new int[]{0, 0, 5};
        diamondCosts[COLA] = new int[]{5, 5, 10};
        diamondCosts[BURGERPAN] = new int[]{0, 0, 5};
        diamondCosts[PATTIES] = new int[]{0, 0, 5};
        diamondCosts[BURGERTABLE] = new int[]{0, 0, 0};
        diamondCosts[BURGERBUNS] = new int[]{0, 0, 5};
        diamondCosts[HOTDOGPAN] = new int[]{0, 0, 5};
        diamondCosts[HOTDOGTABLE] = new int[]{0, 0, 0};
        diamondCosts[HOTDOGBUNS] = new int[]{0, 0, 5};
        diamondCosts[SAUSAGES] = new int[]{0, 0, 5};
        diamondCosts[WARMER] = new int[]{0, 0, 0};
        diamondCosts[KETCHUP] = new int[]{0, 0, 0};
        diamondCosts[LETTUCE] = new int[]{0, 0, 0};
        diamondCosts[TOMATOES] = new int[]{0, 0, 0};
        diamondCosts[FRYER] = new int[]{0, 0, 5};
        diamondCosts[FRIES] = new int[]{5, 5, 10};
    }

    private Font loadNunitoFont(float size) {
         try {
             // load and return the font with the specified size
             Font font = Font.createFont(Font.TRUETYPE_FONT, new File("nunitoSemiBold.ttf")).deriveFont(size);
             GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
             return font;
         } catch (Exception e) {
             e.printStackTrace(); // print exceptions
             return null; // return null if loading fails
         }
    }

    public Image getImage(){return imgs[stars];}
    public int getMoney(){return moneyCosts[type][stars];}
    public int getDiamonds(){return diamondCosts[type][stars];}
    public void setMoney(int m){money = m;}
    public void setDiamonds(int d){money = d;}
    public int getStars(){return stars;}
    public void setClicked(Boolean click){clicked = click;}
    public Boolean getUpgraded(){return upgraded;}

    public void setMouse(Point mousePoint) {
        if (mousePoint != null) {
            p = mousePoint;
        }
    }
    private boolean canUpgrade(){return diamonds >= diamondCosts[type][stars] && money >= moneyCosts[type][stars] && stars < 3;}

    public void draw(Graphics g){
        g.drawImage(upgradebtn, upgradebtnRECT.x, upgradebtnRECT.y, null);
        if(upgradebtnRECT.contains(p) || !canUpgrade()) g.drawImage(upgradeHoverbtn, upgradebtnRECT.x, upgradebtnRECT.y, null);
        if(upgradebtnRECT.contains(p) && canUpgrade() && clicked){
            stars++;
            upgraded = true;
        }

        int xOffset = card.x + 35;
        int yOffset = card.y + 33;

        switch (type) {
            case FRIES, LETTUCE, KETCHUP -> {
                xOffset += 4;
                yOffset += 8;
            }
            case COLA -> {
                xOffset += 9;
                yOffset += 12;
            }
            case PATTIES, SAUSAGES -> {
                xOffset -= 5;
                yOffset -= 2;
            }
        }
        g.drawImage(imgs[stars], xOffset, yOffset, null);

        for(int i = 0; i < 3; i++){
            if(i + 1 <= stars) g.drawImage(star, card.x + 33 + i*23, card.y + 19, null);
            else g.drawImage(emptystar, card.x + 33 + i * 23, card.y + 19, null);
        }

        g.drawImage(diamond, card.x + 80, card.y + 95, null);
        g.drawImage(coin, card.x + 10, card.y + 95, null);

        g.setColor(Color.WHITE);	
        g.setFont(font);
        g.drawString(Integer.toString(moneyCosts[type][stars]), card.x + 34, card.y + 110);
        g.drawString(Integer.toString(diamondCosts[type][stars]), card.x + 104, card.y + 110);
    }
}