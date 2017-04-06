
/**
 * DropCommand extends the Command class and drops the given item from the inventory
 * 
 * @author Taylon Thorpe
 * @version(4/5/17)
 */
class DropCommand extends Command {

    private String itemName;

    /**
     * Constructs a DropCommand with the given itemName
     * @param itemName gives the name of the item to be dropped.
     */
    DropCommand(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Uses the itemName to get the item from the inventory and then drops it.
     * @return String of the itemName that was dropped.
     * @throws NoItemException
     */
    public String execute() {
        if (itemName == null || itemName.trim().length() == 0) {
            return "Drop what?\n";
        }
        try {
            Item theItem = GameState.instance().getItemFromInventoryNamed(
                    itemName);
            GameState.instance().removeFromInventory(theItem);
            GameState.instance().getAdventurersCurrentRoom().add(theItem);
            return itemName + " dropped.\n";
        } catch (Item.NoItemException e) {
            return "You don't have a " + itemName + ".\n";
        }
    }
}
