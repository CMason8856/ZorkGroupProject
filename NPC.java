import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Alfredo Soto
 * Class creates a Non-Player-Character and adds methods
 * that will be used by NPC
 */
public class NPC {

static class NoNpcException extends Exception {}

    private boolean enemy;
    private String name;
    private Room location;
    private int health;
    private ArrayList<Item> inventory;
    
    public static String TOP_LEVEL_DELIM = "===";
    public static String SECOND_LEVEL_DELIM = "---";
    public static String NPC_MARKER = "NPC:";

    /**
     *
     * @param room
     * @param name
     * @param enemy
     * @param health
     *
     * Overloaded NPC Constructors - initialized from .sav file or constructed with variables 
     */
    public NPC(Room room, String name, boolean enemy, int health) {
        location = room;
        this.name = name;
        this.enemy = enemy;
        this.health = health;
    }

    public NPC(Scanner s) throws NoNpcException{

        this.name = s.nextLine();
        //System.out.println(name);
        if(name.indexOf("===")>=0){
            throw new NoNpcException();
        }
            
        String r = s.nextLine();
            //System.out.println(r);
        this.location = GameState.instance().getDungeon().getRoom(r);
            //System.out.println(location);
        this.health = Integer.parseInt(s.nextLine());
            //System.out.println(health);
        String e = s.nextLine();
            //System.out.println(e);
        this.enemy = true;
            //System.out.println(enemy);
    }

    /**
     *
     * @return enemy
     * Returns true or false if enemy
     */
    public boolean getEnemy() {
        return this.enemy;
    }

    /**
     *
     * @param enemy
     * Sets enemy to true or false
     */
    public void setEnemy(boolean enemy) {
        this.enemy = enemy;
    }

    /**
     *
     * @param name
     * Set's name of enemy
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return name
     * Get the name of the enemy.
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param location
     * Set location of the enemy
     */
    public void setLocation(Room location) {
        this.location = location;
    }

    /**
     *
     * @return health
     * Returns the health of the enemy.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     *
     * @param health
     * Sets the health of the enemy.
     */
    public void setHealth(int health) {
        this.health = health;
    }
    public Room getLocation(){
        return location;
    }
    public NPC getNpcByName(String match){
        NPC bro=null;
        for(NPC dude:GameState.instance().getDungeon().getPeople()){
            if(dude.getName().equals(match)){bro=dude;}
        }
        return bro;
    }
    public void storeState(PrintWriter w){
        w.println(NPC_MARKER);
        w.println(name);
        w.println(location.getTitle());
        w.println(health);
        w.println(enemy);
        w.println(SECOND_LEVEL_DELIM);
    }
    public void restoreState(Scanner s){
        String line=s.nextLine();//Location
        line=s.nextLine();//Health
        health=Integer.parseInt(line);
        line=s.nextLine();//enemy boolean
        line=s.nextLine();//'---'
        
    }
}