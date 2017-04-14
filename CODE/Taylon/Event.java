
/**
 * The event class handles multiple events that may occur during the game and
 * calls the Gamestate to change itself.
 * 
 * @author Taylon Thorpe
 * @version(4/5/17)
 */
class Event {
    
    int num;
    Item item;
    boolean bol;

    /**
     *
     * @param num constructs an event class with an int.
     * Constructor for event class
     */
    Event(int num) {
        this.num = num;
    }

    /**
     * @param item constructs an event class with an item.
     * @param item
     * Constructor for event class
     */
    Event(Item item) {
        this.item = item;
    }

    /**
     *@param bol constructs an event class with an boolean.
     * Constructor for event class
     */
    Event(Boolean bol) {
        this.bol = bol;
    }

    /**
     *Score adds the passed in int and then returns the current score.
     * @param score
     * @return the current score.
     *
     */
    static int score(int score) {
        GameState.instance().addScore(score);
        return GameState.getScore();
    }

    /**
     * Wound subtracts health from the player by the passed in int amount.
     * @param health represents the amount of health to take away.
     * @return the current health
     */
    static int wound(int health) {
        GameState.instance().addHealth(health);
        return GameState.getHealth();
    }

    /**
     *
     *Die returns a death message and exits the System.
     * @param death is the boolean to determine if the player died or not.
     * @return death message
     */
    static String die(boolean death) {
         if(death==true)
         {
             System.out.println("You have died...");
             System.exit(0);
             return "You have died...";

         }
         return null;//this should never be returned if this method is working properly
    }

    /**
     *Win returns a win message and exits the System.
     * @param win is the boolean to determine if the player won or not.
     * @return win message
     */
    static String win(boolean win) {
        if(win == true)
        {
            //System.out.println("Congratulations, you won!");
            System.exit(0);
            return "Congratulations, you won!";
        }
        return null;//should never return null
    }

    /**
     * Disappear calls a method from the Gamestate to remove an item.
     * @param item is the item that is to be removed
     */
    static void disappear(Item item) {
        GameState.removeFromInventory(item);
    }

    /**
     *Transform exchanges two items by calling the removeFromInventory method
     * and addItemToInventory method from GameState
     * @param  item is the item that will be removed from the inventory
     * @param item2 
     */
    static void transform(Item item, Item item2) {

        GameState.removeFromInventory(item);
        GameState.addItemToIventory(item2);

    }

    /**
     *Unlock calls the unlockExit method in GameState
     * 
     * @param exit gives the exit that is to be unlocked.
     */
    static void unlock(Exit exit) {
        GameState.unlockExit(exit);
    }

    /**
     *The teleport method takes the adventurer into the starting room of the 
     * dungeon.
     */
    static void teleport() {
        GameState.setAdventurersCurrentRoom(Dungeon.getEntry());
        System.out.println("You have been teleported back to the start of the Dungeon!");
        return;
    }

}