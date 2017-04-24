/**
 * (Descrition)
 *
 * @author (Collin Mason)
 * @version (0.01)
 */

import java.util.ArrayList;

class InventoryCommand extends Command {

	/**
	 * Empty constructor for InventoryCommand
	 */
    InventoryCommand() {}

	/**
	 * @return returns the player's inventory
	 */
    public String execute() {
        ArrayList<String> names = GameState.instance().getInventoryNames();
        if (names.size() == 0) {
            return "You are empty-handed.\n";
        }
        String retval = "You are carrying:\n";
        for (String itemName : names) {
            retval += "   A " + itemName + "\n";
        }
        return retval;
    }
}
