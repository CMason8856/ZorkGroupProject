/**
 * The Dungeon class handles everything to do with the overarching dungeon like
 * placing the rooms and exits. 
 * 
 * @author Taylon Thorpe
 * @version(4/5/17)
 */
import java.util.Hashtable;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
//Dungeon
public class Dungeon {

    public static class IllegalDungeonFormatException extends Exception {
        public IllegalDungeonFormatException(String e) {
            super(e);
        }
    }

    // Variables relating to both dungeon file and game state storage.
    public static String TOP_LEVEL_DELIM = "===";
    public static String SECOND_LEVEL_DELIM = "---";

    // Variables relating to dungeon file (.bork) storage.
    public static String ROOMS_MARKER = "Rooms:";
    public static String EXITS_MARKER = "Exits:";
    public static String ITEMS_MARKER = "Items:";
    public static String NPC_MARKER = "NPC:";

    // Variables relating to game state (.sav) storage.
    static String FILENAME_LEADER = "Dungeon file: ";
    static String ROOM_STATES_MARKER = "Room states:";

    private String name;
    private static Room entry;
    private static Hashtable<String,Room> rooms;
    private Hashtable<String,Item> items;
    private String filename;
    private ArrayList<NPC> people;

    Dungeon(String name, Room entry) {
        init();
        this.filename = null;    // null indicates not hydrated from file.
        this.name = name;
        this.entry = entry;
        rooms = new Hashtable<String,Room>();
    }

    /**
     * Read from the .bork filename passed, and instantiate a Dungeon object
     * based on it.
     * @param filename is the file's name which is passed in and used for the 
     * Dungeon.
     */
    public Dungeon(String filename) throws FileNotFoundException,
            IllegalDungeonFormatException {

        this(filename, true);
    }

    /**
     * Read from the .bork filename passed, and instantiate a Dungeon object
     * based on it, including (possibly) the items in their original locations.
     * @param filename is the file's name which is passed in and used for the 
     * Dungeon.
     * @param initState tells if the Dungeon should be set to it's initial state 
     * or not.
     */
    public Dungeon(String filename, boolean initState)
            throws FileNotFoundException, IllegalDungeonFormatException {
        System.out.println("");
        init();
        this.filename = filename;

        Scanner s = new Scanner(new FileReader(filename));
        name = s.nextLine();

        s.nextLine();   // Throw away version indicator.

        // Throw away delimiter.
        if (!s.nextLine().equals(TOP_LEVEL_DELIM)) {
            throw new IllegalDungeonFormatException("No '" +
                    TOP_LEVEL_DELIM + "' after version indicator.");
        }

        // Throw away Items starter.
        if (!s.nextLine().equals(ITEMS_MARKER)) {
            throw new IllegalDungeonFormatException("No '" +
                    ITEMS_MARKER + "' line where expected.");
        }
        System.out.println("...Initiating Items...");
        try {
            // Instantiate items.
            while (true) {
                add(new Item(s));
            }
        } catch (Item.NoItemException e) {  /* end of items */ }

        // Throw away Rooms starter.
        if (!s.nextLine().equals(ROOMS_MARKER)) {
            throw new IllegalDungeonFormatException("No '" +
                    ROOMS_MARKER + "' line where expected.");
        }
        System.out.println("...Initiating First Room...");
        try {
            // Instantiate and add first room (the entry).
            entry = new Room(s, this, initState, true);
            add(entry);

        System.out.println("...Initiating Rooms...");
            // Instantiate and add other rooms.
            while (true) {
                add(new Room(s, this, initState, true));
            }
        } catch (Room.NoRoomException e) {  /* end of rooms */ }
        // Throw away Exits starter.
        if (!s.nextLine().equals(EXITS_MARKER)) {
            throw new IllegalDungeonFormatException("No '" +
                    EXITS_MARKER + "' line where expected.");
        }
        System.out.println("...Initiating Exits...");
        try {
            // Instantiate exits.
            while (true) {
                Exit exit = new Exit(s, this);
            }
        } catch (Exit.NoExitException e) {  /* end of exits */ }
        
        if(!s.nextLine().equals(NPC_MARKER)) {
            throw new IllegalDungeonFormatException("No " + NPC_MARKER + " line where expected.");
        }
        System.out.println("...Initiating NPC's...");
       
        
        //System.out.println("S is: ");
        boolean loopTrue=true;
            //while(!s.nextLine().equals(SECOND_LEVEL_DELIM)){
            while(loopTrue){
                try{
		people.add(new NPC(s));
                s.nextLine();//throws away Delimiter '---' inbetween NPC's
                }catch(NPC.NoNpcException e){loopTrue=false;}
            }
        System.out.println("...Dungeon Initialization Complete...");
        //System.out.println(people.get(0).getLocation().getTitle());
        s.close();
    }

    // Common object initialization tasks, regardless of which constructor
    // is used.
    private void init() {
        rooms = new Hashtable<String,Room>();
        items = new Hashtable<String,Item>();
        people = new ArrayList<NPC>();
    }

    /**
     * Store the current (changeable) state of this dungeon to the writer
     * passed.
     * @param w identifies what PrintWriter object is being used.
     */
    void storeState(PrintWriter w) throws IOException {
        w.println(FILENAME_LEADER + getFilename());
        w.println(ROOM_STATES_MARKER);
        for (Room room : rooms.values()) {
            room.storeState(w);
        }
        w.println(TOP_LEVEL_DELIM);
        for(NPC npc:people){
        npc.storeState(w);
        }
        w.println(TOP_LEVEL_DELIM);
    }

    /**
     * Restore the (changeable) state of this dungeon to that reflected in the
     * reader passed.
     * @param s gives the Scanner object that will be used to restore the state
     * of the dungeon.
     */
    void restoreState(Scanner s) throws GameState.IllegalSaveFormatException {

        // Note: the filename has already been read at this point.

        if (!s.nextLine().equals(ROOM_STATES_MARKER)) {
            throw new GameState.IllegalSaveFormatException("No '" +
                    ROOM_STATES_MARKER + "' after dungeon filename in save file.");
        }

        String roomName = s.nextLine();
        while (!roomName.equals(TOP_LEVEL_DELIM)) {
            getRoom(roomName.substring(0,roomName.length()-1)).
                    restoreState(s, this);
            roomName = s.nextLine();
        }
        String NpcName=s.nextLine();//Throwing Away 'NPC:'
        NpcName=s.nextLine();//NPC: Name
        while(!NpcName.equals(TOP_LEVEL_DELIM)){
            for(NPC person:people){
                if(person.getName().equals(NpcName)){
                    person.restoreState(s);
                }
            }
            NpcName=s.nextLine();//Next NPC name OR ===
        }
        
    }

    public static Room getEntry() { return entry; }
    public String getName() { return name; }
    public String getFilename() { return filename; }
    public void add(Room room) { rooms.put(room.getTitle(),room); }
    public void add(Item item) { items.put(item.getPrimaryName(),item); }
    public void add(NPC npc) {npc.setLocation(getRoom("Stephen's office"));}

    public static Room getRoom(String roomTitle) {
        return rooms.get(roomTitle);
    }

    /**
     * Get the Item object whose primary name is passed. This has nothing to
     * do with where the Adventurer might be, or what's in his/her inventory,
     * etc.
     * @param primaryItemName is the item name of the item that is returned.
     * @return returns the item with the same passed in name.
     */
    public Item getItem(String primaryItemName) throws Item.NoItemException {

        if (items.get(primaryItemName) == null) {
            throw new Item.NoItemException();
        }
        return items.get(primaryItemName);
    }
	public Hashtable getRooms(){
        return rooms;
    }
        public ArrayList<NPC> getPeople(){
        return people;
    }
}