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

    public NPC(Scanner s) {

        this.name = s.nextLine();
        String r = s.nextLine();
        this.location = GameState.instance().getDungeon().getRoom(r);
        this.health = Integer.parseInt(s.nextLine());
        String e = s.nextLine();
        this.enemy = true;
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

}