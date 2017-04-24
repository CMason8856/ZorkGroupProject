/**
 * Singleton class that makes sure there is only one instance of the dungeon running at a time
 *
 * @author(Collin)
 * @version(0.01)
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class GameState {
	/**
	 * Exception thrown when the format of a is incorrect
	 */
    public static class IllegalSaveFormatException extends Exception {
		/**
		 * @param e, contains the String statement that is printed when the exception is thrown
		 */
        public IllegalSaveFormatException(String e) {
            super(e);
        }
    }

    static String DEFAULT_SAVE_FILE = "bork_save";
    static String SAVE_FILE_EXTENSION = ".sav";
    static String SAVE_FILE_VERSION = "Bork v3.0 save data";

    static String ADVENTURER_MARKER = "Adventurer:";
    static String CURRENT_ROOM_LEADER = "Current room: ";
    static String INVENTORY_LEADER = "Inventory: ";


    public static GameState theInstance;
    private Dungeon dungeon;
    private static ArrayList<Item> inventory;
    private static Room adventurersCurrentRoom;
    
    private static int score = 0;
    private static int health = 50;

	/**
	 * Synchronized method that prevents more than one instance of GameState from being created
	 * @return, returns theInstance variable to the caller after the method determines if one exists already or needed to be created
	 */
    static synchronized GameState instance() {
        if (theInstance == null) {
            theInstance = new GameState();
        }
        return theInstance;
    }
	
	/**
	 * Constructor for GameState
	 * @var inventory, instantiates the inventory ArrayList of Items
	 */
    private GameState() {
        inventory = new ArrayList<Item>();
    }
	
	/**
	 * Loads a saved file into the game
	 * @param filename, contains the name of the .sav file to be loaded
	 * @var s, Scanner object that reads the .sav file into the game
	 */
    void restore(String filename) throws FileNotFoundException,
            IllegalSaveFormatException, Dungeon.IllegalDungeonFormatException {

        Scanner s = new Scanner(new FileReader(filename));

        if (!s.nextLine().equals(SAVE_FILE_VERSION)) {
            throw new IllegalSaveFormatException("Save file not compatible.");
        }

        String dungeonFileLine = s.nextLine();

        if (!dungeonFileLine.startsWith(Dungeon.FILENAME_LEADER)) {
            throw new IllegalSaveFormatException("No '" +
                    Dungeon.FILENAME_LEADER +
                    "' after version indicator.");
        }

        dungeon = new Dungeon(dungeonFileLine.substring(
                Dungeon.FILENAME_LEADER.length()), false);
        dungeon.restoreState(s);

        s.nextLine();  // Throw away "Adventurer:".
        String currentRoomLine = s.nextLine();
        adventurersCurrentRoom = dungeon.getRoom(
                currentRoomLine.substring(CURRENT_ROOM_LEADER.length()));
        if (s.hasNext()) {
            String inventoryList = s.nextLine().substring(
                    INVENTORY_LEADER.length());
            String[] inventoryItems = inventoryList.split(",");
            for (String itemName : inventoryItems) {
                try {
                    addToInventory(dungeon.getItem(itemName));
                } catch (Item.NoItemException e) {
                    throw new IllegalSaveFormatException("No such item '" +
                            itemName + "'");
                }
            }
        }
    }

	/**
	 * Saves game in .sav file
	 */
    void store() throws IOException {
        store(DEFAULT_SAVE_FILE);
    }

	/**
	 * Saves game in .sav file
	 * @param savename, name of the .sav file
	 */
    void store(String saveName) throws IOException {
        String filename = saveName + SAVE_FILE_EXTENSION;
        PrintWriter w = new PrintWriter(new FileWriter(filename));
        w.println(SAVE_FILE_VERSION);
        dungeon.storeState(w);
        w.println(ADVENTURER_MARKER);
        w.println(CURRENT_ROOM_LEADER + adventurersCurrentRoom.getTitle());
        if (inventory.size() > 0) {
            w.print(INVENTORY_LEADER);
            for (int i=0; i<inventory.size()-1; i++) {
                w.print(inventory.get(i).getPrimaryName() + ",");
            }
            w.println(inventory.get(inventory.size()-1).getPrimaryName());
        }
        w.close();
    }

	/**
	 * Initializes Dungeon and places adventurer in the Current Room
	 */
    void initialize(Dungeon dungeon) {
        this.dungeon = dungeon;
        adventurersCurrentRoom = dungeon.getEntry();
    }

	/**
	 * @return names, returns the names of the items in the player's inventory
	 */
    ArrayList<String> getInventoryNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (Item item : inventory) {
            names.add(item.getPrimaryName());
        }
        return names;
    }

	/**
	 * @param item, identifies the item that is added to the inventory list
	 */
    void addToInventory(Item item) /* throws TooHeavyException */ {
        inventory.add(item);
    }

	/**
	 * @param item, identifies the item to be removed from the inventory list
	 */
    static void removeFromInventory(Item item) {
        inventory.remove(item);
    }

	/**
	 * @param item, identifies the itme that is to be added to the inventory
	 */
    static void addItemToIventory(Item item) { inventory.add(item);}

	/**
	 * @param name, contains the name of the item present in the room
	 */
    Item getItemInVicinityNamed(String name) throws Item.NoItemException {

        // First, check inventory.
        for (Item item : inventory) {
            if (item.goesBy(name)) {
                return item;
            }
        }

        // Next, check room contents.
        for (Item item : adventurersCurrentRoom.getContents()) {
            if (item.goesBy(name)) {
                return item;
            }
        }

        throw new Item.NoItemException();
    }

	/**
	 * @return exits.get(0), returns the exits in the room
	 */
    Exit getExitInVicinity() {
        ArrayList<Exit>exits = adventurersCurrentRoom.getExits();
        for(Exit i: exits)
            if(i.getExitLocked()==true) {
                return i;
            }

            return exits.get(0);

    }

	/**
	 * @return true, is returned if the exit is locked
	 * @return false, is returned is the exit is not locked
	 */
    Boolean getExitLockedInVicinity() {
        ArrayList<Exit>exits = adventurersCurrentRoom.getExits();
        for (Exit i: exits) {
            if (i.getExitLocked()==true) {
                return true;
            }

        }
        return false;
    }

	/**
	 * @return true, is returned if there is light in the room
	 * @return false, is returned if there is not light in the room
	 */
    Boolean getRoomLight() {
        Room room = adventurersCurrentRoom;
        if (room.getLight() == true) {
            return true;
        }

        return false;
    }

	/**
	 * @param name, contains the name of an item
	 * @return item, returns the named item
	 */
    Item getItemFromInventoryNamed(String name) throws Item.NoItemException {

        for (Item item : inventory) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        throw new Item.NoItemException();
    }

	/**
	 * @param name, contains the name of an item
	 * @return item, returns the named item
	 * @return null, returns nothing
	 */
    Item getItemFromInventoryName(String name) {
        for (Item item : inventory) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        return null;
    }

	/**
	 * @return adventurersCurrentRoom, returns the room that the adventurer is in
	 */
    Room getAdventurersCurrentRoom() {
        return adventurersCurrentRoom;
    }

	/**
	 * @param room, contains the value of a room
	 */
    public static void setAdventurersCurrentRoom(Room room) {
        adventurersCurrentRoom = room;
    }

	/**
	 * @return dungeon, returns the dungeon value
	 */
    Dungeon getDungeon() {
        return dungeon;
    }
    
	/**
	 * @return score, returns the player's score
	 */
    public static int getScore()
    {
        return score;
    }
    
	/**
	 * @param s, holds the value of the player's score
	 */
    public static void setScore(int s)
    {
        score = s;
    }
    
	/**
	 * @param s, holds the value to be added to the player's score
	 */
    public static void addScore(int s)
    {
        score = score + s;
    }
    
	/**
	 * @return health, returns the player's health
	 */
    public static int getHealth()
    {
        return health;
    }
	
    /**
	 * @param h, holds the value fo the player's health
	 */
    public static void setHealth(int h)
    {
        health = h;
    }
    
	/**
	 * @param h, holds the value to be taken from the player's health
	 */
    public static void addHealth(int h)
    {
        health = health - h;
    }

	/**
	 * @param exit, holds the value of the to be unlocked
	 */
    public static void unlockExit(Exit exit) {
        exit.setExitLocked(false);
    }

	/**
	 * @param room, holds the value of the room to be lit
	 */
    public static void lightRoom(Room room) {room.setLight(true);}


    
}