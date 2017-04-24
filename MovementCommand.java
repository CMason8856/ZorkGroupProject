/**
 * Manages the player's allowed movements
 *
 * @author (Collin Mason)
 * @version (0.01)
 */

class MovementCommand extends Command {

    private String dir;
    private GameState game=GameState.instance();

    /**
     * This command is created when the user input is a direction the users to desires to move.
     * @param dir  direction that the user has entered
     */
    MovementCommand(String dir) {
        this.dir = dir;
    }

    /**
     * Finds the adventurer's current room and checks to see if they can leave current room in desired direction.
     * If movement is possible, adventurer's room is changed. Returns accordingly.
     * @return either new Room description (if movement was possible) or a message saying movement is not possible in desired direction.
     */
    public String execute() {
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        Room nextRoom = currentRoom.leaveBy(dir);
        
        if(nextRoom==null){
            for(Exit e:currentRoom.getExits()){
            e.describe();
            }
            return "You can't go " + dir + ".\n";
        }

        //Boolean exitLocked = GameState.instance().getExitInVicinity().getExitLocked();
        Boolean exitLocked = GameState.instance().getExitLockedInDirection(currentRoom, dir);
        Boolean isLight = nextRoom.getLight();

        if (nextRoom != null && GameState.instance().getExitLockedInDirection(currentRoom,dir) == false && isLight == true) {  // could try/catch here.
            GameState.instance().setAdventurersCurrentRoom(nextRoom);
            return "\n" + nextRoom.describe() + "\n";
        }

        else if (nextRoom != null && GameState.instance().getExitLockedInDirection(currentRoom,dir) == false && isLight == false) {
            GameState.instance().setAdventurersCurrentRoom(nextRoom);
            
            try{
                GameState.instance().getItemFromInventoryNamed("Flashlight");
                nextRoom.setLight(true);
                isLight=nextRoom.getLight();
                game.setAdventurersCurrentRoom(game.getAdventurersCurrentRoom().battle());
                nextRoom=game.getAdventurersCurrentRoom();
                return "\n"+nextRoom.describe()+"\n";
            }catch(Exception ex){}
            return "\nThis room is currently dark, you can't see a thing!\n";
        }

        else if (exitLocked == true && nextRoom != null) {
            return "This exit is locked!" + "\n";
        }
        else {
            System.out.println(""+ exitLocked);
            return "You can't go " + dir + ".\n";
        }
    }
}
