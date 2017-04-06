import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
/**
 *
 * @author Alfredo Soto
 * Class creates a Room and creates methods used by Room
 */
public class Room {

    class NoRoomException extends Exception {}

    static String CONTENTS_STARTER = "Contents: ";

    private String title;
    private String desc;
    private boolean beenHere;
    private ArrayList<Item> contents;
    private ArrayList<Exit> exits;
    private boolean light; //True means lit false means dark
    /**
     * Room Constructor
     * @param title name of room
     */
    Room(String title) {
        init();
        this.title = title;
    }

    /**
     *method to return list of exits
     * @return arraylist of exits
     */
    public ArrayList getExits()
    {
        return exits;
    }
    /**
     * Room Constructor
     * @param s scanner object
     * @param d Dungeon object,
     */
    Room(Scanner s, Dungeon d) throws NoRoomException,
            Dungeon.IllegalDungeonFormatException {

        this(s, d, true, true);
    }

    /** Given a Scanner object positioned at the beginning of a "room" file
     entry, read and return a Room object representing it.
     @param d The containing Dungeon object,
     necessary to retrieve other objects.
     @param initState should items listed for this room be added to it?
     @throws NoRoomException The reader object is not positioned at the
     start of a room entry. A side effect of this is the reader's cursor
     is now positioned one line past where it was.
     @throws Dungeon.IllegalDungeonFormatException A structural problem with the
     dungeon file itself, detected when trying to read this room.
     */
    Room(Scanner s, Dungeon d, boolean initState, boolean light) throws NoRoomException,
            Dungeon.IllegalDungeonFormatException {

        init();
        title = s.nextLine();
        desc = "";
        if (title.equals(Dungeon.TOP_LEVEL_DELIM)) {
            throw new NoRoomException();
        }

        String roomLight = s.nextLine();
        System.out.println(roomLight);

        if (roomLight.equals("light")) {
            this.light = true;
        }

        if (roomLight.equals("dark")) {
            this.light = false;
        }

        String lineOfDesc = s.nextLine();
        while (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM) &&
                !lineOfDesc.equals(Dungeon.TOP_LEVEL_DELIM)) {

            if (lineOfDesc.startsWith(CONTENTS_STARTER)) {
                String itemsList = lineOfDesc.substring(CONTENTS_STARTER.length());
                String[] itemNames = itemsList.split(",");
                for (String itemName : itemNames) {
                    try {
                        if (initState) {
                            add(d.getItem(itemName));
                        }
                    } catch (Item.NoItemException e) {
                        throw new Dungeon.IllegalDungeonFormatException(
                                "No such item '" + itemName + "'");
                    }
                }
            } else {
                desc += lineOfDesc + "\n";
            }
            lineOfDesc = s.nextLine();
        }

        // throw away delimiter
        if (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM)) {
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                    Dungeon.SECOND_LEVEL_DELIM + "' after room.");
        }
    }

    /**
     * Empties out contents and exits arrays, and changes beenHere to false
     */ 
    private void init() {
        contents = new ArrayList<Item>();
        exits = new ArrayList<Exit>();
        beenHere = false;
    }

    /**
     * returns title of room
     * @return title 
     */
    String getTitle() { return title; }
     
    /**
     * returns if there is light in the room
     * @return light 
     */
    Boolean getLight() { return this.light;}
    
    /**
     * changes light parameter
     * 
     */
    void setLight(Boolean light) {
        this.light = light;
    }

    /**
     * changes room description
     * 
     */
    void setDesc(String desc) { this.desc = desc; }

    /**
     * Store the current (changeable) state of this room to the writer
     * passed.
     * @param w printwriter
     */
    void storeState(PrintWriter w) throws IOException {
        w.println(title + ":");
        w.println("beenHere=" + beenHere);
        if (contents.size() > 0) {
            w.print(CONTENTS_STARTER);
            for (int i=0; i<contents.size()-1; i++) {
                w.print(contents.get(i).getPrimaryName() + ",");
            }
            w.println(contents.get(contents.size()-1).getPrimaryName());
        }
        w.println(Dungeon.SECOND_LEVEL_DELIM);
    }
    /**
     * Restores state from .sav file
     * @param s scanner
     * @param d dungeon
     * 
     */
    void restoreState(Scanner s, Dungeon d) throws
            GameState.IllegalSaveFormatException {

        String line = s.nextLine();
        if (!line.startsWith("beenHere")) {
            throw new GameState.IllegalSaveFormatException("No beenHere.");
        }
        beenHere = Boolean.valueOf(line.substring(line.indexOf("=")+1));

        line = s.nextLine();
        if (line.startsWith(CONTENTS_STARTER)) {
            String itemsList = line.substring(CONTENTS_STARTER.length());
            String[] itemNames = itemsList.split(",");
            for (String itemName : itemNames) {
                try {
                    add(d.getItem(itemName));
                } catch (Item.NoItemException e) {
                    throw new GameState.IllegalSaveFormatException(
                            "No such item '" + itemName + "'");
                }
            }
            s.nextLine();  // Consume "---".
        }
    }

    /**
     * prints description of room
     * 
     * @return description 
     */
    public String describe() {
        String description;
        if (beenHere) {
            description = title;
        } else {
            description = title + "\n" + desc;
        }
        for (Item item : contents) {
            description += "\nThere is a " + item.getPrimaryName() + " here.";
        }
        if (contents.size() > 0) { description += "\n"; }
        if (!beenHere) {
            for (Exit exit : exits) {
                description += "\n" + exit.describe();
            }
        }
        beenHere = true;
        return description;
    }

    /**
     * method to check if direction has a valid exit
     * @param dir direction
     * @return an exit object
     * 
     */
    public Room leaveBy(String dir) {
        for (Exit exit : exits) {
            if (exit.getDir().equals(dir)) {
                return exit.getDest();
            }
        }
        return null;
    }
    /**
     * adds an exit to the room
     * @param exit exit to be added
     */
    void addExit(Exit exit) {
        exits.add(exit);
    }

    /**
     * adds an item to the room
     * @param item to be added
     * 
     */
    void add(Item item) {
        contents.add(item);
    }

    /**
     * removes an item in the room
     * @param item item to be removed
     */    
    void remove(Item item) {
        contents.remove(item);
    }

    /**
     * returns an item named 'name'
     * @param name name of item
     * @return item named name
     * 
     */
    Item getItemNamed(String name) throws Item.NoItemException {
        for (Item item : contents) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        throw new Item.NoItemException();
    }
    /**
     * returns the Arraylist of items
     * @return Arraylist<item>
     * 
     */
    ArrayList<Item> getContents() {
        return contents;
    }
}