import java.awt.*;
import javax.swing.*;

class Level1{
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
    private Rectangle grabbed;
    private Point p;
    public Level1(){
        rectOccupied = new boolean[]{false, false, false, false};
        starttime = System.currentTimeMillis();
        money = new int[]{NONE, NONE, NONE, NONE};
        coins = new ImageIcon("coins.png").getImage();
        p = null;
        grabbed = null;
        grabbedtype = -1;
        setup = Setup.getInstance();
        setup.setLevel(1);
        setup.setGoal(130);
        burger = new Hamburger(); 
        cola = new ColaDispenser();
        burger.setImgs(setup.getBurgerStars());
        cola.setImgs(setup.getColaStars());
        orders = new int[]{BURGER, COLA, BURGER, COLA, BURGER, BURGER, COLA, COLA, BURGER, COLA, BURGER, COLA};
        
        people = new Person[]{new Person(PURPLEHATWOMAN), new Person(GREYHOODIEKID), new Person(PONYTAILWOMAN), 
                              new Person(BLONDEPIXIEWOMAN), new Person(PURPLEHATWOMAN), new Person(GREYHOODIEKID),
                              new Person(PONYTAILWOMAN), new Person(EARMUFFMAN), new Person(GREENVESTKID),
                              new Person(BLONDEMAN), new Person(OLDWOMAN), new Person(FLUFFYHATMAN)};
        
        peopleposnum = new int[people.length];
        for(int i = 0; i < people.length; i++){
            peopleposnum[i] = NONE; // all positions r currently undetermined
        }
        
        spawntime = new long[]{5000, 10000, 11000, 16000, 30000, 34000, 35000, 39000, 74000, 76000, 80000, 82000};
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
            if(people[i].getRect()!=null)g.drawRect(people[i].getRect().x, people[i].getRect().y, people[i].getRect().width, people[i].getRect().height);
            if(grabbed!=null) g.drawRect(grabbed.x, grabbed.y, grabbed.width, grabbed.height);
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
                    orders[i] = COMPLETED; // update order
                    people[i].setOrderCompleted(); // set order completed
                }
            }

            if(people[i].getRect()!=null && people[i].getState() == ORDERING){ // draw the persons order on the speech bubble if they are ordering
                if(orders[i] == BURGER) burger.drawBurger(g, people[i].getRect().x - 20,130, false, false);
                if(orders[i] == COLA) cola.drawCola(g, people[i].getRect().x + 35, 185);
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
        }
        if(setup.getMouseReleased()){ // nothing is grabbed since mouse released
            grabbed = null;
        }

        if(people[people.length-1].getState() == WALKINGAWAY){
            setup.setGameover(true); // after last customer walks away, game is over
        }
        
    }
}