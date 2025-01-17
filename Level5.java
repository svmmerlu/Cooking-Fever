import java.awt.*;
import javax.swing.*;

class Level5{
    public static final int PURPLEHATWOMAN = 0, GREYHOODIEKID = 1, PONYTAILWOMAN = 2, BLONDEPIXIEWOMAN = 3, EARMUFFMAN = 4;
    public static final int GREENVESTKID = 5, BLONDEMAN = 6, OLDWOMAN = 7, FLUFFYHATMAN = 8;
    public static final int BURGER = 0, COLA = 1, HOTDOG = 2, KETCHUPHOTDOG = 3, LETTUCEBURGER = 4, TOMATOBURGER = 5;
    public static final int LETTUCETOMATOBURGER = 6, FRIES = 7, COMPLETED = 100;
    public static final int NONE = -1, WALKING = 0, ORDERING = 1, WALKINGAWAY = 2;

    public static final Rectangle[] coinRects = new Rectangle[]{new Rectangle(89, 232, 52, 37),
                                                new Rectangle(309, 232, 52, 37),
                                                new Rectangle(529, 232, 52, 37),
                                                new Rectangle(749, 232, 52, 37)};
    private Setup setup;
    private Image coins;
    private Person [] people;
    private int [] peopleposnum; // the rectangle person x walks to
    private int[] orders;
    private boolean[] rectOccupied;
    private int grabbedtype;
    private int[] money;
    private long starttime;
    private long []spawntime; // the time person x spawns in / can start wlaking into the game if a spot is available
    private Hamburger burger;
    private ColaDispenser cola;
    private Hotdog hotdog;
    private Rectangle grabbed;
    private Point p;
    public Level5(){
        hotdog = new Hotdog();
        rectOccupied = new boolean[]{false, false, false, false};
        starttime = System.currentTimeMillis();
        money = new int[]{NONE, NONE, NONE, NONE};
        coins = new ImageIcon("coins.png").getImage();
        p = null;
        grabbed = null;
        grabbedtype = -1;
        setup = Setup.getInstance();
        setup.setLevel(3);
        setup.setGoal(180);
        burger = new Hamburger(); 
        cola = new ColaDispenser();
        burger.setImgs(setup.getBurgerStars());
        cola.setImgs(setup.getColaStars());
        hotdog.setImgs(setup.getHotdogStars());
        orders = new int[]{LETTUCETOMATOBURGER, BURGER, LETTUCEBURGER, COLA, KETCHUPHOTDOG, LETTUCETOMATOBURGER, BURGER, COLA, COLA, COLA, COLA, KETCHUPHOTDOG, LETTUCETOMATOBURGER, COLA};
        
        people = new Person[]{new Person(OLDWOMAN), new Person(BLONDEMAN), new Person(BLONDEPIXIEWOMAN), 
                              new Person(PURPLEHATWOMAN), new Person(PONYTAILWOMAN), new Person(BLONDEMAN),
                              new Person(BLONDEPIXIEWOMAN), new Person(PURPLEHATWOMAN), new Person(EARMUFFMAN),
                              new Person(GREYHOODIEKID), new Person(FLUFFYHATMAN), new Person(BLONDEMAN), 
                              new Person(PONYTAILWOMAN), new Person(GREYHOODIEKID)};
        
        peopleposnum = new int[people.length];
        for(int i = 0; i < people.length; i++){
            peopleposnum[i] = NONE; // all positions r currently undetermined
        }
        
        spawntime = new long[]{3000, 6000, 9000, 12000, 37000, 40000, 43000, 45000, 71000, 73000, 76000, 79000, 105000, 108000};
    }

    public void setStarttime(long s){starttime = s;}

    private int getRect(){
        int rec = NONE;
        for(int i = 0; i < 4; i++){
            if(!rectOccupied[i]){
                rec = i;
                rectOccupied[i] = true;
                break;
            }
        }
        return rec;
    }
    
    public void draw(Graphics g){
        p = setup.getMouse();
        if(grabbed == null){ // only update is grabbed isn't already a thing or else it will mess w the coordinates of the grabbed object
            grabbed = setup.getGrabbedRect();
            grabbedtype = setup.getGrabbedType();
        }
        setup.draw(g);
        long elapsed = System.currentTimeMillis() - starttime;
        for(int i = 0; i < people.length; i++){ // iterate thru all people
            if(elapsed >= spawntime[i]){ // if it is there time to spawn in
                // we only want to draw them into a spot if there is a open spot for them to walk to
                if(peopleposnum[i] == NONE) peopleposnum[i] = getRect(); // getRect() will be NONE if no open Rect
                if(peopleposnum[i] != NONE){ // if there is a open spot, move the person and draw them to that position
                    people[i].moveTo(peopleposnum[i]);
                    people[i].draw(g);
                }
            }
        }
        for(int i = 0; i < people.length; i++){ 
            //if(people[i].getRect()!=null)g.drawRect(people[i].getRect().x, people[i].getRect().y, people[i].getRect().width, people[i].getRect().height);
            //(grabbed!=null) g.drawRect(grabbed.x, grabbed.y, grabbed.width, grabbed.height);
            // check if player is grabbing an object and if that object collides with a person who is ordering
            if(grabbed != null && people[i].getState() == ORDERING && people[i].getRect().contains(grabbed) && setup.getMouseReleased()){
                if(grabbedtype == orders[i]){ // if the grabbed object matches what that person ordered
                    if(orders[i] == BURGER){ 
                        money[people[i].getRectNum()] = burger.getCost(false, false);
                        setup.removeBurger();
                        System.out.println("BURGER");
                    }
                    if(orders[i] == COLA){
                        money[people[i].getRectNum()] = cola.getCost();
                        setup.removeCola();
                        System.out.println("COLA");
                    } 
                    if(orders[i] == HOTDOG){
                        money[people[i].getRectNum()] = hotdog.getCost();
                        setup.removeHotdog();
                        System.out.println("HOTDOG");
                    } 
                    orders[i] = COMPLETED; // update order
                    people[i].setOrderCompleted(); // set order completed
                }
            }

            if(people[i].getRect()!=null && people[i].getState() == ORDERING){ // draw the persons order on the speech bubble if they are ordering
                if(orders[i] == BURGER) burger.drawBurger(g, people[i].getRect().x - 20,130, false, false);
                if(orders[i] == COLA) cola.drawCola(g, people[i].getRect().x + 35, 185);
                if(orders[i] == HOTDOG) hotdog.drawHotdog(g, people[i].getRect().x + 2, 163, false);
            }

            if(orders[i] == COMPLETED || people[i].getState() == WALKINGAWAY){ // if order is completed or person is walking away
                people[i].moveAway(); // make person walk away
            }

            if(orders[i] != COMPLETED && people[i].getState() == WALKINGAWAY){ // a person is walking away bc their patience ran out
                rectOccupied[people[i].getRectNum()] = false; // rect no longer occupied
                orders[i] = COMPLETED; // mark order as done
            }
        }

        for(int i = 0; i < 4; i++){
            if(money[i] != NONE){ // if there's money at this position
                g.drawImage(coins, coinRects[i].x, coinRects[i].y, null);
                if(!setup.getMouseReleased() && coinRects[i].contains(p)){ // if player presses on the money
                    setup.addMoney(money[i]);
                    money[i] = NONE;
                    rectOccupied[i] = false; // spot no longer occupied once player collects the money in that spot
                }
            }
        }

        if(grabbed != null){ // update grabbed rectangle and drawing since they move with mouse point
            if(grabbedtype == BURGER){
                grabbed = new Rectangle(p.x - 20, p.y - 20, 40, 40);
                burger.drawBurger(g, p.x - 37 - 20, p.y - 35 - 20, false, false);
            }
            if(grabbedtype == COLA){
                grabbed = new Rectangle(p.x - 20, p.y - 20, 40, 40);
                cola.drawCola(g, p.x, p.y);
            }
            if(grabbedtype == HOTDOG){
                grabbed = new Rectangle(p.x - 20, p.y - 20, 55, 40);
                hotdog.drawHotdog(g, p.x - 25, p.y - 17, false);
            }
        }
        if(setup.getMouseReleased()){ // nothing is grabbed since mouse released
            grabbed = null;
        }

        if(people[people.length-1].getState() == WALKINGAWAY){
            setup.setGameover(true); // after last customer walks away, game is over
        }
        
    }
}