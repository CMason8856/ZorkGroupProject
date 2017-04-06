/**
 * Interpreter to relay between the Dungeon and the Player
 *
 * @author(Collin Mason)
 * @version(0.01)
 */

import java.util.Scanner;


public class Interpreter {

    private static GameState state; // not strictly necessary; GameState is 
    // singleton

    public static String USAGE_MSG =
            "Usage: Interpreter borkFile.bork|saveFile.sav.";

	/**
	 * @param args, contains the name of the .bork|sav file to the read in
	 * @var command, holds the value of the player inputed command
	 * @var commandLine, Scanner object to pass commands into the dungeon
	 */
    public static void main(String args[]) {

        if (args.length < 1) {
            System.err.println(USAGE_MSG);
            System.exit(1);
        }

        String command;
        Scanner commandLine = new Scanner(System.in);

        try {
            state = GameState.instance();
            if (args[0].endsWith(".bork")) {
                state.initialize(new Dungeon(args[0]));
                System.out.println("\nWelcome to " +
                        state.getDungeon().getName() + "!");
            } else if (args[0].endsWith(".sav")) {
                state.restore(args[0]);
                System.out.println("\nWelcome back to " +
                        state.getDungeon().getName() + "!");
            } else {
                System.err.println(USAGE_MSG);
                System.exit(2);
            }

            System.out.print("\n" +
                    state.getAdventurersCurrentRoom().describe() + "\n");

            command = promptUser(commandLine);

            while (!command.equals("q")) {

                System.out.print(
                        CommandFactory.instance().parse(command).execute());

                command = promptUser(commandLine);
            }

            System.out.println("Bye!");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * @param commandLine, contains the command value inputed by the player
	 * @return commandLine.nextLine(), returns an empty commandLine object
	 */
    private static String promptUser(Scanner commandLine) {

        System.out.print("> ");
        return commandLine.nextLine();
    }

}