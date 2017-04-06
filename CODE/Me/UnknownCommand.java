

/**
 * @author Alfredo
 * 
 * class to handle unknown commands from user
 */

class UnknownCommand extends Command {

    private String bogusCommand;

    /**
     * This command is created if the user input does not match any of the preset commands..
     * @param bogusCommand  strange user input
     */
    UnknownCommand(String bogusCommand) {
        this.bogusCommand = bogusCommand;
    }

    /**
     * Returns a String that explains the program doesn't know what to do with said command
     * @return  a String that explains the program doesn't know what to with said command
     */
    String execute() {
        return "I'm not sure what you mean by \"" + bogusCommand + "\".\n";
    }
}
