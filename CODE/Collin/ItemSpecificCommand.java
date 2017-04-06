/**
 * @author (Collin Mason)
 * @version (0.01)
 */

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

class ItemSpecificCommand extends Command {

    private String verb;
    private String noun;
    private GameState game = GameState.instance();
    private String secondEvent;
    private String firstThreeLetters2;

	/**
	 * @param verb, name of the action taken
	 * @param noun, name of the item acted upon
	 */
    ItemSpecificCommand(String verb, String noun) {
        this.verb = verb;
        this.noun = noun;
        secondEvent = "";
        firstThreeLetters2 = "";

    }

	/**
	 * @return returns the result of the action taken on the object
	 */
    public String execute() {

        Item itemReferredTo = null;
        Exit exitReferredTo = null;
        try {
            itemReferredTo = GameState.instance().getItemInVicinityNamed(noun);
        } catch (Item.NoItemException e) {
            return "There's no " + noun + " here.";
        }
        exitReferredTo = GameState.instance().getExitInVicinity();
        if (exitReferredTo == null) {
            System.out.println("There are no locked exits here!");
        }

        String msg = itemReferredTo.getMessageForVerb(verb);

        if (itemReferredTo.getEventsForVerb(verb) != null)//if there IS at least one event for this verb
        {
            ArrayList<String> arrayEvent = new ArrayList<>(itemReferredTo.getEventsForVerb(verb));

            for (int j = 0; j < arrayEvent.size(); j++) {
                String firstThreeLetters = arrayEvent.get(j).substring(0, 3);

                //8-tier if-else statement
                if (firstThreeLetters.equals("Unl") || firstThreeLetters.equals("[Un")) {
                    Event.unlock(exitReferredTo);
                }
                if (firstThreeLetters.equals("Die") || firstThreeLetters.equals("[Di")) {
                    Event.die(true);
                }
                if (firstThreeLetters.equals("Win") || firstThreeLetters.equals("[Wi")) {
                    Event.win(true);
                }
                if (firstThreeLetters.equals("Dis") || firstThreeLetters.equals("[Di")) {
                    Event.disappear(itemReferredTo);
                }
                if (firstThreeLetters.equals("[Tr") || firstThreeLetters.equals("Tra")) {
                    String effect = null;
                    ArrayList<String> effects = itemReferredTo.getEffect(verb);
                    for (int i = 0; i < effects.size(); i++) {
                        if (effects.get(i).contains("Tra")) {
                            effect = effects.get(i);
                        }
                    }
                    String[] newItem2 = effect.split("\\(");
                    String newItem1 = newItem2[1].substring(0, newItem2[1].length() - 1);
                    Item newItem;

                    try {
                        newItem = game.getDungeon().getItem(newItem1);
                    } catch (Item.NoItemException ex) {
                        newItem = null;
                    }
                    Event.transform(itemReferredTo, newItem);
                }
                if (firstThreeLetters.equals("Tel") || firstThreeLetters.equals("[Te")) {
                    Event.teleport();
                }
                if (firstThreeLetters.equals("Sco") || firstThreeLetters.equals("[Sc")) {
                    String effect = null;
                    ArrayList<String> effects = itemReferredTo.getEffect(verb);
                    for (int i = 0; i < effects.size(); i++) {
                        if (effects.get(i).contains("Sco")) {
                            effect = effects.get(i);
                        }
                    }

                    int sco = Integer.valueOf(effect.substring(effect.indexOf("(") + 1, effect.indexOf(")")));
                    Event.score(sco);
                }
                if (firstThreeLetters.equals("Wou") || firstThreeLetters.equals("[Wo")) {
                    String effect = null;
                    ArrayList<String> effects = itemReferredTo.getEffect(verb);
                    for (int i = 0; i < effects.size(); i++) {
                        if (effects.get(i).contains("Wou")) {
                            effect = effects.get(i);
                        }
                    }


                    int dam = Integer.valueOf(effect.substring(effect.indexOf("(") + 1, effect.indexOf(")")));
                    Event.wound(dam);
                }
                if (verb.equals("combat")) {
                    Player pc = new Player("You", GameState.instance().getHealth());
                    NPC enemy = new NPC(GameState.instance().getAdventurersCurrentRoom(), "Troll", true, 100);
                    Combat brawl = new Combat(pc, enemy);
                    while (true) {
                        Scanner input = new Scanner(System.in);
                        System.out.println("You have encountered a " + enemy.getName() + " in " + GameState.instance().getAdventurersCurrentRoom().getTitle() + "!");
                        System.out.println("Would you like to 'attack' the " + enemy.getName() + " or use an 'item'?");
                        String userAction = input.next();
                        if (userAction.toLowerCase().equals("attack")) {
                            brawl.playerAttack(enemy);
                        } else if (userAction.toLowerCase().equals("item")) {
                            System.out.println("Which item would you like to use?");
                            String userItem = input.next();
                            Item item = GameState.instance().getItemFromInventoryName(userItem);
                            brawl.playerUseItem(item);
                        }
                        else
                            System.out.println("Invalid entry. Enter 'attack' or 'item' next time!");

                        if(enemy.getHealth() <= 0)
                        {
                            System.out.println("Looks like you won! Congrats!");
                            break;
                        }
                        brawl.enemyTurn();
                        if(pc.getHealth() <= 0)
                        {
                            System.out.println("You have lost the battle and died...");
                            System.exit(0);

                        }
                    }
                    }
                }

                //call the method
                //do the same thing for the second event(if there is one)
            }

            return (msg == null
                    ? "Sorry, you can't " + verb + " the " + noun + "." : msg) + "\n";

        }

}