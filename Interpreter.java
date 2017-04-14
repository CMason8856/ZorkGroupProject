import java.util.Scanner;


public class Interpreter {

    private static GameState state; // not strictly necessary; GameState is 
    // singleton

    public static String USAGE_MSG =
            "Usage: Interpreter borkFile.bork|saveFile.sav.";

    public static void main(String args[]) {
        System.out.println("Enter a file: ");
        String file = new Scanner(System.in).nextLine();
        /**
        if (args.length < 1) {
            System.err.println(USAGE_MSG);
            System.exit(1);
        }
        */
        String command;
        Scanner commandLine = new Scanner(System.in);

        try {
            state = GameState.instance();
            if (file.endsWith(".bork")) {
                state.initialize(new Dungeon(file));
                System.out.println("\nWelcome to " +
                        state.getDungeon().getName() + "!");
            } else if (file.endsWith(".sav")) {
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

    private static String promptUser(Scanner commandLine) {

        System.out.print("> ");
        return commandLine.nextLine();
    }

}