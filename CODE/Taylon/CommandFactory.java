import java.util.List;
import java.util.Arrays;

public class CommandFactory {

    private static CommandFactory theInstance;
    public static List<String> MOVEMENT_COMMANDS =
            Arrays.asList("n","w","e","s","u","d" );

    public static synchronized CommandFactory instance() {
        if (theInstance == null) {
            theInstance = new CommandFactory();
        }
        return theInstance;
    }
    
    /**
     * Creates the one and only instance of CommandFactory
     */
    private CommandFactory() {
    }

    /**
     * Parses through the user input and returns the appropriate command.
     * @param command  user input
     * @return  one of the subclasses of Command that is appropriate based on user input
     */
    public Command parse(String command) {
        String parts[] = command.split(" ");
        String verb = parts[0];
        String noun = parts.length >= 2 ? parts[1] : "";
        if (verb.equals("health")) {
            return new HealthCommand();
        }
        if (verb.equals("score")) {
            return new ScoreCommand();
        }
        if (verb.equals("save")) {
            return new SaveCommand(noun);
        }
        if (verb.equals("take")) {
            return new TakeCommand(noun);
        }
        if (verb.equals("drop")) {
            return new DropCommand(noun);
        }
        if (verb.equals("i") || verb.equals("inventory")) {
            return new InventoryCommand();
        }
        if (MOVEMENT_COMMANDS.contains(verb)) {
            return new MovementCommand(verb);
        }
        if (parts.length == 2) {
            return new ItemSpecificCommand(verb, noun);
        }
        return new UnknownCommand(command);
    }
}
