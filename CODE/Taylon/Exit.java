import java.util.Scanner;

public class Exit {

    class NoExitException extends Exception {}

    private String dir;
    private Room src, dest;
    private boolean exitLocked;

    /**
     * Creates an exit.
     * @param dir direction in which movement is possible from src to dest
     * @param src starting room
     * @param dest destination room
     * @param exitLocked
     */
    Exit(String dir, Room src, Room dest, boolean exitLocked) {
        init();
        this.dir = dir;
        this.src = src;
        this.dest = dest;
        this.exitLocked = exitLocked;
        src.addExit(this);
    }

    /** Given a Scanner object positioned at the beginning of an "exit" file
     entry, read and return an Exit object representing it.
     @param d The dungeon that contains this exit (so that Room objects
     may be obtained.)
     @throws NoExitException The reader object is not positioned at the
     start of an exit entry. A side effect of this is the reader's cursor
     is now positioned one line past where it was.
     @throws Dungeon.IllegalDungeonFormatException A structural problem with the
     dungeon file itself, detected when trying to read this room.
     */
    Exit(Scanner s, Dungeon d) throws NoExitException,
            Dungeon.IllegalDungeonFormatException {

        init();
        String srcTitle = s.nextLine();
        if (srcTitle.equals(Dungeon.TOP_LEVEL_DELIM)) {
            throw new NoExitException();
        }
        src = d.getRoom(srcTitle);
        dir = s.nextLine();
        dest = d.getRoom(s.nextLine());
        String temp = s.nextLine();
        if (temp.equals("unlocked")) {
            exitLocked = false;
        }
        else {
            exitLocked = true;
        }

        // I'm an Exit object. Great. Add me as an exit to my source Room too,
        // though.
        src.addExit(this);

        // throw away delimiter
        if (!s.nextLine().equals(Dungeon.SECOND_LEVEL_DELIM)) {
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                    Dungeon.SECOND_LEVEL_DELIM + "' after exit.");
        }
    }
    
    /**
     * Reads through a file and creates the Exits that are laid out in the file.
     * @param s Scanner object to read through the file
     * @param d Dungeon that is being created
     * @param dir direction of Exit
     * @param src starting room of Exit
     * @param dest destination room of Exit
     * @param exitLocked locked or unlocked status of Exit
     */
    //Exit(Scanner s, Dungeon d, String dir, Room src, Room dest, boolean exitLocked);

    /**
     * Common object initialization tasks.
     */
    private void init() {
    }
    
    /**
     * Returns a String that explains this Exit.
     * @return a String that explains which direction you can move to the (dest)ination room
     */
    String describe() {
        return "You can go " + dir + " to " + dest.getTitle() + ".";
    }
    
    /**
     * Returns direction.
     * @return direction of movement that is possible
     */
    String getDir() { return dir; }
    
    /**
     * Return starting room.
     * @return room in which the exit is
     */
    Room getSrc() { return src; }
    
    /**
     * Return destination room.
     * @return room that the exit leads to by specified direction
     */
    Room getDest() { return dest; }
    
    /**
     * Returns if this Exit is locked or unlocked
     * @return the exitLocked field
     */
    boolean getExitLocked()
    {
        return this.exitLocked;
    }
   
    /**
     * Sets the exitLocked field to a new value (usually 'unlocked' which is 'false')
     * @param unlocked new value of exitLocked field
     */
    void setExitLocked(boolean unlocked)
    {
        this.exitLocked = unlocked;
    }
}