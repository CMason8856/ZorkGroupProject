
/**
 * Created by Michael Timpson on 11/8/16.
 * @author Michael
 * @return
 */
class Event {
    
    int num;
    Item item;
    boolean bol;

    /**
     *
     * @param num
     * Construtor for event class
     */
    Event(int num) {
        this.num = num;
    }

    /**
     *
     * @param item
     * Constructor for event class
     */
    Event(Item item) {
        this.item = item;
    }

    /**
     *
     * @param bol
     * Constructor for event class
     */
    Event(Boolean bol) {
        this.bol = bol;
    }

    /**
     *
     * @param score
     * @return
     *
     */
    static int score(int score) {
        GameState.instance().addScore(score);
        return GameState.getScore();
    }

    /**
     *
     * @param health
     * @return
     */
    static int wound(int health) {
        GameState.instance().addHealth(health);
        return GameState.getHealth();
    }

    /**
     *
     * @param death
     * @return
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
     *
     * @param win
     * @return
     */
    static String win(boolean win) {
        if(win == true)
        {
            System.out.println("Congratulations, you won!");
            System.exit(0);
            return "Congratulations, you won!";
        }
        return null;//should never return null
    }

    /**
     *
     * @param item
     */
    static void disappear(Item item) {
        GameState.removeFromInventory(item);
    }

    /**
     *
     * @param item
     * @return
     */
    static void transform(Item item, Item item2) {

        GameState.removeFromInventory(item);
        GameState.addItemToIventory(item2);

    }

    /**
     *
     * @param exit
     */
    static void unlock(Exit exit) {
        GameState.unlockExit(exit);
    }

    /**
     *
     * @return
     */
    static void teleport() {
        GameState.setAdventurersCurrentRoom(Dungeon.getEntry());
        System.out.println("You have been teleported back to the start of the Dungeon!");
        return;
    }

}