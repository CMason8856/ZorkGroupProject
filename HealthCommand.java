/**
 * Determines what the console prints when the health command is called
 *
 * @author (Collin Mason)
 * @version (0.01)
 */

class HealthCommand extends Command{
    private int health = GameState.instance().getHealth();

	/**
	 * Empty construct for HealthCommand
	 */
    HealthCommand(){}
	
	/**
	 * @return returns the String statement determined by where the player's health falls within a certain range
	 */
    public String execute(){
        if(health >= 90 && health <= 100){
            return "Your health is in magnificant shape.\n";
        }
        else if(health >= 80 && health < 90){
            return "Your health is in good shape.\n";
        }
        else if(health >= 70 && health < 80){
            return "Your health is in passable condition.\n May want to look into getting a little pick-me-up.\n";
        }
        else if(health >= 60 && health < 70){
            return "You are beaten and bruised. Seek aid when possible.\n";
        }
        else if(health >= 50 && health < 60){
            return "You are in a bad state of health. Seek aid ASAP.\n";
        }
        else if(health < 50){
            return "You are in critical condition. Seek immediate aid.\n";
        }
        return null;
    }
}