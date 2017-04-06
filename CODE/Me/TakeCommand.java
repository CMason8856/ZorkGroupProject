

/**
 * @author Alfredo
 * 
 * class to handle TAKE commands from user
 */

class TakeCommand extends Command {

    private String itemName;
/**
 * constructor to create object
 * @param itemName 
 */
    TakeCommand(String itemName) {
        this.itemName = itemName;
    }

/**
 * either prompts user to be more specific in what to take, says the object has already been take, 
 * say that the object doesn't exist to take, or says item is successfully taken
 *  Gamestate is accordingly updated based on the situation 
 * @return 
 */
    public String execute() {
        if (itemName == null || itemName.trim().length() == 0) {
            return "Take what?\n";
        }
        try {
            Room currentRoom =
                    GameState.instance().getAdventurersCurrentRoom();
            Item theItem = currentRoom.getItemNamed(itemName);
            GameState.instance().addToInventory(theItem);
            currentRoom.remove(theItem);
            return itemName + " taken.\n";
        } catch (Item.NoItemException e) {
            // Check and see if we have this already. If no exception is
            // thrown from the line below, then we do.
            try {
                GameState.instance().getItemFromInventoryNamed(itemName);
                return "You already have the " + itemName + ".\n";
            } catch (Item.NoItemException e2) {
                return "There's no " + itemName + " here.\n";
            }
        }
    }
}
