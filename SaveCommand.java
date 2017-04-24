
/**
 * 
 * @author Alfredo
 * class to handle save commands form the user
 */
class SaveCommand extends Command {

    private static String DEFAULT_SAVE_FILENAME = "bork";

    private String saveFilename;

/**
 * Constructor for command
 * @param saveFilename file to save to, user doesn't specify filename, it will use a default filename 
 */
    SaveCommand(String saveFilename) {
        if (saveFilename == null || saveFilename.length() == 0) {
            this.saveFilename = DEFAULT_SAVE_FILENAME;
        } else {
            this.saveFilename = saveFilename;
        }
    }

/**
 * calls gamestate to save and prints if save was successful or not
 * @return string confirming failure or success
 */
    public String execute() {
        try {
            GameState.instance().store(saveFilename);
            return "Data saved to " + saveFilename +
                    GameState.SAVE_FILE_EXTENSION + ".\n";
        } catch (Exception e) {
            System.err.println("Couldn't save!");
            e.printStackTrace();
            return "";
        }
    }
}