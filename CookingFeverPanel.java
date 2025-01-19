import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class CookingFeverPanel extends JPanel implements ActionListener {
    public static final int HOME = 0, PLAY = 1, UPGRADES = 2, MENU = 3, LEVELS = 4, GAMEOVER = 15; // no magic numbers
    public static final int LEVEL1 = 5, LEVEL2 = 6, LEVEL3 = 7, LEVEL4 = 8, LEVEL5 = 9, LEVEL6 = 10, LEVEL7 = 11, LEVEL8 = 12, LEVEL9 = 13, LEVEL10 = 14; // no magic numbers
    private Timer timer;
    private int screen;
    private Home home;
    private Upgrades upgrades;
    private Menu menu;
    private Levels levels;
    private Level1 level1;
    private Level2 level2;
    private Level3 level3;
    private Level4 level4;
    private Level5 level5;
    private Level6 level6;
    private Level7 level7;
    private Setup setup;
    private Gameover gameover;

    public CookingFeverPanel() {
        screen = LEVELS;

        gameover = new Gameover();
        setup = Setup.getInstance();
        home = new Home();
        upgrades = new Upgrades();
        menu = new Menu(screen);
        levels = new Levels();
        level1 = new Level1();
        level2 = new Level2();
        level3 = new Level3();
        level4 = new Level4();
        level5 = new Level5();
        level6 = new Level6();
        level7 = new Level7();
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
            gameover.setMouse(mousePoint);
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
        if(screen == LEVEL1){
            level1.draw(g);
            if(setup.getGameover()) screen = GAMEOVER;
        }
        if(screen == LEVEL2){
            level2.draw(g);
            if(setup.getGameover()) screen = GAMEOVER;
        }
        if(screen == LEVEL3){
            level3.draw(g);
            if(setup.getGameover()) screen = GAMEOVER;
        }
        if(screen == LEVEL4){
            level4.draw(g);
            if(setup.getGameover()) screen = GAMEOVER;
        }
        if(screen == LEVEL5){
            level5.draw(g);
            if(setup.getGameover()) screen = GAMEOVER;
        }
        if(screen == LEVEL6){
            level6.draw(g);
            if(setup.getGameover()) screen = GAMEOVER;
        }
        if(screen == LEVEL7){
            level7.draw(g);
            if(setup.getGameover()) screen = GAMEOVER;
        }
        if(screen == GAMEOVER){gameover.draw(g);}
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
                // REINITIALIZE STUFF
                setup.reinitialize();
                level1 = new Level1(); // restart level 1 for next time it's used
                //repeat for other lvls
                level2 = new Level2();
                level3 = new Level3();
                level4 = new Level4();
                level5 = new Level5();
                level6 = new Level6();
                level7 = new Level7();
            }

            if(screen == GAMEOVER && gameover.getNextHover()){
                if(setup.getGameover()){ // couldnt be reinitialized earlier bc gameover uses some of the things in setup
                    screen = GAMEOVER; // if gameover, switch screen
                    setup.reinitialize();
                    level1 = new Level1(); // restart level 1 for next time it's used
                    //repeat for other lvls
                    level2 = new Level2();
                    level3 = new Level3();
                    level4 = new Level4();
                    level5 = new Level5();
                    level6 = new Level6();
                    level7 = new Level7();
                } 

              screen = LEVELS; // switch screen to LEVELS if next btn on gamover screen is clicked
            }

            setup.setMousePressPoint(e.getPoint());
        }
    }
}
