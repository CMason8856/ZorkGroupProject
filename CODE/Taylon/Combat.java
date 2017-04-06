/**
 * Contains Constructors and Methods for Objects of type Combat
 *
 * @author(Collin Mason)
 * @version(11/9/2016)
 */

import java.util.Random;
import java.util.Scanner;

class Combat{
    GameState stateOfTheGame = GameState.instance();
    private static Random randint = new Random();
    private Player pc;
    private NPC enemy;
    //private ArrayList<Item> inventory = stateOfTheGame.inventory;
    /**
     * Construct Combat Objects
     * @param pc, defines the status of the Player Object at the start of Combat
     * @param enemy, defines the NPC Object at the start of Combat
     */
    Combat(Player pc, NPC enemy){
        this.pc = pc;
        this.enemy = enemy;
    }

    /**
     * @param enemy, identifies the NPC Object that will be hit
     * @return, returns the deamage dealt to the enemy to the caller
     */
    int playerAttack(NPC enemy){
        int dam = randint.nextInt(64)+1;
        int enemyHealth = enemy.getHealth() -  dam;
        return enemyHealth;
    }

    /**
     * @param pc, identifies the Player Object that will be hit
     * @return, returns the damage dealt to the player to the caller
     */
    private static int enemyAttack(Player pc){
        int dam = randint.nextInt(64)+1;
        int playerHealth = pc.getHealth() - dam;
        return playerHealth;
    }

    /**
     * @param invitem, identifies the Item Object that the player is using in Combat
     * @return, returns the numerical effect of the item (i.e. could heal you or hurt the enemy) to the caller
     */
    ItemSpecificCommand playerUseItem(Item invitem){
        stateOfTheGame.instance().getInventoryNames();
        Scanner input = new Scanner(System.in);
        System.out.println("Whhich item would you like to use and what would you like to do with it? ex: 'detonate bomb'");
        String userItem = input.nextLine();
        String[] parseInput = userItem.split(" ");
        return new ItemSpecificCommand(parseInput[0], parseInput[1]);
        }

    /**
     * @return, returns a String representation of the NPC Object's turn to the caller
     */
    String enemyTurn(){return "The Enemy attacked you for " + String.valueOf(enemyAttack(pc)) + ".";}
}