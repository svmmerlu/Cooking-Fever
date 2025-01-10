import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class CookingFeverPanel extends JPanel implements ActionListener {
    public static final int HOME = 0, PLAY = 1, UPGRADES = 2, MENU = 3, LEVELS = 4; // no magic numbers
    public static final int LEVEL1 = 5, LEVEL2 = 6, LEVEL3 = 7, LEVEL4 = 8, LEVEL5 = 9, LEVEL6 = 10, LEVEL7 = 11, LEVEL8 = 12, LEVEL9 = 13, LEVEL10 = 14; // no magic numbers
    private Timer timer;
    private int screen;
    private Home home;
    private Upgrades upgrades;
    private Menu menu;
    private Levels levels;
    private Level1 level1;
    private Setup setup;

    public CookingFeverPanel() {
        screen = LEVELS;

        setup = Setup.getInstance();
        home = new Home();
        upgrades = new Upgrades();
        menu = new Menu(screen);
        levels = new Levels();
        level1 = new Level1();
// sdffds
        timer = new Timer(20, this);
        timer.start();

        setPreferredSize(new Dimension(902, 630)); // screen size
        setFocusable(true);
        requestFocus();
        addMouseListener(new ClickListener());
    }

    public void setMouse() {
        if (isShowing()) { 
            Point p = MouseInfo.getPointerInfo().getLocation();
            Point panelLocation = getLocationOnScreen();
            Point mousePoint = new Point(p.x - panelLocation.x, p.y - panelLocation.y);

            home.setMouse(mousePoint);
            upgrades.setMouse(mousePoint);
            menu.setMouse(mousePoint);
            levels.setMouse(mousePoint);
            setup.setMouse(mousePoint);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        setMouse();
    }

    @Override
    public void paint(Graphics g) {
        if(screen == HOME)home.draw(g);
        if(screen == UPGRADES)upgrades.draw(g);
        if(screen == MENU)menu.draw(g);
        if(screen == LEVELS)levels.draw(g);
        if(screen == LEVEL1)level1.draw(g);
    }

    class ClickListener implements MouseListener {
        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {setup.setMouseReleasePoint(e.getPoint());}

        @Override
        public void mouseClicked(MouseEvent e) {}
    
        @Override
        public void mousePressed(MouseEvent e) {
            //Point clickPoint = e.getPoint();
            if(screen == HOME && home.playHover()) screen = LEVELS;
            if(screen == HOME && home.upgradesHover()) screen = UPGRADES;
            if(screen == UPGRADES) upgrades.setClicked(true);
            if(screen == UPGRADES && upgrades.menuHover()){ 
                menu.setPrevScreen(screen);
                screen = MENU;
            }
            if(screen == LEVELS && levels.menuHover()){ 
                menu.setPrevScreen(screen);
                screen = MENU;
            }
            if(screen == MENU && menu.mainMenuHover()) screen = HOME;
            if(screen == MENU && menu.upgradesHover()) screen = UPGRADES;
            if(screen == MENU && menu.backHover()) screen = menu.getPrevScreen();

            if(screen == LEVELS && levels.getLevelHover() != -1){
                setup.setStarttime();
                screen = levels.getLevelHover();
            }

            if(screen >= LEVEL1 && screen <= LEVEL10 && setup.menuHover()){
                menu.setPrevScreen(screen);
                screen = MENU;
            }

            setup.setMousePressPoint(e.getPoint());
        }
    }
}
